package com.primalimited.core.math;

import com.primalimited.core.dval.Dval;

public class MathUtil {
  static final double DOUBLES_EQUAL_COMPARE_TOLERANCE = 1e-12;

  /**
   * Compares two double values using default tolerance.
   *
   * @param a first double value
   * @param b second double value
   * @return true if difference between doubles is less than default tolerance,
   * false otherwise
   * @see common.dval.Dval
   */
  public static boolean doublesEqual(double a, double b) {
    return doublesEqual(a, b, DOUBLES_EQUAL_COMPARE_TOLERANCE);
  }

  /**
   * Compares two double values using the specified tolerance.
   *
   * @param a first double value
   * @param b second double value
   * @param tolerance compare tolerance
   * @return true if difference between doubles is less than tolerance,
   * false otherwise
   * @see common.dval.Dval
   */
  public static boolean doublesEqual(double a, double b, double tolerance) {
    if (Dval.isDval(a) && Dval.isDval(b))
      return true;
    if (Dval.isDval(a) || Dval.isDval(b))
      return false;
    return Math.abs(a - b) < tolerance;
  }
  
  /**
   * Convert degrees to radians.
   *
   * @param degrees the degrees value to convert.
   * @return the radian value for degrees.
   */
  public static double degreesToRadians(double degrees) {
    return (degrees * (Math.PI / 180.0));
  }

  /**
   * Convert radians to degrees.
   *
   * @param radians the radians value to convert.
   * @return the degrees value for radians.
   */
  public static double radiansToDegrees(double radians) {
    return (radians * (180.0 / Math.PI));
  }
}
