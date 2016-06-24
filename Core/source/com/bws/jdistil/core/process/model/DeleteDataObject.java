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
package com.bws.jdistil.core.process.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.process.IProcessor;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.StringUtil;

/**
  Deletes a data object using submitted information.
  @author Bryan Snipes
*/
public class DeleteDataObject<I, T extends DataObject<I>> extends Processor {

  /**
    Data manager class.
  */
  private Class<? extends IDataManager<I, T>> dataManagerClass = null;

  /**
    Field ID.
  */
  private String fieldId = null;

  /**
    Next processor class.
  */
  private Class<? extends IProcessor> nextProcessorClass = null;
  
  /**
    Creates a new DeleteDataObject object.
    @param dataManagerClass Data manager class.
    @param fieldId Field ID.
    @param nextProcessorClass Next processor class.
  */
  public DeleteDataObject(Class<? extends IDataManager<I, T>> dataManagerClass, String fieldId, Class<? extends IProcessor> nextProcessorClass) {
    super();

    // Validate parameters
    if (dataManagerClass == null) {
      throw new java.lang.IllegalArgumentException("Data manager class is required.");
    }
    if (StringUtil.isEmpty(fieldId)) {
      throw new java.lang.IllegalArgumentException("ID field ID is required.");
    }
    if (nextProcessorClass == null) {
      throw new java.lang.IllegalArgumentException("Next processor class is required.");
    }

    // Set properties
    this.dataManagerClass = dataManagerClass;
    this.fieldId = fieldId;
    this.nextProcessorClass = nextProcessorClass;
  }

  /**
    Retrieves information needed to delete a data object.
    @see com.bws.jdistil.core.process.IProcessor#process
  */
  @SuppressWarnings("unchecked")
	public void process(ProcessContext processContext) throws ProcessException {

    // Set method name
    String methodName = "process";

    // Initialize data manager
    IDataManager<I, T> dataManager = null;

    // Check for a registered factory
    IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);

    // Create data manager
    dataManager = (IDataManager<I, T>)dataManagerFactory.create();

    try {

      // Get data object ID
      I id = (I)ParameterExtractor.getObject(processContext.getRequest(), fieldId);
  
      if (id != null) {
  
        // Retrieve data object
        T dataObject = dataManager.find(id);
  
        // Delete data object if found
        if (dataObject != null) {
          dataManager.delete(dataObject);
        }
      }
      
      // Forward process to next processor
      forward(nextProcessorClass, processContext);
    }
    catch (DataSourceException dataSourceException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.process.model");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Deleting Data Objects", dataSourceException);

      throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
    }
    finally {

      // Recycle data manager
      if (dataManagerFactory != null) {
        dataManagerFactory.recycle(dataManager);
      }
    }
  }

}
