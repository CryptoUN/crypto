package com.anzusdev.cryptoun.businesslogic.cfb;

import java.util.BitSet;
import java.util.HashMap;

import com.anzusdev.cryptoun.businesslogic.aes.AES.Type;
import com.anzusdev.cryptoun.businesslogic.cfb.CFB.Algorithm;
import com.anzusdev.cryptoun.util.des.BitsHandler;
import com.anzusdev.cryptoun.util.des.Util;

public class CFBMethod {
	
	public CFB cfb = new CFB();
	BitsHandler bH = new BitsHandler();
	
	public HashMap<String, String[]> encryptCFB( String plainText, Algorithm algorithm, Type aesType ){
		String plainBits;
		BitSet plainBs;
		if(algorithm == Algorithm.PI){
			plainBits = bH.translateText(plainText);
			System.out.println("pb: " + plainBits);
			plainBs = bH.bsFromString(plainBits);
			return cfb.encrypt(plainBs, plainBits.length(), Algorithm.PI, null);
		}
		if(algorithm == Algorithm.DES){
			plainBs = bH.getBits(plainText);
			plainBits = Util.convertBitSetToString(plainBs, 64);
			return cfb.encrypt(plainBs, plainBits.length(), algorithm, null);
		}
		if(algorithm == Algorithm.AES){
			plainBs = bH.getBits(plainText);
			plainBits = Util.convertBitSetToString(plainBs, 128);
			return cfb.encrypt(plainBs, plainBits.length(), algorithm, aesType);
		}
		return null;
	}
	
	public HashMap<String, String[]> decryptCFB( String cipherText, String ivBits, String desKeyText, int plainSize,  Algorithm algorithm, Type aesType, String aesKeyText ){
		String cipherBits = bH.translateText(cipherText);
		BitSet cipherBs = bH.bsFromString(cipherBits);
		BitSet ivBs = bH.bsFromString(ivBits);
		
		System.out.println("input ciphertext: " + cipherBits);
		if(algorithm == Algorithm.PI)
			return cfb.decrypt(cipherBs, ivBs, null, cipherBits.length(), plainSize, algorithm, null, "");
		
		if(algorithm == Algorithm.DES){
			String desKeyBits = bH.translateText(desKeyText);
			BitSet desKeyBs = bH.bsFromString(desKeyBits);
			return cfb.decrypt(cipherBs, ivBs, desKeyBs, cipherBits.length(), cipherBits.length(), algorithm, null, "");
		}
		
		if(algorithm == Algorithm.AES){
			String aesKeyBits = bH.translateText(aesKeyText);
			return cfb.decrypt(cipherBs, ivBs, null, cipherBits.length(), cipherBits.length(), algorithm, aesType, aesKeyBits);
		}
		
		return null;
	}

}
