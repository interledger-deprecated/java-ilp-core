package org.interledger.ilp;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>Interledger errors may be generated at any point as an Interledger payment is being prepared
 * or by a receiver. Connectors that are notified of an outgoing transfer becoming rejected MUST
 * reject the corresponding incoming transfer with the same error.</p>
 *
 * <p>Connectors SHOULD include their ILP address in the forwardedBy field in the error, but SHOULD
 * NOT modify errors in any other way.</p>
 */
public interface InterledgerError {

  /**
   * Accessor for the ILP Error Code.
   *
   * @return An instance of {@link ErrorCode}.
   */
  ErrorCode getErrorCode();

  /**
   * Accessor for the {@link InterledgerAddress} of the entity that originally emitted this error.
   *
   * @return An instance of {@link InterledgerAddress}.
   */
  InterledgerAddress getTriggeredBy();

  /**
   * The date and time when this error was initially emitted.
   *
   * @return An instance of {@link ZonedDateTime}.
   */
  ZonedDateTime getTriggeredAt();

  /**
   * A list of {@link InterledgerAddress} instances for connectors that have relayed this error
   * message.
   *
   * @return A {@link List} of type {@link InterledgerAddress}.
   */
  List<InterledgerAddress> getForwardedBy();

  /**
   * An octet string with error data provided for debugging purposes.
   *
   * @deprecated See "https://github.com/interledger/rfcs/issues/188"
   */
  @Deprecated
  String getData();

  /**
   * Valid error codes that might be encountered during an Interledger payment.
   */
  enum ErrorCode {

    // Generic sender error.
    F00_BAD_REQUEST("F00", "BAD REQUEST"),

    // The ILP packet was syntactically invalid.
    F01_INVALID_PAQUET("F01", "INVALID PACKET"),

    // There was no way to forward the payment, because the destination ILP address was wrong or the
    // connector does not have a route to the destination.
    F02_UNREACHABLE("F02", "UNREACHABLE"),

    // The amount of the payment is invalid, for example it contains more digits of precision than
    // are available on the destination ledger or the amount is greater than the total amount of
    // the given asset in existence.
    F03_INVALID_AMOUNT("F03", "INVALID AMOUNT"),

    // The receiver deemed the amount insufficient, for example you tried to pay a $100 invoice with
    // $10.
    F04_INSUFFICIENT_DST_AMOUNT("F04", "INSUFFICIENT DST. AMOUNT"),

    // The receiver generated a different condition and cannot fulfill the payment.
    F05_WRONG_CONDITION("F05", "WRONG CONDITION"),

    // The receiver was not expecting a payment like this (the memo and destination address don't
    // make sense in that combination, for example if the receiver does not understand the transport
    // protocol used)
    F06_UNEXPECTED_PAYMENT("F06", "UNEXPECTED PAYMENT"),

    // The receiver is unable to accept this payment due to a constraint. For example, the payment
    // would put the receiver above its maximum account balance.
    F07_CANNOT_RECEIVE("F07", "CANNOT RECEIVE"),

    // Reserved for application layer protocols. Applications MAY use names other than Application
    // Error.
    F99_APPLICATION_ERROR("F99", "APPLICATION ERROR"),

    // A generic unexpected exception. This usually indicates a bug or unhandled error case.
    T00_INTERNAL_ERROR("T00", "INTERNAL ERROR"),

    // The connector has a route or partial route to the destination but was unable to reach the
    // next ledger. Try again later.
    T01_LEDGER_UNREACHABLE("T01", "LEDGER UNREACHABLE"),

    // The ledger is rejecting requests due to overloading. Try again later.
    T02_LEDGER_BUSY("T02", "LEDGER BUSY"),

    // The connector is rejecting requests due to overloading. Try again later.
    T03_CONNECTOR_BUSY("T03", "CONNECTOR BUSY"),

    // The connector would like to fulfill your request, but it doesn't currently have enough money.
    // Try again later.
    T04_INSUFFICIENT_LIQUIDITY("T04", "INSUFFICIENT LIQUIDITY"),

    // The sender is sending too many payments and is being rate-limited by a ledger or connector.
    // If a connector gets this error because they are being rate-limited, they SHOULD retry the
    // payment through a different route or respond to the sender with a T03: Connector Busy error.
    T05_RATE_LIMITED("T05", "RATE LIMITED"),

    // Reserved for application layer protocols. Applications MAY use names other than Application
    // Error.
    T99_APPLICATION_ERROR("T99", "APPLICATION ERROR"),

    // The transfer timed out, meaning the next party in the chain did not respond. This could be
    // because you set your timeout too low or because something look longer than it should. The
    // sender MAY try again with a higher expiry, but they SHOULD NOT do this indefinitely or a
    // malicious connector could cause them to tie up their money for an unreasonably long time.
    R00_TRANSFER_TIMED_OUT("R00", "TRANSFER TIMED OUT"),

    // Either the sender did not send enough money or the exchange rate changed before the payment
    // was prepared. The sender MAY try again with a higher amount, but they SHOULD NOT do this
    // indefinitely or a malicious connector could steal money from them.
    R01_INSUFFICEINT_SOURCE_AMOUNT("R01", "INSUFFICIENT SOURCE AMOUNT"),

    // The connector could not forward the payment, because the timeout was too low to subtract its
    // safety margin. The sender MAY try again with a higher expiry, but they SHOULD NOT do this
    // indefinitely or a malicious connector could cause them to tie up their money for an
    // unreasonably long time.
    R02_INSUFFICIENT_TIMEOUT("R02", "INSUFFICIENT TIMEOUT"),

    // Reserved for application layer protocols. Applications MAY use names other than Application
    // Error.
    R99_APPLICATION_ERROR("R99", "APPLICATION ERROR");

    /**
     * The type of an {@link ErrorCode}, which is determined by the first letter of the error code's
     * code value.
     */
    enum Type {

      // Final errors indicate that the payment is invalid and should not be retried unless the
      // details are changed.
      FINAL,

      // Temporary errors indicate a failure on the part of the receiver or an intermediary system
      // that is unexpected or likely to be resolved soon. Senders SHOULD retry the same payment
      // again, possibly after a short delay.
      TEMPORARY,

      // Relative errors indicate that the payment did not have enough of a margin in terms of money
      // or time. However, it is impossible to tell whether the sender did not provide enough error
      // margin or the path suddenly became too slow or illiquid. The sender MAY retry the payment
      // with a larger safety margin.
      RELATIVE,

      // The error type is unkown.
      UNKNOWN;
    }

    private final String code;
    private final String name;
    private final Type type;

    /**
     * Required-args Constructor.
     *
     * @param code A {@link String} representing the error code as defined by IL-RFC-3.
     * @param name A {@link String} representing the error code name as defined by IL-RFC-3.  This
     */
    ErrorCode(final String code, final String name) {
      this.code = Objects.requireNonNull(code, "code MUST not be null!").trim();
      this.name = Objects.requireNonNull(name, "name MUST not be null!").trim();

      if (code.length() < 3) {
        throw new RuntimeException(
            "Per IL-RFC-3, error code length must be at least 3 characters!");
      }

      // NOTE: Per the R99_APPLICATION_ERROR, applications may use custom names, so no validation
      // should be performed on names.

      switch (this.code.charAt(0)) {
        case 'F':
          this.type = Type.FINAL;
          break;
        case 'T':
          this.type = Type.TEMPORARY;
          break;
        case 'R':
          this.type = Type.RELATIVE;
          break;
        default:
          this.type = Type.UNKNOWN;
      }
    }

    @Override
    public String toString() {
      return String.format("%s - %s", this.code, this.name);
    }

    /**
     * <p>Accessor for this ErrorCode's {@code code} property.</p>
     *
     * <p>Per IL-RFC-3: "Implementations SHOULD NOT depend on the name instead of the code. The
     * name is primarily provided as a convenience to facilitate debugging by humans. If the name
     * does not match the code, the code is the definitive identifier of the error."</p>
     */
    public String getCode() {
      return this.code;
    }

    /**
     * Returns the {@link Type} of this {@link ErrorCode}.
     */
    public Type getErrorCodeType() {
      return this.type;
    }
  }
}
