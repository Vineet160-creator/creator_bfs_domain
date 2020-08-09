
package com.discore.iso8583.parse;

import java.text.ParseException;

import com.discore.iso8583.common.CustomField;
import com.discore.iso8583.common.IsoType;
import com.discore.iso8583.common.IsoValue;
import com.discore.iso8583.util.Bcd;

/** This class is used to parse NUMERIC fields.
 * 
 */
public class NumericParseInfo extends AlphaNumericFieldParseInfo {

	public NumericParseInfo(int len) {
		super(IsoType.NUMERIC, len);
	}

    @Override
	public <T> IsoValue<Number> parseBinary(final int field, final byte[] buf,
                                        final int pos, final CustomField<T> custom)
            throws ParseException {
		if (pos < 0) {
			throw new ParseException(String.format("Invalid bin NUMERIC field %d pos %d",
                    field, pos), pos);
		} else if (pos+(length/2) > buf.length) {
			throw new ParseException(String.format(
                    "Insufficient data for bin %s field %d of length %d, pos %d",
				type, field, length, pos), pos);
		}
		//A long covers up to 18 digits
		if (length < 19) {
			return new IsoValue<Number>(IsoType.NUMERIC, Bcd.decodeToLong(buf, pos, length),
                length, null);
		} else {
			//Use a BigInteger
            try {
                return new IsoValue<Number>(IsoType.NUMERIC,
                    Bcd.decodeToBigInteger(buf, pos, length), length, null);
            } catch (IndexOutOfBoundsException ex) {
                throw new ParseException(String.format(
                    "Insufficient data for bin %s field %d of length %d, pos %d",
                    type, field, length, pos), pos);
            }
		}
	}

}
