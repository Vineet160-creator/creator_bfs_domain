
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;

/** This is the class used to parse ALPHA fields.
 * 
 */
public class AlphaParseInfo extends AlphaNumericFieldParseInfo {

	public AlphaParseInfo(int len) {
		super(IsoType.ALPHA, len);
	}

    @Override
	public <T> IsoValue<?> parseBinary(final int field, final byte[] buf, final int pos,
                                   final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin ALPHA field %d position %d",
                    field, pos), pos);
		} else if (pos+length > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for bin %s field %d of length %d, pos %d",
				type, field, length, pos), pos);
		}
        try {
            if (custom == null) {
                return new IsoValue<>(type, new String(buf, pos, length, getCharacterEncoding()), length, null);
            } else {
                T decoded = custom.decodeField(new String(buf, pos, length, getCharacterEncoding()));
                return decoded == null ?
                    new IsoValue<>(type, new String(buf, pos, length, getCharacterEncoding()), length, null) :
                    new IsoValue<>(type, decoded, length, custom);
            }
        } catch (IndexOutOfBoundsException ex) {
            throw new ParseException(String.format(
                    "Insufficient data for bin %s field %d of length %d, pos %d",
         				type, field, length, pos), pos);
        }
	}

}
