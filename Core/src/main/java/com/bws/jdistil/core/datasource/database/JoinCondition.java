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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  Class representing a SQL join condition.
  @author - Bryan Snipes
*/
public class JoinCondition implements IJoinCondition {

  /**
    Source table name.
  */
  private String sourceTableName = null;

  /**
    Source column name.
  */
  private String sourceColumnName = null;

  /**
    Operator.
  */
  private String operator = null;

  /**
    Target table name.
  */
  private String targetTableName = null;

  /**
    Target column name.
  */
  private String targetColumnName = null;

  /**
    Creates a new Condition object.
    @param sourceTableName - Source table name.
    @param sourceColumnName - Source column name.
    @param operator - Operator.
    @param targetTableName - Target table name.
    @param targetColumnName - Target column name.
  */
  public JoinCondition(String sourceTableName, String sourceColumnName, 
      String operator, String targetTableName, String targetColumnName) {
    
	super();

    // Validate properties
    if (StringUtil.isEmpty(sourceTableName)) {
      throw new IllegalArgumentException("Invalid null source table name.");
    }
    if (StringUtil.isEmpty(sourceColumnName)) {
      throw new IllegalArgumentException("Invalid null source column name.");
    }
    if (!Operators.isValidComparative(operator)) {
      throw new IllegalArgumentException("Invalid operator.");
    }
    if (StringUtil.isEmpty(targetTableName)) {
      throw new IllegalArgumentException("Invalid null target table name.");
    }
    if (StringUtil.isEmpty(targetColumnName)) {
      throw new IllegalArgumentException("Invalid null target column name.");
    }

    // Set properties
    this.sourceTableName = sourceTableName;
    this.sourceColumnName = sourceColumnName;
    this.operator = operator;
    this.targetTableName = targetTableName;
    this.targetColumnName = targetColumnName;
  }

  /**
    Returns the SQL text represented by the condition using a map of aliases.
    @see com.bws.jdistil.core.datasource.database.ISqlGenerator#generateSql
  */
  public String generateSql(Map<String, String> aliases) {

    // Initialize return value
    StringBuffer sqlText = new StringBuffer();

    // Initialize aliases to table names
    String sourceAlias = sourceTableName;
    String targetAlias = targetTableName;

    // Attempt to get alias
    if (aliases != null) {

      if (aliases.containsKey(sourceTableName)) {
        sourceAlias = aliases.get(sourceTableName);
      }
      if (aliases.containsKey(targetTableName)) {
        targetAlias = aliases.get(targetTableName);
      }
    }

    // Build SQL text
    sqlText.append(sourceAlias).append(".").append(sourceColumnName);
    sqlText.append(operator);
    sqlText.append(targetAlias).append(".").append(targetColumnName);

    return sqlText.toString();
  }

  /**
    Returns a list of all table names defined in the join condition.
    @see com.bws.jdistil.core.datasource.database.IJoinCondition#getTableNames
  */
  public List<String> getTableNames() {

    // Create table names list
    List<String> tableNames = new ArrayList<String>();

    // Add source and target table names
    tableNames.add(sourceTableName);
    tableNames.add(targetTableName);

    return tableNames;
  }

}
