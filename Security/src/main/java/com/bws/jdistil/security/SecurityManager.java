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
package com.bws.jdistil.security;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.security.DefaultSecurityManager;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.role.Action;
import com.bws.jdistil.security.role.ActionManager;
import com.bws.jdistil.security.role.Field;
import com.bws.jdistil.security.role.FieldManager;
import com.bws.jdistil.security.role.Role;
import com.bws.jdistil.security.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

/**
  Security manager implementation.
  @author Bryan Snipes
*/
public class SecurityManager extends DefaultSecurityManager {

  /**
    Secured actions.
  */
  private static final Map<String, Action> securedActions = new HashMap<String, Action>();
  
  /**
    Secured fields.
  */
  private static final Map<String, Field> securedFields = new HashMap<String, Field>();
  
  static {
    
    // Set method name
    String methodName = "<initializer>";
    
    try {
      // Load secured actions and fields
      loadSecuredActions();
      loadSecuredFields();
    }
    catch (DataSourceException dataSourceException) {
      
      // Post error message
      Logger logger = Logger.getLogger("com.bws.security");
      logger.logp(Level.SEVERE, "SecurityManager", methodName, "Initializing Security Manager", dataSourceException);
  
      throw new ExceptionInInitializerError(methodName + ":" + dataSourceException.getMessage());
    }
  }
  
  /**
    Creates a new SecurityManager.
  */
  public SecurityManager() {
    super();
  }

  /**
    Retrieves all secured actions.
    @throws DataSourceException
  */
  private static void loadSecuredActions() throws DataSourceException {
    
    // Get action manager factory
    IFactory actionManagerFactory = ConfigurationManager.getFactory(ActionManager.class);
    
    // Create action manager
    ActionManager actionManager = (ActionManager)actionManagerFactory.create();

    try {
    	
      // Retrieve all actions   
      List<Action> actions = actionManager.find();
      
      if (actions != null) {
        
        for (Action action : actions) {
        	
          // Add action keyed by secure ID
          securedActions.put(action.getSecureId(), action);
        }
      }
    }
    finally {

    	// Recycle action manager
    	actionManagerFactory.recycle(actionManager);
    }
  }
  
  /**
    Retrieves all secured fields.
    @throws DataSourceException
  */
  private static void loadSecuredFields() throws DataSourceException {
    
    // Get field manager factory
    IFactory fieldManagerFactory = ConfigurationManager.getFactory(FieldManager.class);
    
    // Create field manager
    FieldManager fieldManager = (FieldManager)fieldManagerFactory.create();
    
    try {
    	
      // Retrieve all fields   
      List<Field> fields = fieldManager.find();
      
      if (fields != null) {
        
        for (Field field : fields) {
        	
          // Add field keyed by secure ID
          securedFields.put(field.getSecureId(), field);
        }
      }
    }
    finally {

    	// Recycle field manager
    	fieldManagerFactory.recycle(fieldManager);
    }
  }
  
  /**
    Returns a user's domain.
    @param session - Current session.
    @return IDomain - User's domain.
    @see ISecurityManager#getDomain(HttpSession)
  */
  @Override
  public IDomain getDomain(HttpSession session) throws SecurityException {

  	// Get domain associated with the current user
  	IDomain domain = (IDomain)session.getAttribute(AttributeNames.DOMAIN);

  	return domain;
  }

  /**
    Returns a value indicating whether or not a user is a domain admin.
    @param session - Current session.
    @return boolean - Domain administrator indicator.
    @see ISecurityManager#isDomainAdmin(HttpSession)
  */
  @Override
  public boolean isDomainAdmin(HttpSession session) throws SecurityException {
  
  	// Get current user
  	User user = (User)session.getAttribute(AttributeNames.USER);
  	
  	return user != null && user.getIsDomainAdmin();
  }
  
  /**
    Returns a value indicating whether or not a user has been authenticated.
    @param session - Current session.
    @return boolean - Authenticated indicator.
    @see ISecurityManager#isAuthenticated(HttpSession)
  */
  @Override
  public boolean isAuthenticated(HttpSession session) throws SecurityException {
  	
  	// Get current user
		User user = (User)session.getAttribute(AttributeNames.USER);

    return user != null; 
  }
  
  /**
    Indicates whether or not authorization is required for a given action ID.
    @param actionId - Action ID.
    @param session - Current session.
    @return boolean - Authorization required indicator.
    @see ISecurityManager#isAuthorizationRequired(String, HttpSession)
  */
  @Override
  public boolean isAuthorizationRequired(String actionId, HttpSession session) throws SecurityException {

    return securedActions.get(actionId) != null;
  }

  /**
    Indicates whether or not the current user is authorized to perform a given action.
    @param actionId Action ID.
    @param session Current session.
    @return boolean Authorization indicator.
    @see ISecurityManager#isAuthorized(String, HttpSession)
  */
  @Override
  public boolean isAuthorized(String actionId, HttpSession session) throws SecurityException {

    // Initialize return value
    boolean isAuthorized = true;
    
    // Get secured action
    Action securedAction = securedActions.get(actionId);
    
    if (securedAction != null) {
    	
    	// Set unauthorized by default
    	isAuthorized = false;

    	// Get current user's roles
      @SuppressWarnings("unchecked")
			List<Role> roles = (List<Role>)session.getAttribute(AttributeNames.ROLES);
      
      if (roles != null && !roles.isEmpty()) {
      	
      	for (Role role : roles) {
      		
          if (!role.getRestrictedTaskIds().contains(securedAction.getTaskId())) {
          	
          	// Set authorized and stop processing roles
          	isAuthorized = true;
          	break;
          }
      	}
      }
    }
    
    return isAuthorized;
  }

  /**
    Indicates whether or not a given field is hidden.
    @param fieldId Field ID.
    @param session Current session.
    @return boolean Hidden indicator.
    @see ISecurityManager#isFieldHidden(String, HttpSession)
  */
  @Override
  public boolean isFieldHidden(String fieldId, HttpSession session) throws SecurityException {

    // Initialize return value
    boolean isHidden = false;
    
    // Get secured field
    Field securedField = securedFields.get(fieldId);
    
    if (securedField != null) {
      
    	// Set hidden by default
    	isHidden = true;

    	// Get current user's roles
      @SuppressWarnings("unchecked")
			List<Role> roles = (List<Role>)session.getAttribute(AttributeNames.ROLES);
      
      if (roles != null && !roles.isEmpty()) {
      	
      	// Get underlying ID used to compare with role values
      	Object referenceId = securedField.getId();
      	
      	for (Role role : roles) {
      	
      		if (!role.getRestrictedFieldIds().contains(referenceId)) {
      			
      			// Set not hidden and stop processing roles
      			isHidden = false;
      			break;
      		}
      	}
      }
    }
    
    return isHidden;
  }

  /**
    Indicates whether or not a given field is read only.
    @param fieldId Field ID.
    @param session Current session.
    @return boolean Read only indicator.
    @see ISecurityManager#isFieldReadOnly(String, HttpSession)
  */
  @Override
  public boolean isFieldReadOnly(String fieldId, HttpSession session) throws SecurityException {

    // Initialize return value
    boolean isReadOnly = false;
    
    // Get secured field
    Field securedField = securedFields.get(fieldId);
    
    if (securedField != null) {
      
    	// Set read only by default
    	isReadOnly = true;

    	// Get current user's roles
      @SuppressWarnings("unchecked")
			List<Role> roles = (List<Role>)session.getAttribute(AttributeNames.ROLES);
      
      if (roles != null && !roles.isEmpty()) {
      	
      	// Get underlying ID used to compare with role values
      	Object referenceId = securedField.getId();
      	
      	for (Role role : roles) {
      	
      		if (!role.getReadOnlyFieldIds().contains(referenceId) && !role.getRestrictedFieldIds().contains(referenceId)) {
      			
      			// Set not read only and stop processing roles
      			isReadOnly = false;
      			break;
      		}
      	}
      }
    }
    
    return isReadOnly;
  }

}
