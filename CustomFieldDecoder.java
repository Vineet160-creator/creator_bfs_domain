package com.discore.iso8583.common;

/**
 * A functional interface to decode a value from a field (string or binary data) into some
 * other data type.
 *
 * 
 */
public interface CustomFieldDecoder<DataType> {

    DataType decodeField(String value);
}
