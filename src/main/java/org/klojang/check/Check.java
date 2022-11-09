package org.klojang.check;

import org.klojang.check.x.msg.CustomMsgFormatter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.klojang.check.x.msg.CustomMsgFormatter.formatSimple;

/**
 * The central class of this Java module. All checks start out here. The
 * {@code Check} class provides static factory methods for {@link IntCheck} and
 * {@link ObjectCheck} instances, which do the actual orchestration of the checks to
 * be executed. The {@code Check} class contains a few validation methods itself,
 * like {@link #fromTo(int, int, int) Check.fromTo()} and
 * {@link #offsetLength(int, int, int) Check.offsetLength()}. These stand somewhat
 * apart from the rest of the Klojang Check. They are included for convenience and
 * for optimal performance.
 *
 * <p>See the <a href="../../../module-summary.html">module summary</a> for a
 * detailed description of Defensive Programming using Klojang Check.
 *
 * @author Ayco Holleman
 */
public final class Check {

  private Check() {
    throw new UnsupportedOperationException();
  }

  private static NullPointerException argumentMustNotBeNull() {
    return new NullPointerException("argument must not be null");
  }

  private static NullPointerException argumentMustNotBeNull(String tag) {
    return new NullPointerException(tag + " must not be null");
  }

  private static final Function<String, IllegalArgumentException> DEF_EXC_FACTORY =
      CommonExceptions.ARGUMENT;

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for testing
   * {@code int} values.
   *
   * @param value the value to be validated
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static IntCheck<IllegalArgumentException> that(int value) {
    return new IntCheck<>(value, null, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@link ObjectCheck} instance suitable for
   * validating values of type {@code <T>}.
   *
   * @param <T> the type of the value to be validated
   * @param value the value to be validated
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> that(T value) {
    return new ObjectCheck<>(value, null, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for
   * validating {@code int} values.
   *
   * @param value the value to be validated
   * @param tag a descriptive name for the value (in case the value is a method
   *     argument probably something close to the parameter name)
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static IntCheck<IllegalArgumentException> that(int value, String tag) {
    return new IntCheck<>(value, tag, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@link ObjectCheck} instance suitable for
   * validating values of type {@code <T>}.
   *
   * @param <T> the type of the value to be validated
   * @param value the value to be validated
   * @param tag a descriptive name for the value (in case the value is a method
   *     argument probably something close to the parameter name)
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> that(T value,
      String tag) {
    return new ObjectCheck<>(value, tag, DEF_EXC_FACTORY);
  }

  /**
   * Static factory method. Returns an {@link ObjectCheck} instance suitable for
   * validating values of type {@code <T>}. The argument will have already passed the
   * {@linkplain CommonChecks#notNull() null test}. Note that an
   * {@code IllegalArgumentException} thrown the specified value fails any of the
   * subsequently specified checks, but if the argument was {@code null}, a
   * {@code NullPointerException} will be thrown.
   *
   * @param <T> the type of the value to be validated
   * @param value the value to be validated
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   * @throws NullPointerException if the argument is {@code null}
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> notNull(T value)
      throws NullPointerException {
    if (value != null) {
      return new ObjectCheck<>(value, null, DEF_EXC_FACTORY);
    }
    throw argumentMustNotBeNull();
  }

  /**
   * Static factory method. Returns an {@link ObjectCheck} instance suitable for
   * validating values of type {@code <T>}. The argument will have already passed the
   * {@linkplain CommonChecks#notNull() null test}. Note that an
   * {@code IllegalArgumentException} thrown the specified value fails any of the
   * subsequently specified checks, but if the argument was {@code null}, a
   * {@code NullPointerException} will be thrown.
   *
   * @param <T> the type of the value to be validated
   * @param value the value to be validated
   * @param tag a descriptive name for the value (in case the value is a method
   *     argument probably something close to the parameter name)
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   * @throws NullPointerException if the argument is {@code null}
   */
  public static <T> ObjectCheck<T, IllegalArgumentException> notNull(T value,
      String tag)
      throws IllegalArgumentException {
    if (value != null) {
      return new ObjectCheck<>(value, tag, DEF_EXC_FACTORY);
    }
    throw argumentMustNotBeNull(tag);
  }

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for
   * validating {@code int} values. If the value fails any of the tests passed to the
   * {@code IntCheck} instance, the exception produced by the provided exception
   * factory is thrown.
   *
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param value the value to be validated
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static <X extends Exception> IntCheck<X> on(Function<String, X> excFactory,
      int value) {
    return new IntCheck<>(value, null, excFactory);
  }

  /**
   * Static factory method. Returns an {@code ObjectCheck} instance suitable for
   * validating values of type {@code <T>}. If the value fails any of the tests
   * passed to the {@code ObjectCheck} instance, the exception produced by the
   * provided exception factory is thrown.
   *
   * @param <T> the type of the value to be validated
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param value the value to be validated
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T, X extends Exception> ObjectCheck<T, X> on(
      Function<String, X> excFactory, T value) {
    return new ObjectCheck<>(value, null, excFactory);
  }

  /**
   * Static factory method. Returns an {@link IntCheck} instance suitable for
   * validating {@code int} values. If the value fails any of the tests passed to the
   * {@code IntCheck} instance, the exception produced by the provided exception
   * factory is thrown.
   *
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param value the value to be validated
   * @param tag a descriptive name for the value (in case the value is a method
   *     argument probably something close to the parameter name)
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @return an {@code IntCheck} instance suitable for testing {@code int} values
   */
  public static <X extends Exception> IntCheck<X> on(
      Function<String, X> excFactory, int value, String tag) {
    return new IntCheck<>(value, tag, excFactory);
  }

  /**
   * Static factory method. Returns a new {@code Check} instance suitable for testing
   * the provided argument.
   *
   * @param <T> the type of the value to be validated
   * @param <X> the type of {@code Exception} thrown if the value fails to pass a
   *     test
   * @param excFactory a function that will produce the exception if the value
   *     fails to pass a test. The function will be pass the exception message and
   *     must return the exception to be thrown
   * @param value the value to be validated
   * @param tag a descriptive name for the value (in case the value is a method
   *     argument probably something close to the parameter name)
   * @return an {@code ObjectCheck} instance suitable for validating values of type
   *     {@code <T>}.
   */
  public static <T, X extends Exception> ObjectCheck<T, X> on(
      Function<String, X> excFactory, T value, String tag) {
    return new ObjectCheck<>(value, tag, excFactory);
  }

  /**
   * All-in-one check for the specified array, offset and length. Verifies that
   * {@code offset} and {@code length} are valid, given the length of the specified
   * array. More precisely:
   *
   * <ol>
   *   <li>throws an {@code IllegalArgumentException} if the array is {@code null}.
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code offset} or {@code length} is less than zero
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code offset+length} is greater than the length of the array
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param array the array
   * @param offset the offset within the array
   * @param length the length of the segment
   * @see #offsetLength(int, int, int)
   * @see java.io.OutputStream#write(byte[], int, int)
   * @see java.io.InputStream#read(byte[], int, int)
   */
  public static void offsetLength(byte[] array, int offset, int length) {
    if (array == null) {
      throw argumentMustNotBeNull("array");
    }
    if ((offset | length) < 0 || offset + length > array.length) {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * All-in-one check for the provided size, offset and length. Verifies that
   * {@code offset} and {@code length} are valid, given tan array or array-like
   * object with the specified size.
   *
   * <ol>
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code size} or {@code offset} or {@code length} is less than zero
   *   <li>throws an {@code IndexOutOfBoundsException} if {@code offset+length} is greater than the length of the array
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param size the length/size of the array or array-like object
   * @param offset the offset
   * @param length the length of the segment
   */
  public static void offsetLength(int size, int offset, int length) {
    if ((size | offset | length) < 0 || size < offset + length) {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * An all-in-one check for the provided list, from-index and to-index. Verifies
   * that the from-index and to-index valid list indices. More precisely:
   *
   * <ol>
   *   <li>Throws a {@code NullPointerException} if the list is {@code null}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * <i>NB The {@code fromTo()} and {@code offsetLength()} checks stand somewhat
   * apart from the rest of the check framework. They happen through "ordinary"
   * static utility method and they test multiple things at once. They are included
   * for convenience and speed.</i>
   *
   * @param list the list
   * @param fromIndex the start index of the sublist
   * @param toIndex the end index of the sublist
   * @return the {@code size} of the sublist
   * @see #fromTo(int, int, int)
   * @see List#subList(int, int)
   */
  public static int fromTo(List<?> list, int fromIndex, int toIndex) {
    if (list == null) {
      throw argumentMustNotBeNull("list");
    }
    if ((fromIndex | toIndex) < 0 || toIndex < fromIndex || list.size() < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * An all-in-one check for the provided array, from-index and to-index. Verifies
   * that the from-index and to-index valid array indices. More precisely:
   *
   * <ol>
   *   <li>Throws a {@code NullPointerException} if the array is {@code null}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * @param array the array
   * @param fromIndex the start index of the array segment
   * @param toIndex the end index of the array segment
   * @param <T> the type of the array elements
   * @return the {@code length} of the array segment
   * @see #fromTo(int, int, int)
   * @see Arrays#copyOfRange(Object[], int, int)
   */
  public static <T> int fromTo(T[] array, int fromIndex, int toIndex) {
    if (array == null) {
      throw argumentMustNotBeNull("array");
    }
    if ((fromIndex | toIndex) < 0 || toIndex < fromIndex || array.length < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * An all-in-one check for the provided string, from-index and to-index. Verifies
   * that the from-index and to-index valid string indices. More precisely:
   *
   * <ol>
   *   <li>Throws a {@code NullPointerException} if the string is {@code null}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * @param string the string
   * @param fromIndex the start index of the substring
   * @param toIndex the end index of the substring
   * @return the {@code length} of the substring
   * @see #fromTo(int, int, int)
   * @see String#substring(int, int)
   */
  public static int fromTo(String string, int fromIndex, int toIndex) {
    if (string == null) {
      throw argumentMustNotBeNull("string");
    }
    if ((fromIndex | toIndex) < 0
        || toIndex < fromIndex
        || string.length() < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * An all-in-one check for the provided size/length (supposedly of an array or
   * array-like object), from-index and to-index. Verifies that the from-index and
   * to-index valid string indices, given the specified size/length. More precisely:
   *
   * <ol>
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code size} or {@code fromIndex} or {@code toIndex} is less than zero
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is less than {@code fromIndex}
   *   <li>Throws an {@code IndexOutOfBoundsException} if {@code toIndex} is greater than the size of the list
   * </ol>
   *
   * <i>NB The {@code fromTo} and {@code offsetLength} checks stand somewhat apart from the
   * rest of the check framework. They happen through "ordinary" static utility method and they
   * test multiple things at once. They are included for convenience and speed.</i>
   *
   * @param size the size (or length) of the array, string, list, etc.
   * @param fromIndex the start index of the segment
   * @param toIndex the end index of the segment
   * @return the {@code length} of the segment
   */
  public static int fromTo(int size, int fromIndex, int toIndex) {
    if ((size | fromIndex | toIndex) < 0 || toIndex < fromIndex || size < toIndex) {
      throw new IndexOutOfBoundsException();
    }
    return toIndex - fromIndex;
  }

  /**
   * Throws an {@code IllegalArgumentException} with the specified message and
   * message arguments. The method is still declared to return a value of type
   * {@code <T>} so it can be used as the expression for a {@code return} statement.
   *
   * @param <T> the desired type of the return value
   * @param message the message (pattern)
   * @param msgArgs the message arguments. Note that these are not
   *     {@code printf}-like message arguments. The first message argument within the
   *     message pattern would be {@code ${0}}; the second would be {@code ${1}},
   *     etc. For more information, see <a
   *     href="../../../module-summary.html#custom-error-messages">Custom Error
   *     Messages</a>.
   * @return nothing, but allows {@code fail()} to be used as the expression in a
   *     {@code return} statement
   * @throws IllegalArgumentException always
   */
  public static <T> T fail(String message, Object... msgArgs)
      throws IllegalArgumentException {
    return failOn(DEF_EXC_FACTORY, message, msgArgs);
  }

  /**
   * Throw the exception supplied by the specified {@code Supplier}. The method is
   * still declared to return a value of type {@code <T>} so it can be used as the
   * expression for a {@code return} statement.
   *
   * @param excFactory the supplier of the exception
   * @param <T> the desired type of the return value
   * @param <X> the type of the exception
   * @return nothing, but allows {@code fail()} to be used as the expression in a
   *     {@code return} statement
   * @throws X always
   */
  public static <T, X extends Throwable> T fail(Supplier<X> excFactory) throws X {
    throw excFactory.get();
  }

  /**
   * Throws an exception produced by the specified exception factory with the
   * specified message and message arguments.
   *
   * @param <T> the type of the object that would have been returned if it had
   *     passed the checks
   * @param <X> the type of the exception
   * @param excFactory a function that takes a {@code String} (the exception
   *     message) and produces an {@code Exception}.
   * @param message the message
   * @param msgArgs the message arguments. Note that these are not
   *     {@code printf}-like message arguments. The first message argument within the
   *     message pattern would be {@code ${0}}; the second would be {@code ${1}},
   *     etc. For more information, see <a
   *     href="../../../module-summary.html#custom-error-messages">Custom Error
   *     Messages</a>.
   * @return nothing, but allows {@code fail} to be used as the expression in a
   *     {@code return} statement
   * @throws X always
   */
  public static <T, X extends Throwable> T failOn(
      Function<String, X> excFactory, String message, Object... msgArgs) throws X {
    if (msgArgs == null || message == null) {
      throw excFactory.apply(message);
    }
    throw excFactory.apply(formatSimple(message, msgArgs));
  }

}
