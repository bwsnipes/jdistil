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
  Defines the binding between a table column and a data object property.
  @author - Bryan Snipes
*/
public class ColumnBinding {

  /**
    Column name.
  */
  private String columnName = null;

  /**
    Column type.
  */
  private int columnType = DbUtil.STRING;

  /**
    Unique member indicator used for duplicate check.
  */
  private boolean isUniqueMember = false;
  
  /**
    Virtual delete indicator. Column type must be boolean.
  */
  private boolean isVirtualDelete = false;

  /**
    Property name.
  */
  private String propertyName = null;

  /**
    Reference table name used to order results for this property binding.
  */
  private String referenceTableName = null;
  
  /**
    Reference ID column name used to order results for this property binding.
  */
  private String referenceIdColumnName = null;

	/**
    Reference column name used to order results for this property binding.
  */
  private String referenceColumnName = null;

  /**
    Creates a new ColumnBinding object.
    @param columnName - Column name.
    @param columnType - Column type.
    @param isUniqueMember - Unique member indicator.
    @param isVirtualDelete - Virtual delete indicator.
    @param propertyName - Property name.
  */
  public ColumnBinding(String columnName, int columnType, boolean isUniqueMember, boolean isVirtualDelete, String propertyName) {
  	this(columnName, columnType, isUniqueMember, isVirtualDelete, propertyName, null, null, null);
  }
  
  /**
    Creates a new ColumnBinding object using reference ordering.
    @param columnName - Column name.
    @param columnType - Column type.
    @param isUniqueMember - Unique member indicator.
    @param isVirtualDelete - Virtual delete indicator.
    @param propertyName - Property name.
    @param referenceTableName - Reference table name.
    @param referenceIdColumnName - Reference ID column name.
    @param referenceColumnName - Reference column name.
  */
  public ColumnBinding(String columnName, int columnType, boolean isUniqueMember, boolean isVirtualDelete, String propertyName, 
  		String referenceTableName, String referenceIdColumnName, String referenceColumnName) {

    super();

    // Validate required parameters
    if (StringUtil.isEmpty(columnName)) {
      throw new IllegalArgumentException("Invalid null column name.");
    }
    if (StringUtil.isEmpty(propertyName)) {
      throw new IllegalArgumentException("Invalid null property name.");
    }
    if (isVirtualDelete && columnType != DbUtil.BOOLEAN) {
      throw new IllegalArgumentException("Invalid column type to support virtual delete.");
    }

    // Set properties
    this.columnName = columnName;
    this.columnType = columnType;
    this.isUniqueMember = isUniqueMember;
    this.isVirtualDelete = isVirtualDelete;
    this.propertyName = propertyName;
    this.referenceTableName = referenceTableName;
    this.referenceIdColumnName = referenceIdColumnName;
    this.referenceColumnName = referenceColumnName;
  }

  /**
    Returns the column name.
    @return String - Column name.
  */
  public String getColumnName() {
    return columnName;
  }

  /**
    Returns the column type.
    @return int - Column type.
  */
  public int getColumnType() {
    return columnType;
  }

  /**
    Returns the unique member indicator.
    @return boolean - Unique member indicator.
  */
  public boolean getIsUniqueMember() {
    return isUniqueMember;
  }
  
  /**
    Returns the virtual delete indicator.
    @return boolean - Virtual delete indicator.
  */
  public boolean getIsVirtualDelete() {
    return isVirtualDelete;
  }

  /**
    Returns the property name.
    @return String - Property name.
  */
  public String getPropertyName() {
    return propertyName;
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
