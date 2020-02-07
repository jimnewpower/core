package com.primalimited.core.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MathUtilTest {
  @Test
  public void doubleComparison() {
    assertTrue(MathUtil.doublesEqual(42.8, 42.8));
  }
  
  @ParameterizedTest(name = "{0} + {1} = {2}")
  @CsvSource({
      "0,    1,   2",
      "1,    2,   3",
      "49,  51, 100",
      "1,  100, 101"
  })
  void add(int first, int second, int expectedResult) {
    assertEquals(expectedResult, first + second,
        () -> first + " + " + second + " should equal " + expectedResult);
  }

}
