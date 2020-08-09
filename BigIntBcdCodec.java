
package com.discore.iso8583.codecs;

import com.discore.iso8583.common.CustomBinaryField;
import com.discore.iso8583.util.Bcd;

import java.math.BigInteger;

/**
 * A custom field encoder/decoder to be used with LLBIN/LLLBIN fields
 * that contain BigIntegers in BCD encoding.
 *
 * 
 */
public class BigIntBcdCodec implements CustomBinaryField<BigInteger> {

    private final boolean rightPadded;

    public BigIntBcdCodec() {
        this(false);
    }
    public BigIntBcdCodec(boolean rightPadded) {
        this.rightPadded = rightPadded;
    }

    @Override
    public BigInteger decodeBinaryField(byte[] value, int pos, int len) {
        return rightPadded ? Bcd.decodeRightPaddedToBigInteger(value, pos, len*2)
            : Bcd.decodeToBigInteger(value, pos, len*2);
    }

    @Override
    public byte[] encodeBinaryField(BigInteger value) {
        final String s = value.toString(10);
        final byte[] buf = new byte[s.length() / 2 + s.length() % 2];
        if (rightPadded) {
            Bcd.encodeRightPadded(s, buf);
        } else {
            Bcd.encode(s, buf);
        }
        return buf;
    }

    @Override
    public BigInteger decodeField(String value) {
        return new BigInteger(value, 10);
    }

    @Override
    public String encodeField(BigInteger value) {
        return value.toString(10);
    }

}
