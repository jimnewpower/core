package com.primalimited.core.bounds;

/**
 * Mutable bounds
 */
final class MutableBounds implements Bounds, BoundsMutator {
  private transient double min;
  private transient double max;

  static MutableBounds of(double min, double max) {
    return new MutableBounds(min, max);
  }
  
  private MutableBounds(double min, double max) {
    this.min = min;
    this.max = max;
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + " " + boundsText();
  }

  @Override
  public double getMin() {
    return this.min;
  }

  @Override
  public double getMax() {
    return this.max;
  }

  @Override
  public double getRange() {
    return this.max - this.min;
  }

  @Override
  public void setBounds(double min, double max) {
    if (!Bounds.valid(min, max))
      return;
    this.min = min;
    this.max = max;
  }
}