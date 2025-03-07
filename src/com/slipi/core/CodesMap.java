package com.slipi.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CodesMap extends HashMap<String, Code> {

    public void init() {
        // Not sure about absolute values, but magnitude and sign are correct
        // Good approximation of regen/discharge bar although this is more reactive at very low speed
        // (more reactive to pedal at start w.r.t. the tablet bar)
        this.put("1D8", new Code("Rear Torque", 8, Arrays.asList(
                new DataField(DataKeyEnumOBD.REAR_TORQUE_REQUEST.name(), "Nm", 8, 13, true, 0.222),
                // Use this for regen/discharge bar
                new DataField(DataKeyEnumOBD.REAR_TORQUE.name(), "Nm", 21, 13, true, 0.222)
        )));
        // Tested
        this.put("118", new Code("Drive state and pedals", 8, Arrays.asList(
                // 0-idle, 1-charge, 2-park, 5-driving
                new DataField(DataKeyEnumOBD.DRIVE_STATE.name(), "", 16, 3, false),
                // Unit? Boolean?! 1 - BrakePress, 2 - No?. Always 2.0, not useful
                new DataField(DataKeyEnumOBD.BRAKE_PRESS.name(), "", 19, 2, false),
                // 1-park, 2-reverse, 3-neutral, 4-drive, 7-idle
                new DataField(DataKeyEnumOBD.GEAR.name(), "", 21, 3, false),
                new DataField(DataKeyEnumOBD.ACCELERATOR_PEDAL_POSITION.name(), "%", 32, 8, false, 0.4)
        )));
        // Tested: UI speed is the same of the tablet
        this.put("257", new Code("Speedometer", 8, Arrays.asList(
                new DataField(DataKeyEnumOBD.SIGNED_SPEED.name(), "km/h", 12, 12, false, 0.08, -40), // Different formula for mph and kph
                new DataField(DataKeyEnumOBD.UI_SPEED.name(), "mph or km/h", 24, 9, false),
                new DataField(DataKeyEnumOBD.MPH_KPH_FLAG.name(), "", 33, 1, false)  // 1.0 km/h, 0.0 mph
        )));
        // No correspondence to table
        this.put("292", new Code("SOC state 292", 8, List.of(
                // Correct SOC level before 2024.20
                new DataField(DataKeyEnumOBD.UI_SOC_292.name(), "%", 0, 10, false, 0.1),
                new DataField(DataKeyEnumOBD.MIN_SOC_292.name(), "%", 10, 10, false, 0.1),
                new DataField(DataKeyEnumOBD.MAX_SOC_292.name(), "%", 20, 10, false, 0.1),
                new DataField(DataKeyEnumOBD.AVG_SOC_292.name(), "%", 30, 10, false, 0.1),
                new DataField(DataKeyEnumOBD.FULL_PACK_WHEN_NEW_ENERGY.name(), "kWh", 40, 10, false, 0.1)
        )));
        // No data saved by ScanMyTesla, but available.
        this.put("293", new Code("Steering and traction modes", 8, List.of(
                // 0 - comfort, 1 - standard, 2 - sport
                new DataField(DataKeyEnumOBD.STEERING_MODE.name(), "", 0, 2, false),
                // 0 - normal, 1 - slippery
                new DataField(DataKeyEnumOBD.TRACTION_MODE.name(), "", 2, 3, false)
        )));
        // TODO: Manage the ENERGY_STATUS_INDEX before returning the value
        this.put("352", new Code("BMS energy status", 8, List.of(
                new DataField(DataKeyEnumOBD.ENERGY_STATUS_INDEX.name(), "", 0, 2, false),
                // Valid for index 0 or 1, index 2 is unknown. BMS_energyToChargeComplete seems off, but it wasn't tested during charge.
                // Index 0 NOMINAL_FULL_PACK is the same of ScanMyTesla
                // Index 1 ENERGY BUFFER is the same as ScanMyTesla if scale 0.01 is used, so we need to divide it by 2.0 to get the correct value
                new DataField(DataKeyEnumOBD.NOMINAL_FULL_PACK_ENERGY_OR_DOUBLE_ENERGY_BUFFER.name(), "kWh", 16, 16, false, 0.02),
                // Index 0 - Same as ScanMyTesla
                new DataField(DataKeyEnumOBD.NOMINAL_ENERGY_REMAINING.name(), "kWh", 32, 16, false, 0.02),
                // Index 0 (48, 16) is the same of Index 1 (32, 16). Seems correct, slightly lower than NOMINAL_ENERGY_REMAINING (not available in ScanMyTesla)
                new DataField(DataKeyEnumOBD.IDEAL_ENERGY_REMAINING.name(), "kWh", 48, 16, false, 0.02),
                // Valid only with index 1, not tested
                new DataField(DataKeyEnumOBD.FULLY_CHARGED.name(), "", 15, 1, false),
                // Valid only with index 2, this is the correct SOC level after 2024.20
                new DataField(DataKeyEnumOBD.UI_SOC_352.name(), "%", 53, 10, false, 0.1)
        )));
        // Tested
        this.put("3B6", new Code("Odometer", 4, List.of(
                new DataField(DataKeyEnumOBD.ODOMETER.name(), "km", 0, 32, false, 0.001)
        )));
        //TODO: Test 382. Possible high beam? Values changes from 382471C280000 to 382471C270000. Not described in odb
        //TODO: Test 3E2 for lights. Values change from 3E20400800A00C004 to 3E20400800A00C004 and 3E20500800A00C004
        //TODO: Test beams with 273 (627 decimal)
        // From new obc
        /*BO_ 627 UI_vehicleControl: 8 VEH
         SG_ UI_accessoryPowerRequest: 0|1@1+ (1,0) [0|0] "" X
         SG_ UI_alarmEnabled: 20|1@1+ (1,0) [0|0] "" X
         SG_ UI_ambientLightingEnabled: 40|1@1+ (1,0) [0|0] "" X
         SG_ UI_autoFoldMirrorsOn: 52|1@1+ (1,0) [0|0] "" X
         SG_ UI_autoHighBeamEnabled: 41|1@1+ (1,0) [0|0] "" X
         SG_ UI_childDoorLockOn: 16|1@1+ (1,0) [0|0] "" X
         SG_ UI_displayBrightnessLevel: 32|8@1+ (0.5,0) [0|0] "%" X
         SG_ UI_domeLightSwitch: 59|2@1+ (1,0) [0|0] "" X
         SG_ UI_driveStateRequest: 62|1@1+ (1,0) [0|0] "" X
         SG_ UI_frontFogSwitch: 3|1@1+ (1,0) [0|0] "" X
         SG_ UI_frontLeftSeatHeatReq: 42|2@1+ (1,0) [0|0] "" X
         SG_ UI_frontRightSeatHeatReq: 44|2@1+ (1,0) [0|0] "" X
         SG_ UI_frunkRequest: 5|1@1+ (1,0) [0|0] "" X
         SG_ UI_globalUnlockOn: 15|1@1+ (1,0) [0|0] "" X
         SG_ UI_honkHorn: 61|1@1+ (1,0) [0|0] "" X
         SG_ UI_intrusionSensorOn: 21|1@1+ (1,0) [0|0] "" X
         SG_ UI_lightSwitch: 1|2@1+ (1,0) [0|0] "" X
         SG_ UI_lockRequest: 17|3@1+ (1,0) [0|0] "" X
         SG_ UI_mirrorDipOnReverse: 53|1@1+ (1,0) [0|0] "" X
         SG_ UI_mirrorFoldRequest: 24|2@1+ (1,0) [0|0] "" X
         SG_ UI_mirrorHeatRequest: 26|1@1+ (1,0) [0|0] "" X
         SG_ UI_powerOff: 31|1@1+ (1,0) [0|0] "" X
         SG_ UI_rearCenterSeatHeatReq: 48|2@1+ (1,0) [0|0] "" X
         SG_ UI_rearFogSwitch: 23|1@1+ (1,0) [0|0] "" X
         SG_ UI_rearLeftSeatHeatReq: 46|2@1+ (1,0) [0|0] "" X
         SG_ UI_rearRightSeatHeatReq: 50|2@1+ (1,0) [0|0] "" X
         SG_ UI_rearWindowLockout: 63|1@1+ (1,0) [0|0] "" X
         SG_ UI_remoteClosureRequest: 54|2@1+ (1,0) [0|0] "" X
         SG_ UI_remoteStartRequest: 27|3@1+ (1,0) [0|0] "" X
         SG_ UI_seeYouHomeLightingOn: 30|1@1+ (1,0) [0|0] "" X
         SG_ UI_steeringBacklightEnabled: 8|1@1+ (1,0) [0|0] "" X
         SG_ UI_steeringButtonMode: 9|3@1+ (1,0) [0|0] "" X
         SG_ UI_stop12vSupport: 22|1@1+ (1,0) [0|0] "" X
         SG_ UI_summonActive: 4|1@1+ (1,0) [0|0] "" X
         SG_ UI_unlockOnPark: 14|1@1+ (1,0) [0|0] "" X
         SG_ UI_walkAwayLock: 13|1@1+ (1,0) [0|0] "" X
         SG_ UI_walkUpUnlock: 12|1@1+ (1,0) [0|0] "" X
         SG_ UI_wiperMode: 6|2@1+ (1,0) [0|0] "" X
         SG_ UI_wiperRequest: 56|3@1+ (1,0) [0|0] "" X*/
    }
}
