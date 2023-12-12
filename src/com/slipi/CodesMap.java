package com.slipi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CodesMap extends HashMap<String, Code> {

    public void init() {
        // To be compared with ScanMyTesla
        this.put("1D8", new Code("Rear Torque", 8, Arrays.asList(
                new DataField("Rear Torque Request", "Nm", 8, 13, true, 0.222),
                new DataField("Rear Torque", "Nm", 21, 13, true, 0.222)
        )));
        // To be tested in car, but it seems correct
        this.put("257", new Code("Speedometer", 8, Arrays.asList(
                new DataField("Signed Speed", "km/h", 12, 12, false, 0.08, -40), // km/h miles/h bit. Different formula for mph and kph
                new DataField("UI Speed", "km/h", 24, 9, false)
        )));
        // Tested
        this.put("292", new Code("State of Charge", 8, List.of(
                new DataField("Stage of Charge", "%", 0, 10, false,0.1),
                new DataField("Min SOC", "%", 10, 10, false,0.1),
                new DataField("Max SOC", "%", 20, 10, false,0.1),
                new DataField("Average SOC", "%", 30, 10, false,0.1)
        )));
        // To be tested: no data
        this.put("293", new Code("Steering and traction modes", 8, List.of(
                new DataField("Steering mode", "", 0, 2, false),
                new DataField("Traction mode", "", 2, 3, false)
        )));
        // Tested
        this.put("3B6", new Code("Odometer", 4, List.of(
                new DataField("Odometer", "km", 0, 32, false, 0.001)
        )));
        // Accumulated charge and discharge
        this.put("3D2", new Code("TotalChargeDischarge", 8, Arrays.asList(
                new DataField("Discharge", "kWh",0, 32, false, 0.001),
                new DataField("Charge", "kWh", 32, 32, false, 0.001)
        )));
    }

}
