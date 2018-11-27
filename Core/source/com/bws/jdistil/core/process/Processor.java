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
package com.bws.jdistil.core.process;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.DirtyUpdateException;
import com.bws.jdistil.core.datasource.DuplicateException;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.message.Messages;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.security.ISecurityManager;

/**
  Abstract processor class providing various utility methods.
  @author Bryan Snipes
*/
public abstract class Processor implements IProcessor {

  /**
    Creates a new Processor object.
  */
  public Processor() {
    super();
  }

  /**
    Gets the current user's domain.
    @param processContext Process context.
    @return IDomain Current user's domain.
  */
  protected IDomain getCurrentDomain(ProcessContext processContext) throws ProcessException {
  
  	// Set method name
  	String methodName = "getCurrentDomain";
  	
  	// Initialize return value
  	IDomain domain = null;
  	
  	if (processContext != null) {
  	
  		// Get security manager
  		ISecurityManager securityManager = processContext.getSecurityManager();
  		
  		if (securityManager != null) {
  			
  			try {
  				
    	    // Get current session
    	    HttpSession session = processContext.getRequest().getSession(true);

    	    // Get current domain
    			domain = securityManager.getDomain(session);
  			}
  		  catch (com.bws.jdistil.core.security.SecurityException securityException) {
  		  	
  		    // Post error message
  		    Logger logger = Logger.getLogger("com.bws.jdistil.core.process");
  		    logger.logp(Level.SEVERE, getClass().getName(), methodName, "Get Current Domain", securityException);
  		
  		    throw new ProcessException(methodName + ":" + securityException.getMessage());
  		  }
  		}
  	}
  	
  	return domain;
  }
  
  /**
    Sets the next page using a specified page ID and process context.
    @param nextPageId Next page ID.
    @param processContext Process context.
  */
  public void setNextPage(String nextPageId, ProcessContext processContext) {

    if (nextPageId != null) {

      // Get next page
      Page nextPage = ConfigurationManager.getPage(nextPageId);

      if (nextPage != null) {
        processContext.setNextPage(nextPage);
      }
    }
  }

  /**
    Forwards the current process using a specified processor class.
    @param processorClass Processor class.
    @param processContext Process context.
  */
  protected void forward(Class<? extends IProcessor> processorClass, ProcessContext processContext)
      throws ProcessException {

    if (processorClass != null) {

      // Initialize processor
      IProcessor processor = null;

      // Check for a registered factory
      IFactory processorFactory = ConfigurationManager.getFactory(processorClass);

      // Create processor
      processor = (IProcessor)processorFactory.create();

      try {
        // Forward process
        processor.process(processContext);
      }
      finally {

        // Recycle procesor
        processorFactory.recycle(processor);
      }
    }
  }

  /**
   * Forwards the current process to all processors defined for the specified action.
   * @param action Action used to forward processing.
   * @param processContext Process context.
   * @throws ProcessException
   */
  protected void forward(Action action, ProcessContext processContext) throws ProcessException {

    if (action != null) {

      // Get processor factories
      List<IFactory> factories = action.getProcessorFactories();

      if (factories != null) {
      	
        for (IFactory factory : factories) {

          // Create processor
          IProcessor processor = (IProcessor)factory.create();

          // Invoke process
          try {
            processor.process(processContext);
          }
          finally {
            factory.recycle(processor);
          }
        }
      }
    }
  }
  
  /**
	  Loads a data object as reference data into the request attributes
	  using a data manager, data object ID, attribute name, and process context.
	  @param dataManagerClass Data manager class.
	  @param dataObjectId Data object ID.
	  @param attributeName Attribute name used to store reference data.
    @param processContext Process context.
	*/
	protected <I, T extends DataObject<I>> void loadReferenceData(Class<? extends IDataManager<I, T>> dataManagerClass, 
			I dataObjectId, String attributeName, ProcessContext processContext) throws ProcessException {
	  
	  // Retrieve data objects
	  T dataObject = findDataObject(dataManagerClass, dataObjectId, processContext);
	  
	  // Add data object to request attributes
	  if (dataObject != null) {
	    processContext.getRequest().setAttribute(attributeName, dataObject);
	  }
	}
	
	/**
	  Loads a list of data objects as reference data into the request attributes
	  using a data manager, attribute name, and process context.
	  @param dataManagerClass Data manager class.
	  @param attributeName Attribute name used to store reference data.
    @param processContext Process context.
	*/
	protected <I, T extends DataObject<I>> void loadReferenceData(Class<? extends IDataManager<I, T>> dataManagerClass, 
			String attributeName, ProcessContext processContext) throws ProcessException {
	  
	  // Retrieve data objects
	  List<T> dataObjects = findDataObjects(dataManagerClass, processContext);
	  
	  // Add data objects to request attributes
	  if (dataObjects != null) {
	    processContext.getRequest().setAttribute(attributeName, dataObjects);
	  }
	}
	
	/**
	  Retrieves a data object using a specified ID.
	  @param dataManagerClass Data manager class.
	  @param id Data object ID.
    @param processContext Process context.
	  @return T Data object.
	*/
	@SuppressWarnings("unchecked")
	protected <I, T extends DataObject<I>> T findDataObject(Class<? extends IDataManager<I, T>> dataManagerClass, I id, ProcessContext processContext) throws ProcessException {
	
	  // Set method name
	  String methodName = "findDataObject";
	
	  // Initialize return value
	  T dataObject = null;
	
	  // Initialize data manager
	  IDataManager<I, T> dataManager = null;
	
	  // Check for a registered factory
	  IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);
	
	  // Create data manager
	  dataManager = (IDataManager<I, T>)dataManagerFactory.create();
	
		// Get current domain
		IDomain domain = getCurrentDomain(processContext);
		
	  try {
	    // Retrieve data object
	    dataObject = dataManager.find(id, domain);
	  }
	  catch (DataSourceException dataSourceException) {
	
	    // Post error message
	    Logger logger = Logger.getLogger("com.bws.jdistil.core.process");
	    logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Object", dataSourceException);
	
	    throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
	  }
	  finally {
	
	    // Recycle data manager
      dataManagerFactory.recycle(dataManager);
	  }
	
	  return dataObject;
	}
	
	/**
	  Retrieves all data objects.
	  @param dataManagerClass Data manager class.
    @param processContext Process context.
	  @return List List of data objects.
	*/
	@SuppressWarnings("unchecked")
	protected <I, T extends DataObject<I>> List<T> findDataObjects(Class<? extends IDataManager<I, T>> dataManagerClass, ProcessContext processContext) throws ProcessException {
	
	  // Set method name
	  String methodName = "findDataObjects";
	
	  // Initialize return value
	  List<T> dataObjects = null;
	
	  // Initialize data manager
	  IDataManager<I, T> dataManager = null;
	
	  // Check for a registered factory
	  IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);
	
	  // Create data manager
	  dataManager = (IDataManager<I, T>)dataManagerFactory.create();
	
		// Get current domain
		IDomain domain = getCurrentDomain(processContext);

		try {
	    // Retrieve data objects
	    dataObjects = dataManager.find(domain);
	  }
	  catch (DataSourceException dataSourceException) {
	
	    // Post error message
	    Logger logger = Logger.getLogger("com.bws.jdistil.core.process");
	    logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", dataSourceException);
	
	    throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
	  }
	  finally {
	
	    // Recycle data manager
      dataManagerFactory.recycle(dataManager);
	  }
	
	  return dataObjects;
	}
	
	/**
	  Retrieves all data objects using a list of data object IDs.
	  @param dataManagerClass Data manager class.
	  @param dataObjectIds List of data object IDs.
    @param processContext Process context.
	  @return List List of data objects.
	*/
	@SuppressWarnings("unchecked")
	protected <I, T extends DataObject<I>> List<T> findDataObjects(Class<? extends IDataManager<I, T>> dataManagerClass, 
			List<I> dataObjectIds, ProcessContext processContext) throws ProcessException {
	
	  // Set method name
	  String methodName = "findDataObjects";
	
	  // Initialize return value
	  List<T> dataObjects = null;
	
	  // Initialize data manager
	  IDataManager<I, T> dataManager = null;
	
	  // Check for a registered factory
	  IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);
	
	  // Create data manager
	  dataManager = (IDataManager<I, T>)dataManagerFactory.create();
	
		// Get current domain
		IDomain domain = getCurrentDomain(processContext);

		try {
	    // Retrieve data objects
	    dataObjects = dataManager.find(dataObjectIds, domain);
	  }
	  catch (DataSourceException dataSourceException) {
	
	    // Post error message
	    Logger logger = Logger.getLogger("com.bws.jdistil.core.process");
	    logger.logp(Level.SEVERE, getClass().getName(), methodName, "Finding Data Objects", dataSourceException);
	
	    throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
	  }
	  finally {
	
	    // Recycle data manager
      dataManagerFactory.recycle(dataManager);
  }
	
	  return dataObjects;
	}
	
	/**
	  Saves a data object using a data manager class. Attribute name is used to
	  store the data object in the request attributes when a duplicate or dirty update occurs.
	  @param dataManagerClass Data manager class.
	  @param dataObject Data object.
	  @param doDirtyUpdateCheck Dirty update check indicator.
	  @param attributeId Attribute ID.
	  @param processContext Process context.
	  @return boolean Success indicator.
	*/
	@SuppressWarnings("unchecked")
	protected <I, T extends DataObject<I>> boolean saveDataObject(Class<? extends IDataManager<I, T>> dataManagerClass, T dataObject, 
	    boolean doDirtyUpdateCheck, String attributeId, ProcessContext processContext) 
	    throws ProcessException {
	
	  // Set method name
	  String methodName = "saveDataObject";
	
	  // Initialize return value
	  boolean isSuccess = false;
	
	  // Initialize data manager
	  IDataManager<I, T> dataManager = null;
	
	  // Check for a registered factory
	  IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);
	
	  // Create data manager
	  dataManager = (IDataManager<I, T>)dataManagerFactory.create();
	
		// Get current domain
		IDomain domain = getCurrentDomain(processContext);

	  try {
	    // Save data object
	    dataManager.save(dataObject, doDirtyUpdateCheck, domain);
	
	    // Set success indicator
	    isSuccess = true;
	  }
	  catch (DuplicateException duplicateException) {
	
	    // Post info message
	    Logger logger = Logger.getLogger("com.bws.jdistil.core.process");
	    logger.logp(Level.INFO, getClass().getName(), methodName, "Saving Data Object", duplicateException);
	
	    // Get locale specific duplicate update message
	    Locale locale = processContext.getRequest().getLocale();
	    String duplicateUpdateMessage = Messages.formatMessage(locale, Messages.DIRTY_UPDATE, null);
	    
	    // Add duplicate error message
	    processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, duplicateUpdateMessage));
	
	    // Add data object to request attributes
	    processContext.getRequest().setAttribute(attributeId, dataObject);
	  }
	  catch (DirtyUpdateException dirtyUpdateException) {
	
	    // Post info message
	    Logger logger = Logger.getLogger("com.bws.jdistil.core.process");
	    logger.logp(Level.INFO, getClass().getName(), methodName, "Saving Data Object", dirtyUpdateException);
	    
	    // Get locale specific dirty update message
	    Locale locale = processContext.getRequest().getLocale();
	    String dirtyUpdateMessage = Messages.formatMessage(locale, Messages.DIRTY_UPDATE, null);
	    
	    // Add dirty update error message
	    processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, dirtyUpdateMessage));
	
	    // Add latest data object to request attributes
	    processContext.getRequest().setAttribute(attributeId, dirtyUpdateException.getData());
	  }
	  catch (DataSourceException dataSourceException) {
	
	    // Post error message
	    Logger logger = Logger.getLogger("com.bws.jdistil.core.process");
	    logger.logp(Level.SEVERE, getClass().getName(), methodName, "Saving Data Object", dataSourceException);
	
	    throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
	  }
	  finally {
	
	    // Recycle data manager
      dataManagerFactory.recycle(dataManager);
	  }
	
	  return isSuccess;
	}
	
}
