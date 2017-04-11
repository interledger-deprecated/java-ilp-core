package org.interledger;

/**
 * An interface that defines how Interledger Packets are typed.  This interface is not an enum in
 * order to allow this library to model non-standard packet types.
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
  String getTypeUri();

  /**
   * Indicates whether some other object is "equal to" this one.  Implementation must only use
   * {@link #getTypeUri()} when calculating equality, and ensure that {@link #hashCode()} conforms
   * to its contract.
   *
   * @see java.lang.Object
   */
  @Override
  boolean equals(Object obj);
}
