package com.anzusdev.cryptoun.util.des;

import java.util.BitSet;

public class Util {

    public static BitSet concatenateBitStrings(BitSet left, BitSet right, int n) {
        BitSet total = new BitSet(n);

        for(int i = 0; i < n/2 ; i++) {
            total.set(i, right.get(i));
            total.set(i + n/2, left.get(i));
        }

        return total;
    }

    public static String convertBitSetToString(BitSet bitString, int n) {
    	if( n <= 0)
    		return "";
    		
    	int numberOfBlocks = (int) Math.ceil((float)bitString.length()/(float)n);
        final StringBuilder buffer = new StringBuilder();
        for(int j = numberOfBlocks; j >= 1; j--){
        	for(int i = n*j  - 1; i >= n*(j-1); i--){
                buffer.append(bitString.get(i)? '1': '0');
            }
        }
        return buffer.toString();
    }

    public static int bitSetToInt(BitSet bitSet) {
        int num = 0, i = 0;

        while (true) {
            i = bitSet.nextSetBit(i);
            if(i < 0) break;
            num |= (1 << i);
            i++;
        }

        num &= Integer.MAX_VALUE;
        return num;
    }

}
