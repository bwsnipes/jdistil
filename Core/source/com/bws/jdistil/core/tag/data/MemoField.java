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
  Component used to write editable data using an HTML text area.
  @author - Bryan Snipes
*/
public class MemoField extends ValueComponent {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = -4105676674682312444L;

	/**
    Creates a new MemoField object.
  */
  public MemoField() {
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

      // Set read only component property if hidden or read only field
      if (isReadOnly || isHidden) {
      	setDynamicAttribute(null, "readonly", "readonly");
      }

      // Get formatted field value
      String fieldValue = StringUtil.convertNull(getFormattedFieldValue(), "");
      
    	// Mask field value if one exists
      if (isHidden && !StringUtil.isEmpty(fieldValue)) {
        fieldValue =  HIDDEN_VALUE;
      }
    	
      // Get attributes
      String attributes = buildAttributes().toString();

      // Write memo field as HTML text area
      jspWriter.print("<textarea" + attributes + ">" + fieldValue + "</textarea>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Memo Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Memo Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
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

    // Set attributes
    setDynamicAttribute(null, "id", fieldId);
    setDynamicAttribute(null, "name", fieldId);

    return super.buildAttributes();
  }

}
