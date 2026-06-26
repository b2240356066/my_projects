`timescale 1ns/10ps
module five_bit_2x1_mux_tb;
	
	// Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
	reg [4:0] In_0; 
	reg [4:0] In_1;
	reg Select;
	// In_0, In_1 and Select are inputs

	wire [4:0] Out;
	//out is output

	five_bit_2x1_mux FBM(In_1, In_0, Select, Out); // call five_bit_2x1_mux function here

	initial 
        begin
        $dumpfile("five_bit_2x1_mux.vcd");
        $dumpvars;// specify the file as five_bit_2x1_mux.vcd

	 // test cases from the pdf
	 
	In_0 = 5'b00011; In_1 = 5'b10100; Select = 1'b0; 
    #10;
    In_0 = 5'b11100; In_1 = 5'b01010; Select = 1'b0;
    #10;
    In_0 = 5'b00101; In_1 = 5'b11111; Select = 1'b1; 
    #10;
    In_0 = 5'b10000; In_1 = 5'b10001; Select = 1'b1;  
	#10;$finish; 
	end


endmodule
