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

import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Component used to write hidden data using an HTML hidden field.
  @author - Bryan Snipes
*/
public class HiddenField extends ValueComponent {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = 3719177591642642145L;

  /**
    Creates a new HiddenField object.
  */
  public HiddenField() {
    super();
  }

  /**
    Writes hidden data.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get field ID
    String fieldId = getFieldId();

    // Initialize hidden indicator
    boolean isHidden = isHidden(fieldId);

    try {
    	
      // Get formatted field value
      String fieldValue = StringUtil.convertNull(getFormattedFieldValue(), "");
      
    	// Mask field value if one exists
      if (isHidden && !StringUtil.isEmpty(fieldValue)) {
        fieldValue =  HIDDEN_VALUE;
      }
    	
      // Set value attribute
      setDynamicAttribute(null, "value", fieldValue);
      
      // Get attributes
      String attributes = buildAttributes().toString();

      // Write hidden field
      jspWriter.print("<input" + attributes + ">");
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Hidden Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Hidden Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return SKIP_BODY;
  }

  /**
    Returns a string buffer containing all attributes and their associated values.
    @return StringBuffer - Attribute values.
  */
  protected StringBuffer buildAttributes() throws JspException {

    // Get field ID
    String fieldId = StringUtil.convertNull(getInstanceFieldId());

    // Append attributes
    setDynamicAttribute(null, "type", "hidden");
    setDynamicAttribute(null, "id", fieldId);
    setDynamicAttribute(null, "name", fieldId);

    return super.buildAttributes();
  }

}
