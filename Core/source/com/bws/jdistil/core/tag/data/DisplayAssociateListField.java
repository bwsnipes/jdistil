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
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.tag.table.Table;
import com.bws.jdistil.core.tag.table.TableRow;
import com.bws.jdistil.core.util.Introspector;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Component used to write a list of data objects associated with a target data object using an HTML select field.
  This component also provides a button for invoking an action capable of editing the data objects associated
  with the target data object.
  @author Bryan Snipes
*/
public class DisplayAssociateListField extends ValueComponent {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = 4923952825130865913L;

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
  public DisplayAssociateListField() {
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
    Writes list of associate data objects using an HTML select field and HTML button.
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
    	// Retrieve unformatted attribute IDs
    	Collection<Object> attributeIds = getFieldValues();
    	
    	// Retrieve associates
    	List<DataObject<Object>> associates = findAssociates(attributeIds);
    	
    	// Write associates view
    	writeAssociateView(jspWriter, isHidden, associates);
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Display Associate List Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Display Associate List Field", uiException);

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
    Returns a list of associate data objects using a specified list of associate IDs.
    @param associateIds List of associate IDs.
    @return List<DataObject<Object>> List of associate data objects.
  */
  private List<DataObject<Object>> findAssociates(Collection<Object> associateIds) throws UiException {
  	
    // Set method name
    String methodName = "findAssociates";

    // Initialize return value
    List<DataObject<Object>> associates = null;
    
    if (associateIds != null && !associateIds.isEmpty()) {
    	
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
      		loadAssociates(dataObjects, associateLookup);

      		// Add associate lookup to associate lookups map
      		associateLookups.put(associateManagerClassName, associateLookup);
      	}
      	
      	// Create associates list
      	associates = new ArrayList<DataObject<Object>>();
      	
      	for (Object associateId : associateIds) {
      		
        	// Retrieve associated from lookup
        	DataObject<Object> associate = associateLookup.get(associateId);
      		
        	if (associate != null) {
        		
        		// Add associate to associates list
        		associates.add(associate);
        	}
      	}
      }
      else {

        // Check for a registered factory
        IFactory dataManagerFactory = ConfigurationManager.getFactory(associateDataManagerClass);

        // Create data manager
        @SuppressWarnings("unchecked")
    		IDataManager<Object, DataObject<Object>> dataManager = (IDataManager<Object, DataObject<Object>>)dataManagerFactory.create();

        try {
        	// Create ID list
        	ArrayList<Object> ids = new ArrayList<Object>(associateIds.size());
        	ids.addAll(associateIds);
        	
          // Retrieve associates
          associates = (List<DataObject<Object>>)dataManager.find(ids);
        }
        catch (DataSourceException dataSourceException) {
          
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.codes.tag.data");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Retrieving Associates for Display Associate List Field", dataSourceException);

          throw new UiException(methodName + ":" + dataSourceException.getMessage());
        }
        finally {

          // Recycle data manager
          if (dataManagerFactory != null) {
          	dataManagerFactory.recycle(dataManager);
          }
        }
      }
    }
    
    return associates;
  }
  
  /**
    Loads all associate data objects associated with a specified list of data objects into an associate lookup.
    @param dataObjects Data objects containing associates.
    @param associateLookup Associate lookup to populate.
  */
  private void loadAssociates(List<DataObject<?>> dataObjects, Map<Object, DataObject<Object>> associateLookup) throws UiException {
  	
    // Set method name
    String methodName = "loadAssociates";

    if (dataObjects != null && !dataObjects.isEmpty()) {
    	
    	// Initialize list of associate IDs
    	List<Object> associateIds = new ArrayList<Object>();
    	
    	for (DataObject<?> dataObject : dataObjects) {
    		
    		// Get associate ID from current data object
    		Object fieldValue = Introspector.getFieldValue(dataObject, getFieldId());
    		
    		if (fieldValue != null && (fieldValue instanceof Collection)) {
    			
    			// Cast to collection
    			@SuppressWarnings("unchecked")
					Collection<Object> fieldValues = (Collection<Object>)fieldValue;
    		
      		// Add associate IDs to list
      		associateIds.addAll(fieldValues);
    		}
    	}
    	
    	if (!associateIds.isEmpty()) {
    		
        // Check for a registered factory
        IFactory dataManagerFactory = ConfigurationManager.getFactory(associateDataManagerClass);

        // Create data manager
        @SuppressWarnings("unchecked")
    		IDataManager<Object, DataObject<Object>> dataManager = (IDataManager<Object, DataObject<Object>>)dataManagerFactory.create();

        try {
          // Retrieve associates
          List<DataObject<Object>> associates = dataManager.find(associateIds);
          
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
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Loading Associate for Display Associate List Field", dataSourceException);

          throw new UiException(methodName + ":" + dataSourceException.getMessage());
        }
        finally {

          // Recycle data manager
          if (dataManagerFactory != null) {
          	dataManagerFactory.recycle(dataManager);
          }
        }
    	}
    }
  }

  /**
    Writes a read only list of associate values.
    @param jspWriter JSP writer.
  	@param isHidden Hidden field indicator.
  	@param associates List of associates.
  */
  private void writeAssociateView(JspWriter jspWriter, boolean isHidden, List<DataObject<Object>> associates) throws IOException, JspException {
  	
    if (associates != null && !associates.isEmpty()) {
    	
    	// Initialize associate field value
  		StringBuffer fieldValues = new StringBuffer();

  		if (isHidden) {
  			
  			fieldValues.append(HIDDEN_VALUE);
  		}
  		else {
  			
        // Initialize converter
        IConverter converter = null;

        // Get associate display field configuration
        Field associateDisplayField = ConfigurationManager.getField(associateDisplayFieldId);

        // Set converter
        if (associateDisplayField != null) {
          converter = associateDisplayField.getConverter();
        }

  			for (DataObject<Object> associate : associates) {
    		
      		// Get associate field value
      		Object fieldValue = Introspector.getFieldValue(associate, associateDisplayFieldId);
      		
      		// Format field value if a converter is defined
          if (converter != null) {
          	fieldValue = converter.format(fieldValue, pageContext.getRequest().getLocale());
          }
          
          // Convert null field value to space
          fieldValue = StringUtil.convertNull(fieldValue, "&nbsp;");

          // Append break if not the first item
          if (fieldValues.length() > 0) {
          	fieldValues.append("<br>");
          }
          
          // Append next field value
          fieldValues.append(fieldValue);
    		}
    	}

      // Get attributes
      String attributes = buildAttributes().toString();

      if (StringUtil.isEmpty(attributes)) {

        // Write display field as plain text
        jspWriter.print(fieldValues);
      }
      else {

        // Write display field as HTML span
        jspWriter.print("<span" + attributes + ">" + fieldValues + "</span>");
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
