package com.primalimited.core.geometry;

import com.primalimited.core.dval.Dval;

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
