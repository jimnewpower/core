package com.primalimited.core.bounds;

import java.util.Collection;
import java.util.Objects;

import com.primalimited.core.dval.Dval;
import com.primalimited.core.geometry.Coordinate;
import com.primalimited.core.math.MathUtil;

/**
 * Aerial, or spatial bounds (e.g. a rectangle).
 */
public class Bounds2D {
  /* instance variables */
  protected Bounds xBounds = Bounds.empty();
  protected Bounds yBounds = Bounds.empty();

  public static Bounds2D empty() {
    return new Bounds2D();
  }

  public static Bounds2D create(Bounds x, Bounds y) {
    Objects.requireNonNull(x, "x bounds cannot be null");
    Objects.requireNonNull(y, "y bounds cannot be null");
    if (!x.isValid()) {
      throw new IllegalArgumentException("x bounds invalid: " + x.boundsText());
    }
    if (!y.isValid()) {
      throw new IllegalArgumentException("y bounds invalid: " + y.boundsText());
    }
    Bounds2D bounds = empty();
    bounds.setXValues(x.getMin(), x.getMax());
    bounds.setYValues(y.getMin(), y.getMax());
    return bounds;
  }

  public static Bounds2D create(double minX, double maxX, double minY, double maxY) {
    Bounds2D bounds = empty();
    bounds.setValues(minX, maxX, minY, maxY);
    return bounds;
  }

  public static Bounds2D from(double[] xArray, double[] yArray) {
    Objects.requireNonNull(xArray, "x array cannot be null");
    Objects.requireNonNull(yArray, "y array cannot be null");
    if (xArray.length == 0)
      throw new IllegalArgumentException("xArray is empty");
    if (xArray.length != yArray.length)
      throw new IllegalArgumentException("xArray.length != yArray.length");

    Bounds2D bounds = empty();
    bounds.expandTo(xArray, yArray);
    return bounds;
  }

  public static Bounds2D from(Collection<Coordinate> coordinates) {
    if (coordinates == null || coordinates.isEmpty())
      return null;
    final Bounds2D bounds = empty();
    coordinates.stream().forEach(c -> bounds.expandTo(c.x, c.y));
    return bounds;
  }

  public static Bounds2D from(Coordinate[] coordinates) {
    Objects.requireNonNull(coordinates, "coordinates cannot be null");

    double minX = EmptyBounds.DEFAULT_VALUE;
    double maxX = -EmptyBounds.DEFAULT_VALUE;
    double minY = EmptyBounds.DEFAULT_VALUE;
    double maxY = -EmptyBounds.DEFAULT_VALUE;
    for (Coordinate coordinate : coordinates) {
      double x = coordinate.x;
      if (Dval.isDval(x) || Double.isInfinite(x) || Double.isNaN(x))
        continue;
      if (x < minX)
        minX = x;
      if (x > maxX)
        maxX = x;

      double y = coordinate.y;
      if (Dval.isDval(y) || Double.isInfinite(y) || Double.isNaN(y))
        continue;
      if (y < minY)
        minY = y;
      if (y > maxY)
        maxY = y;
    }

    return create(minX, maxX, minY, maxY);
  }

  public static Bounds2D from(Bounds2D other) {
    return new Bounds2D(other);
  }

  protected Bounds2D() {
  }

  protected Bounds2D(Bounds2D from) {
    if (from.xBounds.isValid())
      this.xBounds = Bounds.immutable(from.xBounds.getMin(), from.xBounds.getMax());
    if (from.yBounds.isValid())
      this.yBounds = Bounds.immutable(from.yBounds.getMin(), from.yBounds.getMax());
  }

  @Override public String toString() {
    String x = this.xBounds.boundsText();
    String y = this.yBounds.boundsText();
    return getClass().getSimpleName() + " x=" + x + ", y=" + y;
  }

  /**
   * Returns true if the two rectangles have no intersections
   *
   * @param bounds bounds to test
   * @return true if the two rectangles have no intersections, false otherwise
   */
  public boolean disjoint(Bounds2D bounds) {
    if (!isValid())
      return false;
    if (!bounds.isValid())
      return false;

    if (contains(bounds))
      return false;
    if (bounds.contains(this))
      return false;

    boolean outsideX = false;
    if (
      (bounds.getMinX() < getMinX() && bounds.getMaxX() < getMinX()) ||
      (bounds.getMinX() > getMaxX() && bounds.getMaxX() > getMaxX())
    ) {
      outsideX = true;
    }

    boolean outsideY = false;
    if (
      (bounds.getMinY() < getMinY() && bounds.getMaxY() < getMinY()) ||
      (bounds.getMinY() > getMaxY() && bounds.getMaxY() > getMaxY())
    ) {
      outsideY = true;
    }

    return outsideX || outsideY;
  }

  /**
   * Returns true if and only if bounds arg is entirely inside this bounds
   * @param bounds bounds
   * @return true if and only if bounds arg is entirely inside this bounds
   */
  public boolean contains(Bounds2D bounds) {
    if (!isValid())
      return false;
    if (!bounds.isValid())
      return false;

    if (bounds.getMinX() < getMinX())
      return false;
    if (bounds.getMaxX() > getMaxX())
      return false;
    if (bounds.getMinY() < getMinY())
      return false;
    if (bounds.getMaxY() > getMaxY())
      return false;

    return true;
  }

  public boolean contains(double x, double y) {
    if (!isValid())
      return(false);

    if (x < getMinX())
      return(false);
    if (x > getMaxX())
      return(false);
    if (y < getMinY())
      return(false);
    if (y > getMaxY())
      return(false);

    return(true);
  }

  public void expandBounds2D(Bounds2D bounds2D) {
    if (bounds2D == null)
      return;
    bounds2D.expandTo(this);
  }

  /**
   * Updates the bounding x,y values from the {@link Bounds2D}.
   *
   * More specifically, if any x,y values in the {@link Bounds2D} lie
   * outside of the existing values for the Bounds2D, the bounds
   * values will be used.
   *
   * @param other {@link Bounds2D} used to update bounds.
   */
  public void expandTo(Bounds2D other) {
    if (other != null && other.isValid()) {
      double minX = getMinX();
      if (minX > other.getMinX())
        minX = other.getMinX();
      double maxX = getMaxX();
      if (maxX < other.getMaxX())
        maxX = other.getMaxX();

      double minY = getMinY();
      if (minY > other.getMinY())
        minY = other.getMinY();
      double maxY = getMaxY();
      if (maxY < other.getMaxY())
        maxY = other.getMaxY();

      setValues(minX, maxX, minY, maxY);
    }
  }

  /**
   * Update the bounding values from the x, y location.
   * @param x x to update bounds.
   * @param y y to update bounds.
   */
  public void expandTo(double x, double y) {
    if (Dval.isDval(x) || Dval.isDval(y))
      return;

    double minX = getMinX();
    if (minX > x)
      minX = x;
    double maxX = getMaxX();
    if (maxX < x)
      maxX = x;

    double minY = getMinY();
    if (minY > y)
      minY = y;
    double maxY = getMaxY();
    if (maxY < y)
      maxY = y;

    setValues(minX, maxX, minY, maxY);
  }

  /**
   * Updates the bounds based on coordinate arrays.
   *
   * @param xArray array of X coordinates
   * @param yArray array of Y coordinates
   */
  public void expandTo(double[] xArray, double[] yArray) {
    Objects.requireNonNull(xArray, "xArray");
    Objects.requireNonNull(yArray, "yArray");

    if (xArray.length != yArray.length)
      throw new IllegalArgumentException("xArray.length != yArray.length");

    Bounds x = Bounds.of(xArray);
    Bounds y = Bounds.of(yArray);
    expandTo(
      x.isValid() ? x.getMin() : getMinX(),
      x.isValid() ? x.getMax() : getMaxX(),
      y.isValid() ? y.getMin() : getMinY(),
      y.isValid() ? y.getMax() : getMaxY()
    );
  }

  /**
   * Updates the bounds from the specified coordinate range.
   *
   * @param srcMinX the minimum X coordinate of the range
   * @param srcMaxX the maximum X coordinate of the range
   * @param srcMinY the minimum Y coordinate of the range
   * @param srcMaxY the maximum Y coordinate of the range
   */
  public void expandTo(
    double srcMinX,
    double srcMaxX,
    double srcMinY,
    double srcMaxY
  ) {
    double minX = getMinX();
    if (!Dval.isDval(srcMinX)) {
      if (minX > srcMinX)
        minX = srcMinX;
    }
    double maxX = getMaxX();
    if (!Dval.isDval(srcMaxX)) {
      if (maxX < srcMaxX)
        maxX = srcMaxX;
    }

    double minY = getMinY();
    if (!Dval.isDval(srcMinY)) {
      if (minY > srcMinY)
        minY = srcMinY;
    }
    double maxY = getMaxY();
    if (!Dval.isDval(srcMaxY)) {
      if (maxY < srcMaxY)
        maxY = srcMaxY;
    }

    setValues(minX, maxX, minY, maxY);
  }

  public void expandTo(Coordinate c) {
    Objects.requireNonNull(c, "coordinate");
    expandTo(c.x, c.y);
  }

  public void expandTo(Coordinate[] coordinates) {
    Bounds2D other = Bounds2D.from(coordinates);
    if (!other.isValid())
      return;
    expandTo(other);
  }

  /**
   * Clears out all values.
   */
  public void reset() {
    this.xBounds = Bounds.empty();
    this.yBounds = Bounds.empty();
  }

  /**
   * Return true if point is inside the bounds, false otherwise.
   *
   * @param x the X coordinate of the point to evaluate
   * @param y the Y coordinate of the point to evaluate
   * @return true if point is inside the bounds, false otherwise
   */
  public boolean isPointInside(double x, double y) {
    return(getMinX() <= x && x <= getMaxX() && getMinY() <= y && y <= getMaxY());
  }

  /**
   * Sets the bounding coordinates explicitly, regardless of whether
   * these parameters lie inside or outside the existing coordinate
   * ranges.
   *
   * @param minX new minimum X coordinate
   * @param maxX new minimum X coordinate
   * @param minY new minimum Y coordinate
   * @param maxY new minimum Y coordinate
   */
  public void setValues(
    double minX,
    double maxX,
    double minY,
    double maxY
  ) {
    setXValues(minX, maxX);
    setYValues(minY, maxY);
  }

  public void setXValues(
    double minX,
    double maxX
  ) {
    if (Bounds.valid(minX, maxX))
      this.xBounds = Bounds.immutable(minX, maxX);
  }

  public void setYValues(
    double minY,
    double maxY
  ) {
    if (Bounds.valid(minY, maxY))
      this.yBounds = Bounds.immutable(minY, maxY);
  }

  /**
   * Expands (or contracts) the bounds by a percentage.  Bounds will expand if
   * pct argument is greater than 0, and contract if pct argument is less than
   * 0.
   *
   * Each dimension is expanded by the specified percentage of
   * itself. For example, passing in 10.0 for the pct parameter will
   * change extents from [0.0, 0.0, 0.0, 100.0, 200.0, 300.0] to
   * [-5.0, -10.0, -15.0, 105.0, 210.0, 315.0].
   * If the current bounds are not valid, nothing is changed and false
   * is returned.
   *
   * @param pct the percentage to expand.
   * @return true if expansion worked, false otherwise.
   */
  public boolean expandByPercentage(double pct) {
    if (!isValid())
      return(false);
    if (Dval.isDval(pct))
      return false;

    double factor = pct / 100.0;
    // divide factor in half because it will be used on both ends
    factor *= 0.5;

    if (this.xBounds.isValid()) {
      double minX = getMinX();
      double maxX = getMaxX();
      double deltaX = factor * this.xBounds.getRange();
      minX -= deltaX;
      maxX += deltaX;
      setXValues(minX, maxX);
    }

    if (this.yBounds.isValid()) {
      double minY = getMinY();
      double maxY = getMaxY();
      double deltaY = factor * this.yBounds.getRange();
      minY -= deltaY;
      maxY += deltaY;
      setYValues(minY, maxY);
    }

    return(true);
  }

  public boolean expandWidthByPercentage(double pct) {
    if (!isValid())
      return(false);
    if (Dval.isDval(pct))
      return false;

    double factor = pct / 100.0;
    // divide factor in half because it will be used on both ends
    factor *= 0.5;

    if (this.xBounds.isValid()) {
      double minX = getMinX();
      double maxX = getMaxX();
      double deltaX = factor * this.xBounds.getRange();
      minX -= deltaX;
      maxX += deltaX;
      setXValues(minX, maxX);
    }

    return(true);
  }

  public boolean expandHeightByPercentage(double pct) {
    if (!isValid())
      return(false);
    if (Dval.isDval(pct))
      return false;

    double factor = pct / 100.0;
    // divide factor in half because it will be used on both ends
    factor *= 0.5;

    if (this.yBounds.isValid()) {
      double minY = getMinY();
      double maxY = getMaxY();
      double deltaY = factor * this.yBounds.getRange();
      minY -= deltaY;
      maxY += deltaY;
      setYValues(minY, maxY);
    }

    return(true);
  }

  public void dump(int indent) {
    StringBuffer stringBuffer = new StringBuffer(indent);
    for (int index = 0; index < indent; index++)
      stringBuffer.append(' ');
    String whiteSpace = stringBuffer.toString();
    whiteSpace += "  "; //$NON-NLS-1$
    System.out.println(whiteSpace+"minX: "+getMinX());
    System.out.println(whiteSpace+"maxX: "+getMaxX());
    System.out.println(whiteSpace+"minY: "+getMinY());
    System.out.println(whiteSpace+"maxY: "+getMaxY());
    System.out.println(whiteSpace+"width: "+getWidth());
    System.out.println(whiteSpace+"height: "+getHeight());
    System.out.println(whiteSpace+"midpoint: "+getMidpoint());
  }

  public Bounds xBounds() {
    if (!this.xBounds.isValid())
      return this.xBounds;
    return Bounds.immutable(this.xBounds.getMin(), this.xBounds.getMax());
  }

  public Bounds yBounds() {
    if (!this.yBounds.isValid())
      return this.yBounds;
    return Bounds.immutable(this.yBounds.getMin(), this.yBounds.getMax());
  }

  /**
   * @return minX (value of minimum x)
   */
  public double getMinX() {
    return this.xBounds.getMin();
  }

  /**
   * @return maxX (value of maximum x)
   */
  public double getMaxX() {
    return this.xBounds.getMax();
  }

  /**
   * @return minY (value of minimum y)
   */
  public double getMinY() {
    return this.yBounds.getMin();
  }

  /**
   * @return maxY (value of maximum y)
   */
  public double getMaxY() {
    return this.yBounds.getMax();
  }

  /**
   * @return width (maxX - minX)
   */
  public double getWidth() {
    if (!this.xBounds.isValid())
      return Dval.DVAL_DOUBLE;
    return this.xBounds.getRange();
  }

  /**
   * @return height (maxY - minY)
   */
  public double getHeight() {
    if (!this.yBounds.isValid())
      return Dval.DVAL_DOUBLE;
    return this.yBounds.getRange();
  }

  public double ratioXY() {
    if (!isValid())
      return Dval.DVAL_DOUBLE;
    return getWidth() / getHeight();
  }

  public double ratioYX() {
    if (!isValid())
      return Dval.DVAL_DOUBLE;
    return getHeight() / getWidth();
  }

  /**
   * @return a {@link Coordinate}, created from the midpoint of the bounds, or
   * null if the bounds is invalid.
   */
  public Coordinate getMidpoint() {
    if (!isValid())
      return(null);
    double midX = getMidpointX();
    double midY = getMidpointY();
    return(Coordinate.of(midX, midY));
  }

  public double getMidpointX() {
    if (!this.xBounds.isValid())
      return Dval.DVAL_DOUBLE;
    double midX = getMinX() + (getWidth() / 2.0);
    return midX;
  }

  public double getMidpointY() {
    if (!this.yBounds.isValid())
      return Dval.DVAL_DOUBLE;
    double midY = getMinY() + (getHeight() / 2.0);
    return midY;
  }

  public boolean isSame(final Bounds2D other) {
    if (other == null)
      return false;

    if (!MathUtil.doublesEqual(getMinX(), other.getMinX()))
      return false;
    if (!MathUtil.doublesEqual(getMaxX(), other.getMaxX()))
      return false;
    if (!MathUtil.doublesEqual(getMinY(), other.getMinY()))
      return false;
    if (!MathUtil.doublesEqual(getMaxY(), other.getMaxY()))
      return false;

    return true;
  }

  public void makeInvalid() {
    this.xBounds = Bounds.nullBounds();
    this.yBounds = Bounds.nullBounds();
  }

  /**
   * @return false if any value (minX, minY, maxX, maxY) is == Dval.DVAL_DOUBLE,
   * OR if minX &gt;= maxX OR minY &gt;= maxY, true otherwise.
   */
  public boolean isValid() {
    return this.xBounds.isValid() && this.yBounds.isValid();
  }

  public boolean isValidForLatLong() {
    if (!isValid())
      return false;

    if (
      getMinX() < -180
      || getMaxX() > 180
      || getMinY() < -90
      || getMaxY() > 90
    ) {
      return(false);
    }

    /* removed >= because wells are singular points and this test would fail */
    if (getMinX() > getMaxX() || getMinY() > getMaxY())
      return(false);

    return(true);
  }

  public double computeArea() {
    if (!isValid())
      return Dval.DVAL_DOUBLE;
    return getWidth() * getHeight();
  }

  public boolean definesArea() {
    if (!isValid())
      return false;
    return getWidth() > 0 && getHeight() > 0;
  }

  public boolean isDefault() {
    if (
        Double.compare(getMinX(), EmptyBounds.DEFAULT_VALUE) == 0
        && Double.compare(getMaxX(), -EmptyBounds.DEFAULT_VALUE) == 0
        && Double.compare(getMinY(), EmptyBounds.DEFAULT_VALUE) == 0
        && Double.compare(getMaxY(), -EmptyBounds.DEFAULT_VALUE) == 0
      )
      return true;
    return false;
  }
}
