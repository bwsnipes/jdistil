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
import com.bws.jdistil.core.tag.basic.Form;
import com.bws.jdistil.core.util.Descriptions;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Component used to write selectable data using a combination of HTML select field.
  @author Bryan Snipes
*/
public class MultipleListField extends ValueComponent {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -3514086930425879145L;

  /**
	 	Label height in EM scale.
	*/
	private double emLabelHeight = .625;
	
	/**
		List height in EM scale.
	*/
	private double emListHeight = 10;
	
  /**
   	Display headers indicator.
  */
  private boolean isHeaderDisplayed = true;
  
  /**
    Items attribute name.
  */
  private String itemsAttributeName = null;

  /**
    List filter.
  */
  private ListFilter<IListItem> listFilter = null;
  
  /**
    List of groups defined by nested group tags.
  */
  private List<Group> groups = new ArrayList<Group>();
  
  /**
    Creates a new MultipleListField object.
  */
  public MultipleListField() {
    super();
  }

	/**
	 	Sets the label height in EM scale.
	 	@param newEmLabelHeight New label height in EM scale.
	*/
  public void newEmLabelHeight(double newEmLabelHeight) {
		emLabelHeight = newEmLabelHeight;
	}

	/**
	 	Sets the list height in EM scale.
	 	@param newEmListHeight New list height in EM scale.
	*/
	public void setEmListHeight(double newEmListHeight) {
		emListHeight = newEmListHeight;
	}

	/**
   	Sets the display headers indicator.
   	@param newIsHeaderDisplayed New display headers indicator.
  */
  public void setIsHeaderDisplayed(boolean newIsHeaderDisplayed) {
  	isHeaderDisplayed = newIsHeaderDisplayed;
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
    Registers information from a nested group tag.
    @param fieldId Field ID.
    @param isReadOnly Read only indicator.
    @param isHidden Hidden indicator.
    @param values Collection of values.
  */
  protected void registerGroup(String fieldId, boolean isReadOnly, boolean isHidden, Collection<Object> values) {
  
    // Validate field ID
    if (StringUtil.isEmpty(fieldId)) {
      throw new IllegalArgumentException("Field ID is required.");
    }

    // Add group
    groups.add(new Group(fieldId, isReadOnly, isHidden, values));
  }
  
  /**
    Writes selectable data using multiple HTML select fields.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Write component during end tag after nested sub-list tags have registered

    return EVAL_BODY_INCLUDE;
  }

  /**
    Writes selectable data using multiple HTML select fields.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doEndTag() throws JspException {
    
    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    try {
      
      if (groups.isEmpty()) {
        
        // Write non-breakable space
        jspWriter.print("&nbsp;");
      }
      else {
        
        // Get current locale
        Locale locale = pageContext.getRequest().getLocale();
        
        // Write group values as hidden fields
        writeGroupValues(jspWriter, locale);
        
        // Get list items
        List<IListItem> items = getItems();
  
        // Get unselected items
        List<IListItem> unselectedItems = filterSelectedItems(items);
  
        // Calculate total spanned rows
        int totalSpannedRows = groups.size();
        
        // Get component sizes
        double labelHeight = emLabelHeight;
        double groupHeight = emListHeight;
        
        // Calculate source list height
        double sourceHeight = ((labelHeight + groupHeight) * totalSpannedRows) - labelHeight;
        
        // Get first group
        Group group = groups.get(0);
        
        // Get form ID
        String formId = getFormId();
        
        // Set disabled attribute
        String disabledAttribute = group.getIsReadOnly() || group.getIsHidden() ? "disabled=\"true\"" : "";
        
        // Set source field ID
        String sourceFieldId = "source_" + getFieldId();
        
        // Set destination field ID
        String destinationFieldId = "destination_" + group.getFieldId();
        
        // Write start of component table
        jspWriter.println("<table>");
        jspWriter.println("<tr>");
        
        // Write source label and select list
        if (totalSpannedRows == 1) {
          jspWriter.println("<td>");
        }
        else {
          jspWriter.println("<td rowspan=\"" + totalSpannedRows + "\">");
        }
        
        if (isHeaderDisplayed) {
          jspWriter.println("<label style=\"height:" + labelHeight + "em\">" + getFieldDescription(getFieldId()) + "</label><br/>");
        }

        jspWriter.println("<select id=\"" + sourceFieldId + "\" class=\"multipleSelectList\" style=\"height:" + sourceHeight + "em\" multiple>");
  
        if (unselectedItems != null) {
          
          // Get field value converter for source
          IConverter converter = getConverter(getFieldId());
          
          // Write source list items
          for (IListItem item : unselectedItems) {
            
            if (!item.getIsDeleted() && (listFilter == null || !listFilter.isFiltered(item))) {
              
              // Get value and description
              Object value = item.getValue();
              String description = item.getDescription();
              
              // Format value if converter is defined
              if (converter != null) {
                value = converter.format(value, locale);
              }

              // Attempt to translate descriptions to a locale specific descriptions
              description = Descriptions.getDescription(description, locale);

              // Write option value
              jspWriter.println("<option value=\"" + value + "\">" + description + "</option>");
            }
          }
        }
        
        jspWriter.println("</select>");
        jspWriter.println("</td>");
  
        // Write move buttons for first group's select list
        jspWriter.println("<td>");
        jspWriter.println("<input type=\"button\" name=\"button\" value=\"&gt;&gt;\" style=\"width:100%\" onClick=\"javascript:addItems('" + formId + "', '" + group.getFieldId() + "', '" + sourceFieldId + "', '" + destinationFieldId + "', true);\" " + disabledAttribute + "/><br/>");
        jspWriter.println("<input type=\"button\" name=\"button\" value=\"&gt;\" style=\"width:100%\" onClick=\"javascript:addItems('" + formId + "', '" + group.getFieldId() + "', '" + sourceFieldId + "', '" + destinationFieldId + "', false);\" " + disabledAttribute + "/><br/>");
        jspWriter.println("<input type=\"button\" name=\"button\" value=\"&lt;\" style=\"width:100%\" onClick=\"javascript:removeItems('" + formId + "', '" + group.getFieldId() + "', '" + destinationFieldId + "', '" + sourceFieldId + "', false);\" " + disabledAttribute + "/><br/>");
        jspWriter.println("<input type=\"button\" name=\"button\" value=\"&lt;&lt;\" style=\"width:100%\" onClick=\"javascript:removeItems('" + formId + "', '" + group.getFieldId() + "', '" + destinationFieldId + "', '" + sourceFieldId + "', true);\" " + disabledAttribute + "/>");
        jspWriter.println("</td>");
        
        // Write first group's label and select list
        jspWriter.println("<td valign=\"top\">");

        if (isHeaderDisplayed) {
          jspWriter.println("<label style=\"height:" + labelHeight + "em\">" + getFieldDescription(group.getFieldId()) + "</label><br/>");
        }

        // Get group values
      	Collection<Object> values = group.getValues();
        
      	// Write start of select field
      	jspWriter.println("<select id=\"" + destinationFieldId + "\" class=\"multipleSelectList\" style=\"height:" + groupHeight + "em\" multiple>");
          
        if (items != null && values != null && !values.isEmpty()) {

          // Get field value converter for group
          IConverter converter = getConverter(group.getFieldId());
          
          // Write first group's list items
          for (IListItem item : items) {

            // Get value
            Object value = item.getValue();
            
            if (values.contains(value)) {
              
              // Write option value if not filtered
              if (listFilter == null || !listFilter.isFiltered(item)) {

              	if (group.getIsHidden()) {
              		
              		// Write masked field value
                  jspWriter.println("<option value=\"" + HIDDEN_VALUE + "\">" + HIDDEN_VALUE + "</option>");
                  
                  break;
              	}
              	else {
              		
                  // Format value if converter is defined
                  if (converter != null) {
                    value = converter.format(value, locale);
                  }

                  // Get description
                  String description = item.getDescription();
                  
                  // Attempt to translate descriptions to a locale specific descriptions
                  description = Descriptions.getDescription(description, locale);

              		// Write field value
                	jspWriter.println("<option value=\"" + value + "\">" + description + "</option>");
              	}
              }
            }
          }
        }
        
        jspWriter.println("</select>");
        jspWriter.println("</td>");
        jspWriter.println("</tr>");
        
        if (groups.size() > 1) {
          
          for (int index = 1; index < groups.size(); index++) {
            
            // Get next group
            group = groups.get(index);
            
            // Set disabled attribute
            disabledAttribute = group.getIsReadOnly() || group.getIsHidden() ? "disabled=\"true\"" : "";
            
            // Set destination field ID
            destinationFieldId = "destination_" + group.getFieldId();
            
            // Write move buttons for group's select list
            jspWriter.println("<tr>");
            jspWriter.println("<td>");
            jspWriter.println("<input type=\"button\" name=\"button\" value=\"&gt;&gt;\" style=\"width:100%\" onClick=\"javascript:addItems('" + formId + "', '" + group.getFieldId() + "', '" + sourceFieldId + "', '" + destinationFieldId + "', true);\" " + disabledAttribute + "/><br/>");
            jspWriter.println("<input type=\"button\" name=\"button\" value=\"&gt;\" style=\"width:100%\" onClick=\"javascript:addItems('" + formId + "', '" + group.getFieldId() + "', '" + sourceFieldId + "', '" + destinationFieldId + "', false);\" " + disabledAttribute + "/><br/>");
            jspWriter.println("<input type=\"button\" name=\"button\" value=\"&lt;\" style=\"width:100%\" onClick=\"javascript:removeItems('" + formId + "', '" + group.getFieldId() + "', '" + destinationFieldId + "', '" + sourceFieldId + "', false);\" " + disabledAttribute + "/><br/>");
            jspWriter.println("<input type=\"button\" name=\"button\" value=\"&lt;&lt;\" style=\"width:100%\" onClick=\"javascript:removeItems('" + formId + "', '" + group.getFieldId() + "', '" + destinationFieldId + "', '" + sourceFieldId + "', true);\" " + disabledAttribute + "/>");
            jspWriter.println("</td>");
            
            // Write next group's label and select list
            jspWriter.println("<td valign=\"top\">");
            
            if (isHeaderDisplayed) {
            	jspWriter.println("<label style=\"height:" + labelHeight + "em\">" + getFieldDescription(group.getFieldId()) + "</label><br/>");
            }
            
            // Get group values
          	values = group.getValues();
            
          	// Write start of select field
            jspWriter.println("<select id=\"" + destinationFieldId + "\" class=\"multipleSelectList\" style=\"height:" + groupHeight + "em\" multiple>");
            
            if (items != null && values != null && !values.isEmpty()) {
          
              // Get field value converter for group
              IConverter converter = getConverter(group.getFieldId());
              
              // Write first group's list items
              for (IListItem item : items) {

                // Get value
                Object value = item.getValue();
                
                if (values.contains(value)) {
                  
                  // Write option value if not filtered
                  if (listFilter == null || !listFilter.isFiltered(item)) {

                  	if (group.getIsHidden()) {
                  		
                  		// Write masked field value
                      jspWriter.println("<option value=\"" + HIDDEN_VALUE + "\">" + HIDDEN_VALUE + "</option>");
                      
                      break;
                  	}
                  	else {
                  		
                      // Format value if converter is defined
                      if (converter != null) {
                        value = converter.format(value, locale);
                      }

                      // Get description
                      String description = item.getDescription();
                      
                      // Attempt to translate descriptions to a locale specific descriptions
                      description = Descriptions.getDescription(description, locale);

                  		// Write field value
                    	jspWriter.println("<option value=\"" + value + "\">" + description + "</option>");
                  	}
                  }
                }
              }
            }
              
            jspWriter.println("</select>");
            jspWriter.println("</td>");
            jspWriter.println("</tr>");
          }
        }
  
        // Write end of component table
        jspWriter.println("</table>");
      }
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Multiple List Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Multiple List Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }
    finally {

      // Clear groups
      groups.clear();
    }

    return EVAL_PAGE;
  }
  
  /**
	  Cleans up resources each time tag is used.
	  @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
	*/
	public void doFinally() {
	
	  super.doFinally();
	
	  // Reset attributes
	  isHeaderDisplayed = true;
	  itemsAttributeName = null;
	  listFilter = null;
	  groups.clear();
	}
  
  /**
    Returns a list of list items objects.
    @return List List of list item objects.
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

  /**
    Filters a list of items based on all currently selected items.
    @param items List of items.
    @return List List of selected items.
  */
  private List<IListItem> filterSelectedItems(List<IListItem> items) {
  
    // Initialize selected values
    List<Object> selectedValues = new ArrayList<Object>();
    
    for (Group group : groups) {
      
    	if (!group.isHidden) {
    		
        // Get group values
        Collection<Object> values = group.getValues();
        
        // Add group values to selected values
        if (values != null) {
          selectedValues.addAll(values);
        }
    	}
    }

    // Initialize filtered items
    List<IListItem> filteredItems = new ArrayList<IListItem>();
    
    if (items != null && !items.isEmpty()) {
      
      for (IListItem item : items) {
        
        // Add to filtered items if not a filtered item or selected item
        if (!selectedValues.contains(item.getValue())) {
          filteredItems.add(item);
        }
      }
    }
    
    return filteredItems;
  }
  
  /**
    Writes hidden registered group values as hidden fields.
    @param jspWriter JSP writer.
    @param locale Locale.
    @throws java.io.IOException
  */
  private void writeGroupValues(JspWriter jspWriter, Locale locale) throws IOException {
    
    if (groups != null && !groups.isEmpty()) {
      
      for (Group group : groups) {
        
        // Get group values
        Collection<Object> values = group.getValues();

        if (values != null && !values.isEmpty()) {
        	
          if (group.getIsHidden()) {
        		
            // Write hidden value
            jspWriter.println("<input type=\"hidden\" name=\"" + group.getFieldId() + "\" value=\"" + HIDDEN_VALUE + "\" />");
        	}
        	else {
        		
            // Get field value converter for group
            IConverter converter = getConverter(group.getFieldId());
          
            for (Object value : values) {

              // Format value if converter is defined
              if (converter != null) {
                value = converter.format(value, locale);
              }

              // Write hidden value
              jspWriter.println("<input type=\"hidden\" name=\"" + group.getFieldId() + "\" value=\"" + value + "\" />");
            }
        	}
        }
      }
    }
  }
  
  /**
    Returns the ID of the form this component is nested within.
    @return String Form ID.
    @throws JspException
  */
  private String getFormId() throws JspException {
    
    // Initialize form ID
    String formId = "";
    
    // Check for enclosing form
    Form form = (Form)findAncestorWithClass(this, Form.class);
    
    // Attempt to get form ID if an enclosing form is found
    if (form != null) {
      formId = StringUtil.convertNull(form.getDynamicAttribute("id"));
    }
    
    // Trim form ID
    formId = formId.trim();
    
    return formId;
  }
  
  /**
    Returns the description associated with a specified field ID.
   * @param fieldId Field ID.
   * @return String Field description.
   */
  private String getFieldDescription(String fieldId) {
    
    // Initialize return value
    String description = fieldId;
    
    // Get field configuration
    Field field = ConfigurationManager.getField(fieldId);

    // Set value
    if (field != null) {
      description = field.getDescription(pageContext.getRequest().getLocale());
    }

    return description;
  }
  
  /**
    Returns the converter for a specified field.
    @param fieldId Field ID.
    @return IConverter Field value converter.
  */
  private IConverter getConverter(String fieldId) {
    
    // Initialize converter
    IConverter converter = null;

    // Get field configuration
    Field field = ConfigurationManager.getField(fieldId);

    // Set converter
    if (field != null) {
      converter = field.getConverter();
    }
    
    return converter;
  }
  
  
/**
  Inner class used to store information registered by nested group tags.
*/
private class Group {

  /**
    Field ID.
  */
  private String fieldId = null;
  
  /**
    Read only indicator.
  */
  private boolean isReadOnly = false;
  
  /**
    Hidden indicator.
  */
  private boolean isHidden = false;
  
  /**
    Collection of values.
  */
  private Collection<Object> values = null;
  
  /**
    Creates a new Group object.
    @param fieldId Field ID.
    @param isReadOnly Read only indicator.
    @param isHidden Hidden indicator.
    @param values List of values.
  */
  public Group(String fieldId, boolean isReadOnly, boolean isHidden, Collection<Object> values) {
    super();
    
    // Set properties
    this.fieldId = fieldId;
    this.isReadOnly = isReadOnly;
    this.isHidden = isHidden;
    this.values = values;
  }
  
  /**
    Returns the field ID.
    @return String Field ID.
  */
  public String getFieldId() {
    return fieldId;
  }
  
  /**
    Returns the read only indicator.
    @return boolean Read only indicator.
  */
  public boolean getIsReadOnly() {
    return isReadOnly;
  }
  
  /**
    Returns the hidden indicator.
    @return boolean Hidden indicator.
  */
  public boolean getIsHidden() {
		return isHidden;
	}

	/**
    Returns a collection of values.
    @return Collection<Object> Collection of values.
  */
  public Collection<Object> getValues() {
    return values;
  }
}

}
