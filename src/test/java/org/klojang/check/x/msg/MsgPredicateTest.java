package org.klojang.check.x.msg;

import org.junit.Test;
import org.klojang.check.Check;
import org.klojang.check.extra.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.CommonExceptions.INDEX;
import static org.klojang.check.TestUtil.*;

public class MsgPredicateTest {

  @Test(expected = IllegalArgumentException.class)
  public void lambdaAsPredicate00() {
    String foo = null;
    Check.that(foo, "foo").is(s -> s != null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void methodReferenceAsPredicate00() {
    String foo = null;
    Check.that(foo, "foo").is(Objects::nonNull);
  }

  @Test
  public void predicate01() {
    Check.that(List.of("foo")).isNot(empty());
    Check.that(List.of()).is(empty());
    Check.that(List.of()).is(deepNotNull());
    Check.that(List.of()).isNot(deepNotEmpty());
    Check.that(List.of(1, 2, 3)).isNot(empty());
    Check.that(List.of(1, 2, 3)).is(deepNotNull());
    Check.that(List.of(1, 2, 3)).is(deepNotEmpty());
    Check.that(Arrays.asList(String.class, null, null, null)).isNot(empty());
    Check.that(Arrays.asList(String.class, null, null, null))
        .isNot(deepNotNull());
    Check.that(Arrays.asList(String.class, null, null, null))
        .isNot(deepNotEmpty());
    Check.that("foo").isNot(blank());
    Check.that("   ").is(blank());
    Check.that(List.of().isEmpty()).is(yes());
    Check.that(List.of(1, 2, 3).isEmpty()).is(no());
    Check.that(true).isNot(no());
    Check.that(Boolean.TRUE).is(yes());
    Check.that(Boolean.FALSE).isNot(yes());
    Check.that("abc").isNot((String s) -> s.endsWith("xyz"));
    Check.that(new int[10]).is(array());
    Check.that(float[].class).is(array());
    Check.that("foo").isNot(array());
    Check.that(List.class).isNot(array());
  }

  @Test
  public void null00() {
    try {
      Check.on(INDEX, "???", "helium").is(NULL());
    } catch (IndexOutOfBoundsException e) {
      System.out.println(e.getMessage());
      assertEquals("helium must be null (was ???)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void null01() {
    try {
      Check.on(INDEX, null, "helium").isNot(NULL());
    } catch (IndexOutOfBoundsException e) {
      System.out.println(e.getMessage());
      assertEquals("helium must not be null", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notNull00() {
    try {
      Check.that(null, "plutonium").is(notNull());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("plutonium must not be null", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void notNull01() {
    try {
      Check.that("???", "plutonium").isNot(notNull());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("plutonium must be null (was ???)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void yes00() {
    try {
      Check.that(false, "oxygen").is(yes());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("oxygen must be true", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void yes01() {
    try {
      Check.that(true, "oxygen").isNot(yes());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("oxygen must not be true", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void no00() {
    try {
      Check.that(true, "carbon").is(no());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("carbon must be false", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void no01() {
    try {
      Check.that(false, "carbon").isNot(no());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("carbon must not be false", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void empty00() {
    try {
      Check.that(List.of(1F), "iron").is(empty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("iron must be null or empty (was List12[1] of [1.0])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void empty01() {
    try {
      Check.that("", "iron").isNot(empty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("iron must not be null or empty (was \"\")", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void deepNotNull00() {
    try {
      Check.that(pack("foo", null, "bar"), "gold").is(deepNotNull());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "gold must not be null or contain null values (was String[3] of [foo, null, bar])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void deepNotNull01() {
    try {
      Check.that(pack("foo", "bar"), "gold").isNot(deepNotNull());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "gold must be null or contain null values (was String[2] of [foo, bar])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void deepNotEmpty00() {
    try {
      Check.that(pack("foo", "", "bar"), "silver").is(deepNotEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "silver must not be empty or contain empty values (was String[3] of [foo, , bar])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void deepNotEmpty01() {
    try {
      Check.that(pack("foo", "bar"), "silver").isNot(deepNotEmpty());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals(
          "silver must be empty or contain empty values (was String[2] of [foo, bar])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void blank00() {
    try {
      Check.that("foo", "nitrogen").is(blank());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("nitrogen must be null or blank (was foo)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void blank01() {
    try {
      Check.that("  ", "nitrogen").isNot(blank());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("nitrogen must not be null or blank (was \"  \")",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void present_00() {
    try {
      Check.that(Optional.empty(), "calcium").is(present());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Optional calcium must not be empty",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void present_10() {
    try {
      Check.that(Optional.of("42"), "calcium").isNot(present());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Optional calcium must be empty (was Optional[42])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void available_00() {
    try {
      Check.that(Result.notAvailable(), "calcium").is(available());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("No result available for calcium",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void available_10() {
    try {
      Check.that(Result.of("42"), "calcium").isNot(available());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("Result calcium must not be available (was Result[42])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void array00() {
    try {
      Check.that("foo", "copper").is(array());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("copper must be an array (was foo)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void array01() {
    try {
      Check.that("foo bar", "copper").is(array());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("copper must be an array (was foo bar)", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void array10() {
    try {
      Check.that(new double[8], "copper").isNot(array());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("copper must not be an array (was double[8])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void arrayType00() {
    try {
      Check.that(String.class, "copper").is(array());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("copper must be an array type (was String)",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void arrayType10() {
    try {
      Check.that(double[].class, "copper").isNot(array());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("copper must not be an array type (was double[])",
          e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void file01() {
    File f = Path.of("bla", "bla", "bar.Φ").toFile();
    try {
      Check.that(f, "lithium").is(file());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("missing file: bla/bla/bar.Φ", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void fileExists00() {
    Path p = Path.of("bla", "foo", "bla", "bar");
    File file = p.toFile();
    try {
      Check.that(file, "xenon").is(onFileSystem());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("file not found: bla/foo/bla/bar", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void fileExists01() throws IOException {
    File f = File.createTempFile("foo234", null);
    try {
      Check.that(f, "xenon").isNot(onFileSystem());
    } finally {
      f.delete();
    }
  }

  @Test
  public void readable00() throws IOException {
    File f = new File("/bla/foo/bla/bar");
    try {
      Check.that(f, "krypton").is(readable());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("krypton must be readable (was " + f + ")", e.getMessage());
      return;
    }
    fail();
  }

  @Test(expected = IllegalArgumentException.class)
  public void readable01() throws IOException {
    File f = File.createTempFile("foo123", null);
    Check.that(f, "krypton").isNot(readable());
  }

  //@Test(expected = IllegalArgumentException.class)
  public void readable02() throws IOException {
    //    File f = IOMethods.createTempDir();
    //    Check.that(f, "krypton").isNot(readable());
  }

  @Test
  public void writable00() throws IOException {
    File f = new File("/bla/foo/bla/bar");
    try {
      Check.that(f, "argon").is(writable());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
      assertEquals("argon must be writable (was " + f + ")", e.getMessage());
      return;
    }
    fail();
  }

  //@Test(expected = IllegalArgumentException.class)
  public void writable01() throws IOException {
    //    File f = IOMethods.createTempFile();
    //    Check.that(f, "argon").isNot(writable());
  }

  //@Test(expected = IllegalArgumentException.class)
  public void writable02() throws IOException {
    //    File f = IOMethods.createTempDir();
    //    Check.that(f, "argon").isNot(writable());
  }

}
