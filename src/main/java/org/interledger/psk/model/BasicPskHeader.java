package org.interledger.psk.model;

/**
 * Concrete implementation of a pre-shared-key header.
 */
public class BasicPskHeader implements PskMessageHeader {

  private String name;
  private String value;

  /**
   * Constructs a new {@link BasicPskHeader}.
   * 
   * @param name The name of the header. Must not be null or an empty string.
   * @param value The value associated with the header. Must not be null or an empty string.
   */
  public BasicPskHeader(String name, String value) {
    if (name == null || name.isEmpty() || name.contains("\n")) {
      throw new IllegalArgumentException(
          "Header name cannot be null or empty and must not contain line endings ('\n')");
    }

    if (value == null || value.isEmpty() || value.contains("\n")) {
      throw new IllegalArgumentException(
          "Header value cannot be null or empty and must not contain line endings ('\n')");
    }

    /* leading and trailing whitespace is optional, remove it. */
    this.name = name.trim();
    this.value = value.trim();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return name + ": " + value;
  }
}
