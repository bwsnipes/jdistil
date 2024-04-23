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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.message.Messages;

/**
  Action rule requiring data for at least one of a specified set of fields.
  @author - Bryan Snipes
*/
public class AtLeastOneFieldRule implements IActionRule {

	/**
	 * Set of field IDs to validate.
	 */
	private List<String> fieldIds = new ArrayList<String>();
	
  /**
    Creates an AtLeastOneFieldRule object using a set of field IDs.
    @param fieldIds Multiple field IDs.
  */
  public AtLeastOneFieldRule(String ...fieldIds) {
    super();
    
    if (fieldIds != null) {
    	
    	// Add all field ID values to internal list
    	for (String fieldId : fieldIds) {
    		this.fieldIds.add(fieldId);
    	}
    }
  }

  /**
    Validates a given field value and adds a formatted error message to a given list of messages.
    @param actionId - Action ID.
    @param data - Map of action data.
    @param locale - Locale.
    @param messages - List of error messages.
    @return boolean - Valid action data indicator.
  */
  public boolean isValid(String actionId, Map<String, String[]> data, Locale locale, List<String> messages) {

    // Initialize return value
    boolean isValid = true;

    if (messages != null) {

    	// Set to invalid
    	isValid = isValid(data);
    	
    	if (!isValid) {
    		
      	// Initialize field descriptions
      	StringBuffer fieldDescriptions = new StringBuffer();
      	
        for (int index = 0; index < fieldIds.size(); index++) {
        	
        	// Get next field ID
        	String id = fieldIds.get(index);
        	
          // Get field
          Field field = ConfigurationManager.getField(id);

          // Get description
          String description = field.getDescription(locale);
          
          if (index > 0) {
          	
            // Append field description separator
            fieldDescriptions.append(", ");
          }

          // Append field description
          fieldDescriptions.append(description);
        }

        // Create error message
        String errorMessage = Messages.formatMessage(locale, Messages.REQUIRED_FIELD, fieldDescriptions);

        // Add message to list
        messages.add(errorMessage);
    	}
    }

    return isValid;
  }

  private boolean isValid(Map<String, String[]> data) {
  	
  	// Initialize return value
  	boolean isValid = false;
  	
    for (String id : fieldIds) {
  		
      // Get field values
      String[] fieldValues = data.get(id);

      if (fieldValues != null && fieldValues.length > 0) {
      	
      	isValid = true;
      	break;
      }
  	}
    
    return isValid;
  }
  
}
