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
package com.bws.jdistil.core.configuration;

import java.util.Locale;

import com.bws.jdistil.core.resource.ResourceUtil;

/**
  Class defining page specific information.
  @author Bryan Snipes
*/
public class Page {

  /**
    Unique ID.
  */
  private String id = null;

  /**
    Name.
  */
  private String name = null;

  /**
	  Default description.
	*/
	private String defaultDescription = null;
	
  /**
	  Secure indicator.
	*/
	private boolean isSecure = false;
	
  /**
    Creates a new page object using a given ID, name, and secure indicator.
    @param id Unique ID.
    @param name Unique name.
    @param defaultDescription Default description.
    @param isSecure Secure page indicator.
  */
  public Page(String id, String name, String defaultDescription, boolean isSecure) {
    super();

    // Validate required properties
    if (id == null) {
      throw new IllegalArgumentException("Invalid null ID.");
    }
    if (name == null) {
      throw new IllegalArgumentException("Invalid null name.");
    }

    // Set properties
    this.id = id;
    this.name = name;
    this.defaultDescription = defaultDescription;
    this.isSecure = isSecure;
  }

  /**
    Returns the unique page ID.
    @return String Unique page ID.
  */
  public String getId() {
    return id;
  }

  /**
    Returns the name.
    @return String name.
  */
  public String getName() {
    return name;
  }

  /**
	  Returns the description based on a given locale.
	  @param locale Locale.
	  @return String Description.
	*/
	public String getDescription(Locale locale) {
	
	  // Initialize return value
	  String description = null;
	
    // Retrieve description resource names from core resource file
	  String descriptionResources = ResourceUtil.getString(Constants.DESCRIPTION_RESOURCES);
	
	  // Retrieve description
	  if (descriptionResources != null) {
	    description = ResourceUtil.getString(descriptionResources, locale, defaultDescription);
	  }
	
	  // Use default description if one is not found
	  if (description == null) {
	    description = defaultDescription;
	  }
	
	  return description;
	}
	
  /**
    Returns the secure indicator.
    @return boolean Secure indicator.
  */
  public boolean isSecure() {
    return isSecure;
  }

  /**
   * Returns a hash code.
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Compares this page with another instance for equality.
   * @see java.lang.Object#equals
   */
  public boolean equals(Object object) {
    
    // Initialize return value
    boolean isEqual = false;
    
    if (object != null && object instanceof Page) {
     
      // Cast to page
      Page page = (Page)object;
      
      // Compare page IDs
      isEqual = page.getId().equals(id);
    }
    
    return isEqual;
  }
  
}
