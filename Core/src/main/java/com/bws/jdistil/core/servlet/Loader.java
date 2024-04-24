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
package com.bws.jdistil.core.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.configuration.FieldBinding;
import com.bws.jdistil.core.configuration.ObjectBinding;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.Introspector;

/**
  This class provides static methods used to load objects with data contained 
  in a servlet request.
  @author - Bryan Snipes
*/
public class Loader {
  
  /**
    Creates a new instance of the ParameterExtractor class.&nbsp; Defined with
    private access to prohibit instance creation.
  */
  private Loader() {
    super();
  }

  /**
    Returns an instance count for a given object. Instance numbers are appended
    as a suffix to the field ID.
    @param request - Servlet request object.
    @param object - Target object.
    @return int - Instance count.
  */
  public static int getInstanceCount(ServletRequest request, Object object) {

    // Initialize return value
    int instanceCount = 0;

    // Get object binding
    ObjectBinding objectBinding = ConfigurationManager.getObjectBinding(object.getClass());

    if (objectBinding != null) {
      
      // Get field bindings
      Set<FieldBinding> fieldBindings = objectBinding.getFieldBindings();
      
      if (fieldBindings != null) {

        for (FieldBinding fieldBinding : fieldBindings) {
          
          // Get field ID
          String fieldId = fieldBinding.getFieldId();
          
          // Get instances
          List<Integer> instances = ParameterExtractor.getInstances(request, fieldId);
          
          if (instances != null && !instances.isEmpty()) {

            // Set count and stop processing when first field is found
          	instanceCount = instances.size();
            break;
          }
        }
      }
    }

    return instanceCount;
  }

  /**
    Loads data into an instance of this class using the first instance of data
    contained in a given HTTP servlet request. Security manager is used to avoid 
    loading hidden and read only field values.
    @param securityManager - Security manager.
    @param request - HTTP servlet request.
    @param action - Submitted action.
    @param object - Object to load.
  */
  public static void load(ISecurityManager securityManager, HttpServletRequest request, Action action, Object object) throws ServletException {
    load(securityManager, request, action, object, 0);
  }

  /**
    Loads page specific data into an instance of this class using data contained
    in a given HTTP servlet request. Security manager is used to avoid 
    loading hidden and read only field values.
    @param securityManager - Security manager.
    @param request - HTTP servlet request.
    @param action - Submitted action.
    @param object - Object to load.
    @param instance - Instance number.
  */
  public static void load(ISecurityManager securityManager, HttpServletRequest request, Action action, Object object, int instance) throws ServletException {

  	// Set method name
  	String methodName = "load";
  	
    if (request != null && action != null && object != null) {
      
      // Get all action related field IDs
      List<String> fieldIds = action.getFieldIds();
      
      // Get object binding
      ObjectBinding objectBinding = ConfigurationManager.getObjectBinding(object.getClass());

      if (fieldIds != null && objectBinding != null) {
        
      	// Get the current session
      	HttpSession session = request.getSession(true);
      	
      	try {
      		
          for (String fieldId : fieldIds) {
            
          	if (!securityManager.isFieldHidden(fieldId, session) && !securityManager.isFieldReadOnly(fieldId, session)) {
          		
              // Get field
              Field field = ConfigurationManager.getField(fieldId);

              // Get field binding
              FieldBinding fieldBinding = objectBinding.getFieldBinding(fieldId);
              
              if (field != null && fieldBinding != null) {
                
                // Initialize value
                Object value = null;
                
                if (fieldBinding.isCollection()) {
                  value = getValues(request, object, field, fieldBinding, instance);
                }
                else {
                  value = getValue(request, object, field, fieldBinding, instance);
                }

                // Set field value
                Introspector.setFieldValue(object, fieldId, value);
              }
          	}
          }
      	}
        catch (SecurityException securityException) {
          
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.servlet");
          logger.logp(Level.SEVERE, "com.bws.jdistil.core.servlet.Loader", methodName, "Loading Object Data", securityException);
          
          throw new ServletException(methodName + ":" + securityException.getMessage());
        }
      }
    }
  }

  /**
    Returns a field value using a request, field, and instance number.
    @param request - HTTP servlet request.
    @param object - Target object.
    @param field - Field.
    @param fieldBinding - Field binding.
    @param instance - Instance number.
    @return Object - Field value.
  */
  private static Object getValue(HttpServletRequest request, Object object, Field field, FieldBinding fieldBinding, int instance) {

    // Initialize return value
    Object value = null;

    // Get field ID
    String fieldId = field.getId();

    // Get field type
    Integer type = field.getType();

    if (!type.equals(Field.CUSTOM_CLASS)) {

      // Retrieve field value
      value = ParameterExtractor.getObject(request, fieldId, instance);
    }
    else {

      // Get property name
      String propertyName = fieldBinding.getPropertyName();

      // Get property class
      Class<?> propertyClass = Introspector.getPropertyClass(object.getClass(), propertyName);

      // Get object initializer
      Object initializer = ParameterExtractor.getObject(request, fieldId, instance);

      // Create custom object using submitted field value as an initializer
      value = Instantiator.create(propertyClass, initializer.getClass(), initializer);
    }

    return value;
  }

  /**
    Returns a list of field values using a request, field, and instance number.
    @param request - HTTP servlet request.
    @param object - Target object.
    @param field - Field.
    @param fieldBinding - Field binding.
    @param instance - Instance number.
    @return List - List of field values.
  */
  private static Object getValues(HttpServletRequest request, Object object, Field field, FieldBinding fieldBinding, int instance) {

    // Initialize return value
    List<Object> values = new ArrayList<Object>();

    // Get field ID
    String fieldId = field.getId();

    // Get field type
    Integer type = field.getType();

    if (!type.equals(Field.CUSTOM_CLASS)) {

      // Retrieve field value
      values = ParameterExtractor.getObjects(request, fieldId, instance);
    }
    else {

      // Get property name
      String propertyName = fieldBinding.getPropertyName();

      // Get property class
      Class<?> propertyClass = Introspector.getPropertyClass(object.getClass(), propertyName);

      // Get object intiailizers
      List<Object> initializers = ParameterExtractor.getObjects(request, fieldId, instance);

      if (initializers != null) {

        for (Object initializer : initializers) {

          // Create custom object using submitted field value as an initializer
          Object value = Instantiator.create(propertyClass, initializer.getClass(), initializer);

          // Add custom object
          if (value != null) {
            values.add(value);
          }
        }
      }
    }

    return values;
  }

}
