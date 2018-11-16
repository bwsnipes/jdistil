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

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.message.Messages;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.process.model.SaveDataObject;
import com.bws.jdistil.core.security.Cryptographer;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.Descriptions;
import com.bws.jdistil.core.util.StringUtil;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.user.User;
import com.bws.jdistil.security.user.UserManager;

/**
  Saves a user.
  @author - Bryan Snipes
*/
public class SaveUser extends SaveDataObject<Integer, User> {

  /**
    Creates a new SaveUser object.
  */
  public SaveUser() {
    super(User.class, UserManager.class, FieldIds.USER_ID, AttributeNames.USER, 
        true, ViewUsers.class, EditUser.class, false);
  }

  /**
   * Saves a user.
   * @param processContext Process context.
   */
  @Override
	public void process(ProcessContext processContext) throws ProcessException {
		
		// Get current locale
		Locale locale = processContext.getRequest().getLocale();

  	// Get submitted user ID
		Integer userId = ParameterExtractor.getInteger(processContext.getRequest(), FieldIds.USER_ID);

  	// Get submitted password information
		String newPassword = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_NEW_PASSWORD);
		String confirmPassword = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD);

		if (userId == null) {
		
			if (StringUtil.isEmpty(newPassword)) {
				
	      // Get field
	      Field field = ConfigurationManager.getField(FieldIds.USER_AUTHENTICATION_NEW_PASSWORD);

	      // Get description
        String description = field.getDescription(locale);

        // Create error message
        String errorMessage = Messages.formatMessage(locale, Messages.REQUIRED_FIELD, description);

        // Add message to list
        processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, errorMessage));
			}

			if (StringUtil.isEmpty(confirmPassword)) {
				
	      // Get field
	      Field field = ConfigurationManager.getField(FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD);

	      // Get description
        String description = field.getDescription(locale);

        // Create error message
        String errorMessage = Messages.formatMessage(locale, Messages.REQUIRED_FIELD, description);

        // Add message to list
        processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, errorMessage));
			}
		}
		
		if (!StringUtil.isEmpty(newPassword) && !StringUtil.isEmpty(confirmPassword)) {
			
			if (!newPassword.equals(confirmPassword)) {
				
	      // Get mismatch password error message message
	      String errorMessage = Descriptions.getDescription("New password and confirmation password do not match.", locale);

	      // Create process error message and add to process context
				ProcessMessage processMessage = new ProcessMessage(ProcessMessage.ERROR, errorMessage);
				processContext.addMessage(processMessage);
				
				// Return to current page
				processContext.setNextPage(processContext.getCurrentPage());
			}
		}
		
		// Invoke inherited process to save user
		super.process(processContext);
	}
	
  @Override
	protected <I, T extends DataObject<I>> boolean saveDataObject(Class<? extends IDataManager<I, T>> dataManagerClass, T dataObject, 
	    boolean doDirtyUpdateCheck, String attributeId, ProcessContext processContext) 
	    throws ProcessException {
		
  	// Set method name
  	String methodName = "saveDataObject";
  	
  	// Cast to user
  	User user = (User)dataObject;
  	
  	// Get submitted password information
		String newPassword = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_NEW_PASSWORD);
		String confirmPassword = ParameterExtractor.getString(processContext.getRequest(), FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD);

		if (!StringUtil.isEmpty(newPassword) && !StringUtil.isEmpty(confirmPassword)) {
			
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
  		}
  		catch (Exception exception) {
  			
        Logger logger = Logger.getLogger("com.bws.jdistil.security.app.user");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Saving User", exception);
    
        throw new ProcessException(methodName + ":" + exception.getMessage());
  		}
		}
  	
		// Check domain admin permission
		checkDomainAdminPermission(user, processContext);
		
  	// Invoke inherited save method
  	@SuppressWarnings("unchecked")
		boolean isSuccess = super.saveDataObject(dataManagerClass, (T)user, doDirtyUpdateCheck, attributeId, processContext);
  	
		return isSuccess;
	}
	
  private void checkDomainAdminPermission(User user, ProcessContext processContext) throws ProcessException {
  	
  	String methodName = "checkDomainAdminPermission";
  	
  	try {
  		
      // Get current session
      HttpSession session = processContext.getRequest().getSession(true);
      
      // Get security manager
      ISecurityManager securityManager = processContext.getSecurityManager();
  		
  		if (securityManager == null || !securityManager.isDomainAdmin(session)) {
  			user.setIsDomainAdmin(false);
  		}
  	}
  	catch (SecurityException securityException) {
  		
      Logger logger = Logger.getLogger("com.bws.jdistil.security.app.user");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Saving User", securityException);
  
      throw new ProcessException(methodName + ":" + securityException.getMessage());
  	}
  }
}
