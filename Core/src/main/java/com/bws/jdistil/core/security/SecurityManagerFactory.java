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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.factory.SingletonPojoFactory;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.StringUtil;

/**
  Provides static access to an application defined security manager factory using
  a class specified in the framework properties file. A pojo factory using a
  the default security manager class is returned if a custom factory class
  is not defined in the framework properties file.
  @author - Bryan Snipes
*/
public final class SecurityManagerFactory {

  /**
    Security manager factory.
  */
  private static volatile IFactory securityManagerFactory = null;

  /**
    Creates a new SecurityManagerFactory.
  */
  private SecurityManagerFactory() {
    super();
  }

  /**
    Returns a security manager factory instance.
    @return IFactory - Security manager factory.
  */
  public static IFactory getInstance() {

    // Check to see if factory has been created
    if (securityManagerFactory == null) {

      synchronized(SecurityManagerFactory.class) {

        // Check to see if created while waiting
        if (securityManagerFactory == null) {

        	// Attempt to get a custom security manager factory
        	securityManagerFactory = getCustomSecurityManagerFactory();
        	
        	if (securityManagerFactory == null) {
        		
        		// Use singleton pojo factory targeting the default security manager
        		securityManagerFactory = new SingletonPojoFactory(DefaultSecurityManager.class);
        	}
        }
      }
    }

    return securityManagerFactory;
  }
  
  /**
   * Creates and return a custom security manager factory using a class name
   * defined in the core properties file.
   * @return IFactory Custom security manager factory.
   */
  private static IFactory getCustomSecurityManagerFactory() {
  	
  	// Set method name
  	String methodName = "getCustomSecurityManagerFactory";
  	
  	// Initialize return value
  	IFactory securityManagerFactory = null;
  	
    // Retrieve security manager class name
    String securityManagerFactoryClassName = ResourceUtil.getString(Constants.SECURITY_MANAGER_FACTORY);

    if (!StringUtil.isEmpty(securityManagerFactoryClassName)) {
    	
      // Create security manager factory
      Object object = Instantiator.create(securityManagerFactoryClassName);
    	
      if (object != null) {
      	
      	if (object instanceof IFactory) {
      		
      		// Cast to factory and assign to return value
      		securityManagerFactory = (IFactory)object;
      		
      		// Get target class
      		Class<?> targetClass = securityManagerFactory.getTargetClass();
      		
      		// Ensure target class is a security manager
      		if (ISecurityManager.class.isAssignableFrom(targetClass)) {
      			
            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.security");
            logger.logp(Level.SEVERE, "SecurityManagerFactory", methodName, "Invalid custom security manager factory: Target class does not implement ISecurityManager interface.");
      		}
      	}
      	else {
      		
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.security");
          logger.logp(Level.SEVERE, "SecurityManagerFactory", methodName, "Invalid custom security manager factory: Does not implement IFactory interface.");
      	}
      }
      else {
      	
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.security");
        logger.logp(Level.SEVERE, "SecurityManagerFactory", methodName, "Error creating instance of custom security manager factory.");
      }
    }
    
    return securityManagerFactory;
  }
  
}

