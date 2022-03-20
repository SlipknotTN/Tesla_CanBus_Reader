/**
 * CanBus signals interpretation references:
 * https://docs.google.com/document/d/1rNRspKuQbytbaNKUIf3JJVx0mrz-yWTdS_IFul9KBXs/edit?usp=sharing
 */
package com.slipi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.BitSet;

public class Main {

    public static void main(String[] args) {
        CodesMap codesMap = new CodesMap();
        codesMap.init();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "./dumps/RawLog 2022-03-19 18-59-28.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (line.length() > 3) {
                    String codeStr = line.substring(0, 3);
                    String hexStr = line.substring(3);
                    if (codesMap.containsKey(codeStr)) {
                        Code codeObj = codesMap.get(codeStr);
                        if (hexStr.length() == codeObj.dlcBytes * 2) {
                            byte[] bytesFull = Utils.hexStringToByteArray(hexStr);
                            // Debug Hex code
                            //System.out.println("\nHex: " + hexStr);
                            for (DataField dataField : codeObj.dataFields) {
                                // Convert byte array to bits
                                BitSet bitsFull = BitSet.valueOf(bytesFull);
                                BitSet bitDataField = bitsFull.get(dataField.startBit, dataField.startBit + dataField.numBits);
                                // Debug binary code
                                /*for (int i=0; i<bitDataField.length(); i++) {
                                    System.out.print(bitDataField.get(i)?1:0);
                                }*/
                                // The ByteArray doesn't include the trailing zeros
                                byte[] bytesDataFieldFromBitSet = bitDataField.toByteArray();
                                // Pad bytes array
                                byte[] bytesDataField = Arrays.copyOf(bytesDataFieldFromBitSet, Integer.BYTES);
                                // Data are in LITTLE_ENDIAN format
                                ByteBuffer byteBufferDataField = ByteBuffer.allocate(Integer.BYTES);
                                byteBufferDataField.order(ByteOrder.LITTLE_ENDIAN);
                                byteBufferDataField.put(bytesDataField);
                                byteBufferDataField.flip();
                                try {
                                    int valueInt = byteBufferDataField.getInt();
                                    double value = valueInt * dataField.scale + dataField.offset;
                                    System.out.println("\n" + dataField.name + ": " + value + " " + dataField.unit);
                                }
                                catch (BufferUnderflowException e) {
                                    System.err.println("\n" + dataField.name + ": " + e + " -> " + e.getMessage());
                                }

                            }
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
