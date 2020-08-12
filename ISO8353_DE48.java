package com.discore.d48;
import java.util.*;

public class ISO8353_DE48 {
    private static final String DE48 = "48";
    private static final int LEN = 3;
    private static final int CODE_LEN = 1;
    private static final int SUBFIELD_LEN = 2;

    private StringBuilder buffer;
    private int bufPos = 0;
    
    public ISO8353_DE48(String buf) {
        buffer = new StringBuilder(buf);
    }
    

    /** 
     * Parse subfields
    */
    public List<DataElement> parse() {
        bufPos += LEN + CODE_LEN;
        return parseField(DE48, buffer.length()); // TODO
    }

    private List<DataElement> parseField(String field, int endPos) {        
        List<DataElement> result = new ArrayList<>();
        
        if (bufPos < endPos) {
            result.add(new DataElement(field, getChunk(endPos - bufPos)));
            
            while (true) {
                String subField = field + "." + parseValueLength(SUBFIELD_LEN);

                if (RuleMap.hasField(subField)) {
                    bufPos += SUBFIELD_LEN;
                    int fieldLen = RuleMap.getFieldLength(subField);
                    int valueLen = parseValueLength(fieldLen);

                    bufPos += fieldLen;
                    result.addAll(parseField(subField, bufPos + valueLen));
                } else {
                    // Consume result of value
                    bufPos = endPos;
                    break;
                }
            }
        }
        
        return result;
    }

    private int parseValueLength(int fieldLen) {
        String code = getChunk(fieldLen);
        try {
            return Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Return chunk of buffer[bufPos, len)
    private String getChunk(int len) {
        try {
            return buffer.substring(bufPos, bufPos + len);
        } catch (Exception e) {
            return "";
        }
    }

    
    public static void main(String[] args) {
        
        String[] values = new String[] {
            "026C710418C 75130103785020200"
            // "043F710418C 7526010378502020003039990402R1",
            // "049P710418C 7532010378502020003039990402R1050201",
            // "009P710418U "
        };
        
        for (String value: values) {
            ISO8353_DE48 parser = new ISO8353_DE48(value);
            System.err.println("Message: " + value);
            System.err.println("Parsed: " + parser.parse());
        }
    }
}