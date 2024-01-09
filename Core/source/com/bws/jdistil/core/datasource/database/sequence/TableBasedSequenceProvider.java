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
 * using a database table to maintain the sequential values.
 * @author - Bryan Snipes
 */
public class TableBasedSequenceProvider implements ISequenceProvider {

  /**
    Class name attribute used in messaging.
  */
  private static String className = "com.bws.jdistil.core.datasource.database.sequence.TableBasedSequenceProvider";
  
  /**
   * Table name used to manage sequential values.
   */
  private static final String SEQUENCE_TABLE_NAME = "bws_id_lookup";
  
  /**
   * Creates a new instance of the TableBasedSequenceProvider class.
   */
  public TableBasedSequenceProvider() {
    super();
  }
  
  /**
    Increments the sequence value for a specified table and column by a value of
    one and returns the incremented value. Table names and columns not
    found in the sequence reference table will be added with an initial
    sequence value of one.
    @param tableName - Table name.
    @param columnName - Column name.
    @param domain Target domain.
    @return int - Next identity value.
    @throws com.bws.jdistil.core.datasource.DataSourceException
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
      // Turn off auto commit
      connection.setAutoCommit(false);
    
      // Create SQL statement
      String sqlText = "select max_value from " + SEQUENCE_TABLE_NAME +
          " where lower(table_name) = ? and lower(column_name) = ? for update";
      sqlStatement = connection.prepareStatement(sqlText);
  
      // Set SQL parameters
      int index = 1;
      DbUtil.setString(sqlStatement, index++, tableName);
      DbUtil.setString(sqlStatement, index++, columnName);
  
      // Execute SQL statement
      resultSet = sqlStatement.executeQuery();
  
      if (resultSet != null && resultSet.next()) {
  
        // Set identity value
        sequenceValue = resultSet.getInt("max_value") + 1;
  
        // Increment identity value
        updateSequence(connection, tableName, columnName, sequenceValue);
      }
      else {
  
        // Add new table and column - Sequence already defaulted to value of one
        addSequence(connection, tableName, columnName);
      }
      
      // Commit the transaction
      DbUtil.commit(connection);
    }
    catch (SQLException sqlException) {
    	
    	// Rollback the transaction
    	DbUtil.rollback(connection);
  
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
    Inserts a new table sequence record using a given table name and column name.
    @param connection - Database connection.
    @param tableName - Table name.
    @param columnName - Column name.
    @throws com.bws.jdistil.core.datasource.DataSourceException
  */
  private void addSequence(Connection connection, String tableName, String columnName)
      throws DataSourceException {
  
    // Set method name
    String methodName = "addSequence";
    
    // Initialize processing variables
    PreparedStatement sqlStatement = null;
  
    try {
     // Create SQL statement
      String sqlText = "insert into " + SEQUENCE_TABLE_NAME +
          " (table_name, column_name, max_value) values(?, ?, ?)";
      sqlStatement = connection.prepareStatement(sqlText);
  
      // Set SQL parameters
      int index = 1;
      DbUtil.setString(sqlStatement, index++, tableName);
      DbUtil.setString(sqlStatement, index++, columnName);
      DbUtil.setInteger(sqlStatement, index++, Integer.valueOf(1));
  
      // Execute SQL statement
      sqlStatement.executeUpdate();
    }
    catch (SQLException sqlException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.sequence");
      logger.logp(Level.SEVERE, className, methodName, "Adding Sequence", sqlException);
  
      throw new DataSourceException("addSequence:" + sqlException.getMessage());
    }
    finally {
  
      // Close statement
      DbUtil.closeStatement(sqlStatement);
    }
  }
  
  /**
    Updates the sequence value for a specified table and column using a
    specified sequence value.
    @param connection - Database connection.
    @param tableName - Table name.
    @param columnName - Column name.
    @param sequenceValue - New sequence value.
    @throws com.bws.jdistil.core.datasource.DataSourceException
  */
  private static void updateSequence(Connection connection, String tableName, String columnName, int sequenceValue) 
  		throws DataSourceException {
  
    // Set method name
    String methodName = "updateSequence";
    
    // Initialize processing variables
    PreparedStatement sqlStatement = null;
  
    try {
     // Create SQL statement
      String sqlText = "update " + SEQUENCE_TABLE_NAME +
          " set max_value = ? where table_name = ? and column_name = ?";
      sqlStatement = connection.prepareStatement(sqlText);
  
      // Set SQL parameters
      int index = 1;
      DbUtil.setInteger(sqlStatement, index++, Integer.valueOf(sequenceValue));
      DbUtil.setString(sqlStatement, index++, tableName);
      DbUtil.setString(sqlStatement, index++, columnName);
  
      // Execute SQL statement
      sqlStatement.executeUpdate();
    }
    catch (SQLException sqlException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.sequence");
      logger.logp(Level.SEVERE, className, methodName, "Updating Sequence", sqlException);
  
      throw new DataSourceException("updateSequence:" + sqlException.getMessage());
    }
    finally {
  
      // Close statement
      DbUtil.closeStatement(sqlStatement);
    }
  }
  
}
