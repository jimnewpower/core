package com.primalimited.core.dval;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;

public class Dval {
  public static final double DVAL_DOUBLE = 3.4E+76;
  public static final float DVAL_FLOAT = 3.4E+38f;
  public static final int DVAL_INT = Integer.MAX_VALUE;
  public static final long DVAL_LONG = Long.MAX_VALUE;
  public static final byte DVAL_BYTE = Byte.MIN_VALUE;

  private static final DoublePredicate isDVAL = (d) -> Dval.isDval(d);
  private static final DoublePredicate isNaN = (d) -> Double.isNaN(d);
  private static final DoublePredicate isInfinite = (d) -> Double.isInfinite(d);
  public static final DoublePredicate isValid = (d) -> 
      isDVAL.negate().test(d) && isNaN.negate().test(d) && isInfinite.negate().test(d);
  public static final Predicate<Double> VALID_DOUBLE_BOXED = p -> 
      (!Dval.isDval(p.doubleValue()) && !Double.isNaN(p.doubleValue()) && !Double.isInfinite(p.doubleValue()));

  /**
   * Class constructor (private).  This class contains only static methods,
   * therefore it is unnecessary to instantiate this class.
   */
  private Dval() {
  }

  /**
   * Determine if <code>Double</code> is a Dval.
   *
   * @return true if num &gt;= DVAL_DOUBLE, false otherwise.
   */
  public static boolean isDval(Double num) {
    return Double.compare(Dval.DVAL_DOUBLE, num.doubleValue()) == 0;
  }

  /**
   * Determine if <code>double</code> is a Dval.
   *
   * @return true if num == DVAL_DOUBLE, false otherwise.
   */
  public static boolean isDval(double num) {
    return Double.compare(Dval.DVAL_DOUBLE, num) == 0;
  }

  /**
   * Determine if <code>Float</code> is a Dval.
   *
   * @return true if num == DVAL_FLOAT, false otherwise.
   */
  public static boolean isDval(Float num) {
    return Float.compare(num, DVAL_FLOAT) == 0;
  }
  
  /**
   * Determine if <code>float</code> is a Dval.
   *
   * @return true if num &gt;= DVAL_FLOAT, false otherwise.
   */
  public static boolean isDval(float num) {
    return Float.compare(num, DVAL_FLOAT) == 0;
  }

  public static boolean isDval(byte num) {
    return num == DVAL_BYTE;
  }

  public static boolean isDval(Byte num) {
    return num.byteValue() == DVAL_BYTE;
  }

  /**
   * Determine if <code>Integer</code> is a Dval.
   *
   * @return true if num &gt;= DVAL_INT, false otherwise.
   */
  public static boolean isDval(Integer num) {
    return num.intValue() >= DVAL_INT;
  }
  
  /**
   * Determine if <code>int</code> is a Dval.
   *
   * @return true if num &gt;= DVAL_INT, false otherwise.
   */
  public static boolean isDval(int num) {
    return num >= DVAL_INT;
  }

  /**
   * Determine if <code>Long</code> is a Dval.
   *
   * @return true if num &gt;= DVAL_LONG, false otherwise.
   */
  public static boolean isDval(Long num) {
    return num.longValue() >= DVAL_LONG;
  }
  
  /**
   * Determine if <code>long</code> is a Dval.
   *
   * @return true if num &gt;= DVAL_LONG, false otherwise.
   */
  public static boolean isDval(long num) {
    return(num >= DVAL_LONG);
  }

  /**
   * Determine if <code>Number</code> is a Dval.
   *
   * @return true if num &gt;= (TYPE) DVAL, false otherwise.
   */
  public static boolean isDval(Number num) {
    Objects.requireNonNull(num);
    
    if (num instanceof Double)
      return(isDval((Double) num));

    if (num instanceof Float)
      return(isDval((Float) num));

    if (num instanceof Integer)
      return(isDval((Integer) num));

    if (num instanceof Long)
      return(isDval((Long) num));

    if (num instanceof Byte)
      return(isDval((Byte) num));

    throw new IllegalArgumentException(num.getClass().getName() + " type is not supported");
  }

  public static boolean hasDval(int[] array) {
    return Arrays.stream(array).anyMatch(i -> Dval.isDval(i));
  }

  public static boolean allValuesAreDval(int[] array) {
    return Arrays.stream(array).allMatch(i -> Dval.isDval(i));
  }

  public static boolean hasAnyNonDval(int[] array) {
    return Arrays.stream(array).anyMatch(i -> !Dval.isDval(i));
  }

  public static boolean hasDval(double[] array) {
    return Arrays.stream(array).anyMatch(d -> Dval.isDval(d));
  }

  public static boolean allValuesAreDval(double[] array) {
    return !Arrays.stream(array).anyMatch(d -> !Dval.isDval(d));
  }

  public static boolean hasAnyNonDval(double[] array) {
    return Arrays.stream(array).anyMatch(d -> !Dval.isDval(d));
  }
}
