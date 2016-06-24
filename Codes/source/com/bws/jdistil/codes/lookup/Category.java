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
  Data object used to store category data.
  @author Bryan Snipes
*/
public class Category extends DataObject<Integer> implements IListItem, Comparable<Category> {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = 8619674220795950115L;
  
  /**
    Category name.
  */
  private String name = null;

  /**
    Deleted indicator.
  */
  private Boolean isDeleted = null;
  
  /**
    Creates a new Category object.
  */
  public Category() {
    super();
  }

  /**
    Returns the category name.
    @return String Category name.
  */
  public String getName() {
    return name;
  }

  /**
    Sets the category name.
    @param newName New category name.
  */
  public void setName(String newName) {
    updateModifiedStatus(name, newName);
    name = newName;
  }

  /**
    Returns the category ID as the item value.
    @see com.bws.jdistil.core.tag.data.IListItem#getValue
  */
  public Object getValue() {
    return getId();
  }
  
  /**
    Returns the category name as the item description.
    @see com.bws.jdistil.core.tag.data.IListItem#getDescription
  */
  public String getDescription() {
    return name;
  }
  
  /**
    Returns the default value indicator.
    @return Boolean Default value indicator.
  */
  public Boolean getIsDefault() {
    return false;
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
    Compares two category objects based on their names.
    @see Comparable#compareTo
  */
  public int compareTo(Category category) {

    // Initialize return value
    int status = 0;

    if (category != null) {

      // Get category names
      String name1 = getName();
      String name2 = category.getName();

      // Compare category names
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
