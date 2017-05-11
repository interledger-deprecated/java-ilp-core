package org.interledger.ledger.money.format;

import static java.util.Objects.requireNonNull;

import org.interledger.ledger.model.LedgerInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Objects;
import java.util.Optional;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatContext;
import javax.money.format.AmountFormatContextBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryParseException;


/**
 * A formatter for String amounts that considers the scale and precision of the ledger that gives
 * context to the amount.
 * 
 * @author adrianhopebailie
 */
public class LedgerSpecificDecimalMonetaryAmountFormat implements MonetaryAmountFormat {

  private static final String STYLE = "LedgerSpecificDecimalMonetaryAmountFormatForJson";

  private static final String PATTERN = "0.0;-0.0";

  private static final DecimalFormatSymbols SYMBOLS = getStringDecimalFormatSymbols();

  public static final String KEY_PRECISION = "precision";

  public static final String KEY_SCALE = "scale";

  public static final String NEGATIVE_INFINITY = "-infinity";

  private final DecimalFormat format;
  private final CurrencyUnit currencyUnit;
  private final AmountFormatContext context;

  /**
   * Constructs a monetary amount format that matches the currency unit, precision and scale
   * stipulated by the ledger.
   * 
   * @param ledgerInfo Information about the ledger for which this format will apply.
   */
  public LedgerSpecificDecimalMonetaryAmountFormat(LedgerInfo ledgerInfo) {
    this(ledgerInfo.getCurrencyUnit(), ledgerInfo.getPrecision(), ledgerInfo.getScale());
  }

  /**
   * Constructs a monetary amount format for the given currency unit, precision and scale.
   * 
   * @param currencyUnit
   *    The unit of currency.
   * @param precision
   *    Indicates the total number of digits used to represent an amount.
   * @param scale
   *    Indicates the number of digits after the decimal place.
   */
  public LedgerSpecificDecimalMonetaryAmountFormat(CurrencyUnit currencyUnit, int precision,
      int scale) {
    Objects.requireNonNull(currencyUnit);

    this.currencyUnit = currencyUnit;
    this.format = new DecimalFormat(PATTERN, SYMBOLS);
    // Cause an exception to be thrown if parsing a number that will lose data
    this.format.setRoundingMode(RoundingMode.UNNECESSARY); 
    this.format.setMaximumIntegerDigits(precision);
    this.format.setMinimumFractionDigits(scale);
    this.format.setMaximumFractionDigits(scale);

    this.context = AmountFormatContextBuilder
        .of(STYLE)
        .set(KEY_SCALE, scale)
        .set(KEY_PRECISION, precision).build();
  }

  @Override
  public AmountFormatContext getContext() {
    return this.context;
  }

  @Override
  public void print(Appendable appendable, MonetaryAmount amount) throws IOException {
    requireNonNull(appendable).append(queryFrom(amount));
  }

  @Override
  public MonetaryAmount parse(CharSequence text) throws MonetaryParseException {
    Objects.requireNonNull(text);
    try {
      Number number = this.format.parse(text.toString());
      return Monetary.getDefaultAmountFactory().setCurrency(currencyUnit).setNumber(number)
          .create();
    } catch (Exception exception) {
      // Special case.
      if (NEGATIVE_INFINITY.equals(text)) {
        return null;
      }
      throw new MonetaryParseException(exception.getMessage(), text, 0);
    }
  }

  @Override
  public String queryFrom(MonetaryAmount amount) {
    return Optional.ofNullable(amount)
        .map(m -> this.format.format(amount.getNumber().numberValue(BigDecimal.class)))
        .orElse("null");
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.format, currencyUnit);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (LedgerSpecificDecimalMonetaryAmountFormat.class.isInstance(obj)) {
      LedgerSpecificDecimalMonetaryAmountFormat other =
          LedgerSpecificDecimalMonetaryAmountFormat.class.cast(obj);
      return Objects.equals(other.currencyUnit, currencyUnit);
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(LedgerSpecificDecimalMonetaryAmountFormat.class.getName()).append('{')
        .append(" decimalFormat: ").append(this.format).append(',').append(" precision: ")
        .append(this.context.getInt(KEY_PRECISION)).append(',').append(" scale: ")
        .append(this.context.getInt(KEY_SCALE)).append(',').append(" currencyUnit: ")
        .append(currencyUnit).append('}');
    return sb.toString();
  }

  private static DecimalFormatSymbols getStringDecimalFormatSymbols() {
    DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
    symbols.setDecimalSeparator('.');
    symbols.setDigit('#');
    symbols.setInfinity("infinity");
    symbols.setMinusSign('-');
    symbols.setPatternSeparator(';');
    symbols.setZeroDigit('0');
    return symbols;
  }

}
