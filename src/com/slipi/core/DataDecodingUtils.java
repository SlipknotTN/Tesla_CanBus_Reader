package com.slipi.core;


import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class DataDecodingUtils {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

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

    public static ArrayList<ValuedDataField> extractData(String line, CodesMap codesMap, OutputStream logOutputStream) {
        ArrayList<ValuedDataField> valueDataFields = new ArrayList<>();
        if (line.length() > 3) {
            String codeStr = line.substring(0, 3);
            String msgHexStr = line.substring(3);
            if (codesMap.containsKey(codeStr)) {
                Code codeObj = codesMap.get(codeStr);
                // Check that the size of the message is the one expected for the code prefix
                if (msgHexStr.length() == codeObj.dlcBytes * 2) {
                    byte[] bytesMsg = DataDecodingUtils.hexStringToByteArray(msgHexStr);
                    // Convert byte array to bits
                    BitSet bitsMsg = BitSet.valueOf(bytesMsg);
                    // Convert the full message bits from little endian to big endian for debug purpose
                    StringBuilder msgBigEndianBits = new StringBuilder();
                    for (int i = bitsMsg.length(); i >= 0; i--) {
                        String bitValue = (bitsMsg.get(i) ? "1" : "0");
                        msgBigEndianBits.append(bitValue);
                    }
                    for (DataField dataField : codeObj.dataFields) {
                        // Get the bits for the field
                        BitSet bitsField = bitsMsg.get(dataField.startBit, dataField.startBit + dataField.numBits);

                        // BitSet indexes go from less significant bit to most (little endian),
                        // so we need to reverse the order to get the correct bits representation
                        // to apply the manual two's complement
                        StringBuilder fieldBigEndianBits = new StringBuilder();
                        for (int i = dataField.numBits - 1; i >= 0; i--) {
                            String bitValue = (bitsField.get(i) ? "1" : "0");
                            fieldBigEndianBits.append(bitValue);
                        }

                        // Use ByteBuffer to interpret the data AS IS, without any manipulation
                        // Data are in LITTLE_ENDIAN format
                        // The ByteArray doesn't include the trailing zeros
                        byte[] bytesFieldFromBitSet = bitsField.toByteArray();
                        // Pad bytes array
                        byte[] bytesField = Arrays.copyOf(bytesFieldFromBitSet, Integer.BYTES);
                        ByteBuffer byteBufferDataField = ByteBuffer.allocate(Integer.BYTES);
                        byteBufferDataField.order(ByteOrder.LITTLE_ENDIAN);
                        byteBufferDataField.put(bytesField);
                        byteBufferDataField.flip();

                        try {

                            int valueIntDecoded;

                            if (dataField.signed) {

                                // In case of signed integer check the first bit to see if it is negative
                                if (Character.toString(fieldBigEndianBits.charAt(0)).equals("1")) {
                                    // We don't always have exactly 32 bits, so we need to pass
                                    // from unsigned int and custom two's complement
                                    int integerValue = Integer.parseUnsignedInt(fieldBigEndianBits.toString(), 2);
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
                                    valueIntDecoded = -Integer.parseUnsignedInt(negBits, 2);
                                }
                                // Signed integer positive, interpret the value with original bits ordering
                                else {
                                    valueIntDecoded = byteBufferDataField.getInt();
                                }
                            }
                            // Unsigned integer, interpret the value with original bits ordering
                            else {
                                valueIntDecoded = byteBufferDataField.getInt();
                            }
                            // Assuming offset sign is not affected by valueInt sign
                            double valueDoubleProcessed = valueIntDecoded * dataField.scale + dataField.offset;
                            // Save the object to return
                            ValuedDataField valuedDataField = new ValuedDataField(
                                    dataField,
                                    valueDoubleProcessed,
                                    valueIntDecoded,
                                    fieldBigEndianBits.toString(),
                                    msgHexStr,
                                    msgBigEndianBits.toString()
                            );
                            valueDataFields.add(valuedDataField);

                            // Optional log to file
                            String strDebugMessage = valuedDataField.key.name() + ": " + valuedDataField.value + " " + valuedDataField.unit;
                            if (logOutputStream != null) {
                                logOutputStream.write((strDebugMessage + "\n").getBytes());
                            }
                        }
                        catch (BufferUnderflowException e) {
                            System.err.println("\n" + dataField.key.name() + ": " + e + " -> " + e.getMessage());
                        }
                        catch (java.io.IOException e) {
                            System.err.println("\n" + dataField.key.name() + ": " + e + " -> " + e.getMessage());
                        }
                    }
                }
            }
        }
        return valueDataFields;
    }

}
