
package com.discore.iso8583.parse;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;
import com.discore.iso8583.util.Bcd;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 */
public class Date14ParseInfo extends DateTimeParseInfo {

    public Date14ParseInfo() {
   		super(IsoType.DATE14, 14);
   	}

   	@Override
   	public <T> IsoValue<Date> parse(final int field, final byte[] buf,
                                       final int pos, final CustomField<T> custom)
   			throws ParseException, UnsupportedEncodingException {
   		if (pos < 0) {
   			throw new ParseException(String.format("Invalid DATE14 field %d position %d",
                       field, pos), pos);
   		}
   		if (pos+14 > buf.length) {
   			throw new ParseException(String.format("Insufficient data for DATE14 field %d, pos %d",
                       field, pos), pos);
   		}
   		//A SimpleDateFormat in the case of dates won't help because of the missing data
   		//we have to use the current date for reference and change what comes in the buffer
        Calendar cal = Calendar.getInstance();
   		//Set the month in the date
        if (forceStringDecoding) {
            cal.set(Calendar.YEAR, Integer.parseInt(new String(buf, pos, 4, getCharacterEncoding()), 10));
            cal.set(Calendar.MONTH, Integer.parseInt(new String(buf, pos, 2, getCharacterEncoding()), 10)-1);
            cal.set(Calendar.DATE, Integer.parseInt(new String(buf, pos+2, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(new String(buf, pos+4, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.MINUTE, Integer.parseInt(new String(buf, pos+6, 2, getCharacterEncoding()), 10));
            cal.set(Calendar.SECOND, Integer.parseInt(new String(buf, pos+8, 2, getCharacterEncoding()), 10));
        } else {
            cal.set(Calendar.YEAR, ((buf[pos] - 48) * 1000) + ((buf[pos + 1] - 48) * 100) + ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
            cal.set(Calendar.MONTH, ((buf[pos+4] - 48) * 10) + buf[pos + 5] - 49);
            cal.set(Calendar.DATE, ((buf[pos + 6] - 48) * 10) + buf[pos + 7] - 48);
            cal.set(Calendar.HOUR_OF_DAY, ((buf[pos + 8] - 48) * 10) + buf[pos + 9] - 48);
            cal.set(Calendar.MINUTE, ((buf[pos + 10] - 48) * 10) + buf[pos + 11] - 48);
            cal.set(Calendar.SECOND, ((buf[pos + 12] - 48) * 10) + buf[pos + 13] - 48);
        }
        cal.set(Calendar.MILLISECOND,0);
        return createValue(cal, true);
   	}

   	@Override
   	public <T> IsoValue<Date> parseBinary(final int field, final byte[] buf,
                                          final int pos, final CustomField<T> custom)
               throws ParseException {
        if (pos < 0) {
            throw new ParseException(String.format("Invalid DATE14 field %d position %d",
                field, pos), pos);
        }
        if (pos+7 > buf.length) {
            throw new ParseException(String.format("Insufficient data for DATE14 field %d, pos %d",
                field, pos), pos);
        }
   		int[] tens = new int[7];
   		int start = 0;
   		for (int i = pos; i < pos + tens.length; i++) {
   			tens[start++] = Bcd.parseBcdLength(buf[i]);
   		}
   		Calendar cal = Calendar.getInstance();
   		//A SimpleDateFormat in the case of dates won't help because of the missing data
   		//we have to use the current date for reference and change what comes in the buffer
   		//Set the month in the date
        cal.set(Calendar.YEAR, (tens[0] * 100) + tens[1]);
   		cal.set(Calendar.MONTH, tens[2] - 1);
   		cal.set(Calendar.DATE, tens[3]);
   		cal.set(Calendar.HOUR_OF_DAY, tens[4]);
   		cal.set(Calendar.MINUTE, tens[5]);
   		cal.set(Calendar.SECOND, tens[6]);
   		cal.set(Calendar.MILLISECOND,0);
        return createValue(cal, true);
   	}

}
