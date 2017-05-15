package org.interledger.wire.codecs;

import org.interledger.wire.InterledgerPacket;
import org.interledger.wire.InterledgerPacketType;
import org.interledger.wire.codecs.packets.AbstractInterledgerPacketHandler;
import org.interledger.wire.codecs.packets.InterledgerPacketCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A contextual object for matching instances of {@link Codec} to specific class types.
 */
public class CodecContext {

  /**
   * A map of codec that can encode/decode based on a typeId prefix.  This is used when an
   * undetermined packet of bytes are coming in off the wire, and we need to determine how to
   * decode these bytes based on the first typeId header.
   */
  private final Map<InterledgerPacketType, Class<?>> packetCodecs;

  /**
   * A map of codecs that can encode/decode based on a class type.  This is for
   * encoding/decoding
   * objects that are part of a known packet layout.
   */
  private final Map<Class<?>, Codec<?>> codecs;

  /**
   * No-args Constructor.
   */
  public CodecContext() {
    this.packetCodecs = new ConcurrentHashMap<>();
    this.codecs = new ConcurrentHashMap<>();
  }

  public InterledgerPacket read(final InterledgerPacketType typeId, final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(inputStream);
    return (InterledgerPacket) lookup(typeId).read(this, inputStream);
  }

  /**
   * Helper method that accepts an {@link InputStream}, detects the type of the packet to be read
   * and decodes the packet to an instance of {@link InterledgerPacket}.
   * <p>
   * Because {@link InterledgerPacket} is simply a marker interface, callers might prefer to
   * utilize the functionality supplied by {@link #readAndHandle(InputStream,
   * AbstractInterledgerPacketHandler)}.</p>
   *
   * @param inputStream An instance of {@link InputStream} that contains bytes in a certain
   *                    encoding.
   * @return An instance of {@link InterledgerPacket}.
   * @throws IOException If anything goes wrong reading from the {@code inputStream}.
   */
  public InterledgerPacket read(final InputStream inputStream) throws IOException {
    Objects.requireNonNull(inputStream);

    final InterledgerPacketType type = InterledgerPacketType.fromTypeId(inputStream.read());
    return read(type, inputStream);
  }

  /**
   * Helper method that accepts an {@link InputStream} and a type hint, and then decodes the input
   * to the appropriate response payload.
   *
   * @param type        An instance of {@link Class} that indicates the type that should be
   *                    decoded.
   * @param inputStream An instance of {@link InputStream} that contains bytes in a certain
   *                    encoding.
   * @param <T>         The type of object to return, based upon the supplied type of {@code type}.
   * @return An instance of {@link T}.
   * @throws IOException If anything goes wrong reading from the {@code buffer}.
   */
  public <T> T read(final Class<T> type, final InputStream inputStream) throws IOException {
    Objects.requireNonNull(type);
    Objects.requireNonNull(inputStream);

    if (InterledgerPacket.class.isAssignableFrom(type)) {
      inputStream.read(); // swallow type field
    }
    return lookup(type).read(this, inputStream);
  }

  /**
   * Register a converter associated to the supplied {@code type}.
   *
   * @param type      An instance of {link Class} of type {@link T}.
   * @param converter An instance of {@link Codec}.
   * @param <T>       An instance of {@link T}.
   * @return A {@link CodecContext} for the supplied {@code type}.
   */
  public <T> CodecContext register(final Class<T> type, final Codec<T> converter) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(converter);

    this.codecs.put(type, converter);
    if (converter instanceof InterledgerPacketCodec<?>) {
      InterledgerPacketCodec<?> commandTypeConverter = (InterledgerPacketCodec) converter;
      this.packetCodecs.put(commandTypeConverter.getTypeId(), type);
    }
    return this;
  }

  /**
   * Helper method that accepts an {@link InputStream}, detects the type of the packet to be read,
   * decodes the packet to the appropriate type and then passes it to the logic supplied by {@code
   * handler} for further processing.
   *
   * @param inputStream An instance of {@link InputStream} that contains bytes in a certain
   *                    encoding.
   * @param handler     An instance of {@link AbstractInterledgerPacketHandler} that contains
   *                    caller-supplied handling logic.
   * @param <R>         The type of object to return (specify {@link Void}) to return nothing.
   * @return An instance of {@link R}.
   * @throws IOException If anything goes wrong reading from the {@code inputStream}.
   */
  public <R> R readAndHandle(final InputStream inputStream,
                             final AbstractInterledgerPacketHandler<R> handler) throws IOException {
    Objects.requireNonNull(inputStream);
    Objects.requireNonNull(handler);

    final InterledgerPacketType type = InterledgerPacketType.fromTypeId(inputStream.read());
    return handler.execute(read(type, inputStream));
  }

  /**
   * Writes an instance of {@code instance} to the supplied {@link OutputStream}.
   *
   * @param type         An instance of {@link Class} that indicates the type that should be
   *                     encoded.
   * @param instance     An instance of {@link T} that will be encoded to the output stream.
   * @param outputStream An instance of {@link OutputStream} that will be written to.
   * @param <T>          The type of object to encode.
   * @return An instance of {@link CodecContext} for further operations.
   */
  public <T> CodecContext write(
      final Class<T> type, final T instance, final OutputStream outputStream
  ) throws IOException {
    Objects.requireNonNull(type);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    lookup(type).write(this, instance, outputStream);
    return this;
  }

  /**
   * Writes a generic instance of {@code Object} to the supplied {@link OutputStream}.
   *
   * @param instance     An instance of {@link Object} that will be encoded to the output stream.
   * @param outputStream An instance of {@link OutputStream} that will be written to.
   * @return An instance of {@link CodecContext} for further operations.
   */
  public CodecContext write(final Object instance, final OutputStream outputStream)
      throws IOException {
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    lookup(instance.getClass()).writeObject(this, instance, outputStream);
    return this;
  }

  /**
   * Helper method to lookup a {@link Codec} for the specified {@code type}.
   *
   * @param type An instance of {@link Class}.
   * @param <T>  The specific type of {@link Codec} to return.
   */
  private <T> Codec<T> lookup(final Class<T> type) {
    Objects.requireNonNull(type);

    if (codecs.containsKey(type)) {
      return (Codec<T>) codecs.get(type);
    } else if (codecs.containsKey(type.getSuperclass())) {
      return (Codec<T>) codecs.get(type.getSuperclass());
    } else {
      // Check for interfaces...
      return Arrays.stream(type.getInterfaces())
          .filter(codecs::containsKey)
          .map(interfaceClass -> (Codec<T>) codecs.get(interfaceClass))
          .findFirst()
          .orElseThrow(() ->
              new CodecException(
                  String.format("No codec registered for %s or its super classes!",
                      Codec.class.getName(), type.getName())
              )
          );
    }
  }

  /**
   * Lookup a specific {@link Codec} based upon the supplied {@code typeId}.
   *
   * @param typeId An instance of {@link InterledgerPacketType}.
   */
  private Codec<?> lookup(final InterledgerPacketType typeId) {
    if (packetCodecs.containsKey(typeId)) {
      return codecs.get(packetCodecs.get(typeId));
    }
    throw new CodecException(
        "No " + InterledgerPacketCodec.class.getName() + " registered for typeId " + typeId);
  }

  /**
   * Indicates if context has a registered {@link Codec} for the specified class.
   *
   * @param clazz An instance of {@link Class}.
   * @return {@code true} if the supplied class has a registered codec, {@code false} otherwise.
   */
  public boolean hasRegisteredCodec(final Class<?> clazz) throws IOException {
    Objects.requireNonNull(clazz);
    return codecs.containsKey(clazz);
  }

}