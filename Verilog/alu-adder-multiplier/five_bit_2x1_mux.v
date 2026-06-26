module five_bit_2x1_mux(In_1, In_0, Select, Out);
	input [4:0] In_1;
	input [4:0] In_0;
	input Select;
	output [4:0] Out;
	
	// Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
	
assign Out = (Select == 1'b0) ? In_0 : In_1 ; //if select is 0 Out is In_0 else it is In_1

endmodule
