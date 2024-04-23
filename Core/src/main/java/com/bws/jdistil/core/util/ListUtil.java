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

import java.util.ArrayList;
import java.util.List;

/**
  Class used to format and parse lists of objects.
  @author - Bryan Snipes
*/
public class ListUtil {

  /**
    Default delimiter value.
  */
  public static final String COMMA = ",";

  /**
    Default enclosed by value.
  */
  public static final String DOUBLE_QUOTE = "\"";

  /**
    Creates a new instance of the ListConverter class. Defined with private access to prevent instantiation.
  */
  private ListUtil() {
    super();
  }

  /**
    Formats a list of objects as a delimited string.
    @param values - List of strings to format.
    @param delimiter - Custom delimiter.
    @param enclosedBy - Custom enclosed by value.
    @return String - Formatted delimited string value.
  */
  public static String format(List<String> values, String delimiter, String enclosedBy) {

    // Initialize return value
    StringBuffer delimitedString = new StringBuffer();

    if (values != null) {

      // Check for valid list and delimiter
      if (values != null && values.size() > 0 && !StringUtil.isEmpty(delimiter)) {

        // Process all objects
        for (int index = 0; index < values.size(); index++) {

          // Initialize value
          String text = "";

          // Get next value
          String value = values.get(index);
          
          // Get next value
          if (!StringUtil.isEmpty(value)) {
            text = value;
          }

          // Check for enclosure
          if (!StringUtil.isEmpty(enclosedBy)) {
            text = enclosedBy + text + enclosedBy;
          }

          // Add each element to delimited string
          delimitedString.append(text);

          // Add delimiter if not the last value
          if (index != values.size() - 1) {
            delimitedString.append(delimiter);
          }

        }
      }
    }

    return delimitedString.toString();
  }

  /**
    Returns a list of string objects created from a delimited string value.
    @param value - Delimited string value.
    @param delimiter - Custom delimiter.
    @param enclosedBy - Custom enclosed by value.
    @return List - List of string objects.
  */
  public static List<String> parse(String value, String delimiter, String enclosedBy) {

    // Initialize return value
    List<String> values = null;

    // Check for valid string and delimiter
    if (!StringUtil.isEmpty(value) && !StringUtil.isEmpty(delimiter)) {

      // Create values list
      values = new ArrayList<String>();

      // Get position of next delimiter
      int position = value.indexOf(delimiter);

      while (position > 0) {

        // Get next value
        String nextValue = value.substring(0, position);

        // Remove enclosing characters if necessary
        if (!StringUtil.isEmpty(enclosedBy) &&
            nextValue.length() > 2 &&
            nextValue.substring(0, 1).equals(enclosedBy) &&
            nextValue.substring(nextValue.length() - 1).equals(enclosedBy)) {

          nextValue = nextValue.substring(1, nextValue.length() - 1);
        }

        // Add value to list
        values.add(nextValue);

        // Remove parsed value from working string
        value = value.substring(position + 1);

        // Get position of next delimiter
        position = value.indexOf(delimiter);
      }

      // Add remaining value to list
      values.add(value);
    }

    return values;
  }

}
