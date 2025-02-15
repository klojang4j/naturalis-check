package org.klojang.check.x.msg;

import org.klojang.check.types.ComposableIntPredicate;
import org.klojang.check.types.IntObjRelation;
import org.klojang.check.types.IntRelation;
import org.klojang.check.types.Relation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import static org.klojang.check.CommonChecks.*;
import static org.klojang.check.x.msg.MsgIntObjRelation.msgIndexInclusiveInto;
import static org.klojang.check.x.msg.MsgIntObjRelation.msgIndexOf;
import static org.klojang.check.x.msg.MsgIntPredicate.*;
import static org.klojang.check.x.msg.MsgIntRelation.*;
import static org.klojang.check.x.msg.MsgObjIntRelation.*;
import static org.klojang.check.x.msg.MsgPredicate.*;
import static org.klojang.check.x.msg.MsgRelation.*;

public final class CheckDefs {

  private static final Map<Predicate<?>, Function<MsgArgs, String>> predicateFormatters;
  private static final Map<IntPredicate, Function<MsgArgs, String>> intPredicateFormatters;
  private static final Map<Relation<?, ?>, Function<MsgArgs, String>> relationFormatters;
  private static final Map<IntRelation, Function<MsgArgs, String>> intRelationFormatters;
  private static final Map<IntObjRelation<?>, Function<MsgArgs, String>> intObjRelationFormatters;

  private static final Map<Object, String> names;

  private static Map<Predicate<?>, Function<MsgArgs, String>> predicateFormattersTemp = new HashMap<>();
  private static Map<ComposableIntPredicate, Function<MsgArgs, String>> intPredicateFormattersTemp = new HashMap<>();
  private static Map<Relation<?, ?>, Function<MsgArgs, String>> relationFormattersTemp = new HashMap<>();
  private static Map<IntRelation, Function<MsgArgs, String>> intRelationFormattersTemp = new HashMap<>();
  private static Map<IntObjRelation<?>, Function<MsgArgs, String>> intObjRelationFormattersTemp = new HashMap<>();

  private static Map<Object, String> namesTemp = new HashMap<>();

  static {
    setMetadata(NULL(), msgNull(), "NULL");
    setMetadata(notNull(), msgNotNull(), "notNull");
    setMetadata(yes(), msgYes(), "yes");
    setMetadata(no(), msgNo(), "no");
    setMetadata(empty(), msgEmpty(), "empty");
    setMetadata(emptyString(), msgEmpty(), "emptyString"); // recycle message
    setMetadata(notEmpty(), msgNotEmpty(), "notEmpty");
    setMetadata(deepNotNull(), msgDeepNotNull(), "deepNotNull");
    setMetadata(deepNotEmpty(), msgDeepNotEmpty(), "deepNotEmpty");
    setMetadata(blank(), msgBlank(), "blank");
    setMetadata(plainInt(), msgPlainInt(), "plainInt");
    setMetadata(plainShort(), msgPlainShort(), "plainShort");
    setMetadata(array(), msgArray(), "array");
    setMetadata(file(), msgFile(), "file");
    setMetadata(directory(), msgDirectory(), "directory");
    setMetadata(symlink(), msgSymlink(), "symlink");
    setMetadata(onFileSystem(), msgFileExists(), "fileExists");
    setMetadata(readable(), msgReadable(), "readable");
    setMetadata(writable(), msgWritable(), "writable");
    setMetadata(present(), msgPresent(), "present");
    setMetadata(available(), msgAvailable(), "available");
    setMetadata(even(), msgEven(), "even");
    setMetadata(odd(), msgOdd(), "odd");
    setMetadata(positive(), msgPositive(), "positive");
    setMetadata(negative(), msgNegative(), "negative");
    setMetadata(zero(), msgZero(), "zero");
    setMetadata(one(), msgOne(), "one");
    setMetadata(eq(), msgEq(), "eq");
    setMetadata(ne(), msgNe(), "ne");
    setMetadata(gt(), msgGt(), "gt");
    setMetadata(gte(), msgGte(), "gte");
    setMetadata(lt(), msgLt(), "lt");
    setMetadata(lte(), msgLte(), "lte");
    setMetadata(multipleOf(), msgMultipleOf(), "multipleOf");
    setMetadata(EQ(), msgEQ(), "EQ");
    setMetadata(equalTo(), msgEQ(), "equalTo"); // recycle message
    setMetadata(GT(), msgGT(), "GT");
    setMetadata(LT(), msgLT(), "LT");
    setMetadata(GTE(), msgGTE(), "GTE");
    setMetadata(LTE(), msgLTE(), "LTE");
    setMetadata(sameAs(), msgSameAs(), "sameAs");
    setMetadata(nullOr(), msgNullOr(), "nullOr");
    setMetadata(instanceOf(), msgInstanceOf(), "instanceOf");
    setMetadata(supertypeOf(), msgSupertypeOf(), "supertypeOf");
    setMetadata(subtypeOf(), msgSubtypeOf(), "subtypeOf");
    setMetadata(contains(), msgContains(), "contains");
    setMetadata(containsKey(), msgContainsKey(), "containsKey");
    setMetadata(containsValue(), msgContainsValue(), "containsValue");
    setMetadata(in(), msgIn(), "in");
    setMetadata(keyIn(), msgKeyIn(), "keyIn");
    setMetadata(valueIn(), msgValueIn(), "valueIn");
    setMetadata(inArray(), msgIn(), "inArray"); // Recycle message
    setMetadata(containsAll(), msgContainsAll(), "containsAll");
    setMetadata(containedIn(), msgContainedIn(), "containedIn");
    setMetadata(hasSubstring(), msgHasSubstring(), "hasSubstring");
    setMetadata(hasSubstringIC(), msgHasSubstringIC(), "hasSubstringIC");
    setMetadata(substringOf(), msgSubstringOf(), "substringOf");
    setMetadata(equalsIC(), msgEqualsIC(), "equalsIC");
    setMetadata(startsWith(), msgStartsWith(), "startsWith");
    setMetadata(startsWithIC(), msgStartsWithIC(), "startsWithIC");
    setMetadata(endsWith(), msgEndsWith(), "endsWith");
    setMetadata(endsWithIC(), msgEndsWithIC(), "endsWithIC");
    setMetadata(hasPattern(), msgHasPattern(), "hasPattern");
    setMetadata(containsPattern(), msgContainsPattern(), "containsPattern");
    setMetadata(matches(), msgHasPattern(), "matches"); // recycle message
    setMetadata(containsMatch(), msgContainsPattern(), "containsMatch"); // recycle message
    setMetadata(numerical(), msgNumerical(), "numerical");
    setMetadata(parsableAs(), msgParsableAs(), "parsableAs");
    setMetadata(indexOf(), msgIndexOf(), "indexOf");
    setMetadata(indexInclusiveOf(), msgIndexInclusiveInto(), "indexInclusiveInto");
    setMetadata(inIntArray(), msgIn(), "inIntArray"); // Recycle message

    predicateFormatters = Map.copyOf(predicateFormattersTemp);
    intPredicateFormatters = Map.copyOf(intPredicateFormattersTemp);
    relationFormatters = Map.copyOf(relationFormattersTemp);
    intRelationFormatters = Map.copyOf(intRelationFormattersTemp);
    intObjRelationFormatters = Map.copyOf(intObjRelationFormattersTemp);
    names = Map.copyOf(namesTemp);

    predicateFormattersTemp = null;
    intPredicateFormattersTemp = null;
    relationFormattersTemp = null;
    intRelationFormattersTemp = null;
    intObjRelationFormattersTemp = null;
    namesTemp = null;
  }

  public static String nameOf(Object check) {
    return names.get(check);
  }

  public static Function<MsgArgs, String> getPredicateFormatter(Predicate<?> predicate) {
    return predicateFormatters.get(predicate);
  }

  public static Function<MsgArgs, String> getIntPredicateFormatter(IntPredicate predicate) {
    return intPredicateFormatters.get(predicate);
  }

  public static Function<MsgArgs, String> getRelationFormatter(Relation<?, ?> relation) {
    return relationFormatters.get(relation);
  }

  public static Function<MsgArgs, String> getIntRelationFormatter(IntRelation relation) {
    return intRelationFormatters.get(relation);
  }

  public static Function<MsgArgs, String> getIntObjRelationFormatter(IntObjRelation<?> relation) {
    return intObjRelationFormatters.get(relation);
  }

  private static void setMetadata(Predicate<?> check, PrefabMsgFormatter formatter, String name) {
    predicateFormattersTemp.put(check, formatter);
    namesTemp.put(check, name);
  }

  private static void setMetadata(ComposableIntPredicate check, PrefabMsgFormatter formatter, String name) {
    intPredicateFormattersTemp.put(check, formatter);
    namesTemp.put(check, name);
  }

  private static void setMetadata(Relation<?, ?> check, PrefabMsgFormatter formatter, String name) {
    relationFormattersTemp.put(check, formatter);
    namesTemp.put(check, name);
  }

  private static void setMetadata(IntRelation check, PrefabMsgFormatter formatter, String name) {
    intRelationFormattersTemp.put(check, formatter);
    namesTemp.put(check, name);
  }

  private static void setMetadata(IntObjRelation<?> check, PrefabMsgFormatter formatter, String name) {
    intObjRelationFormattersTemp.put(check, formatter);
    namesTemp.put(check, name);
  }

}
