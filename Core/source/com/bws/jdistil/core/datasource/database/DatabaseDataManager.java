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

import com.bws.jdistil.core.CoreException;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.DirtyUpdateException;
import com.bws.jdistil.core.datasource.DuplicateException;
import com.bws.jdistil.core.datasource.FilterCriteria;
import com.bws.jdistil.core.datasource.IDataManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Abstract database data manager class implementing a portion of the IDataManager interface.
  @author Bryan Snipes
*/
public abstract class DatabaseDataManager<I, T extends DataObject<I>> implements IDataManager<I, T> {

  /**
    Data source name.
  */
  private String dataSourceName = null;

  /**
    Creates a new DatabaseDataManager object.
  */
  public DatabaseDataManager() {
    this(null);
  }

  /**
    Creates a new DatabaseDataManager object using a datasource name.
    @param dataSourceName Data source name.
  */
  public DatabaseDataManager(String dataSourceName) {
    super();

    // Set properties
    this.dataSourceName = dataSourceName;
  }

  /**
    Validates a given data object for use with the data object manager.
    @param dataObject Data object.
  */
  protected abstract boolean isValidDataObject(T dataObject);

  /**
    Populates the ID before the data object can be inserted into the database.
    @param dataObject Data object.
  */
  protected abstract void initializeId(DataObject<?> dataObject)
      throws DataSourceException;

  /**
    Returns a list of prepared statements used to create a given data object.
    @param dataObject Data object.
    @param connection Database connection.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getCreateSql(T dataObject, Connection connection,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to update a given data object.
    @param dataObject Data object.
    @param connection Database connection.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getUpdateSql(T dataObject, Connection connection,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to delete a given data object.
    @param dataObject Data object.
    @param connection Database connection.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getDeleteSql(T dataObject, Connection connection,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to find all data objects.
    @param connection Database connection.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(Connection connection, List<PreparedStatement> sqlStatements)
      throws DataSourceException;

  /**
    Returns a prepared statement used to find all data object IDs.
    @param connection Database connection.
    @return PreparedStatement Prepared statement.
  */
  protected abstract PreparedStatement getFindIdSql(Connection connection)
      throws DataSourceException;
  
  /**
    Returns a list of prepared statements used to find a given data object.
    @param id Data object ID.
    @param connection Database connection.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(I id, Connection connection,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to find a list of data objects.
    @param ids List of data object IDs.
    @param connection Database connection.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(List<I> ids, Connection connection,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to find a list of data objects using search criteria.
    @param filterCriteria Filter criteria.
    @param connection Database connection.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(FilterCriteria filterCriteria, Connection connection,
      List<PreparedStatement> sqlStatements) throws DataSourceException;
  
  /**
    Returns a prepared statement used to find data object IDs using search criteria.
    @param filterCriteria Filter criteria.
    @param connection Database connection.
    @return PreparedStatement Prepared statement.
  */
  protected abstract PreparedStatement getFindIdSql(FilterCriteria filterCriteria, Connection connection)
      throws DataSourceException;
  
  /**
    Returns a data object using data from a list of result set objects.&nbsp
    Used in conjunction with the find method.
    @param resultSets List of result set objects.
    @return DataObject Data object.
  */
  protected abstract T createDataObject(List<ResultSet> resultSets) throws DataSourceException;

  /**
    Returns a list of data objects using data from a list of result set objects.
    Used in conjunction with the find method.
    @param resultSets List of result set objects.
    @return List List of data objects.
  */
  protected abstract List<T> createDataObjects(List<ResultSet> resultSets) throws DataSourceException;

  /**
    Returns a list of data object IDs using data from a result set object.
    @param resultSet Result set object.
    @return List List of data object IDs.
  */
  protected abstract List<I> createIds(ResultSet resultSet) throws DataSourceException;
  
  /**
    Creates and eturns a database connection.
    @return Connection Database connection.
  */
  protected Connection openConnection() throws DataSourceException {

    // Set method name variable
    String methodName = "openConnection";

    // Initialize return value
    Connection connection = null;

    // Open non-transaction based connection
    if (dataSourceName == null) {
      connection = DbUtil.openConnection();
    }
    else {
      connection = DbUtil.openConnection(dataSourceName);
    }

    try {
      // Turn off auto commit
      connection.setAutoCommit(false);
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Opening Database Connection", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }

    return connection;
  }

  /**
    Returns a value indicating whether or not a given data object is new.
    @param dataObject Data object.
  */
  protected boolean isNewDataObject(T dataObject) {
    return dataObject == null || dataObject.getId() == null;
  }

  /**
    Returns a value indicating whether or not two given data objects are of the same version.
    @param dataObject1 Data object.
    @param dataObject2 Data object.
  */
  protected boolean isSameVersion(T dataObject1, T dataObject2) {
    
    // Get versions
    Long version1 = dataObject1.getVersion();
    Long version2 = dataObject2.getVersion();
    
    return (version1 == null && version2 == null) || 
        (version1 != null && version2 != null && version1.equals(version2));
  }

  /**
    Returns a prepared statement used to check for duplicate data objects.&nbsp
    Considered a duplicate if one or more result sets are created using the
    returned SQL statement.
    @param connection Database connection.
    @param dataObject Data object.
    @return PreparedStatement Prepared statement.
  */
  protected PreparedStatement getDuplicateSql(T dataObject, Connection connection)
      throws DataSourceException {

    // Do not perform duplicate check by default
    return null;
  }

  /**
    Saves a new data object.
    @param dataObject Data object.
    @see com.bws.jdistil.core.datasource.IDataManager#save
  */
  protected void create(T dataObject) throws DataSourceException {

    // Set method name variable
    String methodName = "create";

    // Check for duplicate
    if (isDuplicate(dataObject)) {
      throw new DuplicateException();
    }

    // Set version
    dataObject.setVersion(new Long(1L));
    
    // Initialize processing variables
    Connection connection = null;

    // Create SQL statement list
    List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();
    
    try {
      // Open connection
      connection = openConnection();

      // Initialize ID
      initializeId(dataObject);

      // Retrieve create SQL
      getCreateSql(dataObject, connection, sqlStatements);

      // Validate create SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getCreateSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        sqlStatement.executeUpdate();
      }

      // Commit all SQL statements
      connection.commit();
    }
    catch (CoreException coreException) {

      // Rollback SQL statements
      DbUtil.rollback(connection);

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Creating Data Object", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {

      // Rollback SQL statements
      DbUtil.rollback(connection);

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Creating Data Object", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close all SQL statements
      DbUtil.closeStatements(sqlStatements);

      // Close connection
      DbUtil.closeConnection(connection);
    }
  }

  /**
    Updates an existing data object.
    @see com.bws.jdistil.core.datasource.IDataManager#save
  */
  protected void update(T dataObject, boolean checkDirty) throws DataSourceException {

    // Set method name variable
    String methodName = "update";

    // Get ID
    I id = dataObject.getId();

    if (checkDirty) {

      // Find data object
      T currentDataObject = find(id);

      // Only update data if same version
      if (!isSameVersion(dataObject, currentDataObject)) {
        throw new DirtyUpdateException(currentDataObject);
      }
    }

    // Check for duplicate
    if (isDuplicate(dataObject)) {
      throw new DuplicateException();
    }

    // Increment version
    Long currentVersion = dataObject.getVersion();

    // Update version
    if (currentVersion == null) {
      dataObject.setVersion(new Long(1L));
    }
    else {
      dataObject.setVersion(new Long(currentVersion.longValue() + 1));
    }
    
    // Initialize processing variables
    Connection connection = null;

    // Create SQL statement list
    List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();
    
    try {
      // Open connection
      connection = openConnection();

      // Retrieve update SQL
      getUpdateSql(dataObject, connection, sqlStatements);

      // Validate create SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getUpdateSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        sqlStatement.executeUpdate();
      }

      // Commit all SQL statements
      connection.commit();
    }
    catch (CoreException coreException) {

      // Rollback SQL statements
      DbUtil.rollback(connection);

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Updating Data Object", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {

      // Rollback SQL statements
      DbUtil.rollback(connection);

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Updating Data Object", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close all SQL statements
      DbUtil.closeStatements(sqlStatements);

      // Close connection
      DbUtil.closeConnection(connection);
    }
  }

  /**
    Saves a data object without checking for dirty data.
    @see com.bws.jdistil.core.datasource.IDataManager#save
  */
  public void save(T dataObject) throws DataSourceException {
    save(dataObject, false);
  }

  /**
    Saves a data object and checks for dirty data based on a given indicator.
    @see com.bws.jdistil.core.datasource.IDataManager#save
  */
  public void save(T dataObject, boolean checkDirty) throws DataSourceException {

    // Set method name variable
    String methodName = "save";

    // Validate data object
    if (!isValidDataObject(dataObject)) {
      throw new DataSourceException(methodName + ": Invalid data object.");
    }

    if (isNewDataObject(dataObject)) {

      // Create data object
      create(dataObject);
    }
    else {

      // Update data object
      update(dataObject, checkDirty);
    }
  }

  /**
    Deletes a data object.
    @see com.bws.jdistil.core.datasource.IDataManager#delete
  */
  public void delete(T dataObject) throws DataSourceException {

    // Set method name variable
    String methodName = "delete";

    // Validate ID
    if (!isValidDataObject(dataObject)) {
      throw new DataSourceException(methodName + ": Invalid ID.");
    }

    // Initialize processing variables
    Connection connection = null;

    // Create SQL statement list
    List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();
    
    try {
      // Open connection
      connection = openConnection();

      // Retrieve remove SQL
      getDeleteSql(dataObject, connection, sqlStatements);

      // Validate delete SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getDeleteSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        sqlStatement.executeUpdate();
      }

      // Commit all SQL statements
      connection.commit();
    }
    catch (CoreException coreException) {

      // Rollback SQL statements
      DbUtil.rollback(connection);

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Deleting Data Object", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {

      // Rollback SQL statements
      DbUtil.rollback(connection);

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Deleting Data Object", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close SQL statements
      DbUtil.closeStatements(sqlStatements);

      // Close connection
      DbUtil.closeConnection(connection);
    }
  }

  /**
    Returns all data objects.
    @see com.bws.jdistil.core.datasource.IDataManager#find
  */
  public List<T> find() throws DataSourceException {

    // Set method name variable
    String methodName = "find";

    // Initialize return value
    List<T> dataObjects = null;

    // Initialize processing variables
    Connection connection = null;

    // Create SQL statement list
    List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();
    
    // Create result set list
    List<ResultSet> resultSets = new ArrayList<ResultSet>();
    
    try {
      // Retrieve database connection
      connection = openConnection();

      // Retrieve find SQL
      getFindSql(connection, sqlStatements);

      // Validate find SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getFindSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        resultSets.add(sqlStatement.executeQuery());
      }

      // Create data object list
      if (resultSets.size() > 0) {
        dataObjects = createDataObjects(resultSets);
      }
    }
    catch (CoreException coreException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close all statements and result sets
      DbUtil.closeStatements(sqlStatements);
      DbUtil.closeResultSets(resultSets);

      // Close connnection
      DbUtil.closeConnection(connection);
    }

    return dataObjects;
  }

  /**
    Returns all data object IDs.
    @see com.bws.jdistil.core.datasource.IDataManager#findIds
  */
  public List<I> findIds() throws DataSourceException {
  
    // Set method name variable
    String methodName = "findIds";
  
    // Initialize return value
    List<I> ids = null;
  
    // Initialize processing variables
    Connection connection = null;
  
    // Initialize SQL statement and result set
    PreparedStatement sqlStatement = null;
    ResultSet resultSet = null;
    
    try {
      // Retrieve database connection
      connection = openConnection();
  
      // Retrieve find SQL
      sqlStatement = getFindIdSql(connection);
  
      // Validate find SQL
      if (sqlStatement == null) {
        throw new DataSourceException(methodName + ": No SQL returned from getFindIdSql method.");
      }
  
      // Execute SQL statement
      resultSet = sqlStatement.executeQuery();
  
      // Create ID list
      if (resultSet != null) {
        ids = createIds(resultSet);
      }
    }
    catch (CoreException coreException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Ids", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Ids", sqlException);
  
      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {
  
      // Close all statements and result sets
      DbUtil.closeStatement(sqlStatement);
      DbUtil.closeResultSet(resultSet);
  
      // Close connnection
      DbUtil.closeConnection(connection);
    }
  
    return ids;
  }
  
  /**
    Returns a data object using a given data object ID.
    @see com.bws.jdistil.core.datasource.IDataManager#find
  */
  public T find(I id) throws DataSourceException {

    // Set method name variable
    String methodName = "find";

    // Initialize return value
    T dataObject = null;

    // Initialize processing variables
    Connection connection = null;

    // Create SQL statement list
    List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();
    
    // Create result set list
    List<ResultSet> resultSets = new ArrayList<ResultSet>();

    try {
      // Retrieve database connection
      connection = openConnection();

      // Retrieve find SQL
      getFindSql(id, connection, sqlStatements);

      // Validate find SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getFindSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        resultSets.add(sqlStatement.executeQuery());
      }

      // Create data object
      if (resultSets.size() > 0) {
        dataObject = createDataObject(resultSets);
      }
    }
    catch (CoreException coreException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Object", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Object", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close all statements and result sets
      DbUtil.closeStatements(sqlStatements);
      DbUtil.closeResultSets(resultSets);

      // Close connnection
      DbUtil.closeConnection(connection);
    }

    return dataObject;
  }

  /**
    Returns a list of data objects using a list of data object IDs.
    Order of data objects is maintained based on the order of data object IDs.
    @see com.bws.jdistil.core.datasource.IDataManager#find
  */
  public List<T> find(List<I> ids) throws DataSourceException {

    // Set method name variable
    String methodName = "find";

    // Initialize return value
    List<T> dataObjects = null;

    // Validate IDs
    if (ids == null || ids.size() == 0) {
      throw new DataSourceException(methodName + ": Invalid IDs.");
    }

    // Initialize processing variables
    Connection connection = null;

    // Create SQL statement list
    List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();
    
    // Create result set list
    List<ResultSet> resultSets = new ArrayList<ResultSet>();

    try {
      // Retrieve database connection
      connection = openConnection();

      // Retrieve find SQL
      getFindSql(ids, connection, sqlStatements);

      // Validate find SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getFindSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        resultSets.add(sqlStatement.executeQuery());
      }

      // Create data object list
      if (resultSets.size() > 0) {
        dataObjects = createDataObjects(resultSets);
      }
      
      if (dataObjects != null) {
        
        Collections.sort(dataObjects, new DataObjectComparator(ids));
      }
    }
    catch (CoreException coreException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close all statements and result sets
      DbUtil.closeStatements(sqlStatements);
      DbUtil.closeResultSets(resultSets);

      // Close connnection
      DbUtil.closeConnection(connection);
    }

    return dataObjects;
  }

  /**
    Returns a list of data objects based on specified search criteria information.
    @param filterCriteria Filter criteria.
    @return List List of data objects.
  */
  public List<T> find(FilterCriteria filterCriteria) throws DataSourceException {
  
    // Set method name variable
    String methodName = "find";
  
    // Initialize return value
    List<T> dataObjects = null;

    // Initialize processing variables
    Connection connection = null;

    // Create SQL statement list
    List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();
    
    // Create result set list
    List<ResultSet> resultSets = new ArrayList<ResultSet>();

    try {
      // Retrieve database connection
      connection = openConnection();

      // Retrieve find SQL
      getFindSql(filterCriteria, connection, sqlStatements);

      // Validate find SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getFindSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        resultSets.add(sqlStatement.executeQuery());
      }

      // Create data object list
      if (resultSets.size() > 0) {
        dataObjects = createDataObjects(resultSets);
      }
    }
    catch (CoreException coreException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", coreException);

      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close all statements and result sets
      DbUtil.closeStatements(sqlStatements);
      DbUtil.closeResultSets(resultSets);

      // Close connnection
      DbUtil.closeConnection(connection);
    }

    return dataObjects;
  }
  
  /**
    Returns a list of data object IDs based on specified search criteria information.
    @param filterCriteria Filter criteria.
    @return List List of data object IDs.
  */
  public List<I> findIds(FilterCriteria filterCriteria) throws DataSourceException {
  
    // Set method name variable
    String methodName = "findIds";
  
    // Initialize return value
    List<I> ids = null;
  
    // Initialize processing variables
    Connection connection = null;
    
    // Initialize statement and result set
    PreparedStatement sqlStatement = null;
    ResultSet resultSet = null;
    
    try {
      // Retrieve database connection
      connection = openConnection();
  
      // Retrieve find SQL
      sqlStatement = getFindIdSql(filterCriteria, connection);
  
      // Validate find SQL
      if (sqlStatement == null) {
        throw new DataSourceException(methodName + ": No SQL returned from getFindIdSql method.");
      }
  
      // Execute SQL statement
      resultSet = sqlStatement.executeQuery();
  
      // Create ID list
      if (resultSet != null) {
        ids = createIds(resultSet);
      }
    }
    catch (CoreException coreException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Ids", coreException);
  
      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Ids", sqlException);
  
      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {
  
      // Close all statements and result sets
      DbUtil.closeStatement(sqlStatement);
      DbUtil.closeResultSet(resultSet);
  
      // Close connnection
      DbUtil.closeConnection(connection);
    }
  
    return ids;
  }
  
  /**
    Returns a value indicating whether or not a given data object already exists.
    @param dataObject Data object.
  */
  protected boolean isDuplicate(T dataObject) throws DataSourceException {

    // Set method name variable
    String methodName = "isDuplicate";

    // Initialize return value
    boolean isDuplicate = false;

    // Initialize processing variables
    Connection connection = null;
    PreparedStatement sqlStatement = null;
    ResultSet resultSet = null;

    try {
      // Retrieve database connection
      connection = openConnection();

      // Retrieve find SQL
      sqlStatement = getDuplicateSql(dataObject, connection);

      // Only perform duplicate check if SQL is provided
      if (sqlStatement != null) {

        // Execute SQL statement
        resultSet = sqlStatement.executeQuery();

        // Set duplicate indicator
        if (resultSet != null  && resultSet.next()) {
          isDuplicate = true;
        }
      }
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Checking for Duplicate", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {

      // Close SQL statement and result set
      DbUtil.closeStatement(sqlStatement);
      DbUtil.closeResultSet(resultSet);

      // Close connnection
      DbUtil.closeConnection(connection);
    }

    return isDuplicate;
  }

/**
  Compares data objects for sorting based on a list of data object IDs.
*/
private class DataObjectComparator implements Comparator<T> {
 
  /**
    List of data object IDs used for comparison.
  */
  private List<I> ids = null;
  
  /**
    Creates a new DataObjectComparator using a list of data object IDs.
    @param ids List of data object IDs.
  */
  public DataObjectComparator(List<I> ids) {
    
    // Validate parameters
    if (ids == null || ids.isEmpty()) {
      throw new IllegalArgumentException("Data object IDs are required.");
    }
    
    // Set properties
    this.ids = ids;
  }
  
  /**
    Compares two data objects for sorting based on an internal list of data object IDs.
    @see java.util.Comparator#compare
  */
  public int compare(T dataObject1, T dataObject2) {
    
    // Initialize return value
    int value = 0; 

    if (dataObject1 != null && dataObject2 != null) {
      
      // Get indexes of data object IDs
      Integer index1 = Integer.valueOf(ids.indexOf(dataObject1.getId()));
      Integer index2 = Integer.valueOf(ids.indexOf(dataObject2.getId()));
      
      // Set value based on index comparison
      value = index1.compareTo(index2);
    }
    
    return value;
  }

  /**
    Compares this comparator with another for equality.
    @see java.util.Comparator#equals 
  */
  public boolean equals(Object object) {
    return this.equals(object);
  }
  
}

}
