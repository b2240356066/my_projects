`timescale 1ns / 1ps

module machine_d_tb;
    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

reg x;
reg CLK;
reg RESET;
wire F;
wire [2:0] S;

parameter Time = 10;

machine_d MDUT(.x(x), .CLK(CLK), .RESET(RESET), .F(F), .S(S));

initial 
    begin 
        CLK = 0;
        forever #10 CLK = ~CLK;

    end

initial
    begin

        $dumpfile("machine_d.vcd");
        $dumpvars;// specify the file as machine_d.vcd
        x = 1'b0;
        RESET = 1'b1; // Assert reset high
        
        #100;

        RESET = 1'b0;


        //(State 000 -> 001) 100-120
        x = 1'b0;
        #20;

        //(State 001 -> 000)120-140
        x = 1'b0;
        #20;

        //(State 000 -> 001) 140-160
        x = 1'b0;
        #20;

        //(State 001 -> 100) 160-180
        x = 1'b1;
        #20;

        //(State 100 -> 101) 180-200
        x = 1'b1;
        #20;

        //(State 101 -> 000) 200-220
        x = 1'b0;
        #20;

        //(State 000 -> 001) 220-240
        x = 1'b0;
        #20;

        //(State 001 -> 000) 240-260
        x = 1'b0;
        #20;

        //(State 000 -> 011) 260-280 
        x = 1'b1;
        #20;

        //(State 011 -> 110) 280-300 
        x = 1'b0;
        #20;

        //(State 110 -> 000) 300-320
        x = 1'b0;
        #20;

        //(State 000 -> 011) 320-340
        x = 1'b1;
        #20;

        //(State 011 -> 010) 340-360
        x = 1'b1;
        #20;

        //(State 010 -> 011) 360-380
        x = 1'b1;
        #20;

        //(State 011 -> 110) 380-400
        x = 1'b0;
        #20;

        //(State 110 -> 000) 400-420
        x = 1'b0;
        #20;

        //(State 000 -> 001) 420-440
        x = 1'b0;
        #20;
        
        //(State 001 -> 000) 440-460 
        x = 1'b0;
        #20;
        
        //(State 000 -> 001) 460-480
        x = 1'b0;
        #20;
        
        //(State 001 -> 000) 480-500
        x = 1'b0;
        #20;

        @(posedge CLK);

        $finish;
    end
endmodule