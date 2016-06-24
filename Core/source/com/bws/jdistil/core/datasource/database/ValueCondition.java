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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
  Class representing a SQL value condition using bound data object information.
  @author - Bryan Snipes
*/
public class ValueCondition implements IValueCondition {

  /**
    Table name.
  */
  private String tableName = null;

  /**
    Column name.
  */
  private String columnName = null;

  /**
    Operator.
  */
  private String operator = null;

  /**
    Parameter value type.
  */
  private int type = DbUtil.STRING;

  /**
    Parameter value.
  */
  private Object value = null;

  /**
    Creates a new ValueCondition object.
    @param tableName - Table name.
    @param columnName - Column name.
    @param operator - Operator.
    @param type - Parameter type.
    @param value - Parameter value.
  */
  public ValueCondition(String tableName, String columnName, String operator, int type, Object value) {
    super();

    // Validate properties
    if (StringUtil.isEmpty(tableName)) {
      throw new IllegalArgumentException("Invalid null table name.");
    }
    if (StringUtil.isEmpty(columnName)) {
      throw new IllegalArgumentException("Invalid null column name.");
    }
    if (!Operators.isValidComparative(operator)) {
      throw new IllegalArgumentException("Invalid operator.");
    }

    // Set properties
    this.tableName = tableName;
    this.columnName = columnName;
    this.operator = operator;
    this.type = type;
    this.value = value;
  }

  /**
    Returns the SQL text represented by the value condition using a map of aliases.
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

    // Initialize translated operator
    String translatedOperator = operator;
    
    // Translate specific uses of 'like' operator
    if (operator.equals(Operators.BEGINS_WITH) || operator.equals(Operators.ENDS_WITH) || operator.equals(Operators.CONTAINS)) {
      translatedOperator = Operators.LIKE;
    }
    
    // Build SQL text
    sqlText.append(alias).append(".").append(columnName);
    sqlText.append(translatedOperator);
    
    // Append parameter place holder if needed
    if (!Operators.isNullComparative(operator)) {
      sqlText.append("?");
    }

    return sqlText.toString();
  }

  /**
    Sets parameter values using a given prepared statement.
    @see com.bws.jdistil.core.datasource.database.IValueCondition#setParameters
  */
  public int setParameters(PreparedStatement sqlStatement, int index) throws SQLException {

    if (!Operators.isNullComparative(operator)) {
    	
      // Initialize translated value
      Object translatedValue = value;
      
      if (value != null) {
      
        // Translate specific uses of 'like' operator
        if (operator.equals(Operators.BEGINS_WITH)) {
          translatedValue = value.toString() + "%";
        }
        else if (operator.equals(Operators.ENDS_WITH)) {
          translatedValue = "%" + value.toString();
        }
        else if (operator.equals(Operators.CONTAINS)) {
          translatedValue = "%" + value.toString() + "%";
        }
      }
      
      // Set parameter
      DbUtil.setObject(sqlStatement, index++, type, translatedValue);
    }

    // Return next parameter index
    return index;
  }

  /**
 		Returns a value indicating whether or not the value condition references a given table name.
	  @param tableName Table name.
	  @return boolean Referenced table indicator.
	*/
	public boolean isTableReferenced(String tableName) {
		return tableName != null && tableName.equalsIgnoreCase(this.tableName);
	}
  
  /**
   	Returns a value indicating whether or not the value condition references a given column name.
    @param tableName Table name.
    @param columnName Column name.
    @return boolean Referenced column indicator.
  */
  public boolean isColumnReferenced(String tableName, String columnName) {
  	return columnName != null && columnName.equalsIgnoreCase(this.columnName) && isTableReferenced(tableName);
  }

}
