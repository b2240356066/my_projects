module multiplier (
    input [3:0] A,
    input [1:0] B,
    output [3:0] p1,
    output [4:0] p2_shifted
);
    
    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

wire [3:0] p2; 

assign p1 = {A[3]&B[0], A[2]&B[0], A[1]&B[0], A[0]&B[0]}; // multiplying all of the contents of A with B[0]

assign p2 = {A[3]&B[1], A[2]&B[1], A[1]&B[1], A[0]&B[1]}; // multipliying all of the contents of A with B[1]

assign p2_shifted = p2 << 1; //same as multiplying it by 2

endmodule  
