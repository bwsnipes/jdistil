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
package com.bws.jdistil.core.validation;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.message.Messages;
import com.bws.jdistil.core.process.IProcessor;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.util.StringUtil;
import com.bws.jdistil.core.validation.rules.IActionRule;
import com.bws.jdistil.core.validation.rules.IFieldRule;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
  Default validator used to validate data from a given servlet request.
  Dependent upon information defined in configuration manager.
  @author Bryan Snipes
*/
public class DefaultValidator implements IProcessor {

  /**
    Creates a new DefaultValidator.
  */
  public DefaultValidator() {
    super();
  }

  /**
    Validates data contained in a given servlet request.
    @see com.bws.jdistil.core.process.Processor#process
  */
  public void process(ProcessContext processContext) throws ProcessException {

    // Check for request data
    if (processContext != null && processContext.getRequest() != null) {

      // Validate page data
      List<String> errorMessages = validatePage(processContext);
      
      // Add error messages to process context
      for (String errorMessage : errorMessages) {
        processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, errorMessage));
      }
    }
  }

  /**
    Returns a map of field IDs and values created using a given servlet request.
    Only non-empty fields are added to map.
    @param request - Servlet request containing data.
    @return Map - Map containing non-empty fields.
  */
  protected Map<String, String[]> createDataMap(HttpServletRequest request) {

    // Initialize return value
    Map<String, String[]> dataMap = new HashMap<String, String[]>();

    if (request != null) {

      // Get field IDs
      Enumeration<?> fieldIds = request.getParameterNames();

      // Process all field Ids
      while (fieldIds.hasMoreElements()) {

        // Get current field ID
        String fieldId = (String)fieldIds.nextElement();

        // Get field values
        String[] fieldValues = request.getParameterValues(fieldId);

        // Add non-empty fields to data map
        if (fieldValues != null && (fieldValues.length > 1 || !StringUtil.isEmpty(fieldValues[0]))) {
          dataMap.put(fieldId, fieldValues);
        }
      }
    }

    return dataMap;
  }

  /**
    Validates page data and returns a list of all encountered errors.
    @param processContext Process context.
    @return List - List of error messages.
  */
  protected List<String> validatePage(ProcessContext processContext) throws ProcessException {

  	// Set method name
  	String methodName =  "validatePage";
  	
    // Initialize return value
    List<String> errorMessages = new ArrayList<String>();

    // Get locale
    Locale locale = processContext.getRequest().getLocale();

    // Get action
    Action action = processContext.getAction();

    // Create data map
    Map<String, String[]> dataMap = createDataMap(processContext.getRequest());

    if (action != null && dataMap != null) {

      // Get current session
      HttpSession session = processContext.getRequest().getSession(true);
      
      // Get security manager
      ISecurityManager securityManager = processContext.getSecurityManager();
      
      // Get action rules
      List<IActionRule> actionRules = action.getRules(locale);

      if (actionRules != null) {

        // Apply all action rules
        for (IActionRule actionRule : actionRules) {
          actionRule.isValid(action.getId(), dataMap, locale, errorMessages);
        }
      }

      // Get field IDs
      List<String> fieldIds = action.getFieldIds();
      
      try {
      	
        // Process all field IDs in correct order
        for (String fieldId : fieldIds) {

          // Retrieve field
          Field field = ConfigurationManager.getField(fieldId);
          
          // Get field values
          String[] fieldValues = (String[])dataMap.get(fieldId);

          // Check required field
          if (fieldValues == null) {

            if (action.isFieldRequired(fieldId)) {
            	
              // Get field description
              String fieldDescription = field.getDescription(locale);

              // Format standard message and add to errors list
              String message = Messages.formatMessage(locale, Messages.REQUIRED_FIELD, fieldDescription);
              errorMessages.add(message);
            }
          }
          else if (!securityManager.isFieldHidden(fieldId, session) && !securityManager.isFieldReadOnly(fieldId, session)) {
          		
        		// Get field rules
            List<IFieldRule> fieldRules = field.getRules(locale);

            // Check field rules
            if (fieldRules != null) {

              // Apply all rules
              for (IFieldRule fieldRule : fieldRules) {

                // Validate all values using each rule
                for (String fieldValue : fieldValues) {
                  fieldRule.isValid(field.getId(), fieldValue, locale, errorMessages);
                }
              }
            }
          }
        }
      }
      catch (SecurityException securityException) {
        
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.validation");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Validating Page", securityException);
        
        throw new ProcessException(methodName + ":" + securityException.getMessage());
      }
    }

    return errorMessages;
  }

}
