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

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  This class provides a static method used to retrieve table sequence values.
  The default datasource defined in the core properties file must include an
  ID lookup table defined below. There is no need to pre-populate the table
  with data because rows will be created when non-existent table and column names
  are encountered. The default table name can be overridden in the core
  properties file.
  Default Table Name - id_lookup
  Table Columns - table_name varchar(x), column_name varchar(x), max_value int
  @author - Bryan Snipes
*/
public class Sequencer {

  /**
    Class name attribute used in messaging.
  */
  private static String className = "com.bws.jdistil.core.datasource.database.Sequencer";

  /**
    Default table name.
  */
  private static final String DEFAULT_TABLE_NAME = "id_lookup";

  /**
    Lookup table name.
  */
  private static String lookupTableName = null;

  /**
    Creates a new instance of the Sequencer class.&nbsp Defined with private
    access to prohibit instance creation.
  */
  private Sequencer() {
    super();
  }

  /**
    Sets the lookup table name used by the sequencer.
  */
  static {

    // Lookup custom table name
    String customTableName = ResourceUtil.getString(Constants.SEQUENCER_TABLE_NAME);

    // Set table name
    if (StringUtil.isEmpty(customTableName)) {
      lookupTableName = DEFAULT_TABLE_NAME;
    }
    else {
      lookupTableName = customTableName;
    }
  }

  /**
    Increments the sequence value for a specified table and column by a value of
    one and returns the incremented value. Table names and columns not
    found in the sequence reference table will be added with an initial
    sequence value of one.
    @param tableName - Table name.
    @param columnName - Column name.
    @return int - Next identity value.
    @throws com.bws.jdistil.core.datasource.DataSourceException
  */
  public static int nextValue(String tableName, String columnName) throws DataSourceException {

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

    try {
      // Open connection
      connection = DbUtil.openConnection();
      
      // Create SQL statement
      String sqlText = "select max_value from " + lookupTableName +
          " where lower(table_name) = ? and lower(column_name) = ?";
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
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
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
  private static void addSequence(Connection connection, String tableName, String columnName)
      throws DataSourceException {

    // Set method name
    String methodName = "addSequence";
    
    // Initialize processing variables
    PreparedStatement sqlStatement = null;

    try {
     // Create SQL statement
      String sqlText = "insert into " + lookupTableName +
          " (table_name, column_name, max_value) values(?, ?, ?)";
      sqlStatement = connection.prepareStatement(sqlText);

      // Set SQL parameters
      int index = 1;
      DbUtil.setString(sqlStatement, index++, tableName);
      DbUtil.setString(sqlStatement, index++, columnName);
      DbUtil.setInteger(sqlStatement, index++, new Integer(1));

      // Execute SQL statement
      sqlStatement.executeUpdate();
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
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
  private static void updateSequence(Connection connection, String tableName,
      String columnName, int sequenceValue) throws DataSourceException {

    // Set method name
    String methodName = "updateSequence";
    
    // Initialize processing variables
    PreparedStatement sqlStatement = null;

    try {
     // Create SQL statement
      String sqlText = "update " + lookupTableName +
          " set max_value = ? where table_name = ? and column_name = ?";
      sqlStatement = connection.prepareStatement(sqlText);

      // Set SQL parameters
      int index = 1;
      DbUtil.setInteger(sqlStatement, index++, new Integer(sequenceValue));
      DbUtil.setString(sqlStatement, index++, tableName);
      DbUtil.setString(sqlStatement, index++, columnName);

      // Execute SQL statement
      sqlStatement.executeUpdate();
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Updating Sequence", sqlException);

      throw new DataSourceException("updateSequence:" + sqlException.getMessage());
    }
    finally {

      // Close statement
      DbUtil.closeStatement(sqlStatement);
    }
  }

}
