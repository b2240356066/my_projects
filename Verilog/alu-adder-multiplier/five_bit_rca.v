module five_bit_rca(
    input [4:0] A,
    input [4:0] B,
    input Cin,
    output [4:0] S,
    output Cout
);

    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

wire [3:0] Carries;
//using full adder function here to add all 5 bits one by one 

//adding LSB bits
full_adder A1(.A(A[0]),.B(B[0]),.Cin(Cin),.S(S[0]),.Cout(Carries[0])); //Cout of this is the carry in of the other full_adder

full_adder A2(.A(A[1]),.B(B[1]),.Cin(Carries[0]),.S(S[1]),.Cout(Carries[1])); //Cout of this is the carry in of the other full_adder

full_adder A3(.A(A[2]),.B(B[2]),.Cin(Carries[1]),.S(S[2]),.Cout(Carries[2])); //Cout of this is the carry in of the other full_adder

full_adder A4(.A(A[3]),.B(B[3]),.Cin(Carries[2]),.S(S[3]),.Cout(Carries[3])); //Cout of this is the carry in of the other full_adder
//adding MSB bits
full_adder A5(.A(A[4]),.B(B[4]),.Cin(Carries[3]),.S(S[4]),.Cout(Cout)); //Cout is the last Cout

endmodule