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

import java.util.List;
import java.util.Map;

/**
  Class representing a SQL join.
  @author - Bryan Snipes
*/
public class Join implements ISqlGenerator {

  /**
    Join type.
  */
  private String type = null;

  /**
    Source table name.
  */
  private String sourceTableName = null;

  /**
    Target table name.
  */
  private String targetTableName = null;

  /**
    Join condition.
  */
  private IJoinCondition joinCondition = null;

  /**
    Inner join constant.
  */
  public static final String INNER_JOIN = "inner join";

  /**
    Full join constant.
  */
  public static final String FULL_JOIN = "full join";

  /**
    Left outer join constant.
  */
  public static final String LEFT_OUTER_JOIN = "left outer join";

  /**
    Right outer join constant.
  */
  public static final String RIGHT_OUTER_JOIN = "right outer join";

  /**
    Creates a new Join object.
    @param type - Join type.
    @param sourceTableName - Source table name.
    @param targetTableName - Target table name.
    @param joinCondition - Join condition.
  */
  public Join(String type, String sourceTableName, String targetTableName, IJoinCondition joinCondition) {
    super();

    // Validate properties
    if (!isValidType(type)) {
      throw new IllegalArgumentException("Invalid join type.");
    }
    if (StringUtil.isEmpty(sourceTableName)) {
      throw new IllegalArgumentException("Invalid null source table name.");
    }
    if (StringUtil.isEmpty(targetTableName)) {
      throw new IllegalArgumentException("Invalid null target table name.");
    }
    if (joinCondition == null) {
      throw new IllegalArgumentException("Invalid null join condition.");
    }

    // Get all table names defined by join conditions
    List<String> tableNames = joinCondition.getTableNames();

    for (String tableName : tableNames) {

      if (!tableName.equalsIgnoreCase(sourceTableName) && !tableName.equalsIgnoreCase(targetTableName)) {
        throw new IllegalArgumentException("Invalid table name defined in join condition. Must be source or target table defined in join.");
      }
    }

    // Set properties
    this.type = type;
    this.sourceTableName = sourceTableName;
    this.targetTableName = targetTableName;
    this.joinCondition = joinCondition;
  }

  /**
    Returns a value indicating whether or not a given join type is valid.
    @param type - Join type.
    @return boolean - Valid join type indicator.
  */
  protected boolean isValidType(String type) {
    return type != null &&
        (type.equalsIgnoreCase(INNER_JOIN) || type.equalsIgnoreCase(FULL_JOIN) ||
        type.equalsIgnoreCase(LEFT_OUTER_JOIN) || type.equalsIgnoreCase(RIGHT_OUTER_JOIN));
  }

  /**
    Returns the SQL text represented by the join using a map of aliases.
    @see com.bws.jdistil.core.datasource.database.ISqlGenerator#generateSql
  */
  public String generateSql(Map<String, String> aliases) {

    // Initialize return value
    StringBuffer sqlText = new StringBuffer();

    // Initialize target alias
    String targetAlias = null;

    // Attempt to get target alias
    if (aliases != null) {

      if (aliases.containsKey(targetTableName)) {
        targetAlias = (String)aliases.get(targetTableName);
      }
    }

    // Append join type
    sqlText.append(type);
    sqlText.append(" ");

    // Append target table
    sqlText.append(targetTableName);
    sqlText.append(" ");
    sqlText.append(StringUtil.convertNull(targetAlias));
    sqlText.append(" on ");

    // Append join conditions
    sqlText.append(joinCondition.generateSql(aliases));

    return sqlText.toString();
  }

  /**
    Returns the source table name.
    @return String - Source table name.
  */
  protected String getSourceTableName() {
    return sourceTableName;
  }

  /**
    Returns the target table name.
    @return String - Target table name.
  */
  protected String getTargetTableName() {
    return targetTableName;
  }
}
