package org.interledger.wire.codecs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.interledger.wire.InterledgerPacket;
import org.interledger.wire.InterledgerPacketType;

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

  public InterledgerPacket read(InputStream buffer) throws IOException {
    final InterledgerPacketType type = InterledgerPacketType.fromTypeId(buffer.read());
    return read(type, buffer);
  }

  public <T> T read(final Class<T> type, final InputStream inputStream) throws IOException {
    Objects.requireNonNull(type);
    Objects.requireNonNull(inputStream);

    if (InterledgerPacket.class.isAssignableFrom(type)) {
      inputStream.read(); // swallow type field
    }
    return lookup(type).read(this, inputStream);
  }

  public <T> CodecContext write(
      final Class<T> type, final T instance, final OutputStream outputStream
  ) throws IOException {
    Objects.requireNonNull(type);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    lookup(type).write(this, instance, outputStream);
    return this;
  }

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

  private Codec<?> lookup(final InterledgerPacketType typeId) {
    if (packetCodecs.containsKey(typeId)) {
      return codecs.get(packetCodecs.get(typeId));
    }
    throw new CodecException(
        "No " + InterledgerPacketCodec.class.getName() + " registered for typeId " + typeId);
  }

  public boolean readable(final InterledgerPacketType typeId) {
    return packetCodecs.containsKey(typeId);
  }

  //  public boolean readable(final InputStream inputStream) throws IOException {
//    Objects.requireNonNull(inputStream);
//    final int type = inputStream.read();
//    return readable(type);
//  }
//

//  public boolean writable(final Object object) {
//    Objects.requireNonNull(object);
//    return codecs.containsKey(object.getClass());
//  }

  /**
   * Indicates if context has a registered {@link Codec} for the specified class.
   *
   * @param clazz An instance of {@link Class}.
   * @return {@code true} if the supplied class has a registered codec, {@code false} otherwise.
   * @throws IOException
   */

  public boolean hasRegisteredCodec(final Class<?> clazz) throws IOException {
    Objects.requireNonNull(clazz);
    return codecs.containsKey(clazz);
  }

}