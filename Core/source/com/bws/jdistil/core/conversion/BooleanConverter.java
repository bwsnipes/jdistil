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

import com.bws.jdistil.core.util.Descriptions;
import com.bws.jdistil.core.util.StringUtil;

import java.util.Locale;

/**
  Class used to format and parse boolean objects.
  Implementation does not support locale specific formats.
  @author - Bryan Snipes
*/
public class BooleanConverter implements IConverter {

  /**
    Yes value constant.
  */
  private static final String YES = "Yes";

  /**
    No value constant.
  */
  private static final String NO = "No";

  /**
   * Singleton instance.
   */
  private static final BooleanConverter booleanConverter = new BooleanConverter();
  
  /**
   * Returns a singleton instance of the BooleanConverter object.
   * @return BooleanConverter Boolean converter object.
   */
  public static BooleanConverter getInstance() {
    return booleanConverter;
  }
  
  /**
    Creates a new BooleanConverter instance. Defined with private access to prevent instantiation.
  */
  private BooleanConverter() {
    super();
  }

  /**
	  Returns a pattern used for conversion if one is used.
	  @param locale - Locale.
	  @return String - Pattern used for conversion.
	*/
	public String getPattern(Locale locale) {
		return null;
	}
	
  /**
    Returns a string representation of a boolean value.
    @param value - Boolean to format.
    @param locale - Locale.
    @return String - String representation of boolean value.
  */
  public String format(Object value, Locale locale) {

    // Initialize return value
    String formattedBoolean = "";

    // Format boolean
    if (value != null && value instanceof Boolean) {
      
        // Cast to correct type
        Boolean booleanValue = (Boolean)value;
        
        // Set formatted boolean value
        formattedBoolean = booleanValue.booleanValue() ? YES : NO;

        // Attempt to translate value to a locale specific description
        formattedBoolean = Descriptions.getDescription(formattedBoolean, locale);
    }

    return formattedBoolean;
  }

  /**
    Returns a boolean object created from a given string value.
    @param value - Boolean string value.
    @param locale - Locale.
    @return Boolean - Boolean created from string value.
  */
  public Object parse(String value, Locale locale) {

    // Initialize return value
    Boolean booleanValue = null;

    // Check for valid string
    if (!StringUtil.isEmpty(value)) {
    	
      // Attempt to translate boolean value to a locale specific description
      String yes = Descriptions.getDescription(YES, locale);
    	
    	// Set boolean value
      booleanValue = value.equalsIgnoreCase(yes) ? Boolean.TRUE : Boolean.FALSE;
    }

    return booleanValue;
  }

}
