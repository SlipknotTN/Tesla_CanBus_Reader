package com.slipi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CodesMap extends HashMap<String, Code> {

    public void init() {
        this.put("1D8", new Code("Rear Torque", 8, Arrays.asList(
                new DataField("Rear Torque Request", "Nm", 8,15, true, 0.1),
                new DataField("Rear Torque", "Nm", 24,13, true, 0.25)
        )));
        this.put("257", new Code("Speedometer", 8, Arrays.asList(
                new DataField("Signed Speed", "km/h", 12, 12, true, 0.08, -40), // km/h miles/h bit. Different formula for mph and kph
                new DataField("UI Speed", "km/h", 24, 8, false)
        )));
        this.put("292", new Code("State of Charge", 8, List.of(
                new DataField("Stage of Charge %", "%", 0, 10, false,0.1)
        )));
        this.put("3B6", new Code("Odometer", 4, List.of(
                new DataField("Odometer", "km", 0, 32, false, 0.001)
        )));
        this.put("3D2", new Code("TotalChargeDischarge", 8, Arrays.asList(
                new DataField("Discharge", "kWh",0, 16, false, 0.001),
                new DataField("Charge", "kWh", 16, 16, false, 0.001)
        )));
    }

}
