package com.primalimited.core.math;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.primalimited.core.dval.Dval;

public class MathUtilTest {
  private static final int N_RANDOM_TESTS = 100;
  private static final int FLOAT_EXPONENT_MIN = -36;
  private static final int FLOAT_EXPONENT_MAX = 36;
  private static final int FLOAT_LARGE_ULPS_EXPONENT = 8;
  private static final int DOUBLE_EXPONENT_MIN = -300;
  private static final int DOUBLE_EXPONENT_MAX = 300;
  private static final int DOUBLE_LARGE_ULPS_EXPONENT = 17;

  @Test
  public void utilityClassConstructor() {
    // for code coverage
    assertThrows(IllegalStateException.class,
        () -> new MathUtil());
    
  }
  @Test
  public void bigUlps() {
    int exp = DOUBLE_LARGE_ULPS_EXPONENT;
    double aa = 1.0;
    while (exp < DOUBLE_EXPONENT_MAX) {
      double a = aa * Math.pow(10, exp);
      double ulp = Math.ulp(a);
      assertTrue(MathUtil.doublesEqual(a, a + ulp));
      assertTrue(MathUtil.doublesEqual(a, a - ulp));
      exp++;
    }
  }

  @Test
  public void bigUlpsFloat() {
    int exp = FLOAT_LARGE_ULPS_EXPONENT;
    float aa = 1.f;
    while (exp < FLOAT_EXPONENT_MAX) {
      float a = aa * (float)Math.pow(10, exp);
      float ulp = Math.ulp(a);
      assertTrue(MathUtil.floatsEqual(a, a + ulp));
      assertTrue(MathUtil.floatsEqual(a, a - ulp));
      exp++;
    }
  }

  @RepeatedTest(N_RANDOM_TESTS)
  public void randomValuesWithUlps() {
    ulpTest(Math.random());
    ulpTest(-Math.random());
  }

  @RepeatedTest(N_RANDOM_TESTS)
  public void randomFloatValuesWithUlps() {
    ulpTest(Double.valueOf(Math.random()).floatValue());
    ulpTest(Double.valueOf(-Math.random()).floatValue());
  }

  private void ulpTest(double aa) {
    int exp = DOUBLE_EXPONENT_MIN;
    while (exp < DOUBLE_EXPONENT_MAX) {
      double a = aa * Math.pow(10, exp);
      double ulp = Math.ulp(a);
      assertTrue(MathUtil.doublesEqual(a, a + ulp), () -> generateMessageTextWithUlp(a, a + ulp));
      assertTrue(MathUtil.doublesEqual(a, a - ulp), () -> generateMessageTextWithUlp(a, a - ulp));
      exp++;
    }
  }

  private void ulpTest(float aa) {
    int exp = -FLOAT_EXPONENT_MIN;
    while (exp < FLOAT_EXPONENT_MAX) {
      float a = aa * (float)Math.pow(10, exp);
      float ulp = Math.ulp(a);
      assertTrue(MathUtil.floatsEqual(a, a + ulp), () -> generateMessageTextWithUlp(a, a + ulp));
      assertTrue(MathUtil.floatsEqual(a, a - ulp), () -> generateMessageTextWithUlp(a, a - ulp));
      exp++;
    }
  }

  @Test
  public void pi() {
    double a = Math.PI;
    double b = a;
    assertTrue(MathUtil.doublesEqual(a, b), () -> generateMessageTextWithUlp(a, b));
  }

  @Test
  public void euler() {
    double a = Math.E;
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
  public void infinityComparisons() {
    assertTrue(MathUtil.doublesEqual(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertTrue(MathUtil.doublesEqual(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    assertFalse(MathUtil.doublesEqual(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
    assertFalse(MathUtil.doublesEqual(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertFalse(MathUtil.doublesEqual(0, Double.POSITIVE_INFINITY));
    assertFalse(MathUtil.doublesEqual(Double.POSITIVE_INFINITY, 0));
    assertFalse(MathUtil.doublesEqual(0, Double.NEGATIVE_INFINITY));
    assertFalse(MathUtil.doublesEqual(Double.NEGATIVE_INFINITY, 0));

    assertTrue(MathUtil.floatsEqual(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
    assertTrue(MathUtil.floatsEqual(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
    assertFalse(MathUtil.floatsEqual(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));
    assertFalse(MathUtil.floatsEqual(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
    assertFalse(MathUtil.floatsEqual(0, Float.NEGATIVE_INFINITY));
    assertFalse(MathUtil.floatsEqual(0, Float.POSITIVE_INFINITY));
    assertFalse(MathUtil.floatsEqual(Float.POSITIVE_INFINITY, 0));
    assertFalse(MathUtil.floatsEqual(Float.NEGATIVE_INFINITY, 0));
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
    
    a = -0.0;
    b = +0.0;
    assertTrue(MathUtil.doublesEqual(a, b));
    
    a = +0.0;
    b = -0.0;
    assertTrue(MathUtil.doublesEqual(a, b));
  }
  
  @Test
  public void zeroComparisonFloat() {
    float a = 0.f;
    float b = 0.f;
    assertTrue(MathUtil.floatsEqual(a, b));

    a = -0.f;
    b = +0.f;
    assertTrue(MathUtil.floatsEqual(a, b));

    a = +0.f;
    b = -0.f;
    assertTrue(MathUtil.floatsEqual(a, b));
  }
  
  @Test
  public void failures() {
    assertFalse(MathUtil.doublesEqual(5, 10));
    assertFalse(MathUtil.doublesEqual(10, 5));
    
    assertFalse(MathUtil.floatsEqual(5f, 10f));
    assertFalse(MathUtil.floatsEqual(10f, 5f));
  }
}
