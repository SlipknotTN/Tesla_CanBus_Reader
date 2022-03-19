package com.slipi;

import java.util.HashMap;

public class CodesMap extends HashMap<String, Code> {

    public void init() {
        //TODO: Record new data and try 292 (state of charge)
        this.put("1D8", new Code("Rear Torque", 8));
        this.put("257", new Code("Speedometer", 8));
    }

}
