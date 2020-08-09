
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;

/** This is the common abstract superclass to parse ALPHA and NUMERIC field types.
 *
 */
public abstract class AlphaNumericFieldParseInfo extends FieldParseInfo {

	public AlphaNumericFieldParseInfo(IsoType t, int len) {
		super(t, len);
	}

    @Override
	public <T> IsoValue<?> parse(final int field, final byte[] buf, final int pos,
                             final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid ALPHA/NUM field %d position %d",
                    field, pos), pos);
		} else if (pos+length > buf.length) {
			throw new ParseException(String.format("Insufficient data for %s field %d of length %d, pos %d",
				type, field, length, pos), pos);
		}
        try {
            String _v = new String(buf, pos, length, getCharacterEncoding());
            if (_v.length() != length) {
                _v = new String(buf, pos, buf.length-pos, getCharacterEncoding()).substring(0, length);
            }
            if (custom == null) {
                return new IsoValue<>(type, _v, length, null);
            } else {
                T decoded = custom.decodeField(_v);
                return decoded == null ? new IsoValue<>(type, _v, length, null) :
                    new IsoValue<>(type, decoded, length, custom);
            }
        } catch (StringIndexOutOfBoundsException ex) {
            throw new ParseException(String.format(
                    "Insufficient data for %s field %d of length %d, pos %d",
                    type, field, length, pos), pos);
        }
	}

}
