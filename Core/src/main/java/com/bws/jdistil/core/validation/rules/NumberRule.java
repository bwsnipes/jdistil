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
package com.bws.jdistil.core.validation.rules;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.conversion.IConverter;
import com.bws.jdistil.core.message.Messages;

import java.util.List;
import java.util.Locale;

/**
  Class used to validate numeric fields associated with one of the numeric field converters.
  @see com.bws.jdistil.core.conversion.NumberConverter
  @see com.bws.jdistil.core.conversion.DecimalConverter
  @see com.bws.jdistil.core.conversion.CurrencyConverter
  @see com.bws.jdistil.core.conversion.PercentageConverter
  @author - Bryan Snipes
*/
public class NumberRule extends BaseFieldRule {

  /**
    Positive or negative value type constant.
  */
  public static final int ANY = 1;

  /**
    Positive value type constant.
  */
  public static final int POSITIVE = 2;

  /**
    Negative value type constant.
  */
  public static final int NEGATIVE = 3;

  /**
    Maximum precision.
  */
  private int maximumPrecision = 11;

  /**
    Maximum scale.
  */
  private int maximumScale = 2;

  /**
    Value type indicator.
  */
  private int valueType = ANY;

  /**
    Creates an empty NumberRule object.
    @param maximumPrecision - Maximum precision.
    @param maximumScale - Maximum scale.
    @param valueType - Value type.
  */
  public NumberRule(int maximumPrecision, int maximumScale, int valueType) {
    super();

    this.maximumPrecision = maximumPrecision;
    this.maximumScale = maximumScale;
    this.valueType = valueType;
  }

  /**
    Validates a numeric field value and adds a formatted error message to a given
    list of messages.
    @param id - Field ID.
    @param value - Field value.
    @param locale - Locale.
    @param messages - List of error messages.
    @return boolean - Valid field value indicator.
  */
  public boolean isValid(String id, String value, Locale locale, List<String> messages) {

    // Initialize return value
    boolean isValid = super.isValid(id, value, locale, messages);

    if (isValid) {

      // Get field
      Field field = ConfigurationManager.getField(id);

      // Get field converter
      IConverter converter = field.getConverter();
      
      if (converter != null) {
        
        // Parse number
        Object object = converter.parse(value, locale);
      
        if (object == null || !(object instanceof Number)) {
        	
          // Set valid indicator
          isValid = false;

          if (messages != null) {
            	
            // Get description
            String description = field.getDescription(locale);

            // Create error message
            String errorMessage = Messages.formatMessage(locale, Messages.INVALID_NUMBER, description);

            // Add message to list
            messages.add(errorMessage);
          }
        }
        else {
        	
          // Cast to number
          Number number = (Number)object;

          if (!isValidPositive(number)) {

            // Set valid indicator
            isValid = false;

            if (messages != null) {
                
              // Get description
              String description = field.getDescription(locale);

              // Build message values array
              Object[] values = new Object[2];
              values[0] = description;
              values[1] = "0";

              // Create error message
              String errorMessage = Messages.formatMessage(locale, Messages.GREATER_THAN_OR_EQUAL_VALUE, values);

              // Add message to list
              messages.add(errorMessage);
            }
          }
          else if (!isValidNegative(number)) {

            // Set valid indicator
            isValid = false;

            if (messages != null) {
                
              // Get description
              String description = field.getDescription(locale);
      
              // Build message values array
              Object[] values = new Object[2];
              values[0] = description;
              values[1] = "0";
      
              // Create error message
              String errorMessage = Messages.formatMessage(locale, Messages.LESS_THAN_VALUE, values);
     
              // Add message to list
              messages.add(errorMessage);
            }
          }
          else if (!isValidPrecision(value)) {

            // Set valid indicator
            isValid = false;

            if (messages != null) {
                
              // Get description
              String description = field.getDescription(locale);
      
              // Build message values array
              Object[] values = new Object[2];
              values[0] = description;
              values[1] = String.valueOf(maximumPrecision);
      
              // Create error message
              String errorMessage = Messages.formatMessage(locale, Messages.INVALID_PRECISION, values);
      
              // Add message to list
              messages.add(errorMessage);
            }
          }
          else if (!isValidScale(value)) {

            // Set valid indicator
            isValid = false;

            if (messages != null) {
                
              // Get description
              String description = field.getDescription(locale);
      
              // Build message values array
              Object[] values = new Object[2];
              values[0] = description;
              values[1] = String.valueOf(maximumScale);
      
              // Create error message
              String errorMessage = Messages.formatMessage(locale, Messages.INVALID_SCALE, values);
      
              // Add message to list
              messages.add(errorMessage);
            }
          }
        }
      }
    }

    return isValid;
  }

  /**
    Validates a given numeric value as a positive number.
    @param value - Field value.
    @return boolean - Valid positive value indicator.
  */
  protected boolean isValidPositive(Number value) {
    return valueType != POSITIVE || value.doubleValue() >= 0;
  }

  /**
    Validates a given numeric value as a negative number.
    @param value - Field value.
    @return boolean - Valid negative value indicator.
  */
  protected boolean isValidNegative(Number value) {
    return valueType != NEGATIVE || value.doubleValue() < 0;
  }

  /**
    Validates the precision of a given string value.
    @param value - Field value.
    @return boolean - Valid precision indicator.
  */
  private boolean isValidPrecision(String value) {
  
    // Get precision of value
    int precision = value.replaceAll("\\D", "").length();
  
    return precision <= maximumPrecision;
  }
  
  /**
    Validates the scale of a given string value.
    @param value - Field value.
    @return boolean - Valid scale indicator.
  */
  private boolean isValidScale(String value) {
  
    // Initialize scale
    int scale = 0;
    
    // Get index of last comma and last decimal
    int commaIndex = value.lastIndexOf(",");
    int decimalIndex = value.lastIndexOf(".");

    // Set index based on
    int index = commaIndex < decimalIndex ? decimalIndex : commaIndex;
    
    if (index > 0) {
    	
    	// Get fractional portion of value
    	String fractionalValue = value.substring(index + 1);
    	
    	// Count digits in fractional value
    	scale = fractionalValue.replaceAll("\\D", "").length();
    }
  
    return scale <= maximumScale;
  }
  
}
