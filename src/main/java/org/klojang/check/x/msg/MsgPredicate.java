package org.klojang.check.x.msg;

import org.klojang.check.x.ArrayInfo;

import static org.klojang.check.x.msg.MsgUtil.className;
import static org.klojang.check.x.msg.MsgUtil.toStr;

final class MsgPredicate {

  private MsgPredicate() {}

  static PrefabMsgFormatter msgNull() {
    return x -> x.negated()
        ? x.name() + " must not be null"
        : x.name() + " must be null (was " + toStr(x.arg()) + ')';
  }

  static PrefabMsgFormatter msgNotNull() {
    return x -> x.negated()
        ? x.name() + " must be null (was " + toStr(x.arg()) + ')'
        : x.name() + " must not be null";
  }

  static PrefabMsgFormatter msgYes() {
    return x -> x.negated()
        ? x.name() + " must not be true"
        : x.name() + " must be true";
  }

  static PrefabMsgFormatter msgNo() {
    return x -> x.negated()
        ? x.name() + " must not be false"
        : x.name() + " must be false";
  }

  static PrefabMsgFormatter msgEmpty() {
    return x -> x.negated()
        ? x.name() + " must not be null or empty (was " + toStr(x.arg()) + ')'
        : x.name() + " must be null or empty (was " + toStr(x.arg()) + ')';
  }

  static PrefabMsgFormatter msgNotEmpty() {
    return x -> x.negated()
        ? x.name() + " must be null or empty (was " + toStr(x.arg()) + ')'
        : x.name() + " must not be null or empty (was " + toStr(x.arg()) + ')';
  }

  static PrefabMsgFormatter msgDeepNotNull() {
    //@formatter:off
    return x ->  x.negated()
            ? x.name() + " must be null or contain null values (was " + toStr(x.arg()) + ')'
            : x.name() + " must not be null or contain null values (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgDeepNotEmpty() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must be empty or contain empty values (was " + toStr(x.arg()) + ')'
        : x.name() + " must not be empty or contain empty values (was " + toStr(x.arg()) + ')';
    //@formatter:off
  }

  static PrefabMsgFormatter msgBlank() {
    return x -> x.negated()
        ? x.name() + " must not be null or blank (was " + toStr(x.arg()) + ')'
        : x.name() + " must be null or blank (was " + toStr(x.arg()) + ')';
  }


  static PrefabMsgFormatter msgPlainInt() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be a plain integer (was " + toStr(x.arg()) + ')' // BS begets BS
        : x.name() + " must be a plain integer: no +/- sign, no leading zeros (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgPlainShort() {
    //@formatter:off
    return x -> x.negated()
        ? x.name() + " must not be a plain short (was " + toStr(x.arg()) + ')' // BS begets BS
        : x.name() + " must be a plain 16-bit integer: no +/- sign, no leading zeros (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgArray() {
    //@formatter:off
    return x -> x.arg() instanceof Class<?> c
        ? x.negated()
            ? x.name() + " must not be an array type (was " + ArrayInfo.create(c) + ')'
            : x.name() + " must be an array type (was " + className(c) + ')'
        : x.negated()
            ? x.name() + " must not be an array (was " + ArrayInfo.describe(x.arg()) + ')'
            : x.name() + " must be an array (was " + toStr(x.arg()) + ')';
    //@formatter:on
  }

  static PrefabMsgFormatter msgFile() {
    return x -> x.negated()
        ? x.name() + " must not be an existing file (was " + x.arg() + ')'
        : "missing file: " + x.arg();
  }

  static PrefabMsgFormatter msgDirectory() {
    return x -> x.negated()
        ? x.name() + " must not be an existing directory (was " + x.arg() + ')'
        : "missing directory: " + x.arg();
  }

  static PrefabMsgFormatter msgSymlink() {
    return x -> x.negated()
        ? x.name() + " must not be a symbolic link (was " + x.arg() + ')'
        : x.name() + " must be a symbolic link (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgFileExists() {
    return x -> x.negated()
        ? x.name() + " must not exist (was " + x.arg() + ')'
        : "file not found: " + x.arg();
  }

  static PrefabMsgFormatter msgReadable() {
    return x ->
        x.negated()
            ? x.name() + " must not be readable (was " + x.arg() + ')'
            : x.name() + " must be readable (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgWritable() {
    return x ->
        x.negated()
            ? x.name() + " must not be writable (was " + x.arg() + ')'
            : x.name() + " must be writable (was " + x.arg() + ')';
  }

  static PrefabMsgFormatter msgPresent() {
    //@formatter:off
    return x ->
        x.negated()
            ? "Optional " + x.name() + " must be empty (was " + toStr(x.arg()) + ')'
            : "Optional " + x.name() + " must not be empty";
    //@formatter:off
  }

  static PrefabMsgFormatter msgAvailable() {
    return x ->
        x.negated()
            ? "Result " + x.name() + " must not be available (was " + toStr(x.arg()) + ')'
            : "No result available for " + x.name();
  }

}
