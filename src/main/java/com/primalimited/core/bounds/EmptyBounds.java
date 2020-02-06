package com.primalimited.core.bounds;

class EmptyBounds implements Bounds {
  static final double DEFAULT_VALUE = 1.0e+40;

  static EmptyBounds create() {
    return new EmptyBounds();
  }

  private EmptyBounds() {
  }

  @Override
  public String toString() {
    String text = "[1e40..-1e40]";
    return getClass().getSimpleName() + " " + text;
  }

  @Override
  public double getMin() {
    return DEFAULT_VALUE;
  }

  @Override
  public double getMax() {
    return -DEFAULT_VALUE;
  }

  @Override
  public double getRange() {
    return Double.MAX_VALUE;
  }

  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public boolean isValidForLogScale() {
    return false;
  }
}
