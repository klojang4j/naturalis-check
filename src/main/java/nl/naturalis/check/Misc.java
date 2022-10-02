package nl.naturalis.check;

import nl.naturalis.base.ArrayType;
import nl.naturalis.base.Emptyable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import static java.lang.invoke.MethodHandles.arrayElementGetter;
import static java.lang.invoke.MethodHandles.arrayLength;
import static java.util.stream.Collectors.joining;
import static nl.naturalis.check.CommonChecks.notNull;

class Misc {

  static final String IMPLODE_SEPARATOR = ", ";

  static int getArrayLength(Object array) {
    try {
      return (int) arrayLength(array.getClass()).invoke(array);
    } catch (Throwable t) {
      throw new InvalidCheckException(t.toString());
    }
  }

  @SuppressWarnings("unchecked")
  static <T> T getArrayElement(Object array, int idx) throws Throwable {
    return (T) arrayElementGetter(array.getClass()).invoke(array, idx);
  }

  static String toShortString(Object obj) {
    return toShortString(obj, 50);
  }

  static String toShortString(Object obj, int maxWidth) {
    int maxElements = divUp(maxWidth, 8);
    int maxEntries = divUp(maxWidth, 16);
    return toShortString(obj, maxWidth, maxElements, maxEntries);
  }

  static int divUp(int value, int divideBy) {
    return (int) Math.ceil((double) value / (double) divideBy);
  }

  static String toShortString(Object obj,
      int maxWidth,
      int maxElements,
      int maxEntries) {
    if (obj == null) {
      return "null";
    } else if (obj.getClass() == String.class || obj instanceof Number) {
      // identify strings and numbers as quickly as possible
      return ellipsis0(obj.toString(), maxWidth);
    }
    String s;
    //@formatter:off
    if (obj instanceof Class<?> c) {
      s = simpleClassName(c);
    } else if (obj instanceof Collection<?> c) {
      String suffix = c.size() > maxElements ? ", ...]" : "]";
      s = '[' + implodeCollection(c, o -> toShortString(o, maxWidth, maxElements, maxEntries), ", ", 0, maxElements) + suffix;
    } else if (obj instanceof Map<?, ?> m) {
      String suffix = m.size() > maxElements ? ", ...}" : "}";
      s = '{' + implodeCollection(m.entrySet(), o -> toShortString(o, maxWidth, maxElements, maxEntries), ", ", 0, maxEntries) + suffix;
    } else if (obj instanceof Map.Entry<?, ?> e) {
      s = toShortString(e.getKey(), maxWidth, maxElements, maxEntries) + '=' + toShortString(e.getValue(), maxWidth, maxElements, maxEntries);
    } else if (obj instanceof int[] ints) {
      String x = ints.length > maxElements ? ", ...]" : "]";
      s = '['+ implodeInts((int[])obj, String::valueOf, ", ", 0, maxElements) + x;
    } else if (obj instanceof Object[] objs) {
      String suffix = objs.length > maxElements ? ", ...]" : "]";
      s = '[' + implodeArray(objs, o -> toShortString(o, maxWidth, maxElements, maxEntries), ", ", 0, maxElements) + suffix;
    } else if (obj.getClass().isArray()) {
      String suffix = getArrayLength(obj) > maxElements ? ", ...]" : "]";
      s = '[' + implodeAny(obj, String::valueOf, ", ", 0, maxElements) + suffix;
    } else {
      s = obj.toString();
    }
    //@formatter:on
    return ellipsis0(s, maxWidth);
  }

  static String ellipsis0(String str, int maxWidth) {
    maxWidth = Math.max(4, maxWidth);
    if (str.length() > maxWidth) {
      return str.substring(0, maxWidth - 3) + "...";
    }
    return str;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  static <T> String implodeCollection(
      Collection<T> collection,
      Function<T, String> stringifier,
      String separator,
      int from,
      int to) {
    int sz = collection.size();
    int x = to == -1 ? sz : Math.min(to, sz);
    if (from == 0) {
      Stream<T> stream = x == sz
          ? collection.stream()
          : collection.stream().limit(x);
      return stream.map(stringifier).collect(joining(separator));
    } else if (collection instanceof List) {
      List<T> sublist = ((List<T>) collection).subList(from, x);
      return sublist.stream().map(stringifier).collect(joining(separator));
    }
    Stream stream = Arrays.stream(collection.toArray(), from, x);
    return (String) stream.map(stringifier).collect(joining(separator));
  }

  static <T> String implodeArray(T[] array,
      Function<T, String> stringifier,
      String separator,
      int from,
      int to) {
    int x = to == -1 ? array.length : Math.min(to, array.length);
    return Arrays.stream(array, from, x)
        .map(stringifier)
        .collect(joining(separator));
  }

  static String implodeInts(int[] array,
      IntFunction<String> stringifier,
      String separator,
      int from,
      int to) {
    int x = to == -1 ? array.length : Math.min(to, array.length);
    return Arrays.stream(array, from, x)
        .mapToObj(stringifier)
        .collect(joining(separator));
  }

  static String implodeAny(Object array,
      Function<Object, String> stringifier,
      String separator,
      int from,
      int to) {
    int len = getArrayLength(array);
    int x = to == -1 ? len : Math.min(to, len);
    StringBuilder sb = new StringBuilder();
    try {
      for (int i = 0; i < x; ++i) {
        if (i != 0) {
          sb.append(separator);
        }
        Object o = getArrayElement(array, i);
        sb.append(stringifier.apply(o));
      }
    } catch (Throwable t) {
      throw new InvalidCheckException(t.toString());
    }
    return sb.toString();
  }

  static <T> boolean isEmpty(T obj) {
    if (obj == null) {
      return true;
    } else if (obj instanceof CharSequence cs) {
      return cs.isEmpty();
    } else if (obj instanceof Collection<?> c) {
      return c.isEmpty();
    } else if (obj instanceof Map<?, ?> m) {
      return m.isEmpty();
    } else if (obj.getClass().isArray()) {
      return getArrayLength(obj) != 0;
    } else if (obj instanceof Emptyable e) {
      return e.isEmpty();
    } else if (obj instanceof Optional<?> o) {
      return o.isPresent() && !isEmpty(o.get());
    }
    return false;
  }

  static boolean isDeepNotEmpty(Object arg) {
    return arg != null
        && (!(arg instanceof CharSequence cs) || cs.length() > 0)
        && (!(arg instanceof Collection<?> c) || dne(c))
        && (!(arg instanceof Map<?, ?> m) || dne(m))
        && (!(arg instanceof Object[] x) || dne(x))
        && (arg.getClass().isArray() || getArrayLength(arg) != 0)
        && (!(arg instanceof Optional<?> o) || dne(o))
        && (!(arg instanceof Emptyable e) || e.isDeepNotEmpty());
  }

  private static boolean dne(Collection<?> coll) {
    if (coll.isEmpty()) {
      return false;
    }
    return coll.stream().allMatch(Misc::isDeepNotEmpty);
  }

  private static boolean dne(Map<?, ?> map) {
    if (map.isEmpty()) {
      return false;
    }
    return map.entrySet().stream().allMatch(Misc::entryDeepNotEmpty);
  }

  private static boolean entryDeepNotEmpty(Object obj) {
    var e = (Map.Entry<?, ?>) obj;
    return isDeepNotEmpty(e.getKey()) && isDeepNotEmpty(e.getValue());
  }

  private static boolean dne(Object[] arr) {
    if (arr.length == 0) {
      return false;
    }
    return Arrays.stream(arr).allMatch(Misc::isDeepNotEmpty);
  }

  private static boolean dne(Optional<?> opt) {
    return opt.isPresent() && isDeepNotEmpty(opt.get());
  }

  /**
   * Verifies that the argument is not null and, if it is array, {@link Collection}
   * or {@link Map}, does not contain any null values. It may still be an empty
   * array, {@code Collection} or {@code Map}, however. For maps, both keys and
   * values are tested for {@code null}.
   *
   * @param arg the object to be tested
   * @return whether it is not null and does not contain any null values
   */
  static boolean isDeepNotNull(Object arg) {
    if (arg == null) {
      return false;
    } else if (arg instanceof Object[] x) {
      return Arrays.stream(x).allMatch(notNull());
    } else if (arg instanceof Collection<?> c) {
      if (isNullRepellent(c)) {
        return true;
      }
      return c.stream().allMatch(notNull());
    } else if (arg instanceof Map<?, ?> m) {
      return m.entrySet().stream()
          .allMatch(e -> e.getKey() != null && e.getValue() != null);
    }
    return true;
  }

  private static final Set<Class<?>> NULL_REPELLERS =
      // Actually, List.of(1) and List.of(1, 2) currently have the same type, but
      // better safe than sorry. They will anyhow be de-duplicated when entering
      // the HashSet
      Set.copyOf(new HashSet<>(
          Arrays.asList(
              Collections.emptyList().getClass(),
              Collections.emptySet().getClass(),
              List.of().getClass(),
              List.of(1).getClass(),
              List.of(1, 2).getClass(),
              List.of(1, 2, 3).getClass(),
              Set.of().getClass(),
              Set.of(1).getClass(),
              Set.of(1, 2).getClass(),
              Set.of(1, 2, 3).getClass())));

  static boolean isNullRepellent(Collection<?> c) {
    return NULL_REPELLERS.contains(c.getClass());
  }

  static String simpleClassName(Object obj) {
    Objects.requireNonNull(obj);
    return simpleClassName(obj.getClass());
  }

  static String simpleClassName(Class<?> clazz) {
    if (clazz.isArray()) {
      return ArrayType.forClass(clazz).toString();
    }
    return clazz.getSimpleName();
  }

  static String className(Class<?> clazz) {
    if (clazz.isArray()) {
      return ArrayType.forClass(clazz).arrayClassName();
    }
    return clazz.getName();
  }

  static String describe(Object obj) {
    if (obj == null) {
      return "null";
    } else if (obj.getClass() == Class.class) {
      return ((Class<?>) obj).getSimpleName() + ".class";
    } else if (obj instanceof Collection<?> c) {
      return c.getClass().getSimpleName() + '[' + c.size() + ']';
    } else if (obj instanceof Map<?, ?> m) {
      return m.getClass().getSimpleName() + '[' + m.size() + ']';
    } else if (obj.getClass().isArray()) {
      return ArrayType.describe(obj);
    }
    return obj.getClass().getSimpleName();
  }

}
