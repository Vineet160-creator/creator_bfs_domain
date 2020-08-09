
package com.discore.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;
import com.discore.iso8583.util.Bcd;

/** This class is used to parse TIME fields.
 * 
 */
public class TimeParseInfo extends DateTimeParseInfo {

	
	public TimeParseInfo() {
		super(IsoType.TIME, 6);
	}

	@Override
	public <T> IsoValue<Date> parse(final int field, final byte[] buf,
                                final int pos, final CustomField<T> custom)
            throws ParseException, UnsupportedEncodingException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid TIME field %d pos %d",
                    field, pos), pos);
		} else if (pos+6 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for TIME field %d, pos %d", field, pos), pos);
		}
		Calendar cal = Calendar.getInstance();
        if (forceStringDecoding) {
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.MINUTE, Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.SECOND, Integer.parseInt(new String(buf, pos+4, 2, getCharacterEncoding()), 10));
        } else {
            cal.set(Calendar.HOUR_OF_DAY, ((buf[pos] - 48) * 10) + buf[pos + 1] - 48);
            cal.set(Calendar.MINUTE, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
            cal.set(Calendar.SECOND, ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48);
        }
        return createValue(cal, false);
	}

	@Override
	public <T> IsoValue<Date> parseBinary(final int field, final byte[] buf,
                                      final int pos, final CustomField<T> custom)
            throws ParseException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin TIME field %d pos %d",
                    field, pos), pos);
		} else if (pos+3 > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for bin TIME field %d, pos %d", field, pos), pos);
		}
		int[] tens = new int[3];
		int start = 0;
		for (int i = pos; i < pos + 3; i++) {
			tens[start++] = Bcd.parseBcdLength(buf[i]);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, tens[0]);
		cal.set(Calendar.MINUTE, tens[1]);
		cal.set(Calendar.SECOND, tens[2]);
        if (tz != null) {
            cal.setTimeZone(tz);
        }
		return new IsoValue<Date>(type, cal.getTime(), null);
	}

}
