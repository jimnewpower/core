package com.primalimited.core.bounds;

import java.util.Objects;

class ImmutableBounds implements Bounds {
  private final double min;
  private final double max;
  private final double range;

  static ImmutableBounds of(double min, double max) {
    return new ImmutableBounds(min, max);
  }

  static ImmutableBounds of(Bounds other) {
    Objects.requireNonNull(other);
    return new ImmutableBounds(other);
  }

  private ImmutableBounds(double min, double max) {
    validateArguments(min, max);
    this.min = min;
    this.max = max;
    this.range = max - min;
  }

  private ImmutableBounds(Bounds bounds) {
    this(bounds.getMin(), bounds.getMax());
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
    return this.range;
  }
}
