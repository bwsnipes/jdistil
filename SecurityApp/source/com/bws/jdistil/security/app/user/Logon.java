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
package com.bws.jdistil.security.app.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.core.security.Cryptographer;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.Descriptions;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.domain.DomainManager;
import com.bws.jdistil.security.role.Role;
import com.bws.jdistil.security.role.RoleManager;
import com.bws.jdistil.security.user.User;
import com.bws.jdistil.security.user.UserManager;

/**
  Handles user logon.
  @author Bryan Snipes
*/
public class Logon extends Processor {

  /**
    Creates a new Logon object.
  */
  public Logon() {
    super();
  }

  /**
   * Handles user logon.
   * @param processContext Process context.
   */
  @Override
	public void process(ProcessContext processContext) throws ProcessException {
		
		// Set method name
		String methodName = "process";

		// Check for errors before attempting logon
    if (!processContext.getErrorMessages().isEmpty()) {
    	
			// Return to current page
			processContext.setNextPage(processContext.getCurrentPage());
    }
    else {

    	// Initialize valid logon indicator
    	boolean isValidLogon = false;
    	
    	// Get submitted logon ID and password
  		String logonId = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_ID);
  		String password = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_PASSWORD);
  		Integer domainId = ParameterExtractor.getInteger(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_DOMAIN_ID);
  		
  		// Initialize target domain
  		IDomain domain = null;
  		
  		if (domainId != null) {

  			// Retrieve selected domain
  			domain = findDataObject(DomainManager.class, domainId, processContext);
  		}

  		// Retrieve user by logon ID
  		User user = findUser(logonId, domain);
  		
  		if (user != null) {
  			
  			try {
  				
    			// Get existing salt value
    			String salt = user.getSalt();
    			byte[] saltBytes = DatatypeConverter.parseBase64Binary(salt);

    			// Encrypt submitted password
    			SecretKey secretKey = Cryptographer.createSecretKey(password, saltBytes, 10000);
    			String encryptedPassword = DatatypeConverter.printBase64Binary(secretKey.getEncoded());
    			
    			// Set valid logon indicator
    			isValidLogon = user.getPassword().equals(encryptedPassword);
  			}
  			catch (Exception exception) {
  				
  	      Logger logger = Logger.getLogger("com.bws.jdistil.security.app.user");
  	      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Validating User Credentails", exception);
  	  
  	      throw new ProcessException(methodName + ":" + exception.getMessage());
  			}
  		}
  		
  		if (isValidLogon) {
  			
  			// Get current session
  			HttpSession session = processContext.getRequest().getSession(true);
  			
  			// Store user in session
  			session.setAttribute(AttributeNames.USER, user);
  			
  			// Get role IDs
  			List<Integer> roleIds = user.getRoleIds();
  			
  			if (roleIds != null && !roleIds.isEmpty()) {
  				
  				// Create object based list
  				List<Integer> valueIds = new ArrayList<Integer>(roleIds.size());
  				
  				// Populate object based list
  				for (Integer roleId : roleIds) {
  					valueIds.add(roleId);
  				}
  				
  				// Retrieve roles
  				List<Role> roles = findDataObjects(RoleManager.class, valueIds, processContext);
  				
  				if (roles != null && !roles.isEmpty()) {
  					
      			// Store user in session
      			session.setAttribute(AttributeNames.ROLES, roles);
  				}
  			}
  			
  			// Handle successful logon
  			handleSuccess(processContext);
  		}
  		else {
  			
  			// Get current locale
  			Locale locale = processContext.getRequest().getLocale();
  			
        // Get logon failed message message
        String errorMessage = Descriptions.getDescription("Logon failed.", locale);

        // Create process error message and add to process context
  			ProcessMessage processMessage = new ProcessMessage(ProcessMessage.ERROR, errorMessage);
  			processContext.addMessage(processMessage);
  			
  			// Forward to view logon process
  			forward(ViewLogon.class, processContext);
  		}
    }
	}

	/**
	 * Default implementation for handling a successful logon which can be overriden by descendant classes.
	 * Navigates to the application defined welcome page.
	 * @param processContext Process context.
	 */
	protected void handleSuccess(ProcessContext processContext) throws ProcessException {
		
		// Get welcome action
		Action welcomeAction = ConfigurationManager.getWelcomeAction();
		
		if (welcomeAction != null) {

			// Forward processing to welcome action
			forward(welcomeAction, processContext);
		}
		else {
			
			throw new ProcessException("Error navigating to welcome page: No welcome page action defined.");
		}
	}
	
	/**
	 * Retrieves a user based on a logon ID.
	 * @param logonId Logon ID.
	 * @param domain Target domain.
	 * @return User User data object.
	 */
	private User findUser(String logonId, IDomain domain) throws ProcessException {
		
		// Set method name
		String methodName = "findUser";
		
		// Initialize return value
		User user = null;
		
    // Get user manager factory
    IFactory userManagerFactory = ConfigurationManager.getFactory(UserManager.class);
  
    // Create user manager
    UserManager userManager = (UserManager)userManagerFactory.create();

    try {
      // Attempt to retrieve user
      user = userManager.findByLogonId(logonId, domain);
    }
    catch (DataSourceException dataSourceException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.security.app.user");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding User By Logon ID", dataSourceException);
  
      throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
    }
    finally {
  
      // Recycle data manager
      userManagerFactory.recycle(userManager);
    }
		
    return user;
	}

}
