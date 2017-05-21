package org.interledger;

import org.interledger.ilp.InterledgerPayment;

/**
 * <p>A top-level interface for all Interledger objects that can be encoded and decoded as an
 * Interledger packet using some sort of encoding, such as ANS.1, JSON, Protobuf or some other
 * encoding. Not all POJOs in this library are considered Interledger "packets". For example, an
 * {@link InterledgerAddress} is used in many packet implementations, but is not something that is
 * sent by itself "on the wire" to facilitate Interledger operations. Conversely, {@link
 * InterledgerPayment} is an Interledger packet because it is sent by itself  "on the wire" to
 * facilitate Interledger operations.</p>
 */
public interface InterledgerPacket {}