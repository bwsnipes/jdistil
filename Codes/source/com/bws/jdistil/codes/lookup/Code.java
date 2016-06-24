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
package com.bws.jdistil.codes.lookup;

import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.tag.data.IListItem;

/**
  Data object used to store code data.
  @author Bryan Snipes
*/
public class Code extends DataObject<Integer> implements IListItem, Comparable<Code> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 7844830006130326587L;

  /**
    Code name.
  */
  private String name = null;

  /**
    Default indicator.
  */
  private Boolean isDefault = null;
  
  /**
    Category ID.
  */
  private Integer categoryId = null;

  /**
    Deleted indicator.
  */
  private Boolean isDeleted = null;
  
  /**
    Creates a new Code object.
  */
  public Code() {
    super();
  }

  /**
    Returns the code name.
    @return String Code name.
  */
  public String getName() {
    return name;
  }

  /**
    Sets the code name.
    @param newName New code name.
  */
  public void setName(String newName) {
    updateModifiedStatus(name, newName);
    name = newName;
  }

  /**
    Returns the default value indicator.
    @return Boolean Default value indicator.
  */
  public Boolean getIsDefault() {
    return isDefault;
  }
  
  /**
    Sets the default value indicator.
    @param newIsDefault New default value indicator.
  */
  public void setIsDefault(Boolean newIsDefault) {
    updateModifiedStatus(isDefault, newIsDefault);
    isDefault = newIsDefault;
  }
  
  /**
    Returns the category ID.
    @return Integer Category ID.
  */
  public Integer getCategoryId() {
    return categoryId;
  }

  /**
    Sets the category ID.
    @param newCategoryId New category ID.
  */
  public void setCategoryId(Integer newCategoryId) {
    updateModifiedStatus(categoryId, newCategoryId);
    categoryId = newCategoryId;
  }

  /**
    Returns the code ID as the item value.
    @see com.bws.jdistil.core.tag.data.IListItem#getValue
  */
  public Object getValue() {
    return getId();
  }

  /**
    Returns the code description as the item description.
    @see com.bws.jdistil.core.tag.data.IListItem#getDescription
  */
  public String getDescription() {
    return name;
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
    @param isDeleted Deleted indicator.
  */
  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
  
  /**
    Compares two code objects based on their names.
    @see Comparable#compareTo
  */
  public int compareTo(Code code) {

    // Initialize return value
    int status = 0;

    if (code != null) {

      // Get code names
      String name1 = getName();
      String name2 = code.getName();

      // Compare code names
      if (name1 != null && name2 == null) {
        status = 1;
      }
      else if (name1 == null && name2 != null) {
        status = -1;
      }
      else if (name1 != null && name2 != null) {
        status = name1.compareTo(name2);
      }
    }

    return status;
  }

}
