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
                    // System.out.println(code + ": " + byte_number);
                    if (codesMap.containsKey(codeStr)) {
                        Code codeObj = codesMap.get(codeStr);
                        if (hexStr.length() == codeObj.dlcBytes * 2) {
                            byte[] bytesFull = Utils.hexStringToByteArray(hexStr);
                            System.out.println("\n" + hexStr);
                            for (DataField dataField : codeObj.dataFields) {
                                // Convert byte array to bits
                                BitSet bitsFull = BitSet.valueOf(bytesFull);
                                BitSet bitDataField = bitsFull.get(dataField.startBit, dataField.startBit + dataField.numBits);
                                // Num bytes (closest multiple of 8 * bits)
                                int numBytesDataField = dataField.numBits  / 8 + ((dataField.numBits  % 8 == 0) ? 0 : 1);
                                for (int i=0; i<32; i++) {
                                    System.out.print(bitDataField.get(i)?1:0);
                                }
                                byte[] bytesDataField = bitDataField.toByteArray();
                                // FIXME: Use the necessary type for every data field?!
                                ByteBuffer byteBufferDataField = ByteBuffer.allocate(Integer.BYTES);
                                byteBufferDataField.order(ByteOrder.LITTLE_ENDIAN);
                                //byteBufferDataField.put(bytesDataField);  // Only 3 bytes, missing last zero. But since it is little endian it is important
                                byteBufferDataField.put(bytesFull);
                                byteBufferDataField.flip();
                                try {
                                    int valueInt = byteBufferDataField.getInt();
                                    double value = valueInt * dataField.scale;
                                    System.out.println(dataField.name + ": " + value);
                                }
                                catch (BufferUnderflowException e) {
                                    System.err.println(dataField.name + ": " + e + " -> " + e.getMessage());
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
