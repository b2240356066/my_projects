`timescale 1s/1ms

module dead_hand(
    input  wire       clk,
    input  wire       reset,
    input  wire [1:0] threat_level,
    input  wire       diplomatic_override,
    input  wire       comms_lost,
    input  wire       system_fault,

    output reg        armed_out,
    output reg        tracking_out,
    output reg        authorization_out,
    output reg        override_ignored,
    output reg [2:0]  main_state_out,
    output reg [1:0]  sub_state_out,
    output reg [31:0] timer_out
);

// Main FSM States
localparam PEACE        = 3'b000; // Normal monitoring
localparam ALERT        = 3'b001; // Suspicious activity
localparam MOBILIZATION = 3'b010; // Forces preparing
localparam ENGAGEMENT   = 3'b011; // Activeplanning (Sub-FSM active)
localparam GLOBAL_WAR   = 3'b101; // Full-scale war (final)
localparam DEADLOCK     = 3'b110; // Point-of-No-Return (final)

// Sub-FSM States
localparam ARM       = 2'b00; // Forces armed
localparam TRACK     = 2'b01; // Target tracking
localparam AUTHORIZE = 2'b10; // Launchauthorization
localparam ABORT     = 2'b11; // De-escalation

reg [31:0] main_timer; // Main FSM timer
reg [31:0] sub_timer; // Sub-FSM timer

always @(posedge clk) begin

    // R1: Reset (reset=1 → PEACE, Sub-FSM=ABORT, timers cleared)
    if (reset) 
    begin
        main_state_out  <= PEACE;
        sub_state_out   <= ABORT;
        main_timer      <= 0;
        sub_timer       <= 0;
        override_ignored<= 0;
    end

    // R2: System fault escalation (system_fault=1 in non-final state → GLOBAL_WAR)
    else if (system_fault && main_state_out != GLOBAL_WAR && main_state_out != DEADLOCK) 
    begin
        main_state_out <= GLOBAL_WAR;
    end

    else 
    begin

        // Late Override Rule (in DEADLOCK or GLOBAL_WAR, or after 2 seconds in AUTHORIZE)
        if (diplomatic_override) 
        begin
            if (main_state_out == DEADLOCK || main_state_out == GLOBAL_WAR) 
            begin
                override_ignored <= 1'b1;
            end
            else if (main_state_out == ENGAGEMENT && sub_state_out == AUTHORIZE && sub_timer >= 2) 
            begin
                override_ignored <= 1'b1;
            end
        end

        // Main FSM
        case (main_state_out)

            PEACE: 
            begin

                sub_state_out <= ABORT;
                sub_timer    <= 0;

                if (threat_level >= 2'b01) 
                begin
                    if (main_timer == 4) // threat_level ≥ 01 for 5s Transition to ALERT
                    begin
                        main_state_out <= ALERT;
                        main_timer <= 0;
                    end 
                    else
                        main_timer <= main_timer + 1;
                end 
                else
                    main_timer <= 0;
            end

            ALERT: 
            begin

                sub_state_out <= ABORT;
                sub_timer    <= 0;

                if (threat_level >= 2'b10) 
                begin
                    if (main_timer == 9) // threat_level ≥ 10 for 10s Transition to MOBILIZATION
                    begin
                        main_state_out <= MOBILIZATION;
                        main_timer <= 0;
                    end 
                    else
                        main_timer <= main_timer + 1;
                end
                else if (threat_level == 2'b00) // threat_level = 00 for 4s Return to PEACE
                begin
                    if (main_timer == 3) 
                    begin
                        main_state_out <= PEACE;
                        main_timer <= 0;
                    end 
                    else
                        main_timer <= main_timer + 1;
                end
                else
                    main_timer <= 0;
            end

            MOBILIZATION: 
            begin

                sub_state_out <= ABORT;
                sub_timer    <= 0;

                if (comms_lost || threat_level == 2'b11) // threat_level = 11 or comms_lost = 1 Transition to ENGAGEMENT
                begin
                    main_state_out <= ENGAGEMENT;
                    sub_state_out  <= ARM;
                    main_timer     <= 0;
                    sub_timer      <= 0;
                end
                else if (threat_level <= 2'b01) // threat_level ≤ 01 for 4s De-escalation to ALERT
                begin
                    if (main_timer == 3) 
                    begin
                        main_state_out <= ALERT;
                        main_timer <= 0;
                    end else
                        main_timer <= main_timer + 1;
                end
                else
                    main_timer <= 0;
            end


            ENGAGEMENT: 
            begin

                // S4: Any Sub-State → ABORT:Ifdiplomatic_override=1 while Main FSM is not in DEADLOCK and not in GLOBAL_WAR.
                if (diplomatic_override && main_state_out != DEADLOCK && main_state_out != GLOBAL_WAR && 
                !(sub_state_out == AUTHORIZE && sub_timer >= 2)) 
                begin
                    sub_state_out <= ABORT;
                    sub_timer <= 0;
                end
                else 
                begin
                    case (sub_state_out)
                    // S1: Entry to ENGAGEMENT: Sub-FSM starts at ARM, sub-timer resets to 0

                        ARM: 
                        begin
                            if (sub_timer == 4) // S2: ARM → TRACK:After 4 seconds in ARM, if no diplomatic override occurs.
                            begin
                                sub_state_out <= TRACK;
                                sub_timer <= 0;
                            end 
                            else
                                sub_timer <= sub_timer + 1;
                        end

                        TRACK: 
                        begin
                            if (sub_timer == 6) // S3: TRACK → AUTHORIZE: After 6 seconds in TRACK, if no diplomatic override occurs.
                            begin
                                sub_state_out <= AUTHORIZE;
                                sub_timer <= 0;
                            end 
                            else
                                sub_timer <= sub_timer + 1;
                        end

                        AUTHORIZE: 
                        begin
                            if (sub_timer == 3) // S5: AUTHORIZE hold: If Sub-FSM stays in AUTHORIZE for 3 seconds, Main FSM transitions to DEADLOCK.
                            begin
                                main_state_out <= DEADLOCK;
                                sub_timer <= 0;
                            end 
                            else
                                sub_timer <= sub_timer + 1;
                        end

                        ABORT: 
                        begin
                            if (sub_timer == 5) // S6: ABORT hold: If Sub-FSM stays in ABORT for 5 seconds, Main FSM transitions back to MOBILIZATION.
                            begin
                                main_state_out <= MOBILIZATION;
                                sub_timer <= 0;
                                main_timer <= 0;
                            end 
                            else
                                sub_timer <= sub_timer + 1;
                        end
                    endcase
                end
            end

            //DEADLOCK and GLOBAL_WAR are irreversible
            GLOBAL_WAR, DEADLOCK: 
            begin
                main_state_out <= main_state_out;
                sub_state_out  <= sub_state_out;
                main_timer     <= main_timer;
                sub_timer      <= sub_timer;
            end

            default: main_state_out <= PEACE;
        endcase
    end
end

// Output
always @(*) 
begin
    timer_out = (main_state_out == ENGAGEMENT) ? sub_timer : main_timer;

    armed_out = (main_state_out == ENGAGEMENT && sub_state_out == ARM);

    tracking_out = (main_state_out == ENGAGEMENT && sub_state_out == TRACK);

    authorization_out = (main_state_out == ENGAGEMENT && sub_state_out == AUTHORIZE);
end

endmodule