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

/**
  Data object used to store group data.
  @author Bryan Snipes
*/
public class Group extends DataObject<Integer> implements IListItem, Comparable<Group> {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -5890588750396881808L;

  /**
    Group name.
  */
  private String name = null;

  /**
    Creates a new Group object.
  */
  public Group() {
    super();
  }

  /**
    Returns the group name.
    @return String Group name.
  */
  public String getName() {
    return name;
  }

  /**
    Sets the group name.
    @param newName New group name.
  */
  public void setName(String newName) {
    updateModifiedStatus(name, newName);
    name = newName;
  }

  /**
    Returns the group ID as the item value.
    @see com.bws.jdistil.core.tag.data.IListItem#getValue
  */
  public Object getValue() {
    return getId();
  }
  
  /**
    Returns the group name as the item description.
    @see com.bws.jdistil.core.tag.data.IListItem#getDescription
  */
  public String getDescription() {
    return name;
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
    return Boolean.FALSE;
  }
  
  /**
    Compares two group objects based on their names.
    @see Comparable#compareTo
  */
  public int compareTo(Group group) {

    // Initialize return value to greater than
    int status = 1;

    if (group != null) {

      // Get current name
      String name = getName();

      // Get parameter name
      String parameterName = group.getName();

      // Compare group names if both are not null
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
