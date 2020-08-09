
package com.discore.iso8583.common;

/**
 * An extension of the CustomField interface, useful for binary fields.
 * CustomBinaryField encoders can return null for the two CustomField
 * methods IF they are only used with binary messages.
 *
 * 
 */
public interface CustomBinaryField<T> extends CustomField<T> {

    T decodeBinaryField(byte[] value, int offset, int length);

   	byte[] encodeBinaryField(T value);

}
