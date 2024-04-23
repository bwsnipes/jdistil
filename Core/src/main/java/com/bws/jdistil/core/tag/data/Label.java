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

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.tag.basic.Component;
import com.bws.jdistil.core.tag.basic.Form;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Label component used to display field names.
  @author - Bryan Snipes
*/
public class Label extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -6894086475146191508L;

  /**
    Field ID.
  */
  private String fieldId = null;

  /**
    Creates a new Label object.
  */
  public Label() {
    super();
  }

  /**
    Sets the field ID.
    @param fieldId - Field Id.
  */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  /**
    Writes a label using an HTML label.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get attributes
    String attributes = buildAttributes().toString();

    // Get request
    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

    // Initialize value to field ID
    String value = fieldId;

    // Get field configuration
    Field field = ConfigurationManager.getField(fieldId);

    // Set value
    if (field != null) {
      value = field.getDescription(request.getLocale());
    }

    // Append required field
    if (isRequiredField()) {
      value = value + "*";
    }
    
    try {
      // Write label as HTML label
      jspWriter.print("<label" + attributes + ">");
      jspWriter.print(StringUtil.convertNull(value));
      jspWriter.print("</label>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Label", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return SKIP_BODY;
  }

  /**
    Returns a value indicating whether or not the associated field is required.
    @return boolean - Required field indicator.
  */
  private boolean isRequiredField() {
  
    // Initialize return value
    boolean isRequiredField = false;
    
    // Check for enclosing form component
    Form form = (Form)findAncestorWithClass(this, Form.class);

    if (form != null) {

      // Get action
      Action action = ConfigurationManager.getAction(form.getActionId());

      // Set required field indicator
      isRequiredField = action.isFieldRequired(fieldId);
    }
  
    return isRequiredField;
  }
  
}
