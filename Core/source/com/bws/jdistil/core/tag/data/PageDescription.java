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

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Page;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Component used to display a page description.
  @author - Bryan Snipes
*/
public class PageDescription extends TagSupport {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = -5908217860281322112L;

	/**
	 * Page ID.
	 */
	private String pageId = null;
	
	/**
    Creates a new PageDescription object.
  */
  public PageDescription() {
    super();
  }

	/**
	  Sets the page ID.
	  @param pageId Page ID.
	*/
  public void setPageId(String pageId) {
  	this.pageId = pageId;
  }
  
  /**
    Writes the page description.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get page from configuration
    Page page = ConfigurationManager.getPage(pageId);
    
    if (page != null) {
    	
      // Get JSP writer
      JspWriter jspWriter = pageContext.getOut();

      try {
        // Get current locale
        Locale locale = pageContext.getRequest().getLocale();

        // Write total items
        jspWriter.print(page.getDescription(locale));
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Displaying Page Description", ioException);

        throw new JspException(methodName + ":" + ioException.getMessage());
      }
    }
    
    return SKIP_BODY;
  }

}
