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
package com.bws.jdistil.core.conversion;

import java.util.Locale;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;

/**
  Class defining format constants used to retrieve converter formats from the formats resource bundle.
  @see FormatResource
  @author - Bryan Snipes
*/
public class Formats {

  /**
    Date format property resource key.
  */
  public static final String DATE_FORMAT = "format.date";

  /**
    Time format property resource key.
  */
  public static final String TIME_FORMAT = "format.time";
  
  /**
    Date time format property resource key.
  */
  public static final String DATE_TIME_FORMAT = "format.date.time";
  
  /**
    Number format property key.
  */
  public static final String NUMBER_FORMAT = "format.number";

  /**
    Decimal format property resource key.
  */
  public static final String DECIMAL_FORMAT = "format.decimal";
  
  /**
    Currency format property resource key.
  */
  public static final String CURRENCY_FORMAT = "format.currency";
  
  /**
    Percentage format property resource key.
  */
  public static final String PERCENTAGE_FORMAT = "format.percentage";
  
  /**
    Creates a new Formats object.  Defined with private access to prevent instantiation.
  */
  private Formats() {
    super();
  }
  
  /**
    Returns a locale specific format for a given format type.
    @param type Format type.
	  @param locale Target locale.
    @return String Locale specific format.
  */
  public static String getFormat(String type, Locale locale) {
  
    // Initialize format
    String format = null;
  
    // Attempt to retrieve format resources name from core properties
    String formatResources = ResourceUtil.getString(Constants.FORMAT_RESOURCES);

    if (formatResources != null) {

      // Attempt to retrieve locale specific format
      format = ResourceUtil.getString(formatResources, locale, type);
    }
      
    if (StringUtil.isEmpty(format)) {
    	
      // Retrieve default format
      format = ResourceUtil.getString(FormatResource.class.getName(), locale, type);
    }
  
    return format;
  }
  
}
