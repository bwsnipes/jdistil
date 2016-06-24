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
package com.bws.jdistil.core.datasource.database;

import com.bws.jdistil.core.util.StringUtil;

import java.util.Collection;

/**
  Defines the binding between a data object property and another data object binding.
  @author Bryan Snipes
*/
public class DependentBinding {

  /**
    Property name.
  */
  private String propertyName = null;

  /**
    Collection indicator.
  */
  private boolean isCollection = false;

  /**
    Data object binding.
  */
  private DataObjectBinding dataObjectBinding = null;

  /**
    Creates a new DependentBinding object. Data object binding must define
    a parent ID column binding and cannot define a virtual delete column binding.
    @param propertyName Property name.
    @param isCollection Collection indicator.
    @param dataObjectBinding Data object binding.
  */
  public DependentBinding(String propertyName, boolean isCollection,
      DataObjectBinding dataObjectBinding) {

    super();

    // Validate required parameters
    if (StringUtil.isEmpty(propertyName)) {
      throw new IllegalArgumentException("Invalid null property name.");
    }
    if (dataObjectBinding == null) {
      throw new IllegalArgumentException("Invalid null data object binding.");
    }
    if (dataObjectBinding.getParentIdColumnBinding() == null) {
      throw new IllegalArgumentException("Invalid null parent ID column binding.");
    }

    // Get column bindings
    Collection<ColumnBinding> columnBindings = dataObjectBinding.getColumnBindings();

    // Validate dependent binding does not contain any virtual delete columns
    for (ColumnBinding columnBinding : columnBindings) {
      if (columnBinding.getIsVirtualDelete()) {
        throw new IllegalArgumentException("Dependent column bindings cannot be marked as virtual delete.");
      }
    }

    // Set required properties
    this.propertyName = propertyName;
    this.isCollection = isCollection;
    this.dataObjectBinding = dataObjectBinding;
  }

  /**
    Returns the property name.
    @return String Property name.
  */
  public String getPropertyName() {
    return propertyName;
  }

  /**
    Returns the collection indicator.
    @return boolean Collection indicator.
  */
  public boolean isCollection() {
    return isCollection;
  }

  /**
    Returns the data object binding.
    @return DataObjectIdBinding Data object binding.
  */
  public DataObjectBinding getDataObjectBinding() {
    return dataObjectBinding;
  }

}
