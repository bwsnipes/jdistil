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

import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.message.Messages;
import com.bws.jdistil.core.process.IProcessor;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.StringUtil;

import javax.servlet.http.HttpSession;

/**
  Retrieves information needed to add/edit a data object.
  @author - Bryan Snipes
*/
public class EditDataObject<I, T extends DataObject<I>> extends Processor {

  /**
    Data object class.
  */
  private Class<? extends T> dataObjectClass = null;
  
  /**
    Data manager class.
  */
  private Class<? extends IDataManager<I, T>> dataManagerClass = null;

  /**
    Field ID.
  */
  private String fieldId = null;
  
  /**
    Attribute ID used to store data object in request attributes.
  */
  private String attributeId = null;

  /**
    Next page ID.
  */
  private String nextPageId = null;

  /**
    Failure processor class.
  */
  private Class<? extends IProcessor> failureProcessorClass = null;

  /**
    Session usage indicator.
  */
  private boolean useSession = false;
  
  /**
    Creates a new EditDataObject object.
    @param dataObjectClass - Data object class.
    @param dataManagerClass - Data manager class.
    @param fieldId - Field ID.
    @param attributeId - Attribute ID.
    @param nextPageId - Next page ID.
    @param failureProcessorClass - Failure processor class.
    @param useSession - Indicates whether or not the data object should be stored in the session.
  */
  public EditDataObject(Class<? extends T> dataObjectClass, Class<? extends IDataManager<I, T>> dataManagerClass, 
      String fieldId, String attributeId, String nextPageId, Class<? extends IProcessor> failureProcessorClass, boolean useSession) {

    super();

    // Validate parameters
    if (dataObjectClass == null) {
      throw new java.lang.IllegalArgumentException("Data object class is required.");
    }
    if (dataManagerClass == null) {
      throw new java.lang.IllegalArgumentException("Data manager class is required.");
    }
    if (StringUtil.isEmpty(fieldId)) {
      throw new java.lang.IllegalArgumentException("ID field ID is required.");
    }
    if (StringUtil.isEmpty(attributeId)) {
      throw new java.lang.IllegalArgumentException("Attribute name is required.");
    }
    if (StringUtil.isEmpty(nextPageId)) {
      throw new java.lang.IllegalArgumentException("Next page ID is required.");
    }
    if (failureProcessorClass == null) {
      throw new java.lang.IllegalArgumentException("Failure processor class is required.");
    }

    // Set properties
    this.dataObjectClass = dataObjectClass;
    this.dataManagerClass = dataManagerClass;
    this.fieldId = fieldId;
    this.attributeId = attributeId;
    this.nextPageId = nextPageId;
    this.failureProcessorClass = failureProcessorClass;
    this.useSession = useSession;
  }

  /**
    Retrieves information needed to add/edit a data object.
    @see com.bws.jdistil.core.process.IProcessor#process
  */
  @SuppressWarnings("unchecked")
	public void process(ProcessContext processContext) throws ProcessException {

    // Set method name
    String methodName = "process";

    // Validate field ID
    if (StringUtil.isEmpty(fieldId)) {
      throw new ProcessException(methodName + ": Field ID is required.");
    }
    
    if (!processContext.getErrorMessages().isEmpty()) {
      
      // Populate reference data
      populateReferenceData(processContext);

      // Set next page
      setNextPage(nextPageId, processContext);
    }
    else {

      // Get current session
      HttpSession session = processContext.getRequest().getSession(true);
      
      // Get data object ID
      I id = (I)ParameterExtractor.getObject(processContext.getRequest(), fieldId);

      if (id == null) {

        // Populate reference data
        populateReferenceData(processContext);

        if (useSession) {

          // Create new data object
          T dataObject = (T)Instantiator.create(dataObjectClass);

          // Store data object in session
          session.setAttribute(attributeId, dataObject);
        }
        
        // Set next page
        setNextPage(nextPageId, processContext);
      }
      else {

        // Find data object
        T dataObject = findDataObject(dataManagerClass, id);

        if (dataObject != null) {

          // Add to request attributes
          processContext.getRequest().setAttribute(attributeId, dataObject);

          // Store data object in session
          if (useSession) {
            session.setAttribute(attributeId, dataObject);
          }

          // Populate reference data
          populateReferenceData(processContext);

          // Set next page
          setNextPage(nextPageId, processContext);
        }
        else {

    	    // Get locale specific deleted update message
    	    Locale locale = processContext.getRequest().getLocale();
    	    String deletedUpdateMessage = Messages.formatMessage(locale, Messages.DELETED_UPDATE, null);
    	    
          // Add deleted data error
          processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, deletedUpdateMessage));

          // Forward processing to failure process
          forward(failureProcessorClass, processContext);
        }
      }
    }
  }
  
  /**
    Populates the request attributes with reference data needed to add/edit a data object.
    @param processContext - Process context.
  */
  protected void populateReferenceData(ProcessContext processContext) throws ProcessException {

    // Do nothing by default
  }

}
