
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;
import com.discore.iso8583.util.Bcd;

/** This class is used to parse fields of type DATE_EXP.
 * 
 */
public class DateExpParseInfo extends DateTimeParseInfo {

	public DateExpParseInfo() {
		super(IsoType.DATE_EXP, 4);
	}

	@Override
	public <T> IsoValue<Date> parse(final int field, final byte[] buf,
                                final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid DATE_EXP field %d position %d",
                    field, pos), pos);
		}
		if (pos+4 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for DATE_EXP field %d pos %d", field, pos), pos);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.DATE, 1);
		//Set the month in the date
        if (forceStringDecoding) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - (cal.get(Calendar.YEAR) % 100)
                    + Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.MONTH, Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10)-1);
        } else {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - (cal.get(Calendar.YEAR) % 100)
                    + ((buf[pos] - 48) * 10) + buf[pos + 1] - 48);
            cal.set(Calendar.MONTH, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 49);
        }
        return createValue(cal, false);
	}

	@Override
	public <T> IsoValue<Date> parseBinary(final int field, final byte[] buf,
                                      final int pos, final CustomField<T> custom)
            throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE_EXP field %d position %d",
                      field, pos), pos);
        }
        if (pos+2 > buf.length) {
            throw new ParseException(String.format(
                      "Insufficient data for DATE_EXP field %d pos %d", field, pos), pos);
        }
		int[] tens = new int[2];
		int start = 0;
		for (int i = pos; i < pos + tens.length; i++) {
			tens[start++] = Bcd.parseBcdLength(buf[i]);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.DATE, 1);
		//Set the month in the date
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)
				- (cal.get(Calendar.YEAR) % 100) + tens[0]);
		cal.set(Calendar.MONTH, tens[1] - 1);
        return createValue(cal, false);
	}
}
