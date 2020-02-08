package com.primalimited.core.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.primalimited.core.dval.Dval;

public class CoordinateTest {
  @Test
  public void xy() {
    double x = 10.0;
    double y = 12.4;
    Coordinate c = Coordinate.of(x, y);
    double delta = 1e-10;
    assertEquals(x, c.x, delta);
    assertEquals(y, c.y, delta);
    assertTrue(Dval.isDval(c.z));
  }
  
  @Test
  public void xyz() {
    double x = 10.0;
    double y = 12.4;
    double z = 32.8;
    Coordinate c = Coordinate.of(x, y, z);
    double delta = 1e-10;
    assertEquals(x, c.x, delta);
    assertEquals(y, c.y, delta);
    assertEquals(z, c.z, delta);
  }
  
  @Test
  public void fromValid() {
    double x = 10.0;
    double y = 12.4;
    double z = 32.8;
    Coordinate orig = Coordinate.of(x, y, z);
    Coordinate c = Coordinate.from(orig);
    double delta = 1e-10;
    assertEquals(orig.x, c.x, delta);
    assertEquals(orig.y, c.y, delta);
    assertEquals(orig.z, c.z, delta);
  }
  
  @Test
  public void fromInvalid() {
    Coordinate nullCoordinate = null;
    assertThrows(NullPointerException.class, () -> Coordinate.from(nullCoordinate));
  }
}
