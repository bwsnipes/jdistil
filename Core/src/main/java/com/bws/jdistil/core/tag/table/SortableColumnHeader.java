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
package com.bws.jdistil.core.tag.table;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.configuration.FieldValues;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.tag.basic.Component;
import com.bws.jdistil.core.tag.basic.Form;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Sortable column header used to sort table data using a sort field and sort direction.
  @author - Bryan Snipes
*/
public class SortableColumnHeader extends Component {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = 1482086846736683961L;

  /**
    Action ID.
  */
  private String actionId = null;

  /**
    Display field ID.
  */
  private String displayFieldId = null;
  
  /**
    Sort field ID.
  */
  private String sortFieldId = null;
  
  /**
    Sort direction ID.
  */
  private String sortDirectionId = null;
  
  /**
    Creates a new SortableColumnHeader object.
  */
  public SortableColumnHeader() {
    super();
  }

  /**
    Sets the submit action ID.
    @param actionId - Action ID.
  */
  public void setActionId(String actionId) {
    this.actionId = actionId;
  }

  /**
    Sets the display field ID.
    @param displayFieldId - Display field ID.
  */
  public void setDisplayFieldId(String displayFieldId) {
    this.displayFieldId = displayFieldId;
  }
  
  /**
    Sets the sort field ID.
    @param sortFieldId - Sort field ID.
  */
  public void setSortFieldId(String sortFieldId) {
    this.sortFieldId = sortFieldId;
  }
  
  /**
    Sets the sort direction ID.
    @param sortDirectionId - Sort direction ID.
  */
  public void setSortDirectionId(String sortDirectionId) {
    this.sortDirectionId = sortDirectionId;
  }
  
  /**
    Writes a sortable column header using an HTML link.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {
  
    // Set method name
    String methodName = "doStartTag";

    // Get current request
    ServletRequest request = pageContext.getRequest();

    // Initialize display field name
    String displayFieldName = "";
    
    // Get display field
    Field displayField = ConfigurationManager.getField(displayFieldId);

    // Get display field name
    if (displayField != null) {
      displayFieldName = displayField.getDescription(request.getLocale());
    }

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get attributes
    String attributes = buildAttributes().toString();

    try {
        // Write link as HTML anchor
        jspWriter.print("<a href=\"#\"" + attributes + ">");
        jspWriter.print(StringUtil.convertNull(displayFieldName));
        
        if (isSorted()) {
        	
          // Get sort direction
          String sortDirection = getSortDirection();
          
          // Get sort images
          String ascendingImageSource = ResourceUtil.getString(Constants.SORT_ASCENDING_IMAGE_SOURCE);
          String descendingImageSource = ResourceUtil.getString(Constants.SORT_DESCENDINGE_IMAGE_SOURCE);
          
          // Write sort direction image
          if (sortDirection.equals(FieldValues.SORT_ASCENDING) && !StringUtil.isEmpty(ascendingImageSource)) {
            jspWriter.print("<img src=\"" + ascendingImageSource + "\" />");
          }
          else if (sortDirection.equals(FieldValues.SORT_DESCENDING) && !StringUtil.isEmpty(descendingImageSource)) {
            jspWriter.print("<img src=\"" + descendingImageSource + "\" />");
          }
        }
        
        jspWriter.print("</a>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Sortable Column Header", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    
    return SKIP_BODY;
  }
  
  /**
    Returns a string buffer containing all attributes and their associated values.
    @see com.bws.jdistil.core.tag.basic.Component#buildAttributes
  */
  protected StringBuffer buildAttributes() throws JspException {

    // Get name of form component enclosing this component
    String formName = getFormName();
    
    // Retrieve sort direction
    String sortDirection = getSortDirection();
    
    // Initialize functions buffer
    StringBuffer functions = new StringBuffer();

    // Append sort field, sort direction and submit action
    functions.append("javascript:setValue('" + formName + "', '" + sortFieldId + "', '" + displayFieldId + "');");
    functions.append("javascript:setValue('" + formName + "', '" + sortDirectionId + "', '" + sortDirection + "');");
    functions.append("javascript:submitAction('" + formName + "', '" + actionId + "', '');return false;");

    // Set onClick attribute using functions
    setDynamicAttribute(null, "onClick", functions.toString());

    return super.buildAttributes();
  }

  /**
    Returns the name of the form component enclosing this component.
    @return String - Form name.
  */
  private String getFormName() {
  
    // Initialize method name
    String methodName = "getFormName";
    
    // Initialize form name
    String formName = "";
    
    // Check for enclosing form
    Form form = (Form)findAncestorWithClass(this, Form.class);
    
    try {
      
      // Attempt to get form name if an enclosing form is found
      if (form != null) {
        formName = StringUtil.convertNull(form.getDynamicAttribute("id"));
      }
    }
    catch (JspException jspException) {
      
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Form Name", jspException);
    }
    
    return formName.trim();
  }

  /**
    Returns a value indicating whether or not the column is sorted.
    @return String - Sorted indicator.
  */
  private boolean isSorted() {
    
    // Retrieve sort field
    String sortField = ParameterExtractor.getString(pageContext.getRequest(), sortFieldId);
  
    // Set sorted indicator
    boolean isSorted = !StringUtil.isEmpty(sortField) && displayFieldId.equals(sortField);
    
    return isSorted;
  }

	/**
    Returns the sort direction based on the currently selected sort field.
    @return String - Sort direction.
  */
  private String getSortDirection() {
    
    // Initialize sort direction
    String sortDirection = FieldValues.SORT_ASCENDING;
    
    // Retrieve sort field
    String sortField = ParameterExtractor.getString(pageContext.getRequest(), sortFieldId);

    if (displayFieldId.equals(sortField)) {

      // Get current request
      ServletRequest request = pageContext.getRequest();

      // Retrieve sort direction name
      String sortDirectionName = ParameterExtractor.getString(request, sortDirectionId);

      // Set toggled sort direction
      if (sortDirectionName.equals(FieldValues.SORT_ASCENDING)) {
        sortDirection = FieldValues.SORT_DESCENDING;
      }
    }
    
    return sortDirection;
  }
  
}
