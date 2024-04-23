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
package com.bws.jdistil.core.servlet.http;

import com.bws.jdistil.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;

/**
  Utility class used to clean objects stored in the session attributes
  collection when an action is encountered that is outside the scope of
  actions associated with any object stored in the session. Objects stored
  in the session can be registered and unregistered with the cleaner using
  the attribute name used to store the object in the session attributes and
  a set of action IDs. The set of action IDs represents all actions where
  it is valid to have the object remain in session. The object will be 
  removed when an action is encountered that is outside the scope of actions
  registered with the object. The session cleaner is used by the core controller
  each time an action is processed to clean the current session.
  @author - Bryan Snipes
  @see com.bws.jdistil.core.servlet.http.Controller
*/
public class SessionCleaner {

  /**
    Registry containing registered attribute names.
  */
  private static final Map<String, Set<String>> registry = new HashMap<String, Set<String>>();
  
  /**
    Creates a new SessionCleaner object. Defined with private access to avoid instantiation.
  */
  private SessionCleaner() {
    super();
  }

  /**
    Registers an attribute with the session cleaner.
    @param attributeName - Attribute name.
    @param actionIds - Set of action IDs.
  */
  public static void registerAttribute(String attributeName, Set<String> actionIds) {

    // Register attribute
    if (!StringUtil.isEmpty(attributeName) && actionIds != null && !actionIds.isEmpty()) {
      registry.put(attributeName, actionIds);
    }
  }

  /**
    Unregisters an attribute with the session cleaner.
    @param attributeName - Attribute name.
  */
  public static void unregisterAttribute(String attributeName) {
  
    if (!StringUtil.isEmpty(attributeName)) {
      
      // Unregister attribute
      registry.remove(attributeName);
    }
  }
  
  /**
    Cleans the session based on a specified action ID.
    @param actionId - Action ID.
    @param session - HTTP session.
  */
  protected static void clean(String actionId, HttpSession session) {
  
    if (!StringUtil.isEmpty(actionId) && session != null) {
      
      // Get attribute names
      Set<String> attributeNames = registry.keySet();
      
      for (String attributeName : attributeNames) {
        
        // Get associated action IDs
        Set<String> actionIds = registry.get(attributeName);
        
        // Remove session value
        if (actionIds == null || !actionIds.contains(actionId)) {
          session.removeAttribute(attributeName);
        }
      }
    }
  }
  
}