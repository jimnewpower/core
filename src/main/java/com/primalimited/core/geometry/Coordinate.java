package com.primalimited.core.geometry;

import java.util.Objects;

import com.primalimited.core.dval.Dval;

/**
 * Immutable spatial coordinate that supports a z value
 */
public class Coordinate {
  public final double x;
  public final double y;
  public final double z;
  
  public static Coordinate of(double x, double y) {
    return new Coordinate(x, y);
  }

  public static Coordinate of(double x, double y, double z) {
    return new Coordinate(x, y, z);
  }

  public static Coordinate from(Coordinate c) {
    Objects.requireNonNull(c);
    return new Coordinate(c.x, c.y, c.z);
  }

  Coordinate(double x, double y) {
    this.x = x;
    this.y = y;
    this.z = Dval.DVAL_DOUBLE;
  }
  
  Coordinate(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
