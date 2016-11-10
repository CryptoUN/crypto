package com.anzusdev.cryptoun.businesslogic.ca;

import java.util.ArrayList;
import java.util.List;

import com.anzusdev.cryptoun.util.ca.UReversibleRule;
import com.anzusdev.cryptoun.util.ca.UnidimentionalRule;

public class UCAMethod {
	
	public List<String> evolve(int rule, int numBits, int iterations) {
		UnidimentionalRule uca = new UnidimentionalRule(rule);
		boolean[][] actualMatrix = new boolean[iterations][numBits];
		for(int i = 0; i < iterations; i++){
			actualMatrix = uca.computeUCA(iterations, numBits).clone();
		}
		return matrixToString(actualMatrix);
	}
	
	public List<String> evolveReversible(int rule, int numBits, int iterations) {
		UReversibleRule ruca = new UReversibleRule(rule);
		boolean[][] actualMatrix = new boolean[iterations][numBits];
		for(int i = 0; i < iterations; i++){
			actualMatrix = ruca.computeUCA(iterations, numBits).clone();
		}
		return matrixToString(actualMatrix);
	}
	
	private List<String> matrixToString(boolean[][] matrix){
		ArrayList<String> data = new ArrayList<>();
		for(int i = 0; i < matrix.length; i++){
			String array = "";
			for(int j = 0; j < matrix[0].length; j++){
				if(matrix[i][j])
					array += "1";
				else
					array += "0";
			}
			data.add(array);
		}
		return data;
	}

}
