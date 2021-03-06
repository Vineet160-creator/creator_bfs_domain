
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import com.discore.iso8583.common.CustomBinaryField;
import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;
import com.discore.iso8583.util.Bcd;
import com.discore.iso8583.util.HexCodec;

/** This class is used to parse fields of type LLLBIN.
 *
 *
 */
public class LllbinParseInfo extends FieldParseInfo {

    public LllbinParseInfo(IsoType t, int len) {
        super(t, len);
    }

	public LllbinParseInfo() {
		super(IsoType.LLLBIN, 0);
	}

	@Override
	public <T> IsoValue<?> parse(final int field, final byte[] buf,
                             final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid LLLBIN field %d pos %d",
                    field, pos), pos);
		} else if (pos+3 > buf.length) {
			throw new ParseException(String.format("Insufficient LLLBIN header field %d",
                    field), pos);
		}
		final int l = decodeLength(buf, pos, 3);
		if (l < 0) {
			throw new ParseException(String.format("Invalid LLLBIN length %d field %d pos %d",
                    l, field, pos), pos);
		} else if (l+pos+3 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for LLLBIN field %d, pos %d len %d",
                    field, pos, l), pos);
		}
		byte[] binval = l == 0 ? new byte[0] : HexCodec.hexDecode(new String(buf, pos + 3, l));
		if (custom == null) {
			return new IsoValue<>(type, binval, binval.length, null);
        } else if (custom instanceof CustomBinaryField) {
            try {
                T dec = ((CustomBinaryField<T>)custom).decodeBinaryField(
                    buf, pos + 3, l);
                return dec == null ? new IsoValue<>(type, binval, binval.length, null) :
                        new IsoValue<>(type, dec, 0, custom);
            } catch (IndexOutOfBoundsException ex) {
                throw new ParseException(String.format(
                        "Insufficient data for LLLBIN field %d, pos %d len %d",
                        field, pos, l), pos);
            }
		} else {
            try {
                T dec = custom.decodeField(
                    l == 0 ? "" : new String(buf, pos + 3, l));
                return dec == null ? new IsoValue<>(type, binval, binval.length, null) :
                        new IsoValue<>(type, dec, l, custom);
            } catch (IndexOutOfBoundsException ex) {
                throw new ParseException(String.format(
                        "Insufficient data for LLLBIN field %d, pos %d len %d",
                        field, pos, l), pos);
            }
		}
	}

	@Override
	public <T> IsoValue<?> parseBinary(final int field, final byte[] buf,
                                   final int pos, final CustomField<T> custom)
            throws ParseException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin LLLBIN field %d pos %d",
                    field, pos), pos);
		} else if (pos+2 > buf.length) {
            throw new ParseException(String.format("Insufficient LLLBIN header field %d",
                             field), pos);
		}
		final int l = getLengthForBinaryParsing(buf, pos);
		if (l < 0) {
            throw new ParseException(String.format("Invalid LLLBIN length %d field %d pos %d",
                             l, field, pos), pos);
		}
		if (l+pos+2 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for bin LLLBIN field %d, pos %d requires %d, only %d available",
                    field, pos, l, buf.length-pos+1), pos);
		}
		byte[] _v = new byte[l];
		System.arraycopy(buf, pos+2, _v, 0, l);
		if (custom == null) {
            int len = getFieldLength(buf, pos);
            return new IsoValue<>(type, _v, len, forceHexadecimalLength);
        } else if (custom instanceof CustomBinaryField) {
            try {
                T dec = ((CustomBinaryField<T>)custom).decodeBinaryField(
                    buf, pos + 2, l);
                return dec == null ? new IsoValue<>(type, _v, _v.length, null) :
                        new IsoValue<>(type, dec, l, custom);
            } catch (IndexOutOfBoundsException ex) {
                throw new ParseException(String.format(
                        "Insufficient data for LLLBIN field %d, pos %d", field, pos), pos);
            }
		} else {
            T dec = custom.decodeField(HexCodec.hexEncode(_v, 0, _v.length));
            return dec == null ? new IsoValue<>(type, _v, null) :
                    new IsoValue<>(type, dec, custom);
		}
	}

	protected int getLengthForBinaryParsing(final byte[] buf, final int pos) {
		return getFieldLength(buf, pos);
	}

	private int getFieldLength(final byte[] buf, final int pos) {
        return forceHexadecimalLength ?
                ((buf[pos] & 0xff) << 8) | (buf[pos + 1] & 0xff)
                :
                ((buf[pos] & 0x0f) * 100) + Bcd.parseBcdLength(buf[pos + 1]);
    }

}
