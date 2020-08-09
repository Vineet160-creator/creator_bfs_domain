
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;
import com.discore.iso8583.util.HexCodec;

/** This class is used to parse fields of type BINARY.
 * 
 */
public class BinaryParseInfo extends FieldParseInfo {

	
	public BinaryParseInfo(int len) {
		super(IsoType.BINARY, len);
	}

	@Override
	public <T> IsoValue<?> parse(final int field, final byte[] buf, final int pos,
                             final CustomField<T> custom)
			throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid BINARY field %d position %d",
                    field, pos), pos);
		}
		if (pos+(length*2) > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for BINARY field %d of length %d, pos %d",
				field, length, pos), pos);
		}
		byte[] binval = HexCodec.hexDecode(new String(buf, pos, length*2));
		if (custom == null) {
			return new IsoValue<>(type, binval, binval.length, null);
		} else {
            T dec = custom.decodeField(new String(buf, pos, length*2, getCharacterEncoding()));
            return dec == null ? new IsoValue<>(type, binval, binval.length, null) :
                    new IsoValue<>(type, dec, length, custom);
		}
	}

	@Override
	public <T> IsoValue<?> parseBinary(final int field, final byte[] buf, final int pos,
                                   final CustomField<T> custom) throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid BINARY field %d position %d",
                      field, pos), pos);
        }
        if (pos+length > buf.length) {
            throw new ParseException(String.format(
                      "Insufficient data for BINARY field %d of length %d, pos %d",
                field, length, pos), pos);
        }
		byte[] _v = new byte[length];
		System.arraycopy(buf, pos, _v, 0, length);
		if (custom == null) {
			return new IsoValue<>(type, _v, length, null);
		} else {
            T dec = custom.decodeField(HexCodec.hexEncode(_v, 0, _v.length));
            return dec == null ? new IsoValue<>(type, _v, length, null) :
                    new IsoValue<>(type, dec, length, custom);
		}
	}

}
