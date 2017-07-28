package org.interledger.codecs.oer;

import org.interledger.codecs.Codec;
import org.interledger.codecs.CodecContext;
import org.interledger.codecs.oer.OerGeneralizedTimeCodec.OerGeneralizedTime;
import org.interledger.codecs.oer.OerIA5StringCodec.OerIA5String;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Objects;

/**
 * An extension of {@link Codec} for reading and writing an ASN.1 OER GeneralizedTime.
 * An ASN.1 GeneralizedTime object is used to represent time values with a higher precision than 
 * done by the UTCTime ASN.1 type (allows a precision down to seconds). 
 * The ASN.1 GeneralizedTime syntax can include fraction-of-second details. The time value is 
 * expressed as a character string in any of the following formats:
 * <ul>
 *  <li>YYYYMMDDHH[MM[SS[.fff]]]</li>
 *  <li>YYYYMMDDHH[MM[SS[.fff]]]Z</li>
 *  <li>YYYYMMDDHH[MM[SS[.fff]]]+-HHMM</li>
 * </ul>
 * However, the interledger specs <b>mandate</b> that time be represented in YYYYMMDDTHHMMSS.fffZ.
 */
public class OerGeneralizedTimeCodec implements Codec<OerGeneralizedTime> {

  protected DateTimeFormatter generalizedTimeFormatter;
  protected ZoneId zoneIdZ;
  
  /**
   * Constructs a new instance of {@link OerGeneralizedTimeCodec}.
   */
  public OerGeneralizedTimeCodec() {
    this.generalizedTimeFormatter = new DateTimeFormatterBuilder()
        .appendValue(ChronoField.YEAR, 4)
        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
        .appendValue(ChronoField.DAY_OF_MONTH, 2)
        .appendValue(ChronoField.HOUR_OF_DAY, 2)
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
        .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
        .parseStrict()
        .appendFraction(ChronoField.MILLI_OF_SECOND, 3, 3, true)
        .appendZoneId()
        .toFormatter();
    
    this.zoneIdZ = ZoneId.of("Z");
  }
  
  @Override
  public OerGeneralizedTime read(CodecContext context, InputStream inputStream) throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(inputStream);
    
    final String timeString = context.read(OerIA5String.class, inputStream).getValue();
    
    final ZonedDateTime value = ZonedDateTime.parse(timeString, this.generalizedTimeFormatter);
    return new OerGeneralizedTime(value);
  }

  @Override
  public void write(CodecContext context, OerGeneralizedTime instance, OutputStream outputStream)
      throws IOException {
    Objects.requireNonNull(context);
    Objects.requireNonNull(instance);
    Objects.requireNonNull(outputStream);
    
    final String formattedTime =
        instance.getValue().withZoneSameInstant(this.zoneIdZ).format(this.generalizedTimeFormatter);
    context.write(new OerIA5String(formattedTime), outputStream);
  }
  
  
  /**
   * A typing mechanism for registering multiple codecs that operate on the same type, in this case
   * {@link ZonedDateTime}.
   */
  public static class OerGeneralizedTime {
    private final ZonedDateTime value;
    
    public OerGeneralizedTime(final ZonedDateTime value) {
      this.value = Objects.requireNonNull(value);
    }
    
    public ZonedDateTime getValue() {
      return this.value;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }

      OerGeneralizedTime other = (OerGeneralizedTime) obj;

      return value.equals(other.value);
    }

    @Override
    public int hashCode() {
      return this.value.hashCode();
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder("OerGeneralizedTime{");
      sb.append("value=").append(value);
      sb.append('}');
      return sb.toString();
    }
  }

}
