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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  Class representing multiple SQL value conditions.
  @author - Bryan Snipes
*/
public class ValueConditions implements IValueCondition {

  /**
    List of groups.
  */
  private List<Group> groups = new ArrayList<Group>();

  /**
    Creates a new ValueConditions object using a value condition.
    @param valueCondition - Value condition.
  */
  public ValueConditions(IValueCondition valueCondition) {

    // Create group
    Group group = new Group(valueCondition, null);

    // Add group
    groups.add(group);
  }

  /**
    Adds a value condition.
    @param operator - Operator.
    @param valueCondition - Value condition.
  */
  public void add(String operator, IValueCondition valueCondition) {

    if (operator != null && valueCondition != null) {

      // Create group
      Group group = new Group(valueCondition, operator);

      // Add group
      groups.add(group);
    }
  }

  /**
    Returns the SQL text represented by the group condition using a map of aliases.
    @see com.bws.jdistil.core.datasource.database.ISqlGenerator#generateSql
  */
  public String generateSql(Map<String, String> aliases) {

    // Initialize return value
    StringBuffer sqlText = new StringBuffer();

    // Start grouping
    sqlText.append("(");

    for (Group group : groups) {

      // Get operator and value condition
      String operator = group.getOperator();
      IValueCondition valueCondition = group.getValueCondition();

      // Append logical operator if not first group
      if (operator != null) {
        sqlText.append(operator);
      }

      // Append condition
      sqlText.append(valueCondition.generateSql(aliases));
    }

    // End grouping
    sqlText.append(")");

    return sqlText.toString();
  }

  /**
    Sets parameter values using a given prepared statement.
    @see com.bws.jdistil.core.datasource.database.IValueCondition#setParameters
  */
  public int setParameters(PreparedStatement sqlStatement, int index) throws SQLException {

    // Initialize next parameter index
    int nextIndex = index;

    for (Group group : groups) {

      // Get value condition
      IValueCondition valueCondition = group.getValueCondition();

      // Set parameter value
      nextIndex = valueCondition.setParameters(sqlStatement, nextIndex);
    }

    return nextIndex;
  }

  /**
		Returns a value indicating whether or not the value condition references a given table name.
	  @param tableName Table name.
	  @return boolean Referenced table indicator.
	*/
	public boolean isTableReferenced(String tableName) {
	
		// Initialize return value
		boolean isTableReferenced = false;
		
    for (Group group : groups) {

      // Get value condition
      IValueCondition valueCondition = group.getValueCondition();

      if (valueCondition.isTableReferenced(tableName)) {
      	
        // Set table referenced indicator and stop processing
      	isTableReferenced = true;
      	break;
      }
    }

    return isTableReferenced;
	}

  /**
   	Returns a value indicating whether or not the value condition references a given column name.
    @param tableName Table name.
    @param columnName Column name.
    @return boolean Referenced column indicator.
  */
  public boolean isColumnReferenced(String tableName, String columnName) {
  	
		// Initialize return value
		boolean isColumnReferenced = false;
		
    for (Group group : groups) {

      // Get value condition
      IValueCondition valueCondition = group.getValueCondition();

      if (valueCondition.isColumnReferenced(tableName, columnName)) {
      	
        // Set column referenced indicator and stop processing
      	isColumnReferenced = true;
      	break;
      }
    }

    return isColumnReferenced;
  }
  
	
/**
  Grouping of a value condition and an operator.
*/
private class Group {

  /**
    Value condition.
  */
  private IValueCondition valueCondition = null;

  /**
    Logical operator.
  */
  private String operator = Operators.AND;

  /**
    Creates a new Group object using a value condition and a logical operator.
    @param valueCondition - Value condition.
    @param operator - Operator.
  */
  public Group(IValueCondition valueCondition, String operator) {
    super();

    // Validate properties
    if (valueCondition == null) {
      throw new IllegalArgumentException("Invalid null value condition.");
    }
    if (operator != null && !Operators.isValidLogical(operator)) {
      throw new IllegalArgumentException("Invalid logical operator.");
    }

    // Set properties
    this.valueCondition = valueCondition;
    this.operator = operator;
  }

  /**
    Returns the value condition.
  */
  public IValueCondition getValueCondition() {
    return valueCondition;
  }

  /**
    Returns the operator.
  */
  public String getOperator() {
    return operator;
  }

}

}
