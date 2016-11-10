package com.anzusdev.cryptoun.util.ca;

public class UnidimentionalRule {
	
	public static final boolean[][] bitsIn = {
		{false, false, false}, {true, false, false}, {false, true, false},
		{true, true, false}, {false, false, true}, {true, false, true},
		{false, true, true}, {true, true, true}
	};
	
	private boolean[] ruleBits;

	public UnidimentionalRule(int numberOfRule){
		numberOfRule = numberOfRule > 255 ? numberOfRule % 255 : numberOfRule;
		this.ruleBits = computeRuleBits(numberOfRule);
	}
	
	public boolean getResponse(boolean[] bits){
		if(bits.length != 3) return false;
		for(int i = 0; i < bitsIn.length; i++){
			if(bits[2] == bitsIn[i][2] && bits[1] == bitsIn[i][1] && bits[0] == bitsIn[i][0]){
				return ruleBits[i];
			}
		}
		return false;
	}
	
	public boolean[] applyToArray(boolean[] array){
		boolean[] arrayOut = new boolean[array.length];
		boolean[] bitsOut = new boolean[3];
		for(int i = 0 ; i < array.length; i++){
			boolean left = i > 0 ? array[i-1] : array[ array.length - 1 ];
			boolean right = i < array.length - 1 ? array[i+1] : array[0];
			bitsOut[2] = left;
			bitsOut[1] = array[i];
			bitsOut[0] = right;
			arrayOut[i] = getResponse(bitsOut);
		}
		return arrayOut;
	}
	
	public boolean[][] computeUCA(int iterations, int numBits){
		boolean[][] matrixOut = new boolean[iterations][numBits];
		for(int i = 0 ; i < numBits; i++)
			matrixOut[0][i] = i == numBits / 2 ? true : false;
		for(int i = 1 ; i < iterations; i++)
			matrixOut[i] = applyToArray(matrixOut[i-1]);
		return matrixOut;
	}
	
	public boolean[] computeRuleBits(int numberOfRule){
		boolean[] bits = new boolean[8];
		int actualPow = 7;
		while(actualPow >= 0){
			if(numberOfRule >= Math.pow(2, actualPow)){
				numberOfRule -= Math.pow(2, actualPow);
				bits[actualPow] = true;
			}else
				bits[actualPow] = false;
			actualPow--;
		}
		return bits;
	}
	
	public boolean[] getRuleBits() {
		return ruleBits;
	}

	public void setRuleBits(boolean[] ruleBits) {
		this.ruleBits = ruleBits;
	}

}
