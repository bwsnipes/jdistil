package com.bws.jdistil.security.app;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.security.app.configuration.ActionIds;
import com.bws.jdistil.security.app.configuration.FieldIds;

public class SecurityManager extends com.bws.jdistil.security.SecurityManager {

  /**
    Default domain action IDs.
  */
  private static final Set<String> defaultDomainActionIds = new HashSet<String>();
  
  /**
    Global domain action IDs.
  */
  private static final Set<String> globalDomainActionIds = new HashSet<String>();

  /**
    Default domain field IDs.
  */
  private static final Set<String> defaultDomainFieldIds = new HashSet<String>();

  /**
    Global domain field IDs.
  */
  private static final Set<String> globalDomainFieldIds = new HashSet<String>();
  
  static {
    
    // Add action IDs restricted to domain admins in the default domain
    defaultDomainActionIds.add(ActionIds.VIEW_DOMAINS);
    defaultDomainActionIds.add(ActionIds.VIEW_DOMAIN_PREVIOUS_PAGE);
    defaultDomainActionIds.add(ActionIds.VIEW_DOMAIN_SELECT_PAGE);
    defaultDomainActionIds.add(ActionIds.VIEW_DOMAIN_NEXT_PAGE);
    defaultDomainActionIds.add(ActionIds.DELETE_DOMAIN);
    defaultDomainActionIds.add(ActionIds.ADD_DOMAIN);
    defaultDomainActionIds.add(ActionIds.EDIT_DOMAIN);
    defaultDomainActionIds.add(ActionIds.SAVE_DOMAIN);
    defaultDomainActionIds.add(ActionIds.CANCEL_DOMAIN);

    // Add field IDs restricted to domain admins in the default domain
    defaultDomainFieldIds.add(FieldIds.DOMAIN_NAME);
    defaultDomainFieldIds.add(FieldIds.DOMAIN_IS_DEFAULT_DATASOURCE);
    defaultDomainFieldIds.add(FieldIds.DOMAIN_DATASOURCE_NAME);
    defaultDomainFieldIds.add(FieldIds.USER_IS_DOMAIN_ADMIN);
    
    // Add action IDs restricted to domain admins
    globalDomainActionIds.add(ActionIds.VIEW_CHANGE_DOMAIN);
    globalDomainActionIds.add(ActionIds.CHANGE_DOMAIN);
    
    // Add field IDs restricted to domain admins
    globalDomainFieldIds.add(FieldIds.DOMAIN_SELECTED_ID);
  }
  
  /**
    Creates a new SecurityManager.
  */
	public SecurityManager() {
		super();
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
  
  	boolean isAuthorizationRequired = false;
  	
  	if (actionId != null) {
  		
  		isAuthorizationRequired = defaultDomainActionIds.contains(actionId) || globalDomainActionIds.contains(actionId) || super.isAuthorizationRequired(actionId, session);
  	}
  	
    return isAuthorizationRequired;
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
  	
  	if (actionId != null) {
  		
  		// Default to unauthorized
  		isAuthorized = false;
  		
  		if (defaultDomainActionIds.contains(actionId)) {
  			
  			// Must be a domain admin working in the default domain to be accessible
  			isAuthorized = isDomainAdmin(session) && isDefaultDomain(session);
  		}
  		else if (globalDomainActionIds.contains(actionId)) {
  			
  			// Must be a domain admin working in the default domain to be accessible
  			isAuthorized = isDomainAdmin(session);
  		}
  		else {
  			
  			// Determine using inherited role based security
  			isAuthorized = super.isAuthorized(actionId, session);
  		}
  	}
  	
    return isAuthorized;
  }
  
  /**
    Indicates whether or not a given field is hidden.
    @param fieldId Field ID.
    @param session Current session.
    @return boolean Hidden field indicator.
    @see ISecurityManager#isFieldReadOnly(String, HttpSession)
  */
  @Override
  public boolean isFieldHidden(String fieldId, HttpSession session) throws SecurityException {
  
    // Initialize return value
    boolean isHidden = false;
    
    if (fieldId != null) {
      
  		// Default to hidden
    	isHidden = true;
    	
    	if (defaultDomainFieldIds.contains(fieldId)) {
    		
  			// Must be a domain admin working in the default domain to be visible
    		isHidden = !isDomainAdmin(session) || !isDefaultDomain(session);
    	}
    	else if (globalDomainFieldIds.contains(fieldId)) {
    		
  			// Must be a domain admin working in the default domain to be visible
    		isHidden = !isDomainAdmin(session);
    	}
    	else {
    		
  			// Determine using inherited role based security
    		isHidden = super.isFieldHidden(fieldId, session);
    	}
    }
    
    return isHidden;
  }

  /**
   * Returns a value indicating whether or not the current user is in the default domain.
   * @param session Http session.
   * @return boolean Default domain indicator.
   * @throws SecurityException
   */
  private boolean isDefaultDomain(HttpSession session) throws SecurityException {
  	
		IDomain domain = getDomain(session);
		boolean isDefaultDomain = domain == null || domain.getId() == null || domain.getId().equals(IDomain.DEFAULT_ID);
  	
		return isDefaultDomain;
  }
}
