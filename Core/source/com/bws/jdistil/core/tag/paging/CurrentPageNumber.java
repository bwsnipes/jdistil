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
package com.bws.jdistil.core.tag.paging;

import com.bws.jdistil.core.configuration.AttributeNames;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Component used to display the current page number.
  @author - Bryan Snipes
*/
public class CurrentPageNumber extends TagSupport {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -6095264601174676086L;

  /**
    Creates a new CurrentPageNumber object.
  */
  public CurrentPageNumber() {
    super();
  }

  /**
    Writes the current page number.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Attempt to retrieve current page number from request attributes
    Integer currentPageNumber = (Integer)pageContext.getRequest().getAttribute(AttributeNames.CURRENT_PAGE_NUMBER);

    // Default to zero if not found
    if (currentPageNumber == null) {
      currentPageNumber = Integer.valueOf(0);
    }

    // Get number formatter based on current locale
    Locale locale = pageContext.getRequest().getLocale();
    NumberFormat numberFormatter = NumberFormat.getIntegerInstance(locale);
    
    try {
      // Write total pages
      jspWriter.print(numberFormatter.format(currentPageNumber));
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.paging");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Total Pages", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return SKIP_BODY;
  }

}
