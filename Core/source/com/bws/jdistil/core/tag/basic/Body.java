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
package com.bws.jdistil.core.tag.basic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;

/**
  Body component used to encapsulate other components.
  @author - Bryan Snipes
*/
public class Body extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 4353699036392276991L;

  /**
    Page ID.
  */
  private String pageId = null;

  /**
    Creates a new Body object.
  */
  public Body() {
    super();
  }

  /**
    Returns the page ID.
    @return String - Page ID.
  */
  public String getPageId() {
    return pageId;
  }
  
  /**
    Sets the page ID.
    @param newPageId - New page ID.
  */
  public void setPageId(String newPageId) {
    pageId = newPageId;
  }

  /**
    Writes an HTML body.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get current page
    Page page = ConfigurationManager.getPage(pageId);
    
    if (page != null && page.isSecure()) {
    	
      // Get session
      HttpSession session = pageContext.getSession();

      // Get security manager
      ISecurityManager securityManager = getSecurityManager();

      try {
      	
        if (!securityManager.isAuthenticated(session)) {
        	
        	// Get logon page
        	Page logonPage = ConfigurationManager.getLogonPage();
        	
        	if (logonPage != null) {
        		
        		// Forward to logon page
        		pageContext.forward(logonPage.getName());
        	}
        	else {
        		
        		// Set error message
        		String errorMessage = "Building Page Body: Unable to forward to logon page because it is undefined.";
        		
            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
            logger.logp(Level.SEVERE, getClass().getName(), methodName, errorMessage);

            throw new JspException(methodName + ":" + errorMessage);
        	}
        }
      }
      catch (SecurityException securityException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Page Body", securityException);

        throw new JspException(methodName + ":" + securityException.getMessage());
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Page Body", ioException);

        throw new JspException(methodName + ":" + ioException.getMessage());
      }
      catch (ServletException servletException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Page Body", servletException);

        throw new JspException(methodName + ":" + servletException.getMessage());
      }
    }

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get attributes
    String attributes = buildAttributes().toString();

    try {
      // Write HTML body
      jspWriter.println("<body" + attributes + ">");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Body", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
    Completes the body output.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    try {
      
      // Write end of body
      jspWriter.println("</body>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Body", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_PAGE;
  }

}
