package org.interledger.ilp.exceptions;

import java.util.Objects;
import java.time.ZonedDateTime;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.InterledgerAddressBuilder;

/**
 * Base ILP exception.
 *
 * RFC REF: https://interledger.org/rfcs/0003-interledger-protocol/#errors
 *
 */
public class InterledgerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // TODO:(0) Add encoding support to serialize it through the wire (binary, JSON,...?, check the RFCs)
    public enum ErrorType {
        FINAL('F'),
        TEMPORARY('T'),
        RELATIVE('R');

        private final String t;
        private ErrorType(char t){
            this.t = Character.toString(t);
        }

        @Override
        public String toString(){
            return t;
        }
    }

    public enum ErrorCode {
        // FINAL ERRORS            
        F00_BAD_REQUEST               ("F00", "BAD REQUEST"             ),
        F01_INVALID_PAQUET            ("F01", "INVALID PAQUET"          ),
        F02_UNREACHABLE               ("F02", "UNREACHABLE"             ),
        F03_INVALID_AMOUNT            ("F03", "INVALID AMOUNT"          ),
        F04_INSUFFICIENT_DST_AMOUNT   ("F04", "INSUFFICIENT DST. AMOUNT"),
        // TODO:(RFC) WRONG CONDITION is actually WRONG FULFILLMENT?
        F05_WRONG_CONDITION           ("F05", "WRONG CONDITION"         ),
        F06_UNEXPECTED_PAYMENT        ("F06", "UNEXPECTED PAYMENT"      ),
        F07_CANNOT_RECEIVE            ("F07", "CANNOT RECEIVE"          ),
        F99_APPLICATION_ERROR         ("F99", "APPLICATION ERROR"       ),

        T00_INTERNAL_ERROR            ("T00", "INTERNAL ERROR"          ),
        T01_LEDGER_UNREACHABLE        ("T01", "LEDGER UNREACHABLE"      ),
        T02_LEDGER_BUSY               ("T02", "LEDGER BUSY"             ),
        T03_CONNECTOR_BUSY            ("T03", "CONNECTOR BUSY"          ),
        T04_INSUFFICIENT_LIQUIDITY    ("T04", "INSUFFICIENT LIQUIDITY"  ),
        T05_RATE_LIMITED              ("T05", "RATE LIMITED"            ),
        T99_APPLICATION_ERROR         ("T99", "APPLICATION ERROR"       ),

        R00_TRANSFER_TIMED_OUT        ("R00", "TRANSFER TIMED OUT"      ),
        R01_INSUFFICEINT_SOURCE_AMOUNT("R01", "INSUFFICEINT SOURCE AMOUNT"),
        R02_INSUFFICIENT_TIMEOUT      ("R02", "INSUFFICIENT TIMEOUT"    ),
        R99_APPLICATION_ERROR         ("R99", "APPLICATION ERROR"       )
        ;
        // TODO:(RFC) ADD FORBIDEN ERROR IF NO CREDENTIALS ARE PROVIDED OR LAUNCH "T01 LEDGER-UNREACHABLE" ???
        // IMAGINE node1 -> conector1 -> conector2 -> (ERR. FORBIDEN) contector3
        //  - How must routing be updated in  conector1/2/3 ?

        public  final ErrorType errorType;
        private final String code;
        private final String name;

        private ErrorCode(String code, String name) {
            code = Objects.requireNonNull(code, "code must not be null").trim();
            name = Objects.requireNonNull(name, "code must not be null").trim();

            if (code.length()!=3) {
                throw new RuntimeException("error code length must be equal to 3");
            }
            char type = code.charAt(0);
            switch (type) {
            case 'F':
                errorType = ErrorType.FINAL;
                break;
            case 'T':
                errorType = ErrorType.TEMPORARY;
                break;
            case 'R':
                errorType = ErrorType.RELATIVE;
                break;
            default:
                throw new IllegalArgumentException("code must start with 'F', 'T' or 'R'.");
            }

            this.code = code;
            this.name = name;
        }

        @Override
        public String toString(){
            return this.code + "-" +this.name;
        }
    }

    private final ErrorCode errCode;
    private final InterledgerAddress triggeredBy;

    private final InterledgerAddress[] forwardedBy;
    private final ZonedDateTime triggeredAt;
    private final String data;

    // TRIGGERING_ILP_NODE is used as a "mark" to avoid nulls.
    // This way our code can differentiate between a coding error
    // (developer sent a null by mistake) and the real intention of not
    // sending selfAddress.
    static final InterledgerAddress TRIGGERING_ILP_NODE = new InterledgerAddressBuilder().value("g.selfAddressNONE").build();
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
    public InterledgerException(
        ErrorCode errCode,
        InterledgerAddress triggeredBy,
        InterledgerAddress[] forwardedBy,
        ZonedDateTime triggeredAt,
        String data,
        InterledgerAddress selfAddress) {
        super();
        this.errCode     = Objects.requireNonNull(errCode    , "errCode     must not be null");
        this.triggeredBy = Objects.requireNonNull(triggeredBy, "triggeredBy must not be null");

        this.triggeredAt = Objects.requireNonNull(triggeredAt, "triggeredAt must not be null");
        this.data        = Objects.requireNonNull(data       , "data        must not be null");

        Objects.requireNonNull(forwardedBy, "forwardedBy must not be null");
        Objects.requireNonNull(selfAddress, "selfAddress must not be null");
        if (TRIGGERING_ILP_NODE.getValue().equals(selfAddress.getValue())) {
            this.forwardedBy = forwardedBy; // Ignore selfAddress
        } else {
            InterledgerAddress[] forwardedByNew = new InterledgerAddress[forwardedBy.length+1];
            for (int idx=0; idx < forwardedByNew.length ; idx++){
                if (forwardedByNew[idx].getValue().equals(selfAddress.getValue())){
                    throw new RuntimeException("loop was detected in the forwardedBy list.");
                }
                forwardedByNew[idx] = forwardedBy[idx];
                forwardedByNew[forwardedByNew.length-1] = selfAddress;
            }
            this.forwardedBy = forwardedByNew;
        }
    }

    /**
     * Constructs an instance of <code>InterledgerException</code>
     * with default parameters for forwardedBy (Empty list) and 
     * triggeredAt (ZonedDateTime.now()). 
     *  In most situations such values match the default ones 
     * when triggering a new exception (vs an exception received
     * from another ILP node that is being forwarded back to 
     * originating request clients)
     * 
     * Check the RFC https://interledger.org/rfcs/0003-interledger-protocol/#errors
     * for the newest updated doc.
     */
    public InterledgerException(
        ErrorCode errCode,
        InterledgerAddress triggeredBy,
        String data) {
        this(errCode, triggeredBy, new InterledgerAddress[]{}, ZonedDateTime.now(), "", TRIGGERING_ILP_NODE);
    }

    public ErrorCode getErrCode() {
        return errCode;
    }

    /**
     * @return ErrorType categorizes the error as FINAL, TEMPORARY or RELATIVE
     *         (See RFC for more info)
     */
    public ErrorType getErrorType(){
        return errCode.errorType;
    }

    public InterledgerAddress getTriggeredBy() {
        return triggeredBy;
    }

    public InterledgerAddress[] getForwardedBy() {
        return forwardedBy;
    }

    public ZonedDateTime getTriggeredAt() {
        return triggeredAt;
    }

    public String getData() {
        return data;
    }


}