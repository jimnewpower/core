package com.primalimited.core.bounds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import com.primalimited.core.dval.Dval;
import com.primalimited.core.geometry.Coordinate;

public class Bounds2DTest {
  @Test
  public void createFromOtherValid() {
    Bounds2D bounds = Bounds2D.from(validMock());
    assertNotNull(bounds);
    assertIsValidMock(bounds);
  }

  @Test
  public void createFromOtherInvalid() {
    Bounds2D bounds = Bounds2D.from(invalidMock());
    assertNotNull(bounds);
    assertFalse(Bounds.valid(bounds.getMinX(), bounds.getMaxX()));
    assertFalse(Bounds.valid(bounds.getMinY(), bounds.getMaxY()));
  }

  @Test
  public void testToString() {
    assertEquals("Bounds2D x=[0..100], y=[20..80]", validMock().toString());
  }

  @Test
  public void getXBounds() {
    Bounds2D bounds = Bounds2D.from(validMock());
    Bounds x = bounds.xBounds();
    double delta = 1e-10;
    assertEquals(0, x.getMin(), delta);
    assertEquals(100, x.getMax(), delta);
  }

  @Test
  public void getXBoundsInvalid() {
    Bounds2D bounds = Bounds2D.from(invalidMock());
    Bounds x = bounds.xBounds();
    assertFalse(x.isValid());
  }

  @Test
  public void getYBounds() {
    Bounds2D bounds = Bounds2D.from(validMock());
    Bounds y = bounds.yBounds();
    double delta = 1e-10;
    assertEquals(20, y.getMin(), delta);
    assertEquals(80, y.getMax(), delta);
  }

  @Test
  public void getWidth() {
    assertEquals(100, validMock().getWidth());
    assertTrue(Dval.isDval(invalidMock().getWidth()));
  }

  @Test
  public void getHeight() {
    assertEquals(60, validMock().getHeight());
    assertTrue(Dval.isDval(invalidMock().getHeight()));
  }

  @Test
  public void getYBoundsInvalid() {
    Bounds2D bounds = Bounds2D.from(invalidMock());
    Bounds y = bounds.yBounds();
    assertFalse(y.isValid());
  }

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
  public void expandToValues_InvalidMinX() {
    Bounds2D bounds = validMock();
    
    bounds.expandTo(Dval.DVAL_DOUBLE/* srcMinX */, 200/* srcMaxX */, 2/* srcMinY */, 96/* srcMaxY */);
    assertEquals(0.0/*unchanged*/, bounds.getMinX(), 1e-10);
    assertEquals(200, bounds.getMaxX(), 1e-10);
    assertEquals(2, bounds.getMinY(), 1e-10);
    assertEquals(96, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToValues_InvalidMaxX() {
    Bounds2D bounds = validMock();
    
    bounds.expandTo(-5/* srcMinX */, Dval.DVAL_DOUBLE/* srcMaxX */, 2/* srcMinY */, 96/* srcMaxY */);
    assertEquals(-5, bounds.getMinX(), 1e-10);
    assertEquals(100/*unchanged*/, bounds.getMaxX(), 1e-10);
    assertEquals(2, bounds.getMinY(), 1e-10);
    assertEquals(96, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToValues_InvalidMinY() {
    Bounds2D bounds = validMock();
    
    bounds.expandTo(-5/* srcMinX */, 200/* srcMaxX */, Dval.DVAL_DOUBLE/* srcMinY */, 96/* srcMaxY */);
    assertEquals(-5, bounds.getMinX(), 1e-10);
    assertEquals(200, bounds.getMaxX(), 1e-10);
    assertEquals(20/*unchanged*/, bounds.getMinY(), 1e-10);
    assertEquals(96, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToValues_InvalidMaxY() {
    Bounds2D bounds = validMock();
    
    bounds.expandTo(-5/* srcMinX */, 200/* srcMaxX */, 2/* srcMinY */, Dval.DVAL_DOUBLE/* srcMaxY */);
    assertEquals(-5, bounds.getMinX(), 1e-10);
    assertEquals(200, bounds.getMaxX(), 1e-10);
    assertEquals(2, bounds.getMinY(), 1e-10);
    assertEquals(80/*unchanged*/, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToArrays() {
    double[] xs = new double[] { 0, 1, 2, 4, 8 };
    double[] ys = new double[] { 3, 9, 81 };
    Bounds2D bounds = Bounds2D.from(xs, ys);
    
    xs = new double[] { -16, -8, -4, -2 };
    ys = new double[] { 111, 222, 333 };
    bounds.expandTo(xs, ys);
    assertEquals(-16, bounds.getMinX(), 1e-10);
    assertEquals(8, bounds.getMaxX(), 1e-10);
    assertEquals(3, bounds.getMinY(), 1e-10);
    assertEquals(333, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToInvalidArrays() {
    double[] xs = new double[] { 0, 1, 2, 4, 8 };
    double[] ys = new double[] { 3, 9, 81 };
    Bounds2D bounds = Bounds2D.from(xs, ys);
    
    xs = new double[] { Dval.DVAL_DOUBLE };
    ys = new double[] { Double.NaN };
    bounds.expandTo(xs, ys);
    assertEquals(0, bounds.getMinX(), 1e-10);
    assertEquals(8, bounds.getMaxX(), 1e-10);
    assertEquals(3, bounds.getMinY(), 1e-10);
    assertEquals(81, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandToNullCoordinatesArrayThrows() {
    Coordinate[] coords = null;
    assertThrows(NullPointerException.class, 
        () -> validMock().expandTo(coords));
  }

  @Test
  public void expandToEmptyCoordinatesArrayThrows() {
    Coordinate[] coords = new Coordinate[] { };
    assertThrows(IllegalArgumentException.class, 
        () -> validMock().expandTo(coords));
  }

  @Test
  public void expandToInvalidCoordinatesArrayNoChange() {
    Coordinate[] coords = new Coordinate[] { Coordinate.of(Dval.DVAL_DOUBLE, Dval.DVAL_DOUBLE) };
    Bounds2D bounds = validMock();
    bounds.expandTo(coords);
    assertIsValidMock(bounds);
  }

  @Test
  public void expandToValidCoordinatesArray() {
    Coordinate[] coords = new Coordinate[] { 
        Coordinate.of(-1000, -2000),
        Coordinate.of(-1000, 2000),
        Coordinate.of(1000, -2000),
        Coordinate.of(1000, 2000)
        };
    Bounds2D bounds = validMock();

    bounds.expandTo(coords);
    
    double delta = 1e-10;
    assertEquals(-1000, bounds.getMinX(), delta);
    assertEquals(1000, bounds.getMaxX(), delta);
    assertEquals(-2000, bounds.getMinY(), delta);
    assertEquals(2000, bounds.getMaxY(), delta);
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
    
    Bounds2D bounds = invalidMock();
    bounds.setXValues(5, 10);
    assertFalse(bounds.isValid());
    
    bounds = invalidMock();
    bounds.setYValues(5, 10);
    assertFalse(bounds.isValid());
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
    assertFalse(invalidMock().contains(validMock()));
    assertFalse(invalidMock().contains(0, 0));
    assertFalse(validMock().contains(invalidMock()));
    
    //disjoint
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

    assertFalse(bounds.contains(other));
    assertFalse(other.contains(bounds));
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
  public void xOverlapsMinButDoesNotContain() {
    Bounds2D left = Bounds2D.create(Bounds.of(-1, 1), validMock().yBounds());
    assertFalse(validMock().contains(left));
  }

  @Test
  public void xOverlapsMaxButDoesNotContain() {
    Bounds2D right = Bounds2D.create(Bounds.of(99, 101), validMock().yBounds());
    assertFalse(validMock().contains(right));
  }

  @Test
  public void yOverlapsMinButDoesNotContain() {
    Bounds2D bounds = Bounds2D.create(validMock().xBounds(), Bounds.of(19, 21));
    assertFalse(validMock().contains(bounds));
  }

  @Test
  public void yOverlapsMaxButDoesNotContain() {
    Bounds2D bounds = Bounds2D.create(validMock().xBounds(), Bounds.of(79, 81));
    assertFalse(validMock().contains(bounds));
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
    assertFalse(validMock().isDefault());
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
    assertFalse(invalidMock().definesArea());
  }

  @Test
  public void definesAreaInvalid() {
    Bounds2D bounds = invalidMock();
    assertFalse(bounds.definesArea());
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
    
    assertTrue(Dval.isDval(invalidMock().getMidpointX()));
    assertTrue(Dval.isDval(invalidMock().getMidpointY()));
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
  public void expandInvalid() {
    Bounds2D bounds = invalidMock();

    bounds.expandByPercentage(50);
    assertTrue(bounds.isDefault());
    assertFalse(bounds.isValid());

    bounds.expandWidthByPercentage(50);
    assertTrue(bounds.isDefault());
    assertFalse(bounds.isValid());

    bounds.expandHeightByPercentage(50);
    assertTrue(bounds.isDefault());
    assertFalse(bounds.isValid());
  }
  
  @Test
  public void expandByPositivePercent() {
    Bounds2D bounds = validMock();
    bounds.expandByPercentage(20);
    assertEquals(-10, bounds.getMinX(), 1e-10);
    assertEquals(110, bounds.getMaxX(), 1e-10);
    assertEquals(14, bounds.getMinY(), 1e-10);
    assertEquals(86, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandByNegativePercent() {
    Bounds2D bounds = validMock();
    bounds.expandByPercentage(-20);
    assertEquals(10, bounds.getMinX(), 1e-10);
    assertEquals(90, bounds.getMaxX(), 1e-10);
    assertEquals(26, bounds.getMinY(), 1e-10);
    assertEquals(74, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandByInvalidPercent() {
    Bounds2D bounds = validMock();
    bounds.expandByPercentage(101);
    assertIsValidMock(bounds);
    bounds.expandByPercentage(-101);
    assertIsValidMock(bounds);
  }

  @Test
  public void expandWidthByPositivePercent() {
    Bounds2D bounds = validMock();
    bounds.expandWidthByPercentage(20);
    assertEquals(-10, bounds.getMinX(), 1e-10);
    assertEquals(110, bounds.getMaxX(), 1e-10);
    assertEquals(20, bounds.getMinY(), 1e-10);
    assertEquals(80, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandWidthByNegativePercent() {
    Bounds2D bounds = validMock();
    bounds.expandWidthByPercentage(-20);
    assertEquals(10, bounds.getMinX(), 1e-10);
    assertEquals(90, bounds.getMaxX(), 1e-10);
    assertEquals(20, bounds.getMinY(), 1e-10);
    assertEquals(80, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandWidthByInvalidPercent() {
    Bounds2D bounds = validMock();
    bounds.expandWidthByPercentage(101);
    assertIsValidMock(bounds);
    bounds.expandWidthByPercentage(-101);
    assertIsValidMock(bounds);
  }

  @Test
  public void expandHeightByPositivePercent() {
    Bounds2D bounds = validMock();
    bounds.expandHeightByPercentage(20);
    assertEquals(0, bounds.getMinX(), 1e-10);
    assertEquals(100, bounds.getMaxX(), 1e-10);
    assertEquals(14, bounds.getMinY(), 1e-10);
    assertEquals(86, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandHeightByNegativePercent() {
    Bounds2D bounds = validMock();
    bounds.expandHeightByPercentage(-20);
    assertEquals(0, bounds.getMinX(), 1e-10);
    assertEquals(100, bounds.getMaxX(), 1e-10);
    assertEquals(26, bounds.getMinY(), 1e-10);
    assertEquals(74, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void expandHeightByInvalidPercent() {
    Bounds2D bounds = validMock();
    bounds.expandHeightByPercentage(101);
    assertIsValidMock(bounds);
    bounds.expandHeightByPercentage(-101);
    assertIsValidMock(bounds);
  }

  @Test
  public void expandToOtherBounds() {
    Bounds2D bounds = validMock();
    Bounds2D other = Bounds2D.create(Bounds.of(84, 200), Bounds.of(-30, 60));
    bounds.expandTo(other);
    assertEquals(0, bounds.getMinX(), 1e-10);
    assertEquals(200, bounds.getMaxX(), 1e-10);
    assertEquals(-30, bounds.getMinY(), 1e-10);
    assertEquals(80, bounds.getMaxY(), 1e-10);
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
  public void expandToDvalXHasNoEffect() {
    Bounds2D bounds = validMock();
    bounds.expandTo(Dval.DVAL_DOUBLE, 40.0);
    assertIsValidMock(bounds);
  }

  @Test
  public void expandToDvalYHasNoEffect() {
    Bounds2D bounds = validMock();
    bounds.expandTo(50, Dval.DVAL_DOUBLE);
    assertIsValidMock(bounds);
  }

  @Test
  public void expandToDvalXAndYHasNoEffect() {
    Bounds2D bounds = validMock();
    bounds.expandTo(Dval.DVAL_DOUBLE, Dval.DVAL_DOUBLE);
    assertIsValidMock(bounds);
  }

  @Test
  public void expandToCoordinateBothHigher() {
    Bounds2D bounds = validMock();
    Coordinate c = Coordinate.of(200, 300);

    bounds.expandTo(c);
    
    double delta = 1e-10;
    assertEquals(0, bounds.getMinX(), delta);
    assertEquals(200, bounds.getMaxX(), delta);
    assertEquals(20, bounds.getMinY(), delta);
    assertEquals(300, bounds.getMaxY(), delta);
  }

  @Test
  public void expandToCoordinateBothLower() {
    Bounds2D bounds = validMock();
    Coordinate c = Coordinate.of(-10, 10);

    bounds.expandTo(c);
    
    double delta = 1e-10;
    assertEquals(-10, bounds.getMinX(), delta);
    assertEquals(100, bounds.getMaxX(), delta);
    assertEquals(10, bounds.getMinY(), delta);
    assertEquals(80, bounds.getMaxY(), delta);
  }

  @Test
  public void expandToNullCoordinateThrows() {
    Bounds2D bounds = validMock();
    Coordinate c = null;
    assertThrows(NullPointerException.class,
        () -> bounds.expandTo(c));
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

  @Test
  public void intersects() {
    Bounds2D bounds0 = Bounds2D.create(Bounds.of(10, 20), Bounds.of(0, 50));
    Bounds2D bounds1 = Bounds2D.create(Bounds.of(19, 30), Bounds.of(49, 100));
    assertTrue(bounds0.intersects(bounds1) && bounds1.intersects(bounds0));

    // x and y both fail
    bounds0 = Bounds2D.create(Bounds.of(10, 20), Bounds.of(0, 50));
    bounds1 = Bounds2D.create(Bounds.of(21, 30), Bounds.of(51, 100));
    assertFalse(bounds0.intersects(bounds1) && bounds1.intersects(bounds0));

    // x fails high
    bounds0 = Bounds2D.create(Bounds.of(10, 20), Bounds.of(0, 50));
    bounds1 = Bounds2D.create(Bounds.of(21, 30), Bounds.of(1, 25));
    assertFalse(bounds0.intersects(bounds1) && bounds1.intersects(bounds0));

    // x fails low
    bounds0 = Bounds2D.create(Bounds.of(10, 20), Bounds.of(0, 50));
    bounds1 = Bounds2D.create(Bounds.of(1, 9), Bounds.of(1, 25));
    assertFalse(bounds0.intersects(bounds1) && bounds1.intersects(bounds0));

    // y fails high
    bounds0 = Bounds2D.create(Bounds.of(10, 20), Bounds.of(0, 50));
    bounds1 = Bounds2D.create(Bounds.of(0, 30), Bounds.of(60, 80));
    assertFalse(bounds0.intersects(bounds1) && bounds1.intersects(bounds0));

    // y fails low
    bounds0 = Bounds2D.create(Bounds.of(10, 20), Bounds.of(0, 50));
    bounds1 = Bounds2D.create(Bounds.of(0, 30), Bounds.of(-20, -2));
    assertFalse(bounds0.intersects(bounds1) && bounds1.intersects(bounds0));
  }

  @Test
  public void intersectsInvalid() {
    assertFalse(validMock().intersects(invalidMock()));
    assertFalse(invalidMock().intersects(validMock()));
  }

  @Test
  public void reset() {
    Bounds2D bounds = validMock();
    assertIsValidMock(bounds);
    assertFalse(bounds.isDefault());
    
    bounds.reset();

    assertTrue(bounds.isDefault());
    assertFalse(Bounds.valid(bounds.getMinX(), bounds.getMaxX()));
    assertFalse(Bounds.valid(bounds.getMinY(), bounds.getMaxY()));
  }

  @Test
  public void makeInvalid() {
    Bounds2D bounds = validMock();
    assertIsValidMock(bounds);
    
    bounds.makeInvalid();

    assertFalse(Bounds.valid(bounds.getMinX(), bounds.getMaxX()));
    assertFalse(Bounds.valid(bounds.getMinY(), bounds.getMaxY()));
  }

  @Test
  public void ratios() {
    Bounds2D bounds = validMock();
    assertIsValidMock(bounds);

    double delta = 1e-10;
    assertEquals(1.66666666666667, bounds.ratioXY(), delta);
    assertEquals(0.6, bounds.ratioYX(), delta);
  }

  @Test
  public void ratioYXWithZeroXRange() {
    Bounds2D bounds = validMock();
    bounds.setXValues(50, 50);
    assertTrue(Dval.isDval(bounds.ratioYX()));
  }

  @Test
  public void ratioYXInvalidBounds() {
    Bounds2D bounds = invalidMock();
    assertTrue(Dval.isDval(bounds.ratioYX()));
  }

  @Test
  public void ratioXYWithZeroYRange() {
    Bounds2D bounds = validMock();
    bounds.setYValues(40, 40);
    assertTrue(Dval.isDval(bounds.ratioXY()));
  }

  @Test
  public void ratioXYInvalidBounds() {
    Bounds2D bounds = invalidMock();
    assertTrue(Dval.isDval(bounds.ratioXY()));
  }

  @Test
  public void setXValues() {
    Bounds2D bounds = validMock();
    bounds.setXValues(3, 44);
    assertEquals(3, bounds.getMinX(), 1e-10);
    assertEquals(44, bounds.getMaxX(), 1e-10);
  }

  @Test
  public void setXValuesSame() {
    Bounds2D bounds = validMock();
    bounds.setXValues(50, 50);
    assertEquals(50, bounds.getMinX(), 1e-10);
    assertEquals(50, bounds.getMaxX(), 1e-10);
  }

  @Test
  public void setXValuesInvalid() {
    Bounds2D bounds = validMock();
    bounds.setXValues(Double.NaN, 5);
    // nothing should change
    assertEquals(0, bounds.getMinX(), 1e-10);
    assertEquals(100, bounds.getMaxX(), 1e-10);

    bounds = validMock();
    bounds.setXValues(3, Dval.DVAL_DOUBLE);
    // nothing should change
    assertEquals(0, bounds.getMinX(), 1e-10);
    assertEquals(100, bounds.getMaxX(), 1e-10);
  }

  @Test
  public void setYValues() {
    Bounds2D bounds = validMock();
    bounds.setYValues(3, 44);
    assertEquals(3, bounds.getMinY(), 1e-10);
    assertEquals(44, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void setYValuesSame() {
    Bounds2D bounds = validMock();
    bounds.setYValues(50, 50);
    assertEquals(50, bounds.getMinY(), 1e-10);
    assertEquals(50, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void setYValuesInvalid() {
    Bounds2D bounds = validMock();
    bounds.setYValues(Double.NaN, 5);
    // nothing should change
    assertEquals(20, bounds.getMinY(), 1e-10);
    assertEquals(80, bounds.getMaxY(), 1e-10);

    bounds = validMock();
    bounds.setYValues(3, Dval.DVAL_DOUBLE);
    // nothing should change
    assertEquals(20, bounds.getMinY(), 1e-10);
    assertEquals(80, bounds.getMaxY(), 1e-10);
  }

  @Test
  public void midpointInvalid() {
    assertNull(invalidMock().getMidpoint());
  }
  
  @Test
  public void computeArea() {
    Bounds2D bounds = validMock();
    assertEquals(6000, bounds.computeArea(), 1e-10);
  }
  
  @Test
  public void computeAreaInvalid() {
    Bounds2D bounds = invalidMock();
    assertTrue(Dval.isDval(bounds.computeArea()));
  }
  
  private static Bounds2D validMock() {
    double minX = 0;
    double maxX = 100;
    double minY = 20;
    double maxY = 80;
    return Bounds2D.create(minX, maxX, minY, maxY);
  }
  
  private void assertIsValidMock(Bounds2D bounds) {
    double delta = 1e-10;
    assertEquals(0, bounds.getMinX(), delta);
    assertEquals(100, bounds.getMaxX(), delta);
    assertEquals(20, bounds.getMinY(), delta);
    assertEquals(80, bounds.getMaxY(), delta);
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
