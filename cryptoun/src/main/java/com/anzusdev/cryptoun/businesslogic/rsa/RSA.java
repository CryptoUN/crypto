package com.anzusdev.cryptoun.businesslogic.rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RSA {
    
    private Key privateKey;
    private Key publicKey;

    public ArrayList<BigInteger> encrypt(BigInteger p, BigInteger q,
            BigInteger e, BigInteger m, int blockSize) {

        ArrayList<BigInteger> blocks = new ArrayList<>();
        String num = m.toString();

        for (int i = 0; i < num.length(); i += blockSize) {
            String block = num.substring(i, Math.min(i + blockSize, num.length()));

            int diff = blockSize - block.length();

            if (diff > 0) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < diff; j++) {
                    sb.append("0");
                }

                sb.append(block);
                block = sb.toString();
            }

            blocks.add(new BigInteger(block));
        }
        
        HashMap<String, Key> keys = KeyGenerator.generate(p, q, e);
        
        this.privateKey = keys.get("private");
        this.publicKey = keys.get("public");
        
        ArrayList<BigInteger> c = new ArrayList<>();
        
        for(BigInteger mi : blocks) {
            c.add(mi.modPow(publicKey.getFirst(), publicKey.getSecond()));
        }
         
        return c;
    }
    
    public String decrypt(ArrayList<BigInteger> c) {

        StringBuilder sb = new StringBuilder();
        for(BigInteger ci: c){
            sb.append(ci.modPow(privateKey.getFirst(), privateKey.getSecond()));
        }
        
        return sb.toString();
    }
    

public static void main(String[] args) {
        RSA rsa = new RSA();
        BigInteger p = new BigInteger("47");
        BigInteger q = new BigInteger("71");
        BigInteger e = new BigInteger("79");
        String x = "999";
        BigInteger m = new BigInteger(x);
        int blockSize = 3;
        
        ArrayList c = rsa.encrypt(p, q, e, m, blockSize);
        System.out.println("message " + m.toString());
        System.out.println("Private key  d" + rsa.privateKey.getFirst() + " n " + rsa.privateKey.getSecond());
        System.out.println("Ciphertext: " + Arrays.toString(c.toArray()));
        String signature = rsa.decrypt(c);
        System.out.println("Decrypted message: " +signature);
        
        System.out.println("Signature verified: " + signature.equals(x));
    
    }

}
