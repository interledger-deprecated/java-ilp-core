package org.interledger.psk.model;

import java.util.Base64;

/**
 * Convenience header representing the public Encryption header found in PSK messages.
 */
public class PskEncryptionHeader extends BasicPskHeader {

  /**
   * Constructs an instance of the header using the give encryption type.
   *
   * @param encryptionType The encryption type indicated.
   */
  public PskEncryptionHeader(final PskEncryptionType encryptionType) {
    super(PskMessageHeader.PublicHeaders.ENCRYPTION, encryptionType.toString());
  }

  /**
   * Constructs an instance of the header with the given encryption type and authentication tag.
   * value.
   *
   * @param encryptionType    The encryption type indicated.
   * @param authenticationTag The authentication tag value.
   */
  public PskEncryptionHeader(final PskEncryptionType encryptionType,
                             final byte[] authenticationTag) {
    super(PskMessageHeader.PublicHeaders.ENCRYPTION,
        encryptionType.toString() + " " + Base64.getUrlEncoder().encodeToString(authenticationTag));
  }

  public PskEncryptionHeader(final String name, final String value) {
    super(name, value);
  }

  /**
   * Convenience method to retrieve the authentication tag value, if it is present in the header.
   */
  public byte[] getAuthenticationTagValue() {
    String[] encryptionData = getValue().split(" ");

    if (encryptionData.length > 1) {
      return Base64.getUrlDecoder().decode(encryptionData[1]);
    }

    return null;
  }

  /**
   * Returns the encryption type indicated in the header.
   */
  public PskEncryptionType getEncryptionType() {
    String[] encryptionData = getValue().split(" ");

    return PskEncryptionType.fromString(encryptionData[0]);
  }

}
