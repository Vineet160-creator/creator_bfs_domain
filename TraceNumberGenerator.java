
package com.discore.iso8583.common;

/** This interface defines the behavior needed to provide sequence numbers for newly created
 * messages. It must provide sequence numbers between 1 and 999999, as per the ISO standard.
 * This value is put in field 11.
 * A default version that simply iterates through an int in memory is provided.
 * 
 *
 */
public interface TraceNumberGenerator {

	/** Returns the next trace number. */
	public int nextTrace();

	/** Returns the last number that was generated. */
	public int getLastTrace();

}
