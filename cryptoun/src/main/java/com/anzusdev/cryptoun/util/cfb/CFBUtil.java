package com.anzusdev.cryptoun.util.cfb;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import com.anzusdev.cryptoun.businesslogic.cfb.CFB;
import com.anzusdev.cryptoun.util.des.BitsHandler;

public class CFBUtil {
	
	public static final int INDEX_I = 0;
	public static final int INDEX_O = 1;
	public static final int INDEX_T = 2;
	public static final int INDEX_M = 3;
	public static final int INDEX_C = 4;
	
	BitsHandler bH = new BitsHandler();
	
	public HashMap<String, String[]> getPrintMap(ArrayList<BitSet[]> arrays, boolean cipher, int r, int n){
		
		int dim = arrays.get(INDEX_I).length;
		String[] iStringArray = new String[dim];
		String[] oStringArray = new String[dim];
		String[] tStringArray = new String[dim];
		String[] mStringArray = new String[dim];
		String[] cStringArray = new String[dim];
		
		for(int i = 0; i < dim; i++){
			iStringArray[i] = bH.convertBSToString( arrays.get(INDEX_I)[i], n );
			oStringArray[i] = bH.convertBSToString( arrays.get(INDEX_O)[i], n );
			tStringArray[i] = bH.convertBSToString( arrays.get(INDEX_T)[i], r );
			if( cipher ){
				mStringArray[i] = bH.convertBSToString( arrays.get(INDEX_M)[i], r );
				cStringArray[i] = bH.convertBSToString( arrays.get(INDEX_C)[dim - 1 - i], r );
			}else{
				mStringArray[i] = bH.convertBSToString( arrays.get(INDEX_M)[dim - 1 - i], r );
				cStringArray[i] = bH.convertBSToString( arrays.get(INDEX_C)[i], r );
			}
			
		}
		
		HashMap<String, String[]> map = new HashMap<>();
		map.put(CFB.I_ARRAY, iStringArray);
		map.put(CFB.O_ARRAY, oStringArray);
		map.put(CFB.T_ARRAY, tStringArray);
		map.put(CFB.M_ARRAY, mStringArray);
		map.put(CFB.C_ARRAY, cStringArray);
		
		return map;
	}

}
