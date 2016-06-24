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
package com.bws.jdistil.core.tag.data;


/**
  Default implementation of the list item interface.
  @author - Bryan Snipes
*/
public class ListItem implements IListItem {

  /**
    Value.
  */
	private Object value = null;
	
  /**
	  Description.
	*/
	private String description = null;
	
  /**
    Default value indicator.
  */
  private Boolean isDefault = Boolean.FALSE;
  
  /**
	  Deleted indicator.
	*/
	private Boolean isDeleted = Boolean.FALSE;
	
	/**
	  Creates a new ListItem object.
	*/
	public ListItem(Object value, String description, Boolean isDefault, Boolean isDeleted) {
		super();
		
    // Validate input parameters
    if (value == null) {
      throw new IllegalArgumentException("Value is required.");
    }
    if (description == null) {
      throw new IllegalArgumentException("Description is required.");
    }
    if (isDeleted == null) {
      throw new IllegalArgumentException("Deleted indicator is required.");
    }
		
    // Set attributes
    this.value = value;
    this.description = description;
    this.isDefault = isDefault; 
    this.isDeleted = isDeleted; 
	}
	
	/**
    Returns the option value.
    @return Object - Option value.
  */
  public Object getValue() {
  	return value;
  }

  /**
    Returns the option description.
    @return String - Option description.
  */
  public String getDescription() {
  	return description;
  }

  /**
    Returns the deleted indicator.
    @return Boolean - Deleted indicator.
  */
  public Boolean getIsDeleted() {
  	return isDeleted;
  }
  
  /**
    Returns the default value indicator.
    @return Boolean - Default value indicator.
  */
  public Boolean getIsDefault() {
  	return isDefault;
  }
  
}
