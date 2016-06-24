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

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.bws.jdistil.core.tag.basic.Component;
import com.bws.jdistil.core.util.Descriptions;

/**
  Menu component used to display a menu and its associated as menu items.
  MenuBar, Menu, and MenuItem components are used together to create application menus
  aligned to the CSS styles defined in the core framework.
  @author - Bryan Snipes
*/
public class Menu extends Component {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = -2524394138711880110L;

  /**
   * Menu title.
   */
  private String title = null;
  
  /**
    Creates a new Menu object.
  */ 
  public Menu() {
    super();
  }

  /**
   * Sets the menu title.
   * @param title
   */
  public void setTitle(String title) {
		this.title = title;
	}

	/**
    Writes the start of a menu component.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get current locale
    Locale locale = pageContext.getRequest().getLocale();
    
    // Attempt to translate title to a locale specific description
    title = Descriptions.getDescription(title, locale);
    
    try {
        // Write start of menu
        jspWriter.println("<li>");
        jspWriter.println("  <a href='#'>" + title + "</a>");
        jspWriter.println("  <ul>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.action");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Start Menu", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
    Writes the end of a menu component.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    try {
      // Write end of menu
      jspWriter.println("  </ul>");
      jspWriter.println("</li>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.action");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing End Menu", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    
    return EVAL_PAGE;
  }

}
