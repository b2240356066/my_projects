`timescale 1ns/1ps
module main_tb;

    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
    
    reg [3:0] A;
    reg [1:0] B;
    reg multiply;
    //these are the inputs

    wire [4:0] Result;
    wire Cout;


    main Main(A, B, multiply, Result, Cout);

    initial 
        begin
        $dumpfile("main.vcd");
        $dumpvars;// specify the file as main.vcd

	// test cases from the pdf
	 
A = 4'b1111; B =  2'b00; multiply = 1'b1; // multiply 1 
#10; 
A = 4'b1111; B =  2'b01; multiply = 1'b1; //multiply 1 
#10; 
A = 4'b1111; B =  2'b10; multiply = 1'b1; //multiply 1 
#10; 
A = 4'b1111; B =  2'b11; multiply = 1'b1; //multiply 1 
#10;

A = 4'b1010; B =  2'b00; multiply = 1'b0; //multiply 0 
#10; 
A = 4'b1010; B =  2'b01; multiply = 1'b0; //multiply 0 
#10; 
A = 4'b1010; B =  2'b10; multiply = 1'b0; //multiply 0 
#10; 
A = 4'b1010; B =  2'b11; multiply = 1'b0; //multiply 0 
#10;$finish;
end
endmodule
