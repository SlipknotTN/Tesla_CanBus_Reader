/**
 * CanBus signals interpretation references:
 * https://docs.google.com/document/d/1rNRspKuQbytbaNKUIf3JJVx0mrz-yWTdS_IFul9KBXs/edit?usp=sharing
 */
package com.slipi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.slipi.core.CodesMap;
import com.slipi.core.DataDecodingUtils;
import com.slipi.core.ValuedDataField;

public class Main {

    public static void main(String[] args) {
        CodesMap codesMap = new CodesMap();
        codesMap.init();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(
                    //"./dumps/RawLog 2025-02-15 13-57-36 - battery  - 42.txt"));
                    //"./dumps/RawLog 2025-02-18 19-08-00 - battery - 37.txt"));
                    "./dumps/RawLog 2025-02-22 12-59-12 - battery - charging - 36.txt"));
                    //"./dumps/RawLog 2023-06-17 12-38-16 - OLD - accel - slowdown.txt"));
                    //"./dumps/RawLog 2025-02-22 13-32-03 - battery - drive - 80.txt"));
            String line = reader.readLine();
            while (line != null) {
                ArrayList<ValuedDataField> valuedDataFields = DataDecodingUtils.extractData(line, codesMap, null);
                for (ValuedDataField valuedDataField : valuedDataFields) {
                    System.out.println(valuedDataField.key.name() + " -> double: " + valuedDataField.value +
                            ", raw int: " + valuedDataField.rawInt +
                            ", binary: " + valuedDataField.fieldBinStr +
                            ", msg hex: " + valuedDataField.msgHexStr +
                            ", msg bin: " + valuedDataField.msgBinStr);
                }
                if (valuedDataFields.size() > 0) {
                    System.out.println();
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
