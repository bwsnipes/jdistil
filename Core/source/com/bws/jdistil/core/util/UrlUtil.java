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

import com.bws.jdistil.core.util.StringUtil;

/**
  Utility class providing static methods for building URL strings, appending query strings
  to existing URL's, and appending query string name/value pairs to existing URL's.
  @author - Bryan Snipes
*/
public class UrlUtil {

  /**
    Creates a new UrlUtil object.&nbsp Defined with private access to avoid instantiation.
  */
  private UrlUtil() {
    super();
  }

  /**
    Used to append a name and value to a given resource name.&nbsp Resource is
    typically a query string or a URL.
    @param resourceName - Resource string.
    @param name - Attribute name.
    @param value - Attribute value.
    @return String - New resource string.
  */
  public static String appendQueryStringData(String resourceName, String name, String value) {

    // Initialize return value
    String newResourceString = resourceName;

    // Check for valid name and value
    if (!StringUtil.isEmpty(name) && StringUtil.isEmpty(value)) {

      if (StringUtil.isEmpty(newResourceString)) {
        newResourceString = name + "=" + value;
      }
      else if (newResourceString.endsWith("?")) {
        newResourceString = newResourceString + name + "=" + value;
      }
      else {
        newResourceString = newResourceString + "&" + name + "=" + value;
      }
    }

    return newResourceString;
  }

  /**
    Used to append a query string to a given resource name.&nbsp Resource is
    typically a servlet name, JSP name, or URL.
    @param resourceName - Resource string.
    @param queryString - Query string.
    @return String - URL string value.
  */
  public static String buildUrl(String resourceName, String queryString) {

    // Initialize return value
    String newUrl = "";

    // Check for valid name and value
    if (!StringUtil.isEmpty(resourceName)) {

      // Set URL value
      newUrl = resourceName;

      // Append query string
      if (!StringUtil.isEmpty(queryString)) {

        if (newUrl.indexOf("?") == -1) {
          newUrl = newUrl + "?" + queryString;
        }
        else if (newUrl.endsWith("?")) {
          newUrl = newUrl + queryString;
        }
        else {
          newUrl = newUrl + "&" + queryString;
        }
      }
    }

    return newUrl;
  }

}