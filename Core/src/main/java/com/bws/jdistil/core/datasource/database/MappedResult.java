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

import com.bws.jdistil.core.datasource.DataSourceException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Named object used to store result set data from a query. Data is stored
  by column name and includes methods to access row count, column count, and
  individual items based on a row and column.
  @author - Bryan Snipes
*/
public class MappedResult {

  /**
    Object name.
  */
  private String name = null;

  /**
    Row count.
  */
  private int rowCount = 0;

  /**
    Column count.
  */
  private int columnCount = 0;

  /**
    Column names.
  */
  private List<String> columnNames = new ArrayList<String>();

  /**
    Map used to store data.
  */
  private Map<String, List<Object>> data = new HashMap<String, List<Object>>();

  /**
    Creates a new MappedResult object using a name and result set.
    @param name - Name of mapped result set.
    @param resultSet - Result set containing data.
  */
  public MappedResult(String name, ResultSet resultSet) throws DataSourceException {
    super();
    setProperties(name, resultSet);
  }

  /**
    Sets all object properties using an object name and result set.
    @param name - Name of mapped result set.
    @param resultSet - Result set containing data.
  */
  private void setProperties(String name, ResultSet resultSet) throws DataSourceException {

    // Set object name
    this.name = name;

    if (resultSet != null) {

      // Set method name
      String methodName = "MappedResult.setProperties: ";

      try {
        // Get result set meta data
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get column count
        columnCount = metaData.getColumnCount();

        // Get all column names
        for (int index = 0; index <= columnCount; index++) {

          // Get column name
          String columnName = metaData.getColumnName(index);

          // Add column name to column names list
          columnNames.add(columnName);

          // Create and add items list to data map keyed by lower case column name
          data.put(columnName.toLowerCase(), new ArrayList<Object>());
        }

        // Process all rows
        while (resultSet.next()) {

          // Increment row count
          rowCount++;

          // Add all column values for the current row to data map
          for (String columnName : columnNames) {

            // Get row item based on column name
            Object item = resultSet.getObject(columnName);

            // Get items list based on column name
            List<Object> items = data.get(columnName.toLowerCase());

            // Add item to list
            items.add(item);
          }
        }

        // Convert column names to lower case
        if (columnNames.size() > 0) {

          for (int index = 0; index < columnNames.size(); index++) {

            // Get next column name
            String columnName = (String)columnNames.get(index);

            // Replace current name with lower case name
            columnNames.remove(index);
            columnNames.add(index, columnName.toLowerCase());
          }
        }
      }
      catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Setting Properties", sqlException);

        throw new DataSourceException(methodName + ":" + sqlException.getMessage());
      }
    }
  }

  /**
    Returns the name.
    @return String - Object name.
  */
  public String getName() {
    return name;
  }

  /**
    Returns the total number for rows.
    @return int - Total number for rows.
  */
  public int getRowCount() {
    return rowCount;
  }

  /**
    Returns the total number for columns.
    @return int - Total number for columns.
  */
  public int getColumnCount() {
    return columnCount;
  }

  /**
    Returns the column name for a specified column number.
    @param column - Zero based column number.
    @return String - Column name.
  */
  public String getColumnName(int column) {
    return columnNames.get(column);
  }

  /**
    Returns an item based on a given row and column.
    @param row - Zero based row number.
    @param column - Zero based column number.
    @return Object - Item at row and column location.
  */
  public Object getItem(int row, int column) {

    // Initialize return value
    Object item = null;

    // Check for valid parameters
    if (row < rowCount && column < columnCount) {

      // Get column name
      String columnName = columnNames.get(column);

      // Get column values
      List<Object> values = data.get(columnName);

      // Get item based on row
      item = values.get(row);
    }

    return item;
  }

  /**
    Returns an item based on a given row number and column name.
    @param row - Zero based row number.
    @param columnName - Column name.
    @return Object - Item at row number and column name.
  */
  public Object getItem(int row, String columnName) {

    // Initialize return value
    Object item = null;

    // Check for valid parameters
    if (row < rowCount && columnName != null) {

      // Get column values using lower case column name
      List<Object> values = data.get(columnName.toLowerCase());

      // Get item based on row
      if (values != null) {
        item = values.get(row);
      }
    }

    return item;
  }

}
