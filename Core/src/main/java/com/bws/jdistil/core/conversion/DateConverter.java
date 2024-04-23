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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
  Class used to format and parse locale specific date objects.
  @author - Bryan Snipes
*/
public class DateConverter implements IConverter {

  /**
   * Standard date format.
   */
  private static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";
  
  /**
   * Singleton instance.
   */
  private static final DateConverter dateConverter = new DateConverter();
  
  /**
   * Returns a singleton instance of the DateConverter object.
   * @return DateConverter Date converter object.
   */
  public static DateConverter getInstance() {
    return dateConverter;
  }
  
  /**
    Creates a new DateConverter instance. Defined with private access to prevent instantiation.
  */
  private DateConverter() {
    super();
  }

  /**
	  Returns a locale specific date pattern.
	  @param locale - Locale.
	  @return String - Locale specific date pattern.
	*/
	public String getPattern(Locale locale) {
		
	  // Get locale specific date formatter
		SimpleDateFormat dateFormatter = (SimpleDateFormat)getDateFormatter(locale);
	  
		return dateFormatter.toPattern();
	}

  /**
    Returns a string representation of a given date object in a locale specific format.
    @param value - Date object.
    @param locale - Locale.
    @return String - String representation of date.
  */
  public String format(Object value, Locale locale) {

    // Initialize return value
    String formattedDate = null;

    if (value != null && value instanceof Date) {

      // Get locale specific date formatter
      DateFormat dateFormatter = getDateFormatter(locale);
      
      // Format date
      formattedDate = dateFormatter.format((Date)value);
    }

    return formattedDate;
  }

  /**
    Returns a string representation of a given date object in a standard date format.
    @param value - Date object.
    @return String - String representation of date in standard date format.
  */
  public String formatAsStandard(Object value) {
  
    // Initialize return value
    String formattedDate = null;
  
    if (value != null && value instanceof Date) {
  
      // Get standard date formatter
      DateFormat dateFormatter = getStandardDateFormatter();
      
      // Format date
      formattedDate = dateFormatter.format((Date)value);
    }
  
    return formattedDate;
  }
  
  /**
    Returns a date object created from a string value in a locale specific format.
    @param value - String value.
    @param locale - Locale.
    @return Object - Date created from string value.
  */
  public Object parse(String value, Locale locale) {

    // Initialize return value
    Date date = null;

    // Check for valid string
    if (!StringUtil.isEmpty(value)) {

    	// First attempt to parse using standard date formatter
    	date = (Date)parseAsStandard(value);
    	
    	if (date == null) {
    		
      	try {
        	
          // Get locale specific date formatter
          DateFormat dateFormatter = getDateFormatter(locale);
          
          // Attempt to parse date
          date = dateFormatter.parse(value);
        }
        catch (ParseException parseException) {
          System.out.println("Error parsing locale specific date: " + parseException.getMessage());
        }
    	}
    }

    return date;
  }

  /**
    Returns a date object created from a string value in a standard date format.
    @param value - String value.
    @return Object - Date created from string value.
  */
  public Object parseAsStandard(String value) {
  
    // Initialize return value
    Date date = null;
  
    // Check for valid string
    if (!StringUtil.isEmpty(value)) {
  
      try {
      	
        // Get standard date formatter
        DateFormat dateFormatter = getStandardDateFormatter();
        
        // Attempt to parse date
        date = dateFormatter.parse(value);
      }
      catch (ParseException parseException) {
        System.out.println("Error parsing standard date: " + parseException.getMessage());
      }
    }
  
    return date;
  }
  
  /**
    Returns a date formatter configured with a locale specific pattern.
    @param locale - Locale.
    @return DateFormat - Locale specific date formatter.
  */
  private DateFormat getDateFormatter(Locale locale) {
   
    // Use default locale if one is not specified
    if (locale == null) {
      locale = Locale.getDefault();
    }
      
    // Create locale specific date formatter
    DateFormat dateFormatter = createDateFormatter(locale);
    
    return dateFormatter;
  }
  
  /**
    Returns a date formatter configured with a standard date pattern.
    @return DateFormat - Date formatter configured with a standard date pattern.
  */
  private DateFormat getStandardDateFormatter() {
   
    // Create standard date formatter
    DateFormat dateFormatter = createStandardDateFormatter();
    
    return dateFormatter;
  }
  
  /**
    Creates a date formatter configured with a locale specific pattern.
    @param locale - Locale.
    @return DateFormat - Locale specific date formatter.
  */
  private DateFormat createDateFormatter(Locale locale) {
    
    // Create date formatter
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    
    // Set to lenient
    dateFormatter.setLenient(false);

    if (dateFormatter instanceof SimpleDateFormat) {
      
      // Retrieve date format
      String format = Formats.getFormat(Formats.DATE_FORMAT, locale);
      
      if (!StringUtil.isEmpty(format)) {

        // Apply locale specific date format
      	((SimpleDateFormat)dateFormatter).applyLocalizedPattern(format);
      }
    }
  
    return dateFormatter;
  }
  
  /**
    Creates a date formatter configured with a standard date pattern.
    @return DateFormat - Standard based date formatter.
  */
  private DateFormat createStandardDateFormatter() {
    
    // Create date formatter
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
    
    // Set to lenient
    dateFormatter.setLenient(false);
  
    if (dateFormatter instanceof SimpleDateFormat) {
      
      // Apply locale specific date format
    	((SimpleDateFormat)dateFormatter).applyLocalizedPattern(STANDARD_DATE_FORMAT);
    }
  
    return dateFormatter;
  }
  
}
