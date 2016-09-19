package org.interledger.ilp.core.exceptions;

public class InsufficientPrecisionException extends InterledgerException {

    private static final long serialVersionUID = -2444350450065775848L;

    /**
     * Creates a new instance of {@code InsufficientPrecisionException}
     * without detail message.
     */
    public InsufficientPrecisionException() {
    }

    /**
     * Constructs an instance of {@code InsufficientPrecisionException}
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientPrecisionException(String msg) {
        super(msg);
    }
}
