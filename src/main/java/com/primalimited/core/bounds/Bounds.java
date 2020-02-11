package com.primalimited.core.bounds;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.primalimited.core.dval.Dval;
import com.primalimited.core.math.MathUtil;

/**
 * Represents two bounding values, e.g. end points of a horizontal or vertical
 * line segment, or the minimum and maximum values of a dataset.
 */
public interface Bounds {
  /**
   * Return the minimum value
   * @return the minimum value
   */
  public double getMin();
  
  /**
   * Return the maximum value
   * @return the maximum value
   */
  public double getMax();
  
  /**
   * Return the range of data (e.g. max-min)
   * @return the range of data
   */
  public double getRange();

  /**
   * Implementations may use this as part of their toString() implementation.
   * 
   * @return descriptive bounds text
   */
  public default String boundsText() {
    NumberFormat nf = NumberFormat.getInstance();
    return "[" 
        + format(nf, getMin()) 
        + ".." 
        + format(nf, getMax()) 
        + "]";
  }

  /**
   * Build formatted text representing this bounds.
   * 
   * @return formatted text representing this bounds.
   */
  public default String format() {
    return format(NumberFormat.getInstance());
  }

  public default String format(NumberFormat nf) {
    return String.format(
      "min=%s max=%s range=%s",
      format(nf, getMin()),
      format(nf, getMax()),
      format(nf, getRange())
    );
  }

  public default String format(String format) {
    return String.format(
      "min=%s max=%s range=%s",
      format(format, getMin()),
      format(format, getMax()),
      format(format, getRange())
    );
  }

  /**
   * Throws IllegalArgumentException if either argument is invalid, or if
   * min &gt; max.
   * 
   * @param min min value 
   * @param max max value
   * @throws IllegalArgumentException if min or max is invalid, or if
   * min &gt; max.
   */
  public default void validateArguments(double min, double max) {
    if (!Dval.isValid.test(min))
      throw new IllegalArgumentException("min is invalid (" + min + ")");
    if (!Dval.isValid.test(max))
      throw new IllegalArgumentException("max is invalid (" + max + ")");
    if (min > max)
      throw new IllegalArgumentException("min (" + min + ") > max (" + max + ")");
  }

  /**
   * Return true if bounds are valid, false otherwise 
   * @return true if bounds are valid, false otherwise
   */
  public default boolean isValid() {
    return valid(getMin(), getMax());
  }

  /**
   * Return true if bounds are valid AND min value is &gt; 0,
   * false otherwise.
   * 
   * @return true if bounds are valid AND min value is &gt; 0,
   * false otherwise.
   */
  public default boolean isValidForLogScale() {
    return valid(getMin(), getMax()) && getMin() > 0.0;
  }

  /**
   * Return true if min == max, false otherwise.
   * @return return true if min == max, false otherwise.
   */
  public default boolean rangeIsZero() {
    return isValid() && MathUtil.doublesEqual(getMin(), getMax());
  }

  /**
   * Return true if this bounds overlaps other bounds, false 
   * otherwise.
   * 
   * @param other other bounds
   * @return true if this bounds overlaps other bounds, false 
   * otherwise.
   */
  public default boolean overlaps(Bounds other) {
    Objects.requireNonNull(other);
    if (!other.isValid())
      return false;
    
    if (!isValid())
      return false;
    
    if (getMax() < other.getMin())
      return false;
    
    if (getMin() > other.getMax())
      return false;
    
    return true;
  }

  /**
   * Determine a discrete bin number for a value, given nBins for range; useful for
   * histograms, color scales, value windows, etc.
   *
   * @param value value
   * @param nBins number of bins for the range
   * @return the bin, if value is within range, -1 otherwise
   */
  public default int getBin(double value, int nBins) {
    if (nBins <= 0 || Dval.isDval(nBins))
      return -1;
    if (value < getMin() || value > getMax())
      return -1;
    int bin = (int)Math.floor(getFractionBetween(value) * (nBins));
    return Math.max(0, Math.min(nBins-1, bin));
  }

  /**
   * Get fraction between bounds for value
   *
   * @param value value
   * @return fraction between bounds endpoints, or Dval if value not within bounds
   */
  public default double getFractionBetween(double value) {
    if (value < getMin() || value > getMax())
      return Dval.DVAL_DOUBLE;

    if (rangeIsZero())
      return 0.0;
    
    return (value - getMin()) / getRange();
  }

  /**
   * Returns true if this bounds represents a null bounds,
   * false otherwise.
   * 
   * @return true if this bounds represents a null bounds,
   * false otherwise.
   */
  public default boolean isNull() {
    return Dval.isDval(getMin()) && Dval.isDval(getMax());
  }

  /**
   * Returns true if this bounds represents an empty bounds,
   * false otherwise.
   * 
   * @return true if this bounds represents an empty bounds,
   * false otherwise.
   */
  public default boolean isEmpty() {
    Bounds empty = empty();
    return MathUtil.doublesEqual(getMin(), empty.getMin()) 
        && MathUtil.doublesEqual(getMax(), empty.getMax());
  }

  /**
   * Expand a mutable bounds to the given value.
   * 
   * @param value value for which to expand
   * @throws IllegalStateException if bounds is immutable
   */
  public default void expandTo(@SuppressWarnings("unused") double value) {
    throw new IllegalStateException("Bounds is immutable");
  }

  /**
   * Returns true if this bounds contains the given value.
   * 
   * @param value the value to evaluate
   * @return true if this bounds contains the given value,
   * false otherwise.
   */
  public default boolean contains(double value) {
    if (value >= getMin() && value <= getMax())
      return true;
    return false;
  }

  /**
   * Bound the given value to this bounds, i.e. if the value
   * is less than the min of the bounds, return bounds min,
   * if the value is greater than the max of the bounds, return
   * the bounds max, otherwise just return the value.
   * 
   * @param value value to bind
   * @return bounds min if value &lt; min, bounds max if 
   * value &gt; max, otherwise return the value argument.
   */
  public default double bound(double value) {
    return Math.min(getMax(), Math.max(getMin(), value));
  }

  /**
   * Bound an integer value to this bounds.
   * 
   * @param value value to bind
   * @return bounds min if value &lt; min, bounds max if 
   * value &gt; max, otherwise return the value argument.
   * @throws IllegalStateException if this bounds has min or
   * max with double precision.
   */
  public default int bound(int value) {
    if (!MathUtil.doublesEqual(getMin(), Math.round(getMin()))
        || !MathUtil.doublesEqual(getMax(), Math.round(getMax())))
      throw new IllegalStateException("attempting to bind integer value to bounds with double precision");
    
    return (int)Math.min(getMax(), Math.max(getMin(), value));
  }

  /**
   * Probability bounds [0..1]
   */
  public static final Bounds PROBABILITY = Bounds.of(0, 1);
  
  /**
   * Fraction bounds [0..1]
   */
  public static final Bounds FRACTION = Bounds.of(0, 1);
  
  /**
   * Percent bounds [0..100]
   */
  public static final Bounds PERCENT = Bounds.of(0, 100);
  
  /**
   * Degrees bounds [0..360]
   */
  public static final Bounds DEGREES = Bounds.of(0, 360);
  
  /**
   * Latitude bounds [0..90]
   */
  public static final Bounds LATITUDE = Bounds.of(0, 90);
  
  /**
   * Longitude bounds [0..180]
   */
  public static final Bounds LONGITUDE = Bounds.of(0, 180);
  
  /**
   * Radians bounds [0..2Pi]
   */
  public static final Bounds RADIANS = Bounds.of(0, 2*Math.PI);
  
  /**
   * 8-bit color value bounds [0..255]
   */
  public static final Bounds RGB_8_BIT = Bounds.of(0, 255);

  /**
   * Factory method to create bounds given two values, min and max.
   * 
   * @param min min value; must be finite and &lt;= max
   * @param max max value; must be finite and &gt;= min
   * @return new instance of a Bounds, initialized with min and max.
   * @throws IllegalArgumentException if min or max is invalid, or if
   * min &gt; max.
   */
  public static Bounds of(double min, double max) {
    return immutable(min, max);
  }

  /**
   * Factory method to create immutable bounds given two values,
   * min and max.
   * 
   * @param min min value; must be finite and &lt;= max
   * @param max max value; must be finite and &gt;= min
   * @return new instance of a Bounds, initialized with min and max.
   * @throws IllegalArgumentException if min or max is invalid, or if
   * min &gt; max.
   */
  public static Bounds immutable(double min, double max) {
    return ImmutableBounds.of(min, max);
  }

  /**
   * Create an empty bounds, used to denote when something has yet
   * to be initialized, but without resorting to null.
   * 
   * @return special instance of bounds that represents 
   * uninitialized bounds.
   */
  public static Bounds empty() {
    return EmptyBounds.create();
  }

  /**
   * Create a null bounds, used to indicate that bounds do not
   * exist, but without resorting to null.
   * 
   * @return special instance of bounds that represents
   * null bounds.
   */
  public static Bounds nullBounds() {
    return new NullBounds();
  }

  /**
   * Given an existing bounds, return a new instance of Bounds
   * that represents the original bounds expanded to the given 
   * array of values, returning a new instance of Bounds.
   * 
   * @param original original bounds
   * @param array array of values for which to expand
   * @return new instance of Bounds that represents the original
   * bounds expanded to the array of values.
   */
  public static Bounds expand(Bounds original, double[] array) {
    Objects.requireNonNull(original);
    
    Bounds arrayBounds = of(array);
    if (!arrayBounds.isValid())
      return original;

    if (!original.isValid())
      return arrayBounds;

    Bounds bounds = Bounds.of(original.getMin(), original.getMax());
    DoubleSummaryStatistics stats = 
        Arrays.stream(array).filter(Dval.isValid).summaryStatistics();
    bounds = expand(bounds, stats.getMin());
    bounds = expand(bounds, stats.getMax());

    return bounds;
  }

  /**
   * Return new instance of bounds that represents the original
   * bounds, expanded to the given value.
   * 
   * @param original original bounds
   * @param value value for which to expand
   * @return new instance of bounds that represents the original
   * bounds, expanded to the given value.
   */
  public static Bounds expand(Bounds original, double value) {
    Objects.requireNonNull(original);
    if (!Dval.isValid.test(value))
      return immutable(original.getMin(), original.getMax());

    double min = Math.min(original.getMin(), value);
    double max = Math.max(original.getMax(), value);

    return immutable(min, max);
  }

  /**
   * Return new instance of bounds that represents the original
   * bounds, expanded by the given percentage.
   * 
   * @param original original bounds
   * @param percent the expansion percentage
   * @return new instance of bounds that represents the original
   * bounds, expanded by the given percentage.
   */
  public static Bounds expandByPercent(Bounds original, double percent) {
    if (!Bounds.PERCENT.contains(Math.abs(percent)))
      return immutable(original.getMin(), original.getMax());

    double fraction = percent / 100.0;
    // add half of the fraction to each end of the bounds
    double halfFraction = fraction / 2.0;
    
    double min = original.getMin() - (halfFraction * original.getRange());
    double max = original.getMax() + (halfFraction * original.getRange());

    return immutable(min, max);
  }

  /**
   * Create new instance of Bounds that represents the minimum and
   * maximum valid values from the given array.
   * 
   * @param array array of values
   * @return new instance of Bounds that represents the minimum and
   * maximum valid values from the given array.
   */
  public static Bounds of(double[] array) {
    if (array == null || array.length == 0)
      return Bounds.nullBounds();

    DoubleSummaryStatistics stats = Arrays
        .stream(array)
        .filter(Dval.isValid)
        .summaryStatistics();

    return createFromStats(stats);
  }

  /**
   * Create new instance of Bounds that represents the minimum and
   * maximum valid values from the given collection of doubles.
   * 
   * @param collection collection of doubles
   * @return new instance of Bounds that represents the minimum and
   * maximum valid values from the given collection of doubles.
   */
  public static Bounds of(Collection<Double> collection) {
    if (collection == null || collection.size() == 0)
      return new NullBounds();

    DoubleSummaryStatistics stats = collection
        .stream()
        .filter(Dval.VALID_DOUBLE_BOXED)
        .mapToDouble(d -> d.doubleValue())
        .summaryStatistics();

    return createFromStats(stats);
  }

  static Bounds createFromStats(DoubleSummaryStatistics stats) {
    Objects.requireNonNull(stats);
    
    double min = stats.getMin();
    double max = stats.getMax();

    if (!Bounds.valid(min, max))
      return Bounds.nullBounds();
    
    return immutable(min, max);
  }

  /**
   * Create new instance of Bounds that represents the minimum and
   * maximum of the given bounds arguments.
   * 
   * @param bounds0 bounds argument
   * @param bounds1 bounds argument
   * @return new instance of Bounds that represents the minimum and
   * maximum of the given bounds arguments.
   * @throws IllegalArgumentException if either bounds argument is invalid
   */
  public static Bounds minMax(Bounds bounds0, Bounds bounds1) {
    Objects.requireNonNull(bounds0);
    Objects.requireNonNull(bounds1);

    if (!bounds0.isValid())
      throw new IllegalArgumentException("bounds0 is invalid:" + bounds0.format());
    
    if (!bounds1.isValid())
      throw new IllegalArgumentException("bounds1 is invalid:" + bounds1.format());
    
    return immutable(Math.min(bounds0.getMin(), bounds1.getMin()), Math.max(bounds0.getMax(), bounds1.getMax()));
  }

  static String format(NumberFormat nf, double value) {
    Objects.requireNonNull(nf);
    String text =
        Double.isNaN(value) ? "NaN"
        : Double.isInfinite(value) ? "Infinity"
        : Dval.isDval(value) ? "Dval" 
        : nf.format(value);
    return text;
  }

  static String format(String format, double value) {
    Objects.requireNonNull(format);
    String text =
        Double.isNaN(value) ? "NaN"
        : Double.isInfinite(value) ? "Infinity"
        : Dval.isDval(value) ? "Dval" 
        : String.format(format, value);
    return text;
  }

  /**
   * Return true if both arguments constitute a valid Bounds: min &lt;= max,
   * and both min and max are finite and non-dval.
   * 
   * @param min min value
   * @param max max value
   * @return true if both arguments constitute a valid Bounds
   */
  public static boolean valid(double min, double max) {
    if (!Dval.isValid.test(min))
      return false;
    if (!Dval.isValid.test(max))
      return false;
    if (min > max)
      return false;
    return true;
  }

  /**
   * Determine the common bounds for a set of bounds
   *
   * @param all set of bounds
   * @return new instance of Bounds that represents the least common bounds for all
   */
  public static Bounds common(Bounds...all) {
    Objects.requireNonNull(all);
    if (all.length == 0)
      throw new IllegalArgumentException();

    if (!allOverlap(all))
      throw new IllegalArgumentException("not all bounds overlap");

    OptionalDouble highestMin =
        Arrays.stream(all).flatMapToDouble(b -> DoubleStream.of(b.getMin())).max();
    OptionalDouble lowestMax =
        Arrays.stream(all).flatMapToDouble(b -> DoubleStream.of(b.getMax())).min();
    return of(highestMin.getAsDouble(), lowestMax.getAsDouble());
  }

  static boolean allOverlap(Bounds...all) {
    Objects.requireNonNull(all);
    if (all.length == 0)
      throw new IllegalArgumentException();

    for (Bounds bounds : all) {
      if (!Arrays.stream(all).allMatch(b -> b.overlaps(bounds)))
        return false;
    }
    return true;
  }

  /**
   * Merge any overlapping bounds in the given set of bounds. For example,
   * the members of the bounds set may represent meeting begin and end
   * times.  This function will merge any overlapping meetings in order to
   * show when the conference room is booked. 
   * 
   * @param set set of bounds
   * @return merged list of overlapping bounds.
   */
  public static List<Bounds> mergeOverlapping(Set<Bounds> set) {
    Objects.requireNonNull(set);

    /* create copy of list as mutable bounds */
    List<MutableBounds> sorted = set.stream()
        .filter(b -> b.isValid())
        .map(b -> MutableBounds.of(b.getMin(), b.getMax()))
        .collect(Collectors.toList());

    /* sort copy by bounds min */
    Collections.sort(sorted, new Comparator<Bounds>() {
      @Override
      public int compare(Bounds b1, Bounds b2) {
        return Double.compare(b1.getMin(), b2.getMin());
      }
    });

    /* merge overlapping bounds */
    List<MutableBounds> merged = new ArrayList<>();
    merged.add(sorted.get(0));
    for (MutableBounds current : sorted) {
      MutableBounds last = merged.get(merged.size() - 1);
      if (current.getMin() <= last.getMax()) {
        last.setBounds(last.getMin(), Math.max(last.getMax(), current.getMax()));
      } else {
        merged.add(current);
      }
    }

    /* return merged list of immutable bounds */
    return merged.stream()
        .map(b -> immutable(b.getMin(), b.getMax()))
        .collect(Collectors.toList());
  }
}
