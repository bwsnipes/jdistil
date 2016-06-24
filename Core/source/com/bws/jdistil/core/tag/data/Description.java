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

import com.bws.jdistil.core.util.Descriptions;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Writes a locale specific description for a given value.
  @author Bryan Snipes
*/
public class Description extends TagSupport {

  /**
    Serial version UID.
  */
	private static final long serialVersionUID = 3604015532278036300L;

  /**
    Value to describe.
  */
  private String value = null;

	/**
    Creates a new Description object.
  */
  public Description() {
     super();
  }

  /**
    Writes a locale specific description for a given value.
    @see javax.servlet.jsp.tagext.TagSupport#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get current locale
    Locale locale = pageContext.getRequest().getLocale();

    // Attempt to translate value to a locale specific description
    value = Descriptions.getDescription(value, locale);

    try {
      // Get JSP writer
      JspWriter jspWriter = pageContext.getOut();

      jspWriter.print(value);
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Rendering Description", ioException);
    }

    return SKIP_BODY;
  }

  /**
    Returns value indicating to evaluate the rest of the page.
    @see javax.servlet.jsp.tagext.TagSupport#doEndTag
  */
  public int doEndTag() {
    return EVAL_PAGE;
  }

  /**
   * Sets the value to describe.
   * @param value Value to describe.
   */
	public void setValue(String value) {
		this.value = value;
	}

}
