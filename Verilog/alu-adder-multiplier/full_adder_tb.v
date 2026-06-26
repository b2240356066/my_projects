`timescale 1 ns/10 ps
module full_adder_tb;

    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

    
    reg A;      // Input bit 1
    reg B;      // Input bit 2
    reg Cin;    // Carry-In bit
    wire Sum;   // Sum output bit
    wire Cout;  // Carry-Out bit

    full_adder FA (A, B, Cin, Sum, Cout);

    initial 
        begin
        $dumpfile("full_adder.vcd");
        $dumpvars; //specifies the file as full_adder.vcd

        A = 4'b0000; B = 4'b0000; Cin = 1'b0; //no carry in 
        #10; 
        A = 4'b0101; B = 4'b0001; Cin = 1'b0; // no carry in
        #10; 
        A = 4'b1010; B = 4'b0010; Cin = 1'b1; // carry in 
        #10; 
        A = 4'b1111; B = 4'b0011; Cin = 1'b1; // carry in
        #10; 
        $finish; 
    end

endmodule