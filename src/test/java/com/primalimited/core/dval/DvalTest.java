package com.primalimited.core.dval;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class DvalTest {
  @Test public void testDouble() {
    double dval = Dval.DVAL_DOUBLE;
    assertTrue(Dval.isDval(Double.valueOf(dval)));
    assertTrue(Dval.isDval(dval));

    double nonDval = Math.random();
    assertFalse(Dval.isDval(Double.valueOf(nonDval)));
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
  
  @Test (expected=NullPointerException.class)
  public void testHasAnyNonDvalWithNull() {
    assertFalse(Dval.hasAnyNonDval((double[])null));
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
}
