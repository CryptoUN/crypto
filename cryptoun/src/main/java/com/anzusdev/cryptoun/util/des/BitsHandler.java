package com.anzusdev.cryptoun.util.des;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;

import static java.nio.charset.StandardCharsets.*;

public class BitsHandler {
	
	public static String[] zeroTable = new String[]{
		"α", "β", "γ", "δ", "ε", "ζ", "η", "θ", "ι", "κ", 
		"λ", "μ", "ν", "ξ", "ᨎ", "π", "ρ", "ς", "σ", "τ", 
		"υ", "φ", "χ", "ψ", "ω", "ᨀ", "ᨁ", "ᨂ", "ᨃ", "ᨄ",
		"ᨅ", "⦱"
	};
	
	public static String[] middleTable = new String[]{
		"⦿", "♔", "♕", "♖", "♗", "♘", "♙", "♚", "♛", "♜", 
		"♝", "♞", "♟", "♠", "♡", "♢", "♣", "♤", "♥", "♦", 
		"♧", "❶", "❷", "❸", "❹", "❺", "❻", "❼", "❽", "❾", 
		"❿", "➁", "⦴"
	};
	
	public static char lastChar = '⦰';
	
	public static char sCommaChar = '⧑';
	public static char dCommaChar = '⧒';
	public static char bslashChar = '⧓';
	
	public BitSet getBits(String text){
	    byte[] bytes = text.getBytes(ISO_8859_1);
	    for(int i = 0 ; i< bytes.length; i++)
	    	bytes[i] = (byte) (bytes[i] & 0xff);
	    
	    BitSet bitset = BitSet.valueOf(bytes);
	    
	    return bitset;
	}
	
	public String getBinaryText(BitSet bits){
		String binary = "";
		for(int i = 0; i < bits.length(); i++)
			binary += bits.get(bits.length() - i - 1) ? "1" : "0";
	    return binary;
	}
	
	public String getText(BitSet bits){
	    String binary = getBinaryText(bits);
		return new String(new BigInteger(binary, 2).toByteArray());
	}
	
	public String getBinaryFromText(String text){
	    return getBinaryText( getBits(text) );
	}
	
	public String getTextFromBinary(String binary){
		int textLength = binary.length()/8;
	    int pos = 0;
	    byte[] buffer = new byte[ textLength ];
	    for(int j = 0; j < textLength; j++){
	        String temp = binary.substring(pos,pos+8);
	        buffer[textLength - 1 - j] = (byte) Integer.parseInt(temp, 2);
	        pos+=8;
	    }
	    String result = new String(buffer, ISO_8859_1);
	    return result.replace("\u0000", "");
	}
	
	public String getTraductionText(String binary){
		if(binary.length() == 0)
			return "";
		int pos = 0;
	    String result = "";
	    if(binary.length() % 8 != 0)
	    	binary = fillString(binary);
	    
	    int textLength = binary.length()/8;
	    byte[] buffer = new byte[ textLength ];
	    for(int j = 0; j < textLength; j++){
	        String temp = binary.substring(pos, pos + 8);
	        buffer[j] = (byte) Integer.parseInt(temp, 2);
	        int index = buffer[j] & 0xFF;
	        if( ((index > 31 && index < 127) && (index != 34 && index != 39 && index != 92)) || (index > 159 && index != 173) )
	        	result += new String( new byte[]{buffer[j]}, ISO_8859_1 );
	        else if( index <= 31 )
	        	result += zeroTable[index];
	        else if( index >= 127 && index <= 159 )
	        	result += middleTable[index - 127];
	        else if (index == 173)
	        	result += String.valueOf(lastChar);
	        else if (index == 34)
	        	result += String.valueOf(dCommaChar);
	        else if (index == 39)
	        	result += String.valueOf(sCommaChar);
	        else
	        	result += String.valueOf(bslashChar);
	        pos += 8;
	    }
		return result;
	}
	
	public String fillString(String binary){
		int lastIndex = binary.length() / 8;
		int numberOfChars = 8 - (binary.length() - lastIndex * 8);
		String filling = "";
		for(int i = 0; i < numberOfChars; i++)
			filling += "0";
		String filled = binary.substring(0, lastIndex) + binary.substring(lastIndex) + filling;
		return filled;
	}
	
	public String translateText(String text){
		byte[] bytesInt = new byte[text.length()];
		int index = text.length()-1;
	    for(int i = 0; i < text.length(); i++){
	    	int indexZero = Arrays.asList(zeroTable).indexOf( text.substring(i,i+1) );
	    	int indexMiddle = Arrays.asList(middleTable).indexOf( text.substring(i,i+1) );
	    	
	    	if( indexZero != -1 )
	    		bytesInt[index-i] = (byte) indexZero;
	    	
	    	else if( indexMiddle != -1 )
	    		bytesInt[index-i] = (byte) (127 + indexMiddle);
	    	
	    	else if( text.charAt(i) == lastChar )
    			bytesInt[index-i] = (byte) 173;
	    	
	    	else if ( text.charAt(i) == dCommaChar )
	    		bytesInt[index-i] = (byte) 34;
	        else if ( text.charAt(i) == sCommaChar )
	        	bytesInt[index-i] = (byte) 39;
	        else if ( text.charAt(i) == bslashChar )
	        	bytesInt[index-i] = (byte) 92;
	    	
	    	else
	    		bytesInt[index-i] = text.substring(i,i+1).getBytes(ISO_8859_1)[0];
	    }
	    
	    BitSet bitset = BitSet.valueOf(bytesInt);
	    return convertBSToString(bitset, text.length()*8);
	}	
	
	public BitSet bsFromString(String s) {
		BitSet bs = new BitSet(s.length());
		for(int i = 0; i < s.length(); i++)
			bs.set(i, s.charAt(s.length() - 1 - i) == '1');
        return bs;
    }
    
    public BitSet getFilling(int n){
    	String filling = "";
    	for(int i = 0; i < n; i++)
    		filling += "0";
    	return bsFromString(filling);
    }
    
    public BitSet concatenateBitStrings(BitSet leftBitString, BitSet rightBitString, int n) {
    	int dim = leftBitString.length() + n;
        BitSet bs = new BitSet(dim);
        for(int i = 0; i < dim; i++){
        	if(i < n){
        		try{
        			bs.set(i, rightBitString.get(i));
        		}catch(IndexOutOfBoundsException e){
        			bs.set(i, false);
        		}
        	}
        	else
        		bs.set(i, leftBitString.get(i - n));
        }
        return bs;
    }
    
    public String convertBSToString(BitSet bits, int numberOfBits){
		String binary = "";
		for(int i = 0; i < numberOfBits; i++){
			try{
				binary += bits.get(numberOfBits - i - 1) ? "1" : "0";
			}catch(IndexOutOfBoundsException e){
				binary += "0";
			}
		}
	    return binary;
	}
    
    public int[] convertBitStringToIntArray(String bitString) {
        int blockSize = 8;
        int[] intArray = new int[bitString.length()/blockSize];
        String subsection;
        int start, end;
        for (int i = 0; i < intArray.length; ++i) {
            start = i * blockSize;
            end = start + blockSize;
            subsection = bitString.substring(start, end);
            intArray[i] = Integer.parseInt(subsection, 2);
        }
        return intArray;
    }

    public String convertIntArrayToBitString(int[] intArray) {
        int blockSize = 8;
        String element;
        StringBuilder bitString = new StringBuilder();
        for (int i = 0; i < intArray.length; ++i) {
            element = Integer.toBinaryString(intArray[i]);
            element = padZerosToLeft(element, blockSize);
            bitString.append(element.substring(element.length() - blockSize, element.length()));
        }
        return  bitString.toString();
    }
    
    private static String padZerosToLeft(String string, int desiredSize) {
        if (string.length() == desiredSize)
            return string;
        StringBuilder paddedString = new StringBuilder(string).reverse();
        for (int i = 0; i < desiredSize - string.length(); i++) {
            paddedString.append('0');
        }
        return paddedString.reverse().toString();
    }
	
}
