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
  Component used to display a read-only value from a list data.
  @author Bryan Snipes
*/
public class DisplayListField extends ValueComponent {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 8754223422477181872L;

  /**
   * Items attribute name.
   */
  private String itemsAttributeName = null;

  /**
   * Creates a new ListField object.
   */
  public DisplayListField() {
    super();
  }

  /**
   * Sets the items attribute name.
   * @param newItemsAttributeName New items attribute name.
   */
  public void setItemsAttributeName(String newItemsAttributeName) {
    itemsAttributeName = newItemsAttributeName;
  }

  /**
   * Writes read-only list data.
   * @see javax.servlet.jsp.tagext.Tag#doStartTag
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

      if (isHidden) {
        
      	// Mask field value if one exists
      	String value = getFieldValue() == null ? "" : HIDDEN_VALUE;

      	// Write hidden value
        jspWriter.print(value);
      }
      else {
        
        // Get list items
        List<IListItem> items = getItems();

        // Write items
        if (items != null) {

          // Get field value
          Object fieldValue = getFieldValue();

        	// Get current locale
          Locale locale = pageContext.getRequest().getLocale();

          // Initialize value buffer
          StringBuffer values = new StringBuffer();
          
          for (IListItem item : items) {

            // Get item value and description
            Object value = item.getValue();
            String description = item.getDescription();
            
            // Initialize selected indicator
            boolean isSelected = false;

            if (fieldValue != null) {

              // Set selected indicator
            	isSelected = fieldValue.equals(value);
            }

            if (isSelected) {

              // Attempt to translate descriptions to a locale specific descriptions
              description = Descriptions.getDescription(description, locale);

              // Append description
              values.append(description);
              
              break;
            }
          }

          // Get attributes
          String attributes = buildAttributes().toString();

          if (StringUtil.isEmpty(attributes)) {

            // Write display field as plain text
            jspWriter.print(values.toString());
          }
          else {

            // Write display field as HTML span
            jspWriter.print("<span" + attributes + ">" + values.toString() + "</span>");
          }
        }
      }
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building List Display Field", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }
    catch (UiException uiException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building List Display Field", uiException);

      throw new JspException(methodName + ":" + uiException.getMessage());
    }

    return SKIP_BODY;
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
