package org.interledger.ilp.exceptions;

import java.util.Objects;

import org.interledger.ilp.InterledgerError;

/**
 * Base ILP exception.
 *
 * RFC REF: https://interledger.org/rfcs/0003-interledger-protocol/#errors
 *
 */
public class InterledgerException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final InterledgerError interledgerError;

    /**
     * Constructs an inmutable instance of <code>InterledgerException</code> 
     * Used by connectors forwarding the exception.
     * Check the RFC https://interledger.org/rfcs/0003-interledger-protocol/#errors
     * for the newest updated doc.
     * The helper static method InterledgerAddress[] InterledgerException.addSelfToForwardedBy(forwardedBy, selfAddress)
     * can be used to create an updated forwardedBy list right before calling the constructor.
     * 
     * @param errCode,    one of the defined InterledgerException.ErrorCode defined.
     * @param triggeredBy ILP address (prefix if it's a ledger) of the entity that originally emitted the error
     * @param forwardedBy ILP address list of connectors that relayed the error message
     * @param triggeredAt Time when the error was initially emitted
     * @param data        OCTET STRINGSIZE(0..8192) Unless otherwise specified, data SHOULD be encoded as UTF-8. 
     *                    Protocols built on top of ILP SHOULD specify the encoding format of error data 
     * @param selfAddress 
     */
    public InterledgerException(final InterledgerError interledgerError) {
        super();
        this.interledgerError = Objects.requireNonNull(interledgerError, "selfAddress must not be null");
    }

    public InterledgerError getInterledgerError() {
        return interledgerError;
    }


}