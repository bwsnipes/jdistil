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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  Class representing multiple SQL join conditions.
  @author - Bryan Snipes
*/
public class JoinConditions implements IJoinCondition {

  /**
    List of groups.
  */
  private List<Group> groups = new ArrayList<Group>();

  /**
    Creates a new JoinConditions object using a join condition.
    @param joinCondition - Join condition.
  */
  public JoinConditions(IJoinCondition joinCondition) {

    // Create group
    Group group = new Group(joinCondition, null);

    // Add group
    groups.add(group);
  }

  /**
    Adds a join condition.
    @param operator - Operator.
    @param joinCondition - Join condition.
  */
  public void add(String operator, IJoinCondition joinCondition) {

    // Create group
    Group group = new Group(joinCondition, operator);

    // Add group
    groups.add(group);
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

    for (int index = 0; index < groups.size(); index++) {

      // Get next group
      Group group = groups.get(index);

      // Get operator and join condition
      String operator = group.getOperator();
      IJoinCondition joinCondition = group.getJoinCondition();

      // Append logical operator if not first group
      if (index != 0) {
        sqlText.append(operator);
      }

      // Append condition
      sqlText.append(joinCondition.generateSql(aliases));
    }

    // End grouping
    sqlText.append(")");

    return sqlText.toString();
  }

  /**
    Returns a list of all table names defined in the join condition.
    @see com.bws.jdistil.core.datasource.database.IJoinCondition#getTableNames
  */
  public List<String> getTableNames() {

    // Create table names list
    List<String> tableNames = new ArrayList<String>();

    for (Group group : groups) {

      // Get join condition
      IJoinCondition joinCondition = group.getJoinCondition();

      // Add table names
      tableNames.addAll(joinCondition.getTableNames());
    }

    return tableNames;
  }


/**
  Grouping of a join condition and an operator.
*/
private class Group {

  /**
    Join condition.
  */
  private IJoinCondition joinCondition = null;

  /**
    Logical operator.
  */
  private String operator = Operators.AND;

  /**
    Creates a new Group object using a join condition and a logical operator.
    @param sqlGenerator - SQL generator.
    @param operator - Operator.
  */
  public Group(IJoinCondition joinCondition, String operator) {
    super();

    // Validate properties
    if (joinCondition == null) {
      throw new IllegalArgumentException("Invalid null join condition.");
    }
    if (operator != null && !Operators.isValidLogical(operator)) {
      throw new IllegalArgumentException("Invalid logical operator.");
    }

    // Set properties
    this.joinCondition = joinCondition;
    this.operator = operator;
  }

  /**
    Returns the join condition.
  */
  public IJoinCondition getJoinCondition() {
    return joinCondition;
  }

  /**
    Returns the operator.
  */
  public String getOperator() {
    return operator;
  }

}

}
