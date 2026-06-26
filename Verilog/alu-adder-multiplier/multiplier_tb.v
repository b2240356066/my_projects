`timescale 1ns/10ps
module multiplier_tb;

   // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

reg [3:0] A;
reg [1:0] B;
//A and B are inputs

wire [3:0] p1;
wire [4:0] p2_shifted;
//p1 and p2_shifted are outputs

multiplier MT(A, B, p1, p2_shifted); //call multiplier function

initial
	begin
	$dumpfile("multiplier.vcd");
	$dumpvars; //specify the file as multiplier.vcd

	//test cases from pdf

	A = 4'b0000; B = 2'b00;
	#10 
	A = 4'b0101; B = 2'b01;
	#10 
	A = 4'b1010; B = 2'b10;
	#10 
	A = 4'b1111; B = 2'b11;
	#10 
	A = 4'b0011; B = 2'b01;
	#10 
	A = 4'b1001; B = 2'b10;
	#10 
	A = 4'b0110; B = 2'b11;
	#10 
	A = 4'b1100; B = 2'b01;$finish;
	end

endmodule 
