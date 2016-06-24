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
  Class used to format and parse date time objects.
  @author - Bryan Snipes
*/
public class DateTimeConverter implements IConverter {

  /**
   * Standard date time format.
   */
  private static final String STANDARD_DATE_TIME_FORMAT = "MM/dd/yyyyTHH:mm";
  
  /**
   * Singleton instance.
   */
  private static final DateTimeConverter dateTimeConverter = new DateTimeConverter();
  
  /**
   * Returns a singleton instance of the DateTimeConverter object.
   * @return DateTimeConverter Date time converter object.
   */
  public static DateTimeConverter getInstance() {
    return dateTimeConverter;
  }
  
  /**
    Creates a new DateTimeConverter instance. Defined with private access to prevent instantiation.
  */
  private DateTimeConverter() {
    super();
  }
  
  /**
	  Returns a locale specific date time pattern.
	  @param locale - Locale.
	  @return String - Locale specific date time pattern.
	*/
	public String getPattern(Locale locale) {
		
	  // Get locale specific date time formatter
		SimpleDateFormat dateTimeFormatter = (SimpleDateFormat)getDateTimeFormatter(locale);
	  
		return dateTimeFormatter.toPattern();
	}
	
  /**
    Returns a string representation of a given date object in a locale specific format.
    @param value - Date object.
    @param locale - Locale.
    @return String - String representation of date and time.
  */
  public String format(Object value, Locale locale) {
  
    // Initialize return value
    String formattedDateTime = null;
  
    if (value != null && value instanceof Date) {
  
      // Get locale specific date time formatter
      DateFormat dateTimeFormatter = getDateTimeFormatter(locale);
      
      // Format date time
      formattedDateTime = dateTimeFormatter.format((Date)value);
    }
  
    return formattedDateTime;
  }
  
  /**
    Returns a string representation of a given date object in a standard date time format.
    @param value - Date object.
    @return String - String representation of date in standard date time format.
  */
  public String formatAsStandard(Object value) {
  
    // Initialize return value
    String formattedDateTime = null;
  
    if (value != null && value instanceof Date) {
  
      // Get standard date time formatter
      DateFormat dateTimeFormatter = getStandardDateTimeFormatter();
      
      // Format date time
      formattedDateTime = dateTimeFormatter.format((Date)value);
    }
  
    return formattedDateTime;
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
  
    	// First attempt to parse using standard date time formatter
    	date = (Date)parseAsStandard(value);
    	
    	if (date == null) {
    		
      	try {
          // Get locale specific date time formatter
          DateFormat dateTimeFormatter = getDateTimeFormatter(locale);
          
          // Attempt to parse date time
          date = dateTimeFormatter.parse(value);
        }
        catch (ParseException parseException) {
          System.out.println("Error parsing date time: " + parseException.getMessage());
        }
    	}
    }
  
    return date;
  }
  
  /**
    Returns a date object created from a string value in a standard date time format.
    @param value - String value.
    @return Object - Date created from string value.
  */
  public Object parseAsStandard(String value) {
  
    // Initialize return value
    Date date = null;
  
    // Check for valid string
    if (!StringUtil.isEmpty(value)) {
  
      try {
      	
        // Get standard date time formatter
        DateFormat dateTimeFormatter = getStandardDateTimeFormatter();
        
        // Attempt to parse date time
        date = dateTimeFormatter.parse(value);
      }
      catch (ParseException parseException) {
        System.out.println("Error parsing standard date time: " + parseException.getMessage());
      }
    }
  
    return date;
  }
  
  /**
    Returns a date time formatter configured with a locale specific pattern.
    @param locale - Locale.
    @return DateFormat - Locale specific date time formatter.
  */
  private DateFormat getDateTimeFormatter(Locale locale) {
   
    // Use default locale if one is not specified
    if (locale == null) {
      locale = Locale.getDefault();
    }
      
    // Create locale specific date time formatter
    DateFormat dateTimeFormatter = createDateTimeFormatter(locale);
    
    return dateTimeFormatter;
  }
  
  /**
    Returns a date time formatter configured with a standard date time pattern.
    @return DateFormat - Date time formatter configured with a standard date time pattern.
  */
  private DateFormat getStandardDateTimeFormatter() {
   
    // Create standard date time formatter
    DateFormat dateTimeFormatter = createStandardDateTimeFormatter();
    
    return dateTimeFormatter;
  }
  
  /**
    Creates a date time formatter configured with a locale specific pattern.
    @param locale - Locale.
    @return DateFormat - Locale specific date time formatter.
  */
  private DateFormat createDateTimeFormatter(Locale locale) {
    
    // Create date time formatter
    DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
    
    // Set to lenient
    dateTimeFormatter.setLenient(false);

    if (dateTimeFormatter instanceof SimpleDateFormat) {
      
      // Retrieve date time format
      String format = Formats.getFormat(Formats.DATE_TIME_FORMAT, locale);
      
      if (!StringUtil.isEmpty(format)) {

        // Apply locale specific date time format
      	((SimpleDateFormat)dateTimeFormatter).applyLocalizedPattern(format);
      }
    }
  
    return dateTimeFormatter;
  }

  /**
    Creates a date time formatter configured with a standard date time pattern.
    @return DateFormat - Standard based date time formatter.
  */
  private DateFormat createStandardDateTimeFormatter() {
    
    // Create date time formatter
    DateFormat dateTimeFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
    
    // Set to lenient
    dateTimeFormatter.setLenient(false);
  
    if (dateTimeFormatter instanceof SimpleDateFormat) {
      
      // Apply locale specific date time format
    	((SimpleDateFormat)dateTimeFormatter).applyLocalizedPattern(STANDARD_DATE_TIME_FORMAT);
    }
  
    return dateTimeFormatter;
  }
  
}
