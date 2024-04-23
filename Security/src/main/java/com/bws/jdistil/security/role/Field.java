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
  Data object used to store field data.
  @author Bryan Snipes
*/
public class Field extends DataObject<Integer> implements IListItem, Comparable<Field> {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -5890588750396881808L;

  /**
    Field name.
  */
  private String name = null;

  /**
	  Secure ID.
	*/
	private String secureId = null;
	
  /**
    Group ID.
  */
  private Integer groupId = null;
  
  /**
    Creates a new Field object.
  */
  public Field() {
    super();
  }

  /**
    Returns the field name.
    @return String Field name.
  */
  public String getName() {
    return name;
  }

  /**
    Sets the field name.
    @param newName New field name.
  */
  public void setName(String newName) {
    updateModifiedStatus(name, newName);
    name = newName;
  }

  /**
	  Returns the secure ID.
	  @return String Secure ID.
	*/
	public String getSecureId() {
	  return secureId;
	}
	
	/**
	  Sets the secure ID.
	  @param newSecureId New secure ID.
	*/
	public void setSecureId(String newSecureId) {
	  updateModifiedStatus(secureId, newSecureId);
	  secureId = newSecureId;
	}
	
  /**
    Returns the group ID.
    @return Integer Group ID.
  */
  public Integer getGroupId() {
    return groupId;
  }
  
  /**
    Sets the group ID.
    @param newGroupId New group ID.
  */
  public void setGroupId(Integer newGroupId) {
    updateModifiedStatus(groupId, newGroupId);
    groupId = newGroupId;
  }
  
  /**
    Returns the field ID as the item value.
    @see com.bws.jdistil.core.tag.data.IListItem#getValue
  */
  public Object getValue() {
    return getId();
  }
  
  /**
    Returns the field name as the item description.
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
    Compares two field objects based on their names.
    @see Comparable#compareTo
  */
  public int compareTo(Field field) {

    // Initialize return value to greater than
    int status = 1;

    if (field != null) {

      // Get current secure ID
      String secureId = getSecureId();

      // Get parameter secure ID
      String parameterSecureId = field.getSecureId();

      // Compare field names if both are not null
      if (parameterSecureId != null) {
        if (secureId == null)
          status = -1;  // Less than
        else
          status = secureId.compareTo(parameterSecureId);
      }
    }

    return status;
  }

}
