package com.slipi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CodesMap extends HashMap<String, Code> {

    public void init() {
        //this.put("1D8", new Code("Rear Torque", 8, List.of(new DataField("Rear Torque Request Nm", 8,15, 2.0))));
        //this.put("257", new Code("Speedometer", 8, List.of(new DataField("Speed", 0, 12, 1.0))));
        //this.put("292", new Code("State of Charge", 8, List.of(new DataField("Stage of Charge %", 0, 10, 0.1))));
        // TODO: Add a better support for unsigned int like in this case (u32), use long is necessary?
        this.put("3B6", new Code("Odometer", 4, List.of(new DataField("Odometer", 0, 32, 0.001))));
        //this.put("3D2", new Code("TotalChargeDischarge", 8, Arrays.asList(new DataField("Discharge", 0, 16, 0.001), new DataField("Charge", 16, 16, 0.001))));
    }

}
