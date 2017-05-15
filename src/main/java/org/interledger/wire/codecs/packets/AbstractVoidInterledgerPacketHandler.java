package org.interledger.wire.codecs.packets;

import org.interledger.ilp.InterledgerPayment;
import org.interledger.ilqp.QuoteLiquidityRequest;
import org.interledger.ilqp.QuoteLiquidityResponse;
import org.interledger.wire.InterledgerPacket;

/**
 * A handler for allowing callers to specify logic based upon an unknown result type. This class can
 * be used in the following manner:
 * <pre>
 * <code>
 * InterledgerPacket decodedPacket = context.read(asn1OerPaymentBytes);
 * new AbstractVoidInterledgerPacketHandler() {
 *   protected void handle(final InterledgerPayment interledgerPayment) {
 *      // ... do handling here.
 *   }
 * }.handle(decodedPacket); // be sure to call .handle!
 * </code>
 * </pre>
 */
public abstract class AbstractVoidInterledgerPacketHandler {

  /**
   * Handle an instance of {@link InterledgerPayment}.
   *
   * @param interledgerPayment An instance of {@link InterledgerPayment}.
   */
  protected abstract void handle(final InterledgerPayment interledgerPayment);

  /**
   * Handle an instance of {@link QuoteLiquidityRequest}.
   *
   * @param quoteLiquidityRequest An instance of {@link QuoteLiquidityRequest}.
   */
  protected abstract void handle(final QuoteLiquidityRequest quoteLiquidityRequest);

  /**
   * Handle an instance of {@link QuoteLiquidityResponse}.
   *
   * @param quoteLiquidityResponse An instance of {@link QuoteLiquidityResponse}.
   */
  protected abstract void handle(final QuoteLiquidityResponse quoteLiquidityResponse);

  // TODO: Handle the rest of the ILQP packets!

  /**
   * The main handler method to coerce an instance of {@link InterledgerPacket} into its actual
   * type, apply some business logic, and optionally return a value in response.
   *
   * @param packet An instance of {@link InterledgerPacket}.
   */
  public final void execute(final InterledgerPacket packet) {
    if (packet instanceof InterledgerPayment) {
      this.handle((InterledgerPayment) packet);
    } else {
      throw new RuntimeException(String.format("Unhandled InterledgerPacket: ", packet));
    }
  }

  /**
   * A utility class that provides default implementations of
   * {@link AbstractInterledgerPacketHandler} so that a caller is only forced to implement the
   * handlers that are of interest.
   */
  public static class Template extends AbstractVoidInterledgerPacketHandler {

    @Override
    protected void handle(InterledgerPayment interledgerPayment) {

    }

    @Override
    protected void handle(QuoteLiquidityRequest quoteLiquidityRequest) {

    }

    @Override
    protected void handle(QuoteLiquidityResponse quoteLiquidityResponse) {

    }
  }
}
