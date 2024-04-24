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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Utility class used to handle the object instantiation.
  @author Bryan Snipes
*/
public class Instantiator {

  /**
   * Class name.
   */
  private static final String className = "Instantiator";
  
  /**
    Creates an Instantiator object.&nbsp; Defined with private access
    to avoid object instantiation.
  */
  private Instantiator() {
    super();
  }

  /**
    Returns a class using a class name.
    @param className - Class name.
    @return Class - Object class.
  */
  public static Class<?> getClass(String className) {

    // Initialize return value
    Class<?> objectClass = null;

    // Set method name
    String methodName = "getClass";

    if (!StringUtil.isEmpty(className)) {

      try {
        // Get class
        objectClass = Class.forName(className);
      }
      catch (ClassNotFoundException classNotFoundException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.util");
        logger.logp(Level.SEVERE, className, methodName, "Getting Object Class", classNotFoundException);
      }
    }

    return objectClass;
  }

  /**
    Instantiates an object using a class name.
    @param className - Class name.
    @return Object - Newly instantiated object.
  */
  public static Object create(String className) {
    return create(getClass(className));
  }

  /**
    Instantiates an object using class name, parameter type, and parameter value.
    @param className - Class name.
    @param parameterType - Parameter type.
    @param parameterValue - Parameter value.
    @return Object - Newly instantiated object.
  */
  public static Object create(String className, Class<?> parameterType, Object parameterValue) {
    return create(getClass(className), parameterType, parameterValue);
  }

  /**
    Instantiates an object using a class name, a list of parameter types, and
    a list of parameter values.
    @param className - Class name.
    @param parameterTypes - List of parameter types.
    @param parameterValues - List of parameter values.
    @return Object - Newly instantiated object.
  */
  public static Object create(String className, List<? extends Class<?>> parameterTypes, List<? extends Object> parameterValues) {
    return create(getClass(className), parameterTypes, parameterValues);
  }

  /**
    Instantiates an object using a given class.
    @param objectClass - Object class.
    @return Object - Newly instantiated object.
  */
  public static Object create(Class<?> objectClass) {
    return create(objectClass, (Class<?>)null, (Object)null);
  }

  /**
    Instantiates an object using an object class, parameter type, and parameter value.
    @param objectClass - Object class.
    @param parameterType - Parameter type.
    @param parameterValue - Parameter value.
    @return Object - Newly instantiated object.
  */
  public static Object create(Class<?> objectClass, Class<?> parameterType, Object parameterValue) {

    // Initialize types and values
    List<Class<?>> types = null;
    List<Object> values = null;

    if (parameterType != null) {
      types = new ArrayList<Class<?>>(1);
      types.add(parameterType);
    }

    if (parameterValue != null) {
      values = new ArrayList<Object>(1);
      values.add(parameterValue);
    }

    return create(objectClass, types, values);
  }

  /**
    Instantiates an object using an object class, a list of parameter types, and
    a list of parameter values.
    @param objectClass - Object class.
    @param parameterTypes - List of parameter types.
    @param parameterValues - List of parameter values.
    @return Object - Newly instantiated object.
  */
  public static Object create(Class<?> objectClass, List<? extends Class<?>> parameterTypes, List<? extends Object> parameterValues) {

    // Initialize return value
    Object object = null;

    if (objectClass != null) {

      // Set method name
      String methodName = "create";

      try {

        if (parameterTypes == null || parameterTypes.size() == 0) {

          object = objectClass.getDeclaredConstructor().newInstance();
        }
        else {

          // Create types
          Class<?>[] types = new Class[parameterTypes.size()];

          // Initialize index
          int index = 0;
          
          // Populate types
          for (Class<?> parameterClass : parameterTypes) {
            types[index++] = parameterClass.getClass();
          }

          // Get constructor
          Constructor<?> constructor = objectClass.getConstructor(types);

          if (constructor != null) {

            // Initialize values
            Object[] values = null;

            // Populate values
            if (parameterValues != null && parameterValues.size() > 0) {
              values = parameterValues.toArray();
            }

            // Create object
            object = constructor.newInstance(values);
          }
        }
      }
      catch (NoSuchMethodException noSuchMethodException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.util");
        logger.logp(Level.SEVERE, className, methodName, "Instanitating Object", noSuchMethodException);
      }
      catch (InvocationTargetException invocationTargetException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.util");
        logger.logp(Level.SEVERE, className, methodName, "Instanitating Object", invocationTargetException);
      }
      catch (IllegalAccessException accessException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.util");
        logger.logp(Level.SEVERE, className, methodName, "Instanitating Object", accessException);
      }
      catch (InstantiationException instantiationException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.util");
        logger.logp(Level.SEVERE, className, methodName, "Instanitating Object", instantiationException);
      }
    }

    return object;
  }

}
