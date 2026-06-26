`timescale 1 ns/10 ps
module five_bit_rca_tb;

  // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

reg [4:0] A, B ;
reg Cin;
//A, B and Cin are inputs 

wire Cout; 
wire [4:0] S;
//Cout and S(Sum) are outputs 


five_bit_rca FBRCA(A, B, Cin, S, Cout); //calling the function

initial 
  begin
    $dumpfile("five_bit_rca.vcd");
    $dumpvars;// specify the file as five_bit_rca.vcd

// test cases from the pdf

  A = 5'b00000; B = 5'b00000; Cin = 1'b0; //no carry in 
  #10; 
  A = 5'b00001; B = 5'b00010; Cin = 1'b0; // no carry in
  #10; 
  A = 5'b00100; B = 5'b00011; Cin = 1'b0; // no carry in 
  #10; 
  A = 5'b01010; B = 5'b00101; Cin = 1'b0; // no carry in
  #10;

  A = 5'b00000; B = 5'b11111; Cin = 1'b1; // carry in 
  #10; 
  A = 5'b00011; B = 5'b10101; Cin = 1'b1; // carry in
  #10; 
  A = 5'b01100; B = 5'b01010; Cin = 1'b1; // carry in 
  #10; 
  A = 5'b11100; B = 5'b00001; Cin = 1'b1; // carry in
  #10;$finish;
  end

endmodule