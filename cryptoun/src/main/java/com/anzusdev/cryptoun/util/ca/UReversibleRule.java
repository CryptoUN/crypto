package com.anzusdev.cryptoun.util.ca;

public class UReversibleRule {
	
	public static final boolean[][] bitsIn = {
		{false, false, false}, {true, false, false}, {false, true, false},
		{true, true, false}, {false, false, true}, {true, false, true},
		{false, true, true}, {true, true, true}
	};
	
	private boolean[][] ruleBits;

	public UReversibleRule(int numberOfRule){
		numberOfRule = numberOfRule > 255 ? numberOfRule % 255 : numberOfRule;
		this.ruleBits = computeRuleBits(numberOfRule, 255 - numberOfRule);
	}
	
	public boolean getResponse(boolean[] bits, boolean prev){
		if(bits.length != 3) return false;
		for(int i = 0; i < bitsIn.length; i++){
			if(bits[2] == bitsIn[i][2] && bits[1] == bitsIn[i][1] && bits[0] == bitsIn[i][0]){
				if(prev)
					return ruleBits[1][i];
				else
					return ruleBits[0][i];
			}
		}
		return false;
	}
	
	public boolean[] applyToArray(boolean[] array, boolean[] arrayPrev){
		boolean[] arrayOut = new boolean[array.length];
		boolean[] bitsOut = new boolean[3];
		for(int i = 0 ; i < array.length; i++){
			boolean left = i > 0 ? array[i-1] : false; //array[ array.length - 1 ]
			boolean right = i < array.length - 1 ? array[i+1] : false;//array[0];
			bitsOut[2] = left;
			bitsOut[1] = array[i];
			bitsOut[0] = right;
			arrayOut[i] = getResponse(bitsOut, arrayPrev[i]);
		}
		return arrayOut;
	}
	
	public boolean[][] computeUCA(int iterations, int numBits){
		boolean[][] matrixOut = new boolean[iterations*2-1][numBits];
		
		for(int i = 0 ; i < numBits; i++){
			matrixOut[0][i] = i == numBits / 2 ? true : false;
			matrixOut[1][i] = i == numBits / 2 ? true : false;
		}
		
		for(int i = 2 ; i < iterations; i++)
			matrixOut[i] = applyToArray(matrixOut[i-1], matrixOut[i-2]);
		
		matrixOut[iterations] = matrixOut[iterations-2];
		for(int i = iterations+1; i < iterations*2 -1; i++){
			if(i == iterations)
				matrixOut[i] = applyToArray(matrixOut[i-2], matrixOut[i-1]);
			else
				matrixOut[i] = applyToArray(matrixOut[i-1], matrixOut[i-2]);
			System.out.println();
			for(int j = 0; j < matrixOut[i].length; j++)
				System.out.print(matrixOut[i][j] ? "1": "0");
		}
		return matrixOut;
	}
	
	public boolean[][] computeRuleBits(int numberOfRule, int numberOfReverseRule){
		boolean[][] bits = new boolean[2][8];
		for(int i = 0; i < bits.length; i++){
			int actualPow = 7;
			if(i == 1)
				numberOfRule = numberOfReverseRule;
			while(actualPow >= 0){
				if(numberOfRule >= Math.pow(2, actualPow)){
					numberOfRule -= Math.pow(2, actualPow);
					bits[i][actualPow] = true;
				}else
					bits[i][actualPow] = false;
				actualPow--;
			}
		}
		return bits;
	}
	
	public boolean[][] getRuleBits() {
		return ruleBits;
	}

	public void setRuleBits(boolean[][] ruleBits) {
		this.ruleBits = ruleBits;
	}

}
