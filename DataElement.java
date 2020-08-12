package com.discore.d48;

public class DataElement {
    public String field;
    public String value;

    public DataElement(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + field + ": " + value + ")";
    }
}