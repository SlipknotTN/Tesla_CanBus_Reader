package com.slipi;

public class DataField {
    String name;
    int startBit;
    int numBits;
    double scale;

    public DataField(String name, int startBit, int numBits, double scale) {
        this.name = name;
        this.startBit = startBit;
        this.numBits = numBits;
        this.scale = scale;
    }

    public DataField(String name, int startBit, int numBits) {
        this.name = name;
        this.startBit = startBit;
        this.numBits = numBits;
        this.scale = 1.0;
    }
}
