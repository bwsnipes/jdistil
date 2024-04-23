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
import com.bws.jdistil.core.util.StringUtil;

import java.util.List;
import java.util.Locale;

/**
  Abstract base class implementing the IFieldRule interface.&nbsp; Provides default
  validation checking, support for storing and retrieving the error text and
  support for retrieving field descriptions based on a field ID.
  @author - Bryan Snipes
*/
public abstract class BaseFieldRule implements IFieldRule {

  /**
    Creates an empty BaseFieldRule object.
  */
  public BaseFieldRule() {
    super();
  }

  /**
    Validates a given field value and adds a formatted error message to a given
    list of messages.
    @param id - Field ID.
    @param value - Field value.
    @param locale - Locale.
    @param messages - List of error messages.
    @return boolean - Valid field value indicator.
  */
  public boolean isValid(String id, String value, Locale locale, List<String> messages) {

    // Validate value
    boolean isValid = !StringUtil.isEmpty(value);

    if (!isValid && messages != null) {

      // Get field
      Field field = ConfigurationManager.getField(id);

      // Get description
      String description = field.getDescription(locale);

      // Create error message
      String errorMessage = Messages.formatMessage(locale, Messages.REQUIRED_FIELD, description);

      // Add message to list
      messages.add(errorMessage);
    }

    return isValid;
  }

}
