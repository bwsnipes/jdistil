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
package com.bws.jdistil.core.tag.data;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.conversion.IConverter;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.tag.table.Table;
import com.bws.jdistil.core.tag.table.TableRow;
import com.bws.jdistil.core.util.Introspector;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Component used to write a data object associated with a target data object using an HTML text field.
  This component also provides a button for invoking an action capable of editing the data object associated
  with the target data object.
  @author Bryan Snipes
*/
public class DisplayAssociateField extends ValueComponent {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = -1353153594134563225L;

	/**
	  Items attribute name.
	*/
	private Class<? extends IDataManager<?, ?>> associateDataManagerClass = null;
	
	/**
    Associate display field ID.
  */
  protected String associateDisplayFieldId = null;
  
	/**
  	Map of associate lookups containing data objects supporting efficient retrieval of data when component is nested inside a table component.
  */
  private Map<String, Map<Object, DataObject<Object>>> associateLookups = new HashMap<String, Map<Object, DataObject<Object>>>();
  
  /**
    Creates a new AssociateListField object.
  */
  public DisplayAssociateField() {
    super();
  }

  /**
    Sets the associate data manager class.
    @param newAssociateDataManagerClass New associate data manager class.
  */
  public void setAssociateDataManagerClass(Class<? extends IDataManager<?, ?>> newAssociateDataManagerClass) {
  	associateDataManagerClass = newAssociateDataManagerClass;
  }

	/**
    Sets the associate display field ID indicating the value to display.
    @param newAssociateDisplayFieldId New associate display field ID.
  */
  public void setAssociateDisplayFieldId(String newAssociateDisplayFieldId) {
  	associateDisplayFieldId = newAssociateDisplayFieldId;
  }

  /**
    Writes an associate data object using an HTML text field and HTML button.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get field ID
    String fieldId = getFieldId();

    // Initialize hidden indicator
    boolean isHidden = isHidden(fieldId);

    try {
    	// Retrieve unformatted attribute ID
    	Object attributeId = getFieldValue();
    	
    	// Get current domain
    	IDomain domain = getCurrentDomain();
    	
    	// Retrieve associate
    	DataObject<Object> associate = findAssociate(attributeId, domain);
    	
    	// Write associate view
    	writeAssociateView(jspWriter, isHidden, associate);
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Display Associate Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Display Associate Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }
    
    return SKIP_BODY;
  }

  /**
    Ends tag processing by clearing associate lookup data.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    // Attempt to get enclosing parent table
    Table table = (Table)findAncestorWithClass(this, Table.class);
  
    if (table != null && table.isLastRow()) {
    	
    	// Clear lookup once table data is processed
    	associateLookups.clear();
    }

    return EVAL_PAGE;
  }

  /**
    Returns an associate data object using a specified associate ID.
    @param associateId Associate ID.
    @param domain Current domain.
    @return DataObject<Object> Associate data object.
  */
  private DataObject<Object> findAssociate(Object associateId, IDomain domain) throws UiException {
  	
    // Set method name
    String methodName = "findAssociates";

    // Initialize return value
    DataObject<Object> associate = null;
    
    if (associateId != null) {
    	
      // Check for parent table and table row
      Table table = (Table)findAncestorWithClass(this, Table.class);
      TableRow tableRow = (TableRow)findAncestorWithClass(this, TableRow.class);
      
      // Use a single query to retrieve associates supporting all table data
      if (table != null && tableRow != null) {
      	
      	// Get associate data manager class name
      	String associateManagerClassName = associateDataManagerClass.getName();
      	
      	// Attempt to get associate lookup based on data manager class name
      	Map<Object, DataObject<Object>> associateLookup = associateLookups.get(associateManagerClassName);
      	
      	if (associateLookup == null) {
      		
      		// Create associate lookup
      		associateLookup = new HashMap<Object, DataObject<Object>>();
      		
      		// Retrieve all data objects representing table data
      		List<DataObject<?>> dataObjects = table.getDataObjects();
      		
      		// Load associate lookup
      		loadAssociates(dataObjects, associateLookup, domain);

      		// Add associate lookup to associate lookups map
      		associateLookups.put(associateManagerClassName, associateLookup);
      	}
      
      	// Retrieve associated from lookup
      	associate = associateLookup.get(associateId);
      }
      else {
      	
        // Check for a registered factory
        IFactory dataManagerFactory = ConfigurationManager.getFactory(associateDataManagerClass);

        // Create data manager
        @SuppressWarnings("unchecked")
    		IDataManager<Object, DataObject<Object>> dataManager = (IDataManager<Object, DataObject<Object>>)dataManagerFactory.create();

        try {
          // Retrieve associate
          associate = dataManager.find(associateId, domain);
        }
        catch (DataSourceException dataSourceException) {
          
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.codes.tag.data");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Retrieving Associate for Display Associate Field", dataSourceException);

          throw new UiException(methodName + ":" + dataSourceException.getMessage());
        }
        finally {

          // Recycle data manager
        	dataManagerFactory.recycle(dataManager);
        }
      }
    }
    
    return associate;
  }
  
  /**
    Loads all associate data objects associated with a specified list of data objects into an associate lookup.
    @param dataObjects Data objects containing associates.
    @param associateLookup Associate lookup to populate.
    @param domain Current domain.
  */
  private void loadAssociates(List<DataObject<?>> dataObjects, Map<Object, DataObject<Object>> associateLookup, IDomain domain) throws UiException {
  	
    // Set method name
    String methodName = "loadAssociates";

    if (dataObjects != null && !dataObjects.isEmpty()) {
    	
    	// Initialize list of associate IDs
    	List<Object> associateIds = new ArrayList<Object>();
    	
    	for (DataObject<?> dataObject : dataObjects) {
    		
    		// Get associate ID from current data object
    		Object associateId = Introspector.getFieldValue(dataObject, getFieldId());
    		
    		// Add associate ID to list
    		associateIds.add(associateId);
    	}
    	
    	if (!associateIds.isEmpty()) {
    		
        // Check for a registered factory
        IFactory dataManagerFactory = ConfigurationManager.getFactory(associateDataManagerClass);

        // Create data manager
        @SuppressWarnings("unchecked")
    		IDataManager<Object, DataObject<Object>> dataManager = (IDataManager<Object, DataObject<Object>>)dataManagerFactory.create();

        try {
          // Retrieve associates
          List<DataObject<Object>> associates = dataManager.find(associateIds, domain);
          
          if (associates != null && !associates.isEmpty()) {
          	
          	for (DataObject<Object> associate : associates) {
          		
          		// Add associate to lookup
          		associateLookup.put(associate.getId(), associate);
          	}
          }
        }
        catch (DataSourceException dataSourceException) {
          
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.codes.tag.data");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Loading Associates for Display Associate Field", dataSourceException);

          throw new UiException(methodName + ":" + dataSourceException.getMessage());
        }
        finally {

          // Recycle data manager
        	dataManagerFactory.recycle(dataManager);
        }
    	}
    }
  }

  /**
    Writes a read only associate value.
    @param jspWriter JSP writer.
  	@param isHidden Hidden field indicator.
  	@param associate Associate data object.
  */
  private void writeAssociateView(JspWriter jspWriter, boolean isHidden, DataObject<Object> associate) throws IOException, JspException {
  	
      if (associate != null) {
      	
      	// Initialize associate field value
    		Object fieldValue = HIDDEN_VALUE;
  
    		if (!isHidden) {
    			
          // Initialize converter
          IConverter converter = null;
  
          // Get associate display field configuration
          Field associateDisplayField = ConfigurationManager.getField(associateDisplayFieldId);
  
          // Set converter
          if (associateDisplayField != null) {
            converter = associateDisplayField.getConverter();
          }
  
      		// Get associate field value
      		fieldValue = Introspector.getFieldValue(associate, associateDisplayFieldId);
      		
      		// Format field value if a converter is defined
          if (converter != null) {
          	fieldValue = converter.format(fieldValue, pageContext.getRequest().getLocale());
          }
  
          // Convert null field value to space
          fieldValue = StringUtil.convertNull(fieldValue, "&nbsp;");
    		}
  
        // Get attributes
        String attributes = buildAttributes().toString();
  
        if (StringUtil.isEmpty(attributes)) {
  
          // Write display field as plain text
          jspWriter.print(fieldValue);
        }
        else {
  
          // Write display field as HTML span
          jspWriter.print("<span" + attributes + ">" + fieldValue + "</span>");
        }
      }
  }
  
  /**
	  Cleans up resources each time tag is used.
	  @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
	*/
	public void doFinally() {
	
	  super.doFinally();
	
	  // Reset attributes
	  associateDisplayFieldId = null;
	  associateDataManagerClass = null;
	}
  
}
