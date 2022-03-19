/**
 * CanBus signals interpretation references:
 * https://docs.google.com/document/d/1rNRspKuQbytbaNKUIf3JJVx0mrz-yWTdS_IFul9KBXs/edit?usp=sharing
 */
package com.slipi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        CodesMap codesMap = new CodesMap();
        codesMap.init();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "RawLog 2022-03-18 23-12-04.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.length() > 3) {
                    String codeStr = line.substring(0, 3);
                    String hexStr = line.substring(3);
                    // System.out.println(code + ": " + byte_number);
                    if (codesMap.containsKey(codeStr)) {
                        Code codeObj = codesMap.get(codeStr);
                        if (hexStr.length() == codeObj.dlc_bytes * 2) {
                            byte[] bytesData = Utils.hexStringToByteArray(hexStr);
                            ByteBuffer byteBuffer = ByteBuffer.allocate(codeObj.dlc_bytes / 2);
                            // Read only the first 4 bytes
                            byteBuffer.put(Arrays.copyOfRange(bytesData, 0, 4));
                            int dataValue = byteBuffer.getInt();
                            System.out.println(codeObj.description + ": " + dataValue);
                        }
                    }
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
