
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;
import com.discore.iso8583.util.Bcd;

/** This class is used to parse fields of type LLLVAR.
 */
public class LllvarParseInfo extends FieldParseInfo {

	
	public LllvarParseInfo() {
		super(IsoType.LLLVAR, 0);
	}

	public <T> IsoValue<?> parse(final int field, final byte[] buf,
                             final int pos, final CustomField<T> custom)
	throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid LLLVAR field %d pos %d",
                    field, pos), pos);
		} else if (pos+3 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for LLLVAR header field %d pos %d", field, pos), pos);
		}
        final int len = decodeLength(buf, pos, 3);
		if (len < 0) {
            throw new ParseException(String.format("Invalid LLLVAR length %d(%s) field %d pos %d",
                    len, new String(buf, pos, 3), field, pos), pos);
		} else if (len+pos+3 > buf.length) {
			throw new ParseException(String.format("Insufficient data for LLLVAR field %d, pos %d len %d",
                    field, pos, len), pos);
		}
		String _v;
        try {
            _v = len == 0 ? "" : new String(buf, pos + 3, len, getCharacterEncoding());
        } catch (IndexOutOfBoundsException ex) {
            throw new ParseException(String.format(
                    "Insufficient data for LLLVAR header, field %d pos %d len %d", field, pos, len), pos);
        }
		//This is new: if the String's length is different from the specified length in the
		//buffer, there are probably some extended characters. So we create a String from
		//the rest of the buffer, and then cut it to the specified length.
		if (_v.length() != len) {
			_v = new String(buf, pos + 3, buf.length-pos-3,
					getCharacterEncoding()).substring(0, len);
		}
		if (custom == null) {
			return new IsoValue<>(type, _v, len, null);
		} else {
			T decoded = custom.decodeField(_v);
			//If decode fails, return string; otherwise use the decoded object and its codec
            return decoded == null ? new IsoValue<>(type, _v, len, null) :
                new IsoValue<>(type, decoded, len, custom);
		}
	}

	public <T> IsoValue<?> parseBinary(final int field, final byte[] buf,
                                   final int pos, final CustomField<T> custom)
			throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin LLLVAR field %d pos %d", field, pos), pos);
		} else if (pos+2 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for bin LLLVAR header, field %d pos %d", field, pos), pos);
		}
		final int len = ((buf[pos] & 0x0f) * 100) + Bcd.parseBcdLength(buf[pos + 1]);
		if (len < 0) {
			throw new ParseException(String.format(
                    "Invalid bin LLLVAR length %d, field %d pos %d", len, field, pos), pos);
		} else if (len+pos+2 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for bin LLLVAR field %d, pos %d", field, pos), pos);
		}
		if (custom == null) {
			return new IsoValue<>(type, new String(buf, pos + 2, len, getCharacterEncoding()), null);
		} else {
			IsoValue<T> v = new IsoValue<>(type, custom.decodeField(
					new String(buf, pos + 2, len, getCharacterEncoding())), custom);
			if (v.getValue() == null) {
				return new IsoValue<>(type,
						new String(buf, pos + 2, len, getCharacterEncoding()), null);
			}
			return v;
		}
	}

}
