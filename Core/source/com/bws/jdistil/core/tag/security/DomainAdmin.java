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
package com.bws.jdistil.core.tag.security;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.tag.basic.Component;

/**
  Component used to only display nested components if the application is configured
  for multiple tenants and the current user is a domain admin.
  @author - Bryan Snipes
*/
public class DomainAdmin extends Component {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = 8090329041495125456L;

	/**
    Creates a new DomainAccess object.
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
    		// Determine if application is configured to support multiple tenants
        String multipleTenants = ResourceUtil.getString(Constants.MULTIPLE_TENANTS);
    		boolean isMultipleTenantsSupported = multipleTenants != null && multipleTenants.equalsIgnoreCase(Boolean.TRUE.toString());
    		
    		// Domain admin field is only accessible if the application is configured to support multiple tenants
    		// and the current user is a domain admin. This field is not included in the list of fields available when defining roles.
    		if (isMultipleTenantsSupported && securityManager.isDomainAdmin(session)) {
    			
    			bodyInstruction = EVAL_BODY_INCLUDE;
    		}
      }
      catch (SecurityException securityException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Checking Domain Access", securityException);

        throw new JspException(methodName + ":" + securityException.getMessage());
      }
    }

    return bodyInstruction;
  }

}
