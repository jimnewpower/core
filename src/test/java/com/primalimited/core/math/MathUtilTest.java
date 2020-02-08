package com.primalimited.core.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MathUtilTest {
  @Test
  public void constants() {
    assertEquals(1e-12, MathUtil.DEFAULT_EPSILON, 1e-30);
  }
  
  @Test
  public void infinityComparison() {
    assertTrue(MathUtil.doublesEqual(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
    assertTrue(MathUtil.doublesEqual(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
  }

  @Test
  public void NaN_Comparison() {
    assertThrows(IllegalArgumentException.class,
        () -> MathUtil.doublesEqual(Double.NaN, 0.0));
    assertThrows(IllegalArgumentException.class,
        () -> MathUtil.doublesEqual(0.0, Double.NaN));
  }
  
  @Test
  public void tinyComparison() {
    assertTrue(MathUtil.doublesEqual(Math.PI/1e12, Math.PI/1e12));
  }

  @Test
  public void hugeComparison() {
    assertTrue(MathUtil.doublesEqual(Math.PI * 1e20, Math.PI * 1e20));
  }

  @Test
  public void zeroComparison() {
    double a = 0.0;
    double b = 0.0;
    assertTrue(MathUtil.doublesEqual(a, b, 1e-20));
  }

  @Test
  public void doubleComparison() {
    assertTrue(MathUtil.doublesEqual(42.8, 42.8));
  }

  @Test
  public void doubleComparisonWithEpsilon() {
    assertTrue(MathUtil.doublesEqual(42.12345678901234567890, 42.12345678901234567890, 1e-21));
  }

  @Test
  public void doubleComparisonHighEpsilon() {
    assertTrue(MathUtil.doublesEqual(42, 42.5, 1.0));
  }
  
  @Test
  public void tinyComparisonFloat() {
    float epsilon = 1e-10f;
    assertTrue(MathUtil.floatsEqual(0.0001234f, 0.0001234f, epsilon));
  }

  @Test
  public void hugeComparisonFloat() {
    float epsilon = 1e-10f;
    assertTrue(MathUtil.floatsEqual(1001001001f, 1001001001f, epsilon));
  }

  @Test
  public void zeroComparisonFloat() {
    float epsilon = 1e-10f;
    float a = 0.f;
    float b = 0.f;
    assertTrue(MathUtil.floatsEqual(a, b, epsilon));
  }

  @Test
  public void floatComparison() {
    float epsilon = 1e-10f;
    assertTrue(MathUtil.floatsEqual(42.8f, 42.8f, epsilon));
  }

  @Test
  public void floatComparisonHighEpsilon() {
    float epsilon = 1e-20f;
    assertTrue(MathUtil.floatsEqual(42.12345678901234567890f, 42.12345678901234567890f, epsilon));
  }
}
