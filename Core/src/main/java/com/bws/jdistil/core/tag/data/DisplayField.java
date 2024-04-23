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
  Component used to display read-only data.
  @author - Bryan Snipes
*/
public class DisplayField extends ValueComponent {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 7547070070773467929L;

  /**
    Creates a new DisplayField object.
  */
  public DisplayField() {
    super();
  }

  /**
    Writes read-only data.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();
    
    // Get field ID
    String fieldId = getFieldId();

    // Initialize read only indicator
    boolean isHidden = isHidden(fieldId);

    try {

      if (isHidden) {
  
      	// Mask field value if one exists
      	String value = getFieldValue() == null ? "" : HIDDEN_VALUE;

      	// Write hidden value
        jspWriter.print(value);
      }
      else {
        
        // Get formatted field value
        String fieldValue = StringUtil.convertNull(getFormattedFieldValue(), "&nbsp;");

        // Get attributes
        String attributes = buildAttributes().toString();

        if (StringUtil.isEmpty(attributes)) {

          // Write display field as plain text
          jspWriter.print(fieldValue);
        }
        else {

          // Write display field as HTML span
          jspWriter.print("<span" + attributes + ">" + fieldValue + "</span>");
        }
      }    
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Display Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Display Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }

    return SKIP_BODY;
  }

}
