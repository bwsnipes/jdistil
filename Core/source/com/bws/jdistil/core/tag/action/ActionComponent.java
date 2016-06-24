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
package com.bws.jdistil.core.tag.action;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.message.Messages;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.tag.basic.Component;
import com.bws.jdistil.core.tag.basic.Form;
import com.bws.jdistil.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

/**
  Abstract base component containing attributes common to all action UI components.
  @author - Bryan Snipes
*/
public abstract class ActionComponent extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 9097707398525383L;

  /**
    Action ID.
  */
  private String actionId = null;

  /**
    Confirmation message key.
  */
  private String confirmationMessageKey = null;
  
  /**
    List of registered field data objects.
  */
  private List<FieldData> registeredFieldData = new ArrayList<FieldData>();

  /**
    Creates a new ActionComponent object.
  */
  public ActionComponent() {
    super();
  }

  /**
    Returns the action ID.
    @return String - Action ID.
  */
  public String getActionId() {
    return actionId;
  }

  /**
    Sets the submit action ID.
    @param actionId - Action ID.
  */
  public void setActionId(String actionId) {
    this.actionId = actionId;
  }

  /**
    Sets the confirmation message key.
    @param newConfirmationMessageKey - New confirmation message key.
  */
  public void setConfirmationMessageKey(String newConfirmationMessageKey) {
    confirmationMessageKey = newConfirmationMessageKey;
  }
  
  /**
    Cleans up resources each time tag is used.
    @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
  */
  public void doFinally() {
  
    super.doFinally();

    // Reset attributes
    actionId = null;
    confirmationMessageKey = null;
    
    // Clear action data
    registeredFieldData.clear();
  }
  
  /**
    Returns the action description.
    @return String - Action description.
  */
  protected String getActionDescription() {

    // Initialize return value
    String description = null;

    // Get current locale
    Locale locale = pageContext.getRequest().getLocale();

    // Get action
    Action action = ConfigurationManager.getAction(getActionId());

    if (action != null) {
      description = action.getDescription(locale);
    }

    return description;
  }

  /**
    Registers action data.
    @param actionData - Acion data.
    @throws javax.servlet.jsp.JspException
  */
  protected void addActionData(ActionData actionData) throws JspException {

    if (actionData != null) {
      
      // Set method name
      String methodName = "addActionData";
      
      try {
        // Get field information
        String fieldId = actionData.getFieldId();
        String fieldValue = actionData.getFormattedFieldValue();

        // Registered field data
        registeredFieldData.add(new FieldData(fieldId, fieldValue));
      }
      catch (UiException uiException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.action");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Action Data", uiException);

        throw new JspException(methodName + ":" + uiException.getMessage());
      }
    }
  }

  /**
    Returns a string buffer containing all attributes and their associated values.
    @see com.bws.jdistil.core.tag.basic.Component#buildAttributes
  */
  protected StringBuffer buildAttributes() throws JspException {

    // Set method name
    String methodName = "buildAttributes";

    // Get security manager
    ISecurityManager securityManager = getSecurityManager();

    if (securityManager != null) {

      // Get session
      HttpSession session = pageContext.getSession();

      try {

        // Override disabled attribute if not authorized
        if (!securityManager.isAuthorized(actionId, session)) {
          setDynamicAttribute(null, "disabled", "true");
        }
      }
      catch (SecurityException securityException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.action");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Action Component", securityException);
      }
    }

    // Check for disabled attribute
    String disabledAttribute = (String)getDynamicAttribute("disabled");
    
    if (disabledAttribute == null || !disabledAttribute.equals("true")) { 
    	
      // Initialize form ID
      String formId = "";
      
      // Check for enclosing form
      Form form = (Form)findAncestorWithClass(this, Form.class);
      
      // Attempt to get form ID if an enclosing form is found
      if (form != null) {
        formId = StringUtil.convertNull(form.getDynamicAttribute("id"));
      }
      
      // Trim form ID
      formId = formId.trim();
      
      // Initialize functions buffer
      StringBuffer functions = new StringBuffer();

      if (registeredFieldData.size() > 0) {

        // Initialize
        for (FieldData fieldData : registeredFieldData) {

          // Get field ID and field value
          String fieldId = fieldData.getFieldId();
          String fieldValue = fieldData.getFieldValue();

          // Append action value
          functions.append("javascript:setValue('" + formId + "', '" + fieldId + "', '" + fieldValue + "');");
        }
      }

      // Get confirmation message
      String confirmationMessage = null;
      
      // Lookup confirmation message
      if (!StringUtil.isEmpty(confirmationMessageKey)) {
        Locale locale = pageContext.getRequest().getLocale();
        confirmationMessage = Messages.formatMessage(locale, confirmationMessageKey, null);
      }
      
      // Convert null value to an empty string
      confirmationMessage = StringUtil.convertNull(confirmationMessage).trim();
      
      // Append submit action last
      functions.append("javascript:submitAction('" + formId + "', '" + actionId + "', '" + confirmationMessage + "');return false;");

      // Set onClick attribute using functions
      setDynamicAttribute(null, "onClick", functions.toString());
    }

    return super.buildAttributes();
  }

/**
  Inner class used to store registered field information.
*/
 private class FieldData {
   
   /**
     Field ID.
   */
   private String fieldId = null;
   
   /**
     Field value.
   */
   private String fieldValue = null;
   
   /**
     Creates a new FieldData object using a field ID and field value.
     @param fieldId - Field ID.
     @param fieldValue - Field value.
   */
   public FieldData(String fieldId, String fieldValue) {
     super();
     
     // Set properties
     this.fieldId = fieldId;
     this.fieldValue = fieldValue;
   }
   
   /**
     Returns the field ID.
     @return String - Field ID.
   */
   public String getFieldId() {
     return fieldId;
   }
   
   /**
     Returns the field value.
     @return String - Field value.
   */
   public String getFieldValue() {
     return fieldValue;
   }
   
 }
 
}
