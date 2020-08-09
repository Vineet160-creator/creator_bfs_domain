
package com.discore.iso8583.impl;

import com.discore.iso8583.common.TraceNumberGenerator;

/** Simple implementation of a TraceNumberGenerator with an internal
 * number that is increased in memory but is not stored anywhere.
 * 
 *
 */
public class SimpleTraceGenerator implements TraceNumberGenerator {

	private volatile int value = 0;

	/** Creates a new instance that will use the specified initial value. This means
	 * the first nextTrace() call will return this number.
	 * @param initialValue a number between 1 and 999999.
	 * @throws IllegalArgumentException if the number is less than 1 or greater than 999999. */
	public SimpleTraceGenerator(int initialValue) {
		if (initialValue < 1 || initialValue > 999999) {
			throw new IllegalArgumentException("Initial value must be between 1 and 999999");
		}
		value = initialValue - 1;
	}

	public int getLastTrace() {
		return value;
	}

	/** Returns the next number in the sequence. This method is synchronized, because the counter
	 * is incremented in memory only. */
	public synchronized int nextTrace() {
		value++;
		if (value > 999999) {
			value = 1;
		}
		return value;
	}

}
