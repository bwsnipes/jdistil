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
import com.bws.jdistil.core.message.Messages;

import java.util.List;
import java.util.Locale;

/**
  Class used to validate the maximum length of a field value.
  @author - Bryan Snipes
*/
public class MaxLengthRule extends BaseFieldRule {

  /**
    Maximum size.
  */
  private int maximumSize = 500;

  /**
    Creates a default MaxLengthRule object.
  */
  public MaxLengthRule() {
    this(500);
  }

  /**
    Creates a MaxLengthRule object.
    @param maximumSize - Maximum size.
  */
  public MaxLengthRule(int maximumSize) {
    super();

    // Set maximum size
    if (maximumSize > 0) {
      this.maximumSize = maximumSize;
    }
  }

  /**
    Validates a field value and adds a formatted error message to a given list of messages.
    @param id - Field ID.
    @param value - Field value.
    @param locale - Locale.
    @param messages - List of error messages.
    @return boolean - Valid field value indicator.
  */
  public boolean isValid(String id, String value, Locale locale, List<String> messages) {

    // Validate value
    boolean isValid = super.isValid(id, value, locale, messages);

    if (isValid) {
      
      // Check max length
      isValid = value == null || value.length() <= maximumSize;
      
      if (!isValid && messages != null) {

        // Get field
        Field field = ConfigurationManager.getField(id);
  
        // Get description
        String description = field.getDescription(locale);
  
        // Build message values array
        Object[] values = new Object[2];
        values[0] = description;
        values[1] = String.valueOf(maximumSize);
  
        // Create error message
        String errorMessage = Messages.formatMessage(locale, Messages.LESS_THAN_CHARACTERS, values);
  
        // Add message to list
        messages.add(errorMessage);
      }
    }

    return isValid;
  }


}
