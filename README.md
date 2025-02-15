# Klojang Check

Ensuring that your program input, object state, or method arguments are valid before
execution is crucial for writing robust software. _Klojang Check_ simplifies precondition 
validation, making your code cleaner, more expressive, and reducing the need for 
excessive unit testing. It helps separate precondition validation from business logic in 
an elegant, concise way.

_Klojang Check_'s take on precondition validation is different from, for example,
Guava's [Preconditions](https://guava.dev/releases/19.0/api/docs/com/google/common/base/Preconditions.html)
class or
Apache's [Validate](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/Validate.html)
class, which serve a similar purpose. It provides a template for embedding your own 
checks. In addition, it comes with a large set of predefined checks on values of various 
types. These checks are associated with short, informative error messages, so you don't 
have to invent them yourselves.

Here is an example (explained below) of _Klojang Check_ in action:

```java
Check.that(numberOfChairs).is(positive()).is(lte(), 4).is(even());
```

Note that this statement requires no additional unit tests. On the other hand, if you 
would hand-code this check, it would look something like this:

```java
if(numberOfChairs <= 0 || numberOfChairs > 4 || numberOfChairs % 2 != 0) {
    throw new IllegalArgumentException("Invalid number of chairs");
}
```

Now you _will_ have to write six tedious unit tests to maintain your code coverage. 
Thus, using _Klojang Check_ saves you time and makes your code easier to maintain.

Of course, this shifts the burden of responsibility to <i>Klojang Check</i>.
_Klojang Check_ itself maintains very high levels of code coverage. The latest test 
coverage results can be found
**[here](https://klojang4j.github.io/klojang-check/21/coverage)**.

### Performance

_Klojang Check_ is lightweight: it provides a template for embedding checks without 
adding measurable overhead compared to hand-coding your checks. You can find 
**JMH benchmarks** for _Klojang Check_
**[here](https://github.com/klojang4j/klojang-check-jmh)**. 
Of course, if you need to check whether a value is in a `Map`, a lookup is unavoidable. 
_Klojang Check_ simply provides a cleaner way to express this:

```java
Check.that(value).is(keyIn(), map);  // Ensure 'value' is a key in 'map'
```

## Getting Started

To use _Klojang Check_, add the following dependency to your Maven POM file:

```xml
<dependency>
    <groupId>org.klojang</groupId>
    <artifactId>klojang-check</artifactId>
    <version>21.3.0</version>
</dependency>
```

or Gradle build script:

```
dependencies {
    implementation 'org.klojang:klojang-check:21.3.0'
}
```

The **Javadocs** for _Klojang Check_ can be found 
**[here](https://klojang4j.github.io/klojang-check/21/api)**.

## Usage

### Null checks

```java
// throws a NullPointerException if foo is null:
Check.notNull(foo);
```

### The CommonChecks class

The [CommonChecks](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html)
class is a grab bag of common checks on arguments, fields (a.k.a. state), variables, etc.

```java
import static org.klojang.check.CommonChecks.*;

Check.that(length).is(gte(), 0);
Check.that(divisor).isNot(zero());
Check.that(collection).isNot(empty());
Check.that(file).is(writable());
Check.that(firstName).is(substringOf(), fullName);
Check.that(i).is(indexOf(), list);
Check.that(employee.isManager()).is(yes());
```

The `Check.that(...)` method either returns an
[IntCheck](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/IntCheck.html)
or an
[ObjectCheck](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/ObjectCheck.html)
object, depending on whether the argument is an `int` or anything else. `IntCheck` gives
you access to int-specific checks like
[gte()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#gte()),
[zero()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#zero()),
and [indexOf()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#indexOf()).
With `ObjectCheck`, the argument passed to `Check.that(...)` determines which checks can
be executed. The
[writable()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#writable())
check, for example, can only be used if the argument is a
[File](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/File.html).
The
[empty()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#emptu())
check, on the other hand, can be applied to a wide variety of objects. These are also
valid checks:

```java
import static org.klojang.check.CommonChecks.empty;

Check.that(map).is(empty());
Check.that(array).isNot(empty());
Check.that(string).isNot(empty());
Check.that(optional).isNot(empty());
Check.that(file).is(empty());
Check.that(directory).is(empty());
```

### Custom Checks

Of course, you can also provide your own checks:

```java
Check.that(length).is(i -> i >= 0);
Check.that(file).is(File::canWrite);
Check.that(employee).is(Employee::isManager);
```

### Chaining Checks

The `is(...)` and `isNot(...)` methods return the same `IntCheck` or `ObjectCheck`
instance as the one that was created by `Check.that(...)`. This allows you to chain
multiple checks on the same value:

```java
Check.that(numberOfChairs).is(positive()).is(lte(), 4).is(even());
Check.notNull(file).is(writable());
```

Note that the checks in the `CommonChecks` class _only_ validate what they advertise to be
validating. Notably, **they will never do an implicit null check.** If the `file` argument
in the above example can possibly be null, you must start with an explicit null check.
(There are a few exceptions to this rule. For example, the
[empty()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#empty()),
[notEmpty()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#notEmpty()),
and
[deepNotEmpty()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#deepNotEmpty())
checks do include a null test. This will then be clearly documented in the 
[javadocs](https://klojang4j.github.io/klojang-check/21/api).)

Checks on different values can be also be chained:

```java
Check.that(numberOfChairs).is(positive()).and(numberOfTables).is(one());
```

Generally, though, we do not recommend this. Just write:

```java
Check.that(numberOfChairs).is(positive());
Check.that(numberOfTables).is(one());
```

This makes it easier to see which values are being validated.

### The ComposablePredicate and Relation Interfaces

With _Klojang Check_ you can execute two basic types of tests, specified through the
[ComposablePredicate](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/types/ComposablePredicate.html)
and
[Relation](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/types/Relation.html)
interfaces. (Both have int specializations like
[ComposableIntPredicate](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/types/ComposableIntPredicate.html).)
`ComposablePredicate` is an extension of 
[Predicate](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/Predicate.html)
that adds various `default` methods that assist in composing checks (combining multiple 
checks into one more fine-grained check). See 
[Composite Checks](#composite-checks). 
The `Relation` interface does not have a `java.util.function` equivalent. The functional 
method of `Relation` is called `exists()`. It takes two arguments and returns a `boolean`.

Take this example:

```java
Check.that(firstName).is(substringOf(), fullName);
```

The `substringOf()` method returns a `Relation<String, String>`. _Klojang Check_ will pass
`firstName` as the first argument to the `exists()` method and `fullName` as the second.
If the `exists()` call returns `true`, `firstName` has passed the check; otherwise it has
failed the check. To demystify things even further, this is how the `substringOf()` method
is implemented:

```java
public static Relation<String, String> substringOf() {
    return (arg1, arg2) -> arg2.contains(arg1);
}
```

### Tagging the Tested Value

<i>Klojang Check</i> generates a short, informative error message if the input value fails
a test.

```java
Check.notNull(null);
// error message: argument must not be null

Check.that(-42).is(gte(), 0);
// error message: argument must be >= 0 (was -42)
```

To improve the error messages, you can tag values with meaningful names:

```java
Check.notNull(foo, "foo");
// error message: foo must not be null

Check.that(length, "length").is(gte(), 0);
// error message: length must be >= 0 (was -42)
    
Check.that(numberOfChairs, "number of chairs").is(positive());
Check.that(numberOfTables, "number of tables").is(one());
```

This is especially useful when checking multiple method arguments as the
error message will make it immediately clear which argument failed to pass a test. The tag
could be the name of the method parameter, but it can really be anything you like. The
[Tag](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/Tag.html)
class contains string constants for some commonly used argument names:

```java
import static org.klojang.check.Tag.LENGTH;

Check.that(length, LENGTH).is(gte(), 0);
// error message: length must be >= 0 (was -42)
```

### Validating Argument Properties

With _Klojang Check_ you can test not just arguments, but also argument properties.
To do this, provide
a [Function](https://download.java.net/java/early_access/panama/docs/api/java.base/java/util/function/Function.html)
that extracts the value to be tested from the argument.

```java
Check.that(fullName).has(String::length, lte(), 100);
Check.that(person).has(Person::firstName, substringOf(), person.fullName());
```

The [CommonProperties](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonProperties.html)
class contains some extraction functions that might be helpful:

```java
import static org.klojang.check.CommonProperties.strlen;
import static org.klojang.check.CommonProperties.type;
import static org.klojang.check.CommonProperties.abs;

Check.that(fullName).has(strlen(), lte(), 100);
Check.that(employee).has(type(), instanceOf(), Manager.class);
Check.that(angle).has(abs(), lte(), 90);
```

As the last example illustrates, the word "property" needs to be taken in the broadest
sense here. These are really just functions that are passed the argument and return the
value to be tested.

### Providing a Custom Error Message

If you prefer, you can provide your own error message:

```java
Check.that(foo).is(notNull(), "null is not a valid value for foo");
Check.that(length).is(lte(), 100, "length must not exceed 100 characters");
```

The message may contain message arguments:

```java
Check.that(length).is(lte(), maxLen, "length must be <= ${0} (was ${1})", maxLen, length);
```

There are a five predefined message arguments that you can use in your error message:

- ${arg} &#8212; The value being tested.
- ${obj} &#8212; The value against which the input value is tested, if applicable. For
  example, in `Check.that(length).is(lte(), maxLen)`, the value of `length` is tested
  against
  the value of `maxLen`.
- ${type} &#8212; The type of the value being tested.
- ${tag} &#8212; The tag you gave to the value (defaults to "argument" if you did not
  provide a tag).
- ${test} &#8212; The name of the test. For example: "notNull" or "lte". If you
  executed a custom check (a lambda or method reference), this will be a pretty
  unintelligible string.

Thus, the previous check can also be written as follows:

```java
Check.that(length).is(lte(), maxLen, "length must be <= ${obj} (was ${arg})");
```

The above error message contains message arguments, but you don't need to provide them
yourself!

### Throwing a Custom Exception

By default, <i>Klojang Check</i> will throw an `IllegalArgumentException` if the input
value fails any of the checks following `Check.that(...)`. This can be customized in two
ways:

1. By using the `Check.on(...)` methods instead of the `Check.that(...)` methods. These
   methods allow you to provide a `Function` that takes a string (the error message) and
   returns the exception to be thrown.
2. By providing a `Supplier` that supplies the exception to be thrown.

Here is an example of each:

```java
// Error message "stale connection" is passed to the constructor of IllegalStateException:
Check.on(IllegalStateException::new, connection.isOpen()).is(yes(), "stale connection");
Check.that(connection.isOpen()).is(yes(), () -> new IllegalStateException("stale connection"));
```

The [CommonExceptions](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonExceptions.html)
class contains exception factories for some common exceptions:

```java
import static org.klojang.check.CommonExceptions.STATE;
import static org.klojang.check.CommonExceptions.illegalState;

Check.on(STATE, connection.isOpen()).is(yes(), "stale connection");
Check.that(connection.isOpen()).is(yes(), illegalState("stale connection"));
```

### Returning the Validated Value

Once a value has passed all checks, you can assign it to a field or variable using
[ObjectCheck::ok()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/ObjectCheck.html#ok())
or [IntCheck::ok()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/IntCheck.html#ok()):

```java
this.person = Check.notNull(person).has(Person::age, gte(), 18).ok();
```

You can optionally pass a transformation function to the `ok()` method:

```java
this.age = Check.notNull(person).ok(Person::age);
```

The transformation function can throw checked exceptions, so even at this late stage you
can still reject the value if needed. (See
[FallibleFunction](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/fallible/FallibleFunction.html)
and [FallibleToIntFunction](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/fallible/FallibleToIntFunction.html)
for more details.)

### Composite Checks

Sometimes, proper validation can only be done through a combination of checks.
For example, you may want to enforce one of these conditions:

- _**x must be either A or B**_
- _**either x must be A or y must be B**_:

In the first case you want to provide two alternative checks for the same value. In the
second case you want to validate two interrelated values.

_Klojang Check_ enables you to do this using the `default` methods on the
[ComposablePredicate](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/relation/ComposablePredicate.html)
and [ComposableIntPredicate](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/relation/ComposableIntPredicate.html)
interfaces. Here we use
the [or()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/types/ComposablePredicate.html#or(org.klojang.check.types.Relation,O))
method of `ComposablePredicate`:

```java
Check.that(collection).is(empty().or(contains(), "FOO"));
Check.that(collection1).is(empty().or(collection2, contains(), "FOO"));
```

What if you want the first check to be a
[Relation](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/types/Relation.html),
like [gte()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#gte())
or [instanceOf()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#instanceOf())?
Or if you want the first check to be a self-written lambda or a method reference?
In that case you can start your composition with one of four special checks:
[valid()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#valid()),
[validInt()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#validInt()),
[invalid()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#invalid()),
and [invalidInt()](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/CommonChecks.html#invalidInt()).
These are dummy checks specifically meant for composition and should not be used in
isolation. The first two checks always pass and can be used as the start of a series of
AND-joined checks. The last two always fail and can be used as the start of a series of
OR-joined checks.

```java
import static org.klojang.check.CommonChecks.valid;
import static org.klojang.check.CommonChecks.invalid;

Check.that(collection).is(valid().and(contains(), "FOO").and(contains(), "BAR"));
Check.that(collection).is(invalid().or(contains(), "FOO").or(contains(), "BAR"));
```

### Logical Quantifiers

When combining checks you can also employ
[logical quantifiers](https://klojang4j.github.io/klojang-check/21/api/org.klojang.check/org/klojang/check/types/Quantifier.html).
Using logical qualifiers you can provide multiple values against which to validate the
input value. They allow you to specify that the input value must pass a check for
**_all of_** the provided values, **_at least one of_** the provided values, or
**_none of_** the provided values. Here is an example:

```java
import static org.klojang.check.types.Quantifier.noneOf;

Check.that(collection).is(notEmpty().and(contains(), noneOf(), "FOO", "BAR", "BOZO"));
```

What if there is just one check you want to execute, but you still want to use a logical
quantifier? Again you can use the dummy checks mentioned above:

```java
import static org.klojang.check.types.Quantifier.allOf;

Check.that(collection).is(valid().and(contains(), allOf(), "FOO", "BAR", "BOZO"));
```

## About

<img alt="Naturalis logo" src="docs/logo-groen.png" style="float:left;width:5%;padding:0 12px 12px 0" />

<i>Klojang Check</i> is developed by [Naturalis](https://www.naturalis.nl/en), the
Dutch national biodiversity research institute. It maintains one of the largest
collections of zoological and botanical specimens in the world. Help fund biodiversity
research by
[donating to Naturalis](https://www.naturalis.nl/over-ons/steun-naturalis/doneren).





