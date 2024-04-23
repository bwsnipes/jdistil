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

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.tag.basic.Component;

/**
  Component used to only display nested components if the current user is a domain admin.
  @author - Bryan Snipes
*/
public class DomainAccess extends Component {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = 8090329041495125456L;
	
	/**
	 * Indicates access is based the current user having domain admin privileges.
	 */
	private boolean isDomainAdminRequired = true;
	
	/**
	 * Indicates access is based on the user currently being in the default domain.
	 */
	private boolean isDefaultDomainRequired = true;
	
	/**
    Creates a new DomainAdmin object.
  */
  public DomainAccess() {
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
    
    if (isMultitenancyEnabled()) {
    	
      // Get session
      HttpSession session = pageContext.getSession();

      // Get security manager
      ISecurityManager securityManager = getSecurityManager();
      
      if (securityManager != null) {

        try {
        	
        	// Check for valid domain and privilege
        	if (isValidDomain(securityManager, session) && isValidPrivilege(securityManager, session)) {
      		
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
    }

    return bodyInstruction;
  }

  /**
   * Returns a value indicating whether or not multitenancy is enabled in the application.
   * @return boolean Multitenancy enabled indicator.
   */
  private boolean isMultitenancyEnabled() {
  	
  	// Get multitenancy indicator
  	String multitenancyEnabled = ResourceUtil.getString(Constants.MULTITENANCY_ENABLED);
  	boolean isMultitenancyEnabled = multitenancyEnabled != null && multitenancyEnabled.equalsIgnoreCase(Boolean.TRUE.toString());
  	
  	return isMultitenancyEnabled;
  }

  /**
   * Returns a value indicating whether or not the user has the domain admin privilege if required for access.
   * @param securityManager Security manager.
   * @param session HTTP session.
   * @return boolean Valid privilege indicator.
   * @throws SecurityException
   */
  private boolean isValidPrivilege(ISecurityManager securityManager, HttpSession session) throws SecurityException {
  
  	return !isDomainAdminRequired || securityManager.isDomainAdmin(session);
  }
  
  /**
   * Returns a value indicating whether or not the user is in default domain if required for access.
   * @param securityManager Security manager.
   * @param session HTTP session.
   * @return boolean Valid domain indicator.
   * @throws SecurityException
   */
  private boolean isValidDomain(ISecurityManager securityManager, HttpSession session) throws SecurityException {
  	
  	boolean isDomainAccessible = true;
  	
  	if (isDefaultDomainRequired) {
  		
			// Get current domain
			IDomain domain = securityManager.getDomain(session);

			// Check to ensure user is in the default domain
			isDomainAccessible = domain == null || domain.getId() == null || domain.getId().equals(IDomain.DEFAULT_ID);
  	}
  	
  	return isDomainAccessible;
	}
  	
	/**
	 * Sets indicator requiring the user to be in the default domain.
	 * @param isDefaultDomainRequired Required default domain indicator.
	 */
	public void setIsDefaultDomainRequired(boolean isDefaultDomainRequired) {
		this.isDefaultDomainRequired = isDefaultDomainRequired;
	}

  /**
   * Sets indicator requiring the user to have domain admin privileges.
   * @param isDomainAdminRequired Required domain admin privilege indicator. 
   */
	public void setIsDomainAdminRequired(boolean isDomainAdminRequired) {
		this.isDomainAdminRequired = isDomainAdminRequired;
	}
	
}
