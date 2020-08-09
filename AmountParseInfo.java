
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;

/** This class is used to parse AMOUNT fields.
 */
public class AmountParseInfo extends FieldParseInfo {

	
	public AmountParseInfo() {
		super(IsoType.AMOUNT, 12);
	}

    @Override
	public <T> IsoValue<BigDecimal> parse(final int field, final byte[] buf,
                                      final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid AMOUNT field %d position %d",
                    field, pos), pos);
		}
		if (pos+12 > buf.length) {
			throw new ParseException(String.format("Insufficient data for AMOUNT field %d, pos %d",
                    field, pos), pos);
		}
		String c = new String(buf, pos, 12, getCharacterEncoding());
		try {
			return new IsoValue<>(type, new BigDecimal(c).movePointLeft(2));
		} catch (NumberFormatException ex) {
			throw new ParseException(String.format("Cannot read amount '%s' field %d pos %d",
                    c, field, pos), pos);
        } catch (IndexOutOfBoundsException ex) {
            throw new ParseException(String.format(
                    "Insufficient data for AMOUNT field %d, pos %d", field, pos), pos);
		}
	}

    @Override
	public <T> IsoValue<BigDecimal> parseBinary(final int field, final byte[] buf,
                                            final int pos, final CustomField<T> custom)
            throws ParseException {
		char[] digits = new char[13];
		digits[10] = '.';
		int start = 0;
		for (int i = pos; i < pos + 6; i++) {
			digits[start++] = (char)(((buf[i] & 0xf0) >> 4) + 48);
			digits[start++] = (char)((buf[i] & 0x0f) + 48);
			if (start == 10) {
				start++;
			}
		}
		try {
			return new IsoValue<>(IsoType.AMOUNT, new BigDecimal(new String(digits)));
		} catch (NumberFormatException ex) {
			throw new ParseException(String.format("Cannot read amount '%s' field %d pos %d",
                    new String(digits), field, pos), pos);
        } catch (IndexOutOfBoundsException ex) {
            throw new ParseException(String.format(
                    "Insufficient data for AMOUNT field %d, pos %d", field, pos), pos);
		}
	}

}
