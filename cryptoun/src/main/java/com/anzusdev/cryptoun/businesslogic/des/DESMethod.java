package com.anzusdev.cryptoun.businesslogic.des;

import java.util.BitSet;
import java.util.HashMap;

import com.anzusdev.cryptoun.util.des.BitsHandler;
import com.anzusdev.cryptoun.util.des.Util;

public class DESMethod {
	
	public static final String CIPHER_TEXT = "ciphertext";	
	public static final String CIPHER_BITS = "cipherBits";
	
	public static final String DES_KEY_TEXT = "desKeyText";
	public static final String DES_KEY_BITS = "desKeyBits";
	
	public static final String PLAIN_TEXT = "plaintext";
	public static final String PLAIN_BITS = "plainBits";
	
	BitsHandler bH = new BitsHandler();
	
	public HashMap<String, String> encryptDES( String plainText ){
		String cipherBits = "";
		BitSet desKeyBs = DES.generateInitialKey();
		String desKeyBits = bH.convertBSToString(desKeyBs, 64);
		
		BitSet plain = bH.getBits(plainText);
		String plainBits = Util.convertBitSetToString(plain, 64);
		
		for(int i = 0; i < plainBits.length(); i += DES.BLOCK_SIZE){
			String block = plainBits.length() < i + DES.BLOCK_SIZE ? 
								plainBits.substring(i) : 
								plainBits.substring(i, i + DES.BLOCK_SIZE);
			
			BitSet blockBs = bH.bsFromString(block);
			
			BitSet cipherBs = DES.encrypt(blockBs, desKeyBs );
			cipherBits += bH.convertBSToString(cipherBs, block.length());
		}
		
		HashMap<String, String> map = new HashMap<>();
		
		map.put(CIPHER_BITS, cipherBits);
		map.put(DES_KEY_BITS, desKeyBits );
		map.put(CIPHER_TEXT, bH.getTraductionText(cipherBits));
		map.put(DES_KEY_TEXT, bH.getTraductionText(desKeyBits));
		return map;
	}
	
	public HashMap<String, String> decryptDES( String cipherText, String desKeyText ){
		String cipherBits = bH.translateText(cipherText);
		String desKeyBits = bH.translateText(desKeyText);
		
		BitSet desKeyBs = bH.bsFromString(desKeyBits);
		
		String plainBits = "";
		for(int i = 0; i < cipherBits.length(); i+= DES.BLOCK_SIZE){
			String block = cipherBits.length() < i + DES.BLOCK_SIZE ?
								cipherBits.substring(i) : 
								cipherBits.substring(i, i + DES.BLOCK_SIZE);
			
			BitSet blockBs = bH.bsFromString(block);
			
			BitSet plainBs = DES.decrypt(blockBs, desKeyBs);
			plainBits += Util.convertBitSetToString(plainBs, 64);
		}
		
		HashMap<String, String> map = new HashMap<>();
		
		map.put(PLAIN_BITS, plainBits);
		map.put(PLAIN_TEXT, bH.getTextFromBinary(plainBits));
		return map;
	}

}
