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
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.util.Descriptions;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Component used to write selectable data using an HTML select field.
  @author Bryan Snipes
*/
public class ListField extends ValueComponent {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 3644090964041560651L;

  /**
    Instruction.
  */
  protected String instruction = null;

  /**
    Items attribute name.
  */
  protected String itemsAttributeName = null;

  /**
    List filter.
  */
  private ListFilter<IListItem> listFilter = null;
  
  /**
    Creates a new ListField object.
  */
  public ListField() {
    super();
  }

  /**
    Sets the instruction to add to displayed items.
    @param newInstruction New instruction.
  */
  public void setInstruction(String newInstruction) {
    instruction = newInstruction;
  }

  /**
    Sets the items attribute name.
    @param newItemsAttributeName New items attribute name.
  */
  public void setItemsAttributeName(String newItemsAttributeName) {
    itemsAttributeName = newItemsAttributeName;
  }

  /**
    Sets the list filter.
    @param newListFilter New list filter.
  */
  public void setListFilter(ListFilter<IListItem> newListFilter) {
    listFilter = newListFilter;
  }
  
  /**
    Writes selectable data using an HTML select field.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get field ID
    String fieldId = getFieldId();

    // Initialize hidden and read only indicators
    boolean isHidden = isHidden(fieldId);
    boolean isReadOnly = isReadOnly(fieldId);

    if (isReadOnly || isHidden) {

    	// Write restricted view
    	writeRestrictedView(fieldId, isReadOnly, isHidden, jspWriter);
    }
    else {
    	
    	// Write accessible view
    	writeAccessibleView(fieldId, isReadOnly, isHidden, jspWriter);
    }

    return SKIP_BODY;
  }

  private void writeAccessibleView(String fieldId, boolean isReadOnly, boolean isHidden, JspWriter jspWriter) throws JspException {
  	
  	// Set method name
  	String methodName = "writeAccessibleView";
  	
    // Get attributes
    String attributes = buildAttributes().toString();

    try {
    	
      // Write start of HTML select
      jspWriter.println("<select" + attributes + ">");
  
      // Write instruction if one is specified
      if (instruction == null) {
        jspWriter.println("<option value=\"\"></option>");
      }
      else {
        jspWriter.println("<option value=\"\">" + instruction + "</option>");
      }
  
      // Get list items
      List<IListItem> items = getItems();

      // Write items
      if (items != null) {
  
        // Get field value
        Object fieldValue = getFieldValue();
  
        // Get field configuration
        Field field = ConfigurationManager.getField(fieldId);
  
        // Initialize converter
        IConverter converter = null;
  
        // Set converter
        if (field != null) {
          converter = field.getConverter();
        }
  
      	// Get current locale
        Locale locale = pageContext.getRequest().getLocale();

        for (IListItem item : items) {
  
          // Filter out items if filter is specified
          if (listFilter == null || !listFilter.isFiltered(item)) {
            
            // Get item value and description
            Object value = item.getValue();
            String description = item.getDescription();
            Boolean isDefault = item.getIsDefault();
            Boolean isDeleted = item.getIsDeleted();
            
            // Initialize selected indicator
            boolean isSelected = false;
  
            if (fieldValue != null) {
  
              // Set selected indicator
              isSelected = fieldValue.equals(value);
            }
            else if (isDefault != null) {
            	
              // Set selected indicator using default value
              isSelected = isDefault;
            }
  
            // Only display selected or non-deleted items
            if (isSelected || isDeleted == null || !isDeleted.booleanValue()) {
              
              // Format value if a converter was defined
              if (converter != null) {
                value = converter.format(value, pageContext.getRequest().getLocale());
              }
  
              // Attempt to translate descriptions to a locale specific descriptions
              description = Descriptions.getDescription(description, locale);
              
              // Set value and selected attributes
              String valueAttribute = " value=\"" + value + "\" ";
              String selectedAttribute = isSelected ? "selected" : "";
  
              // Write option tag
              jspWriter.println("<option" + valueAttribute + selectedAttribute + ">" + description + "</option>");
            }
          }
        }
      }
  
      // Write end of HTML select
      jspWriter.println("</select>");
    }
    catch (IOException ioException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building List Field", ioException);
  
      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {
  
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building List Field", uiException);
  
      throw new JspException(methodName + ":" + uiException.getMessage());
    }
  }
  
  private void writeRestrictedView(String fieldId, boolean isReadOnly, boolean isHidden, JspWriter jspWriter) throws JspException {

  	// Set method name
  	String methodName = "writeRestrictedView";
  	
    try {

      // Get list items
      List<IListItem> items = getItems();

      if (items != null) {
      	
        // Get field value
        Object fieldValue = getFieldValue();

        // Get field configuration
        Field field = ConfigurationManager.getField(fieldId);

        // Initialize converter
        IConverter converter = null;

        // Set converter
        if (field != null) {
          converter = field.getConverter();
        }

        for (IListItem item : items) {

          // Filter out items if filter is specified
          if (listFilter == null || !listFilter.isFiltered(item)) {
            
            // Get item value and description
            Object value = item.getValue();
            String description = item.getDescription();
            Boolean isDefault = item.getIsDefault();
            
            // Initialize selected indicator
            boolean isSelected = false;

            if (fieldValue != null) {

              // Set selected indicator
              isSelected = fieldValue.equals(value);
            }
            else if (isDefault != null) {
            	
              // Set selected indicator using default value
              isSelected = isDefault;
            }

            if (isSelected) {
              
              // Format value if a converter was defined
              if (converter != null) {
                value = converter.format(value, pageContext.getRequest().getLocale());
              }

            	// Mask field value and description if a field value exists
              if (isHidden && value != null) {
                value = HIDDEN_VALUE;
              	description =  HIDDEN_VALUE;
              }

              // Set value and selected attributes
              String idAttribute = "id=\"" + fieldId + "\" ";
              String descriptionAttribute = "value=\"" + description + "\" ";
              String readOnlyAttribute = "readonly=\"readonly\" ";

              // Write display component as HTML text field
              jspWriter.println("<input type=\"text\" " + idAttribute + descriptionAttribute + readOnlyAttribute + " />");

              // Set value and selected attributes
              String nameAttribute = " name=\"" + fieldId + "\" ";
              String valueAttribute = " value=\"" + value + "\" ";

              // Write value component as HTML hidden field
              jspWriter.println("<input type=\"hidden\" " + nameAttribute + valueAttribute + " />");
              
              break;
            }
          }
        }
      }
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building List Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building List Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }
  }
  
  /**
	  Cleans up resources each time tag is used.
	  @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
	*/
	public void doFinally() {
	
	  super.doFinally();
	
	  // Reset attributes
	  instruction = null;
	  itemsAttributeName = null;
	  listFilter = null;
	}
  
  /**
    Returns a string buffer containing all attributes and their associated values.
    @see com.bws.jdistil.core.tag.basic.Component#buildAttributes
  */
  protected StringBuffer buildAttributes() throws JspException {

    // Get field ID
    String fieldId = StringUtil.convertNull(getInstanceFieldId());

    // Append field ID
    setDynamicAttribute(null, "id", fieldId);
    setDynamicAttribute(null, "name", fieldId);

    return super.buildAttributes();
  }

  /**
   * Returns a list of list items objects.
   * @return List List of list item objects.
   */
  @SuppressWarnings("unchecked")
	protected List<IListItem> getItems() throws UiException, JspException {

    // Initialize return value
    List<IListItem> items = null;

    // Retrieve values
    if (itemsAttributeName != null) {
      items = (List<IListItem>)pageContext.getRequest().getAttribute(itemsAttributeName);
    }

    return items;
  }

}
