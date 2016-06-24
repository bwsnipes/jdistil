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

/**
  Defines the binding between a data object property and a data object ID binding.
  @author - Bryan Snipes
*/
public class AssociateBinding {

  /**
    Property name.
  */
  private String propertyName = null;

  /**
    Collection indicator.
  */
  private boolean isCollection = false;

  /**
    Table name.
  */
  private String tableName = null;

  /**
    Parent ID column binding.
  */
  private IdColumnBinding parentIdColumnBinding = null;

  /**
    Assocaite ID column binding.
  */
  private IdColumnBinding associateIdColumnBinding = null;

  /**
    Reference table name used to order results for property binding.
  */
  private String referenceTableName = null;
  
  /**
    Reference ID column name used to order results for this property binding.
  */
  private String referenceIdColumnName = null;
  
  /**
    Reference column name used to order results for property binding.
  */
  private String referenceColumnName = null;
  
  /**
    Creates a new AssociateBinding object.
    @param propertyName - Property name.
    @param isCollection - Collection indicator.
    @param tableName - Table name.
    @param parentIdColumnBinding - Parent ID column binding.
    @param associateIdColumnBinding - Associate ID column binding.
  */
  public AssociateBinding(String propertyName, boolean isCollection, String tableName, IdColumnBinding parentIdColumnBinding, IdColumnBinding associateIdColumnBinding) {
  	this(propertyName, isCollection, tableName, parentIdColumnBinding, associateIdColumnBinding, null, null, null);
  }

  /**
    Creates a new AssociateBinding object using reference ordering.
    @param propertyName - Property name.
    @param isCollection - Collection indicator.
    @param tableName - Table name.
    @param parentIdColumnBinding - Parent ID column binding.
    @param associateIdColumnBinding - Associate ID column binding.
    @param referenceTableName - Reference table name.
    @param referenceIdColumnName - Reference ID column name.
    @param referenceColumnName - Reference column name.
  */
  public AssociateBinding(String propertyName, boolean isCollection, String tableName, IdColumnBinding parentIdColumnBinding, 
  		IdColumnBinding associateIdColumnBinding,	String referenceTableName, String referenceIdColumnName, String referenceColumnName) {
  
    super();
  
    // Validate required parameters
    if (StringUtil.isEmpty(propertyName)) {
      throw new IllegalArgumentException("Invalid null property name.");
    }
    if (tableName == null) {
      throw new IllegalArgumentException("Invalid null table name.");
    }
    if (parentIdColumnBinding == null) {
      throw new IllegalArgumentException("Invalid null parent ID column binding.");
    }
    if (associateIdColumnBinding == null) {
      throw new IllegalArgumentException("Invalid null associate ID column binding.");
    }
  
    // Set required properties
    this.propertyName = propertyName;
    this.isCollection = isCollection;
    this.tableName = tableName;
    this.parentIdColumnBinding = parentIdColumnBinding;
    this.associateIdColumnBinding = associateIdColumnBinding;
    this.referenceTableName = referenceTableName;
    this.referenceIdColumnName = referenceIdColumnName;
    this.referenceColumnName = referenceColumnName;
  }
  
  /**
    Returns the property name.
    @return String - Property name.
  */
  public String getPropertyName() {
    return propertyName;
  }

  /**
    Returns the collection indicator.
    @return boolean - Collection indicator.
  */
  public boolean isCollection() {
    return isCollection;
  }

  /**
    Returns the table name.
    @return String - Table name.
  */
  public String getTableName() {
    return tableName;
  }

  /**
    Returns the parent ID column binding.
    @return IdColumnBinding - Parent ID column binding.
  */
  public IdColumnBinding getParentIdColumnBinding() {
    return parentIdColumnBinding;
  }

  /**
    Returns the associate ID column binding.
    @return IdColumnBinding - Associate ID column binding.
  */
  public IdColumnBinding getAssociateIdColumnBinding() {
    return associateIdColumnBinding;
  }

  /**
    Returns the reference table name.
    @return String - Reference table name.
  */
  public String getReferenceTableName() {
  	return referenceTableName;
  }
  
  /**
    Returns the reference ID column name.
    @return String - Reference ID column name.
  */
  public String getReferenceIdColumnName() {
  	return referenceIdColumnName;
  }
  
  /**
    Returns the reference column name.
    @return String - Reference column name.
  */
  public String getReferenceColumnName() {
  	return referenceColumnName;
  }
  
  /**
    Returns a value indicating whether or not reference ordering is configured.
    @return boolean - Reference ordering indicator.
  */
  public boolean useReferenceOrdering() {
  	return !StringUtil.isEmpty(referenceTableName) && !StringUtil.isEmpty(referenceIdColumnName) && !StringUtil.isEmpty(referenceColumnName);
  }
  
}
