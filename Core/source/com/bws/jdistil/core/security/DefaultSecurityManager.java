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
package com.bws.jdistil.core.security;

import com.bws.jdistil.core.configuration.AttributeNames;

import javax.servlet.http.HttpSession;

/**
  Default security manager.
  @author - Bryan Snipes
*/
public class DefaultSecurityManager implements ISecurityManager {

  /**
    Creates a new DefaultSecurityManager.
  */
  public DefaultSecurityManager() {
    super();
  }

  /**
    Returns a user's domain.
    @param session - Current session.
    @return IDomain - User's domain.
    @see ISecurityManager#getDomain(HttpSession)
  */
  @Override
  public IDomain getDomain(HttpSession session) throws SecurityException {
  	return null;
  }

  /**
    Returns a value indicating whether or not a user is a domain admin.
    @param session - Current session.
    @return boolean - Domain administrator indicator.
    @see ISecurityManager#isDomainAdmin(HttpSession)
  */
  @Override
  public boolean isDomainAdmin(HttpSession session) throws SecurityException {
  	return false;
  }

	/**
    Returns a value indicating whether or not a user has been authenticated.
    @param session - Current session.
    @return boolean - Authenticated indicator.
    @see ISecurityManager#isAuthenticated(HttpSession)
  */
  @Override
  public boolean isAuthenticated(HttpSession session) throws SecurityException {

    // Attempt to authentication indicator from session
    Boolean isAuthenticated = (Boolean)session.getAttribute(AttributeNames.IS_AUTHENTICATED);

    return isAuthenticated != null && isAuthenticated.equals(Boolean.TRUE);
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

    return false;
  }

  /**
    Indicates whether or not the current user is authorized to perform a given action.
    @param actionId - Action ID.
    @param session - Current session.
    @return boolean - Authorization indicator.
    @see ISecurityManager#isAuthorized(String, HttpSession)
  */
  @Override
  public boolean isAuthorized(String actionId, HttpSession session) throws SecurityException {

    return true;
  }

  /**
    Indicates whether or not a given field is hidden.
    @param fieldId - Field ID.
    @param session - Current session.
    @return boolean - Hidden indicator.
    @see ISecurityManager#isFieldHidden(String, HttpSession)
  */
  @Override
  public boolean isFieldHidden(String fieldId, HttpSession session) throws SecurityException {

    return false;
  }

  /**
    Indicates whether or not a given field is read only.
    @param fieldId - Field ID.
    @param session - Current session.
    @return boolean - Read only indicator.
    @see ISecurityManager#isFieldReadOnly(String, HttpSession)
  */
  @Override
  public boolean isFieldReadOnly(String fieldId, HttpSession session) throws SecurityException {

    return false;
  }

}
