module main(A, B, multiply, Result, Cout);
    input [3:0] A;
    input [1:0] B;
    input multiply;
    output [4:0] Result;
    output Cout;

    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

wire [3:0] p1;
wire [4:0] p2_shifted;
//p1 and p2_shifted are interior wires that are outputs of full adders

wire [4:0]  MUX_output_1;
wire [4:0]  MUX_output_2;
//MUX_output_1 and MUX_output_2 are interior wires that are outputs of MUX

wire Cin = 1'b0;// it is 0 initially

multiplier MT(A, B, p1, p2_shifted); //calling multiplier
five_bit_2x1_mux MUX1({1'b0, p1}, {1'b0, A}, multiply, MUX_output_1); // calling five_bit_2x1_mux and also because p1 and A are 4 bits we need to add a zero to make 5 bits
five_bit_2x1_mux MUX2(p2_shifted,{3'b0, B}, multiply, MUX_output_2); // calling five_bit_2x1_mux  and also beacuse B is 2 bits we need to add 3 zeros to make 5 bits
five_bit_rca FBRCA( MUX_output_1,  MUX_output_2, Cin, Result, Cout); // calling five_bit_rca 

endmodule
