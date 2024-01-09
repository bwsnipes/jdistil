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

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.conversion.IConverter;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.tag.basic.Component;
import com.bws.jdistil.core.tag.table.Table;
import com.bws.jdistil.core.tag.table.TableRow;
import com.bws.jdistil.core.util.Introspector;
import com.bws.jdistil.core.util.StringUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

/**
  Abstract base component containing attributes common to all value UI components.
  @author - Bryan Snipes
*/
public abstract class ValueComponent extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -6417641342046119782L;

  /**
   * Hidden value text.
   */
  protected static final String HIDDEN_VALUE = "*********";
  
  /**
    Attribute name.
  */
  private String attributeName = null;

  /**
    Field ID.
  */
  private String fieldId = null;

  /**
    Field value.
  */
  private Object fieldValue = null;
  
  /**
    Default field value.
  */
  private Object defaultValue = null;

  /**
	  Explicit field value indicator.
	*/
  private boolean isExplicitFieldValue = false;
  
  /**
    Creates a new ValueComponent object.
  */
  public ValueComponent() {
    super();
  }

  /**
	  Cleans up resources each time tag is used.
	  @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
	*/
	public void doFinally() {
	
	  super.doFinally();
	  
		// Reset attributes
		attributeName = null;
		fieldId = null;
		fieldValue = null;
		defaultValue = null;
		isExplicitFieldValue = false;
	}
  
  /**
    Sets the attribute name.
    @param attributeName - Attribute name.
  */
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  /**
    Sets the field ID.
    @param fieldId - Field ID.
  */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }
  
  /**
    Directly sets the field value instead of retrieving a field value 
    from a target data object, request attributes, or request parameters.
    @param fieldValue - Field value.
  */
  public void setFieldValue(Object fieldValue) {
    this.fieldValue = fieldValue;
    isExplicitFieldValue = true;
  }
  
  /**
    Sets the default field value.
    @param defaultValue - Default field value.
  */
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
  
  /**
	  Returns a value indicating whether or not the specified field is hidden.
	  @param fieldId Field ID.
	  @return boolean Hidden field indicator.
	*/
  protected boolean isHidden(String fieldId) throws JspException {
  	
    // Set method name
    String methodName = "isHidden";

    // Initialize return value
    boolean isHidden = false;
    
    // Get session
    HttpSession session = pageContext.getSession();

    // Get security manager
    ISecurityManager securityManager = getSecurityManager();
    
    if (securityManager != null) {

      try {
        // Set hidden indicator
        isHidden = securityManager.isFieldHidden(fieldId, session);
      }
      catch (SecurityException securityException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Checking Hidden Field", securityException);

        throw new JspException(methodName + ":" + securityException.getMessage());
      }
    }
    
    return isHidden;
  }
  
  /**
	  Returns a value indicating whether or not the specified field is read only.
	  @param fieldId Field ID.
	  @return boolean Read only field indicator.
	*/
	protected boolean isReadOnly(String fieldId) throws JspException {
		
	  // Set method name
	  String methodName = "isReadOnly";
	
	  // Initialize return value
	  boolean isReadOnly = false;
	  
	  // Get session
	  HttpSession session = pageContext.getSession();
	
	  // Get security manager
	  ISecurityManager securityManager = getSecurityManager();
	  
	  if (securityManager != null) {
	
	    try {
	      // Set read only indicator
	    	isReadOnly = securityManager.isFieldReadOnly(fieldId, session);
	    }
	    catch (SecurityException securityException) {
	
	      // Post error message
	      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
	      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Checking Read Only Field", securityException);
	
	      throw new JspException(methodName + ":" + securityException.getMessage());
	    }
	  }
	  
	  return isReadOnly;
	}
	
  /**
    Returns the component's field value or values using an attribute name and field ID.
    The attribute name is used to retrieve a field accessible object from the
    request attributes and the field ID is used to retrieve the field's 
    associated property value. The multiple value indicator ensures a collection of values
    is returned when data is retrieved from the request parameters. This property has no 
    impact when data is retrieved from a field accessible object's associated property value.
    @param attributeName - Attribute name. 
    @param fieldId - Field ID.
    @param isMultiple Multiple value indicator.
    @return Object - Field value.
  */
  protected Object getFieldValue(String attributeName, String fieldId, boolean isMultiple) {

    // Initialize return value
    Object fieldValue = this.fieldValue;

    if (!isExplicitFieldValue) {
      
      if (fieldId != null) {

        // Get request
        ServletRequest request = pageContext.getRequest();

        // Check for parent table and table row
        Table table = (Table)findAncestorWithClass(this, Table.class);
        TableRow tableRow = (TableRow)findAncestorWithClass(this, TableRow.class);
        
        // Avoid using data object for table headers and footers
        if (table != null && tableRow != null) {

          // Use current data object of parent table
          DataObject<?> dataObject = table.getCurrentValue();

          // Get field value
          if (dataObject != null) {
            fieldValue = Introspector.getFieldValue(dataObject, fieldId);
          }
        }
        else {

          // Initialize data object
          DataObject<?> dataObject = null;

          if (attributeName != null) {

            // Retrieve data object from request attributes
            dataObject = (DataObject<?>)request.getAttribute(attributeName);

            if (dataObject != null) {

              // Get field value from data object
              fieldValue = Introspector.getFieldValue(dataObject, fieldId);
            }
          }
          
          if (fieldValue == null) {

            // Attempt to get field value from request attributes
            fieldValue = request.getAttribute(fieldId);
          }

          if (fieldValue == null) {
          	
          	if (!isMultiple) {
          		
              // Get field value from request parameters
              fieldValue = ParameterExtractor.getObject(request, fieldId);
          	}
          	else {
          		
              // Get field values from request parameters
          		fieldValue = ParameterExtractor.getObjects(request, fieldId);
          	}
          }

          if (fieldValue == null) {
          	
          	if (!isMultiple) {
          	
              // Set value using submitted parameter value
              fieldValue = request.getParameter(fieldId);
          	}
          	else {
          		
              // Get submitted parameter values
              String[] parameterValues = request.getParameterValues(fieldId);
              
              if (parameterValues != null) {
              	
              	// Create list of values
              	ArrayList<String> values = new ArrayList<String>();
              	
              	// Populate formatted values with submitted parameter values
              	for (String parameterValue : parameterValues) {
              		values.add(parameterValue);
              	}
              	
              	// Assign list of values as the field value
              	fieldValue = values;
            	}
          	}
          }
        }
      }

      // Use default value if specified and no field value is defined
      if (fieldValue == null && defaultValue != null) {
        fieldValue = defaultValue;
      }
    }
    
    return fieldValue;
  }

  /**
    Returns the component's field value as a formatted string using an attribute
    name and field ID. The attribute name is used to retrieve a field accessible
    object from the request attributes and the field ID is used to retrieve
    the field's associated property value.
    @param attributeName - Attribute name.
    @param fieldId - Field ID.
    @return String - Formatted field value.
    @throws com.bws.jdistil.core.tag.UiException
  */
  protected String getFormattedFieldValue(String attributeName, String fieldId) throws UiException {

    // Initialize return value
    String formattedFieldValue = null;

    // Get field value
    Object fieldValue = getFieldValue(attributeName, fieldId, false);

    if (fieldValue != null) {

      // Get field
      Field field = ConfigurationManager.getField(fieldId);

      // Get formatted field value
      formattedFieldValue = getFormattedFieldValue(field, fieldValue);
    }
    
    return formattedFieldValue;
  }

  /**
	  Returns the component's field value as a formatted string using an attribute
	  name and field ID. The attribute name is used to retrieve a field accessible
	  object from the request attributes and the field ID is used to retrieve
	  the field's associated property value.
	  @param attributeName - Attribute name.
	  @param fieldId - Field ID.
	  @return String - Formatted field value.
	  @throws com.bws.jdistil.core.tag.UiException
	*/
	protected Collection<String> getFormattedFieldValues(String attributeName, String fieldId) throws UiException {
	
	  // Initialize return value
	  Collection<String> formattedFieldValues = null;
	
	  // Get field value
	  Object fieldValue = getFieldValue(attributeName, fieldId, true);
	  
	  if (fieldValue != null && (fieldValue instanceof Collection)) {
	  	
	  	@SuppressWarnings("unchecked")
			Collection<Object> fieldValues = (Collection<Object>)fieldValue;
	  	
	  	if (!fieldValues.isEmpty()) {
	  		
        // Get field
        Field field = ConfigurationManager.getField(fieldId);

	  		// Create formatted values
	  		formattedFieldValues = new ArrayList<String>(fieldValues.size());
	  		
	  		for (Object value : fieldValues) {
	  			
	  			// Initialize formatted field value
	  			String formattedFieldValue = getFormattedFieldValue(field, value);

	  			// Add formatted field value to list
	  			if (formattedFieldValue != null) {
	  				formattedFieldValues.add(formattedFieldValue);
	  			}
	  		}
	  	}
	  }
	  
	  return formattedFieldValues;
	}
	
	/**
	 * Returns a formatted field value using a given field and value. 
	 * @param field Field definition.
	 * @param value Value to format.
	 * @return String Formatted field value.
	 */
	protected String getFormattedFieldValue(Field field, Object value) {

		// Initialize formatted field value
		String formattedValue = null;

		if (value != null) {
			
	    if (field == null) {

	      // Convert to string
	      formattedValue = value.toString();
	    }
	    else {
	    	
	      // Get current request
	      ServletRequest request = pageContext.getRequest();

	      // Get field converter
	      IConverter converter = field.getConverter();

	      if (converter == null) {

	        // Use string representation
	        formattedValue = value.toString();
	      }
	      else {
	        
	      	try {
	      		
		        // Attempt to format
		        formattedValue = converter.format(value, request.getLocale());
	      	}
	      	catch (Exception exception) {
	      		
	  	      // Post error message
	  	      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
	  	      logger.logp(Level.INFO, getClass().getName(), "getFormattedFieldValue", "Formatting Field Value", exception);
	      	}
	        
	        // Use string value if unable to format
	        if (StringUtil.isEmpty(formattedValue)) {
	          formattedValue = value.toString();
	        }
	      }
	    }
		}
    
    return formattedValue;
	}
	
  /**
	  Returns the attribute name.
	  @return String - Attribute name.
	*/
	public String getAttributeName() {
	  return attributeName;
	}
	
	/**
    Returns the field ID associated with this component.
    @return String - Field ID.
  */
  public String getFieldId() {
    return fieldId;
  }

  /**
    Returns an instance specific field ID associated with this component.
    @return String - Field ID.
  */
  protected String getInstanceFieldId() {

    // Initialize field ID
    String instanceFieldId = fieldId;

    // Check for parent table
    Table table = (Table)findAncestorWithClass(this, Table.class);

    if (table != null) {

      // Set instance based on table row if component is enclosed inside a table
      int instance = table.getCurrentRow();

      // Build instance specific name
      instanceFieldId = fieldId + "_" + String.valueOf(instance);
    }

    return instanceFieldId;
  }

  /**
    Returns the field value associated with this component.
    @return Object - Field value.
  */
  protected Object getFieldValue() {
    return getFieldValue(attributeName, fieldId, false);
  }

  /**
	  Returns the field values associated with this component.
	  @return Collection - Field values.
	*/
	@SuppressWarnings("unchecked")
	protected Collection<Object> getFieldValues() {
	  
		// Initialize return value
		Collection<Object> fieldValues = null;
		
		// Get field value
		Object fieldValue = getFieldValue(attributeName, fieldId, true);
		
		if (fieldValue != null && (fieldValue instanceof Collection)) {
			
			// Cast to collection
			fieldValues = (Collection<Object>)fieldValue;
		}
		
		return fieldValues;
	}
	
  /**
    Returns the field value associated with this component as a formatted string.
    @return String - Formatted field value.
    @throws com.bws.jdistil.core.tag.UiException
  */
  public String getFormattedFieldValue() throws UiException {
    return getFormattedFieldValue(attributeName, fieldId);
  }

  /**
	  Returns the field values associated with this component as formatted strings.
	  @return Collection - Formatted field values.
	  @throws com.bws.jdistil.core.tag.UiException
	*/
	public Collection<String> getFormattedFieldValues() throws UiException {
	  return getFormattedFieldValues(attributeName, fieldId);
	}
	
}
