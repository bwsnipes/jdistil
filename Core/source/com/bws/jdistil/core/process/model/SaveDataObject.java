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

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.message.Messages;
import com.bws.jdistil.core.process.IProcessor;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.core.servlet.Loader;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.servlet.ServletException;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.StringUtil;

/**
  Saves a data object using submitted data.
  @author - Bryan Snipes
*/
public class SaveDataObject<I, T extends DataObject<I>> extends Processor {

  /**
    Data object class.
  */
  private Class<? extends DataObject<I>> dataObjectClass = null;

  /**
    Data manager class.
  */
  private Class<? extends IDataManager<I, T>> dataManagerClass = null;

  /**
    Attribute ID.
  */
  private String attributeId = null;

  /**
    Field ID.
  */
  private String fieldId = null;

  /**
    Check dirty update indicator.
  */
  private boolean doDirtyUpdateCheck = false;

  /**
    Success processor class.
  */
  private Class<? extends IProcessor> successProcessorClass = null;

  /**
    Failure processor class.
  */
  private Class<? extends IProcessor> failureProcessorClass = null;

  /**
    Session usage indicator.
  */
  private boolean useSession = false;
  
  /**
    Creates a new SaveDataObject object.
    @param dataObjectClass - Data object class.
    @param dataManagerClass - Data manager class.
    @param fieldId - Field ID.
    @param attributeId - Attribute name.
    @param doDirtyUpdateCheck - Dirty update check indicator.
    @param successProcessorClass - Success processor class.
    @param failureProcessorClass - Failure processor class.
    @param useSession - Indicates whether or not the data object should be stored in the session.
  */
  public SaveDataObject(Class<? extends DataObject<I>> dataObjectClass, Class<? extends IDataManager<I, T>> dataManagerClass, 
      String fieldId, String attributeId, boolean doDirtyUpdateCheck, Class<? extends IProcessor> successProcessorClass,
      Class<? extends IProcessor> failureProcessorClass, boolean useSession) {

    super();

    // Validate parameters
    if (dataObjectClass == null) {
      throw new java.lang.IllegalArgumentException("Data object class is required.");
    }
    if (dataManagerClass == null) {
      throw new java.lang.IllegalArgumentException("Data manager class is required.");
    }
    if (StringUtil.isEmpty(attributeId)) {
      throw new java.lang.IllegalArgumentException("Attribute name is required.");
    }
    if (StringUtil.isEmpty(fieldId)) {
      throw new java.lang.IllegalArgumentException("ID field ID is required.");
    }
    if (successProcessorClass == null) {
      throw new java.lang.IllegalArgumentException("Success processor class is required.");
    }
    if (failureProcessorClass == null) {
      throw new java.lang.IllegalArgumentException("Failure processor class is required.");
    }

    // Set properties
    this.dataObjectClass = dataObjectClass;
    this.dataManagerClass = dataManagerClass;
    this.attributeId = attributeId;
    this.fieldId = fieldId;
    this.doDirtyUpdateCheck = doDirtyUpdateCheck;
    this.successProcessorClass = successProcessorClass;
    this.failureProcessorClass = failureProcessorClass;
    this.useSession = useSession;
  }

  /**
    Saves a data object using submitted data.
    @see com.bws.jdistil.core.process.IProcessor#process
  */
  @SuppressWarnings("unchecked")
	public void process(ProcessContext processContext) throws ProcessException {

    // Check for errors before attempting to save
    if (!processContext.getErrorMessages().isEmpty()) {

      // Forward processing to failed processor class
      forward(failureProcessorClass, processContext);
    }
    else {
    	
      // Initialize data object
      T dataObject = null;

      if (useSession) {
        
        // Get current session
        HttpSession session = processContext.getRequest().getSession(true);

        // Retrieve data object from session
        dataObject = (T)session.getAttribute(attributeId);
      }
      else {
        
        // Get data object ID
        I id = (I)ParameterExtractor.getInteger(processContext.getRequest(), fieldId);

        if (id == null) {

          // Create new data object
          dataObject = (T)Instantiator.create(dataObjectClass);
        }
        else {

          // Retrieve existing data object
          dataObject = findDataObject(dataManagerClass, id);
        }
      }

      if (dataObject != null) {

      	// Set method name
      	String methodName = "process";
      	
      	try {
      		
          // Populate data object
          Loader.load(processContext.getSecurityManager(), processContext.getRequest(), processContext.getAction(), dataObject);

          // Save data object
          boolean isSuccess = saveDataObject(dataManagerClass, dataObject, doDirtyUpdateCheck, attributeId, processContext);

          // Forward processing based on successful save
          if (isSuccess) {
            forward(successProcessorClass, processContext);
          }
          else {
            forward(failureProcessorClass, processContext);
          }
      	}
        catch (ServletException servletException) {
          
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.core.process.model");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Saving Data Object", servletException);
          
          throw new ProcessException(methodName + ":" + servletException.getMessage());
        }
      }
      else {

  	    // Get locale specific deleted update message
  	    Locale locale = processContext.getRequest().getLocale();
  	    String deletedUpdateMessage = Messages.formatMessage(locale, Messages.DELETED_UPDATE, null);
  	    
        // Add deleted data error
        processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, deletedUpdateMessage));

        // Forward processing to success processor class
        forward(successProcessorClass, processContext);
      }
    }
  }

}
