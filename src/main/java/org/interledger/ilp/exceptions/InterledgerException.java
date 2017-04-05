package org.interledger.ilp.exceptions;

import java.util.ArrayList;
import java.time.ZonedDateTime;

import org.interledger.ilp.InterledgerAddress;

/**
 * Base ILP exception.
 *
 * RFC REF: https:https://interledger.org/rfcs/0003-interledger-protocol/#errors
 *
 */
public class InterledgerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // TODO:(0) Add encoding support to serialize it through the wire (binary, JSON,...?, check the RFCs)
    public enum ErrorType {
        FINAL('F'),
        TEMPORARY('T'),
        RELATIVE('R');

        String t;
        ErrorType(char t){
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

        private final String code;
        private final String name;

        ErrorCode(String code, String name) {
            if (code==null || name==null){
                throw new RuntimeException("code and/or name can not be null");
            }
            code = code.trim();
            name = name.trim();
            if (code.length()!=3) {
                throw new RuntimeException("error code length must be equal to 3");
            }
            char type = code.charAt(0);
            if (type != 'F' && type !='T' && type !='R'){
                throw new RuntimeException("error code must be := 'F' | 'T' | 'R'");
            }
            this.code = code;
            this.name = name;
        }

        @Override
        public String toString(){
            return this.code + "-" +this.name;
        }

        public ErrorType getErrorType(){
            char type = code.charAt(0);
            switch (type) {
            case 'F':
                return ErrorType.FINAL;
            case 'T':
                return ErrorType.TEMPORARY;
            case 'R':
                return ErrorType.RELATIVE;
            default:
                throw new IllegalArgumentException("Invalid type " + type + ", InterledgerException serialVersionUID: " + serialVersionUID);
            }
        }

    }

    private final ErrorCode errCode;
    private final InterledgerAddress triggeredBy;
    
    /**
     * Note. The forwardedBy ArrayList is final but it's still possible to add members
     *    to it through the addSelfToForwardedByList method.
     *    (It's just the forwardedBy reference that is final, so it can NOT be assigned
     *    to a different arrayList)
     *    This is done on purpose since the InterledgerException instances can
     *    be relatively big (data is up to 8192 bytes) and cloning the full exception
     *    to add a single a member to the list could cause performance/memory trouble in
     *    some scenarios.
     */
    private final ArrayList<InterledgerAddress> forwardedBy;
    private final ZonedDateTime triggeredAt;
    private final String data;
    
    // Avoid nulls when forwaredBy is empty
    public static final ArrayList<InterledgerAddress> EMPTY_ForwardedBy_List = 
            new ArrayList<InterledgerAddress>();
    /**
     * Constructs an instance of <code>InterledgerException</code> 
     * Check the RFC https://interledger.org/rfcs/0003-interledger-protocol/#errors
     * for the newest updated doc.
     * 
     * @param errCode,    one of the defined InterledgerException.ErrorCode defined.
     * @param triggeredBy ILP address (prefix if it's a ledger) of the entity that originally emitted the error
     * @param forwardedBy ILP address list of connectors that relayed the error message
     * @param triggeredAt Time when the error was initially emitted
     * @param data        OCTET STRINGSIZE(0..8192) Unless otherwise specified, data SHOULD be encoded as UTF-8. 
     *                    Protocols built on top of ILP SHOULD specify the encoding format of error data 
     */
    public InterledgerException(
        ErrorCode errCode,
        InterledgerAddress triggeredBy,
        ArrayList<InterledgerAddress> forwardedBy,
        ZonedDateTime triggeredAt, 
        String data) {
        super();
        // TODO:(0) check params are not null.
        this.errCode     = errCode;
        this.triggeredBy = triggeredBy;
        this.forwardedBy = forwardedBy;
        this.triggeredAt = triggeredAt;
        this.data        = data;
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
        this(errCode, triggeredBy, EMPTY_ForwardedBy_List, ZonedDateTime.now(), data);
    }

    public ErrorCode getErrCode() {
        return errCode;
    }
    
    public ErrorType getErrorType(){
        return errCode.getErrorType();
    }

    public InterledgerAddress getTriggeredBy() {
        return triggeredBy;
    }

    public ArrayList<InterledgerAddress> getForwardedBy() {
        return forwardedBy;
    }

    public ZonedDateTime getTriggeredAt() {
        return triggeredAt;
    }

    public String getData() {
        return data;
    }

    /**
     * Used by connectors to add themself to the forwaredBy list
     * @param selfAdd connector ILP address.
     */
    public void addSelfToForwardedByList(InterledgerAddress selfAdd) {
        this.forwardedBy.add(selfAdd);
    }

}