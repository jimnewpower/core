package com.primalimited.core.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MutableBoundsTest {
  @Test
  public void create() {
    MutableBounds bounds = MutableBounds.of(0, 1);
    assertEquals(0, bounds.getMin(), 1e-10);
    assertEquals(1, bounds.getMax(), 1e-10);
    assertTrue(bounds.isValid());
  }
  
  @Test
  public void mutations() {
    MutableBounds bounds = MutableBounds.of(0, 1);
    bounds.setBounds(100, 200);
    assertEquals(100, bounds.getMin(), 1e-10);
    assertEquals(200, bounds.getMax(), 1e-10);
  }
  
  @Test
  public void setBoundsInvalid() {
    MutableBounds bounds = MutableBounds.of(0, 1);
    bounds.setBounds(Double.NaN, Double.NEGATIVE_INFINITY);
    assertEquals(0, bounds.getMin(), 1e-10);
    assertEquals(1, bounds.getMax(), 1e-10);
    assertTrue(bounds.isValid());
  }
  
  @Test
  public void getRange() {
    assertEquals(32, MutableBounds.of(0, 32).getRange(), 1e-10);
  }
  
  @Test
  public void testToString() {
    assertEquals("MutableBounds [52..78]", MutableBounds.of(52, 78).toString());
  }
}
