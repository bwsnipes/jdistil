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

import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;

/**
  Component used in the body of Fields tag to include all field IDs associated with a specified action.
  @author Bryan Snipes
*/
public class IncludeActionFields extends ValueComponent {

  /**
    Serial version UID.
  */
	private static final long serialVersionUID = -3124218983575360765L;

	/**
	  Action ID.
	*/
	private String actionId = null;
	
  /**
    Alias indicator.
  */
  private boolean isAlias = false;
  
	/**
    Creates an IncludeActionFields object.
  */
  public IncludeActionFields() {
    super();
  }

  /**
    Sets the action ID.
    @param actionId Action ID.
  */
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

  /**
    Sets the alias indicator.
    @param isAlias Alias indicator.
  */
  public void setIsAlias(boolean isAlias) {
  	this.isAlias = isAlias;
  }
  
	/**
    Registers all field IDs bound to a given action to be included by an enclosing fields component.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Check for enclosing fields component
  	Fields fields = (Fields)findAncestorWithClass(this, Fields.class);

    if (fields != null) {

    	if (!isAlias) {
    		
        // Include field IDs for non-aliased action
    		includeActionFields(fields, actionId);
    	}
    	else {
    		
    		// Get servlet request
    		ServletRequest request = pageContext.getRequest();
    		
    		// Attempt to find actual action ID in request attributes 
    		Object attributeValue = request.getAttribute(actionId);
    		
    		if (attributeValue != null) {

    			// Only string values can be used to include action fields
    			if (attributeValue instanceof String) {
    				
      			// Include action field IDs
    				includeActionFields(fields, (String)attributeValue);
    			}
    		}
    		else {
    			
      		// Attempt to find actual field IDs in parameter values 
    			String[] parameterValues = request.getParameterValues(actionId);
    			
    			if (parameterValues != null && parameterValues.length > 0) {
    				
    				for (String parameterValue : parameterValues) {
    					
        			// Include action field IDs
      				includeActionFields(fields, parameterValue);
    				}
    			}
    		}
    	}
    	
    }

    return SKIP_BODY;
  }

	/**
    Includes all field IDs bound to a given action to be included by an enclosing fields component.
    @param fields Fields component.
    @param actionId Action ID.
  */
  private void includeActionFields(Fields fields, String actionId) {
  	
  	// Get parent binding
		Action action = ConfigurationManager.getAction(actionId);
		
		if (action != null) {
			
			// Get field IDs associated with the action
			List<String> fieldIds = action.getFieldIds();
			
			if (fieldIds != null) {
				
				for (String fieldId : fieldIds) {

					// Include field ID
					fields.includeFieldId(fieldId);
				}
			}
		}
  }
}
