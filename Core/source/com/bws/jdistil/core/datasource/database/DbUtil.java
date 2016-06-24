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
import com.bws.jdistil.core.configuration.FieldValues;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
  Utility class providing database related services for opening connections,
  closing connections, committing and rolling back transactions, closing
  SQL statements and result sets, retrieving data as different data types
  from a SQL result set and setting SQL parameters of different data types in a
  prepared SQL statement.
  @author - Bryan Snipes
*/
public class DbUtil {

  /**
    Class name attribute used in messaging.
  */
  private static String className = "com.bws.jdistil.core.datasource.database.DbUtil";

  /**
    String type constant.
  */
  public static final int STRING = 0;

  /**
    Boolean type constant.
  */
  public static final int BOOLEAN = 1;

  /**
    Date type constant.
  */
  public static final int DATE = 2;

  /**
    Time type constant.
  */
  public static final int TIME = 3;

  /**
    Timestamp type constant.
  */
  public static final int TIMESTAMP = 4;

  /**
    Byte type constant.
  */
  public static final int BYTE = 5;

  /**
    Short type constant.
  */
  public static final int SHORT = 6;

  /**
    Integer type constant.
  */
  public static final int INTEGER = 7;

  /**
    Long type constant.
  */
  public static final int LONG = 8;

  /**
    Float type constant.
  */
  public static final int FLOAT = 9;

  /**
    Double type constant.
  */
  public static final int DOUBLE = 10;

  /**
    Ascii stream type constant.
  */
  public static final int ASCII_STREAM = 11;

  /**
    Binary stream type constant.
  */
  public static final int BINARY_STREAM = 12;

  /**
    Character stream type constant.
  */
  public static final int CHARACTER_STREAM = 13;

  /**
    Binary array type constant.
  */
  public static final int BYTE_ARRAY = 14;

  /**
    Creates an empty DbUtil object.&nbsp Private access because all methods are static.
  */
  private DbUtil() {
    super();
  }

  /**
    Returns a pooled database connection using the primary data source defined
    in the core properties file.
    @return Connection - Connection from connection pool.
  */
  public static Connection openConnection() throws DataSourceException {
    return openConnection(null);
  }

  /**
    Returns a pooled database connection using a specified data source name.
    @param dataSourceName - Data source name.
    @return Connection - Connection from connection pool.
  */
  public static Connection openConnection(String dataSourceName) throws DataSourceException {

    // Set method name
    String methodName = "openConnection";

    // Initialize return value
    Connection connection = null;

    // Retrieve default data source name if one is not specified
    if (dataSourceName == null) {
      dataSourceName = ResourceUtil.getString(Constants.DATASOURCE_NAME);
    }

    // Verify data source name
    if (dataSourceName == null) {
      throw new DataSourceException("Invalid null data source name");
    }

    // Retrieve context factory name from core property file
    String contextFactory = ResourceUtil.getString(Constants.INITIAL_CONTEXT_FACTORY);

    // Create context properties
    Properties properties = new Properties();
    
    // Set context factory property
    if (!StringUtil.isEmpty(contextFactory)) {
      properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
    }

    try {
      // Create initial context
      InitialContext initialContext = new InitialContext(properties);

      // Retrieve data source
      DataSource dataSource = (DataSource)initialContext.lookup(dataSourceName);

      // Open connection
      connection = dataSource.getConnection();
    }
    catch (NamingException namingException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Opening DB Connection", namingException);

      throw new DataSourceException(methodName + ": " + namingException.getMessage());
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Opening DB Connection", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }

    return connection;
  }

  /**
    Attempts to close a given database connection.
    @param connection - Connection to be closed.
  */
  public static void closeConnection(Connection connection) {

    // Set method name
    String methodName = "closeConnection";

    try {
      if (connection != null) {
        connection.close();
      }
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Closing DB Connection", sqlException);
    }
  }

  /**
    Attempts to commit any submitted SQL statements using a given connection.
    @param connection - Database connection.
  */
  public static void commit(Connection connection) {

    // Set method name
    String methodName = "commit";

    try {
      // Check for valid connection and commit type
      if (connection != null && !connection.getAutoCommit()) {

        // Commit any submitted statements
        connection.commit();
      }
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Committing SQL Statements", sqlException);
    }
  }

  /**
    Attempts to rollback any submitted SQL statements using a given connection.
    @param connection - Database connection.
  */
  public static void rollback(Connection connection) {

    // Set method name
    String methodName = "rollback";

    try {
      // Check for valid connection and commit type
      if (connection != null && !connection.getAutoCommit()) {

        // Rollback any submitted statements
        connection.rollback();
      }
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Rolling Back SQL Statements", sqlException);
    }
  }

  /**
    Attempts to close a list of SQL statements.
    @param sqlStatements - List of SQL statements.
  */
  public static void closeStatements(List<? extends Statement> sqlStatements) {
    if (sqlStatements != null) {
      for (Statement sqlStatement : sqlStatements) {
        closeStatement(sqlStatement);
      }
    }
  }

  /**
    Attempts to close a list of SQL result sets.
    @param resultSets - List of SQL result sets.
  */
  public static void closeResultSets(List<ResultSet> resultSets) {
    if (resultSets != null) {
      for (ResultSet resultSet : resultSets) {
        closeResultSet(resultSet);
      }
    }
  }

  /**
    Attempts to close a SQL statement.
    @param statement - SQL statement to close.
  */
  public static void closeStatement(Statement statement) {

    // Set method name
    String methodName = "closeStatement";

    try {
      if (statement != null) {
        statement.close();
      }
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Closing SQL Statement", sqlException);
    }
  }

  /**
    Attempts to close a SQL result set.
    @param resultSet - SQL result set to close.
  */
  public static void closeResultSet(ResultSet resultSet) {

    // Set method name
    String methodName = "closeResultSet";

    try {
      if (resultSet != null) {
        resultSet.close();
      }
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Closing SQL Result Set", sqlException);
    }
  }

  /**
    Returns a string value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return String - Column value.
  */
  public static String getString(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    String value = null;

    if (resultSet != null && columnName != null) {
      value = resultSet.getString(columnName);
    }

    return value;
  }

  /**
    Returns a list of string values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<String> getStringList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<String> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<String>();

      // Populate list
      while (resultSet.next()) {
        values.add(getString(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a primitive boolean value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return boolean - Column value.
  */
  public static boolean getBoolean(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    boolean value = false;

    if (resultSet != null && columnName != null) {

      // Get column value
      String columnValue = resultSet.getString(columnName);

      // Set boolean value
      value = columnValue != null && columnValue.equalsIgnoreCase(FieldValues.TRUE);
    }

    return value;
  }

  /**
    Returns a list of boolean values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Boolean> getBooleanList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Boolean> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Boolean>();

      // Populate list
      while (resultSet.next()) {
        boolean value = getBoolean(resultSet, columnName);
        values.add(new Boolean(value));
      }
    }

    return values;
  }

  /**
    Returns a date value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Date - Column value.
  */
  public static Date getDate(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Date value = null;

    if (resultSet != null && columnName != null) {
      value = resultSet.getDate(columnName);
    }

    return value;
  }

  /**
    Returns a list of date values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Date> getDateList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Date> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Date>();

      // Populate list
      while (resultSet.next()) {
        values.add(getDate(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a time value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Time - Column value.
  */
  public static Date getTime(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Date value = null;

    if (resultSet != null && columnName != null) {
      value = resultSet.getTime(columnName);
    }

    return value;
  }

  /**
    Returns a list of time values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Date> getTimeList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Date> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Date>();

      // Populate list
      while (resultSet.next()) {
        values.add(getTime(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a timestamp value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Timestamp - Column value.
  */
  public static Timestamp getTimestamp(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Timestamp value = null;

    if (resultSet != null && columnName != null) {
      value = resultSet.getTimestamp(columnName);
    }

    return value;
  }

  /**
    Returns a list of timestamp values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Timestamp> getTimestampList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Timestamp> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Timestamp>();

      // Populate list
      while (resultSet.next()) {
        values.add(getTimestamp(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a byte value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Byte - Column value.
  */
  public static Byte getByte(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Byte value = null;

    if (resultSet != null && columnName != null) {

      // Get column value
      byte columnValue = resultSet.getByte(columnName);

      // Check for null value
      if (!resultSet.wasNull()) {
        value = new Byte(columnValue);
      }
    }

    return value;
  }

  /**
    Returns a list of byte values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Byte> getByteList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Byte> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Byte>();

      // Populate list
      while (resultSet.next()) {
        values.add(getByte(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a short value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Short - Column value.
  */
  public static Short getShort(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Short value = null;

    if (resultSet != null && columnName != null) {

      // Get column value
      short columnValue = resultSet.getShort(columnName);

      // Check for null value
      if (!resultSet.wasNull()) {
        value = new Short(columnValue);
      }
    }

    return value;
  }

  /**
    Returns a list of short values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Short> getShortList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Short> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Short>();

      // Populate list
      while (resultSet.next()) {
        values.add(getShort(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns an integer value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Integer - Column value.
  */
  public static Integer getInteger(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Integer value = null;

    if (resultSet != null && columnName != null) {

      // Get column value
      int columnValue = resultSet.getInt(columnName);

      // Check for null value
      if (!resultSet.wasNull()) {
        value = new Integer(columnValue);
      }
    }

    return value;
  }

  /**
    Returns a list of integer values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Integer> getIntegerList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Integer> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Integer>();

      // Populate list
      while (resultSet.next()) {
        values.add(getInteger(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a long value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Long - Column value.
  */
  public static Long getLong(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Long value = null;

    if (resultSet != null && columnName != null) {

      // Get column value
      long columnValue = resultSet.getLong(columnName);

      // Check for null value
      if (!resultSet.wasNull()) {
        value = new Long(columnValue);
      }
    }

    return value;
  }

  /**
    Returns a list of long values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Long> getLongList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Long> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Long>();

      // Populate list
      while (resultSet.next()) {
        values.add(getLong(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a float value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Float - Column value.
  */
  public static Float getFloat(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Float value = null;

    if (resultSet != null && columnName != null) {

      // Get column value
      float columnValue = resultSet.getFloat(columnName);

      // Check for null value
      if (!resultSet.wasNull()) {
        value = new Float(columnValue);
      }
    }

    return value;
  }

  /**
    Returns a list of float values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Float> getFloatList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Float> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Float>();

      // Populate list
      while (resultSet.next()) {
        values.add(getFloat(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a double value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return Double - Column value.
  */
  public static Double getDouble(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    Double value = null;

    if (resultSet != null && columnName != null) {

      // Get column value
      double columnValue = resultSet.getDouble(columnName);

      // Check for null value
      if (!resultSet.wasNull()) {
        value = new Double(columnValue);
      }
    }

    return value;
  }

  /**
    Returns a list of double values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<Double> getDoubleList(ResultSet resultSet, String columnName) throws SQLException {

    // Initialize return value
    List<Double> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<Double>();

      // Populate list
      while (resultSet.next()) {
        values.add(getDouble(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a string value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return String - Column value.
  */
  public static String getAsciiStream(ResultSet resultSet, String columnName)
      throws SQLException, IOException {

    // Initialize return value
    String value = null;

    if (resultSet != null && columnName != null) {

      // Retrieve column value as input stream
      InputStream inputStream = resultSet.getAsciiStream(columnName);

      if (inputStream != null) {

        // Create buffer to store data
        StringBuffer buffer = new StringBuffer();

        // Create buffered reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Read first line
        String line = reader.readLine();

        // Continue processing until all lines are read
        while (line != null) {

          // Append line to buffer
          buffer.append(line);

          // Read next line
          line = reader.readLine();
        }

        // Set value
        value = buffer.toString();
      }
    }

    return value;
  }

  /**
    Returns a list of ascii stream values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<String> getAsciiStreamList(ResultSet resultSet, String columnName)
    throws SQLException, IOException {

    // Initialize return value
    List<String> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<String>();

      // Populate list
      while (resultSet.next()) {
        values.add(getAsciiStream(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a string value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return byte[] - Column value.
  */
  public static byte[] getByteStream(ResultSet resultSet, String columnName)
      throws SQLException, IOException {

    // Initialize return value
    byte[] value = null;

    if (resultSet != null && columnName != null) {

      // Retrieve column value as input stream
      InputStream inputStream = resultSet.getBinaryStream(columnName);

      if (inputStream != null) {

        // Create output stream to store data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Create buffered input stream
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        // Create byte buffer
        byte[] buffer = new byte[1024];

        // Read first set of bytes
        int position = bufferedInputStream.read(buffer, 0, buffer.length);

        // Continue processing until all bytes are read
        while (position != -1) {

          // Write bytes to output stream
          outputStream.write(buffer, outputStream.size(), position);

          // Read next set of bytes
          position = bufferedInputStream.read(buffer, position, buffer.length);
        }

        // Set value
        value = outputStream.toByteArray();
      }
    }

    return value;
  }

  /**
    Returns a list of byte stream values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<byte[]> getByteStreamList(ResultSet resultSet, String columnName)
      throws SQLException, IOException {

    // Initialize return value
    List<byte[]> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<byte[]>();

      // Populate list
      while (resultSet.next()) {
        values.add(getByteStream(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a string value using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return String - Column value.
  */
  public static String getCharacterStream(ResultSet resultSet, String columnName)
      throws SQLException, IOException {

    // Initialize return value
    String value = null;

    if (resultSet != null && columnName != null) {

      // Retrieve column value as reader
      Reader reader = resultSet.getCharacterStream(columnName);

      if (reader != null) {

        // Create buffer to store data
        StringBuffer buffer = new StringBuffer();

        // Create buffered reader
        BufferedReader bufferedReader = new BufferedReader(reader);

        // Read first line
        String line = bufferedReader.readLine();

        // Continue processing until all lines are read
        while (line != null) {

          // Append line to buffer
          buffer.append(line);

          // Read next line
          line = bufferedReader.readLine();
        }

        // Set value
        value = buffer.toString();
      }
    }

    return value;
  }

  /**
    Returns a list of character stream values using a given SQL result set and column name.
    @param resultSet - Result set.
    @param columnName - Column name.
    @return List - Column values.
  */
  public static List<String> getCharacterStreamList(ResultSet resultSet, String columnName)
      throws SQLException, IOException {

    // Initialize return value
    List<String> values = null;

    if (resultSet != null && columnName != null) {

      // Create values list
      values = new ArrayList<String>();

      // Populate list
      while (resultSet.next()) {
        values.add(getCharacterStream(resultSet, columnName));
      }
    }

    return values;
  }

  /**
    Returns a value using a given SQL result set, column name, and column type.
    @param resultSet - Result set.
    @param columnName - Column name.
    @param columnType - Column type.
    @return Object - Column value.
  */
  public static Object getObject(ResultSet resultSet, String columnName, int columnType)
      throws SQLException, IOException {

    // Initialize return value
    Object value = null;

    if (resultSet != null) {

      switch (columnType) {
        case STRING:
          value = getString(resultSet, columnName);
          break;
        case BOOLEAN:
          value = new Boolean(getBoolean(resultSet, columnName));
          break;
        case DATE:
          value = getDate(resultSet, columnName);
          break;
        case TIME:
          value = getTime(resultSet, columnName);
          break;
        case TIMESTAMP:
          value = getTime(resultSet, columnName);
          break;
        case BYTE:
          value = getByte(resultSet, columnName);
          break;
        case SHORT:
          value = getShort(resultSet, columnName);
          break;
        case INTEGER:
          value = getInteger(resultSet, columnName);
          break;
        case LONG:
          value = getLong(resultSet, columnName);
          break;
        case FLOAT:
          value = getFloat(resultSet, columnName);
          break;
        case DOUBLE:
          value = getDouble(resultSet, columnName);
          break;
        case ASCII_STREAM:
          value = getAsciiStream(resultSet, columnName);
          break;
        case CHARACTER_STREAM:
          value = getCharacterStream(resultSet, columnName);
          break;
        default:

          // Allow JDBC driver handle conversion
          value = resultSet.getObject(columnName);
          break;
      }
    }

    return value;
  }

  /**
    Sets a SQL parameter of type string using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setString(PreparedStatement statement, int index, String value)
      throws SQLException {

    // Set parameter
    statement.setString(index, value);
  }

  /**
    Sets a SQL parameter of type boolean using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setBoolean(PreparedStatement statement, int index, Boolean value)
      throws SQLException {

    if (statement != null) {

      // Get primitive boolean value
      boolean isTrue = value != null && value.equals(Boolean.TRUE);

      // Set parameter
      statement.setString(index, isTrue ? FieldValues.TRUE : FieldValues.FALSE);
    }
  }

  /**
    Sets a SQL parameter of type date using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setDate(PreparedStatement statement, int index, Date value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.DATE);
      }
      else {
        statement.setDate(index, new java.sql.Date(value.getTime()));
      }
    }
  }

  /**
    Sets a SQL parameter of type time using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setTime(PreparedStatement statement, int index, Date value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.TIME);
      }
      else {
        statement.setTime(index, new java.sql.Time(value.getTime()));
      }
    }
  }

  /**
    Sets a SQL parameter of type timestamp using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setTimestamp(PreparedStatement statement, int index, Date value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.TIMESTAMP);
      }
      else {
        statement.setTimestamp(index, new java.sql.Timestamp(value.getTime()));
      }
    }
  }

  /**
    Sets a SQL parameter of type byte using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setByte(PreparedStatement statement, int index, Byte value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.TINYINT);
      }
      else {
        statement.setByte(index, value.byteValue());
      }
    }
  }

  /**
    Sets a SQL parameter of type short using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setShort(PreparedStatement statement, int index, Short value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.SMALLINT);
      }
      else {
        statement.setShort(index, value.shortValue());
      }
    }
  }

  /**
    Sets a SQL parameter of type integer using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setInteger(PreparedStatement statement, int index, Integer value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.INTEGER);
      }
      else {
        statement.setInt(index, value.intValue());
      }
    }
  }

  /**
    Sets a SQL parameter of type long using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setLong(PreparedStatement statement, int index, Long value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.BIGINT);
      }
      else {
        statement.setLong(index, value.longValue());
      }
    }
  }

  /**
    Sets a SQL parameter of type float using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setFloat(PreparedStatement statement, int index, Float value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.FLOAT);
      }
      else {
        statement.setFloat(index, value.floatValue());
      }
    }
  }

  /**
    Sets a SQL parameter of type double using a prepared SQL statement, an index
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setDouble(PreparedStatement statement, int index, Double value)
      throws SQLException {

    if (statement != null) {

      // Set parameter
      if (value == null) {
        statement.setNull(index, Types.DOUBLE);
      }
      else {
        statement.setDouble(index, value.doubleValue());
      }
    }
  }

  /**
    Sets a SQL parameter of type ascii stream using a prepared SQL statement,
    an index and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setAsciiStream(PreparedStatement statement, int index, String value)
      throws SQLException {

    // Check for null value
    if (value == null) {
      statement.setNull(index, Types.LONGVARCHAR);
    }
    else {

      // Convert value to byte array
      byte[] bytes = value.getBytes();

      // Create byte array input stream
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

      // Set parameter
      statement.setAsciiStream(index, inputStream, bytes.length);
    }
  }

  /**
    Sets a SQL parameter of type binary stream using a prepared SQL statement,
    an index and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setBinaryStream(PreparedStatement statement, int index, byte[] value)
      throws SQLException {

    // Check for null value
    if (value == null) {
      statement.setNull(index, Types.LONGVARBINARY);
    }
    else {

      // Create byte array input stream
      ByteArrayInputStream inputStream = new ByteArrayInputStream(value);

      // Set parameter
      statement.setBinaryStream(index, inputStream, value.length);
    }
  }

  /**
    Sets a SQL parameter of type character stream using a prepared SQL statement,
    an index and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param value - Parameter value.
  */
  public static void setCharacterStream(PreparedStatement statement, int index, String value)
      throws SQLException {

    // Check for null value
    if (value == null) {
      statement.setNull(index, Types.LONGVARCHAR);
    }
    else {

      // Convert value to byte array
      byte[] bytes = value.getBytes();

      // Create byte array input stream
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

      // Set parameter
      statement.setBinaryStream(index, inputStream, bytes.length);
    }
  }

  /**
    Sets a SQL parameter using a prepared SQL statement, an index, a type
    and a value.
    @param statement - Prepared SQL statement.
    @param index - Parameter index.
    @param type - Parameter type.
    @param value - Parameter value.
  */
  public static void setObject(PreparedStatement statement, int index, int type, Object value)
      throws SQLException {

    if (statement != null) {

      switch (type) {
        case STRING:
          setString(statement, index, (String)value);
          break;
        case BOOLEAN:
          setBoolean(statement, index, (Boolean)value);
          break;
        case DATE:
          setDate(statement, index, (Date)value);
          break;
        case TIME:
          setTime(statement, index, (Date)value);
          break;
        case TIMESTAMP:
          setTime(statement, index, (Date)value);
          break;
        case BYTE:
          setByte(statement, index, (Byte)value);
          break;
        case SHORT:
          setShort(statement, index, (Short)value);
          break;
        case INTEGER:
          setInteger(statement, index, (Integer)value);
          break;
        case LONG:
          setLong(statement, index, (Long)value);
          break;
        case FLOAT:
          setFloat(statement, index, (Float)value);
          break;
        case DOUBLE:
          setDouble(statement, index, (Double)value);
          break;
        case ASCII_STREAM:
          setAsciiStream(statement, index, (String)value);
          break;
        case BINARY_STREAM:
          setBinaryStream(statement, index, (byte[])value);
          break;
        case CHARACTER_STREAM:
          setCharacterStream(statement, index, (String)value);
          break;
        default:

          // Allow JDBC driver handle conversion
          statement.setObject(index, value);
          break;
      }
    }
  }

  /**
    Returns a SQL condition using a given column name, column type, value,
    a string containing valid wildcard characters, and an Oracle database
    indicator.&nbsp Column type must be a valid type defined by the java.sql.Types
    class.

    Example 1:
    getCondition("customer.name", Types.VARCHAR, "J*hn Smith", "*", true, false)
    returns: "customer.name like \'j%hn smith\'"

    Example 2:
    getCondition("customer.birth_date", Types.DATE, "10/X2/2%X1", "*XY", true, true)
    returns: "to_char(customer.birth_date, 'mm-dd-yyyy') like '10-%-%'"

    @param columnName - Database column name. example: "table_name.column_name"
    @param columnType - Column types based on constants in java.sql.Types.
    @param value - Value that the user entered.
    @param wildCards - Additional wildcards (besides %).
    @param convertLowerCase - Lower case return value indicator.
    @param isOracle - Oracle database indicator.
    @return String - SQL condition.
    @throws com.bws.jdistil.core.util.DataSourceException
  */
  public static String getCondition(String columnName, int columnType,
      String value, String wildCards, boolean convertLowerCase, boolean isOracle)
      throws DataSourceException {

    // Initialize return variable
    String sqlCondition = null;

    // Check for null value first
    if (value == null) {
      sqlCondition = columnName + " is null";
    }
    else {

      // Remove white space
      value = value.trim();

      // Convert to lowercase if necessary
      if (convertLowerCase) {
        value = value.toLowerCase();
        wildCards = wildCards == null ? wildCards : wildCards.toLowerCase();
      }

      // Check for any specified wildcards
      if (wildCards != null && wildCards.length() > 0) {

        // Convert to character array
        char[] wildCharacters = wildCards.toCharArray();

        // Replace wildcards with SQL wildcards
        for (int index = 0; index < wildCharacters.length; index++) {
          value = value.replace(wildCharacters[index], '%');
        }
      }

      // Check for date and time column types
      if (columnType == Types.DATE || columnType == Types.TIME  || columnType == Types.TIMESTAMP) {

        // Get date condition
        sqlCondition = getDateTimeCondition(columnName, columnType, value, isOracle);
      }
      else {

        // Add delimiters to character column types
        if (columnType == Types.CHAR || columnType == Types.VARCHAR || columnType == Types.LONGVARCHAR) {
          value = "'" + value + "'";
        }

        // Set operator based on existence of wildcards
        String operator = value.indexOf('%') > -1 ? " like " : " = ";

        // Build condition
        sqlCondition = columnName + operator + value;
      }
    }

    return sqlCondition;
  }

  /**
    Takes a date, time, or timestamp value and a column name and generates
    a sql string that can be appended to a where statement.

    Example:
    getDateTimeCondition("customer.last_updated", Types.DATE, "1/1/2001", true)
    return: "to_char(customer.last_updated, 'MM-DD-YYYY') like '01-01-2001'"

    @param columnName - Database column name. example: "table_name.column_name"
    @param columnType - Column types based on constants in java.sql.Types.
    @param value - Date value.
    @param isOracle - Oracle database indicator.
    @return String - SQL condition.
    @throws com.bws.jdistil.core.util.DataSourceException
  */
  private static String getDateTimeCondition(String columnName, int columnType,
      String value, boolean isOracle) throws DataSourceException {

    // Initialize return value
    String sqlCondition = "";

    // Replace slashes so only one date format has to be checked
    value = value.replace('/', '-');

    // Break up the string on any '-' characters
    StringTokenizer tokenizer = new StringTokenizer(value, "-");

    while (tokenizer.hasMoreElements()) {

      // Get next element
      String element = (String)tokenizer.nextElement();

      // Append "-" if not first element
      if (sqlCondition.length() > 0) {
        sqlCondition += "-";
      }

      // Check to see if any wildcards are in the input
      if (element.indexOf("%") == -1) {

        // Figure out if padding is needed
        String intVal = String.valueOf((new Integer(element)).intValue());

        // Pad any single numeric values
        if (intVal.length() == 1) {
          intVal = "0" + intVal;
        }

        // Add the date value
        sqlCondition += intVal;
      }
      else {

        // This element has a wildcard, so append only a wildcard value
        sqlCondition += "%";
      }
    }

    // Break up the string on any ':' characters
    tokenizer = new StringTokenizer(value, ":");

    while (tokenizer.hasMoreElements()) {

      // Get next element
      String element = (String)tokenizer.nextElement();

      // Append ":" if not first element
      if (sqlCondition.length() > 0) {
        sqlCondition += ":";
      }

      // Check to see if any wildcards are in the input
      if (element.indexOf("%") == -1) {

        // Figure out if padding is needed
        String intVal = String.valueOf((new Integer(element)).intValue());

        // Pad any single numeric values
        if (intVal.length() == 1) {
          intVal = "0" + intVal;
        }

        // Add the date value
        sqlCondition += intVal;
      }
      else {

        // This element has a wildcard, so append only a wildcard value
        sqlCondition += "%";
      }
    }

    // Delimit SQL condition with quotes
    sqlCondition = "'" + sqlCondition + "'";

    // Set operator based on existence of wildcards
    String operator = sqlCondition.indexOf('%') > -1 ? " like " : " = ";

    if (isOracle) {

      // Initialize format to Oracle date format
      String format = "'mm-dd-yyyy'";

      // Set format based on time related column types
      if (columnType == Types.TIME) {
        format = "'hh:mi:ss'";
      }
      else if (columnType == Types.TIMESTAMP) {
        format = "'mm-dd-yyyy hh:mi:ss'";
      }

      // Return Oracle condition
      sqlCondition = "to_char(" + columnName + ", " + format + ")" + operator + sqlCondition;
    }
    else {

      // Return non-Oracle condition
      sqlCondition = columnName + operator + sqlCondition ;
    }

    return sqlCondition;
  }

  /**
    Creates and executes a query defined by a SQL string and returns a mapped
    result object encapsulating the results.
    @param sqlString - SQL query string.
    @return MappedResult - Mapped result object.
    @throws java.sql.SQLException
  */
  public static MappedResult executeQuery(String name, String sqlString)
      throws DataSourceException {

    // Set method name
    String methodName = "SqlUtil.executeQuery: ";

    // Intialize return value
    MappedResult mappedResult;

    // Initialize processing variables
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    ResultSet resultSet = null;

    try {
      // Open connection
      connection = DbUtil.openConnection();

      // Prepare statement
      preparedStatement = connection.prepareStatement(sqlString);

      // Execute query
      resultSet = preparedStatement.executeQuery();

      // Create mapped result
      mappedResult = new MappedResult(name, resultSet);
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, className, methodName, "Executing Query", sqlException);

      throw new DataSourceException(methodName + ":" + sqlException.getMessage());
    }
    finally {

      // Close result set, statement and connection
      DbUtil.closeResultSet(resultSet);
      DbUtil.closeStatement(preparedStatement);
      DbUtil.closeConnection(connection);
    }

    return mappedResult;
  }

}