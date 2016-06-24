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
package com.bws.jdistil.core.validation;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.factory.SingletonPojoFactory;
import com.bws.jdistil.core.process.IProcessor;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.StringUtil;

/**
  Provides static access to an application defined validator factory using
  a class specified in the framework properties file. A pojo factory using a
  the default validator class is returned if a custom factory class
  is not defined in the framework properties file.
  @author - Bryan Snipes
*/
public final class ValidatorFactory {

  /**
    Validator factory.
  */
  private static volatile IFactory validatorFactory = null;

  /**
    Creates a new ValidatorFactory.
  */
  private ValidatorFactory() {
    super();
  }

  /**
    Returns a validator factory instance.
    @return IFactory - Validator factory.
  */
  public static IFactory getInstance() {

    // Check to see if factory has been created
    if (validatorFactory == null) {

      synchronized(ValidatorFactory.class) {

        // Check to see if created while waiting
        if (validatorFactory == null) {

        	// Attempt to get a custom validator factory
        	validatorFactory = getCustomValidatorFactory();
        	
        	if (validatorFactory == null) {
        		
        		// Use singleton pojo factory targeting the default validator
        		validatorFactory = new SingletonPojoFactory(DefaultValidator.class);
        	}
        }
      }
    }

    return validatorFactory;
  }

  /**
   * Creates and return a custom validator using a class name defined in the core properties file.
   * @return IFactory Custom validator factory.
   */
  private static IFactory getCustomValidatorFactory() {
  	
  	// Set method name
  	String methodName = "getCustomValidatorFactory";
  	
  	// Initialize return value
  	IFactory validatorFactory = null;
  	
    // Retrieve validator class name
    String validatorFactoryClassName = ResourceUtil.getString(Constants.VALIDATOR_FACTORY);

    if (!StringUtil.isEmpty(validatorFactoryClassName)) {
    	
      // Create validator factory
      Object object = Instantiator.create(validatorFactoryClassName);
    	
      if (object != null) {
      	
      	if (object instanceof IFactory) {
      		
      		// Cast to factory and assign to return value
      		validatorFactory = (IFactory)object;
      		
      		// Get target class
      		Class<?> targetClass = validatorFactory.getTargetClass();
      		
      		// Ensure target class is a processor
      		if (IProcessor.class.isAssignableFrom(targetClass)) {
      			
            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.validator");
            logger.logp(Level.SEVERE, "SecurityManagerFactory", methodName, "Invalid custom validator factory: Target class does not implement IProcessor interface.");
      		}
      	}
      	else {
      		
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.validator");
          logger.logp(Level.SEVERE, "SecurityManagerFactory", methodName, "Invalid custom validator factory: Does not implement IFactory interface.");
      	}
      }
      else {
      	
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.validator");
        logger.logp(Level.SEVERE, "SecurityManagerFactory", methodName, "Error creating instance of custom validator factory.");
      }
    }
    
    return validatorFactory;
  }
  
}

