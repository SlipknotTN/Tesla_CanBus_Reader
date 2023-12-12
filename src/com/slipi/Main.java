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

    static private String hexToBin(String hex){
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }

    public static void main(String[] args) {
        CodesMap codesMap = new CodesMap();
        codesMap.init();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "./dumps/RawLog 2023-06-17 12-38-16 - accel - slowdown.txt"));
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
                            //System.out.println("\nFull Hex: " + hexStr);
                            String binStr = Main.hexToBin(hexStr);

                            for (DataField dataField : codeObj.dataFields) {

                                // Convert byte array to bits
                                BitSet bitsFull = BitSet.valueOf(bytesFull);
                                BitSet bitDataField = bitsFull.get(dataField.startBit, dataField.startBit + dataField.numBits);

                                // The ByteArray doesn't include the trailing zeros
                                byte[] bytesDataFieldFromBitSet = bitDataField.toByteArray();
                                // Pad bytes array
                                byte[] bytesDataField = Arrays.copyOf(bytesDataFieldFromBitSet, Integer.BYTES);
                                // Data are in LITTLE_ENDIAN format
                                ByteBuffer byteBufferDataField = ByteBuffer.allocate(Integer.BYTES);
                                byteBufferDataField.order(ByteOrder.LITTLE_ENDIAN);
                                byteBufferDataField.put(bytesDataField);
                                byteBufferDataField.flip();

                                //BitSet indexes go from less significant bit to most, reverse order
                                System.out.print("\nBinary: ");
                                StringBuilder bigEndianBits = new StringBuilder();
                                for (int i = dataField.numBits - 1; i >= 0; i--) {
                                    String bitValue = (bitDataField.get(i) ? "1" : "0");
                                    System.out.print(bitValue);
                                    if (i % 4 == 0) {
                                        System.out.print(" ");
                                    }
                                    bigEndianBits.append(bitValue);
                                }
                                System.out.println();

                                try {

                                    Integer valueInt;
                                    // In case of signed integer check the first bit to see if it is negative
                                    if (dataField.signed && Character.toString(bigEndianBits.charAt(0)).equals("1")) {
                                        // We don't always have exactly 32 bits, so we need to pass
                                        // from unsigned int and custom two's complement
                                        int integerValue = Integer.parseUnsignedInt(bigEndianBits.toString(), 2);
                                        integerValue -= 1;
                                        String binStrMinusOne = Integer.toBinaryString(integerValue);
                                        StringBuilder negBitsBuilder = new StringBuilder();
                                        // Two's complement
                                        for (int i = 0; i < binStrMinusOne.length(); i++) {
                                            if (Character.toString(binStrMinusOne.charAt(i)).equals("1")) {
                                                negBitsBuilder.append("0");
                                            } else {
                                                negBitsBuilder.append("1");
                                            }
                                        }
                                        String negBits = negBitsBuilder.toString();
                                        valueInt = -Integer.parseUnsignedInt(negBits, 2);
                                    }
                                    else {
                                        valueInt = byteBufferDataField.getInt();
                                    }
                                    // Assuming offset sign is not affected by valueInt sign
                                    double value = valueInt * dataField.scale + dataField.offset;
                                    System.out.println(dataField.name + " int: " + valueInt + ", double: " + value + " " + dataField.unit);
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
