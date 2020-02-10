package com.primalimited.core.dval;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

public class DvalTest {
  @Test
  public void predicates() {
    assertTrue(Dval.isValid.test(Math.random()));
    assertFalse(Dval.isValid.test(Dval.DVAL_DOUBLE));
    assertFalse(Dval.isValid.test(Double.NaN));
    assertFalse(Dval.isValid.test(Double.POSITIVE_INFINITY));
    assertFalse(Dval.isValid.test(Double.NEGATIVE_INFINITY));

    assertTrue(Dval.VALID_DOUBLE_BOXED.test(Double.valueOf(Math.random())));
    assertFalse(Dval.VALID_DOUBLE_BOXED.test(Double.valueOf(Dval.DVAL_DOUBLE)));
    assertFalse(Dval.VALID_DOUBLE_BOXED.test(Double.valueOf(Double.NaN)));
    assertFalse(Dval.VALID_DOUBLE_BOXED.test(Double.valueOf(Double.POSITIVE_INFINITY)));
    assertFalse(Dval.VALID_DOUBLE_BOXED.test(Double.valueOf(Double.NEGATIVE_INFINITY)));
  }
  
  @Test public void testDouble() {
    double dval = Dval.DVAL_DOUBLE;
    assertTrue(Dval.isDval(Double.valueOf(dval)));
    assertTrue(Dval.isDval(dval));

    double nonDval = Math.random();
    assertFalse(Dval.isDval(Double.valueOf(nonDval)));
    assertFalse(Dval.isDval(nonDval));
  }

  @Test public void testFloat() {
    float dval = Dval.DVAL_FLOAT;
    assertTrue(Dval.isDval(Float.valueOf(dval)));
    assertTrue(Dval.isDval(dval));

    float nonDval = 3498.57f;
    assertFalse(Dval.isDval(Float.valueOf(nonDval)));
    assertFalse(Dval.isDval(nonDval));
  }

  @Test public void testInteger() {
    int dval = Dval.DVAL_INT;
    assertTrue(Dval.isDval(Integer.valueOf(dval)));
    assertTrue(Dval.isDval(dval));

    int nonDval = (int) Math.round(Math.random() * 1000);
    assertFalse(Dval.isDval(Integer.valueOf(nonDval)));
    assertFalse(Dval.isDval(nonDval));
  }

  @Test public void testLong() {
    long dval = Dval.DVAL_LONG;
    assertTrue(Dval.isDval(Long.valueOf(dval)));
    assertTrue(Dval.isDval(dval));

    long nonDval = (long) Math.round(Math.random() * 1000);
    assertFalse(Dval.isDval(Long.valueOf(nonDval)));
    assertFalse(Dval.isDval(nonDval));
  }

  @Test public void testByte() {
    byte dval = Dval.DVAL_BYTE;
    assertTrue(Dval.isDval(Byte.valueOf(dval)));
    assertTrue(Dval.isDval(dval));

    byte nonDval = 16;
    assertFalse(Dval.isDval(Byte.valueOf(nonDval)));
    assertFalse(Dval.isDval(nonDval));
  }

  @Test public void allValuesAreDval() {
    int size = 100;
    double[] valid = new double[size];
    for (int index = 0; index < size; index++)
      valid[index] = Math.random();
    assertFalse(Dval.hasDval(valid));
    assertFalse(Dval.allValuesAreDval(valid));

    double[] someDval = new double[size];
    for (int index = 0; index < size; index++)
      someDval[index] = Math.random();
    someDval[10] = Dval.DVAL_DOUBLE;
    someDval[35] = Dval.DVAL_DOUBLE;
    assertTrue(Dval.hasDval(someDval));
    assertFalse(Dval.allValuesAreDval(someDval));

    double[] allDval = new double[size];
    for (int index = 0; index < size; index++)
      allDval[index] = Dval.DVAL_DOUBLE + Math.random();
    assertTrue(Dval.hasDval(allDval));
    assertTrue(Dval.allValuesAreDval(allDval));
  }
  
  @Test
  public void intArrayHasDval() {
    int[] array = new int[] { 0, 1, 2, 3, 4, 5, 6 };
    assertFalse(Dval.hasDval(array));
    
    array = new int[] { 0, 1, 2, Dval.DVAL_INT, 4, 5, 6 };
    assertTrue(Dval.hasDval(array));
  }

  @Test
  public void intArrayHasAnyNonDval() {
    int[] array = new int[] { 0, 1, 2, 3, 4, 5, 6 };
    assertTrue(Dval.hasAnyNonDval(array));
    
    array = new int[] { Dval.DVAL_INT, 1, 2, Dval.DVAL_INT, 4, 5, Dval.DVAL_INT };
    assertTrue(Dval.hasAnyNonDval(array));
    
    array = new int[] { Dval.DVAL_INT, Dval.DVAL_INT, Dval.DVAL_INT, Dval.DVAL_INT };
    assertFalse(Dval.hasAnyNonDval(array));
  }

  @Test
  public void intArrayAllDval() {
    int[] array = new int[] { 0, 1, 2, 3, 4, 5, 6 };
    assertFalse(Dval.allValuesAreDval(array));
    
    array = new int[] { Dval.DVAL_INT, Dval.DVAL_INT, Dval.DVAL_INT, Dval.DVAL_INT };
    assertTrue(Dval.allValuesAreDval(array));
  }

  @Test
  public void testHasAnyNonDvalWithNull() {
    assertThrows(NullPointerException.class, () -> Dval.hasAnyNonDval((double[])null), () -> "");
  }
  
  @Test
  public void testHasAnyNonDvalWithEmpty() {
    assertFalse(Dval.hasAnyNonDval(new double[0]));
  }
  
  @Test
  public void testHasAnyNonDvalWithAllDval() {
    double[] allDvals = new double[10];
    Arrays.fill(allDvals, Dval.DVAL_DOUBLE);
    assertFalse(Dval.hasAnyNonDval(allDvals));
  }
  
  @Test
  public void testHasAnyDvalWithNoDvals() {
    double[] noDvals = new double[] {-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5};
    assertTrue(Dval.hasAnyNonDval(noDvals));
  }
  
  @Test
  public void testHasAnyDvalWithOneNonDval() {
    double[] allButOneDvals = new double[10];
    Arrays.fill(allButOneDvals, Dval.DVAL_DOUBLE);
    allButOneDvals[5] = 1234;
    assertTrue(Dval.hasAnyNonDval(allButOneDvals));
  }
  
  @Test
  public void nullNumberThrows() {
    Number number = null;
    assertThrows(NullPointerException.class, 
        () -> Dval.isDval(number));
  }
  
  @Test
  public void doubleNumber() {
    Number number = Double.valueOf(32.4);
    assertFalse(Dval.isDval(number));
    number = Double.valueOf(Dval.DVAL_DOUBLE);
    assertTrue(Dval.isDval(number));
  }
  
  @Test
  public void floatNumber() {
    Number number = Float.valueOf(32.4f);
    assertFalse(Dval.isDval(number));
    number = Float.valueOf(Dval.DVAL_FLOAT);
    assertTrue(Dval.isDval(number));
  }
  
  @Test
  public void intNumber() {
    Number number = Integer.valueOf(32);
    assertFalse(Dval.isDval(number));
    number = Integer.valueOf(Dval.DVAL_INT);
    assertTrue(Dval.isDval(number));
  }
  
  @Test
  public void longNumber() {
    Number number = Long.valueOf(1024L);
    assertFalse(Dval.isDval(number));
    number = Long.valueOf(Dval.DVAL_LONG);
    assertTrue(Dval.isDval(number));
  }
  
  @Test
  public void byteNumber() {
    byte b = 32;
    Number number = Byte.valueOf(b);
    assertFalse(Dval.isDval(number));
    number = Byte.valueOf(Dval.DVAL_BYTE);
    assertTrue(Dval.isDval(number));
  }
  
  @Test
  public void unsupportedNumberType() {
    Number number = new AtomicInteger(42);
    assertThrows(IllegalArgumentException.class,
        () -> Dval.isDval(number));
  }
}
