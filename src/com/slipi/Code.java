package com.slipi;



public class Code {

    String description;
    int dlc_bytes;

    public Code(String description, int dlc_bytes) {
        this.description = description;
        // This is the number of bytes sent, the actual data could be only a subset
        this.dlc_bytes = dlc_bytes;
    }

}
