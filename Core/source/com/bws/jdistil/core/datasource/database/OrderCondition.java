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

import com.bws.jdistil.core.configuration.FieldValues;
import com.bws.jdistil.core.util.StringUtil;

import java.util.Map;

/**
  Class representing a SQL order condition using bound data object information.
  @author - Bryan Snipes
*/
public class OrderCondition implements IOrderCondition {

  /**
    Table name.
  */
  private String tableName = null;

  /**
    Column name.
  */
  private String columnName = null;

  /**
    Order direction.
  */
  private String direction = FieldValues.SORT_ASCENDING;

  /**
    Creates a new OrderCondition object.
    @param tableName - Table name.
    @param columnName - Column name.
  */
  public OrderCondition(String tableName, String columnName) {
    this(tableName, columnName, null);
  }

  /**
    Creates a new OrderCondition object.
    @param tableName - Table name.
    @param columnName - Column name.
    @param direction - Operator.
  */
  public OrderCondition(String tableName, String columnName, String direction) {
    super();

    // Validate properties
    if (StringUtil.isEmpty(tableName)) {
      throw new IllegalArgumentException("Invalid null table name.");
    }
    if (StringUtil.isEmpty(columnName)) {
      throw new IllegalArgumentException("Invalid null column name.");
    }

    // Set properties
    this.tableName = tableName;
    this.columnName = columnName;

    // Set direction if specified
    if (!StringUtil.isEmpty(direction) && (direction.equals(FieldValues.SORT_ASCENDING) || direction.equals(FieldValues.SORT_DESCENDING))) {
      this.direction = direction;
    }
  }

  /**
    Returns the SQL text represented by the order condition using a map of aliases.
    @see com.bws.jdistil.core.datasource.database.ISqlGenerator#generateSql
  */
  public String generateSql(Map<String, String> aliases) {

    // Initialize return value
    StringBuffer sqlText = new StringBuffer();

    // Initialize alias to table name
    String alias = tableName;

    // Attempt to get alias
    if (aliases != null && aliases.containsKey(tableName)) {
      alias = aliases.get(tableName);
    }

    // Build SQL text
    if (direction.equals(FieldValues.SORT_ASCENDING)) {
      sqlText.append(alias).append(".").append(columnName).append(" ").append(" asc ");
    }
    else {
      sqlText.append(alias).append(".").append(columnName).append(" ").append(" desc ");
    }
    
    return sqlText.toString();
  }

  /**
		Returns a value indicating whether or not the order condition references a given table name.
	  @param tableName Table name.
	  @return boolean Referenced table indicator.
	*/
	public boolean isTableReferenced(String tableName) {
		return tableName != null && tableName.equalsIgnoreCase(this.tableName);
	}
  
}
