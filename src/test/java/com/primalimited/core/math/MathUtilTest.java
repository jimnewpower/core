package com.primalimited.core.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MathUtilTest {
  @Test
  public void tinyComparison() {
    assertTrue(MathUtil.doublesEqual(Math.PI/1e12, Math.PI/1e12));
  }

  @Test
  public void hugeComparison() {
    assertTrue(MathUtil.doublesEqual(Math.PI * 1e20, Math.PI * 1e20));
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
  
  @ParameterizedTest(name = "{0} + {1} = {2}")
  @CsvSource({
      "0,    1,   1",
      "1,    2,   3",
      "49,  51, 100",
      "1,  100, 101"
  })
  void add(int first, int second, int expectedResult) {
    assertEquals(expectedResult, first + second,
        () -> first + " + " + second + " should equal " + expectedResult);
  }

}
