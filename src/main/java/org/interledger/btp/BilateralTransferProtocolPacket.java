package org.interledger.btp;

import org.interledger.btp.BilateralTransferProtocolPacket.Handler.AbstractHandler;

import java.util.Objects;

public interface BilateralTransferProtocolPacket {


  /**
   * The requestId of the call.
   *
   * @return An list of {@link SubProtocolData}.
   */
  long getRequestId();


  /**
   * A handler interface that defines all types of {@link BilateralTransferProtocolPacket} to
   * handle. For actual usage, consider an instance of {@link Handler.AbstractHandler}, which
   * provides useful scaffolding to assist in actually handling concrete packets at runtime.
   */
  interface Handler<R> {

    /**
     * The main handler method to coerce an instance of {@link BilateralTransferProtocolPacket} into
     * its actual type, apply some business logic, and optionally return a value in response.
     *
     * @param packet An instance of {@link BilateralTransferProtocolPacket}.
     *
     * @return An instance of type {@link R}, in response to the supplied input.
     */
    R execute(BilateralTransferProtocolPacket packet);

    /**
     * A handler for allowing callers to specify logic based upon an unknown result type. This class
     * can be used in the following manner:
     *
     * <pre>
     * <code>
     *
     * final BilateralTransferProtocolPacket decodedPacket = context.read(asn1OerMessageBytes);
     * final String stringValue = new AbstractHandler&lt;String&gt;() {
     *   protected String handle(final MessagePacket btpMessage) {
     *      // ... do handling here.
     *      return btpMessage.toString();
     *   }
     * }.execute(decodedPacket); // be sure to call .execute!
     *
     * // Do something with 'stringValue'
     * </code>
     * </pre>
     *
     * @param <R> the type of the result of the handler.
     */
    abstract class AbstractHandler<R> implements Handler<R> {

      /**
       * Handle an instance of {@link MessagePacket}.
       *
       * @param btpMessage An instance of {@link MessagePacket}.
       *
       * @return An instance of type {@link R}, in response to the supplied input.
       */
      protected abstract R handle(final MessagePacket btpMessage);

      // TODO: Handle the rest of the BTP packets!

      /**
       * The main handler method to coerce an instance of {@link BilateralTransferProtocolPacket}
       * into its actual type, apply some business logic, and optionally return a value in response.
       *
       * @param packet An instance of {@link BilateralTransferProtocolPacket}.
       *
       * @return An instance of type {@link R}, in response to the supplied input.
       */
      @Override
      public final R execute(final BilateralTransferProtocolPacket packet) {
        Objects.requireNonNull(packet);
        if (packet instanceof MessagePacket) {
          return this.handle((MessagePacket) packet);
        } else {
          throw new BtpRuntimeException("Unhandled BilateralTransferProtocolPacket: " + packet);
        }
      }

      /**
       * A utility class that provides default implementations of {@link AbstractHandler} so that a
       * caller is only forced to implement the handlers that are of interest. The idea behind this
       * class is that an implementor will only override the methods that are desired to be handled,
       * and if any unimplemented methods are called, an exception will be thrown. For example, if a
       * caller knows that the result is going to be of type {@link MessagePacket},
       * then it is not useful to add boilerplate implementations of the other handler methods that
       * do nothing, just to satisfy the abstract-class requirements of {@link AbstractHandler}.
       */
      public static class HelperHandler<R> extends AbstractHandler<R> {

        @Override
        protected R handle(MessagePacket btpMessage) {
          throw new BtpRuntimeException(
              "Not yet implemented. Override this method to provide a useful implementation!");
        }

        // TODO: Handle the rest of the BTP packets!

      }
    }
  }

  /**
   * A handler interface that defines all types of {@link BilateralTransferProtocolPacket} to
   * handle. For actual usage, consider an instance of {@link VoidHandler.AbstractVoidHandler},
   * which provides useful scaffolding to assist in actually handling concrete packets at runtime.
   */
  interface VoidHandler {

    /**
     * The main handler method to coerce an instance of {@link BilateralTransferProtocolPacket}
     * into its actual type, apply some business logic, and optionally return a value in response.
     *
     * @param packet An instance of {@link BilateralTransferProtocolPacket}.
     */
    void execute(BilateralTransferProtocolPacket packet);

    /**
     * An abstract implementation of {@link VoidHandler} for allowing callers to specify logic based
     * upon an unknown result type extending {@link BilateralTransferProtocolPacket}. This class can
     * be used in the following manner:
     *
     * <pre>
     * <code>
     * final BilateralTransferProtocolPacket decodedPacket = context.read(asn1OerMessageBytes);
     * new AbstractVoidHandler() {
     *   protected void handle(final MessagePacket btpMessage) {
     *      // ... do handling here.
     *   }
     * }.execute(decodedPacket); // be sure to call .execute!
     * </code>
     * </pre>
     */
    abstract class AbstractVoidHandler implements VoidHandler {

      /**
       * Handle an instance of {@link MessagePacket}.
       *
       * @param btpMessage An instance of {@link MessagePacket}.
       */
      protected abstract void handle(final MessagePacket btpMessage);


      // TODO: Handle the rest of the BTP packets!

      /**
       * The main handler method to coerce an instance of {@link BilateralTransferProtocolPacket}
       * into its actual type, apply some business logic, and optionally return a value in response.
       *
       * @param packet An instance of {@link BilateralTransferProtocolPacket}.
       */
      public final void execute(final BilateralTransferProtocolPacket packet) {
        Objects.requireNonNull(packet);
        if (packet instanceof MessagePacket) {
          this.handle((MessagePacket) packet);
        } else {
          throw new BtpRuntimeException("Unhandled BilateralTransferProtocolPacket: " + packet);
        }

        // TODO: Handle the rest of the BTP packets!

      }

      /**
       * A utility class that provides default implementations of {@link AbstractVoidHandler}
       * methods so that a caller is only forced to implement the handlers that are of interest. The
       * idea behind this class is that an implementor will only override the methods that are
       * desired to be handled, and if any unimplemented methods are called, an exception will be
       * thrown. For example, if a caller knows that the result is going to be of type {@link
       * MessagePacket}, then it is not useful to add boilerplate implementations of the other
       * handler methods that do nothing, just to satisfy the abstract-class requirements of {@link
       * AbstractHandler}.
       */
      public static class HelperHandler extends AbstractVoidHandler {

        @Override
        protected void handle(MessagePacket btpMessage) {
          throw new BtpRuntimeException(
              "Not yet implemented. Override this method to provide a useful implementation!");
        }

        // TODO: Handle the rest of the BTP packets!

      }
    }
  }
}
