package com.anzusdev.cryptoun.businesslogic.rsa;


import java.math.BigInteger;


public class Key {
    
    private BigInteger first;
    private BigInteger second;

    public Key(BigInteger first, BigInteger second) {
        this.first = first;
        this.second = second;
    }

    public BigInteger getFirst() {
        return first;
    }

    public void setFirst(BigInteger first) {
        this.first = first;
    }

    public BigInteger getSecond() {
        return second;
    }

    public void setSecond(BigInteger second) {
        this.second = second;
    }
    
    
    
}
