package nl.naturalis.check;

import java.math.BigDecimal;
import java.math.BigInteger;

import static java.math.BigDecimal.ONE;

/*
 * Implementations of string-to-number checks in the CommonChecks class. Separate
 * from the CheckImpl class because there are quite a few of them.
 */
final class StringCheckImpls {

  private static final BigDecimal MIN_DOUBLE_BD =
      new BigDecimal(Double.toString(Double.MIN_VALUE));

  private static final BigDecimal MAX_DOUBLE_BD =
      new BigDecimal(Double.toString(Double.MAX_VALUE));

  private static final BigDecimal BIG_MIN_FLOAT =
      new BigDecimal(Float.toString(Float.MIN_VALUE));
  private static final BigDecimal BIG_MAX_FLOAT =
      new BigDecimal(Float.toString(Float.MAX_VALUE));

  private static final BigDecimal MIN_LONG_BD = new BigDecimal(Long.MIN_VALUE);

  private static final BigDecimal MAX_LONG_BD = new BigDecimal(Long.MAX_VALUE);

  private static final BigDecimal MIN_INT_BD = new BigDecimal(Integer.MIN_VALUE);

  private static final BigDecimal MAX_INT_BD = new BigDecimal(Integer.MAX_VALUE);

  private static final BigDecimal MIN_SHORT_BD = new BigDecimal(Short.MIN_VALUE);

  private static final BigDecimal MAX_SHORT_BD = new BigDecimal(Short.MAX_VALUE);

  private static final BigDecimal MIN_BYTE_BD = new BigDecimal(Byte.MIN_VALUE);

  private static final BigDecimal MAX_BYTE_BD = new BigDecimal(Byte.MAX_VALUE);

  private static final int MAX_INT_STR_LEN =
      String.valueOf(Integer.MAX_VALUE).length();

  private static final int MAX_SHORT_STR_LEN =
      String.valueOf(Short.MAX_VALUE).length();

  private StringCheckImpls() {
    throw new UnsupportedOperationException();
  }

  static boolean isLong(String s) {
    return parsable(s, MIN_LONG_BD, MAX_LONG_BD);
  }

  static boolean isInt(String s) {
    return parsable(s, MIN_INT_BD, MAX_INT_BD);
  }

  static boolean isShort(String s) {
    return parsable(s, MIN_SHORT_BD, MAX_SHORT_BD);
  }

  static boolean isByte(String s) {
    return parsable(s, MIN_BYTE_BD, MAX_BYTE_BD);
  }

  private static boolean parsable(String s, BigDecimal min, BigDecimal max) {
    if (!s.isEmpty()) {
      try {
        BigDecimal bd;
        return isRound(bd = new BigDecimal(s))
            && bd.compareTo(min) >= 0
            && bd.compareTo(max) <= 0;
      } catch (NumberFormatException ignored) {
      }
    }
    return false;
  }

  static boolean isDouble(String s) {
    if (!s.isEmpty()) {
      BigDecimal bd;
      try {
        bd = new BigDecimal(s);
      } catch (NumberFormatException e) {
        return false;
      }
      BigDecimal x = bd.abs();
      return x.compareTo(MIN_DOUBLE_BD) >= 0 && x.compareTo(MAX_DOUBLE_BD) <= 0;
    }
    return false;
  }

  static boolean isFloat(String s) {
    if (!s.isEmpty()) {
      BigDecimal bd;
      try {
        bd = new BigDecimal(s);
      } catch (NumberFormatException e) {
        return false;
      }
      BigDecimal x = bd.abs();
      return x.compareTo(BIG_MIN_FLOAT) >= 0 && x.compareTo(BIG_MAX_FLOAT) <= 0;
    }
    return false;
  }

  static boolean isRound(double d) {
    return isRound(new BigDecimal(Double.toString(d)));
  }

  static boolean isRound(BigDecimal bd) {
    // NB The 1st check is cheap and catches a lot of the cases.
    // The 2nd second is superfluous in the presence of the 3rd.
    // However, it still catches quite a few cases and seems
    // sufficiently cheap compared to the 3rd check that it seems
    // worth paying the price of wasted CPU cycles if we do fall
    // through to the 3rd check. The 3rd and 4th check are
    // equivalent, but the 3rd check seems more efficient. Needs
    // to be measured though.
    return bd.scale() <= 0
        || bd.stripTrailingZeros().scale() == 0
        || bd.divideToIntegralValue(ONE).compareTo(bd) == 0
        /*|| bd.remainder(ONE).signum() == 0 */;
  }

  static boolean isPlainInt(String s) {
    return isPlain(s, MAX_INT_STR_LEN, 31);
  }

  static boolean isPlainShort(String s) {
    return isPlain(s, MAX_SHORT_STR_LEN, 15);
  }

  private static boolean isPlain(String s, int maxStrLen, int maxBitLen) {
    if (s.isEmpty() || s.length() > maxStrLen) {
      return false;
    } else if (s.charAt(0) == '0') {
      return s.length() == 1;
    }
    // check for '+' and '-'
    char c;
    if ((c = s.charAt(0)) < '0' || c > '9') {
      return false;
    }
    try {
      return new BigInteger(s).bitLength() <= maxBitLen;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
