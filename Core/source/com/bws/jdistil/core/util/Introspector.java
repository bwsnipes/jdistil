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
package com.bws.jdistil.core.util;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.FieldBinding;
import com.bws.jdistil.core.configuration.ObjectBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
  Provides utility methods for introspecting objects.
  @author Bryan Snipes
*/
public class Introspector {

  /**
    Creates a new FieldAccessible object.
  */
  private Introspector() {
    super();
  }

  /**
	  Returns the property name bound to a given field ID.
	  @param objectClass Target class.
	  @param fieldId Field ID.
	  @return String Property name.
	*/
	public static String getPropertyName(Class<?> objectClass, String fieldId) {
	
	  // Initialize return value
	  String propertyName = null;
	
	  if (objectClass != null && !StringUtil.isEmpty(fieldId)) {
	
	    // Get object binding
	    ObjectBinding objectBinding = ConfigurationManager.getObjectBinding(objectClass);
	
	    if (objectBinding != null) {
	      
	      // Get field binding
	      FieldBinding fieldBinding = objectBinding.getFieldBinding(fieldId);
	      
	      // Get property name
	      if (fieldBinding != null) {
	        propertyName = fieldBinding.getPropertyName();
	      }
	    }
	  }
	
	  return propertyName;
	}
	
  /**
    Returns a property class using a property name.
    @param objectClass Target class.
    @param propertyName Property name.
    @return Class Property class.
  */
  public static Class<?> getPropertyClass(Class<?> objectClass, String propertyName) {
  
    // Initialize return value
    Class<?> propertyClass = null;
  
    if (objectClass != null && !StringUtil.isEmpty(propertyName)) {
  
      // Get method name
      String methodName = "get" + propertyName;
  
      try {
        // Get method
        Method method = objectClass.getMethod(methodName);
  
        // Get property value
        if (method != null) {
          propertyClass = method.getReturnType();
        }
      }
      catch (NoSuchMethodException noSuchMethodException) {
        System.out.println("getPropertyValue: " + noSuchMethodException.getMessage());
      }
    }
  
    return propertyClass;
  }
  
  /**
    Returns a property value using a property name.
    @param object Target object.
    @param propertyName Property name.
    @return Object Property value.
  */
  public static Object getPropertyValue(Object object, String propertyName) {
  
    // Initialize return value
    Object propertyValue = null;
  
    if (object != null && !StringUtil.isEmpty(propertyName)) {
  
      // Get method name
      String methodName = "get" + propertyName;
  
      try {
        // Get method
        Method method = object.getClass().getMethod(methodName, (Class[])null);
  
        // Get property value
        if (method != null) {
          propertyValue = method.invoke(object);
        }
      }
      catch (NoSuchMethodException noSuchMethodException) {
        System.out.println("getPropertyValue: " + noSuchMethodException.getMessage());
      }
      catch (InvocationTargetException invocationTargetException) {
        System.out.println("getPropertyValue: " + invocationTargetException.getMessage());
      }
      catch (IllegalAccessException illegalAccessException) {
        System.out.println("getPropertyValue: " + illegalAccessException.getMessage());
      }
    }
  
    return propertyValue;
  }
  
  /**
    Sets a property value using a property name and value.
    @param object Target object.
    @param propertyName Property name.
    @param propertyValue Property value.
  */
  public static void setPropertyValue(Object object, String propertyName, Object propertyValue) {
  
    if (object != null && !StringUtil.isEmpty(propertyName)) {
  
      // Get method name
      String methodName = "set" + propertyName;
  
      // Get object class
      Class<?> objectClass = object.getClass();
      
      // Create parameter types array
      Class<?> propertyClass = getPropertyClass(objectClass, propertyName);
      Class<?>[] parameterTypes = new Class<?>[] {propertyClass};
  
      try {
        // Get method
        Method method = objectClass.getMethod(methodName, parameterTypes);
  
        if (method != null) {
  
          // Create values arrays
          Object[] values = new Object[] {propertyValue};
  
          // Set property value
          method.invoke(object, values);
        }
      }
      catch (NoSuchMethodException noSuchMethodException) {
        System.out.println("setPropertyValue: " + noSuchMethodException.getMessage());
      }
      catch (InvocationTargetException invocationTargetException) {
        System.out.println("setPropertyValue: " + invocationTargetException.getMessage());
      }
      catch (IllegalAccessException illegalAccessException) {
        System.out.println("setPropertyValue: " + illegalAccessException.getMessage());
      }
    }
  }

  /**
    Returns a field value using an object and a field ID.
    @param object Target object.
    @param fieldId Field ID.
    @return Object Field value.
  */
  public static Object getFieldValue(Object object, String fieldId) {

    // Initialize return value
    Object propertyValue = null;

    if (object != null && !StringUtil.isEmpty(fieldId)) {
      
      // Get property name
      String propertyName = getPropertyName(object.getClass(), fieldId);

      // Get property value
      if (!StringUtil.isEmpty(propertyName)) {
        propertyValue = getPropertyValue(object, propertyName);
      }
    }

    return propertyValue;
  }

  /**
    Sets a field value using a field ID and value.
    @param object Target object.
    @param fieldId Field ID.
    @param fieldValue Field value.
  */
  public static void setFieldValue(Object object, String fieldId, Object fieldValue) {

    if (object != null && !StringUtil.isEmpty(fieldId)) {
      
      // Get property name
      String propertyName = getPropertyName(object.getClass(), fieldId);

      // Set property value
      if (!StringUtil.isEmpty(propertyName)) {
        setPropertyValue(object, propertyName, fieldValue);
      }
    }
  }

}
