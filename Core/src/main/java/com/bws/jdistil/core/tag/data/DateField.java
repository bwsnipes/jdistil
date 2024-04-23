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

import com.bws.jdistil.core.conversion.DateConverter;
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Component used to write editable data using an HTML date field.
  @author - Bryan Snipes
*/
public class DateField extends ValueComponent {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = 1481524823400665221L;

	/**
    Creates a new DateField object.
  */
  public DateField() {
    super();
  }

  /**
    Writes editable data using an HTML date field.
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

    	// Initialize field value
    	String fieldValue = null;
    	
      if (isHidden || isReadOnly) {
      	
      	// Use text field for hidden and read only values
        setDynamicAttribute(null, "type", "text");

        // Set read only component property if hidden or read only field
        setDynamicAttribute(null, "readonly", "readonly");

        // Get formatted field value
        fieldValue = StringUtil.convertNull(getFormattedFieldValue(), "");
        
      	// Mask field value if one exists
        if (isHidden && !StringUtil.isEmpty(fieldValue)) {
          fieldValue =  HIDDEN_VALUE;
        }
      }
      else {
      	
      	// Use date field for editable values
        setDynamicAttribute(null, "type", "date");
        
        // Get standard formatted field value
        fieldValue = StringUtil.convertNull(getStandardFormattedFieldValue(), "");
      }
      
      // Set value attribute
      setDynamicAttribute(null, "value", fieldValue);

      // Get attributes
      String attributes = buildAttributes().toString();

      // Write date field as HTML date or text field
      jspWriter.print("<input" + attributes + "/>");
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Date Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Date Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return SKIP_BODY;
  }

  /**
    Returns the field value formatted in standard date format.
    @return String Date formatted in standard date format.
  */
  private String getStandardFormattedFieldValue() {
  	
  	DateConverter dateConverter = DateConverter.getInstance();
  	
  	return dateConverter.formatAsStandard(getFieldValue());
  }
  
  /**
    Returns a string buffer containing all attributes and their associated values.
    @see com.bws.jdistil.core.tag.basic.Component#buildAttributes
  */
  protected StringBuffer buildAttributes() throws JspException {

    // Get field ID
    String fieldId = StringUtil.convertNull(getInstanceFieldId());

    // Set attributes
    setDynamicAttribute(null, "id", fieldId);
    setDynamicAttribute(null, "name", fieldId);

    return super.buildAttributes();
  }

}
