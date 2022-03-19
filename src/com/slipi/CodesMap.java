package com.slipi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CodesMap extends HashMap<String, Code> {

    public void init() {
        //TODO: Record new data and try 292 (state of charge)
        this.put("1D8", new Code("Rear Torque", 8, List.of(new DataField("RearTorque", 16))));
        this.put("257", new Code("Speedometer", 8, List.of(new DataField("Speed", 12, 1.0f))));
        this.put("3D2", new Code("TotalChargeDischarge", 8, Arrays.asList(
                new DataField("Discharge", 16, 0.001f), new DataField("Charge", 16, 0.001f))));
    }

}
