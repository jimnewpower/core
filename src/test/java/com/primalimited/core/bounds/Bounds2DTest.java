package com.primalimited.core.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.primalimited.core.geometry.Coordinate;

public class Bounds2DTest {

  @Test
  public void createFromValidBounds() {
    Bounds x = Bounds.DEGREES;
    Bounds y = Bounds.DEGREES;
    Bounds2D bounds = Bounds2D.create(x, y);
    assertNotNull(bounds);
    assertTrue(bounds.isValid());
  }

  @Test
  public void createThrowsFromInvalidXBoundsArgument() {
    Bounds x = Bounds.empty();
    Bounds y = Bounds.DEGREES;
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds2D.create(x, y));
  }

  @Test
  public void createThrowsFromInvalidYBoundsArgument() {
    Bounds x = Bounds.FRACTION;
    Bounds y = Bounds.empty();
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds2D.create(x, y));
  }

  @Test
  public void fromValidArrays() {
    Bounds2D valid = Bounds2D.from(new double[] { 0, 1 }, new double[] { 0, 1 });
    assertNotNull(valid);
    assertTrue(valid.isValid());
  }

  @Test
  public void throwFromZeroLengthXArrayArgument() {
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds2D.from(new double[] { }, new double[] { 0, 1 }));
  }

  @Test
  public void throwFromZeroLengthYArrayArgument() {
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds2D.from(new double[] { 0, 1 }, new double[] { }));
  }

  @Test
  public void fromNullCoordinateCollectionThrows() {
    Collection<Coordinate> coords = null;
    assertThrows(NullPointerException.class, 
        () -> Bounds2D.from(coords));
  }

  @Test
  public void fromEmptyCoordinateCollectionThrows() {
    Collection<Coordinate> coords = new ArrayList<>();
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds2D.from(coords));
  }

  @Test
  public void fromCoordinatesCollection() {
    Collection<Coordinate> coords = new ArrayList<>();
    coords.add(Coordinate.of(0, 0));
    Bounds2D bounds = Bounds2D.from(coords);
    assertNotNull(bounds);
    assertTrue(bounds.isValid());
  }

  @Test
  public void fromNullCoordinateArrayThrows() {
    Coordinate[] coords = null;
    assertThrows(NullPointerException.class, 
        () -> Bounds2D.from(coords));
  }

  @Test
  public void fromEmptyCoordinateArrayThrows() {
    Coordinate[] coords = new Coordinate[] { };
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds2D.from(coords));
  }

  @Test
  public void fromCoordinatesArray() {
    Coordinate[] coords = new Coordinate[] { Coordinate.of(0, 0) };
    Bounds2D bounds = Bounds2D.from(coords);
    assertNotNull(bounds);
    assertTrue(bounds.isValid());
  }

  @Test
  public void valid() {
    assertTrue(validMock().isValid());
    assertFalse(invalidMock().isValid());
  }
  
  @Test
  public void validForLatLong() {
    assertTrue(Bounds2D.create(1, 179, 1, 89).isValidForLatLong());
    assertFalse(Bounds2D.create(-1, 179, 1, 89).isValidForLatLong());
    assertFalse(Bounds2D.create(1, 181, 1, 89).isValidForLatLong());
    assertFalse(Bounds2D.create(1, 179, -1, 89).isValidForLatLong());
    assertFalse(Bounds2D.create(1, 179, 1, 91).isValidForLatLong());
    assertFalse(Bounds2D.empty().isValidForLatLong());
  }
  
  @Test
  public void containsOnInvalid() {
    assertFalse(invalidMock().contains(0, 0));
  }
  
  @Test
  public void containsMinima() {
    Bounds2D bounds = validMock();
    double x = bounds.getMinX();
    double y = bounds.getMinY();
    assertTrue(bounds.contains(x, y));
  }

  @Test
  public void containsMaxima() {
    Bounds2D bounds = validMock();
    double x = bounds.getMaxX();
    double y = bounds.getMaxY();
    assertTrue(bounds.contains(x, y));
  }

  @Test
  public void containsFailsOnX() {
    Bounds2D bounds = validMock();
    double x = bounds.getMinX() - 1.0;
    double y = bounds.getMaxY();
    assertFalse(bounds.contains(x, y));
  }

  @Test
  public void containsFailsOnY() {
    Bounds2D bounds = validMock();
    double x = bounds.getMinX();
    double y = bounds.getMaxY() + 1.0;
    assertFalse(bounds.contains(x, y));
  }

  @Test
  public void isPointInsideOnInvalid() {
    assertFalse(invalidMock().isPointInside(0, 0));
  }
  
  @Test
  public void isPointInsideMinima() {
    Bounds2D bounds = validMock();
    double x = bounds.getMinX();
    double y = bounds.getMinY();
    assertTrue(bounds.isPointInside(x, y));
  }

  @Test
  public void isPointInsideMaxima() {
    Bounds2D bounds = validMock();
    double x = bounds.getMaxX();
    double y = bounds.getMaxY();
    assertTrue(bounds.isPointInside(x, y));
  }

  @Test
  public void isDefault() {
    Bounds2D bounds = Bounds2D.empty();
    assertTrue(bounds.isDefault());
  }

  @Test
  public void notDefault() {
    Bounds2D bounds = validMock();
    assertFalse(bounds.isDefault());
  }

  @Test
  public void definesArea() {
    Bounds2D bounds = validMock();
    assertTrue(bounds.definesArea());
  }

  @Test
  public void doesNotdefineArea() {
    Bounds2D bounds = Bounds2D.create(Bounds.of(0, 0), Bounds.of(0, 0));
    assertFalse(bounds.definesArea());
  }

  @Test
  public void midpoints() {
    double delta = 1e-10;
    assertEquals(50, validMock().getMidpointX(), delta);
    assertEquals(50, validMock().getMidpointY(), delta);
    
    assertEquals(50, validMock().getMidpoint().x, delta);
    assertEquals(50, validMock().getMidpoint().y, delta);
  }
  
  @Test
  public void expandToEmptyXArrayThrows() {
    assertThrows(IllegalArgumentException.class,
        () -> validMock().expandTo(new double[] { }, new double[] { 0 }));
  }

  @Test
  public void expandToEmptyYArrayThrows() {
    assertThrows(IllegalArgumentException.class,
        () -> validMock().expandTo(new double[] { 0 }, new double[] { }));
  }

  @Test
  public void expandByPositivePercent() {
    Bounds2D bounds = validMock();
    bounds.expandByPercentage(20);
    assertEquals(-10, bounds.getMinX(), 1e-10);
    assertEquals(110, bounds.getMaxX(), 1e-10);
    assertEquals(-10, bounds.getMinY(), 1e-10);
    assertEquals(110, bounds.getMaxY(), 1e-10);
  }
  
  @Test
  public void expandToOtherBounds() {
    Bounds2D bounds = validMock();
    Bounds2D other = Bounds2D.create(Bounds.of(84, 200), Bounds.of(-30, 60));
    bounds.expandTo(other);
    assertEquals(0, bounds.getMinX(), 1e-10);
    assertEquals(200, bounds.getMaxX(), 1e-10);
    assertEquals(-30, bounds.getMinY(), 1e-10);
    assertEquals(100, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToOtherBoundsOkWhenThisIsInvalid() {
    Bounds2D bounds = Bounds2D.empty();
    Bounds2D other = Bounds2D.create(Bounds.of(10, 20), Bounds.of(30, 40));
    bounds.expandTo(other);
    assertEquals(10, bounds.getMinX(), 1e-10);
    assertEquals(20, bounds.getMaxX(), 1e-10);
    assertEquals(30, bounds.getMinY(), 1e-10);
    assertEquals(40, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToOtherBoundsThrowsWhenOtherIsNull() {
    Bounds2D bounds = validMock();
    Bounds2D other = null;
    assertThrows(NullPointerException.class,
        () -> bounds.expandTo(other));
  }

  @Test
  public void expandToOtherBoundsThrowsWhenOtherIsInvalid() {
    Bounds2D bounds = validMock();
    Bounds2D other = invalidMock();
    assertThrows(IllegalArgumentException.class,
        () -> bounds.expandTo(other));
  }

  @Test
  public void disjointInvalidThis() {
    assertTrue(invalidMock().disjoint(validMock()));
  }

  @Test
  public void disjointInvalidOther() {
    assertTrue(validMock().disjoint(invalidMock()));
  }

  @Test
  public void disjointThisContainsOther() {
    Bounds2D bounds = validMock();
    Bounds2D contained = containedMock(bounds, 5/* offset */);
    assertTrue(bounds.contains(contained));
    assertFalse(bounds.disjoint(contained));
  }

  @Test
  public void disjointOtherContainsThis() {
    Bounds2D bounds = validMock();
    Bounds2D contained = containedMock(bounds, 5/* offset */);
    assertTrue(bounds.contains(contained));
    assertFalse(contained.disjoint(bounds));
  }

  @Test
  public void disjointBothValid() {
    double minX = 0;
    double maxX = 100;
    double minY = 0;
    double maxY = 100;
    Bounds2D bounds = Bounds2D.create(minX, maxX, minY, maxY);

    minX = 101;
    maxX = 200;
    minY = 101;
    maxY = 200;
    Bounds2D other = Bounds2D.create(minX, maxX, minY, maxY);
    
    assertTrue(bounds.disjoint(other));
  }

  private static Bounds2D validMock() {
    double minX = 0;
    double maxX = 100;
    double minY = 0;
    double maxY = 100;
    return Bounds2D.create(minX, maxX, minY, maxY);
  }
  
  private static Bounds2D invalidMock() {
    return Bounds2D.empty();
  }
  
  private static Bounds2D containedMock(Bounds2D container, int offset) {
    double minX = container.getMinX() + offset;
    double maxX = container.getMaxX() - offset;
    double minY = container.getMinY() + offset;
    double maxY = container.getMaxY() - offset;
    return Bounds2D.create(minX, maxX, minY, maxY);
  }
}
