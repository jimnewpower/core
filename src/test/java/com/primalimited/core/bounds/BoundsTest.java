package com.primalimited.core.bounds;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.primalimited.core.dval.Dval;

public class BoundsTest {
  @Test
  public void constants() {
    double delta = 1e-10;
    
    assertEquals(0, Bounds.PROBABILITY.getMin(), delta);
    assertEquals(1, Bounds.PROBABILITY.getMax(), delta);

    assertEquals(0, Bounds.FRACTION.getMin(), delta);
    assertEquals(1, Bounds.FRACTION.getMax(), delta);

    assertEquals(0, Bounds.PERCENT.getMin(), delta);
    assertEquals(100, Bounds.PERCENT.getMax(), delta);

    assertEquals(0, Bounds.RADIANS.getMin(), delta);
    assertEquals(2 * Math.PI, Bounds.RADIANS.getMax(), delta);

    assertEquals(0, Bounds.DEGREES.getMin(), delta);
    assertEquals(360, Bounds.DEGREES.getMax(), delta);

    assertEquals(0, Bounds.LATITUDE.getMin(), delta);
    assertEquals(90, Bounds.LATITUDE.getMax(), delta);

    assertEquals(0, Bounds.LONGITUDE.getMin(), delta);
    assertEquals(180, Bounds.LONGITUDE.getMax(), delta);

    assertEquals(0, Bounds.RGB_8_BIT.getMin(), delta);
    assertEquals(255, Bounds.RGB_8_BIT.getMax(), delta);
  }
  
  @Test
  public void boundValue() {
    Bounds bounds = Bounds.of(8, 16);
    assertEquals(8, bounds.bound(2), 1e-10);
    assertEquals(16, bounds.bound(20), 1e-10);
  }

  @Test
  public void boundIntToDoublePrecisionBoundsShouldThrow() {
    Bounds bounds = Bounds.of(1.25, 2.50);
    assertThrows(IllegalStateException.class,
        () -> bounds.bound(2));
  }

  @Test
  public void zeroRangeOnInvalid() {
    Bounds bounds = Bounds.nullBounds();
    assertFalse(bounds.rangeIsZero());
  }

  @Test
  public void zeroRange() {
    double value = 55.2468101214;
    Bounds bounds = Bounds.of(value, value);
    assertTrue(bounds.rangeIsZero());
  }

  @Test
  public void nonZeroRange() {
    double min = 55.2468101211;
    double max = 55.2468101219;
    Bounds bounds = Bounds.of(min, max);
    assertFalse(bounds.rangeIsZero());
  }

  @Test
  public void typicalValidBounds() {
    double min = 3.2;
    double max = 6.4;
    Bounds bounds = Bounds.immutable(min, max);
    assertEquals(min, bounds.getMin(), 0.0);
    assertEquals(max, bounds.getMax(), 0.0);
    assertEquals(max-min, bounds.getRange(), 0.0);
    assertTrue(bounds.isValid());
  }

  @Test
  public void identicalMinMaxValidBounds() {
    double min = 1.0;
    double max = 1.0;
    Bounds bounds = Bounds.immutable(min, max);
    assertEquals(min, bounds.getMin(), 0.0);
    assertEquals(max, bounds.getMax(), 0.0);
    assertEquals(max-min, bounds.getRange(), 0.0);
    assertTrue(bounds.isValid());
  }

  @Test
  public void expandSingleValue() {
    Bounds unchanged = Bounds.expand(Bounds.PERCENT, Dval.DVAL_DOUBLE);
    assertEquals(0, unchanged.getMin(), 1e-10);
    assertEquals(100, unchanged.getMax(), 1e-10);

    Bounds changed = Bounds.expand(Bounds.PERCENT, 200);
    assertEquals(0, changed.getMin(), 1e-10);
    assertEquals(200, changed.getMax(), 1e-10);
  }
  
  @Test
  public void expandArray() {
    Bounds orig = Bounds.of(0, 1);
    Bounds expanded = Bounds.expand(orig, new double[] { });
    assertEquals(0, expanded.getMin(), 1e-10);
    assertEquals(1, expanded.getMax(), 1e-10);

    expanded = Bounds.expand(orig, new double[] { 0, 1 });
    assertEquals(0, expanded.getMin(), 1e-10);
    assertEquals(1, expanded.getMax(), 1e-10);

    expanded = Bounds.expand(orig, new double[] { 2, 2 });
    assertEquals(0, expanded.getMin(), 1e-10);
    assertEquals(2, expanded.getMax(), 1e-10);

    expanded = Bounds.expand(orig, new double[] { -1, -1 });
    assertEquals(-1, expanded.getMin(), 1e-10);
    assertEquals(1, expanded.getMax(), 1e-10);
    
    expanded = Bounds.expand(Bounds.nullBounds(), new double[] { 2, 4, 8, 16, 32, 64 });
    assertEquals(2, expanded.getMin(), 1e-10);
    assertEquals(64, expanded.getMax(), 1e-10);
  }

  @Test
  public void expandByPositivePercent() {
    Bounds original = Bounds.of(0, 1);
    double percent = 10;
    Bounds expanded = Bounds.expandByPercent(original, percent);
    assertNotNull(expanded);
    assertTrue(expanded.isValid());
    assertEquals(-0.05, expanded.getMin(), 1e-10);
    assertEquals(1.05, expanded.getMax(), 1e-10);

    original = Bounds.DEGREES;
    percent = 20;
    expanded = Bounds.expandByPercent(original, percent);
    assertNotNull(expanded);
    assertTrue(expanded.isValid());
    assertEquals(-36, expanded.getMin(), 1e-10);
    assertEquals(396, expanded.getMax(), 1e-10);
  }

  @Test
  public void expandByNegativePercent() {
    Bounds original = Bounds.of(0, 1);
    double percent = -10;
    Bounds expanded = Bounds.expandByPercent(original, percent);
    assertNotNull(expanded);
    assertTrue(expanded.isValid());
    assertEquals(0.05, expanded.getMin(), 1e-10);
    assertEquals(0.95, expanded.getMax(), 1e-10);
    
    original = Bounds.RGB_8_BIT;
    percent = -15;
    expanded = Bounds.expandByPercent(original, percent);
    assertNotNull(expanded);
    assertTrue(expanded.isValid());
    assertEquals(19.125, expanded.getMin(), 1e-10);
    assertEquals(235.875, expanded.getMax(), 1e-10);
  }

  @Test
  public void expandByInvalidPercent() {
    Bounds original = Bounds.of(0, 1);
    double percent = 938457;
    Bounds expanded = Bounds.expandByPercent(original, percent);
    assertNotNull(expanded);
    assertTrue(expanded.isValid());
    assertEquals(original.getMin(), expanded.getMin(), 1e-10);
    assertEquals(original.getMax(), expanded.getMax(), 1e-10);
  }

  @Test
  public void minMaxOverlapping() {
    Bounds bounds0 = Bounds.of(0, 10);
    Bounds bounds1 = Bounds.of(5, 12);
    
    Bounds minMax = Bounds.minMax(bounds0, bounds1);
    assertEquals(0, minMax.getMin(), 1e-10);
    assertEquals(12, minMax.getMax(), 1e-10);
    
    minMax = Bounds.minMax(bounds1, bounds0);
    assertEquals(0, minMax.getMin(), 1e-10);
    assertEquals(12, minMax.getMax(), 1e-10);
  }

  @Test
  public void minMaxDisjoint() {
    Bounds bounds0 = Bounds.of(0, 10);
    Bounds bounds1 = Bounds.of(15, 20);
    
    Bounds minMax = Bounds.minMax(bounds0, bounds1);
    assertEquals(0, minMax.getMin(), 1e-10);
    assertEquals(20, minMax.getMax(), 1e-10);
    
    minMax = Bounds.minMax(bounds1, bounds0);
    assertEquals(0, minMax.getMin(), 1e-10);
    assertEquals(20, minMax.getMax(), 1e-10);
  }

  @Test
  public void minMaxNullArg0() {
    Bounds bounds0 = null;
    Bounds bounds1 = Bounds.of(15, 20);
    
    assertThrows(NullPointerException.class, 
        () -> Bounds.minMax(bounds0, bounds1));
  }

  @Test
  public void minMaxNullArg1() {
    Bounds bounds0 = Bounds.PERCENT;
    Bounds bounds1 = null;
    
    assertThrows(NullPointerException.class, 
        () -> Bounds.minMax(bounds0, bounds1));
  }

  @Test
  public void minMaxInvalidArg0() {
    Bounds bounds0 = Bounds.nullBounds();
    Bounds bounds1 = Bounds.of(15, 20);
    
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds.minMax(bounds0, bounds1));
  }

  @Test
  public void minMaxInvalidArg1() {
    Bounds bounds0 = Bounds.DEGREES;
    Bounds bounds1 = Bounds.empty();
    
    assertThrows(IllegalArgumentException.class, 
        () -> Bounds.minMax(bounds0, bounds1));
  }

  @Test
  public void createFromArray() {
    Bounds bounds = Bounds.of(new double[] {
       2, 4, 6, 8, 10, 12, 14, 16, 18
    });
    assertTrue(bounds.isValid());
    assertEquals(2.0, bounds.getMin(), 0.0);
    assertEquals(18.0, bounds.getMax(), 0.0);
    assertEquals(16.0, bounds.getRange(), 0.0);
  }

  @Test
  public void createFromEmptyOrDvalArray() {
    Bounds bounds = Bounds.of(new double[] { });
    // empty array should return NullBounds object, which is invalid
    // and returns DVAL for min(), max(), range()
    assertFalse(bounds.isValid());
    assertTrue(Dval.isDval(bounds.getMin()));
    assertTrue(Dval.isDval(bounds.getMax()));
    assertTrue(Dval.isDval(bounds.getRange()));

    bounds = Bounds.of(new double[] { Dval.DVAL_DOUBLE, Dval.DVAL_DOUBLE });
    // array of all DVALs should return NullBounds object, which is invalid
    // and returns DVAL for min(), max(), range()
    assertFalse(bounds.isValid());
    assertTrue(Dval.isDval(bounds.getMin()));
    assertTrue(Dval.isDval(bounds.getMax()));
    assertTrue(Dval.isDval(bounds.getRange()));
  }

  @Test
  public void createFromCollection() {
    Collection<Double> doubles = new ArrayList<>();
    doubles.add(Double.valueOf(324.0));
    doubles.add(Double.valueOf(586.0));
    doubles.add(Double.valueOf(919.0));
    doubles.add(Double.valueOf(101.0));
    Bounds bounds = Bounds.of(doubles);
    assertTrue(bounds.isValid());
    assertEquals(101.0, bounds.getMin(), 0.0);
    assertEquals(919.0, bounds.getMax(), 0.0);
    assertEquals(818.0, bounds.getRange(), 0.0);
  }

  @Test
  public void createFromBadCollectionShouldReturnNullBounds() {
    Collection<Double> doubles = new ArrayList<>();
    Bounds bounds = Bounds.of(doubles);
    // empty collection should return NullBounds object, which is invalid
    // and returns DVAL for min(), max(), range()
    assertFalse(bounds.isValid());
    assertTrue(Dval.isDval(bounds.getMin()));
    assertTrue(Dval.isDval(bounds.getMax()));
    assertTrue(Dval.isDval(bounds.getRange()));
  }

  @Test
  public void fractionBetween() {
    Bounds bounds = Bounds.of(0, 100);
    double tolerance = 1e-5;
    assertEquals(0.0,  bounds.getFractionBetween(0), tolerance);
    assertEquals(0.1,  bounds.getFractionBetween(10), tolerance);
    assertEquals(0.25, bounds.getFractionBetween(25), tolerance);
    assertEquals(0.33, bounds.getFractionBetween(33), tolerance);
    assertEquals(0.5,  bounds.getFractionBetween(50), tolerance);
    assertEquals(0.75, bounds.getFractionBetween(75), tolerance);
    assertEquals(1.0,  bounds.getFractionBetween(100), tolerance);
  }

  @Test
  public void fractionBetweenZeroRange() {
    Bounds bounds = Bounds.of(0, 0);
    assertEquals(0.0, bounds.getFractionBetween(0.0), 0.0);
    assertTrue(Dval.isDval(bounds.getFractionBetween(-1.0)));
    assertTrue(Dval.isDval(bounds.getFractionBetween(1.0)));
  }

  @Test
  public void staticValidTest() {
    assertFalse(Bounds.valid(0, Dval.DVAL_DOUBLE));
    assertFalse(Bounds.valid(Dval.DVAL_DOUBLE, 0));
  }
  
  @Test
  public void validForLogScale() {
    assertFalse(Bounds.empty().isValidForLogScale());
    assertFalse(Bounds.nullBounds().isValidForLogScale());

    assertTrue(Bounds.of(1e-10, 1).isValidForLogScale());
    assertTrue(Bounds.of(1, 10).isValidForLogScale());
    assertTrue(Bounds.of(1000, 100000).isValidForLogScale());
    assertFalse(Bounds.of(0, 10).isValidForLogScale());
    assertFalse(Bounds.of(-5, 200).isValidForLogScale());
    assertFalse(Bounds.of(-50, -10).isValidForLogScale());
  }
  
  @Test
  public void isNull() {
    assertTrue(Bounds.nullBounds().isNull());
    assertFalse(Bounds.DEGREES.isNull());

    // min dval but valid max == not null
    assertFalse(new Bounds() {
      @Override public double getMin() { return Dval.DVAL_DOUBLE; }
      @Override public double getMax() { return 0; }
      @Override public double getRange() { return 0; }
    }.isNull());

    // max dval but valid min == not null
    assertFalse(new Bounds() {
      @Override public double getMin() { return 0; }
      @Override public double getMax() { return Dval.DVAL_DOUBLE; }
      @Override public double getRange() { return 0; }
    }.isNull());
  }
  
  @Test
  public void boundInts() {
    assertEquals(Bounds.PERCENT.getMin(), Bounds.PERCENT.bound(-32));
    assertEquals(Bounds.PERCENT.getMax(), Bounds.PERCENT.bound(125));
  }

  @Test
  public void boundDoubles() {
    assertEquals(Bounds.FRACTION.getMin(), Bounds.FRACTION.bound(-0.234523));
    assertEquals(Bounds.FRACTION.getMax(), Bounds.FRACTION.bound(1.29834));
  }

  @Test
  public void expandToImmutableShouldThrow() {
    assertThrows(IllegalStateException.class, () -> Bounds.FRACTION.expandTo(1.2345));
  }
  
  @Test
  public void getBinInvalidBin() {
    assertEquals(-1, Bounds.FRACTION.getBin(-1, 10/* nBins */));
  }

  @Test
  public void getBinInvalidNBins() {
    assertEquals(-1, Bounds.FRACTION.getBin(1, Dval.DVAL_INT/* nBins */));
  }

  @Test
  public void intervalsAndBins() {
    int nBins = 3;
    Bounds bounds = Bounds.of(0, 3000);
    assertEquals(0, bounds.getBin(bounds.getMin(), nBins));
    assertEquals(0, bounds.getBin(500, nBins));
    assertEquals(1, bounds.getBin(1500, nBins));
    assertEquals(2, bounds.getBin(2500, nBins));
    assertEquals(nBins-1, bounds.getBin(bounds.getMax(), nBins));

    bounds = Bounds.of(0, 100);
    nBins = 100;
    for (int i=0; i<100; i++) {
      double val = i + 0.5;
      assertEquals(i, bounds.getBin(val, nBins));
    }
    assertEquals(0, bounds.getBin(bounds.getMin(), nBins));
    assertEquals(nBins-1, bounds.getBin(bounds.getMax(), nBins));

    bounds = Bounds.of(-100, 100);
    nBins = 100;
    assertEquals( 0, bounds.getBin(-99.5, nBins));
    assertEquals(99, bounds.getBin( 99.5, nBins));

    assertEquals(-1, Bounds.of(0, 1).getBin(Dval.DVAL_DOUBLE, 100));
    assertEquals(-1, Bounds.of(0, 1).getBin(-1, 10));
    assertEquals(-1, Bounds.of(0, 1).getBin(Dval.DVAL_DOUBLE, 0));

    bounds = Bounds.of(20.0, 80.0);
    assertEquals(0, bounds.getBin(20.0, 60/*nBins*/));
    assertEquals(59, bounds.getBin(80.0, 60/*nBins*/));
  }

  @Test
  public void overlapsInvalidThis() {
    assertFalse(Bounds.nullBounds().overlaps(Bounds.FRACTION));
  }

  @Test
  public void overlapsInvalidOther() {
    assertFalse(Bounds.PERCENT.overlaps(Bounds.empty()));
  }

  @Test
  public void overlapsNullOtherArgThrows() {
    Bounds other = null;
    assertThrows(NullPointerException.class,
        () -> Bounds.PERCENT.overlaps(other));
  }

  @Test
  public void overlaps() {
    assertTrue(Bounds.of(0, 0).overlaps(Bounds.of(0, 0)));
    assertTrue(Bounds.of(0, 1).overlaps(Bounds.of(0, 1)));
    assertTrue(Bounds.of(0, 100).overlaps(Bounds.of(10, 90)));
    assertTrue(Bounds.of(10, 90).overlaps(Bounds.of(0, 100)));
    assertTrue(Bounds.of(-1000, 1000).overlaps(Bounds.of(-2000, 2000)));
    assertTrue(Bounds.of(-2000, 2000).overlaps(Bounds.of(-1000, 1000)));
    assertTrue(Bounds.of(-1, 1).overlaps(Bounds.of(0, 0)));
    assertTrue(Bounds.of(0, 0).overlaps(Bounds.of(-1, 1)));
    assertTrue(Bounds.of(0, 100).overlaps(Bounds.of(100, 200)));
    assertTrue(Bounds.of(100, 200).overlaps(Bounds.of(0, 100)));

    assertFalse(Bounds.of(0, 10).overlaps(Bounds.of(10.01, 20)));
    assertFalse(Bounds.of(10.01, 20).overlaps(Bounds.of(0, 10)));
    assertFalse(Bounds.of(-99, 99).overlaps(Bounds.of(-100, -99.01)));
    assertFalse(Bounds.of(-1, 1).overlaps(Bounds.of(2, 3)));
    assertFalse(Bounds.of(2, 3).overlaps(Bounds.of(-1, 1)));
  }

  @Test
  public void allOverlap() {
    Bounds[] all = new Bounds[] {
      Bounds.of(  0, 100),
      Bounds.of(  5,  81),
      Bounds.of( 10,  90),
      Bounds.of( 16, 102),
      Bounds.of( 20,  80)
    };
    assertTrue(Bounds.allOverlap(all));

    all = new Bounds[] {
      Bounds.of(  0, 10),
      Bounds.of( 11, 20),
      Bounds.of(  7,  8),
      Bounds.of(  2,  4)
    };
    assertFalse(Bounds.allOverlap(all));
  }

  @Test
  public void allOverlapEmpty() {
    final Bounds[] all = new Bounds[] { };
    assertThrows(IllegalArgumentException.class,
        () -> Bounds.allOverlap(all));
  }

  @Test
  public void common() {
    Bounds[] all = new Bounds[] {
      Bounds.of(  0, 0),
      Bounds.of(  0, 0),
      Bounds.of(  0, 0)
    };
    Bounds bounds = Bounds.common(all);
    assertEquals(0, bounds.getMin(), 0.0);
    assertEquals(0, bounds.getMax(), 0.0);
  }

  @Test
  public void common2() {
    Bounds[] all = new Bounds[] {
      Bounds.of(  0, 100),
      Bounds.of(  5,  81),
      Bounds.of( 10,  90),
      Bounds.of( 16, 102),
      Bounds.of( 20,  80)
    };
    Bounds bounds = Bounds.common(all);
    assertEquals(20, bounds.getMin(), 0.0);
    assertEquals(80, bounds.getMax(), 0.0);
  }

  @Test
  public void mergeOverlapping() {
    Set<Bounds> set = new HashSet<>();
    set.add(Bounds.of(1, 2));
    set.add(Bounds.of(3, 5));
    set.add(Bounds.of(4, 6));

    List<Bounds> merged = Bounds.mergeOverlapping(set);
    assertEquals(1, merged.get(0).getMin(), 1e-10);
    assertEquals(2, merged.get(0).getMax(), 1e-10);
    assertEquals(3, merged.get(1).getMin(), 1e-10);
    assertEquals(6, merged.get(1).getMax(), 1e-10);

    set = new HashSet<>();
    set.add(Bounds.of(8.5, 11));
    set.add(Bounds.of(11.5, 12.25));
    set.add(Bounds.of(12, 13.5));
    set.add(Bounds.of(9.5, 11.5));
    set.add(Bounds.of(14, 15));

    merged = Bounds.mergeOverlapping(set);
    assertEquals(8.5, merged.get(0).getMin(), 1e-10);
    assertEquals(13.5, merged.get(0).getMax(), 1e-10);
    assertEquals(14, merged.get(1).getMin(), 1e-10);
    assertEquals(15, merged.get(1).getMax(), 1e-10);
  }

  @Test
  public void commonWithDisjointBounds() {
    Bounds[] all = new Bounds[] {
      Bounds.of(  0, 10),
      Bounds.of( 11, 20),
      Bounds.of(  7,  8),
      Bounds.of(  2,  4)
    };
    assertThrows(IllegalArgumentException.class, () -> Bounds.common(all));
  }

  @Test
  public void commonNullArg() {
    Bounds[] all = null;
    assertThrows(NullPointerException.class, () -> Bounds.common(all));
  }

  @Test 
  public void commonEmptyArg() {
    Bounds[] all = new Bounds[] {
    };
    assertThrows(IllegalArgumentException.class, () -> Bounds.common(all));
  }

  @Test
  public void formatWithNumberFormat() {
    NumberFormat nf = NumberFormat.getInstance();
    assertEquals("-684.25", Bounds.format(nf, -684.25));
    assertEquals("0", Bounds.format(nf, 0));
    assertEquals("42.01", Bounds.format(nf, 42.01));

    String expected = "min=0 max=255 range=255";
    String actual = Bounds.RGB_8_BIT.format(nf);
    assertEquals(expected, actual);
  }

  @Test
  public void formatWithStringFormat() {
    String format = "%.2f";
    
    double value = -684.25;
    String expected = "-684.25";
    String actual = Bounds.format(format, value);
    assertEquals(expected, actual);
    
    value = 0;
    expected = "0.00";
    actual = Bounds.format(format, value);
    assertEquals(expected, actual);

    value = 42.01;
    expected = "42.01";
    actual = Bounds.format(format, value);
    assertEquals(expected, actual);
    
    expected = "min=0.00000 max=255.000 range=255.000";
    actual = Bounds.RGB_8_BIT.format("%g");
    assertEquals(expected, actual);
  }

  @Test
  public void formatSpecialCases() {
    NumberFormat nf = NumberFormat.getInstance();
    assertEquals("NaN", Bounds.format(nf, Double.NaN));
    assertEquals("Infinity", Bounds.format(nf, Double.POSITIVE_INFINITY));
    assertEquals("Dval", Bounds.format(nf, Dval.DVAL_DOUBLE));
  }

  @Test
  public void formatWithNullNumberFormatArgShouldThrow() {
    NumberFormat nf = null;
    assertThrows(NullPointerException.class, 
        () -> Bounds.format(nf, Double.NaN));
  }
  
  @Test 
  public void dvalMinArg() {
    assertThrows(IllegalArgumentException.class, () -> Bounds.immutable(Dval.DVAL_DOUBLE, 10));
  }

  @Test 
  public void inifiniteMinArg() {
    assertThrows(IllegalArgumentException.class, () -> Bounds.immutable(Double.POSITIVE_INFINITY, 10));
  }

  @Test 
  public void NaN_MinArg() {
    assertThrows(IllegalArgumentException.class, () -> Bounds.immutable(Double.NaN, 10));
  }

  @Test 
  public void dvalMaxArg() {
    assertThrows(IllegalArgumentException.class, () -> Bounds.immutable(5, Dval.DVAL_DOUBLE));
  }

  @Test 
  public void inifiniteMaxArg() {
    assertThrows(IllegalArgumentException.class, () -> Bounds.immutable(5, Double.POSITIVE_INFINITY));
  }

  @Test 
  public void NaN_MaxArg() {
    assertThrows(IllegalArgumentException.class, () -> Bounds.immutable(5, Double.NaN));
  }

  @Test 
  public void minGreaterThanMaxArg() {
    assertThrows(IllegalArgumentException.class, () -> Bounds.immutable(5, 0));
  }

  @Test 
  public void createFromNullBoundsObject() {
    assertThrows(IllegalArgumentException.class, () -> ImmutableBounds.of(new NullBounds()));
  }
}
