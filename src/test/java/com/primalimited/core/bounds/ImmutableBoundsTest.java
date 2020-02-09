package com.primalimited.core.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ImmutableBoundsTest {
  @Test
  public void create() {
    ImmutableBounds bounds = ImmutableBounds.of(Bounds.FRACTION);
    assertEquals(0, bounds.getMin(), 1e-10);
    assertEquals(1, bounds.getMax(), 1e-10);
  }

  @Test
  public void createFromBadArgsThrows() {
    assertThrows(IllegalArgumentException.class,
        () -> ImmutableBounds.of(Double.NaN, Double.POSITIVE_INFINITY));
  }

  @Test
  public void invalidCloneThrows() {
    assertThrows(IllegalArgumentException.class,
        () -> ImmutableBounds.of(Bounds.empty()));
    assertThrows(IllegalArgumentException.class,
        () -> ImmutableBounds.of(Bounds.nullBounds()));
  }

  @Test
  public void createFromNullThrows() {
    Bounds bounds = null;
    assertThrows(NullPointerException.class, 
        () -> ImmutableBounds.of(bounds));
  }
  
  @Test
  public void testToString() {
    assertEquals("ImmutableBounds [0..100]", ImmutableBounds.of(Bounds.PERCENT).toString());
  }
}
