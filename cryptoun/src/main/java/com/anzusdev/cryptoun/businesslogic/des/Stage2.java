package com.anzusdev.cryptoun.businesslogic.des;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

import com.anzusdev.cryptoun.util.des.Util;

public class Stage2 {
	
	//S-boxes
    private static int[][] S1 = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};

    private static int[][] S2 = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};

    private static int[][] S3 = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};

    private static int[][] S4 = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};

    private static int[][] S5 = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};

    private static int[][] S6 = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};

    private static int[][] S7 = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};

    private static int[][] S8 = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

    //Expansion
    private static int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    //Permutation
    private static int[] P = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25};


    public static BitSet feistelFunction(BitSet ri, BitSet key) {

        BitSet expandedRi = new BitSet(48);

        //Expansion
        for (int i = 0; i < E.length; i++) {
            expandedRi.set(47 - i, ri.get(32 - E[i]));
        }

        expandedRi.xor(key);

        BitSet subRi = new BitSet(32);

        //S-boxes
        int[][][] SBoxes = {S1, S2, S3, S4, S5, S6, S7, S8};
        int row, col;

        for (int i = 0, s = 7; i < 48; i += 6, s--) {
            row = (expandedRi.get(i) ? 1 : 0) + (expandedRi.get(i + 5) ? 2 : 0);
            col = Util.bitSetToInt(expandedRi.get(i + 1, i + 5));
            BitSet output = BitSet.valueOf(new long[]{SBoxes[s][row][col]});
            for (int j = 0; j < output.length(); j++) {
                subRi.set((7 - s)* 4 + j, output.get(j));
            }
        }

        BitSet permRi = new BitSet(32);

        //Permutation
        for (int i = 0; i < P.length; i++) {
            permRi.set(31 - i, subRi.get(32 - P[i]));
        }

        return permRi;
    }

    public static BitSet rounds(BitSet m0, BitSet[] keys) {

        BitSet li, ri, result;

        li = m0.get(32, 64);
        ri = m0.get(0, 32);

        for (int i = 0; i < keys.length; i++) {
            BitSet fi = feistelFunction(ri, keys[i]);
            BitSet temp = (BitSet) ri.clone();
            ri = (BitSet) li.clone();
            ri.xor(fi);
            li = temp;
        }

        result = Util.concatenateBitStrings(ri, li, 64);

        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

        //Round 16
        String sm ="1100001010001100100101100000110101000011010000100011001000110100";
        String sKey = "110010110011110110001011000011100001011111110101";
        BitSet m = new BitSet(64);
        BitSet [] key = {new BitSet(64)};

        for(int i  = 63, j = 0; i >= 0; i--, j++){
            m.set(i, sm.charAt(j) == '1');
        }

        for(int i  = 47, j = 0; i >= 0; i--, j++){
            key[0].set(i, (sKey.charAt(j) == '1'));
        }


        System.out.println("m "+Util.convertBitSetToString(m, 64));
        BitSet res = Stage2.rounds(m, key);
        BitSet ciphertext = DES.finalPermutation(res);

        System.out.println("ciphertext "+Util.convertBitSetToString(ciphertext, 64));
    }

}