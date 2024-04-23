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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
  Base class used to validate fields using a regular expression.
  @author - Bryan Snipes
*/
public class RegularExpressionRule extends BaseFieldRule {

	/**
	 * Regular expression matcher.
	 */
	private Pattern pattern = null;
	
	/**
	 * Format displayed for input guidance.
	 */
	private String displayFormat = "";
	
  /**
    Creates an RegularExpressionRule object using a regular expression.
  */
  public RegularExpressionRule(String regularExpression) {

  	this(regularExpression, null);
  }

  /**
    Creates an RegularExpressionRule object using a regular expression and display format.
  */
  public RegularExpressionRule(String regularExpression, String displayFormat) {
    super();
    
    if (!StringUtil.isEmpty(regularExpression)) {
    	
    	pattern = Pattern.compile(regularExpression);
    }
    
    if (displayFormat != null) {
    	
      this.displayFormat = displayFormat;
    }
  }
  
  /**
    Validates a field value using a regular expression and adds a formatted error message to a given list of messages.
    @param id - Field ID.
    @param value - Field value.
    @param locale - Locale.
    @param messages - List of error messages.
    @return boolean - Valid field value indicator.
  */
  public boolean isValid(String id, String value, Locale locale, List<String> messages) {

    // Validate value
    boolean isValid = super.isValid(id, value, locale, messages);

    if (isValid && pattern != null) {
    	
    	// Validate value using regular expression 
    	Matcher matcher = pattern.matcher(value);
  		isValid = matcher.matches();
  		
      if (!isValid && messages != null) {
        
        // Get field
        Field field = ConfigurationManager.getField(id);
  
        // Get description
        String description = field.getDescription(locale);
  
        // Build message values array
        Object[] values = new Object[2];
        values[0] = description;
        values[1] = displayFormat;
  
        // Create error message
        String errorMessage = Messages.formatMessage(locale, Messages.INVALID_FORMAT, values);

        // Add message to list
        messages.add(errorMessage);
      }
  		
    }

    return isValid;
  }

}
