package org.interledger.codecs.btp.packettypes;

import org.interledger.InterledgerRuntimeException;

import java.util.Objects;

/**
 * An interface that defines how Interledger Packets are typed using ASN.1 OER encoding.
 */
public interface BtpPacketType {

  int BTP_RESPONSE_PACKET_TYPE = 1;
  int BTP_ERROR_PACKET_TYPE = 2;
  int BTP_PREPARE_PACKET_TYPE = 3;
  int BTP_FULFILL_PACKET_TYPE = 4;
  int BTP_REJECT_PACKET_TYPE = 5;
  int BTP_MESSAGE_PACKET_TYPE = 6;

  /**
   * A helper method that will translate an integer into an instance of {@link
   * BtpPacketType}. Note that this method only handles standard Btp packets types.
   * To operate upon non-standard packets, a different method should be used.
   *
   * @param type The integer type.
   *
   * @return An instance of {@link BtpPacketType}.
   *
   * @throws InvalidPacketTypeException If the supplied {@code type} is invalid.
   */
  static BtpPacketType fromTypeId(final int type) throws InvalidPacketTypeException {
    switch (type) {
      case BTP_RESPONSE_PACKET_TYPE:
        return new ResponsePacketType();
      case BTP_ERROR_PACKET_TYPE:
        return new ErrorPacketType();
      case BTP_PREPARE_PACKET_TYPE:
        return new PreparePacketType();
      case BTP_FULFILL_PACKET_TYPE:
        return new FulfillPacketType();
      case BTP_REJECT_PACKET_TYPE:
        return new RejectPacketType();
      case BTP_MESSAGE_PACKET_TYPE:
        return new MessagePacketType();
      default:
        throw new InvalidPacketTypeException(
          String.format("%s is an unsupported Packet Type!", type));
    }
  }

  /**
   * The packet's type identifier, as specified by IL-RFC-3.
   *
   * @return An {@link Integer} representing the type of this packet.
   */
  Integer getTypeIdentifier();


  /**
   * An exception that indicates if a packet type is invalid for the current implementation.
   */
  class InvalidPacketTypeException extends InterledgerRuntimeException {

    private static final long serialVersionUID = 6086784345849001539L;

    public InvalidPacketTypeException(String message) {
      super(message);
    }
  }

  /**
   * An abstract implementation of {@link BtpPacketType}.
   */
  abstract class AbstractBtpPacketType implements BtpPacketType {

    private final Integer typeIdentifier;

    protected AbstractBtpPacketType(final Integer typeIdentifier) {
      this.typeIdentifier = Objects.requireNonNull(typeIdentifier);
    }

    public Integer getTypeIdentifier() {
      return typeIdentifier;
    }

    @Override
    public String toString() {
      return "AbstractInterledgerPacketType{"
        + "typeIdentifier=" + typeIdentifier
        + '}';
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }

      AbstractBtpPacketType that = (AbstractBtpPacketType) obj;

      return typeIdentifier.equals(that.typeIdentifier);
    }

    @Override
    public int hashCode() {
      return typeIdentifier.hashCode();
    }
  }
}
