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
import com.bws.jdistil.core.datasource.FilterCriteria;
import com.bws.jdistil.core.datasource.OrderCriterion;
import com.bws.jdistil.core.datasource.ValueCriterion;
import com.bws.jdistil.core.datasource.database.sequence.ISequenceProvider;
import com.bws.jdistil.core.datasource.database.sequence.SequenceProviderFactory;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.Introspector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Abstract bound database data manager class that uses binding information to implement data manager functionality.
  @author Bryan Snipes
*/
public abstract class BoundDatabaseDataManager<I, T extends DataObject<I>> extends DatabaseDataManager<I, T> {

  /**
    Property binding registry keyed by class name.
  */
  private static final Map<String, DataObjectBinding> bindingRegistry = new HashMap<String, DataObjectBinding>();

  /**
    Data object binding.
  */
  private DataObjectBinding dataObjectBinding = null;

  /**
    Creates a new BoundDatabaseDataManager object.
  */
  public BoundDatabaseDataManager() {
    super();

    // Establish data object binding
    dataObjectBinding = getDataObjectBinding();
  }

  /**
    Creates and returns a data object binding.
    @return DataObjectBinding Data object binding.
  */
  protected abstract DataObjectBinding createDataObjectBinding();

  /**
    Lazily creates and returns a data object binding.
    @return DataObjectBinding Data object binding.
  */
  private DataObjectBinding getDataObjectBinding() {

    // Get target class
    String targetClassName = getClass().getName();

    // Attempt to retrieve data object binding
    DataObjectBinding dataObjectBinding = bindingRegistry.get(targetClassName);

    if (dataObjectBinding == null) {

      // Create data object binding
      dataObjectBinding = createDataObjectBinding();

      // Register data object binding
      if (dataObjectBinding != null) {
        bindingRegistry.put(targetClassName, dataObjectBinding);
      }
    }

    return dataObjectBinding;
  }

  /**
    Validates a given data object for use with the data object manager.
    @see DatabaseDataManager#isValidDataObject(DataObject)
  */
  @Override
  protected boolean isValidDataObject(T dataObject) {

    // Initialize return value
    boolean isValid = false;

    if (dataObject != null) {

      // Get data object class name
      String className = dataObject.getClass().getName();

      // Get binding class name
      String bindingClassName = dataObjectBinding.getDataObjectClass().getName();

      // Set valid indicator
      isValid = className.equals(bindingClassName);
    }

    return isValid;
  }

  /**
    Populates the ID using the database sequencer which deals only with integer type IDs.
    This method must be overridden to handle data objects with non-integer IDs.
    @see DatabaseDataManager#initializeId(DataObject, IDomain)
  */
  @Override
  protected void initializeId(DataObject<?> dataObject, IDomain domain) throws DataSourceException {

    // Get table name
    String tableName = dataObjectBinding.getTableName();

    // Get ID column binding
    IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

    // Get column and property names
    String columnName = idColumnBinding.getColumnName();
    String propertyName = idColumnBinding.getPropertyName();

    // Check for existing value
    Object propertyValue = Introspector.getPropertyValue(dataObject, propertyName);

    // Only set values that haven't already been set
    if (propertyValue == null) {

    	// Get sequence provider factory
    	IFactory sequenceProviderFactory = SequenceProviderFactory.getInstance();
    	
    	// Create sequence provider
    	ISequenceProvider sequenceProvider = (ISequenceProvider)sequenceProviderFactory.create();
    	
    	try {
        // Get next sequence value
        int value = sequenceProvider.nextValue(tableName, columnName, domain);

        // Set property value
        Introspector.setPropertyValue(dataObject, propertyName, new Integer(value));
    	}
    	finally {
    		
    		// Recycle sequence provider
    		sequenceProviderFactory.recycle(sequenceProvider);
    	}
    }
  }

  /**
    Returns a prepared statement used to check for duplicate data objects.
    @see DatabaseDataManager#getDuplicateSql(DataObject, Connection, IDomain)
  */
  @Override
  protected PreparedStatement getDuplicateSql(T dataObject, Connection connection, IDomain domain)
      throws DataSourceException {

    // Create SQL statements starting with current data object and binding
    return getDuplicateSql(dataObject, dataObjectBinding, connection, domain);
  }

  /**
    Populates a list of prepared statements used to create a given data object.
    @see DatabaseDataManager#getCreateSql(DataObject, Connection, IDomain, List)
  */
  @Override
  protected void getCreateSql(T dataObject, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Create SQL statements starting with current data object and binding
    getCreateSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to update a given data object.
    @see DatabaseDataManager#getUpdateSql(DataObject, Connection, IDomain, List)
  */
  @Override
  protected void getUpdateSql(T dataObject, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Create SQL statements starting with current data object and binding
    getUpdateSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to delete a given data object.
    @see DatabaseDataManager#getDeleteSql(DataObject, Connection, IDomain, List)
  */
  @Override
  protected void getDeleteSql(T dataObject, Connection connection, IDomain domain,
      List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Create SQL statements starting with current data object and binding
    getDeleteSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to find a given data object.
    @see DatabaseDataManager#getFindSql(Object, Connection, IDomain, List)
  */
  @Override
  protected void getFindSql(I id, Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) 
  		throws DataSourceException {

    // Get table name
    String tableName = dataObjectBinding.getTableName();

    // Get ID column binding
    IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

    // Get binding data
    String columnName = idColumnBinding.getColumnName();
    int columnType = idColumnBinding.getColumnType();

    // Create value condition
    ValueCondition valueCondition = new ValueCondition(tableName, columnName, Operators.EQUALS, columnType, id);

    // Get SQL statements
    getFindSql(valueCondition, null, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to find all data objects.
    @see DatabaseDataManager#getFindSql(Connection, IDomain, List)
  */
  @Override
  protected void getFindSql(Connection connection, IDomain domain, List<PreparedStatement> sqlStatements)
      throws DataSourceException {

    // Get SQL statements
    this.getFindSql(null, null, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to find a list of data objects.
    @see DatabaseDataManager#getFindSql(List, Connection, IDomain, List)
  */
  @Override
  protected void getFindSql(List<I> ids, Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) 
  		throws DataSourceException {

    // Get table name
    String tableName = dataObjectBinding.getTableName();

    // Get ID column binding
    IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

    // Get binding data
    String columnName = idColumnBinding.getColumnName();
    int columnType = idColumnBinding.getColumnType();

    // Initialize value conditions
    ValueConditions valueConditions = null;

    for (I id : ids) {

      // Create value condition
      ValueCondition valueCondition = new ValueCondition(tableName, columnName, Operators.EQUALS, columnType, id);

      // Build value conditions
      if (valueConditions == null) {
        valueConditions = new ValueConditions(valueCondition);
      }
      else {
        valueConditions.add(Operators.OR, valueCondition);
      }
    }

    // Get SQL statements
    getFindSql(valueConditions, null, connection, domain, sqlStatements);
  }

  /**
    Returns a list of prepared statements used to find a list of data objects using search criteria.
    @see DatabaseDataManager#getFindSql(FilterCriteria, Connection, IDomain, List)
  */
  @Override
  protected void getFindSql(FilterCriteria filterCriteria, Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) 
  		throws DataSourceException {
    
  	// Set method name
  	String methodName = "getFindSql";
  	
    // Initialize value conditions
    ValueConditions valueConditions = null;

    // Initialize order conditions
    OrderConditions orderConditions = null;

    if (filterCriteria != null) {
      
      // Get search conditions
      List<ValueCriterion> valueCriteria = filterCriteria.getValueCriteria();
  
      for (ValueCriterion valueCriterion : valueCriteria) {
      	
	      // Create value condition
	      ValueCondition valueCondition = getValueCondition(dataObjectBinding, valueCriterion);
	      
	      if (valueCondition != null) {
	      	
		      // Build value conditions
		      if (valueConditions == null) {
		        valueConditions = new ValueConditions(valueCondition);
		      }
		      else {
		        valueConditions.add(Operators.AND, valueCondition);
		      }
	      }
	      else {
	      	
	        // Post warning message
	        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
	        logger.logp(Level.WARNING, getClass().getName(), methodName, "Value condition could not be created.");
	      }
      }
  
      // Get order criteria
      List<OrderCriterion> orderCriteria = filterCriteria.getOrderCriteria();

      for (OrderCriterion orderCriterion : orderCriteria) {

        // Create order condition
        OrderCondition orderCondition = getOrderCondition(dataObjectBinding, orderCriterion);

        if (orderCondition != null) {
        	
          // Build order conditions
          if (orderConditions == null) {
            orderConditions = new OrderConditions(orderCondition);
          }
          else {
            orderConditions.add(orderCondition);
          }
        }
	      else {
	      	
	        // Post warning message
	        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
	        logger.logp(Level.WARNING, getClass().getName(), methodName, "Order condition could not be created.");
	      }
      }
    }
    
    // Retrieve find SQL
    getFindSql(valueConditions, orderConditions, connection, domain, sqlStatements);
  }
  
  /**
    Returns a prepared statement used to find data object IDs using search criteria.
    @see DatabaseDataManager#getFindIdSql(FilterCriteria, Connection, IDomain)
  */
  @Override
  protected PreparedStatement getFindIdSql(FilterCriteria filterCriteria, Connection connection, IDomain domain)
      throws DataSourceException {
    
  	// Set method name
  	String methodName = "getFindIdSql";
  	
    // Initialize value conditions
    ValueConditions valueConditions = null;

    // Initialize order conditions
    OrderConditions orderConditions = null;

    if (filterCriteria != null) {
  
      // Get search conditions
      List<ValueCriterion> valueCriteria = filterCriteria.getValueCriteria();
  
      for (ValueCriterion valueCriterion : valueCriteria) {
      	
	      // Create value condition
	      ValueCondition valueCondition = getValueCondition(dataObjectBinding, valueCriterion);
	      
	      if (valueCondition != null) {
	      	
		      // Build value conditions
		      if (valueConditions == null) {
		        valueConditions = new ValueConditions(valueCondition);
		      }
		      else {
		        valueConditions.add(Operators.AND, valueCondition);
		      }
	      }
	      else {
	      	
	        // Post warning message
	        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
	        logger.logp(Level.WARNING, getClass().getName(), methodName, "Value condition could not be created.");
	      }
      }
  
      // Get order criteria
      List<OrderCriterion> orderCriteria = filterCriteria.getOrderCriteria();

      for (OrderCriterion orderCriterion : orderCriteria) {

        // Create order condition
        OrderCondition orderCondition = getOrderCondition(dataObjectBinding, orderCriterion);

        if (orderCondition != null) {
        	
          // Build order conditions
          if (orderConditions == null) {
            orderConditions = new OrderConditions(orderCondition);
          }
          else {
            orderConditions.add(orderCondition);
          }
        }
	      else {
	      	
	        // Post warning message
	        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
	        logger.logp(Level.WARNING, getClass().getName(), methodName, "Order condition could not be created.");
	      }
      }
    }
  
    return getFindIdSql(valueConditions, orderConditions, connection, domain);
  }
  
  /**
   * Returns a value condition using a data object binding and value criterion. Initially processes
   * the column and associate bindings of the given data object binding before recursively checking
   * the data object binding of each dependent binding. Processing continues until a value condition 
   * is created.
   * @param dataObjectBinding Data object binding.
   * @param valueCriterion Value criterion.
   * @return ValueCondition Value condition created from the value criterion.
   */
  private ValueCondition getValueCondition(DataObjectBinding dataObjectBinding, ValueCriterion valueCriterion) {

  	// Initialize return value
  	ValueCondition valueCondition = null;
  	
    // Get property name
    String propertyName = valueCriterion.getPropertyName();

    if (propertyName != null) {

    	// Get operator and field value
      String operator = valueCriterion.getOperator();
      Object fieldValue = valueCriterion.getPropertyValue();
    	
    	// Get column binding by property name
      ColumnBinding columnBinding = dataObjectBinding.getColumnBindingByPropertyName(propertyName);

      if (columnBinding != null) {
      	
      	// Get table name
      	String tableName = dataObjectBinding.getTableName();
      	
        // Get column name and type
        String columnName = columnBinding.getColumnName();
        int type = columnBinding.getColumnType();

        // Create value condition
        valueCondition = new ValueCondition(tableName, columnName, operator, type, fieldValue);
      }
      else {
      	
        // Get associate binding by property name
        AssociateBinding associateBinding = dataObjectBinding.getAssociateBindingByPropertyName(propertyName);

        if (associateBinding != null) {
        	
        	// Get table name
        	String tableName = associateBinding.getTableName();
        	
        	// Get associate ID column binding
        	IdColumnBinding associateIdColumnBinding = associateBinding.getAssociateIdColumnBinding();
        	
          // Get column name and type
          String columnName = associateIdColumnBinding.getColumnName();
          int type = associateIdColumnBinding.getColumnType();

          // Create value condition
          valueCondition = new ValueCondition(tableName, columnName, operator, type, fieldValue);
        }
        else {
        	
        	// Get dependent bindings
        	Collection<DependentBinding> dependentBindings = dataObjectBinding.getDependentBindings();
        	
        	if (dependentBindings != null) {
        		
        		for (DependentBinding dependentBinding : dependentBindings) {
        			
        			// Get dependent data object binding
        			DataObjectBinding dependentDataObjectBinding = dependentBinding.getDataObjectBinding();
        			
        			// Use dependent bindings to recursively attempt to obtain value condition 
        			valueCondition = getValueCondition(dependentDataObjectBinding, valueCriterion);
        			
        			if (valueCondition != null) {
        				break;
        			}
        		}
        	}
        }
      }
    }

  	return valueCondition;
  }
  
  private OrderCondition getOrderCondition(DataObjectBinding dataObjectBinding, OrderCriterion orderCriterion) {

  	// Initialize return value
  	OrderCondition orderCondition = null;
  	
    // Get property name
    String propertyName = orderCriterion.getPropertyName();

    if (propertyName != null) {

      // Get order criterion properties
      String direction = orderCriterion.getDirection();
    	
    	// Get column binding by property name
      ColumnBinding columnBinding = dataObjectBinding.getColumnBindingByPropertyName(propertyName);

      if (columnBinding != null) {
      	
      	// Initialize table name and column name
      	String tableName = null;
      	String columnName = null;
      	
      	if (columnBinding.useReferenceOrdering()) {
      		
        	// Use reference table name and column name
        	tableName = columnBinding.getReferenceTableName();
        	columnName = columnBinding.getReferenceColumnName();
      	}
      	else {
      		
        	// Use binding table name and column name
        	tableName = dataObjectBinding.getTableName();
        	columnName = columnBinding.getColumnName();
      	}
      	
        // Create order condition
        orderCondition = new OrderCondition(tableName, columnName, direction);
      }
      else {
      	
        // Get associate binding by property name
        AssociateBinding associateBinding = dataObjectBinding.getAssociateBindingByPropertyName(propertyName);

        if (associateBinding != null) {

        	// Initialize table name and column name
        	String tableName = null;
        	String columnName = null;

        	if (associateBinding.useReferenceOrdering()) {
        		
          	// Use reference table name and column name
          	tableName = associateBinding.getReferenceTableName();
          	columnName = associateBinding.getReferenceColumnName();
        	}
        	else {
        		
          	// Use associate binding table name
          	tableName = associateBinding.getTableName();
          	
          	// Get associate ID column binding
          	IdColumnBinding associateIdColumnBinding = associateBinding.getAssociateIdColumnBinding();
          	
            // Use associate binding column name
          	columnName = associateIdColumnBinding.getColumnName();
        	}

          // Create order condition
          orderCondition = new OrderCondition(tableName, columnName, direction);
        }
        else {
        	
        	// Get dependent bindings
        	Collection<DependentBinding> dependentBindings = dataObjectBinding.getDependentBindings();
        	
        	if (dependentBindings != null) {
        		
        		for (DependentBinding dependentBinding : dependentBindings) {
        			
        			// Get dependent data object binding
        			DataObjectBinding dependentDataObjectBinding = dependentBinding.getDataObjectBinding();
        			
        			// Use dependent bindings to recursively attempt to obtain order condition 
        			orderCondition = getOrderCondition(dependentDataObjectBinding, orderCriterion);
        			
        			if (orderCondition != null) {
        				break;
        			}
        		}
        	}
        }
      }
    }

  	return orderCondition;
  }
  
  /**
    Populates a list of prepared statements used to find all data object IDs.
    @see DatabaseDataManager#getFindIdSql(Connection, IDomain)
  */
  @Override
  protected PreparedStatement getFindIdSql(Connection connection, IDomain domain) throws DataSourceException {
  
    // Get SQL statement
    return getFindIdSql(null, null, connection, domain);
  }
  
  /**
    Returns a prepared statement used to check for duplicate data objects.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @return PreparedStatement Prepared statement.
  */
  private PreparedStatement getDuplicateSql(T dataObject, DataObjectBinding dataObjectBinding, Connection connection, IDomain domain)
  		throws DataSourceException {

    // Initialize return value
    PreparedStatement sqlStatement = null;

    // Get table name
    String tableName = dataObjectBinding.getTableName();

    // Initialize unique member processed indicator
    boolean isUniqueMemberProcessed = false;

    // Initialize value conditions
    ValueConditions valueConditions = null;

    // Get column bindings
    Collection<ColumnBinding> columnBindings = dataObjectBinding.getColumnBindings();

    // Process unique members first
    for (ColumnBinding columnBinding : columnBindings) {

      if (columnBinding.getIsUniqueMember()) {

        // Set unique member processed indicator
        isUniqueMemberProcessed = true;

        // Get binding data
        String propertyName = columnBinding.getPropertyName();
        String columnName = columnBinding.getColumnName();
        int columnType = columnBinding.getColumnType();

        // Get property value
        Object propertyValue = Introspector.getPropertyValue(dataObject, propertyName);

        // Create value condition
        ValueCondition valueCondition = new ValueCondition(tableName, columnName, Operators.EQUALS, columnType, propertyValue);

        // Build value condition
        if (valueConditions == null) {
          valueConditions = new ValueConditions(valueCondition);
        }
        else {
          valueConditions.add(Operators.AND, valueCondition);
        }
      }
    }

    // Only proceed if a unique member was processed
    if (isUniqueMemberProcessed) {

      // Get data object ID
      Object id = dataObject.getId();

      if (id != null) {
        
        // Get ID column binding
        IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

        // Get binding data
        String columnName = idColumnBinding.getColumnName();
        int columnType = idColumnBinding.getColumnType();

        // Create value condition
        ValueCondition valueCondition = new ValueCondition(tableName, columnName, Operators.NOT_EQUALS, columnType, id);

        // Build value condition
        if (valueConditions == null) {
          valueConditions = new ValueConditions(valueCondition);
        }
        else {
          valueConditions.add(Operators.AND, valueCondition);
        }
      }

      // Append domain condition
      appendDomainCondition(tableName, domain, valueConditions);
      
      // Create list of SQL statements
      List<PreparedStatement> sqlStatements = new ArrayList<PreparedStatement>();

      // Get SQL statements
      getFindSql(valueConditions, null, connection, domain, sqlStatements);

      // Only need the first statement directed at primary table
      sqlStatement = sqlStatements.get(0);
    }

    return sqlStatement;
  }

  /**
    Populates a list of prepared statements used to create a given data object using
    a data object, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getCreateSql(DataObject<?> dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Set method name
    String methodName = "getCreateSql";

    // Get ID column binding
    IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

    // Get column bindings
    Collection<ColumnBinding> columnBindings = dataObjectBinding.getColumnBindings();

    // Get columns and parameters
    String columns = getInsertColumns(idColumnBinding, columnBindings);
    String parameters = getInsertParameters(idColumnBinding, columnBindings);

    // Get table name
    String tableName = dataObjectBinding.getTableName();

    // Build SQL text
    String sql = "insert into " + tableName + "(" + columns + ") values (" + parameters + ")";

    // Initialize SQL statement
    PreparedStatement statement = null;

    try {
      // Create SQL statement
      statement = connection.prepareStatement(sql);
    }
    catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Create SQL", sqlException);

        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }

    // Initialize parameter index
    int parameterIndex = 1;

    // Set parameter values
    parameterIndex = setParameter(statement, dataObject.getId(), idColumnBinding, parameterIndex);
    parameterIndex = setParameters(statement, dataObject, columnBindings, parameterIndex);
    parameterIndex = setParameter(statement, domain, parameterIndex);

    // Add statement to list
    sqlStatements.add(statement);

    // Get create associate SQL
    getCreateAssociateSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);

    // Get create dependent SQL
    getCreateDependentSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to create associate data objects
    using a data object, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getCreateAssociateSql(DataObject<?> dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Set method name
    String methodName = "getCreateAssociateSql";

    // Get data object ID
    Object id = dataObject.getId();

    // Get associate bindings
    Collection<AssociateBinding> associateBindings = dataObjectBinding.getAssociateBindings();

    for (AssociateBinding associateBinding : associateBindings) {

      // Get table name and property name
      String tableName = associateBinding.getTableName();
      String propertyName = associateBinding.getPropertyName();

      // Get parent ID and associate ID column bindings
      IdColumnBinding parentIdColumnBinding = associateBinding.getParentIdColumnBinding();
      IdColumnBinding associateIdColumnBinding = associateBinding.getAssociateIdColumnBinding();

      // Get columns and parameters
      String columns = getInsertColumns(parentIdColumnBinding, associateIdColumnBinding);
      String parameters = getInsertParameters(parentIdColumnBinding, associateIdColumnBinding);

      // Build SQL text
      String sql = "insert into " + tableName + "(" + columns + ") values (" + parameters + ")";

      if (associateBinding.isCollection()) {

        // Retrieve associate IDs
        Collection<?> associateIds = (Collection<?>)Introspector.getPropertyValue(dataObject, propertyName);

        if (associateIds != null) {

          // Create value iterator
          Iterator<?> values = associateIds.iterator();

          while (values.hasNext()) {

            // Get next associate ID
            Object associateId = values.next();

            // Initialize SQL statement
            PreparedStatement statement = null;

            try {
              // Create SQL statement
              statement = connection.prepareStatement(sql);
            }
            catch (SQLException sqlException) {

                // Post error message
                Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
                logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Create Associate SQL", sqlException);
  
                throw new DataSourceException(methodName + ": " + sqlException.getMessage());
            }

            // Initialize parameter index
            int parameterIndex = 1;

            // Set parameter values
            parameterIndex = setParameter(statement, id, parentIdColumnBinding, parameterIndex);
            parameterIndex = setParameter(statement, associateId, associateIdColumnBinding, parameterIndex);

            // Add statement to list
            sqlStatements.add(statement);
          }
        }
      }
      else {

        // Retrieve associate ID
        Object associateId = Introspector.getPropertyValue(dataObject, propertyName);

        // Initialize SQL statement
        PreparedStatement statement = null;

        try {
          // Create SQL statement
          statement = connection.prepareStatement(sql);
        }
        catch (SQLException sqlException) {

            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
            logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Create Associate SQL", sqlException);

            throw new DataSourceException(methodName + ": " + sqlException.getMessage());
        }

        // Initialize parameter index
        int parameterIndex = 1;

        // Set parameter values
        parameterIndex = setParameter(statement, id, parentIdColumnBinding, parameterIndex);
        parameterIndex = setParameter(statement, associateId, associateIdColumnBinding, parameterIndex);

        // Add statement to list
        sqlStatements.add(statement);
      }
    }
  }

  /**
    Populates a list of prepared statements used to create dependent data objects
    using a data object, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getCreateDependentSql(DataObject<?> dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Get dependent bindings
    Collection<DependentBinding> dependentBindings = dataObjectBinding.getDependentBindings();

    for (DependentBinding dependentBinding : dependentBindings) {

      // Get property name
      String propertyName = dependentBinding.getPropertyName();

      // Get dependent binding
      DataObjectBinding dependentDataObjectBinding = dependentBinding.getDataObjectBinding();

      // Get parent ID column binding
      IdColumnBinding parentIdcolumnBinding = dependentDataObjectBinding.getParentIdColumnBinding();

      if (dependentBinding.isCollection()) {

        // Retrieve dependent data objects
    		@SuppressWarnings("unchecked")
    		Collection<DataObject<?>> dependentDataObjects = (Collection<DataObject<?>>)Introspector.getPropertyValue(dataObject, propertyName);

        if (dependentDataObjects != null) {

          // Create value iterator
          Iterator<DataObject<?>> values = dependentDataObjects.iterator();

          while (values.hasNext()) {

            // Get next dependent data object
          	DataObject<?> dependentDataObject = values.next();

            // Initialize ID
            initializeId(dependentDataObject, domain);

            // Get parent ID property name
            String parentIdPropertyName = parentIdcolumnBinding.getPropertyName();

            // Set parent ID
            Introspector.setPropertyValue(dependentDataObject, parentIdPropertyName, dataObject.getId());
            
            // Get create SQL data object
            getCreateSql(dependentDataObject, dependentDataObjectBinding, connection, domain, sqlStatements);
          }
        }
      }
      else {

        // Get dependent data object
        DataObject<?> dependentDataObject = (DataObject<?>)Introspector.getPropertyValue(dataObject, propertyName);

        if (dependentDataObject != null) {

          // Initialize ID
          initializeId(dependentDataObject, domain);
          
          // Get parent ID property name
          String parentIdPropertyName = parentIdcolumnBinding.getPropertyName();

          // Set parent ID
          Introspector.setPropertyValue(dependentDataObject, parentIdPropertyName, dataObject.getId());
            
          // Get create SQL data object
          getCreateSql(dependentDataObject, dependentDataObjectBinding, connection, domain, sqlStatements);
        }
      }
    }
  }

  /**
    Populates a list of prepared statements used to update a given data object using
    a data object, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getUpdateSql(T dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Set method name
    String methodName = "getUpdateSql";

    if (dataObject.isModified()) {

      // Get ID column binding
      IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

      // Get column bindings
      Collection<ColumnBinding> columnBindings = dataObjectBinding.getColumnBindings();

      // Get columns and condition
      String columns = getUpdateColumns(columnBindings);
      String condition = getCondition(idColumnBinding, domain);

      // Get table name
      String tableName = dataObjectBinding.getTableName();

      // Build SQL text
      String sql = "update " + tableName + " set " + columns + " where " + condition;

      // Initialize SQL statement
      PreparedStatement statement = null;

      try {
        // Create SQL statement
        statement = connection.prepareStatement(sql);
      }
      catch (SQLException sqlException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Update SQL", sqlException);
    
          throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }

      // Initialize parameter index
      int parameterIndex = 1;

      // Set parameter values
      parameterIndex = setParameters(statement, dataObject, columnBindings, parameterIndex);
      parameterIndex = setParameter(statement, dataObject.getId(), idColumnBinding, parameterIndex);
      parameterIndex = setParameter(statement, domain, parameterIndex);

      // Add statement to list
      sqlStatements.add(statement);
    }

    // Get delete associate SQL
    getDeleteAssociateSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);

    // Get delete dependent SQL
    getDeleteDependentSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);

    // Get create associate SQL
    getCreateAssociateSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);

    // Get create dependent SQL
    getCreateDependentSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to delete a given data object using
    a data object ID, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getDeleteSql(DataObject<?> dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Set method name
    String methodName = "getDeleteSql";

    // Attempt to apply virtual delete SQL first
    getVirtualDeleteSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);

    // Perform hard delete if no SQL was added during virtual delete
    if (sqlStatements.size() == 0) {

      // Get delete associate SQL
      getDeleteAssociateSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);

      // Get delete dependent SQL
      getDeleteDependentSql(dataObject, dataObjectBinding, connection, domain, sqlStatements);

      // Get ID column binding
      IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

      // Get condition
      String condition = getCondition(idColumnBinding, domain);

      // Get table name
      String tableName = dataObjectBinding.getTableName();

      // Build SQL text
      String sql = "delete from " + tableName + " where " + condition;

      // Initialize SQL statement
      PreparedStatement statement = null;

      try {
        // Create SQL statement
        statement = connection.prepareStatement(sql);
      }
      catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Delete SQL", sqlException);

        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }

      // Initialize parameter index
      int parameterIndex = 1;

      // Set parameter values
      parameterIndex = setParameter(statement, dataObject.getId(), idColumnBinding, parameterIndex);
      parameterIndex = setParameter(statement, domain, parameterIndex);

      // Add statement to list
      sqlStatements.add(statement);
    }
  }

  /**
    Populates a list of prepared statements used to virtually delete a given data object using
    a data object, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getVirtualDeleteSql(DataObject<?> dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Set method name
    String methodName = "getVirtualDeleteSql";

    // Get column bindings
    Collection<ColumnBinding> columnBindings = dataObjectBinding.getColumnBindings();

    // Get virtual delete column binding
    ColumnBinding columnBinding = getVirtualDeleteBinding(columnBindings);

    if (columnBinding != null) {

      // Build virtual delete column
      String column = getUpdateColumn(columnBinding);

      // Get ID column binding
      IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

      // Get column and condition
      String condition = getCondition(idColumnBinding, domain);

      // Get table name
      String tableName = dataObjectBinding.getTableName();

      // Build SQL text
      String sql = "update " + tableName + " set " + column + " where " + condition;

      // Initialize SQL statement
      PreparedStatement statement = null;

      // Initialize parameter index
      int parameterIndex = 1;

      try {
        // Create SQL statement
        statement = connection.prepareStatement(sql);
      }
      catch (SQLException sqlException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Virtual Delete SQL", sqlException);

          throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }

      // Set parameter values
      parameterIndex = setParameter(statement, Boolean.TRUE, columnBinding, parameterIndex);
      parameterIndex = setParameter(statement, dataObject.getId(), idColumnBinding, parameterIndex);
      parameterIndex = setParameter(statement, domain, parameterIndex);

      // Add statement to list
      sqlStatements.add(statement);
    }
  }

  /**
    Returns a virtual delete column binding if one exists.
    @param columnBindings Collection of column bindings.
    @return ColumnBinding Column binding.
  */
  private ColumnBinding getVirtualDeleteBinding(Collection<ColumnBinding> columnBindings) {

    // Initialize return value
    ColumnBinding virtualDeleteBinding = null;

    if (columnBindings != null) {

      // Build delimited columns
      for (ColumnBinding columnBinding : columnBindings) {

        // Check for virtual delete column binding
        if (columnBinding.getIsVirtualDelete()) {
          virtualDeleteBinding = columnBinding;
          break;
        }
      }
    }

    return virtualDeleteBinding;
  }

  /**
    Returns a value condition including a condition to avoid virtual deletes.
    @param originalValueCondition Original value condition.
    @return IValueCondition Original value condition or a new value condition
    including the virtual delete condition.
  */
  private IValueCondition appendVirtualDeleteCondition(IValueCondition originalValueCondition) {

    // Initialize return value
    IValueCondition modifiedValueCondition = originalValueCondition;

    // Attempt to get virtual delete binding
    ColumnBinding columnBinding = getVirtualDeleteBinding(dataObjectBinding.getColumnBindings());

    if (columnBinding != null) {

      // Get table name and column name
      String tableName = dataObjectBinding.getTableName();
      String columnName = columnBinding.getColumnName();

      if (originalValueCondition == null) {

        // Create virtual delete condition
        IValueCondition virtualDeleteCondition = new ValueCondition(tableName, columnName, Operators.EQUALS, DbUtil.BOOLEAN, Boolean.FALSE);

        // Set return value to virtual delete condition
        modifiedValueCondition = virtualDeleteCondition;
      }
      else if (!originalValueCondition.isColumnReferenced(tableName, columnName)) {
      	
        // Create virtual delete condition
        IValueCondition virtualDeleteCondition = new ValueCondition(tableName, columnName, Operators.EQUALS, DbUtil.BOOLEAN, Boolean.FALSE);

        // Create value conditions using original condition and append virtual delete condition
        ValueConditions valueConditions = new ValueConditions(originalValueCondition);
        valueConditions.add(Operators.AND, virtualDeleteCondition);

        // Set return value to value conditions
        modifiedValueCondition = valueConditions;
      }
    }

    return modifiedValueCondition;
  }

  /**
    Returns a value condition including a domain specific condition.
    @param domain Target domain.
    @param originalValueCondition Original value condition.
    @return IValueCondition Original value condition or a new value condition
  */
  private IValueCondition appendDomainCondition(String tableName, IDomain domain, IValueCondition originalValueCondition) {
  
    // Initialize return value
    IValueCondition modifiedValueCondition = originalValueCondition;

    if (isDomainAware()) {
      
    	// Get domain ID column name
    	String columnName = getDomainIdColumnName();
    	
    	// Initialize domain ID
    	Integer domainId = IDomain.DEFAULT_ID;
    	
    	// Use specified domain ID if provided
    	if (domain != null) {
    		domainId = domain.getId();
    	}

      if (originalValueCondition == null) {

        // Create domain condition
        IValueCondition domainCondition = new ValueCondition(tableName, columnName, Operators.EQUALS, DbUtil.INTEGER, domainId);

        // Set return value to domain condition
        modifiedValueCondition = domainCondition;
      }
      else if (!originalValueCondition.isColumnReferenced(tableName, columnName)) {
      	
        // Create domain condition
        IValueCondition domainCondition = new ValueCondition(tableName, columnName, Operators.EQUALS, DbUtil.INTEGER, domainId);

        // Create value conditions using original condition and append domain condition
        ValueConditions valueConditions = new ValueConditions(originalValueCondition);
        valueConditions.add(Operators.AND, domainCondition);

        // Set return value to value conditions
        modifiedValueCondition = valueConditions;
      }
    }

    return modifiedValueCondition;
  }

  /**
    Populates a list of prepared statements used to delete associate data objects
    using a data object, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getDeleteAssociateSql(DataObject<?> dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Set method name
    String methodName = "getDeleteAssociateSql";

    // Get associate bindings
    Collection<AssociateBinding> associateBindings = dataObjectBinding.getAssociateBindings();

    for (AssociateBinding associateBinding : associateBindings) {

      // Get table name
      String tableName = associateBinding.getTableName();

      // Get parent ID column binding
      IdColumnBinding parentIdColumnBinding = associateBinding.getParentIdColumnBinding();

      // Get condition
      String condition = getCondition(parentIdColumnBinding);

      // Build SQL text
      String sql = "delete from " + tableName + " where " + condition;

      // Initialize SQL statement
      PreparedStatement statement = null;

      try {
        // Create SQL statement
        statement = connection.prepareStatement(sql);
      }
      catch (SQLException sqlException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Delete Associate SQL", sqlException);
    
          throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }

      // Initialize parameter index
      int parameterIndex = 1;

      // Set parameter values
      parameterIndex = setParameter(statement, dataObject.getId(), parentIdColumnBinding, parameterIndex);

      // Add statement to list
      sqlStatements.add(statement);
    }
  }

  /**
    Populates a list of prepared statements used to delete dependent data objects
    using a data object, data object binding, database connection and list of SQL statements.
    @param dataObject Data object.
    @param dataObjectBinding Data object binding.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  private void getDeleteDependentSql(DataObject<?> dataObject, DataObjectBinding dataObjectBinding,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Get dependent bindings
  	Collection<DependentBinding> dependentBindings = dataObjectBinding.getDependentBindings();

    for (DependentBinding dependentBinding : dependentBindings) {

      // Get property name
      String propertyName = dependentBinding.getPropertyName();

      // Get dependent data object binding
      DataObjectBinding dependentDataObjectBinding = dependentBinding.getDataObjectBinding();

      if (dependentBinding.isCollection()) {

        // Retrieve dependent data objects
        @SuppressWarnings("unchecked")
				Collection<DataObject<?>> dependentDataObjects = (Collection<DataObject<?>>)Introspector.getPropertyValue(dataObject, propertyName);

        if (dependentDataObjects != null) {

          // Create value iterator
          Iterator<DataObject<?>> values = dependentDataObjects.iterator();

          while (values.hasNext()) {

            // Get next dependent data object
          	DataObject<?> dependentDataObject = values.next(); 

            // Get delete SQL data object
            getDeleteSql(dependentDataObject, dependentDataObjectBinding, connection, domain, sqlStatements);
          }
        }
      }
      else {

        // Get dependent data object
        DataObject<?> dependentDataObject = (DataObject<?>)Introspector.getPropertyValue(dataObject, propertyName);

        // Get delete SQL data object
        if (dependentDataObject != null) {
          getDeleteSql(dependentDataObject, dependentDataObjectBinding, connection, domain, sqlStatements);
        }
      }
    }
  }

  /**
    Populates a list of prepared statements used to find a list of data objects
    using a value condition, database connection and list of SQL statements.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  protected void getFindSql(IValueCondition valueCondition, IOrderCondition orderCondition,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Get find SQL with no external joins
    getFindSql(null, valueCondition, orderCondition, connection, domain, sqlStatements);
  }

  /**
    Populates a list of prepared statements used to find a list of data objects
    using a list of joins, value condition, database connection and list of SQL statements.
    @param joins List of joins.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @param connection Database connection.
    @param domain Target domain.
    @param sqlStatements List of SQL statements.
  */
  protected void getFindSql(List<Join> joins, IValueCondition valueCondition, IOrderCondition orderCondition,
      Connection connection, IDomain domain, List<PreparedStatement> sqlStatements) throws DataSourceException {

    // Get primary table name
    String primaryTableName = dataObjectBinding.getTableName();

    // Create aliases map
    Map<String, String> aliases = new HashMap<String, String>();

    // Add primary table alias
    aliases.put(primaryTableName, "x");

    // Ensure joins is not null
    if (joins == null) {
    	joins = new ArrayList<Join>();
    }
    
    // Append required associate and dependent join
    appendRequiredJoins(dataObjectBinding, orderCondition, joins);
    appendRequiredAssociateJoins(dataObjectBinding, valueCondition, orderCondition, joins);
    appendRequiredDependentJoins(dataObjectBinding, valueCondition, orderCondition, joins);
    
    // Create join SQL
    String joinSql = createJoinSql(joins, aliases);

    // Append virtual delete condition if one exists
    valueCondition = appendVirtualDeleteCondition(valueCondition);

    // Append domain condition
    valueCondition = appendDomainCondition(primaryTableName, domain, valueCondition);
    
    // Initialize condition SQL
    String valueConditionSql = "";
    String orderConditionSql = "";

    // Get finder specific value condition SQL
    if (valueCondition != null) {
      valueConditionSql = " where " + valueCondition.generateSql(aliases);
    }

    // Get finder specific order condition SQL
    if (orderCondition != null) {
      orderConditionSql = " order by " + orderCondition.generateSql(aliases);
    }

    // Create primary SQL statement
    getFindPrimarySql(connection, primaryTableName, joinSql, valueConditionSql, orderConditionSql, valueCondition, sqlStatements);

    // Create associate SQL statements
    getFindAssociateSql(connection, primaryTableName, joinSql, valueConditionSql, valueCondition, aliases, sqlStatements);

    // Create dependent SQL statements
    getFindDependentSql(1, dataObjectBinding, aliases, connection, primaryTableName, joinSql, valueConditionSql, valueCondition, sqlStatements);
  }

  /**
   * Creates and appends joins to a list of joins if column binding use associative ordering and 
   * the associative table is referenced by an order condition.
   * @param dataObjectBinding Data object binding.
   * @param orderCondition Order condition.
   * @param joins List of joins to populate.
   */
  private void appendRequiredJoins(DataObjectBinding dataObjectBinding, IOrderCondition orderCondition, List<Join> joins) {
  	
  	if (orderCondition != null) {
  		
      // Get associate bindings
    	Collection<ColumnBinding> columnBindings = dataObjectBinding.getColumnBindings();

    	if (columnBindings != null && !columnBindings.isEmpty()) {
    		
        // Get source table name
        String sourceTableName = dataObjectBinding.getTableName();

        for (ColumnBinding columnBinding : columnBindings) {

        	if (columnBinding.useReferenceOrdering()) {
        		
            // Get reference table name
            String referenceTableName = columnBinding.getReferenceTableName();
        		
      			if (orderCondition.isTableReferenced(referenceTableName)) {
      				
      				// Get source column name
      				String sourceColumnName = columnBinding.getColumnName();
      				
      				// Get reference ID column name
      				String referenceIdColumnName = columnBinding.getReferenceIdColumnName();
      				
              // Create reference join condition
              JoinCondition referenceJoinCondition = new JoinCondition(sourceTableName, sourceColumnName, Operators.EQUALS, referenceTableName, referenceIdColumnName);
            	
              // Create reference join
              Join referenceJoin = new Join(Join.LEFT_OUTER_JOIN, sourceTableName, referenceTableName, referenceJoinCondition);

              // Add join to list
              joins.add(referenceJoin);
      			}
        	}
        }
    	}
  	}
  }
  
  /**
   * Creates and appends associate joins to a list of joins if their associated target table
   * is referenced by a value condition or order condition.
   * @param dataObjectBinding Data object binding.
   * @param valueCondition Value condition.
   * @param orderCondition Order condition.
   * @param joins List of joins to populate.
   */
  private void appendRequiredAssociateJoins(DataObjectBinding dataObjectBinding, IValueCondition valueCondition, IOrderCondition orderCondition, List<Join> joins) {
  	
    // Get associate bindings
  	Collection<AssociateBinding> associateBindings = dataObjectBinding.getAssociateBindings();

  	if (associateBindings != null && !associateBindings.isEmpty()) {
  		
      // Get source table name
      String sourceTableName = dataObjectBinding.getTableName();

      // Get ID column binding
      IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

      // Set source column name
      String sourceColumnName = idColumnBinding.getColumnName();

      for (AssociateBinding associateBinding : associateBindings) {

        // Get target table name
        String targetTableName = associateBinding.getTableName();

        // Initialize reference table name
        String referenceTableName = null;
        
        // Set reference table name if reference ordering is enabled
        if (associateBinding.useReferenceOrdering()) {
        	referenceTableName = associateBinding.getReferenceTableName();
        }
        
        // Set join type indicators
        boolean isValueConditionJoin = valueCondition != null && valueCondition.isTableReferenced(targetTableName);
        boolean isOrderConditionJoin = orderCondition != null && orderCondition.isTableReferenced(targetTableName);
        boolean isReferenceOrderConditionJoin = orderCondition != null && orderCondition.isTableReferenced(referenceTableName);
        
  			if (isValueConditionJoin || isOrderConditionJoin || isReferenceOrderConditionJoin) {
  				
          // Get parent ID column binding
          IdColumnBinding parentIdColumnBinding = associateBinding.getParentIdColumnBinding();

          // Default target column name to source column name
          String targetColumnName = parentIdColumnBinding.getColumnName();

          // Create join condition
          JoinCondition joinCondition = new JoinCondition(sourceTableName, sourceColumnName, Operators.EQUALS, targetTableName, targetColumnName);

          // Set join type
          String joinType = isValueConditionJoin ? Join.INNER_JOIN : Join.LEFT_OUTER_JOIN;
          
          // Create associate join
          Join associateJoin = new Join(joinType, sourceTableName, targetTableName, joinCondition);

          // Add join to list
          joins.add(associateJoin);
          
          if (isReferenceOrderConditionJoin) {
          	
          	// Get reference ID column name
          	String referenceIdColumnName = associateBinding.getReferenceIdColumnName();
          	
            // Create reference join condition
            JoinCondition referenceJoinCondition = new JoinCondition(targetTableName, targetColumnName, Operators.EQUALS, referenceTableName, referenceIdColumnName);
          	
            // Create reference join
            Join referenceJoin = new Join(Join.LEFT_OUTER_JOIN, targetTableName, referenceTableName, referenceJoinCondition);

            // Add join to list
            joins.add(referenceJoin);
          }
  			}
      }
  	}
  }
  
  /**
   * Recursively creates and appends dependent joins to a list of joins if their associated target table
   * is referenced by a value condition or order condition.
   * @param dataObjectBinding Data object binding.
   * @param valueCondition Value condition.
   * @param orderCondition Order condition.
   * @param joins List of joins to populate.
   */
  private void appendRequiredDependentJoins(DataObjectBinding dataObjectBinding, IValueCondition valueCondition, IOrderCondition orderCondition, List<Join> joins) {
  	
    // Get dependent bindings
  	Collection<DependentBinding> dependentBindings = dataObjectBinding.getDependentBindings();

    if (dependentBindings != null && !dependentBindings.isEmpty()) {
    	
      // Get source table name
      String sourceTableName = dataObjectBinding.getTableName();

      // Initialize associate joins
      List<Join> dependentJoins = new ArrayList<Join>();
      
      for (DependentBinding dependentBinding : dependentBindings) {

        // Get dependent data object binding
        DataObjectBinding dependentDataObjectBinding = dependentBinding.getDataObjectBinding();
      	
        // Recursively append required dependent joins
        appendRequiredDependentJoins(dependentDataObjectBinding, valueCondition, orderCondition, dependentJoins);
        
        // Get target table name
        String targetTableName = dependentDataObjectBinding.getTableName();

        if (!dependentJoins.isEmpty() || 
        	  (valueCondition != null && valueCondition.isTableReferenced(targetTableName)) || 
      			(orderCondition != null && orderCondition.isTableReferenced(targetTableName))) {

          // Get parent ID column binding
          IdColumnBinding parentIdColumnBinding = dependentDataObjectBinding.getParentIdColumnBinding();

          // Get source column name
          String sourceColumnName = parentIdColumnBinding.getColumnName();

          // Get ID column binding
          IdColumnBinding idColumnBinding = dependentDataObjectBinding.getIdColumnBinding();

          // Get target column name
          String targetColumnName = idColumnBinding.getColumnName();

          // Create join condition
          JoinCondition joinCondition = new JoinCondition(sourceTableName, sourceColumnName,
              Operators.EQUALS, targetTableName, targetColumnName);

          // Create dependent join
          Join dependentJoin = new Join(Join.INNER_JOIN, sourceTableName, targetTableName, joinCondition);
        	
        	// Append dependent join 
        	joins.add(dependentJoin);
        	
        	// Append recursively obtained dependent joins
        	joins.addAll(dependentJoins);
        }
        
      	// Clear existing associate joins
      	dependentJoins.clear();
      }  	
    }
  }
  
  /**
   * Creates and returns join SQL using a list of joins and a map of aliases keyed by table name.
   * @param joins List of joins.
   * @param aliases Map of aliases keyed by table name.
   * @return String Join SQL.
   */
  private String createJoinSql(List<Join> joins, Map<String, String> aliases) {
  	
    // Initialize return value
    StringBuffer joinSql = new StringBuffer();

    // Initialize join table count
    int joinTableCount = 1;

    if (joins != null) {

      // Append finder specific joins
      for (Join join : joins) {

        // Get source and target table names
        String sourceTableName = join.getSourceTableName().toLowerCase();
        String targetTableName = join.getTargetTableName().toLowerCase();

        // Add aliases
        if (!aliases.containsKey(sourceTableName)) {
          aliases.put(sourceTableName, "j" + String.valueOf(joinTableCount++));
        }
        if (!aliases.containsKey(targetTableName)) {
          aliases.put(targetTableName, "j" + String.valueOf(joinTableCount++));
        }

        // Append spacing
        if (joinSql.length() > 0) {
          joinSql.append(" ");
        }

        // Append join SQL
        joinSql.append(join.generateSql(aliases));
      }
    }
    
    return joinSql.toString();
  }

  /**
   * Creates a primary SQL statement and adds it to a given list of SQL statements. 
   * @param connection Database connection.
   * @param primaryTableName Primary table name.
   * @param joinSql Join SQL.
   * @param valueConditionSql Value condition SQL.
   * @param orderConditionSql Order condition SQL.
   * @param valueCondition Value condition.
   * @param sqlStatements List of SQL statements to populate.
   * @throws DataSourceException
   */
  private void getFindPrimarySql(Connection connection, String primaryTableName, String joinSql, String valueConditionSql, String orderConditionSql, 
  		IValueCondition valueCondition, List<PreparedStatement> sqlStatements)
  		throws DataSourceException {
  	
    // Set method name
    String methodName = "getFindPrimarySql";

    // Build SQL for primary table
    StringBuffer sql = new StringBuffer();
    sql.append("select distinct x.* from ").append(primaryTableName).append(" x ");
    sql.append(joinSql).append(valueConditionSql).append(orderConditionSql);

    // Initialize SQL statement
    PreparedStatement sqlStatement = null;

    try {
      // Create SQL statement
      sqlStatement = connection.prepareStatement(sql.toString());

      // Set finder specific SQL parameters
      if (valueCondition != null) {
        valueCondition.setParameters(sqlStatement, 1);
      }

      // Add statement to list
      sqlStatements.add(sqlStatement);
    }
    catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Find Primary SQL", sqlException);

        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
  }
  
  /**
   * Creates an associate SQL statement for each associated join and adds it to a given list of SQL statements. 
   * @param connection Database connection.
   * @param primaryTableName Primary table name.
   * @param joinSql Join SQL.
   * @param valueConditionSql Value condition SQL.
   * @param valueCondition Value condition.
   * @param aliases Map of aliases keyed by table name. 
   * @param sqlStatements List of SQL statements to populate.
   * @throws DataSourceException
   */
  private void getFindAssociateSql(Connection connection, String primaryTableName, String joinSql, String valueConditionSql,  
  		IValueCondition valueCondition, Map<String, String> aliases, List<PreparedStatement> sqlStatements) 
  		throws DataSourceException {
  	
    // Set method name
    String methodName = "getFindAssociateSql";

    // Get associate bindings
    Collection<AssociateBinding> associateBindings = dataObjectBinding.getAssociateBindings();
    
    if (associateBindings != null && !associateBindings.isEmpty()) {
    	
      // Get source table name
      String sourceTableName = dataObjectBinding.getTableName().toLowerCase();

      // Get ID column binding
      IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

      // Set source column name
      String sourceColumnName = idColumnBinding.getColumnName();

      // Create associate aliases map
      Map<String, String> associateAliases = new HashMap<String, String>();
      
      for (AssociateBinding associateBinding : associateBindings) {

      	// Reset associate aliases
      	associateAliases.clear();
      	associateAliases.putAll(aliases);

      	// Get target table name
        String targetTableName = associateBinding.getTableName().toLowerCase();

        // Initialize associate join SQL
        String associateJoinSql = "";
        
        if (!associateAliases.containsKey(targetTableName)) {
        	
          // Add target aliases if undefined
        	associateAliases.put(targetTableName, "a2");
        	
          // Add source aliases if undefined
          if (!associateAliases.containsKey(sourceTableName)) {
          	associateAliases.put(sourceTableName, "a1");
          }
        	
          // Get parent ID column binding
          IdColumnBinding parentIdColumnBinding = associateBinding.getParentIdColumnBinding();

          // Default target column name to source column name
          String targetColumnName = parentIdColumnBinding.getColumnName();

          // Create join condition
          JoinCondition joinCondition = new JoinCondition(sourceTableName, sourceColumnName,
              Operators.EQUALS, targetTableName, targetColumnName);

          // Create associate join
          Join associateJoin = new Join(Join.INNER_JOIN, sourceTableName, targetTableName, joinCondition);
          
          // Create associate join SQL
          associateJoinSql = associateJoin.generateSql(associateAliases);
        }

        // Get target aliases
        String targetAlias = associateAliases.get(targetTableName);

        // Build associate SQL
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct ").append(targetAlias).append(".*");
        sql.append(" from ").append(primaryTableName).append(" x ");
        sql.append(joinSql).append(" ").append(associateJoinSql);
        sql.append(valueConditionSql);

        try {
          // Create SQL statement
        	PreparedStatement sqlStatement = connection.prepareStatement(sql.toString());

          // Set finder specific SQL parameters
          if (valueCondition != null) {
            valueCondition.setParameters(sqlStatement, 1);
          }

          // Add statement to list
          sqlStatements.add(sqlStatement);
        }
        catch (SQLException sqlException) {

            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
            logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Find Associate SQL", sqlException);

            throw new DataSourceException(methodName + ": " + sqlException.getMessage());
        }
      }
    }
  }
  
  private int getFindDependentSql(int startIndex, DataObjectBinding parentDataObjectBinding, Map<String, String> parentAliases, Connection connection, 
  		String primaryTableName, String joinSql, String valueConditionSql, IValueCondition valueCondition, List<PreparedStatement> sqlStatements) 
  		throws DataSourceException {
  	
    // Set method name
    String methodName = "getFindDependentSql";

    // Get dependent bindings
    Collection<DependentBinding> dependentBindings = parentDataObjectBinding.getDependentBindings();

    if (dependentBindings != null && !dependentBindings.isEmpty()) {

      // Get source table name
      String sourceTableName = parentDataObjectBinding.getTableName().toLowerCase();

      // Create dependent aliases map
      Map<String, String> dependentAliases = new HashMap<String, String>();

      for (DependentBinding dependentBinding : dependentBindings) {
      
      	// Reset dependent aliases
      	dependentAliases.clear();
      	dependentAliases.putAll(parentAliases);

        // Add source aliases if undefined
        if (!dependentAliases.containsKey(sourceTableName)) {
        	dependentAliases.put(sourceTableName, "d" + String.valueOf(startIndex++));
        }
      	
        // Get dependent data object binding
        DataObjectBinding dependentDataObjectBinding = dependentBinding.getDataObjectBinding();
      	
        // Get target table name
        String targetTableName = dependentDataObjectBinding.getTableName().toLowerCase();
        
        // Initialize defined dependent join indicator
        boolean isDependentJoinDefined = false;
        
        if (!dependentAliases.containsKey(targetTableName)) {
        	
          // Add target aliases if undefined
        	dependentAliases.put(targetTableName, "d" + String.valueOf(startIndex++));
        	
        	// Set defined dependent join indicator
        	isDependentJoinDefined = true;
        }
        
      	// Initialize dependent join SQL
      	String dependentJoinSql = "";
      	
        if (!isDependentJoinDefined) {
        	
          // Get parent ID column binding
          IdColumnBinding parentIdColumnBinding = dependentDataObjectBinding.getParentIdColumnBinding();

          // Get source column name
          String sourceColumnName = parentIdColumnBinding.getColumnName();

          // Get ID column binding
          IdColumnBinding idColumnBinding = dependentDataObjectBinding.getIdColumnBinding();

          // Get target column name
          String targetColumnName = idColumnBinding.getColumnName();

          // Create join condition
          JoinCondition joinCondition = new JoinCondition(sourceTableName, sourceColumnName,
              Operators.EQUALS, targetTableName, targetColumnName);

          // Create dependent join
          Join dependentJoin = new Join(Join.INNER_JOIN, sourceTableName, targetTableName, joinCondition);
          
          // Append dependent join SQL
          dependentJoinSql = dependentJoin.generateSql(dependentAliases);
        }
        
        // Get target aliases
        String targetAlias = dependentAliases.get(targetTableName);
        
        // Build dependent SQL
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct ").append(targetAlias).append(".*");
        sql.append(" from ").append(primaryTableName).append(" x ");
        sql.append(joinSql).append(" ").append(dependentJoinSql);
        sql.append(valueConditionSql);

        try {
          // Create SQL statement
        	PreparedStatement sqlStatement = connection.prepareStatement(sql.toString());

          // Set finder specific SQL parameters
          if (valueCondition != null) {
            valueCondition.setParameters(sqlStatement, 1);
          }

          // Add statement to list
          sqlStatements.add(sqlStatement);
        }
        catch (SQLException sqlException) {

            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
            logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Find Dependent SQL", sqlException);

            throw new DataSourceException(methodName + ": " + sqlException.getMessage());
        }
        
        // Recursively find dependent SQL
        startIndex = getFindDependentSql(startIndex, dependentDataObjectBinding, dependentAliases, connection, primaryTableName, dependentJoinSql, valueConditionSql, valueCondition, sqlStatements);
      }
    }
    
    return startIndex;
  }
  
  /**
    Returns a list of data objects from the default domain using a list joins and value condition.
    @param joins List of joins.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @return List List of data objects.
  */
  protected List<T> find(List<Join> joins, IValueCondition valueCondition, IOrderCondition orderCondition) 
  		throws DataSourceException {
  	
  	return find(joins, valueCondition, orderCondition, null);
  }

  /**
    Returns a list of data objects from a specified domain using a list joins and value condition.
    @param joins List of joins.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @param domain Target domain.
    @return List List of data objects.
  */
  protected List<T> find(List<Join> joins, IValueCondition valueCondition, IOrderCondition orderCondition, IDomain domain) 
  		throws DataSourceException {

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
      getFindSql(joins, valueCondition, orderCondition, connection, domain, sqlStatements);

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
    Returns a list of data object IDs from the default domain using a list joins and value condition.
    @param joins List of joins.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @return List List of data object IDs.
  */
  protected List<I> findIds(List<Join> joins, IValueCondition valueCondition, IOrderCondition orderCondition)
      throws DataSourceException {
  	
  	return findIds(joins, valueCondition, orderCondition, null);
  }
  
  /**
    Returns a list of data object IDs from a specified domain using a list joins and value condition.
    @param joins List of joins.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @param domain Target domain.
    @return List List of data object IDs.
  */
  protected List<I> findIds(List<Join> joins, IValueCondition valueCondition, IOrderCondition orderCondition, IDomain domain)
      throws DataSourceException {

  	// Set method name variable
    String methodName = "findIds";
  
    // Initialize return value
    List<I> dataObjectIds = null;
  
    // Initialize processing variables
    Connection connection = null;
    PreparedStatement sqlStatement = null;
    ResultSet resultSet = null;
    
    try {
      // Retrieve database connection
      connection = openConnection(domain);
  
      // Retrieve find SQL
      sqlStatement = getFindIdSql(joins, valueCondition, orderCondition, connection, domain);
  
      // Validate find SQL
      if (sqlStatement == null) {
        throw new DataSourceException(methodName + ": No SQL returned from getFindIdSql method.");
      }
  
      // Execute SQL statement
      resultSet = sqlStatement.executeQuery();
  
      // Create data object ID list
      dataObjectIds = createIds(resultSet);
    }
    catch (CoreException coreException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Object IDs", coreException);
  
      throw new DataSourceException(methodName + ": " + coreException.getMessage());
    }
    catch (SQLException sqlException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Object IDs", sqlException);
  
      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    finally {
  
      // Close all statements and result sets
      DbUtil.closeStatement(sqlStatement);
      DbUtil.closeResultSet(resultSet);
  
      // Close connnection
      DbUtil.closeConnection(connection);
    }
  
    return dataObjectIds;
  }
  
  /**
    Returns a prepared statement used to find a list of data object IDs
    using a value condition and database connection.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @param connection Database connection.
    @param domain Target domain.
    @return PreparedStatement Prepared statement.
  */
  protected PreparedStatement getFindIdSql(IValueCondition valueCondition, IOrderCondition orderCondition, Connection connection, IDomain domain)
      throws DataSourceException {

    // Get find SQL with no external joins
    return getFindIdSql(null, valueCondition, orderCondition, connection, domain);
  }

  /**
    Returns a prepared statement used to find a list of data object IDs
    using a list of joins, value condition, and database connection.
    @param joins List of joins.
    @param valueCondition Value condition.
    @param orderCondition Order condition.
    @param connection Database connection.
    @param domain Target domain.
    @return PreparedStatement Prepared statement.
  */
  protected PreparedStatement getFindIdSql(List<Join> joins, IValueCondition valueCondition, IOrderCondition orderCondition,
      Connection connection, IDomain domain) throws DataSourceException {

    // Set method name
    String methodName = "getFindIdSql";

    // Initialize return value
    PreparedStatement sqlStatement = null;
    
    // Get primary table name
    String primaryTableName = dataObjectBinding.getTableName();

    // Create aliases map
    Map<String, String> aliases = new HashMap<String, String>();

    // Add primary table alias
    aliases.put(primaryTableName, "x");

    // Ensure joins is not null
    if (joins == null) {
    	joins = new ArrayList<Join>();
    }
    
    // Append required associate and dependent join
    appendRequiredJoins(dataObjectBinding, orderCondition, joins);
    appendRequiredAssociateJoins(dataObjectBinding, valueCondition, orderCondition, joins);
    appendRequiredDependentJoins(dataObjectBinding, valueCondition, orderCondition, joins);
    
    // Create join SQL
    String joinSql = createJoinSql(joins, aliases);

    // Append virtual delete condition if one exists
    valueCondition = appendVirtualDeleteCondition(valueCondition);

    // Append domain condition
    valueCondition = appendDomainCondition(primaryTableName, domain, valueCondition);
    
    // Initialize condition SQL
    String valueConditionSql = "";
    String orderConditionSql = "";

    // Get finder specific value condition SQL
    if (valueCondition != null) {
      valueConditionSql = " where " + valueCondition.generateSql(aliases);
    }

    // Get finder specific order condition SQL
    if (orderCondition != null) {
      orderConditionSql = " order by " + orderCondition.generateSql(aliases);
    }

    // Get ID column binding
    IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

    // Get primary column name
    String primaryColumnName = "x." + idColumnBinding.getColumnName();

    // Build SQL for primary table
    StringBuffer sql = new StringBuffer();
    sql.append("select distinct ").append(primaryColumnName);
    sql.append(" from ").append(primaryTableName).append(" x ");
    sql.append(joinSql).append(valueConditionSql).append(orderConditionSql);

    try {
      // Create SQL statement
      sqlStatement = connection.prepareStatement(sql.toString());

      // Set finder specific SQL parameters
      if (valueCondition != null) {
        valueCondition.setParameters(sqlStatement, 1);
      }
    }
    catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Find ID SQL", sqlException);
  
        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }
    
    return sqlStatement;
  }
  
  /**
    Returns a list of data object IDs using data from a result set object.
    @see DatabaseDataManager#createIds(ResultSet)
  */
  @Override
  protected List<I> createIds(ResultSet resultSet) throws DataSourceException {
    
    // Set method name
    String methodName = "createIds";

    // Initialize return value
    List<I> ids = null;

    if (resultSet != null) {

      // Create ID list
      ids = new ArrayList<I>();

      try {

        while (resultSet.next()) {

          // Get data object ID column binding
          IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();

          // Get column type and column name
          int columnType = idColumnBinding.getColumnType();
          String columnName = idColumnBinding.getColumnName();
          
          // Get next ID
          @SuppressWarnings("unchecked")
					I id = (I)DbUtil.getObject(resultSet, columnName, columnType);
          
          // Add ID to list
          ids.add(id);
        }
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Creating IDs", ioException);
  
        throw new DataSourceException(methodName + ": " + ioException.getMessage());
      }
      catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Creating IDs", sqlException);
  
        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }
    }

    return ids;
  }

  /**
    Returns a data object using data from a list of result set objects.
    @see DatabaseDataManager#createDataObject(List)
  */
  @Override
  protected T createDataObject(List<ResultSet> resultSets) throws DataSourceException {

    // Initialize return value
    T dataObject = null;

    // Create data objects - Only one is created
    List<T> dataObjects = createDataObjects(resultSets);

    // Get first data object if one exists
    if (dataObjects != null && dataObjects.size() > 0) {
      dataObject = dataObjects.get(0);
    }

    return dataObject;
  }

  /**
    Returns a list of data objects using data from a list of result set objects.
    @see DatabaseDataManager#createDataObjects
  */
  @Override
  protected List<T> createDataObjects(List<ResultSet> resultSets) throws DataSourceException {

    // Initialize return value
    List<T> dataObjects = null;

    // Create data objects
    if (resultSets != null) {
      dataObjects = createDataObjects(dataObjectBinding, resultSets.iterator());
    }

    return dataObjects;
  }

  /**
    Populates a list of data objects using a data object binding and data from a
    list of result set objects.
    @param dataObjectBinding Data object binding.
    @param resultSets Result set iterator.
    @return List Data objects.
  */
  private List<T> createDataObjects(DataObjectBinding dataObjectBinding, Iterator<ResultSet> resultSets) throws DataSourceException {

    // Set method name
    String methodName = "createDataObjects";

    // Initialize return value
    List<T> dataObjects = null;

    if (resultSets != null && resultSets.hasNext()) {

      // Create data objects list
      dataObjects = new ArrayList<T>();

      // Get data object class
      Class<?> dataObjectClass = dataObjectBinding.getDataObjectClass();

      // Create data object lookup used by associates and dependents
      Map<Object, DataObject<?>> dataObjectLookup = new HashMap<Object, DataObject<?>>();

      // Get next result set
      ResultSet resultSet = resultSets.next();

      try {

        while (resultSet.next()) {

          // Create new data object
          @SuppressWarnings("unchecked")
					T dataObject = (T)Instantiator.create(dataObjectClass);

          // Get data object ID and data object column bindings
          IdColumnBinding idColumnBinding = dataObjectBinding.getIdColumnBinding();
          Collection<ColumnBinding> columnBindings = dataObjectBinding.getColumnBindings();

          // Populate data object
          populate(dataObject, idColumnBinding, resultSet);
          populate(dataObject, columnBindings, resultSet);

          // Add data object to list
          dataObjects.add(dataObject);

          // Add to data object lookup
          dataObjectLookup.put(dataObject.getId(), dataObject);
        }
      }
      catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Creating Data Objects", sqlException);
  
        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }

      // Create associates
      createAssociates(dataObjectBinding, resultSets, dataObjectLookup);

      // Create dependents
      createDependents(dataObjectBinding, resultSets, dataObjectLookup);
    }

    return dataObjects;
  }

  /**
    Recursively creates associate data objects and assigns them to their parent's
    associated property using a parent data object binding, an iterator of result
    sets, and a parent data object lookup.
    @param parentBinding Parent data object binding.
    @param resultSets Result set iterator.
    @param parentLookup Parent data object lookup.
  */
  private void createAssociates(DataObjectBinding parentBinding, Iterator<ResultSet> resultSets, Map<Object, DataObject<?>> parentLookup) 
  		throws DataSourceException {

    // Set method name
    String methodName = "createAssociates";
    
    // Get associate bindings
    Collection<AssociateBinding> associateBindings = parentBinding.getAssociateBindings();

    // Check to see if associate bindings are defined
    if (associateBindings.size() > 0) {

      // Create parent values lookup used to store parent associate values
      Map<Object, List<Object>> parentValueLookup = new HashMap<Object, List<Object>>();

      for (AssociateBinding associateBinding : associateBindings) {

        // Get parent ID and associate ID column binding
        IdColumnBinding parentIdColumnBinding = associateBinding.getParentIdColumnBinding();
        IdColumnBinding associateIdColumnBinding = associateBinding.getAssociateIdColumnBinding();

        // Clear parent value lookup for each associate binding
        parentValueLookup.clear();

        if (resultSets != null && resultSets.hasNext()) {

          // Get next result set
          ResultSet resultSet = resultSets.next();

          try {
            
            while (resultSet.next()) {

              // Retrieve parent ID and associate ID
              Object parentId = getColumnValue(parentIdColumnBinding, resultSet);
              Object associateId = getColumnValue(associateIdColumnBinding, resultSet);

              if (parentId != null && associateId != null) {

                // Attempt to get existing parent values
                List<Object> parentValues = parentValueLookup.get(parentId);

                // Create parent values if not already present
                if (parentValues == null) {
                  parentValues = new ArrayList<Object>();
                  parentValueLookup.put(parentId, parentValues);
                }

                // Add associate ID to parent values
                parentValues.add(associateId);
              }
            }
          }
          catch (SQLException sqlException) {
  
            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
            logger.logp(Level.SEVERE, getClass().getName(), methodName, "Creating Associates", sqlException);
      
            throw new DataSourceException(methodName + ": " + sqlException.getMessage());
          }

          if (parentValueLookup.size() > 0) {

            // Get parent ID iterator
            Iterator<Object> parentIds = parentValueLookup.keySet().iterator();

            while (parentIds.hasNext()) {

              // Get next parent ID
              Object parentId = parentIds.next();

              // Get parent data object
              DataObject<?> parentDataObject = parentLookup.get(parentId);

              if (parentDataObject != null) {

                // Get property name
                String propertyName = associateBinding.getPropertyName();

                // Get parent values
                List<Object> parentValues = parentValueLookup.get(parentId);

                // Set parent data object's associate property
                if (associateBinding.isCollection()) {
                  Introspector.setPropertyValue(parentDataObject, propertyName, parentValues);
                }
                else {
                  Introspector.setPropertyValue(parentDataObject, propertyName, parentValues.get(0));
                }
              }
            }
          }
        }
      }
    }
  }

  /**
    Recursively creates dependent data objects and assigns them to their parent's
    associated property using a parent data object binding, an iterator of result
    sets, and a parent data object lookup.
    @param parentBinding Parent data object binding.
    @param resultSets Result set iterator.
    @param parentLookup Parent data object lookup.
  */
  private void createDependents(DataObjectBinding parentBinding, Iterator<ResultSet> resultSets, Map<Object, DataObject<?>> parentLookup) 
  		throws DataSourceException {

    // Set method name
    String methodName = "createDependents";
    
    // Get dependent bindings
    Collection<DependentBinding> dependentBindings = parentBinding.getDependentBindings();

    // Check to see if dependent bindings are defined
    if (dependentBindings.size() > 0) {

      // Create parent values lookup used to set parent dependent properties
      Map<Object, List<DataObject<?>>> parentValueLookup = new HashMap<Object, List<DataObject<?>>>();

      // Create data object lookup used by next level of descendants
      Map<Object, DataObject<?>> dataObjectLookup = new HashMap<Object, DataObject<?>>();

      for (DependentBinding dependentBinding : dependentBindings) {

        // Get dependent data object binding
        DataObjectBinding dependentDataObjectBinding = dependentBinding.getDataObjectBinding();

        // Get dependent data object class
        Class<?> dependentDataObjectClass = dependentDataObjectBinding.getDataObjectClass();

        // Get parent ID and associate ID column bindings
        IdColumnBinding parentIdColumnBinding = dependentDataObjectBinding.getParentIdColumnBinding();
        IdColumnBinding dependentIdColumnBinding = dependentDataObjectBinding.getIdColumnBinding();

        // Get column bindings
        Collection<ColumnBinding> columnBindings = dependentDataObjectBinding.getColumnBindings();

        // Clear parent value and data object lookups
        parentValueLookup.clear();
        dataObjectLookup.clear();

        if (resultSets != null && resultSets.hasNext()) {

          // Get next result set
          ResultSet resultSet = resultSets.next();

          try {
            
            while (resultSet.next()) {

              // Create dependent data object
							DataObject<?> dependentDataObject = (DataObject<?>)Instantiator.create(dependentDataObjectClass);

              // Populate dependent data object
              populate(dependentDataObject, dependentIdColumnBinding, resultSet);
              populate(dependentDataObject, parentIdColumnBinding, resultSet);
              populate(dependentDataObject, columnBindings, resultSet);

              // Get parent ID
              String parentIdPropertyName = parentIdColumnBinding.getPropertyName();
              Object parentId = Introspector.getPropertyValue(dependentDataObject, parentIdPropertyName);

              // Attempt to get existing parent values
              List<DataObject<?>> parentValues = parentValueLookup.get(parentId);

              // Create parent values if not already present
              if (parentValues == null) {
                parentValues = new ArrayList<DataObject<?>>();
                parentValueLookup.put(parentId, parentValues);
              }

              // Add descendent to parent values
              parentValues.add(dependentDataObject);

              // Add to data object lookup
              dataObjectLookup.put(dependentDataObject.getId(), dependentDataObject);
            }
          }
          catch (SQLException sqlException) {
            
            // Post error message
            Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
            logger.logp(Level.SEVERE, getClass().getName(), methodName, "Creating Dependents", sqlException);
      
            throw new DataSourceException(methodName + ": " + sqlException.getMessage());
          }

          if (parentValueLookup.size() > 0) {

            // Get parent ID iterator
            Iterator<Object> parentIds = parentValueLookup.keySet().iterator();

            while (parentIds.hasNext()) {

              // Get next parent ID
            	Object parentId = parentIds.next();

              // Get parent data object
              DataObject<?> parentDataObject = parentLookup.get(parentId);

              if (parentDataObject != null) {

                // Get property name
                String propertyName = dependentBinding.getPropertyName();

                // Get parent values
                List<DataObject<?>> parentValues = parentValueLookup.get(parentId);

                // Set parent data object's dependent property
                if (dependentBinding.isCollection()) {
                  Introspector.setPropertyValue(parentDataObject, propertyName, parentValues);
                }
                else {
                  Introspector.setPropertyValue(parentDataObject, propertyName, parentValues.get(0));
                }
              }
            }
          }

          // Create dependents
          if (dataObjectLookup.size() > 0) {
            createDependents(dependentDataObjectBinding, resultSets, dataObjectLookup);
          }
        }
      }
    }
  }

  /**
    Populates a property accessible object using an ID column binding
    and a result set object.
    @param object Target object.
    @param idColumnBinding ID column binding.
    @param resultSet Result set.
  */
  private void populate(Object object, IdColumnBinding idColumnBinding, ResultSet resultSet) throws DataSourceException {

    // Set method name
    String methodName = "populate";

    if (resultSet != null) {

      // Get column binding information
      String propertyName = idColumnBinding.getPropertyName();
      String columnName = idColumnBinding.getColumnName();
      int columnType = idColumnBinding.getColumnType();

      try {
        // Get column value
        Object columnValue = DbUtil.getObject(resultSet, columnName, columnType);

        // Set property value
        Introspector.setPropertyValue(object, propertyName, columnValue);
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Populating Object", ioException);

        throw new DataSourceException(methodName + ": " + ioException.getMessage());
      }
      catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Populating Object", sqlException);

        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }
    }
  }

  /**
    Populates a property accessible object using a list of column bindings and a result set object.
    @param object Target object.
    @param columnBindings Collection of data object column bindings.
    @param resultSet Result set.
  */
  private void populate(Object object, Collection<ColumnBinding> columnBindings, ResultSet resultSet)
      throws DataSourceException {

    // Set method name
    String methodName = "populate";

    if (resultSet != null) {

      // Populate data object
      for (ColumnBinding columnBinding : columnBindings) {

        // Get column binding information
        String propertyName = columnBinding.getPropertyName();
        String columnName = columnBinding.getColumnName();
        int columnType = columnBinding.getColumnType();

        try {
          // Get column value
          Object columnValue = DbUtil.getObject(resultSet, columnName, columnType);

          // Set property value
          Introspector.setPropertyValue(object, propertyName, columnValue);
        }
        catch (IOException ioException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Populating Object", ioException);

          throw new DataSourceException(methodName + ": " + ioException.getMessage());
        }
        catch (SQLException sqlException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Populating Object", sqlException);

          throw new DataSourceException(methodName + ": " + sqlException.getMessage());
        }
      }
    }
  }

  /**
    Returns a column value using an ID column binding and a result set object.
    @param idColumnBinding ID column binding.
    @param resultSet Result set.
    @return Object Insert column name string.
  */
  private Object getColumnValue(IdColumnBinding idColumnBinding, ResultSet resultSet) throws DataSourceException {

    // Set method name
    String methodName = "getColumnValue";

    // Initialize return value
    Object columnValue = null;

    if (resultSet != null) {

      // Get column binding information
      String columnName = idColumnBinding.getColumnName();
      int columnType = idColumnBinding.getColumnType();

      try {
        // Get value
        columnValue = DbUtil.getObject(resultSet, columnName, columnType);
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Retrieving Column Value", ioException);

        throw new DataSourceException(methodName + ": " + ioException.getMessage());
      }
      catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Retrieving Column Value", sqlException);

        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }
    }

    return columnValue;
  }

  /**
    Creates and returns an insert column name string consisting of comma delimited
    column names using a parent ID column binding and an associate ID column binding.
    @param parentIdColumnBinding Parent ID column binding.
    @param associateIdColumnBinding Associate ID column bindings.
    @return String Insert column name string.
  */
  private String getInsertColumns(IdColumnBinding parentIdColumnBinding, IdColumnBinding associateIdColumnBinding) {

    // Initialize return value
    String columns = null;

    if (parentIdColumnBinding != null && associateIdColumnBinding != null) {

      // Build columns
      columns = parentIdColumnBinding.getColumnName() + "," + associateIdColumnBinding.getColumnName();
    }

    return columns;
  }

  /**
    Creates and returns an insert column name string consisting of comma delimited
    column names using an ID column binding and a set of column bindings.
    @param idColumnBinding ID column binding.
    @param columnBindings Collection of column bindings.
    @return String Insert column name string.
  */
  private String getInsertColumns(IdColumnBinding idColumnBinding, Collection<ColumnBinding> columnBindings) {

    // Initialize return value
    String columns = null;

    if (idColumnBinding != null && columnBindings != null) {

      // Build columns
      columns = idColumnBinding.getColumnName();

      // Build delimited columns
      for (ColumnBinding columnBinding : columnBindings) {

        // Append column name
        columns = columns + "," + columnBinding.getColumnName();
      }

      if (isDomainAware()) {
      	
        // Append domain ID column
        columns = columns + "," + getDomainIdColumnName();
      }
    }

    return columns;
  }

  /**
    Creates and returns an insert parameter values string consisting of comma
    delimited parameter value place holders using a parent ID column binding and
    an associate ID column binding.
    @param parentIdColumnBinding Parent ID column binding.
    @param associateIdColumnBinding Associate ID column bindings.
    @return String Insert parameter values string.
  */
  private String getInsertParameters(IdColumnBinding parentIdColumnBinding, IdColumnBinding associateIdColumnBinding) {

    // Initialize return value
    String parameters = null;

    if (parentIdColumnBinding != null && associateIdColumnBinding != null) {

      // Build parameters
      parameters = "?,?";
    }

    return parameters;
  }

  /**
    Creates and returns an insert parameter values string consisting of comma
    delimited parameter value place holders using an ID column binding and
    a set of column bindings.
    @param idColumnBinding ID column binding.
    @param columnBindings Collection of column bindings.
    @return String Insert parameter values string.
  */
  private String getInsertParameters(IdColumnBinding idColumnBinding, Collection<ColumnBinding> columnBindings) {

    // Initialize return value
    String parameters = null;

    if (idColumnBinding != null && columnBindings != null) {

      // Build parameters
      parameters = "?";

      // Build delimited parameters
      for (int index = 0; index < columnBindings.size(); index++) {

        // Append parameter
        parameters = parameters + ",?";
      }

      if (isDomainAware()) {
      	
        // Append parameter
        parameters = parameters + ",?";
      }
    }

    return parameters;
  }

  /**
    Creates and returns an update column name string using data object column binding.
    @param columnBinding Column binding.
    @return String Update column name string.
  */
  private String getUpdateColumn(ColumnBinding columnBinding) {

    // Initialize return value
    String column = null;

    if (columnBinding != null) {

      // Build column
      column = columnBinding.getColumnName() + " = ?";
    }

    return column;
  }

  /**
    Creates and returns an update column name string consisting of comma delimited
    column name conditions using data object column bindings.
    @param columnBindings Collection of data object column bindings.
    @return String Update column name string.
  */
  private String getUpdateColumns(Collection<ColumnBinding> columnBindings) {

    // Initialize return value
    String columns = null;

    if (columnBindings != null) {

      // Build delimited columns
      for (ColumnBinding columnBinding : columnBindings) {

        // Build columns
        if (columns == null) {
          columns = columnBinding.getColumnName() + " = ?";
        }
        else {
          columns = columns + ", " + columnBinding.getColumnName() + " = ?";
        }
      }
    }

    return columns;
  }

  /**
    Creates and returns a condition string using a column binding.
    @param columnBinding Column binding.
    @return String Condition string.
  */
  private String getCondition(ColumnBinding columnBinding) {

    // Initialize return value
    String condition = null;

    // Build condition
    if (columnBinding != null) {
      condition = columnBinding.getColumnName() + " = ?";
    }

    return condition;
  }

  /**
    Creates and returns a condition string using a column binding and domain.
    @param columnBinding Column binding.
    @param domain Target domain.
    @return String Condition string.
  */
  private String getCondition(ColumnBinding columnBinding, IDomain domain) {
  
    // Initialize return value
    String condition = null;
  
    if (columnBinding != null) {
    	
      // Build condition using column binding
      condition = columnBinding.getColumnName() + " = ?";
    }
    
    if (isDomainAware()) {
    	
    	if (condition == null) {
    		
    		// Build condition using domain ID
        condition = getDomainIdColumnName() + " = ?";
    	}
    	else {
    		
    		// Append condition usin domain ID
        condition = condition + " and " + getDomainIdColumnName() + " = ?";
    	}
    }
  
    return condition;
  }
  
  /**
    Sets parameter values of a given SQL statement using a value,
    column binding, and a starting parameter index.
    @param sqlStatement SQL statement.
    @param value Parameter value.
    @param columnBinding Column binding.
    @param index Parameter index.
    @return int Next parameter index.
  */
  private int setParameter(PreparedStatement statement, Object value, ColumnBinding columnBinding, int index) throws DataSourceException {

    // Set method name
    String methodName = "setParameter";

    // Initialize parameter index
    int nextIndex = index;

    if (statement != null && columnBinding != null) {

      // Get column type
      int columnType = columnBinding.getColumnType();

      try {
        // Set SQL parameter
        DbUtil.setObject(statement, nextIndex++, columnType, value);
      }
      catch (SQLException sqlException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Setting SQL Parameter", sqlException);

        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }
    }

    return nextIndex;
  }

  /**
    Sets parameter values of a given SQL statement using a property accessible object,
    list of column bindings, and a starting parameter index.
    @param sqlStatement SQL statement.
    @param object Target object.
    @param columnBindings Collection of data object column bindings.
    @param index Parameter index.
    @return int Next parameter index.
  */
  private int setParameters(PreparedStatement statement, Object object,
  		Collection<ColumnBinding> columnBindings, int index) throws DataSourceException {

    // Set method name
    String methodName = "setParameters";

    // Initialize parameter index
    int nextIndex = index;

    if (statement != null && object != null && columnBindings != null) {

      // Set parameters
      for (ColumnBinding columnBinding : columnBindings) {

        // Get column type
        int columnType = columnBinding.getColumnType();

        try {
          // Retrieve property value
          Object propertyValue = Introspector.getPropertyValue(object, columnBinding.getPropertyName());

          // Set SQL parameter
          DbUtil.setObject(statement, nextIndex++, columnType, propertyValue);
        }
        catch (SQLException sqlException) {

          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Setting SQL Parameters", sqlException);

          throw new DataSourceException(methodName + ": " + sqlException.getMessage());
        }
      }
    }

    return nextIndex;
  }

  /**
    Sets parameter values of a given SQL statement using a specified domain.
    @param sqlStatement SQL statement.
    @param domain Target domain.
    @param index Parameter index.
    @return int Next parameter index.
  */
  private int setParameter(PreparedStatement statement, IDomain domain, int index) throws DataSourceException {
  
    // Set method name
    String methodName = "setParameter";
  
    // Initialize parameter index
    int nextIndex = index;
  
    if (statement != null && isDomainAware()) {
  
    	// Initialize domain ID
    	Integer domainId = IDomain.DEFAULT_ID;
    	
    	// Use specified domain ID if provided
    	if (domain != null) {
    		domainId = domain.getId();
    	}
    	
      try {
        // Set SQL parameter
        DbUtil.setObject(statement, nextIndex++, DbUtil.INTEGER, domainId);
      }
      catch (SQLException sqlException) {
  
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Setting SQL Parameter", sqlException);
  
        throw new DataSourceException(methodName + ": " + sqlException.getMessage());
      }
    }
  
    return nextIndex;
  }
  
}
