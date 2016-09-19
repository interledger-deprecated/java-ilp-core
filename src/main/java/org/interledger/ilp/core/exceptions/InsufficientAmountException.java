package org.interledger.ilp.core.exceptions;

public class InsufficientAmountException extends InterledgerException {

    private static final long serialVersionUID = 1964855399622853866L;

    /**
     * Creates a new instance of {@code InsufficientAmountException}
     * without detail message.
     */
    public InsufficientAmountException() {
    }

    /**
     * Constructs an instance of {@code InsufficientAmountException} with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientAmountException(String msg) {
        super(msg);
    }
}
