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
package com.bws.jdistil.core.tag.data;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Component used to write selectable data using an HTML check box field.
  @author - Bryan Snipes
*/
public class CheckField extends ValueComponent {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -236522394784903865L;

  /**
    Creates a new CheckField object.
  */
  public CheckField() {
    super();
  }

  /**
    Writes selectable data using an HTML check box field.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get field ID
    String fieldId = getFieldId();

    // Initialize hidden and read only indicators
    boolean isHidden = isHidden(fieldId);
    boolean isReadOnly = isReadOnly(fieldId);

    try {

      if (isHidden || isReadOnly) {
      	
      	// Use text field for hidden and read only values
        setDynamicAttribute(null, "type", "text");

        // Set read only component property if hidden or read only field
        setDynamicAttribute(null, "readonly", "readonly");
        
        // Initialize field value as masked value
        String fieldValue = HIDDEN_VALUE;
        
        if (!isHidden) {
        	
          // Get formatted field value
          fieldValue = getFormattedFieldValue();
        }

        // Set value attribute
        setDynamicAttribute(null, "value", fieldValue);
      }
      else {
      	
      	// Use date field for editable values
        setDynamicAttribute(null, "type", "checkbox");
        
        // Get field value
        Object fieldValue = getFieldValue();

        // Set checked attribute
        if (fieldValue != null && fieldValue.equals(Boolean.TRUE)) {
          setDynamicAttribute(null, "checked", null);
        }
        
        // Get value representing checked state
        String checkedValue = getCheckedValue();

        // Set value attribute
        setDynamicAttribute(null, "value", checkedValue);
      }
      
      // Get attributes
      String attributes = buildAttributes().toString();

      // Write check field as HTML check box or text field
      jspWriter.print("<input" + attributes + "/>");
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Check Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Check Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return SKIP_BODY;
  }

  /**
    Returns a string buffer containing all attributes and their associated values.
    @see com.bws.jdistil.core.tag.basic.Component#buildAttributes
  */
  protected StringBuffer buildAttributes() throws JspException {

    // Get field ID
    String fieldId = StringUtil.convertNull(getInstanceFieldId());

    // Append attributes
    setDynamicAttribute(null, "id", fieldId);
    setDynamicAttribute(null, "name", fieldId);

    return super.buildAttributes();
  }

  /**
    Returns a value representing a checked state.
    @return String - Checked state value.
  */
  private String getCheckedValue() {

  	// Initialize value
  	Object value = Boolean.TRUE;

  	// Get field ID
  	String fieldId = getFieldId();
  	
    // Get field
    Field field = ConfigurationManager.getField(fieldId);

    if (field != null) {

    	// Get field type
    	Integer type = field.getType();
    	
    	if (!type.equals(Field.BOOLEAN)) {
    		
    		// Use field value if not boolean type
    		value = getFieldValue();
    	}
    }
    
  	// Initialize checked value
  	String checkedValue = getFormattedFieldValue(field, value);
  	
    return checkedValue;
  }

}
