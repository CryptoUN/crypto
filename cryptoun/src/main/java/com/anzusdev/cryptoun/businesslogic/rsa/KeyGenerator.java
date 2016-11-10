package com.anzusdev.cryptoun.businesslogic.rsa;


import java.math.BigInteger;
import java.util.HashMap;



public class KeyGenerator {
    
    public static HashMap<String, Key> generate(BigInteger p, BigInteger q, BigInteger e) {
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)));
        
        if(e.compareTo(BigInteger.ONE) < 0 || e.compareTo(phi) > 0) {
            throw new IllegalArgumentException("e should be between 1 and phi");
        } else if (e.gcd(phi).compareTo(BigInteger.ONE) != 0) {
            throw new IllegalArgumentException("e should be a relative prime to phi");
        }
        
        BigInteger d = EEA.getY(phi, e);
        if(d.compareTo(BigInteger.ZERO) < 0) {
            d = phi.add(d);
        }

        HashMap keys = new HashMap<String, Key>();
        
        keys.put("private", new Key(d, n));
        keys.put("public", new Key(e, n));
        
        return keys;
    }
   
}
