module machine_d(
    input wire x,
    input wire CLK,
    input wire RESET,
    output wire F,
    output wire [2:0] S
);
    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

    // current state bits
    wire A, B, C;

    assign S = {A, B, C};

    // next state bits
    wire n_A;
    wire n_B;
    wire n_C;

    //n_A minterms
    wire t_nA1 = A & (~B) & (~C);
    wire t_nA2 = (~B) & C & x;
    wire t_nA3 = (~A) & B & C & (~x);

    assign n_A = t_nA1 | t_nA2 | t_nA3 ;

    //n_B minterms 
    wire t_nB1 = A & (~B) & (~C) & (~x);
    wire t_nB2 = (~A) & B & C ;
    wire t_nB3 = (~A) & (~C) & x;

    assign n_B = t_nB1 | t_nB2 | t_nB3 ;

    //n_C minterms
    wire t_nC1 = (~B) & (~C) & x;
    wire t_nC2 = (~A) & (~C);

    assign n_C = t_nC1 | t_nC2;

    //D flip flop for state bits
    dff dffA (.D(n_A), .CLK(CLK), .RESET(RESET), .Q(A));
    dff dffB (.D(n_B), .CLK(CLK), .RESET(RESET), .Q(B));
    dff dffC (.D(n_C), .CLK(CLK), .RESET(RESET), .Q(C));


    // output is 1 when F is 110 
    assign F = A & B & (~C);

endmodule