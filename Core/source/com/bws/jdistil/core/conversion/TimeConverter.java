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
  Class used to format and parse locale specific time objects.
  @author - Bryan Snipes
*/
public class TimeConverter implements IConverter {

  /**
   * Standard time format.
   */
  private static final String STANDARD_TIME_FORMAT = "HH:mm";
  
  /**
   * Singleton instance.
   */
  private static final TimeConverter timeConverter = new TimeConverter();
  
  /**
   * Returns a singleton instance of the TimeConverter object.
   * @return TimeConverter Boolean converter object.
   */
  public static TimeConverter getInstance() {
    return timeConverter;
  }
  
  /**
    Creates a new TimeConverter instance. Defined with private access to prevent instantiation.
  */
  private TimeConverter() {
    super();
  }

  /**
	  Returns a locale specific time pattern.
	  @param locale - Locale.
	  @return String - Locale specific time pattern.
	*/
	public String getPattern(Locale locale) {
		
	  // Get locale specific time formatter
		SimpleDateFormat timeFormatter = (SimpleDateFormat)getTimeFormatter(locale);
	  
		return timeFormatter.toPattern();
	}
	
  /**
    Returns a string representation of a given date object in a locale specific format.
    @param value - Date object.
    @param locale - Locale.
    @return String - String representation of time.
  */
  public String format(Object value, Locale locale) {
  
    // Initialize return value
    String formattedTime = null;
  
    if (value != null && value instanceof Date) {
  
      // Get locale specific time formatter
      DateFormat timeFormatter = getTimeFormatter(locale);
      
      // Format time
      formattedTime = timeFormatter.format((Date)value);
    }
  
    return formattedTime;
  }
  
  /**
    Returns a string representation of a given date object in a standard time format.
    @param value - Date object.
    @return String - String representation of date in standard time format.
  */
  public String formatAsStandard(Object value) {
  
    // Initialize return value
    String formattedTime = null;
  
    if (value != null && value instanceof Date) {
  
      // Get standard time formatter
      DateFormat timeFormatter = getStandardTimeFormatter();
      
      // Format time
      formattedTime = timeFormatter.format((Date)value);
    }
  
    return formattedTime;
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
  
    	// First attempt to parse using standard time formatter
    	date = (Date)parseAsStandard(value);
    	
    	if (date == null) {
    		
        try {

        	// Get locale specific time formatter
          DateFormat timeFormatter = getTimeFormatter(locale);
          
          // Attempt to parse time
          date = timeFormatter.parse(value);
        }
        catch (ParseException parseException) {
          System.out.println("Error parsing time: " + parseException.getMessage());
        }
    	}    	
    }
  
    return date;
  }
  
  /**
    Returns a date object created from a string value in a standard time format.
    @param value - String value.
    @return Object - Date created from string value.
  */
  public Object parseAsStandard(String value) {
  
    // Initialize return value
    Date date = null;
  
    // Check for valid string
    if (!StringUtil.isEmpty(value)) {
  
      try {
      	
        // Get standard time formatter
        DateFormat timeFormatter = getStandardTimeFormatter();
        
        // Attempt to parse time
        date = timeFormatter.parse(value);
      }
      catch (ParseException parseException) {
        System.out.println("Error parsing standard time: " + parseException.getMessage());
      }
    }
  
    return date;
  }

  /**
    Returns a time formatter configured with a locale specific pattern.
    @param locale - Locale.
    @return DateFormat - Locale specific time formatter.
  */
  private DateFormat getTimeFormatter(Locale locale) {
   
    // Use default locale if one is not specified
    if (locale == null) {
      locale = Locale.getDefault();
    }
      
    // Create locale specific time formatter
    DateFormat timeFormatter = createTimeFormatter(locale);
  
    return timeFormatter;
  }
  
  /**
    Returns a time formatter configured with a standard time pattern.
    @return DateFormat - Time formatter configured with a standard time pattern.
  */
  private DateFormat getStandardTimeFormatter() {
   
    // Create standard time formatter
    DateFormat timeFormatter = createStandardTimeFormatter();
    
    return timeFormatter;
  }
  
  /**
    Creates a time formatter configured with a locale specific pattern.
    @param locale - Locale.
    @return DateFormat - Locale specific time formatter.
  */
  private DateFormat createTimeFormatter(Locale locale) {
    
    // Create date formatter
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
    
    // Set to lenient
    timeFormatter.setLenient(false);

    if (timeFormatter instanceof SimpleDateFormat) {
      
      // Retrieve time format
      String format = Formats.getFormat(Formats.TIME_FORMAT, locale);
      
      if (!StringUtil.isEmpty(format)) {

        // Apply locale specific time format
      	((SimpleDateFormat)timeFormatter).applyLocalizedPattern(format);
      }
    }
  
    return timeFormatter;
  }
  
  /**
    Creates a time formatter configured with a standard time pattern.
    @return DateFormat - Standard based date formatter.
  */
  private DateFormat createStandardTimeFormatter() {
    
    // Create time formatter
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
    
    // Set to lenient
    timeFormatter.setLenient(false);
  
    if (timeFormatter instanceof SimpleDateFormat) {
      
      // Apply locale specific time format
    	((SimpleDateFormat)timeFormatter).applyLocalizedPattern(STANDARD_TIME_FORMAT);
    }
  
    return timeFormatter;
  }
  
}
