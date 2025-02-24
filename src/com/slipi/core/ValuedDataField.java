package com.slipi.core;

public class ValuedDataField extends DataField {

    // Metric value when available
    public double value;  // Int value + scaling and offset
    public int rawInt;  // Bytes decoded as int without any scaling or offset
    public String fieldBinStr;  // Field binary representation in big endian order for easier interpretation
    public String msgHexStr;  // Whole message excluding the prefix in the original order (Little Endian)
    public String msgBinStr; // Whole emssage excluding the prefix in big endian order for easier interpretation

    public ValuedDataField(DataField dataField, double value, int rawInt, String fieldBinStr, String msgHexStr, String msgBinStr) {
        super(dataField);
        this.value = value;
        this.rawInt = rawInt;
        this.fieldBinStr = fieldBinStr;
        this.msgHexStr = msgHexStr;
        this.msgBinStr = msgBinStr;
    }

}
