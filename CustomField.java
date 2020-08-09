
package com.discore.iso8583.common;

/** Defines the behavior for a custom field value encoder/decoder. This
 * is just an intersection of CustomFieldEncoder and CustomFieldDecoder.
 * 
 * 
 */
public interface CustomField<DataType>
		extends CustomFieldEncoder<DataType>, CustomFieldDecoder<DataType> {
}
