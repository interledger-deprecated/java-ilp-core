package org.interledger.ilp.core.exceptions;

public class LedgerTransferException extends InterledgerException {

    private static final long serialVersionUID = -2444350450065775848L;

    /**
     * Creates a new instance of {@code LedgerTransferException}
     * without detail message.
     */
    public LedgerTransferException() {
    }

    /**
     * Constructs an instance of {@code LedgerTransferException}
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public LedgerTransferException(String msg) {
        super(msg);
    }
}
