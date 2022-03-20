package com.slipi;

public class DataField {
    String name;
    String unit;
    int startBit;
    int numBits;
    // Not used currently
    boolean signed;
    double scale;
    double offset;

    public DataField(String name, String unit, int startBit, int numBits, boolean signed) {
        this.name = name;
        this.unit = unit;
        this.startBit = startBit;
        this.numBits = numBits;
        this.signed = signed;
        this.scale = 1.0;
        this.offset = 0.0;
    }

    public DataField(String name, String unit, int startBit, int numBits, boolean signed, double scale) {
        this.name = name;
        this.unit = unit;
        this.startBit = startBit;
        this.numBits = numBits;
        this.signed = signed;
        this.scale = scale;
        this.offset = 0.0;
    }

    public DataField(String name, String unit, int startBit, int numBits, boolean signed, double scale, double offset) {
        this.name = name;
        this.unit = unit;
        this.startBit = startBit;
        this.numBits = numBits;
        this.signed = signed;
        this.scale = scale;
        this.offset = offset;
    }
}
