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
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

/**
  Link component used to perform some action.
  @author - Bryan Snipes
*/
public class Link extends ActionComponent implements BodyTag {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -407772921056074524L;

  /**
    Body content.
  */
  private BodyContent bodyContent = null;

  /**
    Creates a new Link object.
  */
  public Link() {
    super();
  }

  /**
    Initializes the body.
  */
  public void doInitBody() throws JspException {

    // Do nothing
  }

  /**
    Sets the body content.
    @param bodyContent - Body content.
  */
  public void setBodyContent(BodyContent bodyContent) {
    this.bodyContent = bodyContent;
  }

  /**
    Writes a link using an HTML anchor.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get attributes
    String attributes = buildAttributes().toString();

    // Check for disabled attribute
    String disabledAttribute = (String)getDynamicAttribute("disabled");
    
    if (disabledAttribute == null || !disabledAttribute.equals("true")) {
    
      // Get JSP writer
      JspWriter jspWriter = pageContext.getOut();

      try {
        // Initialize body text
        String bodyText = null;

        if (bodyContent != null) {

          // Get body text
          bodyText = StringUtil.convertNull(bodyContent.getString());

          // Clear body content buffer
          bodyContent.clearBody();
        }

        if (StringUtil.isEmpty(bodyText)) {

          // Write action description as HTML anchor link
          jspWriter.print("<a href=\"#\"" + attributes + ">");
          jspWriter.print(StringUtil.convertNull(getActionDescription()));
          jspWriter.print("</a>");
        }
        else {

          // Write body text as HTML anchor link
          jspWriter.print("<a href=\"#\"" + attributes + ">");
          jspWriter.print(bodyText);
          jspWriter.print("</a>");
        }
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.action");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Link", ioException);

        throw new JspException(methodName + ":" + ioException.getMessage());
      }
    }
    
    return EVAL_PAGE;
  }

  /**
    Cleans up resources each time tag is used.
    @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
  */
  public void doFinally() {
  
    super.doFinally();
    
    // Clear body content
    bodyContent = null;
  }

}
