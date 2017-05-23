package org.interledger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.interledger.InterledgerPacket.Handler.AbstractHandler;
import org.interledger.InterledgerPacket.VoidHandler.AbstractVoidHandler;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.ilqp.QuoteLiquidityRequest;
import org.interledger.ilqp.QuoteLiquidityResponse;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for {@link InterledgerPacket}.
 */
public class HelperHandlerTest {

  /**
   * A private class used for testing...see below.
   */
  private static final class TestAbstractHandler extends AbstractHandler<String> {

    @Override
    protected String handle(InterledgerPayment interledgerPayment) {
      interledgerPayment.getDestinationAmount();
      return "a";
    }

    @Override
    protected String handle(QuoteLiquidityRequest quoteLiquidityRequest) {
      quoteLiquidityRequest.getRequestId();
      return "b";
    }

    @Override
    protected String handle(QuoteLiquidityResponse quoteLiquidityResponse) {
      quoteLiquidityResponse.getRequestId();
      return "c";
    }
  }

  /**
   * A private class used for testing...see below.
   */
  private static final class TestAbstractVoidHandler extends AbstractVoidHandler {

    @Override
    protected void handle(InterledgerPayment interledgerPayment) {
      interledgerPayment.getDestinationAmount();
    }

    @Override
    protected void handle(QuoteLiquidityRequest quoteLiquidityRequest) {
      quoteLiquidityRequest.getRequestId();
    }

    @Override
    protected void handle(QuoteLiquidityResponse quoteLiquidityResponse) {
      quoteLiquidityResponse.getRequestId();
    }
  }

  ////////////////////////////
  // Tests for AbstractHandler
  ////////////////////////////

  @Test(expected = NullPointerException.class)
  public void testAbstractHandler_NullExecute() throws Exception {
    try {
      new AbstractHandler.HelperHandler().execute(null);
    } catch (NullPointerException e) {
      throw e;
    }
  }

  @Test
  public void testAbstractHandler_InterledgerPayment() throws Exception {
    final InterledgerPayment interledgerPayment = mock(InterledgerPayment.class);
    final QuoteLiquidityRequest quoteLiquidityRequest = mock(QuoteLiquidityRequest.class);
    final QuoteLiquidityResponse quoteLiquidityResponse = mock(QuoteLiquidityResponse.class);

    final String actual = new TestAbstractHandler().execute(interledgerPayment);

    assertThat(actual, is("a"));
    Mockito.verify(interledgerPayment).getDestinationAmount();
    Mockito.verifyNoMoreInteractions(quoteLiquidityRequest);
    Mockito.verifyNoMoreInteractions(quoteLiquidityResponse);
  }

  @Test
  public void testAbstractHandler_QuoteLiquidityRequest() throws Exception {
    final InterledgerPayment interledgerPayment = mock(InterledgerPayment.class);
    final QuoteLiquidityRequest quoteLiquidityRequest = mock(QuoteLiquidityRequest.class);
    final QuoteLiquidityResponse quoteLiquidityResponse = mock(QuoteLiquidityResponse.class);

    final String actual = new TestAbstractHandler().execute(quoteLiquidityRequest);

    assertThat(actual, is("b"));
    Mockito.verifyNoMoreInteractions(interledgerPayment);
    Mockito.verify(quoteLiquidityRequest).getRequestId();
    Mockito.verifyNoMoreInteractions(quoteLiquidityResponse);
  }

  @Test
  public void testAbstractHandler_QuoteLiquidityResponse() throws Exception {
    final InterledgerPayment interledgerPayment = mock(InterledgerPayment.class);
    final QuoteLiquidityRequest quoteLiquidityRequest = mock(QuoteLiquidityRequest.class);
    final QuoteLiquidityResponse quoteLiquidityResponse = mock(QuoteLiquidityResponse.class);

    final String actual = new TestAbstractHandler().execute(quoteLiquidityResponse);

    assertThat(actual, is("c"));
    Mockito.verifyNoMoreInteractions(interledgerPayment);
    Mockito.verifyNoMoreInteractions(quoteLiquidityRequest);
    Mockito.verify(quoteLiquidityResponse).getRequestId();
  }

  ////////////////////////////
  // Tests for AbstractVoidHandler
  ////////////////////////////

  @Test(expected = NullPointerException.class)
  public void testAbstractVoidHandler_NullExecute() throws Exception {
    try {
      new AbstractVoidHandler.HelperHandler().execute(null);
    } catch (NullPointerException e) {
      throw e;
    }
  }

  @Test
  public void testAbstractVoidHandler_InterledgerPayment() throws Exception {
    final InterledgerPayment interledgerPayment = mock(InterledgerPayment.class);
    final QuoteLiquidityRequest quoteLiquidityRequest = mock(QuoteLiquidityRequest.class);
    final QuoteLiquidityResponse quoteLiquidityResponse = mock(QuoteLiquidityResponse.class);

    new TestAbstractVoidHandler().execute(interledgerPayment);

    Mockito.verify(interledgerPayment).getDestinationAmount();
    Mockito.verifyNoMoreInteractions(quoteLiquidityRequest);
    Mockito.verifyNoMoreInteractions(quoteLiquidityResponse);
  }

  @Test
  public void testAbstractVoidHandler_QuoteLiquidityRequest() throws Exception {
    final InterledgerPayment interledgerPayment = mock(InterledgerPayment.class);
    final QuoteLiquidityRequest quoteLiquidityRequest = mock(QuoteLiquidityRequest.class);
    final QuoteLiquidityResponse quoteLiquidityResponse = mock(QuoteLiquidityResponse.class);

    new TestAbstractHandler().execute(quoteLiquidityRequest);

    Mockito.verifyNoMoreInteractions(interledgerPayment);
    Mockito.verify(quoteLiquidityRequest).getRequestId();
    Mockito.verifyNoMoreInteractions(quoteLiquidityResponse);
  }

  @Test
  public void testAbstractVoidHandler_QuoteLiquidityResponse() throws Exception {
    final InterledgerPayment interledgerPayment = mock(InterledgerPayment.class);
    final QuoteLiquidityRequest quoteLiquidityRequest = mock(QuoteLiquidityRequest.class);
    final QuoteLiquidityResponse quoteLiquidityResponse = mock(QuoteLiquidityResponse.class);

    new TestAbstractHandler().execute(quoteLiquidityResponse);

    Mockito.verifyNoMoreInteractions(interledgerPayment);
    Mockito.verifyNoMoreInteractions(quoteLiquidityRequest);
    Mockito.verify(quoteLiquidityResponse).getRequestId();
  }

}