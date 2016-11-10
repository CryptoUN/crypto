package com.anzusdev.cryptoun.businesslogic.cfb;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import com.anzusdev.cryptoun.businesslogic.aes.AES;
import com.anzusdev.cryptoun.businesslogic.aes.AES.Type;
import com.anzusdev.cryptoun.businesslogic.aes.KeyGenerator;
import com.anzusdev.cryptoun.businesslogic.des.DES;
import com.anzusdev.cryptoun.businesslogic.des.DESMethod;
import com.anzusdev.cryptoun.util.cfb.CFBUtil;
import com.anzusdev.cryptoun.util.des.BitsHandler;

public class CFB {
	
	public enum Algorithm {
	    PI, DES, AES 
	}
	
	public static final String I_ARRAY = "IArray";
	public static final String O_ARRAY = "OArray";
	public static final String T_ARRAY = "tArray";
	public static final String M_ARRAY = "mArray";
	public static final String C_ARRAY = "cArray";
	
	public static final String CIPHER_BITS = "cipherBits";
	public static final String CIPHER_TEXT = "cipherText";
	
	public static final String PLAIN_BITS = "plainBits";
	public static final String PLAIN_TEXT = "plainText";
	
	public static final String AES_KEY_BITS = "aesKeyBits";
	public static final String AES_KEY_TEXT = "aesKeyText";
	
	public static final String M_SIZE_OUT = "mSize";
	
	public static final String IV = "iv";
	public static final String M_SIZE = "mSize";
	
	BitsHandler bH = new BitsHandler();
	CFBUtil cfbUtil = new CFBUtil();
	
	BitSet desKeyBs;
	int[] aesKeyBs;
	
	public BitSet[] divideBits(BitSet m, int numberOfBits, int r){
		int fillingSize = numberOfBits % r == 0 ? 0 : r - numberOfBits % r;  
		
		BitSet filling = new BitSet(fillingSize);
		filling.clear();
		
		BitSet completed = bH.concatenateBitStrings(m, filling, fillingSize);
		
		BitSet[] mArray = new BitSet[(numberOfBits + fillingSize) / r];
		for(int i = 0; i < (numberOfBits + fillingSize) / r; i++)
			mArray[i] = completed.get(i * r, i * r + r);
		
		return mArray;
	}
	
	public HashMap<String, String[]> encrypt(BitSet plainBs, int numberOfBits, Algorithm algorithm, Type aesType){
		ArrayList<BitSet[]> arrays = new ArrayList<>();
		int n, r;
		
		switch(algorithm){
			case DES: n = 64; r = 16; desKeyBs = DES.generateInitialKey();
			break;
			case AES: n = 128; r = 32; aesKeyBs = KeyGenerator.generateInitialKey(aesType);
			break;
			case PI:
			default: n=4; r=3;
			break;
		}
		
		BitSet[] mArray = divideBits(plainBs, numberOfBits, r);
		int iterations = mArray.length;
		
		BitSet[] iArray = new BitSet[ iterations ];
		BitSet[] oArray = new BitSet[ iterations ];
		BitSet[] tArray = new BitSet[ iterations ];
		BitSet[] cArray = new BitSet[ iterations ];
		
		System.out.println("\nCipher\nj\tIj\tOj\ttj\tmj\tcj");
		int reverseIndex = iterations - 1;
		for( int j = 0; j < iterations; j++){
			iArray[j] = j == 0 ? getIVj(j, null, null, r, n) : 
								 getIVj(j, cArray[j-1], iArray[j-1], r, n);
			oArray[j] = getOj(iArray[j], algorithm, r, n, true, aesType);
			tArray[j] = getTj(oArray[j], r, n);
			cArray[j] = getCj(tArray[j], mArray[reverseIndex - j]);
			
			System.out.println( (j+1) + "\t" + bH.convertBSToString(iArray[j], n) + "\t" + bH.convertBSToString(oArray[j], n) + "\t" + bH.convertBSToString(tArray[j], r) + "\t" + bH.convertBSToString(mArray[reverseIndex - j], r) + "\t" + bH.convertBSToString(cArray[j], r) + "\t");
		}
		
		int cTextLength = iterations * r;
		BitSet cipherBS = new BitSet( cTextLength );
		for(int i = 0; i < cTextLength; i++)
			cipherBS.set(i, cArray[ (cTextLength - 1 - i) / r ].get(i % r));
		
		String cipherBits = bH.convertBSToString(cipherBS, cTextLength);
		System.out.println("cipherBits: " + cipherBits);
		
		arrays.add(iArray);
		arrays.add(oArray);
		arrays.add(tArray);
		arrays.add(mArray);
		arrays.add(cArray);
		
		HashMap<String, String[]> map =  cfbUtil.getPrintMap(arrays, true, r, n);
		
		map.put(CIPHER_BITS, new String[]{cipherBits});
		map.put(CIPHER_TEXT, new String[]{ bH.getTraductionText(cipherBits) });
		map.put(IV, new String[]{ map.get(I_ARRAY)[0] });
		map.put(M_SIZE_OUT, new String[]{ String.valueOf(numberOfBits) });
		
		if(algorithm == Algorithm.DES){
			String desKeyBits = bH.convertBSToString(desKeyBs, 64);
			map.put(DESMethod.DES_KEY_BITS, new String[]{ desKeyBits });
			map.put(DESMethod.DES_KEY_TEXT, new String[]{ bH.getTraductionText(desKeyBits) });
		}
		if(algorithm == Algorithm.AES){
			String aesKeyBits = bH.convertIntArrayToBitString(aesKeyBs);
			map.put(AES_KEY_BITS, new String[]{ aesKeyBits });
			map.put(AES_KEY_TEXT, new String[]{ bH.getTraductionText(aesKeyBits) });
		}
		
		return map;
	}
	
	public HashMap<String, String[]> decrypt(BitSet cipherBs, BitSet ivBs, BitSet desKeyBs, int numberOfBits, int plainSize, Algorithm algorithm, Type aesType, String aesKeyBits){
		ArrayList<BitSet[]> arrays = new ArrayList<>();
		int n, r;
		
		switch(algorithm){
			case DES: n = 64; r = 16; this.desKeyBs = desKeyBs;
			break;
			case AES: n =128; r = 32; this.aesKeyBs = bH.convertBitStringToIntArray(aesKeyBits);
			break;
			case PI:
			default: n=4; r=3;
			break;
		}
		BitSet[] cArray = divideBits(cipherBs, numberOfBits, r);
		
		int iterations = cArray.length;
		
		BitSet[] iArray = new BitSet[ iterations ];
		BitSet[] oArray = new BitSet[ iterations ];
		BitSet[] tArray = new BitSet[ iterations ];
		BitSet[] mArray = new BitSet[ iterations ];
		
		System.out.println("\nDecipher\nj\tIj\tOj\ttj\tcj\tmj");
		
		int reverseIndex = iterations - 1;
		
		for( int j = 0; j < iterations; j++){
			iArray[j] = j == 0 ? ivBs : 
						getIVj(j, cArray[reverseIndex - (j-1)], iArray[j-1], r, n);
			
			oArray[j] = getOj(iArray[j], algorithm, r, n, false, aesType);
			tArray[j] = getTj(oArray[j], r, n);
			mArray[j] = getCj(tArray[j], cArray[reverseIndex - j]);
			
			System.out.println( (j+1) + "\t" + bH.convertBSToString(iArray[j], n) + "\t" + bH.convertBSToString(oArray[j], n) + "\t" + bH.convertBSToString(tArray[j], r) + "\t" + bH.convertBSToString(cArray[reverseIndex - j], r) + "\t" + bH.convertBSToString(mArray[j], r) + "\t");
		}
		
		int pTextLength = iterations * r;
		BitSet plainBS = new BitSet( pTextLength );
		for(int i = 0; i < pTextLength; i++)
			plainBS.set(i, mArray[ (pTextLength - 1 - i) / r ].get(i % r));
		
		String plainBits = bH.convertBSToString(plainBS, pTextLength);
		plainBits = plainBits.substring(0, plainSize);
		System.out.println("plainBits: " + plainBits);
		
		arrays.add(iArray);
		arrays.add(oArray);
		arrays.add(tArray);
		arrays.add(mArray);
		arrays.add(cArray);
		
		HashMap<String, String[]> map =  cfbUtil.getPrintMap(arrays, false, r, n);
		
		map.put(PLAIN_BITS, new String[]{plainBits});
		map.put(PLAIN_TEXT, new String[]{ bH.getTextFromBinary(plainBits)/*bH.getTraductionText(plainBits)*/ });
		
		System.out.println("Plain1: " + bH.getTextFromBinary(plainBits));
		System.out.println("Plain2: " + bH.getTraductionText(plainBits));
		
		return map;
	}
	
	private BitSet getIVj(int j, BitSet cjPrev, BitSet ivjPrev, int r, int n){
		if(j == 0)
			return generateIV(n);
		
		BitSet iv = new BitSet(n);
		for(int i = 0; i < n; i++){
			if(i < r){
				try{
					iv.set( i, cjPrev.get(i) );
				}catch(IndexOutOfBoundsException e){
					iv.set( i, false);
				}
			}else{
				try{
					iv.set( i, ivjPrev.get(i - r) );
				}catch(IndexOutOfBoundsException e){
					iv.set( i, false);
				}
			}
		}
		return iv;
	}
	
	private BitSet getOj(BitSet ivj, Algorithm algorithm, int r, int n, boolean isEncryption, Type aesType){
		if( algorithm == Algorithm.PI){
			List<Integer> pi = generatePi();
			int index = n - 1;
			BitSet oj = new BitSet(n);
			for(int i = 0; i < n; i++){
				try{
					oj.set( index - i, ivj.get(index - pi.get(i)) );
				}catch(IndexOutOfBoundsException e){
					oj.set( index - 1, false );
				}
			}
			return oj;
		}
		if( algorithm == Algorithm.DES){
			return DES.encrypt(ivj, desKeyBs);
		}
		if( algorithm == Algorithm.AES){
			String ivjBits = bH.convertBSToString(ivj, n);
			int[] ivjIntA = bH.convertBitStringToIntArray(ivjBits);
			int[] aesEncIntA = AES.encrypt(ivjIntA, aesKeyBs, aesType);
			String aesEncBits = bH.convertIntArrayToBitString(aesEncIntA);
			return bH.bsFromString(aesEncBits);
		}
		return null;
	}
	
	private BitSet getTj(BitSet oj, int r, int n){
		BitSet tj = new BitSet(r);
		int index = n - 1;
		int diference = n - r;
		for(int i = index; i >= diference; i--){
			try{
				tj.set(i - diference, oj.get(i) );
			}catch(IndexOutOfBoundsException e){
				tj.set(i - diference, false);
			}
		}
		return tj;
	}
	
	private BitSet getCj(BitSet tj, BitSet mj){
		BitSet cj = (BitSet) tj.clone();
		cj.xor(mj);
		return cj;
	}
	
	private BitSet generateIV(int n){
		BitSet iv = new BitSet(n);
		for(int i = 0; i < n; i++){
			iv.set( i, Math.round( Math.random() ) == 1 ? true: false);
		}
		return iv;
	}
	
	private List<Integer> generatePi(){
		List<Integer> pi = new ArrayList<>();
		pi.add(1);
		pi.add(2);
		pi.add(3);
		pi.add(0);
		return pi;
	}

}
