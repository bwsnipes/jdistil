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
  Class used to format and parse currency values.
  @author - Bryan Snipes
*/
public class CurrencyConverter implements IConverter {

  /**
   * Singleton instance.
   */
  private static final CurrencyConverter currencyConverter = new CurrencyConverter();
  
  /**
   * Returns a singleton instance of the CurrencyConverter object.
   * @return CurrencyConverter Currency converter object.
   */
  public static CurrencyConverter getInstance() {
    return currencyConverter;
  }
  
  /**
    Creates a new CurrencyConverter instance. Defined with private access to prevent instantiation.
  */
  private CurrencyConverter() {
    super();
  }

  /**
	  Returns a pattern used for conversion if one is used.
	  @param locale - Locale.
	  @return String - Pattern used for conversion.
	*/
	public String getPattern(Locale locale) {
		
    // Get locale specific currency formatter
		DecimalFormat currencyFormatter = (DecimalFormat)getCurrencyFormatter(locale);
    
		return currencyFormatter.toPattern();
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
  
      // Get locale specific currency formatter
      NumberFormat currencyFormatter = getCurrencyFormatter(locale);
      
      // Format number
      formattedNumber = currencyFormatter.format(value);
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
  
      // Get locale specific currency formatter
      NumberFormat currencyFormatter = getCurrencyFormatter(locale);
      
      // Attempt to parse value as a number
      ParsePosition parsePosition = new ParsePosition(0);
      Number parsedNumber = currencyFormatter.parse(value, parsePosition);
  
      // Verify the entire string was parsed
      if (parsePosition.getIndex() == value.trim().length()) {
        number = parsedNumber;
      }
    }
  
    return number;
  }
  
  /**
    Returns a locale specific currency formatter.
    @param locale - Locale.
    @return NumberFormat - Locale specific currency formatter.
  */
  private NumberFormat getCurrencyFormatter(Locale locale) {
   
    // Use default locale if one is not specified
    if (locale == null) {
      locale = Locale.getDefault();
    }
      
    // Create locale specific currency formatter
    NumberFormat currencyFormatter = createCurrencyFormatter(locale);
    
    return currencyFormatter;
  }
  
  /**
    Creates a locale specific currency formatter.
    @param locale - Locale.
    @return NumberFormat - Locale specific currency formatter.
  */
  private NumberFormat createCurrencyFormatter(Locale locale) {
    
    // Create currency formatter
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    
    if (currencyFormatter instanceof DecimalFormat) {
      
      // Retrieve currency format
      String format = Formats.getFormat(Formats.CURRENCY_FORMAT, locale);
      
      if (!StringUtil.isEmpty(format)) {

        // Apply locale specific currency format
      	((DecimalFormat)currencyFormatter).applyLocalizedPattern(format);
      }
    }
  
    return currencyFormatter;
  }
  
}
