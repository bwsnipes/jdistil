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
package com.bws.jdistil.core.datasource.database.sequence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.security.IDomain;

/**
 * Sequence provider that provides sequential values for a specified table and column
 * using a database sequence.
 * @author - Bryan Snipes
 */
public class SequenceBasedSequenceProvider implements ISequenceProvider {

  /**
   * Class name attribute used in messaging.
   */
  private static String className = "com.bws.jdistil.core.datasource.database.sequence.SequenceBasedSequenceProvider";
  
  /**
   * Creates a new instance of the SequenceBasedSequenceProvider class.
   */
  public SequenceBasedSequenceProvider() {
    super();
  }
  
  /**
   * Increments the sequence value for a specified table and column using a database sequence.
   * @param tableName - Table name.
   * @param columnName - Column name.
   * @param domain Target domain.
   * @return int - Next identity value.
   * @throws com.bws.jdistil.core.datasource.DataSourceException
   */
  @Override
  public int nextValue(String tableName, String columnName, IDomain domain) throws DataSourceException {
  
    // Set method name
    String methodName = "nextValue";
    
    // Check for valid table name
    if (tableName == null) {
      throw new DataSourceException("Invalid null table name.");
    }
  
    // Check for valid column name
    if (columnName == null) {
      throw new DataSourceException("Invalid null column name.");
    }
  
    // Convert table and column names to lower case
    tableName = tableName.toLowerCase();
    columnName = columnName.toLowerCase();
  
    // Initialize return value
    int sequenceValue = 1;
  
    // Initialize processing variables
    Connection connection = null;
    PreparedStatement sqlStatement = null;
    ResultSet resultSet = null;
  
    // Open non-transaction based connection
    if (domain == null) {
      connection = DbUtil.openConnection();
    }
    else {
    	String dataSourceName = domain.getDatasourceName();
      connection = DbUtil.openConnection(dataSourceName);
    }

    try {
      // Create SQL statement
      String sqlText = buildSql(tableName, columnName);
      sqlStatement = connection.prepareStatement(sqlText);
      
      // Execute SQL statement
      resultSet = sqlStatement.executeQuery();
  
      if (resultSet != null && resultSet.next()) {
  
        // Set identity value
        sequenceValue = resultSet.getInt(0);
      }
    }
    catch (SQLException sqlException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.sequence");
      logger.logp(Level.SEVERE, className, methodName, "Getting Next Value", sqlException);
  
      throw new DataSourceException("nextValue:" + sqlException.getMessage());
    }
    finally {
  
      // Close statement and result set
      DbUtil.closeStatement(sqlStatement);
      DbUtil.closeResultSet(resultSet);
      
      // Close connection
      DbUtil.closeConnection(connection);
    }
  
    return sequenceValue;
  }
  
  /**
   * Returns the SQL used to retrieve the next value from a database sequence based on a specified table name and column name.
   * @param tableName - Table name.
   * @param columnName - Column name.
   * @return String SQL used to retrieve next value from a database sequence.
   */
  private String buildSql(String tableName, String columnName) {
  
  	return "select NEXT VALUE FOR " + buildSequenceName(tableName, columnName);
  }
  
  /**
   * Returns the database sequence name based on a specified table name and column name.
   * @param tableName - Table name.
   * @param columnName - Column name.
   * @return String Database sequence name.
   */
  private String buildSequenceName(String tableName, String columnName) {
  
  	return tableName + "_" + columnName + "_seq";
  }
  
}
