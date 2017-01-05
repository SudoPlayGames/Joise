package com.sudoplay.util;

public class ModuleID {

  private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

  private static final String MODULE_ID_PREFIX = "FUNC:";

  public static String create() {
    return MODULE_ID_PREFIX + createUUIDString();
  }

  /**
   * Generate a RFC4122, version 4 ID. Example:
   * "92329D39-6F5C-4520-ABFC-AAB64544E172"
   */
  private static String createUUIDString() {
    char[] uuid = new char[36];
    int r;

    // rfc4122 requires these characters
    uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
    uuid[14] = '4';

    // Fill in random data.  At i==19 set the high bits of clock sequence as
    // per rfc4122, sec. 4.1.5
    for (int i = 0; i < 36; i++) {

      if (uuid[i] == 0) {
        r = (int) (Math.random() * 16);
        uuid[i] = CHARS[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
      }
    }
    return new String(uuid);
  }
}
