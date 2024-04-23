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
package com.bws.jdistil.core.resource;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.util.ListUtil;
import com.bws.jdistil.core.util.StringUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Utility class providing resource lookup services on the default or any specified resource bundle.
  @author - Bryan Snipes
*/
public class ResourceUtil {

  /**
    Object used for synchronization.
  */
  private static final Object lock = new Object();

  /**
    Merged resource bundle cache.
  */
  private static final Map<String, ResourceBundle> mergedResourceBundleCache = new Hashtable<String, ResourceBundle>();

  /**
    Class name attribute used in messaging.
  */
  protected static final String className = "com.bws.jdistil.core.resource.ResourceUtil";

  /**
    Creates an empty ResourceUtil object.&nbsp; Private access because all methods are static.
  */
  private ResourceUtil() {
    super();
  }

  public static String getPropertyFileName() {
  
  	// Initialize return value
  	String propertyFileName = Constants.PROPERTY_FILE;
  	
  	// Check for environment system property
  	String env = System.getProperty(Constants.ENV_PROPERTY_NAME, "");
  	
  	if (!StringUtil.isEmpty(env)) {
  		
  		// Build environment specific property file name
  		propertyFileName = Constants.PROPERTY_FILE + "-" + env;
  	}
  	
  	return propertyFileName;
  }
  
  /**
    Returns the core resource bundle.
    @return ResourceBundle - Resource bundle.
  */
  public static ResourceBundle getBundle() {
    return getBundle(getPropertyFileName());
  }

  /**
    Returns a resource bundle using a given resource name.&nbsp; If the provided
    resource name contains a comma delimited list of resource bundle names, a
    MergedResource bundle containing the contents of all resource bundles defined
    in the list is returned.
    @param resourceName - Resource bundle name.
    @return ResourceBundle - Resource bundle.
  */
  public static ResourceBundle getBundle(String resourceName) {
    return getBundle(resourceName, null, null);
  }

  /**
    Returns a resource bundle using a given resource name and locale. If the
    provided resource name contains a comma delimited list of resource bundle
    names, a MergedResource bundle containing the contents of all resource
    bundles defined in the list is returned.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @return ResourceBundle - Resource bundle.
  */
  public static ResourceBundle getBundle(String resourceName, Locale locale) {
    return getBundle(resourceName, locale, null);
  }

  /**
    Returns a resource bundle using a given resource name, locale, and class loader.
    If the provided resource name contains a comma delimited list of resource
    bundle names, a MergedResource bundle containing the contents of all resource
    bundles defined in the list is returned.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param loader - Class loader.
    @return ResourceBundle - Resource bundle.
  */
  public static ResourceBundle getBundle(String resourceName, Locale locale, ClassLoader loader) {

    // Set method name
    String methodName = "getBundle";
    
    // Initialize return value
    ResourceBundle resourceBundle = null;

    if (!StringUtil.isEmpty(resourceName)) {

      try {

        // Use default locale if one is not specified
        if (locale == null) {
          locale = Locale.getDefault();
        }

        // Check for multiple resource names
        if (resourceName.indexOf(",") < 0) {

          // Attempt to retrieve resource bundle - Use class loader if specified
          if (loader == null) {
            resourceBundle = ResourceBundle.getBundle(resourceName, locale);
          }
          else {
            resourceBundle = ResourceBundle.getBundle(resourceName, locale, loader);
          }
        }
        else {

          // Check cache for merged resource using locale specific key
          String key = locale + resourceName;
          resourceBundle = mergedResourceBundleCache.get(key);

          if (resourceBundle == null) {

            // Load merged resource
            synchronized (lock) {
              resourceBundle = loadMergedResource(resourceName, locale, loader);
              lock.notifyAll();
            }

          }
        }

      }
      catch (MissingResourceException resourceException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.resource");
        logger.logp(Level.WARNING, className, methodName, "Retrieving Resource Bundle", resourceException);
      }

    }

    return resourceBundle;
  }

  /**
    Returns an enumeration of core resource bundle keys.
    @return Enumeration - Enumeration of resource bundle keys.
  */
  public static Enumeration<?> getKeys() {
    return getKeys(getPropertyFileName());
  }

  /**
    Returns an enumeration of resource bundle keys based on a given resource name.
    @param resourceName - Resource bundle name.
    @return Enumeration - Enumeration of resource bundle keys.
  */
  public static Enumeration<?> getKeys(String resourceName) {
    return getKeys(resourceName, null, null);
  }

  /**
    Returns an enumeration of resource bundle keys based on a given resource
    name and locale.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @return Enumeration - Enumeration of resource bundle keys.
  */
  public static Enumeration<?> getKeys(String resourceName, Locale locale) {
    return getKeys(resourceName, locale, null);
  }

  /**
    Returns an enumeration of resource bundle keys based on a given resource
    name, locale, and class loader.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param loader - Class loader.
    @return Enumeration - Enumeration of resource bundle keys.
  */
  public static Enumeration<?> getKeys(String resourceName, Locale locale, ClassLoader loader) {

    // Initialize return value
    Enumeration<?> keys = null;

    if (resourceName != null) {

      // Attempt to retrieve resource bundle
      ResourceBundle resourceBundle = getBundle(resourceName, locale, loader);

      // Get keys
      if (resourceBundle != null) {
        keys = resourceBundle.getKeys();
      }
    }

    return keys;
  }

  /**
    Returns a string value from the core resource bundle based on a given resource key.
    @param resourceKey - Resource key.
    @return String - String associated with resource key.
  */
  public static String getString(String resourceKey) {
    return getString(getPropertyFileName(), resourceKey);
  }

  /**
    Returns a string value based on a specified resource bundle name and resource key.
    @param resourceName - Resource bundle name.
    @param resourceKey - Resource key.
    @return String - String associated with resource key.
  */
  public static String getString(String resourceName, String resourceKey) {
    return getString(resourceName, null, null, resourceKey);
  }

  /**
    Returns a string value based on a specified resource bundle name, locale and
    resource key.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param resourceKey - Resource key.
    @return String - String associated with resource key.
  */
  public static String getString(String resourceName, Locale locale, String resourceKey) {
    return getString(resourceName, locale, null, resourceKey);
  }

  /**
    Returns a string value based on a specified resource bundle name, locale,
    class loader and resource key.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param loader - Class loader.
    @param resourceKey - Resource key.
    @return String - String associated with resource key.
  */
  public static String getString(String resourceName, Locale locale, ClassLoader loader, String resourceKey) {

    // Set method name
    String methodName = "getString";
    
    // Initialize return value
    String value = null;

    if (resourceName != null && resourceKey != null) {

      // Get resource bundle
      ResourceBundle resourceBundle = getBundle(resourceName, locale, loader);

      if (resourceBundle != null && resourceBundle.containsKey(resourceKey)) {

        try {
          value = resourceBundle.getString(resourceKey);
        }
        catch (MissingResourceException resourceException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.resource");
          logger.logp(Level.SEVERE, className, methodName, "Retrieving String", resourceException);
        }

      }
    }

    return value;
  }

  /**
    Returns a string array from the core resource bundle based on a given resource key.
    @param resourceKey - Resource key.
    @return String - String array associated with resource key.
  */
  public static String[] getStringArray(String resourceKey) {
    return getStringArray(getPropertyFileName(), resourceKey);
  }

  /**
    Returns a string array based on a specified resource bundle name and resource key.
    @param resourceName - Resource bundle name.
    @param resourceKey - Resource key.
    @return String[] - String array associated with resource key.
  */
  public static String[] getStringArray(String resourceName, String resourceKey) {
    return getStringArray(resourceName, null, null, resourceKey);
  }

  /**
    Returns a string array based on a specified resource bundle name, locale and
    resource key.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param resourceKey - Resource key.
    @return String[] - String array associated with resource key.
  */
  public static String[] getStringArray(String resourceName, Locale locale, String resourceKey) {
    return getStringArray(resourceName, locale, null, resourceKey);
  }

  /**
    Returns a string array based on a specified resource bundle name, locale,
    class loader, and resource key.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param loader - Class loader.
    @param resourceKey - Resource key.
    @return String[] - String array associated with resource key.
  */
  public static String[] getStringArray(String resourceName, Locale locale, ClassLoader loader, String resourceKey) {

    // Set method name
    String methodName = "getStringArray";
    
    // Initialize return value
    String[] values = null;

    if (resourceName != null && resourceKey != null) {

      // Attempt to retrieve resource bundle
      ResourceBundle resourceBundle = getBundle(resourceName, locale, loader);

      if (resourceBundle != null && resourceBundle.containsKey(resourceKey)) {

        try {
          values = resourceBundle.getStringArray(resourceKey);
        }
        catch (MissingResourceException resourceException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.resource");
          logger.logp(Level.SEVERE, className, methodName, "Retrieving String", resourceException);
        }

      }
    }

    return values;
  }

  /**
    Returns an object from the core resource bundle based on a given resource key.
    @param resourceKey - Resource key.
    @return Object - Object associated with resource key.
  */
  public static Object getObject(String resourceKey) {
    return getObject(getPropertyFileName(), resourceKey);
  }

  /**
    Returns an object based on a specified resource bundle name and resource key.
    @param resourceName - Resource bundle name.
    @param resourceKey - Resource key.
    @return Object - Object associated with resource key.
  */
  public static Object getObject(String resourceName, String resourceKey) {
    return getObject(resourceName, null, null, resourceKey);
  }

  /**
    Returns an object based on a specified resource bundle name, locale and resource key.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param resourceKey - Resource key.
    @return Object - Object associated with resource key.
  */
  public static Object getObject(String resourceName, Locale locale, String resourceKey) {
    return getObject(resourceName, locale, null, resourceKey);
  }

  /**
    Returns an object based on a specified resource bundle name, locale,
    class loader, and resource key.
    @param resourceName - Resource bundle name.
    @param locale - Locale.
    @param loader - Class loader.
    @param resourceKey - Resource key.
    @return Object - Object associated with resource key.
  */
  public static Object getObject(String resourceName, Locale locale, ClassLoader loader, String resourceKey) {

    // Set method name
    String methodName = "getObject";
    
    // Initialize return value
    Object value = null;

    if (resourceName != null && resourceKey != null) {

      // Attempt to retrieve resource bundle
      ResourceBundle resourceBundle = getBundle(resourceName, locale, loader);

      if (resourceBundle != null && resourceBundle.containsKey(resourceKey)) {

        try {
          value = resourceBundle.getObject(resourceKey);
        }
        catch (MissingResourceException resourceException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.resource");
          logger.logp(Level.SEVERE, className, methodName, "Retrieving Object", resourceException);
        }

      }
    }

    return value;
  }

  /**
    Returns a merged resource bundle using a comma delimited list of resource names,
    a locale and class loader. The default class loader is used if one is not specified.
    @param resourceNames - Comma delimited list of resource names.
    @param locale - Locale.
    @param loader - Class loader.
    @return ResourceBundle - Merged resource bundle.
  */
  private static ResourceBundle loadMergedResource(String resourceNames, Locale locale, ClassLoader loader) {

    // Create locale specific resource key
    String key = locale + resourceNames;

    // Check to see if resource was loaded while waiting
    ResourceBundle mergedResourceBundle = mergedResourceBundleCache.get(key);

    if (mergedResourceBundle == null) {

      // Get list of resource bundles
      List<ResourceBundle> bundleList = getBundleList(resourceNames, locale, loader);

      if (bundleList != null) {

        // Create merged resource
        mergedResourceBundle = new MergedResource(bundleList);
        
        // Add merged resource to cache using locale specific key
        mergedResourceBundleCache.put(key, mergedResourceBundle);
      }
    }

    return mergedResourceBundle;
  }

  /**
    Returns a list of resource bundles using a comma delimited list of resource
    names, a locale, and a class loader. The default class loader is used if one
    is not specified.
    @param resourceNames - Comma delimited list of resource names.
    @param locale - Locale.
    @param loader - Class loader.
    @return List - List of resource bundles.
  */
  private static List<ResourceBundle> getBundleList(String resourceNames, Locale locale, ClassLoader loader) {

    // Set method name
    String methodName = "getBundleList";
    
    // Initialize return value
    List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();

    if (resourceNames != null) {

      // Parse into list of names
      List<String> names = ListUtil.parse(resourceNames, ListUtil.COMMA, null);
      
      for (String resourceName : names) {
        
        try {
          // Initialize resource bundle
          ResourceBundle resourceBundle = null;

          // Attempt to retrieve resource bundle - Use class loader if specified
          if (loader == null) {
            resourceBundle = ResourceBundle.getBundle(resourceName, locale);
          }
          else {
            resourceBundle = ResourceBundle.getBundle(resourceName, locale, loader);
          }

          // Add bundle to list
          if (resourceBundle != null) {
            bundles.add(resourceBundle);
          }
        }
        catch (MissingResourceException resourceException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.resource");
          logger.logp(Level.SEVERE, className, methodName, "Retrieving Bundle List", resourceException);
        }
      }
    }

    return bundles;
  }

}