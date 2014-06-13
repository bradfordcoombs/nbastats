package com.bc.stats.util;

public class StringUtil {
   public static String getSafeArrayValue(String[] values, int index) {
      return values.length > 0 ? values[index].trim() : "";
   }

   public static Long safeConvertStringArrayToLong(String[] values, int index) {
      String safeValue = getSafeArrayValue(values, index);
      return Long.parseLong(safeValue.length() > 0 ? safeValue : "0");
   }

   public static boolean equalsValueOrNull(String string1, String string2) {
      return isNullOrEmpty(string1) || safeEquals(string1, string2);
   }

   public static boolean equalsValueOrNullIgnoreCase(String string1, String string2) {
      return isNullOrEmpty(string1) || safeEqualsIgnoreCase(string1, string2);
   }

   public static boolean isNullOrEmpty(String string) {
      if(string == null || string.equals("")) {
         return true;
      }
      return false;
   }

   public static boolean safeEquals(String string1, String string2) {
      if(string1 == null) {
         string1 = "";
      }
      if(string2 == null) {
         string2 = "";
      }
      return string1.equals(string2);
   }

   public static boolean safeEqualsIgnoreCase(String string1, String string2) {
      return safeEquals(string1.toLowerCase(), string2.toLowerCase());
   }
}
