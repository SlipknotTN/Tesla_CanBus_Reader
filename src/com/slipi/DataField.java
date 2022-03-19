package com.slipi;

public class DataField {
    String name;
    int numbits;
    float scale;

    public DataField(String name, int numBits, float scale) {
        this.name = name;
        this.numbits = numBits;
        this.scale = scale;
    }

    public DataField(String name, int numBits) {
        this.name = name;
        this.numbits = numBits;
        this.scale = 1.0f;
    }
}
