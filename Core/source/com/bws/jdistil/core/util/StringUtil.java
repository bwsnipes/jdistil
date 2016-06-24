/*
 * Copyright (C) 2015 Bryan W. Snipes
 * 
 * This file is part of the JDistil web application framework.
 * 
 * JDistil is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JDistil is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JDistil.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bws.jdistil.core.util;

/**
  This class provides static string utility methods.
  @author - Bryan Snipes
*/
public class StringUtil {

  /**
    Creates a new instance of the StringUtil class.
  */
  private StringUtil() {
    super();
  }

  /**
    Returns a value indicating whether or not a given string is empty (null or blank).
    @param target - Target object.
    @return boolean - Empty indicator.
  */
  public static boolean isEmpty(String target) {
    return target == null || target.trim().length() == 0;
  }

  /**
    Returns the string representation of a given object or an empty string if
    the object is null.
    @param source - Source object.
    @return String - Formatted string value.
  */
  public static String convertNull(Object source) {
    return convertNull(source, "");
  }

  /**
    Returns the string representation of a given object or a replacement string
    if the object is null.
    @param source - Source object.
    @param replacement - Replacement substring.
    @return String - Formatted string value.
  */
  public static String convertNull(Object source, String replacement) {
    return source == null ? replacement : source.toString();
  }

  /**
    Inserts a substring into a string value at a specified position.
    @param source - Source string.
    @param substring - Substring to insert.
    @param position - Position to insert.
    @return String - Formatted string value.
  */
  public static String insert(String source, String substring, int position) {

    // Initialize return value
    String formattedSource = source;

    if (formattedSource != null && substring != null) {

      // Validate position
      if (position >= 0 && position < formattedSource.length()) {

        // Insert substring
        formattedSource = formattedSource.substring(0, position) + substring +
            formattedSource.substring(position);
      }
    }

    return formattedSource;
  }

  /**
    Removes a substring from a given string value.
    @param source - Source string.
    @param substring - Substring to remove.
    @return String - Formatted string value.
  */
  public static String remove(String source, String substring) {

    // Initialize return value
    String formattedSource = source;

    if (formattedSource != null && substring != null) {

      // Get first occurrence of substring
      int position = formattedSource.indexOf(substring);

      while (position != -1) {

        // Remove substring
        if (position + substring.length() == formattedSource.length()) {
          formattedSource = formattedSource.substring(0, position);
        }
        else {
          formattedSource = formattedSource.substring(0, position) +
              formattedSource.substring(position + substring.length());
        }

        // Get next occurrence of substring
        position = formattedSource.indexOf(substring);
      }
    }

    return formattedSource;
  }

  /**
    Replaces a substring using another substring value.
    @param source - Source string.
    @param target - Substring to replace.
    @param replacement - Replacement substring.
    @return String - Formatted string value.
  */
  public static String replace(String source, String target, String replacement) {

    // Initialize return value
    String formattedSource = source;

    if (formattedSource != null && formattedSource.length() != 0 &&
        target != null && target.length() != 0 && replacement != null &&
        replacement.length() != 0 && !target.equals(replacement)) {

      // Get first occurrence of target
      int position = formattedSource.indexOf(target);

      while (position != -1) {

        // Remove substring
        if (position + target.length() == formattedSource.length()) {
          formattedSource = formattedSource.substring(0, position) + replacement;
        }
        else {
          formattedSource = formattedSource.substring(0, position) + replacement +
              formattedSource.substring(position + target.length());
        }

        // Get next occurrence of target
        position = formattedSource.indexOf(target, position + replacement.length());
      }
    }

    return formattedSource;
  }

  /**
    Replaces carriage return, line feed, and tab formatting characters using
    specified replacement values.&nbsp Null replacement values will be ignored.
    @param source - Source string.
    @param carriageReturn - Carriage return value.
    @param lineFeed - Line feed value.
    @param tab - Tab value.
    @return String - Formatted string value.
  */
  public static String replaceFormatChars(String source, String carriageReturn,
      String lineFeed, String tab) {

    // Initialize return value
    String formattedSource = source;

    if (formattedSource != null) {

      // Replace carriage returns
      if (carriageReturn != null) {
        formattedSource = replace(formattedSource, "\r", carriageReturn);
      }

      // Replace line feeds
      if (lineFeed != null) {
        formattedSource = replace(formattedSource, "\n", lineFeed);
      }

      // Replace tabs
      if (tab != null) {
        formattedSource = replace(formattedSource, "\t", tab);
      }
    }

    return formattedSource;
  }

}
