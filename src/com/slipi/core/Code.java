package com.slipi.core;


import java.util.List;

public class Code {

    String description;
    int dlcBytes;
    List<DataField> dataFields;

    public Code(String description, int dlcBytes, List<DataField> dataFields) {
        this.description = description;
        // This is the number of bytes sent, the actual data could be only a subset
        this.dlcBytes = dlcBytes;
        this.dataFields = dataFields;
    }

}
