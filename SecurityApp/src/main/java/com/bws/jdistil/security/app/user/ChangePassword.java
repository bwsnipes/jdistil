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

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
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
import com.bws.jdistil.security.user.User;
import com.bws.jdistil.security.user.UserManager;

/**
  Handles changing a user's password.
  @author Bryan Snipes
*/
public class ChangePassword extends Processor {

  /**
    Creates a new ChangePassword object.
  */
  public ChangePassword() {
    super();
  }

  /**
   * Handles changing a user's password.
   * @param processContext Process context.
   */
  @Override
	public void process(ProcessContext processContext) throws ProcessException {
		
		// Set method name
		String methodName = "process";
		
    // Check for errors before changing password
    if (!processContext.getErrorMessages().isEmpty()) {
    	
			// Return to current page
			processContext.setNextPage(processContext.getCurrentPage());
    }
    else {
    	
			// Get current locale
			Locale locale = processContext.getRequest().getLocale();

			// Get submitted password information
  		String password = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_PASSWORD);
  		String newPassword = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_NEW_PASSWORD);
  		String confirmPassword = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD);

  		if (!newPassword.equals(confirmPassword)) {
  			
        // Get mismatch password error message message
        String errorMessage = Descriptions.getDescription("New password and confirmation password do not match.", locale);

        // Create process error message and add to process context
  			ProcessMessage processMessage = new ProcessMessage(ProcessMessage.ERROR, errorMessage);
  			processContext.addMessage(processMessage);
  			
  			// Return to current page
  			processContext.setNextPage(processContext.getCurrentPage());
  		}
  		else {
  			
      	// Initialize valid logon indicator
      	boolean isValidLogon = false;

      	// Get current session
  			HttpSession session = processContext.getRequest().getSession(true);
  			
  			// Get user in session
  			User user = (User)session.getAttribute(AttributeNames.USER);
  			
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
    	      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Changing User Password", exception);
    	  
    	      throw new ProcessException(methodName + ":" + exception.getMessage());
    			}
  			}

  			if (isValidLogon) {
  				
	  	    // Get user manager factory
	  	    IFactory userManagerFactory = ConfigurationManager.getFactory(UserManager.class);
	  	  
	  	    // Create user manager
	  	    UserManager userManager = (UserManager)userManagerFactory.create();

	  	    // Get current domain
	  	    IDomain domain = getCurrentDomain(processContext);
	  	    
	  	    try {
	  	    	
    				// Create salt value
    				byte[] saltBytes = Cryptographer.createSalt();
    				String salt = DatatypeConverter.printBase64Binary(saltBytes);
    				
    				// Encrypt submitted password
    				SecretKey secretKey = Cryptographer.createSecretKey(newPassword, saltBytes, 10000);
    				String encryptedPassword = DatatypeConverter.printBase64Binary(secretKey.getEncoded());
  
    		  	// Set salt and password
    		  	user.setSalt(salt);
    		  	user.setPassword(encryptedPassword);
  
	  	      // Save user
	  	      userManager.save(user, domain);
	    			
	    			// Handle successful logon
	    			handleSuccess(processContext);
	  	    }
	  	    catch (Exception exception) {
	  	  
	  	      // Post error message
	  	      Logger logger = Logger.getLogger("com.bws.jdistil.security.app.user");
	  	      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Changing User Password", exception);
	  	  
	  	      throw new ProcessException(methodName + ":" + exception.getMessage());
	  	    }
	  	    finally {
	  	  
	  	      // Recycle data manager
	  	      userManagerFactory.recycle(userManager);
	  	    }
  			}
  			else {

		  		// Get incorrect password error message message
          String errorMessage = Descriptions.getDescription("Invalid logon.", locale);

          // Create process error message and add to process context
    			ProcessMessage processMessage = new ProcessMessage(ProcessMessage.ERROR, errorMessage);
    			processContext.addMessage(processMessage);
    			
    			// Return to current page
    			processContext.setNextPage(processContext.getCurrentPage());
  			}
  		}
    }
	}

	/**
	 * Default implementation for handling a successful change password which can be overridden by descendant classes.
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
	
}
