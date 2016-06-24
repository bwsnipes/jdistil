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

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

/**
  Component used in the body of a Fields tag to specify a Field IDs to be excluded when writing field data.
  @author Bryan Snipes
*/
public class ExcludeField extends ValueComponent {

  /**
    Serial version UID.
  */
	private static final long serialVersionUID = 6737867672713990560L;

	/**
	  Field ID.
	*/
	private String fieldId = null;
	
  /**
	  Alias indicator.
	*/
	private boolean isAlias = false;
	
	/**
    Creates an ExcludeField object.
  */
  public ExcludeField() {
    super();
  }

  /**
    Sets the field ID.
    @param fieldId Field ID.
  */
  public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

  /**
	  Sets the alias indicator.
	  @param isAlias Alias indicator.
	*/
	public void setIsAlias(boolean isAlias) {
		this.isAlias = isAlias;
	}

	/**
    Registers a field ID to be excluded by an enclosing fields component.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Check for enclosing fields component
  	Fields fields = (Fields)findAncestorWithClass(this, Fields.class);

    if (fields != null) {

    	if (!isAlias) {
    		
        // Exclude non-aliased field ID
        fields.excludeFieldId(fieldId);
    	}
    	else {
    		
    		// Get servlet request
    		ServletRequest request = pageContext.getRequest();
    		
    		// Attempt to find actual field ID in request attributes 
    		Object attributeValue = request.getAttribute(fieldId);
    		
    		if (attributeValue != null) {

    			// Only string values can be excluded
    			if (attributeValue instanceof String) {
    				
      			// Exclude field ID
      			fields.excludeFieldId((String)attributeValue);
    			}
    		}
    		else {
    			
      		// Attempt to find actual field IDs in parameter values 
    			String[] parameterValues = request.getParameterValues(fieldId);
    			
    			if (parameterValues != null && parameterValues.length > 0) {
    				
    				for (String parameterValue : parameterValues) {
    					
        			// Exclude field ID
        			fields.excludeFieldId(parameterValue);
    				}
    			}
    		}
    	}
    }

    return SKIP_BODY;
  }

  /**
	  Cleans up resources each time tag is used.
	  @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
	*/
	public void doFinally() {
	
	  // Reset attributes
	  fieldId = null;
	  isAlias = false;

	  super.doFinally();
	}
  
}
