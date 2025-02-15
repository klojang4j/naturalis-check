package org.klojang.check;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.TestUtil.*;

public class CommonChecksTest {

  @Test(expected = IllegalArgumentException.class)
  public void NULL01() {
    Check.that(new Object()).is(NULL());
  }

  @Test(expected = IllegalArgumentException.class)
  public void NULL02() {
    Check.that("Hello, world").is(NULL());
    assertTrue(true);
  }

  @Test
  public void NULL03() {
    Check.that(null).is(NULL());
  }

  @Test(expected = IllegalArgumentException.class)
  public void NULL04() {
    // Yes, this one will work, too
    Check.that(Integer.valueOf(7)).is(NULL());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notNull01() {
    Check.that(null).is(notNull());
    assertTrue(true);
  }

  @Test
  public void notNull02() {
    Check.that(new Object()).is(notNull());
    assertTrue(true);
  }

  @Test
  public void notNull03() {
    Check.that(Optional.empty()).is(notNull());
    assertTrue(true);
  }

  @Test
  public void notNull05() {
    Check.that((Integer) 42).is(notNull());
    assertTrue(true);
  }

  @Test
  public void yes01() {
    Check.that(true).is(yes());
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void yes02() {
    Check.that(false).is(yes());
  }

  @Test
  public void no01() {
    Check.that(false).is(no());
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void no02() {
    Check.that(true).is(no());
  }

  @Test
  public void empty01() {
    Check.that(null).is(empty());
    Check.that("").is(empty());
    Check.that(Optional.empty()).is(empty());
    Check.that(Optional.of("")).is(empty());
    Check.that(List.of()).is(empty());
    Check.that(Set.of()).is(empty());
    Check.that(Map.of()).is(empty());
    Check.that(EMPTY_OBJECT_ARRAY).is(empty());
    Check.that(EMPTY_STRING_ARRAY).is(empty());
    Check.that(new char[0]).is(empty());
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void empty02() {
    Check.that("").isNot(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void empty03() {
    Check.that("foo").is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void empty04() {
    Check.that(List.of("")).is(empty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void empty05() {
    Check.that(new byte[] {07}).is(empty());
  }

  @Test
  public void notEmpty01() {
    Check.that(null).isNot(notEmpty());
    Check.that("").isNot(notEmpty());
    Check.that(Optional.empty()).isNot(notEmpty());
    Check.that(Optional.of("")).isNot(notEmpty());
    Check.that(List.of()).isNot(notEmpty());
    Check.that(Set.of()).isNot(notEmpty());
    Check.that(Map.of()).isNot(notEmpty());
    Check.that(EMPTY_OBJECT_ARRAY).isNot(notEmpty());
    Check.that(EMPTY_STRING_ARRAY).isNot(notEmpty());
    Check.that(new char[0]).isNot(notEmpty());
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty02() {
    Check.that(null).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty03() {
    Check.that("").is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty04() {
    Check.that(Optional.empty()).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty05() {
    Check.that(Optional.of("")).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty06() {
    Check.that(List.of()).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty07() {
    Check.that(Set.of()).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty08() {
    Check.that(Map.of()).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty09() {
    Check.that(EMPTY_OBJECT_ARRAY).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty10() {
    Check.that(EMPTY_STRING_ARRAY).is(notEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void notEmpty11() {
    Check.that(new char[0]).is(notEmpty());
  }

  @Test
  public void emptyString00() {
    Check.that((String) null).is(emptyString());
  }

  @Test
  public void emptyString01() {
    Check.that("").is(emptyString());
  }

  @Test
  public void emptyString02() {
    Check.that(" ").isNot(emptyString());
  }

  @Test
  public void deepNotNull01() {
    Check.that(Short.valueOf((short) 7)).is(deepNotNull());
    Check.that(LocalDateTime.now()).is(deepNotNull());
    Check.that(EMPTY_OBJECT_ARRAY).is(deepNotNull());
    Check.that(pack("FOO")).is(deepNotNull());
    Check.that(List.of()).is(deepNotNull());
    Check.that(List.of("FOO")).is(deepNotNull());
    Check.that(Set.of()).is(deepNotNull());
    Check.that(Set.of("BAR")).is(deepNotNull());
    Check.that(Map.of()).is(deepNotNull());
    Check.that(Map.of("John", "Smith")).is(deepNotNull());
    Check.that(Optional.of("BAR")).is(deepNotNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotNull02() {
    Check.that(null).is(deepNotNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotNull03() {
    Check.that(new String[] {null}).is(deepNotNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotNull04() {
    Check.that(new String[] {"FOO", null}).is(deepNotNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotNull05() {
    Check.that(Collections.singleton(null)).is(deepNotNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotNull06() {
    List<String> l = new ArrayList<>();
    l.add(null);
    l.add("BAR");
    l.add(null);
    Check.that(l).is(deepNotNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotNull07() {
    Map<String, Object> m = new HashMap<>();
    m.put(null, "XXX");
    Check.that(m).is(deepNotNull());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotNull08() {
    Map<String, Object> m = new HashMap<>();
    m.put("XXX", null);
    Check.that(m).is(deepNotNull());
  }

  @Test
  public void deepNotEmpty00() {
    Check.that((Integer) 7).is(deepNotEmpty());
    Check.that(LocalDateTime.now()).is(deepNotEmpty());
    Check.that("FOO").is(deepNotEmpty());
    Check.that(pack("FOO")).is(deepNotEmpty());
    Check.that(List.of("FOO")).is(deepNotEmpty());
    Check.that(Set.of("FOO")).is(deepNotEmpty());
    Check.that(Map.of("John", "Smith")).is(deepNotEmpty());
    Check.that(Optional.of(Map.of("weekend", List.of("saturday", "sunday")))).is(
        deepNotEmpty());
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty01() {
    Check.that(null).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty02() {
    Check.that("").is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty03() {
    Check.that(EMPTY_STRING_ARRAY).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty04() {
    Check.that(pack("a", null, "b")).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty05() {
    Check.that(pack("a", "", "b")).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty06() {
    Object[] arr0 = pack(EMPTY_STRING_ARRAY);
    assertSame(EMPTY_STRING_ARRAY, arr0);
    Object[] arr1 = pack("X", "Y", arr0);
    Object[] arr2 = pack("1", "2", arr1);
    Object[] arr3 = pack("a", "b", arr2);
    Check.that(arr3).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty07() {
    Check.that(List.of()).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty08() {
    Check.that(List.of(Set.of())).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty09() {
    Check.that(List.of(Set.of(Optional.empty()))).is(deepNotEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deepNotEmpty11() {
    Check.that(Map.of("", "Smith")).is(deepNotEmpty());
  }

  @Test
  public void blank00() {
    Check.that("foo").isNot(blank());
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void blank01() {
    Check.that("foo").is(blank());
  }

  @Test
  public void blank02() {
    String s = null;
    Check.that(s).is(blank());
    assertTrue(true);
  }

  @Test
  public void blank03() {
    Check.that("     ").is(blank());
    assertTrue(true);
  }

  @Test
  public void file00() throws IOException {
    File f = File.createTempFile("foo" + millis(), null);
    try {
      Check.that(f).is(file());
    } finally {
      f.delete();
    }
    Check.that(f).isNot(file());
  }

  @Test(expected = IllegalArgumentException.class)
  public void file01() {
    Check.that(new File("/-/-/*.bar/" + millis())).is(file());
  }

  @Test
  public void directory00() throws IOException {
    Path p = Files.createTempDirectory("foo" + millis());
    try {
      Check.that(p.toFile()).is(directory());
    } finally {
      Files.deleteIfExists(p);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void directory01() {
    Check.that(new File("/-/-/*.bar/" + millis())).is(directory());
  }

  @Test(expected = IllegalArgumentException.class)
  public void symlink01() {
    Check.that(new File("/-/-/*.bar/" + millis())).is(symlink());
  }

  @Test
  public void symlink00() throws IOException {
    Path p0 = Files.createTempFile("foo", "bar" + millis());
    Path p1 = Path.of(p0.getParent().toString(), "bozo" + millis());
    Path p2 = Files.createSymbolicLink(p1, p0);
    try {
      Check.that(p2.toFile()).is(symlink());
    } finally {
      Files.deleteIfExists(p0);
      Files.deleteIfExists(p1);
      Files.deleteIfExists(p2);
    }
  }

  @Test
  public void hasPattern00() {
    Check.that("abcd123").is(hasPattern(), Pattern.compile("^\\w{4}\\d{3}$"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void hasPattern01() {
    Check.that("abcd123").is(hasPattern(), Pattern.compile("^\\w{3}\\d{4}$"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void hasPattern02() {
    Check.that("abcd123").is(hasPattern(), Pattern.compile("foo"));
  }

  @Test
  public void containsPattern00() {
    Check.that("abcd123").is(containsPattern(), Pattern.compile("\\w{4}"));
  }

  @Test
  public void containsPattern01() {
    Check.that("abcd123").is(containsPattern(), Pattern.compile("^\\w{4}\\d{3}$"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void containsPattern02() {
    Check.that("abcd123").is(containsPattern(), Pattern.compile("^\\d{5,6}"));
  }

  @Test
  public void matching00() {
    Check.that("abcd123").is(matches(), "^\\w{4}\\d{3}$");
  }

  @Test(expected = IllegalArgumentException.class)
  public void matching01() {
    Check.that("abcd123").is(matches(), "\\d{4}");
  }

  @Test
  public void matchFor00() {
    Check.that("abcd123").is(containsMatch(), "\\d{3}");
  }

  @Test(expected = IllegalArgumentException.class)
  public void matchFor01() {
    Check.that("abcd123").is(containsMatch(), "\\d{4}");
  }

  @Test(expected = IllegalArgumentException.class)
  public void indexOf00() {
    int[] ints = new int[0];
    Check.that(0).is(indexOf(), ints);
  }

  @Test
  public void indexExclusiveOf00() {
    // we want to especially test here that indexExclusiveOf() is a
    // pure alias of indexOf(): it just returns indexOf(), and so should
    // inherit its error message
    int[] ints = new int[10];
    try {
      Check.that(42).is(indexExclusiveOf(), ints);
    } catch (IllegalArgumentException e) {
      assertEquals("argument must be >= 0 and < 10 (was 42)", e.getMessage());
    }
    // but also its name!
    try {
      Check.that(42).is(indexExclusiveOf(), ints, "${test} test failed");
    } catch (IllegalArgumentException e) {
      assertEquals("indexOf test failed", e.getMessage());
    }
  }

  @Test
  public void subtypeOf00() {
    Check.that(FileOutputStream.class).is(subtypeOf(), OutputStream.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void subtypeOf01() {
    Check.that(OutputStream.class).is(subtypeOf(), FileOutputStream.class);
  }

  @Test
  public void instanceOf00() {
    Check.that(new ByteArrayOutputStream()).is(instanceOf(), OutputStream.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void instanceOf01() {
    Check.that("foo").is(instanceOf(), FileOutputStream.class);
  }

  private static long millis() {
    return System.currentTimeMillis();
  }

}
