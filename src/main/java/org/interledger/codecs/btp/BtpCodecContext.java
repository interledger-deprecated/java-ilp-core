package org.interledger.codecs.btp;

import org.interledger.btp.BilateralTransferProtocolPacket;
import org.interledger.btp.BilateralTransferProtocolPacket.Handler;
import org.interledger.btp.BilateralTransferProtocolPacket.VoidHandler;
import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.CodecException;
import org.interledger.codecs.btp.packettypes.BtpPacketType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A contextual object for matching instances of {@link Codec} to specific class types.
 */
public class BtpCodecContext extends CodecContext {

  /**
   * A map of codec that can encode/decode based on a typeId prefix. This is used when an
   * undetermined packet of bytes are coming in off the wire, and we need to determine how to decode
   * these bytes based on the first typeId header.
   */
  private final Map<BtpPacketType, Class<?>> btpPacketCodecs;

  /**
   * No-args Constructor.
   */
  public BtpCodecContext() {
    super();
    this.btpPacketCodecs = new ConcurrentHashMap<>();
  }


  /**
   * Register a converter associated to the supplied {@code type}.
   *
   * @param type      An instance of {link Class} of type {@link T}.
   * @param converter An instance of {@link Codec}.
   * @param <T>       An instance of {@link T}.
   *
   * @return A {@link BtpCodecContext} for the supplied {@code type}.
   */
  public <T> BtpCodecContext register(final Class<? extends T> type,
      final Codec<T> converter) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(converter);
    if (converter instanceof BtpPacketCodec<?>) {
      BtpPacketCodec<?> commandTypeConverter =
          (BtpPacketCodec) converter;
      this.btpPacketCodecs.put(commandTypeConverter.getTypeId(), type);
    } else {
      super.register(type, converter);
    }
    return this;
  }

  /**
   * Helper method that accepts an {@link InputStream}, detects the type of the packet to be read
   * and decodes the packet to {@link BilateralTransferProtocolPacket}. Because {@link
   * BilateralTransferProtocolPacket} is
   * simply a marker interface, callers might prefer to utilize the functionality supplied by
   * {@link
   * #readAndHandle(InputStream, Handler)} or {@link #readAndHandle(InputStream, VoidHandler)}.
   *
   * @param inputStream An instance of {@link InputStream} that contains bytes in a certain
   *                    encoding.
   *
   * @return An instance of {@link BilateralTransferProtocolPacket}.
   *
   * @throws IOException If anything goes wrong reading from the {@code inputStream}.
   */
  public BilateralTransferProtocolPacket readBtpPacket(final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(inputStream);

    final BtpPacketType type = BtpPacketType.fromTypeId(
        inputStream.read());
    return read(type, inputStream);
  }

  /**
   * Read a {@link BilateralTransferProtocolPacket} from the {@code inputStream}.
   *
   * @param typeId      An instance of {@link BtpPacketType}.
   * @param inputStream An instance of {@link InputStream}
   *
   * @return An instance of {@link BilateralTransferProtocolPacket} as read from the input stream.
   *
   * @throws IOException If anything goes wrong reading from the {@code inputStream}.
   */
  public BilateralTransferProtocolPacket read(final BtpPacketType typeId,
      final InputStream inputStream)
      throws IOException {
    Objects.requireNonNull(inputStream);
    return (BilateralTransferProtocolPacket) lookup(typeId).read(this, inputStream);
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
   *
   * @return An instance of {@link T}.
   *
   * @throws IOException If anything goes wrong reading from the {@code buffer}.
   */
  public <T> T read(final Class<T> type, final InputStream inputStream) throws IOException {
    Objects.requireNonNull(type);
    Objects.requireNonNull(inputStream);

    if (BilateralTransferProtocolPacket.class.isAssignableFrom(type)) {
      //noinspection ResultOfMethodCallIgnored
      inputStream.read(); // swallow type field
    }
    return lookup(type).read(this, inputStream);
  }

  /**
   * Helper method that accepts a {@link byte[]} and a type hint, and then decodes the input to the
   * appropriate response payload.
   *
   * <p>NOTE: This methods wraps IOExceptions in RuntimeExceptions.
   *
   * @param type An instance of {@link Class} that indicates the type that should be decoded.
   * @param data An instance of {@link byte[]} that contains bytes in a certain encoding.
   * @param <T>  The type of object to return, based upon the supplied type of {@code type}.
   *
   * @return An instance of {@link T}.
   */
  public <T> T read(final Class<T> type, final byte[] data) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(data);

    try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
      if (BilateralTransferProtocolPacket.class.isAssignableFrom(type)) {
        //noinspection ResultOfMethodCallIgnored
        bais.read(); // swallow type field
      }
      return lookup(type).read(this, bais);
    } catch (IOException e) {
      throw new CodecException("Unable to decode " + type.getCanonicalName(), e);
    }

  }

  /**
   * Read an object from the buffer according to the rules defined in the {@link
   * BtpCodecContext}, and
   * handle any terminating logic inside of {@code packetHandler}.
   *
   * @param inputStream   An instance of {@link InputStream} to read data from.
   * @param packetHandler A {@link VoidHandler} that allows callers to supply
   *                      business logic to be applied against the packet, depending on what the
   *                      runtime-version of the packet ultimately is.
   *
   * @throws IOException If anything goes wrong while reading from the InputStream.
   */
  public void readAndHandle(final InputStream inputStream,
      final VoidHandler packetHandler) throws IOException {

    Objects.requireNonNull(inputStream);
    Objects.requireNonNull(packetHandler);

    final BilateralTransferProtocolPacket btpPacket = this.readBtpPacket(inputStream);
    packetHandler.execute(btpPacket);
  }

  /**
   * Read an object from {@code inputStream} according to the rules defined in the {@code context},
   * handle any concrete logic inside of {@code packetHandler}, and return a result.
   *
   * @param inputStream      An instance of {@link InputStream} to read data from.
   * @param btpPacketHandler A {@link Handler} that allows callers to supply business
   *                         logic to be applied against the packet, depending on what the
   *                         runtime-version of the packet ultimately is, and then return a value.
   *
   * @return An instance of {@link R}.
   *
   * @throws IOException If anything goes wrong while reading from the InputStream.
   */
  public <R> R readAndHandle(final InputStream inputStream,
      final Handler<R> btpPacketHandler) throws IOException {
    Objects.requireNonNull(inputStream);
    Objects.requireNonNull(btpPacketHandler);

    final BilateralTransferProtocolPacket btpPacket = this.readBtpPacket(inputStream);
    return btpPacketHandler.execute(btpPacket);
  }

  /**
   * Writes an instance of {@code instance} to the supplied {@link OutputStream}.
   *
   * @param type         An instance of {@link Class} that indicates the type that should be
   *                     encoded.
   * @param instance     An instance of {@link T} that will be encoded to the output stream.
   * @param outputStream An instance of {@link OutputStream} that will be written to.
   * @param <T>          The type of object to encode.
   *
   * @return An instance of {@link BtpCodecContext} for further operations.
   */
  public <T> BtpCodecContext write(final Class<T> type, final T instance,
      final OutputStream outputStream) throws IOException {
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
   *
   * @return An instance of {@link BtpCodecContext} for further operations.
   */
  public BtpCodecContext write(final Object instance,
      final OutputStream outputStream)
      throws IOException {
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);

    lookup(instance.getClass()).writeObject(this, instance, outputStream);
    return this;
  }

  /**
   * Writes an instance of {@code instance} to an in-memory stream and returns the result as a
   * {@link byte[]}.
   *
   * <p>NOTE: This methods wraps any IOExceptions in a RuntimeException.
   *
   * @param type     An instance of {@link Class} that indicates the type that should be encoded.
   * @param instance An instance of {@link T} that will be encoded to the output stream.
   * @param <T>      The type of object to encode.
   *
   * @return The encoded object.
   */
  public <T> byte[] write(final Class<T> type, final T instance) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(instance);

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      lookup(type).write(this, instance, baos);
      return baos.toByteArray();
    } catch (IOException e) {
      throw new CodecException("Error encoding " + type.getCanonicalName(), e);
    }
  }

  /**
   * Writes a generic instance of {@code Object} to an in-memory stream and returns the result as a
   * {@link byte[]}.
   *
   * <p>NOTE: This methods wraps any IOExceptions in a RuntimeException.
   *
   * @param instance An instance of {@link Object} that will be encoded to the output stream.
   *
   * @return An instance of {@link BtpCodecContext} for further operations.
   */
  public byte[] write(final Object instance) {
    Objects.requireNonNull(instance);

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      lookup(instance.getClass()).writeObject(this, instance, baos);
      return baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Error encoding " + instance.getClass());
    }
  }


  /**
   * Lookup a specific {@link Codec} based upon the supplied {@code typeId}.
   *
   * @param typeId An instance of {@link BtpPacketType}.
   */
  private Codec<?> lookup(final BtpPacketType typeId) {
    if (btpPacketCodecs.containsKey(typeId)) {
      return lookup(btpPacketCodecs.get(typeId));
    }
    throw new CodecException(
        "No " + BtpPacketCodec.class.getName() + " registered for typeId "
            + typeId);
  }

}
