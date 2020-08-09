package com.discore.iso8583.common;

/**
 * Defines the behavior of a custom field encoder, which will convert a value of some
 * data type to a String.
 *
 *
 */
public interface CustomFieldEncoder<DataType> {

    String encodeField(DataType value);
}
