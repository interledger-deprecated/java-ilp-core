package org.interledger;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A builder for immutable instances of {@link InterledgerAddress}.
 *
 * <p> <em>NOTE: j{@code InterledgerAddressBuilder} is not thread-safe and generally should not be
 * stored in a field or collection, but instead used immediately to create instances.</em> </p>
 */
public class InterledgerAddressBuilder {

  private String value;

  /**
   * No-args Constructor.
   */
  public InterledgerAddressBuilder() {
  }

  /**
   * Constructor to enable copy-functionality.
   */
  public InterledgerAddressBuilder(final InterledgerAddress interledgerAddress) {
    Objects.requireNonNull(interledgerAddress, "InterledgerAddress must not be null!");
    this.value = interledgerAddress.getValue();
  }

  /**
   * Builder method to actually construct an instance of {@link InterledgerAddress} from the data in
   * this builder.
   */
  public InterledgerAddress build() {
    return new InterledgerAddressImpl(this);
  }

  /**
   * Assign a new value to this builder.
   *
   * @param value A {@link String} representing this builder's "value", which is the string version
   *     of an Interledger Address.
   */
  public InterledgerAddressBuilder value(final String value) {
    this.value = Objects.requireNonNull(value, "value must not be null!");
    return this;
  }

  /**
   * Static helper method to construct a new builder.
   *
   * @return An instance of {@link InterledgerAddressBuilder}.
   */
  public static InterledgerAddressBuilder builder() {
    return new InterledgerAddressBuilder();
  }

  /**
   * A private, immutable implementation of {@link InterledgerAddress}.  To construct an instance of
   * this class, use an instance of {@link InterledgerAddressBuilder}.
   */
  private static final class InterledgerAddressImpl implements InterledgerAddress {

    private static final String REGEX
        = "(?=^.{1,1023}$)"
        + "^(g|private|example|peer|self|test[1-3])[.]([a-zA-Z0-9_~-]+[.])*([a-zA-Z0-9_~-]+)?$";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private final String value;

    /**
     * Required-args Constructor.
     *
     * @param builder An non-null instance of {@link InterledgerAddressBuilder} to construct a new
     *     instance from.
     */
    private InterledgerAddressImpl(final InterledgerAddressBuilder builder) {
      Objects.requireNonNull(builder, "InterledgerAddressBuilder must not be null!");
      Objects.requireNonNull(builder.value, "InterledgerAddress must not be null!");

      if (!isValidInterledgerAddress(builder.value)) {
        throw new IllegalArgumentException(
            "Invalid characters in address.  Reference Interledger RFC-15 for proper format.");
      }

      this.value = builder.value;
    }

    /**
     * Helper method to determine if an Interledger Address conforms to the specifications outlined
     * in Interledger RFC #15.
     *
     * @param value A {@link String} representing a potential Interledger Address value.
     * @return {@code true} if the supplied {@code value} conforms to the requirements of RFC 15;
     * {@code false} otherwise.
     * @see "https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses"
     */
    private boolean isValidInterledgerAddress(final String value) {
      Objects.requireNonNull(value);
      return PATTERN.matcher(value).matches();
    }

    /**
     * Accessor method for this address's {@link String} value. <p> NOTE: This is distinct from
     * {@link#toString()} to allow for the two values to diverge, e.g., for debugging or logging
     * purposes.
     * </p>
     *
     * @return The value of the {@code value} attribute
     */
    @Override
    public String getValue() {
      return this.value;
    }

    @Override
    public InterledgerAddress with(String segment) {
      Objects.requireNonNull(segment, "Segment String must not be null!");
      final StringBuilder sb = new StringBuilder(this.getValue());
      if (!this.isLedgerPrefix()) {
        sb.append(".");
      }
      sb.append(segment);

      return new InterledgerAddressBuilder().value(sb.toString()).build();
    }

    @Override
    public boolean equals(final Object object) {
      if (this == object) {
        return true;
      }
      if (object == null || getClass() != object.getClass()) {
        return false;
      }

      InterledgerAddressImpl interledgerAddressImpl = (InterledgerAddressImpl) object;

      return value.equals(interledgerAddressImpl.value);
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("InterledgerAddressImpl{");
      sb.append("value='").append(value).append('\'');
      sb.append('}');
      return sb.toString();
    }
  }
}
