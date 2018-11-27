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
package com.bws.jdistil.security.tag;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.tag.basic.Component;

/**
  Component used to only display nested components if the current user is a domain admin.
  @author - Bryan Snipes
*/
public class DomainAdmin extends Component {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = 8090329041495125456L;

	/**
    Creates a new DomainAdmin object.
  */
  public DomainAdmin() {
    super();
  }

	/**
    Writes an HTML body.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";
    
    // Initialize body instruction
    int bodyInstruction = SKIP_BODY;
    
    // Get session
    HttpSession session = pageContext.getSession();

    // Get security manager
    ISecurityManager securityManager = getSecurityManager();
    
    if (securityManager != null) {

      try {

    		if (securityManager.isDomainAdmin(session)) {
    			
    			// Get current domain
    			IDomain domain = securityManager.getDomain(session);

    			// Check to ensure user is in the default domain
    			if (domain == null || domain.getId() == null || domain.getId().equals(IDomain.DEFAULT_ID)) {
    				
      			bodyInstruction = EVAL_BODY_INCLUDE;
    			}
    		}
      }
      catch (SecurityException securityException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Checking Domain Admin Access", securityException);

        throw new JspException(methodName + ":" + securityException.getMessage());
      }
    }

    return bodyInstruction;
  }

}
