module machine_jk(
    input wire x,
    input wire CLK,
    input wire RESET,
    output wire F,
    output wire [2:0] S
);
    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!


wire J2, K2, J1, K1, J0, K0; // Inputs to the FFs
wire Q2, Q1, Q0;

// S[2] is Q2, S[1] is Q1, S[0] is Q0.
assign S = {Q2, Q1, Q0};

//Q2 JK flip flop (MSB)
jkff Q2JKff(.J(J2), .K(K2), .CLK(CLK), .RESET(RESET), .Q(Q2));

//Q1 JK flip flop 
jkff Q1JKff(.J(J1), .K(K1), .CLK(CLK), .RESET(RESET), .Q(Q1));

//Q0 JK flip flop (LSB)
jkff Q0JKff(.J(J0), .K(K0), .CLK(CLK), .RESET(RESET), .Q(Q0));


// Logic for Q2 (MSB, corresponds to A in machine_d)
wire D_A = (Q2 & ~Q1 & ~Q0) | (~Q1 & Q0 & x) | (~Q2 & Q1 & Q0 & ~x);
assign J2 = D_A;
assign K2 = ~D_A;

// Logic for Q1 (corresponds to B in machine_d)
wire D_B = (Q2 & ~Q1 & ~Q0 & ~x) | (~Q2 & Q1 & Q0) | (~Q2 & ~Q0 & x);
assign J1 = D_B;
assign K1 = ~D_B;

// Logic for Q0 (LSB, corresponds to C in machine_d)
wire D_C = (~Q1 & ~Q0 & x) | (~Q2 & ~Q0);
assign J0 = D_C;
assign K0 = ~D_C;

// Output logic 
assign F = (Q2 & Q1 & ~Q0);
endmodule



