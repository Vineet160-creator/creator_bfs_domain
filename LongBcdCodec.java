
package com.discore.iso8583.codecs;

import com.discore.iso8583.common.CustomBinaryField;
import com.discore.iso8583.util.Bcd;

/**
 * A custom field encoder/decoder to be used with LLBIN/LLLBIN fields
 * that contain Longs in BCD encoding.
 *
 *
 */
public class LongBcdCodec implements CustomBinaryField<Long> {

    private final boolean rightPadded;

    public LongBcdCodec() {
        this(false);
    }
    public LongBcdCodec(boolean rightPadding) {
        rightPadded = rightPadding;
    }

    @Override
    public Long decodeBinaryField(byte[] value, int pos, int length) {
        return rightPadded ? Bcd.decodeRightPaddedToLong(value, pos, length*2)
                : Bcd.decodeToLong(value, pos, length*2);
    }

    @Override
    public byte[] encodeBinaryField(Long value) {
        final String s = Long.toString(value, 10);
        final byte[] buf = new byte[s.length() / 2 + s.length() % 2];
        if (rightPadded) {
            Bcd.encodeRightPadded(s, buf);
        } else {
            Bcd.encode(s, buf);
        }
        return buf;
    }

    @Override
    public Long decodeField(String value) {
        return Long.parseLong(value, 10);
    }

    @Override
    public String encodeField(Long value) {
        return value.toString();
    }
}
