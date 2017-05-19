package org.interledger.wire.codecs.packets;

import org.interledger.ilp.InterledgerPayment;
import org.interledger.ilqp.QuoteLiquidityRequest;
import org.interledger.ilqp.QuoteLiquidityResponse;
import org.interledger.wire.InterledgerPacket;

/**
 * A handler for allowing callers to specify logic based upon an unknown result type.  This class
 * can be used in the following manner:
 * <pre>
 * <code>
 * InterledgerPacket decodedPacket = context.read(asn1OerPaymentBytes);
 * new AbstractInterledgerPacketHandler&lt;String&gt;() {
 *   protected void handle(final InterledgerPayment interledgerPayment) {
 *      // ... do handling here.
 *   }
 * }.execute(decodedPacket); // be sure to call .execute!
 * </code>
 * </pre>
 */
public abstract class AbstractInterledgerPacketHandler<R> {

  /**
   * Handle an instance of {@link InterledgerPayment}.
   *
   * @param interledgerPayment An instance of {@link InterledgerPayment}.
   * @return An instance of type {@link R}, in response to the supplied input.
   */
  protected abstract R handle(final InterledgerPayment interledgerPayment);

  /**
   * Handle an instance of {@link QuoteLiquidityRequest}.
   *
   * @param quoteLiquidityRequest An instance of {@link QuoteLiquidityRequest}.
   * @return An instance of type {@link R}, in response to the supplied input.
   */
  protected abstract R handle(final QuoteLiquidityRequest quoteLiquidityRequest);

  /**
   * Handle an instance of {@link QuoteLiquidityResponse}.
   *
   * @param quoteLiquidityResponse An instance of {@link QuoteLiquidityResponse}.
   * @return An instance of type {@link R}, in response to the supplied input.
   */
  protected abstract R handle(final QuoteLiquidityResponse quoteLiquidityResponse);

  // TODO: Handle the rest of the ILQP packets!

  /**
   * The main handler method to coerce an instance of {@link InterledgerPacket} into its actual
   * type, apply some business logic, and optionally return a value in response.
   *
   * @param packet An instance of {@link InterledgerPacket}.
   * @return An instance of type {@link R}, in response to the suppllied input.
   */
  public final R execute(final InterledgerPacket packet) {
    if (packet instanceof InterledgerPayment) {
      return this.handle((InterledgerPayment) packet);
    } else if (packet instanceof QuoteLiquidityRequest) {
      return this.handle((QuoteLiquidityRequest) packet);
    } else if (packet instanceof QuoteLiquidityResponse) {
      return this.handle((QuoteLiquidityResponse) packet);
    } else {
      throw new RuntimeException(String.format("Unhandled InterledgerPacket: ", packet));
    }
  }

  /**
   * A utility class that provides default implementations of
   * {@link AbstractInterledgerPacketHandler} so that a caller is only forced to implement the
   * handlers that are of interest.
   */
  public static class Template<R> extends AbstractInterledgerPacketHandler<R> {

    @Override
    protected R handle(InterledgerPayment interledgerPayment) {
      return null;
    }

    @Override
    protected R handle(QuoteLiquidityRequest quoteLiquidityRequest) {
      return null;
    }

    @Override
    protected R handle(QuoteLiquidityResponse quoteLiquidityResponse) {
      return null;
    }
  }
}
