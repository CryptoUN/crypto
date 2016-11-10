package com.anzusdev.cryptoun.util.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EEA {
	
	public static HashMap<String, List<BigInteger>> getDownData(BigInteger a, BigInteger b){
		List<BigInteger> aList = new ArrayList<>();
		List<BigInteger> bList = new ArrayList<>();
		List<BigInteger> qList = new ArrayList<>();
		aList.add(a);
		bList.add(b);
		while(!b.equals(new BigInteger("0"))){
			qList.add(a.divideAndRemainder(b)[0]);
			aList.add(b);
			b = a.subtract( qList.get(qList.size() - 1).multiply(bList.get(bList.size() -1)) );
			a = bList.get(bList.size()-1);
			bList.add(b);
			
		}
		HashMap<String, List<BigInteger>> map = new HashMap<>();
		map.put("a", aList);
		map.put("b", bList);
		map.put("q", qList);
		return map;
	}
	
	public static HashMap<String, List<BigInteger>> getUpData(HashMap<String, List<BigInteger>> map){
		BigInteger y;
		List<BigInteger> xList = new ArrayList<>();
		List<BigInteger> yList = new ArrayList<>();
		xList.add(new BigInteger("1"));
		yList.add(new BigInteger("0"));
		BigInteger d = map.get("a").get( map.get("a").size()-1 );
		for(int i = 1; i < map.get("a").size(); i++){
			xList.add(yList.get(yList.size()-1));
			y = d.subtract( map.get("a").get(map.get("a").size()-1 - i ).multiply(xList.get(xList.size()-1)) ).divide( map.get("b").get(map.get("b").size()-1 - i) );
			yList.add(y);
		}
		HashMap<String, List<BigInteger>> mapUp = new HashMap<>();
		mapUp.put("x", xList);
		mapUp.put("y", yList);
		List<BigInteger> dList = new ArrayList<>();
		dList.add(d);
		mapUp.put("d", dList );
		
		return mapUp;
	}
	
	public static BigInteger getY(BigInteger a, BigInteger b){
		HashMap<String, List<BigInteger>> mapDown = getDownData(a, b);
		HashMap<String, List<BigInteger>> mapUp = getUpData(mapDown);
		return mapUp.get("y").get(mapUp.get("y").size() -1);
	}

	public static void main(String[] args) {
		HashMap<String, List<BigInteger>> mapDown = getDownData(new BigInteger("372"), new BigInteger("321"));
		System.out.println(getDownData(new BigInteger("372"), new BigInteger("321")));
		System.out.println(getUpData(mapDown));
		System.out.println( getY(new BigInteger("372"), new BigInteger("321")) );
		
	}

}
