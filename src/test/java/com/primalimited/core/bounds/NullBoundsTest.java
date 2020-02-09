package com.primalimited.core.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.primalimited.core.dval.Dval;

public class NullBoundsTest {
  @Test
  public void testNullBounds() {
    Bounds bounds = NullBounds.create();
    assertTrue(bounds.isNull());
    assertTrue(Dval.isDval(bounds.getMin()));
    assertTrue(Dval.isDval(bounds.getMax()));
    assertFalse(bounds.isValid());
  }
  
  @Test
  public void testToString() {
    assertEquals("NullBounds [Dval..Dval]", NullBounds.create().toString());
  }
}
