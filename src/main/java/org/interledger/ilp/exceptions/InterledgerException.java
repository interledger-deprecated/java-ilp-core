package org.interledger.ilp.exceptions;

/**
 * Base ILP exception.
 *
 * @author Manuel Polo [mistermx@gmail.com]
 */
public class InterledgerException extends RuntimeException {
    
    private static final int BAD_REQUEST           = 400;
    private static final int UNAUTHORIZED          = 401;
    private static final int FORBIDDEN             = 403;
    private static final int NOT_FOUND             = 404;
    private static final int UNPROCESSABLE_ENTITY  = 422;
    private static final int INTERNAL_SERVER_ERROR = 500;

    final RegisteredException exception;
    final String description;
    final Throwable cause;
    
    private static final Throwable NOT_CAUSE_ATTACHED = new Throwable();

    public enum RegisteredException {
        // TODO:(0) Adjust to RFC:  https://github.com/interledger/rfcs/blob/master/0003-interledger-protocol/0003-interledger-protocol.md (Errors section)
        
        /*
         * TODO: Avoid Oracle attachs. 
         * Providing detailed exception info can be used by attackants
         * about the internals of the server. Since we are treating withh
         * money minimal information must be provided.
         * Ussually BAD_REQUEST , INTERNAL_ERROR must be enough to let external
         * peers known whether the problem is on their request or in the internal
         * processing of such request.
         */
        InternalError             (INTERNAL_SERVER_ERROR, "InternalErrorError"),
        InsufficientAmountError   (INTERNAL_SERVER_ERROR, "InsufficientAmountError"),
        InsufficientPrecisionError(INTERNAL_SERVER_ERROR, "InsufficientPrecisionError"),
        LedgerTransferError       (INTERNAL_SERVER_ERROR, "LedgerTransferError"),
        MaximunDataSizeExceeded   (INTERNAL_SERVER_ERROR, "MaximunDataSizeExceededError"),
        TransferNotFoundError     (NOT_FOUND            , "TransferNotFoundError"),
        AccountExists             (INTERNAL_SERVER_ERROR, "AccountExistsError"),
        AccountNotFoundError      (NOT_FOUND            , "AccountNotFoundError"),
        FulfillmentNotFoundError  (NOT_FOUND            , "FulfillmentNotFoundError"),
        UnauthorizedError         (UNAUTHORIZED         , "UnauthorizedError"),
        ForbiddenError            (FORBIDDEN            , "UnauthorizedError"),
        BadRequestError           (BAD_REQUEST          , "BadRequestError"),
        MissingFulfillmentError   (NOT_FOUND            , "MissingFulfillmentError"),
        AlreadyRolledBackError    (UNPROCESSABLE_ENTITY , "AlreadyRolledBackError"),
        TransferNotConditionalError(UNPROCESSABLE_ENTITY , "TransferNotConditionalError"),
//        InvalidFulfillmentError   (HttpResponseStatus.UNPROCESSABLE_ENTITY , "InvalidFulfillmentError"),
        UnmetConditionError       (UNPROCESSABLE_ENTITY , "UnmetConditionError");
        
        
        
        private final int HTTPErrorCode;
        private final String sID;

        RegisteredException(int HTTPErrorCode, String sID) {
            this.HTTPErrorCode = HTTPErrorCode;
            this.sID = sID;
        }


        public int getHTTPErrorCode() {
            return HTTPErrorCode;
        }

        public String getsID() {
            return sID;
        }
    }
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of <code>InterledgerException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InterledgerException(RegisteredException exception, String description, Throwable cause) {
        super();
        this.exception = exception;
        this.description = description;
        this.cause = cause;
    }
    
    public InterledgerException(RegisteredException exception, String description) {
        this(exception, description, NOT_CAUSE_ATTACHED);
   }

}
