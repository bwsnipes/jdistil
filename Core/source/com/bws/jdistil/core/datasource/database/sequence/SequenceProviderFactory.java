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
package com.bws.jdistil.core.datasource.database.sequence;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.factory.SingletonPojoFactory;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.StringUtil;

/**
  Provides static access to an application defined sequence provider factory using
  a class specified in the framework properties file. A pojo factory using a
  the default sequence provider class is returned if a custom factory class
  is not defined in the framework properties file.
  @author - Bryan Snipes
*/
public final class SequenceProviderFactory {

  /**
    Sequence provider factory.
  */
  private static volatile IFactory sequenceProviderFactory = null;

  /**
    Creates a new SequenceProviderFactory.
  */
  private SequenceProviderFactory() {
    super();
  }

  /**
    Returns a sequence provider factory instance.
    @return IFactory - Sequence provider factory.
  */
  public static IFactory getInstance() {

    // Check to see if factory has been created
    if (sequenceProviderFactory == null) {

      synchronized(SequenceProviderFactory.class) {

        // Check to see if created while waiting
        if (sequenceProviderFactory == null) {

        	// Attempt to get a custom sequence provider factory
        	sequenceProviderFactory = getCustomSequenceProviderFactory();
        	
        	if (sequenceProviderFactory == null) {
        		
        		// Use singleton pojo factory targeting the default sequence provider
        		sequenceProviderFactory = new SingletonPojoFactory(DefaultSequenceProvider.class);
        	}
        }
      }
    }

    return sequenceProviderFactory;
  }
  
  /**
   * Creates and return a custom sequence provider factory using a class name
   * defined in the core properties file.
   * @return IFactory Custom sequence provider factory.
   */
  private static IFactory getCustomSequenceProviderFactory() {
  	
  	// Set method name
  	String methodName = "getCustomSecurityManagerFactory";
  	
  	// Initialize return value
  	IFactory sequenceProviderFactory = null;
  	
    // Retrieve sequence provider class name
    String sequenceProviderFactoryClassName = ResourceUtil.getString(Constants.SEQUENCE_PROVIDER_FACTORY);

    if (!StringUtil.isEmpty(sequenceProviderFactoryClassName)) {
    	
      // Create sequence provider factory
      Object object = Instantiator.create(sequenceProviderFactoryClassName);
    	
      if (object != null) {
      	
      	if (object instanceof IFactory) {
      		
      		// Cast to factory and assign to return value
      		sequenceProviderFactory = (IFactory)object;
      		
      		// Get target class
      		Class<?> targetClass = sequenceProviderFactory.getTargetClass();
      		
      		// Ensure target class is a sequence provider
      		if (ISequenceProvider.class.isAssignableFrom(targetClass)) {
      			
            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.sequence");
            logger.logp(Level.SEVERE, "SequenceProviderFactory", methodName, "Invalid custom sequence provider factory: Target class does not implement ISequenceProvider interface.");
      		}
      	}
      	else {
      		
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.sequence");
          logger.logp(Level.SEVERE, "SequenceProviderFactory", methodName, "Invalid custom sequence provider factory: Does not implement IFactory interface.");
      	}
      }
      else {
      	
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.sequence");
        logger.logp(Level.SEVERE, "SequenceProviderFactory", methodName, "Error creating instance of custom sequence provider factory.");
      }
    }
    
    return sequenceProviderFactory;
  }
  
}

