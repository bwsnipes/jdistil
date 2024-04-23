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
  Class used to validate field values based on a field's associated field converter.
  Validation is not performed if a converter is not defined.
  @author - Bryan Snipes
*/
public class ConverterRule extends BaseFieldRule {

  /**
    Creates an empty ConverterFieldRule object.
  */
  public ConverterRule() {
    super();
  }

  /**
    Validates a field value based on the field's associated field converter.
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

      // Get field
      Field field = ConfigurationManager.getField(id);

      // Get field converter
      IConverter converter = field.getConverter();
      
      if (converter != null) {
        
        // Attempt to convert the value using the fields defined converter
        isValid = converter.parse(value, locale) != null;
        
        if (!isValid && messages != null) {
          
          // Get description
          String description = field.getDescription(locale);
 
          // Get conversion pattern if one exists 
          String pattern = converter.getPattern(locale);
          
          if (pattern == null) {

            // Set pattern to empty string if undefined
          	pattern = "";
          }
          
          // Build message values array
          Object[] values = new Object[2];
          values[0] = description;
          values[1] = pattern;

          // Create error message
          String errorMessage = Messages.formatMessage(locale, Messages.INVALID_FORMAT, values);

          // Add message to list
          messages.add(errorMessage);
        }
      }
    }

    return isValid;
  }

}
