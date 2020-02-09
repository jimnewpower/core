package com.primalimited.core.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class EmptyBoundsTest {
  @Test
  public void testEmptyBounds() {
    Bounds bounds = EmptyBounds.create();
    assertEquals(EmptyBounds.DEFAULT_VALUE, bounds.getMin(), 1e-30);
    assertEquals(-EmptyBounds.DEFAULT_VALUE, bounds.getMax(), 1e-30);
    assertFalse(bounds.isValid());
  }
  
  @Test
  public void logScaleValidity() {
    assertFalse(EmptyBounds.create().isValidForLogScale());
  }
  
  @Test
  public void testToString() {
    assertEquals("EmptyBounds [1e40..-1e40]", EmptyBounds.create().toString());
  }
}
