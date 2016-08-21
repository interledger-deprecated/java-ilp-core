package org.interledger.ilp.core.exceptions;

/**
 * Base ILP exception
 * 
 * @author Manuel Polo <mistermx@gmail.com>
 */
public abstract class InterledgerException extends RuntimeException {

	/**
	 * Creates a new instance of <code>InterledgerException</code> without
	 * detail message.
	 */
	public InterledgerException() {
	}

	/**
	 * Constructs an instance of <code>InterledgerException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public InterledgerException(String msg) {
		super(msg);
	}
}
