package org.klojang.check.relation;

import org.klojang.check.CommonChecks;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static org.klojang.check.relation.Private.checkArg;

/**
 * Utility methods, mostly concerned with starting a composition of tests. Note that
 * while the predicates in the {@link CommonChecks} class are, in fact, already
 * either a {@link ComposablePredicate} or a {@link ComposableIntPredicate}, the
 * relational interfaces in this package obviously are not, and neither are
 * handwritten lambdas and method references. The utility methods defined here make
 * sure you can still start your composition with a {@link Relation}, lambda or
 * method reference.
 *
 * @author Ayco Holleman
 */
public class ComposeMethods {

  /**
   * Returns a {@code ComposablePredicate} that always evaluates to {@code true}.
   * This method can be used as the first of a series of AND-joined checks if there
   * is no need for an initial
   * {@linkplain CommonChecks#notNull() notNull() null check}.
   *
   * <blockquote><pre>{@code
   * Check.that(color).is(valid().and(equalTo(), noneOf(), GREEN, BLUE, YELLOW));
   * }</pre></blockquote>
   *
   * @param <T> the type of the value being tested (which is ignored by the
   *     returned {@code ComposablePredicate})
   * @return a {@code ComposablePredicate} that always evaluates to {@code true}
   */
  public static <T> ComposablePredicate<T> valid() {
    return x -> true;
  }

  /**
   * Returns a {@code ComposableIntPredicate} that always evaluates to {@code true}.
   * This method can be used as the first of a series of AND-joined checks.
   *
   * @return a {@code ComposableIntPredicate} that always evaluates to {@code true}
   */
  public static ComposableIntPredicate validInt() {
    return x -> true;
  }

  /**
   * Returns a {@code ComposablePredicate} that always evaluates to {@code true}.
   * This method can be used as the first of a series of OR-joined checks if there is
   * no need for an initial
   * {@linkplain CommonChecks#notNull() notNull() null check}.
   *
   * <blockquote><pre>{@code
   * Check.that(color).is(invalid().or(equalTo(), anyOf(), GREEN, BLUE, YELLOW));
   * }</pre></blockquote>
   *
   * @param <T> the type of the value being tested (which is ignored by the
   *     returned {@code ComposablePredicate})
   * @return a {@code ComposablePredicate} that always evaluates to {@code false}
   */
  public static <T> ComposablePredicate<T> invalid() {
    return x -> false;
  }

  /**
   * Returns a {@code ComposableIntPredicate} that always evaluates to {@code true}.
   * This method can be used as the first of a series of OR-joined checks.
   *
   * @return a {@code ComposableIntPredicate} that always evaluates to {@code false}
   */
  public static ComposableIntPredicate invalidInt() {
    return x -> false;
  }

  /**
   * Converts a {@code Predicate} to the equivalent {@code ComposablePredicate} so it
   * can take part in a composition. This method can be used to convert a predefined
   * {@code Predicate} constant from outside Klojang Check to a
   * {@code ComposablePredicate}, or to hard-cast a lambda or method reference to a
   * {@code ComposablePredicate}. This method is only needed if the
   * {@code Predicate}, lambda or method reference must be the first test of the
   * composition.
   *
   * <blockquote><pre>{@code
   * Check.that(sentence).is(validIf((String s) -> s.contains("to"))
   *    .orElse((String s) -> s.contains("be"));
   *    .orElse((String s) -> s.contains("or"));
   *    .orElse((String s) -> s.contains("not")));
   * }</pre></blockquote>
   *
   * @param test the {@code Predicate} to convert
   * @param <T> the type of the value being tested
   * @return the equivalent {@code ComposablePredicate}
   */
  public static <T> ComposablePredicate<T> validIf(Predicate<T> test) {
    checkArg(test);
    return test::test;
  }

  /**
   * Converts an {@code IntPredicate} to the equivalent
   * {@code ComposableIntPredicate}, so it can take part in a composition. This
   * method can be used to convert a predefined {@code IntPredicate} constant from
   * outside Klojang Check to a {@code ComposableIntPredicate}, or to hard-cast a
   * lambda or method reference. This method is only needed if the
   * {@code IntPredicate}, lambda or method reference must be the first test of the
   * composition.
   *
   * @param test the {@code IntPredicate} to convert
   * @return the equivalent {@code ComposableIntPredicate}
   */
  public static ComposableIntPredicate validIntIf(IntPredicate test) {
    checkArg(test);
    return test::test;
  }

  /**
   * Converts a {@code Relation} to a {@code ComposablePredicate}. More precisely:
   * this method returns a {@code ComposablePredicate} that evaluates to {@code true}
   * if the value being tested has the specified relation to the specified value.
   * This method is only needed if the {@code Relation} must be the first test of the
   * composition.
   *
   * <blockquote><pre>{@code
   * Check.that(Year.now()).is(validIf(GT(), Year.of(2000))
   *    .andAlso(LT(), Year.of(3000))
   *    .andAlso((Year y) -> y.isLeap());
   * }</pre></blockquote>
   *
   * @param relation the relationship test to execute
   * @param object the object of the relation
   * @param <S> the type of the subject of the relation
   * @param <O> the type of the object of the relation
   * @return a {@code ComposablePredicate} that evaluates to {@code true} if the
   *     value being tested has the specified relation to the specified value
   */
  public static <S, O> ComposablePredicate<S> validIf(Relation<S, O> relation,
      O object) {
    checkArg(relation);
    return s -> relation.exists(s, object);
  }

  /**
   * Converts an {@code IntObjRelation} to a {@code ComposableIntPredicate}. More
   * precisely: this method returns a {@code ComposableIntPredicate} that evaluates
   * to {@code true} if the value being tested has the specified relation to the
   * specified value. This method is only needed if the {@code IntObjRelation} must
   * be the first test of the composition.
   *
   * @param relation the relationship test to execute
   * @param object the object of the relation
   * @param <O> the type of the object of the relation
   * @return a {@code ComposableIntPredicate} that evaluates to {@code true} if the
   *     value being tested has the specified relation to the specified value
   */
  public static <O> ComposableIntPredicate validIntIf(IntObjRelation<O> relation,
      O object) {
    checkArg(relation);
    return s -> relation.exists(s, object);
  }

  /**
   * Converts an {@code Relation} to a {@code ComposableIntPredicate}. More
   * precisely: this method returns a {@code ComposableIntPredicate} that evaluates
   * to {@code true} if the value being tested has the specified relation to the
   * specified value. This method is only needed if the {@code IntRelation} must be
   * the first test of the composition.
   *
   * @param relation the relationship test to execute
   * @param object the object of the relation
   * @return a {@code ComposableIntPredicate} that evaluates to {@code true} if the
   *     value being tested has the specified relation to the specified value
   */
  public static ComposableIntPredicate validIntIf(IntRelation relation, int object) {
    checkArg(relation);
    return s -> relation.exists(s, object);
  }

}
