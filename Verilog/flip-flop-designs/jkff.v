module jkff (
    input J,      // Data input
    input K,      // Data input
    input CLK,    // Clock input
    input RESET,  // Asynchronous reset, active high
    output reg Q  // Output
);
    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

    always @(posedge CLK or posedge RESET) begin
        
        if (RESET) begin
            Q <= 1'b0;
        end
        
        // This is the synchronous operation, which occurs on the CLK edge
        // only if RESET is not active (i.e., when RESET is low or the
        // edge of RESET has passed).
        else begin
            case ({J, K})
                2'b00: begin
                    // No change
                    Q <= Q;
                end
                2'b01: begin
                    // Reset
                    Q <= 1'b0;
                end
                2'b10: begin
                    // Set
                    Q <= 1'b1;
                end
                2'b11: begin
                    // Toggle
                    Q <= ~Q;
                end
            endcase
        end
    end
    
endmodule