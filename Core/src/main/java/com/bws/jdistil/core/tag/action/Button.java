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

import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Button component used to perform some action.
  @author - Bryan Snipes
*/
public class Button extends ActionComponent {

  /**	
   * Serial version UID.
   */
  private static final long serialVersionUID = -8764612872531575664L;

  /**
    Creates a new Button object.
  */
  public Button() {
    super();
  }

  /**
    Writes a button using an HTML button.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get attributes
    String attributes = buildAttributes().toString();

    try {
      // Write button as HTML button
      jspWriter.print("<input" + attributes + "/>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.action");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Button", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_PAGE;
  }

  /**
    Returns a string buffer containing all attributes and their associated values.
    @see com.bws.jdistil.core.tag.basic.Component#buildAttributes
  */
  protected StringBuffer buildAttributes() throws JspException {

    if (!isAttributeDefined("value")) {

      // Get action description
      String description = getActionDescription();

      // Set value using action description
      if (!StringUtil.isEmpty(description)) {
        setDynamicAttribute(null, "value", description);
      }
    }

    // Set button type based on an image source
    if (isAttributeDefined("src")) {
      setDynamicAttribute(null, "type", "image");
    }
    else {
      setDynamicAttribute(null, "type", "button");
    }

    return super.buildAttributes();
  }

}
