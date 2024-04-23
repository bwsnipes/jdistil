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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
  Component used to write selectable boolean values using an HTML select field.
  @author - Bryan Snipes
*/
public class BooleanListField extends ListField {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = 3150556386753822946L;

  /**
    Creates a new BooleanListField object.
  */
  public BooleanListField() {
    super();
  }

  /**
   * Returns a list of list items objects containing boolean values.
   * @return List - List of list item objects containing boolean values.
   */
	protected List<IListItem> getItems() throws UiException {

    // Initialize return value
    List<IListItem> items = new ArrayList<IListItem>();

    // Get field ID
    String fieldId = getFieldId();

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
    
    if (converter != null) {
      
      // Format boolean values using field converter
      String yes = converter.format(Boolean.TRUE, locale);
      String no = converter.format(Boolean.FALSE, locale);
      
      // Create list items using formatted boolean values
      ListItem yesItem = new ListItem(Boolean.TRUE, yes, Boolean.FALSE, Boolean.FALSE);
      ListItem noItem = new ListItem(Boolean.FALSE, no, Boolean.FALSE, Boolean.FALSE);
      
      // Add values to list items
      items.add(yesItem);
      items.add(noItem);
    }
    else {
    	
      // Create list items using string representation of boolean values
      ListItem yesItem = new ListItem(Boolean.TRUE, Boolean.TRUE.toString(), Boolean.FALSE, Boolean.FALSE);
      ListItem noItem = new ListItem(Boolean.FALSE, Boolean.FALSE.toString(), Boolean.FALSE, Boolean.FALSE);
      
      // Add values to list items
      items.add(yesItem);
      items.add(noItem);
    }
    
    return items;
  }

}
