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
package com.bws.jdistil.security.role;

import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.tag.data.IListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
  Data object used to store role data.
  @author Bryan Snipes
*/
public class Role extends DataObject<Integer> implements IListItem, Comparable<Role> {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -4878480185656158994L;

  /**
    Role name.
  */
  private String name = null;

  /**
    Deleted indicator.
  */
  private Boolean isDeleted = Boolean.FALSE;
  
  /**
    List of restricted task IDs.
  */
  private List<Integer> restrictedTaskIds = new ArrayList<Integer>();
  
  /**
    List of restricted field IDs.
  */
  private List<Integer> restrictedFieldIds = new ArrayList<Integer>();
  
  /**
    List of read only field IDs.
  */
  private List<Integer> readOnlyFieldIds = new ArrayList<Integer>();
  
  /**
    Creates a new Role object.
  */
  public Role() {
    super();
  }

  /**
    Returns the role name.
    @return String Role name.
  */
  public String getName() {
    return name;
  }

  /**
    Sets the role name.
    @param newName New role name.
  */
  public void setName(String newName) {
    updateModifiedStatus(name, newName);
    name = newName;
  }

  /**
    Returns the list of restricted task IDs.
    @return List List of restricted task IDs.
  */
  public List<Integer> getRestrictedTaskIds() {
    return Collections.unmodifiableList(restrictedTaskIds);
  }
  
  /**
    Sets the list of restricted task IDs.
    @param newRestrictedTaskIds New list of restricted task IDs.
  */
  public void setRestrictedTaskIds(List<Integer> newRestrictedTaskIds) {
    
    // Clear existing values
    restrictedTaskIds.clear();
    
    // Set new restricted task IDs
    if (newRestrictedTaskIds != null) {
      restrictedTaskIds.addAll(newRestrictedTaskIds);
    }
  }
  

  /**
    Returns the list of restricted field IDs.
    @return List List of restricted field IDs.
  */
  public List<Integer> getRestrictedFieldIds() {
    return Collections.unmodifiableList(restrictedFieldIds);
  }
  
  /**
    Sets the list of restricted field IDs.
    @param newRestrictedFieldIds New list of restricted field IDs.
  */
  public void setRestrictedFieldIds(List<Integer> newRestrictedFieldIds) {
    
    // Clear existing values
    restrictedFieldIds.clear();
    
    // Set new restricted field IDs
    if (newRestrictedFieldIds != null) {
      restrictedFieldIds.addAll(newRestrictedFieldIds);
    }
  }
  
  /**
    Returns the list of read only field IDs.
    @return List List of read only field IDs.
  */
  public List<Integer> getReadOnlyFieldIds() {
    return Collections.unmodifiableList(readOnlyFieldIds);
  }
  
  /**
    Sets the list of read only field IDs.
    @param newReadOnlyFieldIds New list of read only field IDs.
  */
  public void setReadOnlyFieldIds(List<Integer> newReadOnlyFieldIds) {
    
    // Clear existing values
    readOnlyFieldIds.clear();
    
    // Set new read only field IDs
    if (newReadOnlyFieldIds != null) {
      readOnlyFieldIds.addAll(newReadOnlyFieldIds);
    }
  }
  
  /**
    Returns the default value indicator.
    @return Boolean - Default value indicator.
  */
  public Boolean getIsDefault() {
  	return Boolean.FALSE;
  }
  
  /**
    Returns the deleted indicator.
    @return Boolean Deleted indicator.
  */
  public Boolean getIsDeleted() {
    return isDeleted;
  }
  
  /**
    Sets the deleted indicator.
    @param newIsDeleted New deleted indicator.
  */
  public void setIsDeleted(Boolean newIsDeleted) {
    updateModifiedStatus(isDeleted, newIsDeleted);
    isDeleted = newIsDeleted;
  }
  
  /**
    Returns the role ID as the item value.
    @see com.bws.jdistil.core.tag.data.IListItem#getValue
  */
  public Object getValue() {
    return getId();
  }
  
  /**
    Returns the role name as the item description.
    @see com.bws.jdistil.core.tag.data.IListItem#getDescription
  */
  public String getDescription() {
    return name;
  }
  
  /**
    Compares two role objects based on their names.
    @see Comparable#compareTo
  */
  public int compareTo(Role role) {

    // Initialize return value to greater than
    int status = 1;

    if (role != null) {

      // Get current name
      String name = getName();

      // Get parameter name
      String parameterName = role.getName();

      // Compare role names if both are not null
      if (parameterName != null) {
        if (name == null)
          status = -1;  // Less than
        else
          status = name.compareTo(parameterName);
      }
    }

    return status;
  }

}
