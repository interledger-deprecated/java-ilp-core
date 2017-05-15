package org.interledger.wire;

import org.interledger.wire.codecs.packets.PaymentPacketType;
import org.interledger.wire.codecs.packets.QuoteByDestinationRequestPacketType;
import org.interledger.wire.codecs.packets.QuoteByDestinationResponsePacketType;
import org.interledger.wire.codecs.packets.QuoteBySourceRequestPacketType;
import org.interledger.wire.codecs.packets.QuoteBySourceResponsePacketType;
import org.interledger.wire.codecs.packets.QuoteLiquidityRequestPacketType;
import org.interledger.wire.codecs.packets.QuoteLiquidityResponsePacketType;

import java.net.URI;
import java.util.Objects;

/**
 * An interface that defines how Interledger Packets are typed using ASN.1 OER encoding.
 */
public interface InterledgerPacketType {

  Integer ILP_PAYMENT_TYPE = 1;
  Integer ILQP_QUOTE_LIQUIDITY_REQUEST_TYPE = 2;
  Integer ILQP_QUOTE_LIQUIDITY_RESPONSE_TYPE = 3;
  Integer ILQP_QUOTE_BY_SOURCE_AMOUNT_REQUEST_TYPE = 4;
  Integer ILQP_QUOTE_BY_SOURCE_AMOUNT_RESPONSE_TYPE = 5;
  Integer ILQP_QUOTE_BY_DESTINATION_AMOUNT_REQUEST_TYPE = 6;
  Integer ILQP_QUOTE_BY_DESTINATION_AMOUNT_RESPONSE_TYPE = 7;

  /**
   * The packet's type identifier, as specified by IL-RFC-3.
   *
   * @return An {@link Integer} representing the type of this packet.
   */
  Integer getTypeIdentifier();

  /**
   * A URI representing the formal type of this packet per the Interledger Header Type registry
   * maintained at IANA.
   *
   * @return An instance of {@link String}.
   * @see "http://www.iana.org/assignments/interledger-header-types"
   */
  URI getTypeUri();

  /**
   * A helper method that will translate an integer into an instance of {@link
   * InterledgerPacketType}.  Note that this method only handled standard Interledger packets types.
   * To operate upon non-standard packets, a different method should be used.
   *
   * @param type The integer type.
   * @return An instance of {@link InterledgerPacketType}.
   * @throws InvalidPacketTypeException If the supplied {@code type} is invalid.
   */
  static InterledgerPacketType fromTypeId(final int type) throws InvalidPacketTypeException {
    switch (type) {
      case 1:
        // ILP_PAYMENT_TYPE
        return new PaymentPacketType();
      case 2:
        // ILQP_QUOTE_LIQUIDITY_REQUEST_TYPE
        return new QuoteLiquidityRequestPacketType();
      case 3:
        // ILQP_QUOTE_LIQUIDITY_RESPONSE_TYPE;
        return new QuoteLiquidityResponsePacketType();
      case 4:
        // ILQP_QUOTE_BY_SOURCE_AMOUNT_REQUEST_TYPE
        return new QuoteBySourceRequestPacketType();
      case 5:
        // ILQP_QUOTE_BY_SOURCE_AMOUNT_RESPONSE_TYPE
        return new QuoteBySourceResponsePacketType();
      case 6:
        // ILQP_QUOTE_BY_DESTINATION_AMOUNT_REQUEST_TYPE;
        return new QuoteByDestinationRequestPacketType();
      case 7:
        return new QuoteByDestinationResponsePacketType();
      case 0:
      default:
        throw new InvalidPacketTypeException(
            String.format("%s is an unsupported Packet Type!", type));
    }
  }

  /**
   * An exception that indicates if a packet type is invalid for the current implementation.
   */
  class InvalidPacketTypeException extends RuntimeException {

    public InvalidPacketTypeException(String message) {
      super(message);
    }
  }

  /**
   * An abstract implementation of {@link InterledgerPacketType}.
   */
  abstract class AbstractInterledgerPacketType implements InterledgerPacketType {

    private final Integer typeIdentifier;
    private final URI typeUri;

    protected AbstractInterledgerPacketType(final Integer typeIdentifier, final URI typeUri) {
      this.typeIdentifier = Objects.requireNonNull(typeIdentifier);
      this.typeUri = Objects.requireNonNull(typeUri);
    }

    public Integer getTypeIdentifier() {
      return typeIdentifier;
    }

    public URI getTypeUri() {
      return typeUri;
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("AbstractInterledgerPacketType{");
      sb.append("typeIdentifier=").append(typeIdentifier);
      sb.append(", typeUri=").append(typeUri);
      sb.append('}');
      return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }

      AbstractInterledgerPacketType that = (AbstractInterledgerPacketType) obj;

      if (!typeIdentifier.equals(that.typeIdentifier)) {
        return false;
      }
      return typeUri.equals(that.typeUri);
    }

    @Override
    public int hashCode() {
      int result = typeIdentifier.hashCode();
      result = 31 * result + typeUri.hashCode();
      return result;
    }
  }
}
