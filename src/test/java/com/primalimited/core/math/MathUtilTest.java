package com.primalimited.core.math;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.primalimited.core.dval.Dval;

public class MathUtilTest {
  private static final int N_RANDOM_TESTS = 10000;
  
  @Test
  public void floats() {
    float a = 7.943681E-7f;
    float b = 7.94368E-7f;
    assertTrue(MathUtil.floatsEqual(a, b));
    
    a = 902.6224f;
    b = 902.6223f;
    assertTrue(MathUtil.floatsEqual(a, b));
  }
  
  @Test
  public void doubles() {
    double a = -1.9848080436844008E242;
    double b = -1.9848080436844006E242;
    assertTrue(MathUtil.doublesEqual(a, b));
    
    a = -198.48080436844006;
    b = -198.48080436844003;
    assertTrue(MathUtil.doublesEqual(a, b));
    
    a = -3.6892123150172095E-9;
    b = -3.68921231501721E-9;
    assertTrue(MathUtil.doublesEqual(a, b));
    
    a = 93.753758280853;
    b = 93.75375828085299;
    assertTrue(MathUtil.doublesEqual(a, b));
  }
  
  @Test
  public void randomPositiveValuesWithUlps() {
    for (int test = 0; test < N_RANDOM_TESTS; test++)
      ulpTest(Math.random());
  }

  @Test
  public void randomNegativeValuesWithUlps() {
    for (int test = 0; test < N_RANDOM_TESTS; test++)
      ulpTest(-Math.random());
  }

  private void ulpTest(double aa) {
    int exp = -300;//Double.MIN_EXPONENT+1;
    while (exp < 300/* Double.MAX_EXPONENT-1 */) {
      double a = aa * Math.pow(10, exp);

      double b = a + (Math.ulp(a) / 2.0);
      assertTrue(MathUtil.doublesEqual(a, b), () -> generateMessageTextWithUlp(a, b));
      
      double c = a - (Math.ulp(a) / 2.0);
      assertTrue(MathUtil.doublesEqual(a, c), () -> generateMessageTextWithUlp(a, c));
      
      exp++;
    }
  }

  @Test
  public void randomPositiveFloatValuesWithUlps() {
    for (int test = 0; test < N_RANDOM_TESTS; test++)
      ulpTest(Double.valueOf(Math.random()).floatValue());
  }

  @Test
  public void randomNegativeFloatValuesWithUlps() {
    for (int test = 0; test < N_RANDOM_TESTS; test++)
      ulpTest(Double.valueOf(-Math.random()).floatValue());
  }

  private void ulpTest(float aa) {
    int exp = -36;//Float.MIN_EXPONENT;
    while (exp < 36/* Float.MAX_EXPONENT */) {
      float a = aa * (float)Math.pow(10, exp);
      
      float b = a + (Math.ulp(a) / 2.f);
      assertTrue(MathUtil.floatsEqual(a, b), () -> generateMessageTextWithUlp(a, b));
      
      float c = a - (Math.ulp(a) / 2.f);
      assertTrue(MathUtil.floatsEqual(a, c), () -> generateMessageTextWithUlp(a, c));
      
      exp++;
    }
  }

  @Test
  public void pi() {
    double a = Math.PI;
    double b = a;
    assertTrue(MathUtil.doublesEqual(a, b), () -> generateMessageTextWithUlp(a, b));
  }

  private static String generateMessageTextWithUlp(float a, float b) {
    return String.format("a=%g b=%g ulp=%g", a, b, Math.ulp(a));
  }

  private static String generateMessageTextWithUlp(double a, double b) {
    return String.format("a=%g b=%g ulp=%g", a, b, Math.ulp(a));
  }

  @Test
  public void euler() {
    double a = Math.E;
    double b = a;
    assertTrue(MathUtil.doublesEqual(a, b), () -> "a=" + a + " b=" + b + " ulp=" + Math.ulp(a));
  }

  @Test
  public void infinityComparisons() {
    assertTrue(MathUtil.doublesEqual(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertTrue(MathUtil.doublesEqual(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));

    assertTrue(MathUtil.floatsEqual(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
    assertTrue(MathUtil.floatsEqual(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
  }

  @Test
  public void NaN_Comparison() {
    // any comparison with NaN should return false
    assertFalse(MathUtil.doublesEqual(Double.NaN, 0.0));
    assertFalse(MathUtil.doublesEqual(0.0, Double.NaN));
    assertFalse(MathUtil.doublesEqual(Double.NaN, Double.NaN));

    assertFalse(MathUtil.floatsEqual(Float.NaN, 0.0f));
    assertFalse(MathUtil.floatsEqual(0.0f, Float.NaN));
    assertFalse(MathUtil.floatsEqual(Float.NaN, Float.NaN));
  }
  
  @Test
  public void dvals() {
    assertTrue(MathUtil.doublesEqual(Dval.DVAL_DOUBLE, Dval.DVAL_DOUBLE));
    assertFalse(MathUtil.doublesEqual(Dval.DVAL_DOUBLE, 0.0));
    assertFalse(MathUtil.doublesEqual(0.0, Dval.DVAL_DOUBLE));

    assertTrue(MathUtil.floatsEqual(Dval.DVAL_FLOAT, Dval.DVAL_FLOAT));
    assertFalse(MathUtil.floatsEqual(Dval.DVAL_FLOAT, 0.f));
    assertFalse(MathUtil.floatsEqual(0.f, Dval.DVAL_FLOAT));
  }
  
  @Test
  public void zeroComparisonDouble() {
    double a = 0.0;
    double b = 0.0;
    assertTrue(MathUtil.doublesEqual(a, b));
  }
  
  @Test
  public void zeroComparisonFloat() {
    float a = 0.f;
    float b = 0.f;
    assertTrue(MathUtil.floatsEqual(a, b));
  }
}
