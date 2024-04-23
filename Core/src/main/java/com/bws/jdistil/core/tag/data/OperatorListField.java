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

import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.tag.UiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.jsp.JspException;

/**
  Component used to write selectable operator values using an HTML select field.
  @author - Bryan Snipes
*/
public class OperatorListField extends ListField {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = -797976066863303849L;

	/**
	 * Text mode indicator. Text mode supports "like" condition operators for string based fields.
	 */
	private boolean isTextMode = false;
	
	/**
	 * Symbol usage indicator. Operator symbols are used instead of their text equivalents.
	 */
	private boolean isUseSymbols = false;
	
	/**
    Creates a new OperatorListField object.
  */
  public OperatorListField() {
    super();
  }

  /**
   * Sets the text mode indicator.
   * @param isTextMode Text mode indicator.
   */
  public void setIsTextMode(boolean isTextMode) {
		this.isTextMode = isTextMode;
	}

  /**
   * Sets the use symbols indicator.
   * @param isUseSymbols Use symbols indicator.
   */
	public void setIsUseSymbols(boolean isUseSymbols) {
		this.isUseSymbols = isUseSymbols;
	}
	
  /**
    Writes selectable data using an HTML select field.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {
	
  	setDynamicAttribute(null, "data-type", "operator");
  	
  	return super.doStartTag();
  }
  
	/**
   * Returns a list of list items objects containing operator values.
   * @return List - List of list item objects containing operator values.
   */
	protected List<IListItem> getItems() throws UiException {

    // Initialize return value
    List<IListItem> items = new ArrayList<IListItem>();

    if (isTextMode) {
    	
    	// Initialize descriptions using symbols
    	String beginsWithDescription = Operators.BEGINS_WITH;
    	String containsDescription = Operators.CONTAINS;
    	String endsWithDescription = Operators.ENDS_WITH;
    	
    	
    	if (!isUseSymbols) {
    		
    		// Get current locale
    		Locale locale = pageContext.getRequest().getLocale();
    		
    		// Retrieve locale specific descriptions
    		beginsWithDescription = Operators.getDescription(Operators.BEGINS_WITH, locale);
    		containsDescription = Operators.getDescription(Operators.CONTAINS, locale);
    		endsWithDescription = Operators.getDescription(Operators.ENDS_WITH, locale);
    	}
    	
      // Create list items using string representation of boolean values
      ListItem beginsWithItem = new ListItem(Operators.BEGINS_WITH, beginsWithDescription, Boolean.FALSE, Boolean.FALSE);
      ListItem containsItem = new ListItem(Operators.CONTAINS, containsDescription, Boolean.FALSE, Boolean.FALSE);
      ListItem endsWithItem = new ListItem(Operators.ENDS_WITH, endsWithDescription, Boolean.FALSE, Boolean.FALSE);
      
      // Add values to list items
      items.add(beginsWithItem);
      items.add(containsItem);
      items.add(endsWithItem);
    }
    else {
    	
    	// Initialize descriptions using symbols
    	String equalsDescription = Operators.EQUALS;
    	String notEqualsDescription = Operators.NOT_EQUALS;
    	String greaterThanDescription = Operators.GREATER_THAN;
    	String greaterThanEqualsDescription = Operators.GREATER_THAN_OR_EQUAL;
    	String lessThanDescription = Operators.LESS_THAN;
    	String lessThanEqualsDescription = Operators.LESS_THAN_OR_EQUAL;

    	if (!isUseSymbols) {
    		
    		// Get current locale
    		Locale locale = pageContext.getRequest().getLocale();
    		
    		// Retrieve locale specific descriptions
    		equalsDescription = Operators.getDescription(Operators.EQUALS, locale);
    		notEqualsDescription = Operators.getDescription(Operators.NOT_EQUALS, locale);
    		greaterThanDescription = Operators.getDescription(Operators.GREATER_THAN, locale);
    		greaterThanEqualsDescription = Operators.getDescription(Operators.GREATER_THAN_OR_EQUAL, locale);
    		lessThanDescription = Operators.getDescription(Operators.LESS_THAN, locale);
    		lessThanEqualsDescription = Operators.getDescription(Operators.LESS_THAN_OR_EQUAL, locale);
    	}

    	// Create list items using string representation of boolean values
      ListItem equalsItem = new ListItem(Operators.EQUALS, equalsDescription, Boolean.FALSE, Boolean.FALSE);
      ListItem notEqualsItem = new ListItem(Operators.NOT_EQUALS, notEqualsDescription, Boolean.FALSE, Boolean.FALSE);
      ListItem greaterThanItem = new ListItem(Operators.GREATER_THAN, greaterThanDescription, Boolean.FALSE, Boolean.FALSE);
      ListItem greaterThanEqualsItem = new ListItem(Operators.GREATER_THAN_OR_EQUAL, greaterThanEqualsDescription, Boolean.FALSE, Boolean.FALSE);
      ListItem lessThanItem = new ListItem(Operators.LESS_THAN, lessThanDescription, Boolean.FALSE, Boolean.FALSE);
      ListItem lessThanEqualsItem = new ListItem(Operators.LESS_THAN_OR_EQUAL, lessThanEqualsDescription, Boolean.FALSE, Boolean.FALSE);
      
      // Add values to list items
      items.add(equalsItem);
      items.add(notEqualsItem);
      items.add(greaterThanItem);
      items.add(greaterThanEqualsItem);
      items.add(lessThanItem);
      items.add(lessThanEqualsItem);
    }
    
    return items;
  }

}
