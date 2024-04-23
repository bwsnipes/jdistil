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

import com.bws.jdistil.core.util.StringUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
  Class used to format and parse decimal values.
  @author - Bryan Snipes
*/
public class DecimalConverter implements IConverter {

  /**
   * Singleton instance.
   */
  private static final DecimalConverter DecimalConverter = new DecimalConverter();
  
  /**
   * Returns a singleton instance of the DecimalConverter object.
   * @return DecimalConverter Decimal converter object.
   */
  public static DecimalConverter getInstance() {
    return DecimalConverter;
  }
  
  /**
    Creates a new DecimalConverter instance. Defined with private access to prevent instantiation.
  */
  private DecimalConverter() {
    super();
  }
  
  /**
	  Returns a pattern used for conversion if one is used.
	  @param locale - Locale.
	  @return String - Pattern used for conversion.
	*/
	public String getPattern(Locale locale) {
		
	  // Get locale specific decimal formatter
		DecimalFormat decimalFormatter = (DecimalFormat)getDecimalFormatter(locale);
	  
		return decimalFormatter.toPattern();
	}

	/**
    Returns a locale specific string representation of a given number object.
    @param value - Number to format.
    @param locale - Locale.
    @return String - Formatted string representation of object.
  */
  public String format(Object value, Locale locale) {
  
    // Initialize return value
    String formattedNumber = null;
  
    if (value != null) {
  
      // Get locale specific decimal formatter
      NumberFormat decimalFormatter = getDecimalFormatter(locale);
      
      // Format number
      formattedNumber = decimalFormatter.format(value);
    }
  
    return formattedNumber;
  }
  
  /**
    Returns a locale specific number object created from a given string value.
    @param value - String value.
    @param locale - Locale.
    @return Object - Number created from string value.
  */
  public Object parse(String value, Locale locale) {
  
    // Initialize return value
    Number number = null;
  
    // Check for valid string
    if (!StringUtil.isEmpty(value)) {
  
      // Get locale specific decimal formatter
      NumberFormat decimalFormatter = getDecimalFormatter(locale);
      
      // Attempt to parse value as a number
      ParsePosition parsePosition = new ParsePosition(0);
      Number parsedNumber = decimalFormatter.parse(value, parsePosition);
  
      // Verify the entire string was parsed
      if (parsePosition.getIndex() == value.trim().length()) {
        number = parsedNumber;
      }
    }
  
    return number;
  }
  
  /**
    Returns a locale specific decimal formatter.
    @param locale - Locale.
    @return NumberFormat - Locale specific decimal formatter.
  */
  private NumberFormat getDecimalFormatter(Locale locale) {
   
    // Use default locale if one is not specified
    if (locale == null) {
      locale = Locale.getDefault();
    }
      
    // Create locale specific decimal formatter
    NumberFormat decimalFormatter = createDecimalFormatter(locale);
    
    return decimalFormatter;
  }
  
  /**
    Creates a locale specific decimal formatter.
    @param locale - Locale.
    @return NumberFormat - Locale specific decimal formatter.
  */
  private NumberFormat createDecimalFormatter(Locale locale) {
    
    // Create decimal formatter
    NumberFormat decimalFormatter = NumberFormat.getNumberInstance(locale);
    
    if (decimalFormatter instanceof DecimalFormat) {
      
      // Retrieve decimal format
      String format = Formats.getFormat(Formats.DECIMAL_FORMAT, locale);
      
      if (!StringUtil.isEmpty(format)) {

        // Apply locale specific decimal format
      	((DecimalFormat)decimalFormatter).applyLocalizedPattern(format);
      }
    }
  
    return decimalFormatter;
  }
  
}
