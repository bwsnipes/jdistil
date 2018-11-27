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
import com.bws.jdistil.core.security.IDomain;

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

	private static final String DEFAULT_DOMAIN_ID_COLUMN_NAME = "domain_id";
	
  /**
    Creates a new DatabaseDataManager object.
  */
  public DatabaseDataManager() {
    super();
  }

  /**
    Returns a value indicating whether or not the data manager is domain aware.
    A domain aware data manager will use the default or specified domain provided 
    in method invocations to manage data object information.  
    @return boolean Domain aware indicator.
  */
  protected boolean isDomainAware() {
  	return true;
  }
  
  /**
    Returns the underlying column name used to persist domain ID values. 
    This method can be overridden to provide a different column name if needed.
    @return String Domain ID column name.
  */
  protected String getDomainIdColumnName() {
  	return DEFAULT_DOMAIN_ID_COLUMN_NAME;
  }
  
  /**
    Validates a given data object for use with the data object manager.
    @param dataObject Data object.
  */
  protected abstract boolean isValidDataObject(T dataObject);

  /**
    Populates the ID before the data object can be inserted into the database.
    @param dataObject Data object.
    @param domain Target domain.
  */
  protected abstract void initializeId(DataObject<?> dataObject, IDomain domain)
      throws DataSourceException;

  /**
    Returns a list of prepared statements used to create a given data object.
    @param dataObject Data object.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getCreateSql(T dataObject, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to update a given data object.
    @param dataObject Data object.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getUpdateSql(T dataObject, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to delete a given data object.
    @param dataObject Data object.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getDeleteSql(T dataObject, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to find all data objects.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(Connection connection, IDomain domain, List<PreparedStatement> sqlStatements)
      throws DataSourceException;

  /**
    Returns a prepared statement used to find all data object IDs.
    @param connection Database connection.
    @param domain Target domain.
    @return PreparedStatement Prepared statement.
  */
  protected abstract PreparedStatement getFindIdSql(Connection connection, IDomain domain)
      throws DataSourceException;
  
  /**
    Returns a list of prepared statements used to find a given data object.
    @param id Data object ID.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(I id, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to find a list of data objects.
    @param ids List of data object IDs.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(List<I> ids, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException;

  /**
    Returns a list of prepared statements used to find a list of data objects using search criteria.
    @param filterCriteria Filter criteria.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of prepared statements.
  */
  protected abstract void getFindSql(FilterCriteria filterCriteria, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException;
  
  /**
    Returns a prepared statement used to find data object IDs using search criteria.
    @param filterCriteria Filter criteria.
    @param connection Database connection.
    @param domain Target domain.
    @return PreparedStatement Prepared statement.
  */
  protected abstract PreparedStatement getFindIdSql(FilterCriteria filterCriteria, Connection connection, IDomain domain)
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
    @param domain Target domain.
    @return Connection Database connection.
  */
  protected Connection openConnection(IDomain domain) throws DataSourceException {

    // Set method name variable
    String methodName = "openConnection";

    // Initialize return value
    Connection connection = null;

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
    @param domain Target domain.
    @return PreparedStatement Prepared statement.
  */
  protected PreparedStatement getDuplicateSql(T dataObject, Connection connection, IDomain domain)
      throws DataSourceException {

    // Do not perform duplicate check by default
    return null;
  }

  /**
    Saves a new data object using a specified domain.
    @param dataObject Data object to save.
    @param domain Target domain.
  */
  protected void create(T dataObject, IDomain domain) throws DataSourceException {

    // Set method name variable
    String methodName = "create";

    // Check for duplicate
    if (isDuplicate(dataObject, domain)) {
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
      connection = openConnection(domain);

      // Initialize ID
      initializeId(dataObject, domain);

      // Retrieve create SQL
      getCreateSql(dataObject, connection, domain, sqlStatements);

      // Validate create SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getCreateSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        sqlStatement.executeUpdate();
      }

      // Commit all SQL statements
      DbUtil.commit(connection);
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
    Saves an existing data object using a specified domain.
    @param dataObject Data object to save.
    @param checkDirty Indicates whether or not to check for dirty data.
    @param domain Target domain.
  */
  protected void update(T dataObject, boolean checkDirty, IDomain domain) throws DataSourceException {

    // Set method name variable
    String methodName = "update";

    // Get ID
    I id = dataObject.getId();

    if (checkDirty) {

      // Find data object
      T currentDataObject = find(id, domain);

      // Only update data if same version
      if (!isSameVersion(dataObject, currentDataObject)) {
        throw new DirtyUpdateException(currentDataObject);
      }
    }

    // Check for duplicate
    if (isDuplicate(dataObject, domain)) {
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
      connection = openConnection(domain);

      // Retrieve update SQL
      getUpdateSql(dataObject, connection, domain, sqlStatements);

      // Validate create SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getUpdateSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        sqlStatement.executeUpdate();
      }

      // Commit all SQL statements
      DbUtil.commit(connection);
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
		@see IDataManager#save(DataObject)
  */
  @Override
  public void save(T dataObject) throws DataSourceException {
    save(dataObject, false, null);
  }

  /**
    Saves a data object using a specified domain.
		@see IDataManager#save(DataObject, IDomain)
  */
  @Override
  public void save(T dataObject, IDomain domain) throws DataSourceException {
  	save(dataObject, false, domain);
  }
  
  /**
    Saves a data object and checks for dirty data based on a given indicator.
		@see IDataManager#save(DataObject, boolean)
  */
  @Override
  public void save(T dataObject, boolean checkDirty) throws DataSourceException {
  	save(dataObject, checkDirty, null);
  }
  
  /**
    Saves a data object and checks for dirty data using a specified domain.
		@see IDataManager#save(DataObject, boolean, IDomain)
  */
  @Override
  public void save(T dataObject, boolean checkDirty, IDomain domain) throws DataSourceException {
  	
    // Set method name variable
    String methodName = "save";

    // Validate data object
    if (!isValidDataObject(dataObject)) {
      throw new DataSourceException(methodName + ": Invalid data object.");
    }

    if (isNewDataObject(dataObject)) {

      // Create data object
      create(dataObject, domain);
    }
    else {

      // Update data object
      update(dataObject, checkDirty, domain);
    }
  }

  /**
    Deletes a data object using the default domain.
		@see IDataManager#delete(DataObject)
  */
  @Override
  public void delete(T dataObject) throws DataSourceException {
  	delete(dataObject, null);
  }
  
  /**
    Deletes a data object using a specified domain.
		@see IDataManager#delete(DataObject, IDomain)
  */
  @Override
  public void delete(T dataObject, IDomain domain) throws DataSourceException {
  	
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
      connection = openConnection(domain);

      // Retrieve remove SQL
      getDeleteSql(dataObject, connection, domain, sqlStatements);

      // Validate delete SQL
      if (sqlStatements.size() <= 0) {
        throw new DataSourceException(methodName + ": No SQL returned from getDeleteSql method.");
      }

      // Execute all SQL statements
      for (PreparedStatement sqlStatement : sqlStatements) {
        sqlStatement.executeUpdate();
      }

      // Commit all SQL statements
      DbUtil.commit(connection);
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
		@see IDataManager#find()
  */
  @Override
  public List<T> find() throws DataSourceException {
  	return find((IDomain)null);
  }

  /**
    Returns all data objectsusing a specified domain.
		@see IDataManager#find(IDomain)
  */
  @Override
  public List<T> find(IDomain domain) throws DataSourceException {
  
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
      connection = openConnection(domain);

      // Retrieve find SQL
      getFindSql(connection, domain, sqlStatements);

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
		@see IDataManager#findIds()
  */
  @Override
  public List<I> findIds() throws DataSourceException {
  	return findIds((IDomain)null);
  }

  /**
    Returns all data object IDs.
		@see IDataManager#findIds(IDomain)
  */
  @Override
  public List<I> findIds(IDomain domain) throws DataSourceException {

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
      connection = openConnection(domain);
  
      // Retrieve find SQL
      sqlStatement = getFindIdSql(connection, domain);
  
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
		@see IDataManager#find(Object)
  */
  @Override
  public T find(I id) throws DataSourceException {
  	return find(id, null);
  }

  /**
    Returns a data object using a given data object ID.
		@see IDataManager#find(Object, IDomain)
  */
  @Override
  public T find(I id, IDomain domain) throws DataSourceException {
  	
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
      connection = openConnection(domain);

      // Retrieve find SQL
      getFindSql(id, connection, domain, sqlStatements);

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
		@see IDataManager#find(List)
  */
  @Override
  public List<T> find(List<I> ids) throws DataSourceException {
  	return find(ids, null);
  }

  /**
    Returns a list of data objects using a list of data object IDs.
		@see IDataManager#find(List, IDomain)
  */
  @Override
  public List<T> find(List<I> ids, IDomain domain) throws DataSourceException {
  	
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
      connection = openConnection(domain);

      // Retrieve find SQL
      getFindSql(ids, connection, domain, sqlStatements);

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
		@see IDataManager#find(FilterCriteria)
  */
  @Override
  public List<T> find(FilterCriteria filterCriteria) throws DataSourceException {
  	return find(filterCriteria, null);
  }

  /**
    Returns a list of data objects based on specified filter criteria information.
		@see IDataManager#find(FilterCriteria, IDomain)
  */
  @Override
  public List<T> find(FilterCriteria filterCriteria, IDomain domain) throws DataSourceException {
  
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
      connection = openConnection(domain);

      // Retrieve find SQL
      getFindSql(filterCriteria, connection, domain, sqlStatements);

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
		@see IDataManager#findIds(FilterCriteria)
  */
  @Override
  public List<I> findIds(FilterCriteria filterCriteria) throws DataSourceException {
  	return findIds(filterCriteria, null);
  }

  /**
    Returns a list of data object IDs based on specified search criteria information.
    @see IDataManager#findIds(FilterCriteria, IDomain)
  */
  @Override
  public List<I> findIds(FilterCriteria filterCriteria, IDomain domain) throws DataSourceException {
  	
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
      connection = openConnection(domain);
  
      // Retrieve find SQL
      sqlStatement = getFindIdSql(filterCriteria, connection, domain);
  
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
    @param domain Target domain.
    @return boolean Duplicate data object indicator.
  */
  protected boolean isDuplicate(T dataObject, IDomain domain) throws DataSourceException {

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
      connection = openConnection(domain);

      // Retrieve find SQL
      sqlStatement = getDuplicateSql(dataObject, connection, domain);

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
