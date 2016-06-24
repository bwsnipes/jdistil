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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Resource bundle class used to handle merging the contents of multiple resource
  bundles.&nbsp Required to get around problem in JDK 1.3 where the parent
  of a resource bundle is not searched for values.
  @author - Bryan Snipes
*/
public class MergedResource extends ResourceBundle {

  /**
    Map of all contents.
  */
  private final Map<String, Object> contents = new HashMap<String, Object>();

  /**
    Class name attribute used in messaging.
  */
  protected static String className = "com.bws.jdistil.core.resource.MergedResource";

  /**
    Creates a new MergedResource object using the contents from a list of
    resource bundles.&nbsp Illegal argument is thrown if all items in the provided
    list are not of type ResourceBundle.
    @param resourceBundles - List of resource bundles.
    @throws java.lang.IllegalArgumentException
  */
  public MergedResource(List<ResourceBundle> resourceBundles) {
    super();

    // Check for valid list
    if (resourceBundles != null) {

      // Initialize processed bundles list
      List<ResourceBundle> processedBundles = new ArrayList<ResourceBundle>(resourceBundles.size());

      for (ResourceBundle bundle : resourceBundles) {

        // Check to see if bundle has already been processed
        if (!processedBundles.contains(bundle)) {

          // Get all bundle keys
          Enumeration<?> keys = bundle.getKeys();

          // Check for empty bundle
          if (keys != null) {

            // Add all objects to map
            while (keys.hasMoreElements()) {

              // Get next key
              String key = keys.nextElement().toString();

              // Log message if key already processed
              if (contents.containsKey(key)) {

                // Post error message
                Logger logger = Logger.getLogger("com.bws.jdistil.core.resource");
                logger.logp(Level.WARNING, getClass().getName(), "<constructor>", "Creating Merged Resource: Resource key already exists");
              }

              // Add bundle data to contents
              contents.put(key, bundle.getObject(key));
            }

            // Add bundle to processed list
            processedBundles.add(bundle);
          }
        }
      }
    }
  }

  /**
    Returns an object from the bundle based on a given key.
    @see java.util.ResourceBundle#handleGetObject
  */
  protected Object handleGetObject(String key) throws MissingResourceException {
    return contents.get(key);
  }

  /**
    Returns an enumeration of all bundle keys.
    @see java.util.ResourceBundle#getKeys
  */
  public Enumeration<String> getKeys() {

    // Initialize return value
    Enumeration<String> keys = null;

    if (contents.size() > 0) {
      keys = Collections.enumeration(contents.keySet());
    }

    return keys;
  }

}