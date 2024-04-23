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
package com.bws.jdistil.core.datasource.database.connection;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.factory.SingletonPojoFactory;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.StringUtil;

/**
  Provides static access to an application defined connection provider factory using
  a class specified in the framework properties file. A pojo factory using a
  the default connection provider class is returned if a custom factory class
  is not defined in the framework properties file.
  @author - Bryan Snipes
*/
public final class ConnectionProviderFactory {

  /**
    Connection provider factory.
  */
  private static volatile IFactory connectionProviderFactory = null;

  /**
    Creates a new ConnectionProviderFactory.
  */
  private ConnectionProviderFactory() {
    super();
  }

  /**
    Returns a connection provider factory instance.
    @return IFactory - Connection provider factory.
  */
  public static IFactory getInstance() {

    // Check to see if factory has been created
    if (connectionProviderFactory == null) {

      synchronized(ConnectionProviderFactory.class) {

        // Check to see if created while waiting
        if (connectionProviderFactory == null) {

        	// Attempt to get a custom connection provider factory
        	connectionProviderFactory = getCustomConnectionProviderFactory();
        	
        	if (connectionProviderFactory == null) {
        		
        		// Use singleton pojo factory targeting the default connection provider
        		connectionProviderFactory = new SingletonPojoFactory(JndiConnectionProvider.class);
        	}
        }
      }
    }

    return connectionProviderFactory;
  }
  
  /**
   * Creates and return a custom connection provider factory using a class name
   * defined in the core properties file.
   * @return IFactory Custom connection provider factory.
   */
  private static IFactory getCustomConnectionProviderFactory() {
  	
  	// Set method name
  	String methodName = "getCustomSecurityManagerFactory";
  	
  	// Initialize return value
  	IFactory connectionProviderFactory = null;
  	
    // Retrieve connection provider class name
    String connectionProviderFactoryClassName = ResourceUtil.getString(Constants.CONNECTION_PROVIDER_FACTORY);

    if (!StringUtil.isEmpty(connectionProviderFactoryClassName)) {
    	
      // Create connection provider factory
      Object object = Instantiator.create(connectionProviderFactoryClassName);
    	
      if (object != null) {
      	
      	if (object instanceof IFactory) {
      		
      		// Cast to factory and assign to return value
      		connectionProviderFactory = (IFactory)object;
      		
      		// Get target class
      		Class<?> targetClass = connectionProviderFactory.getTargetClass();
      		
      		// Ensure target class is a connection provider
      		if (IConnectionProvider.class.isAssignableFrom(targetClass)) {
      			
            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.connection");
            logger.logp(Level.SEVERE, "ConnectionProviderFactory", methodName, "Invalid custom connection provider factory: Target class does not implement IConnectionProvider interface.");
      		}
      	}
      	else {
      		
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.connection");
          logger.logp(Level.SEVERE, "ConnectionProviderFactory", methodName, "Invalid custom connection provider factory: Does not implement IFactory interface.");
      	}
      }
      else {
      	
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.connection");
        logger.logp(Level.SEVERE, "ConnectionProviderFactory", methodName, "Error creating instance of custom connection provider factory.");
      }
    }
    
    return connectionProviderFactory;
  }
  
}

