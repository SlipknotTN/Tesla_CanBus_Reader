package com.slipi.core;

public class DataField {

    public DataKeyEnumOBD key;
    public String unit;
    public int startBit;
    public int numBits;
    // Not used currently
    public boolean signed;
    public double scale;
    public double offset;

    public DataField(DataField dataField) {
        this.key = dataField.key;
        this.unit = dataField.unit;
        this.startBit = dataField.startBit;
        this.numBits = dataField.numBits;
        this.signed = dataField.signed;
        this.scale = dataField.scale;
        this.offset = dataField.offset;
    }

    public DataField(String keyStr, String unit, int startBit, int numBits, boolean signed) {
        this.key = DataKeyEnumOBD.valueOf(keyStr);
        this.unit = unit;
        this.startBit = startBit;
        this.numBits = numBits;
        this.signed = signed;
        this.scale = 1.0;
        this.offset = 0.0;
    }

    public DataField(String keyStr, String unit, int startBit, int numBits, boolean signed, double scale) {
        this.key = DataKeyEnumOBD.valueOf(keyStr);
        this.unit = unit;
        this.startBit = startBit;
        this.numBits = numBits;
        this.signed = signed;
        this.scale = scale;
        this.offset = 0.0;
    }

    public DataField(String keyStr, String unit, int startBit, int numBits, boolean signed, double scale, double offset) {
        this.key = DataKeyEnumOBD.valueOf(keyStr);
        this.unit = unit;
        this.startBit = startBit;
        this.numBits = numBits;
        this.signed = signed;
        this.scale = scale;
        this.offset = offset;
    }
}
