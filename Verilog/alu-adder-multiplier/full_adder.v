module full_adder(
    input A,
    input B,
    input Cin,
    output S,
    output Cout
);

    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
	
wire w1, w2, w3;

//first half adder
xor G1(w1, A, B);
and G2(w2, A, B);

//second half adder

xor G3(S, w1, Cin);
and G4(w3, w1, Cin);

assign Cout = w2 | w3; // Cout = w2 or w3;

endmodule