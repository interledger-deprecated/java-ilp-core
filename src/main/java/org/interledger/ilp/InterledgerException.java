package org.interledger.ilp;

import java.util.Optional;

/**
 * An extension of {@link RuntimeException} that contains an {@link InterledgerError}.
 */
public abstract class InterledgerException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final Optional<InterledgerError> interledgerError;

  /**
   * Constructs a new Interledger exception with a {@code null} as its interledgerError object and
   * {@code null} as its detail message.  The cause is not initialized, and may subsequently be
   * initialized by a call to {@link #initCause}.
   */
  public InterledgerException() {
    this.interledgerError = Optional.empty();
  }

  /**
   * Constructs a new runtime exception with {@code null} as its detail message.  The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param interledgerError An instance of {@link InterledgerError}.
   */
  public InterledgerException(final InterledgerError interledgerError) {
    this.interledgerError = Optional.ofNullable(interledgerError);
  }

  /**
   * Constructs a new runtime exception with the specified detail message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link #initCause}.
   *
   * @param interledgerError An instance of {@link InterledgerError}.
   * @param message          the detail message. The detail message is saved for later retrieval by
   *                         the {@link #getMessage()} method.
   */
  public InterledgerException(final InterledgerError interledgerError, final String message) {
    super(message);
    this.interledgerError = Optional.ofNullable(interledgerError);
  }

  /**
   * <p>Constructs a new runtime exception with the specified detail message and cause.</p>
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated in this runtime exception's detail message.</p>
   *
   * @param interledgerError An instance of {@link InterledgerError}.
   * @param message          the detail message (which is saved for later retrieval by the {@link
   *                         #getMessage()} method).
   * @param cause            the cause (which is saved for later retrieval by the {@link
   *                         #getCause()} method). (A <tt>null</tt> value is permitted, and
   *                         indicates that the cause is nonexistent or unknown.)
   * @since 1.4
   */
  public InterledgerException(
      final InterledgerError interledgerError,
      final String message,
      final Throwable cause
  ) {
    super(message, cause);
    this.interledgerError = Optional.ofNullable(interledgerError);
  }

  /**
   * Constructs a new runtime exception with the specified cause and a
   * detail message of <tt>(cause==null ? null : cause.toString())</tt>
   * (which typically contains the class and detail message of
   * <tt>cause</tt>).  This constructor is useful for runtime exceptions
   * that are little more than wrappers for other throwables.
   *
   * @param interledgerError An instance of {@link InterledgerError}.
   * @param cause            the cause (which is saved for later retrieval by the {@link
   *                         #getCause()} method). (A <tt>null</tt> value is permitted, and
   *                         indicates that the cause is nonexistent or unknown.)
   * @since 1.4
   */
  public InterledgerException(final InterledgerError interledgerError, final Throwable cause) {
    super(cause);
    this.interledgerError = Optional.ofNullable(interledgerError);
  }

  /**
   * Constructs a new runtime exception with the specified detail
   * message, cause, suppression enabled or disabled, and writable
   * stack trace enabled or disabled.
   *
   * @param interledgerError   An instance of {@link InterledgerError}.
   * @param message            the detail message.
   * @param cause              the cause.  (A {@code null} value is permitted, and indicates that
   *                           the cause is nonexistent or unknown.)
   * @param enableSuppression  whether or not suppression is enabled or disabled
   * @param writableStackTrace whether or not the stack trace should be writable
   * @since 1.7
   */
  protected InterledgerException(
      final InterledgerError interledgerError,
      final String message,
      final Throwable cause,
      final boolean enableSuppression,
      final boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.interledgerError = Optional.ofNullable(interledgerError);
  }

  /**
   * Accessor for {@code interledgerError} contained in this exception.
   */
  public Optional<InterledgerError> getInterledgerError() {
    return interledgerError;
  }
}
