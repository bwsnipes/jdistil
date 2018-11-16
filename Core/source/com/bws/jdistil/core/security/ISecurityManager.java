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

import javax.servlet.http.HttpSession;

/**
  Interface defining methods required by a Security Manager.
  @author - Bryan Snipes
*/
public interface ISecurityManager {

  /**
    Returns a user's domain.
    @param session - Current session.
    @return IDomain - User's domain.
  */
  public IDomain getDomain(HttpSession session) throws SecurityException;
  
  /**
    Returns a value indicating whether or not a user has been authenticated.
    @param session - Current session.
    @return boolean - Authenticated indicator.
  */
  public boolean isAuthenticated(HttpSession session) throws SecurityException;

  /**
    Indicates whether or not authorization is required for a given action ID.
    @param actionId - Action ID.
    @param session - Current session.
    @return boolean - Authorization required indicator.
  */
  public boolean isAuthorizationRequired(String actionId, HttpSession session)
      throws SecurityException;

  /**
    Indicates whether or not the current user is authorized to perform a given action.
    @param actionId - Action ID.
    @param session - Current session.
    @return boolean - Authorization indicator.
  */
  public boolean isAuthorized(String actionId, HttpSession session)
      throws SecurityException;

  /**
    Indicates whether or not a given field is hidden.
    @param fieldId - Field ID.
    @param session - Current session.
    @return boolean - Hidden indicator.
  */
  public boolean isFieldHidden(String fieldId, HttpSession session)
      throws SecurityException;

  /**
    Indicates whether or not a given field is read only.
    @param fieldId - Field ID.
    @param session - Current session.
    @return boolean - Read only indicator.
  */
  public boolean isFieldReadOnly(String fieldId, HttpSession session)
      throws SecurityException;

}
