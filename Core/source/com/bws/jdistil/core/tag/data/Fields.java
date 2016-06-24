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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Writes field values as HTML hidden fields. Fields to include are registered by other tags embedded in the body of this tag.
	@see com.bws.jdistil.core.tag.data.IncludeField
	@see com.bws.jdistil.core.tag.data.ExcludeField
	@see com.bws.jdistil.core.tag.data.IncludeActionFields
  @author Bryan Snipes
*/
public class Fields extends ValueComponent {

  /**
    Serial version UID.
  */
	private static final long serialVersionUID = 3803964175278764564L;

  /**
	  Set of field IDs to include.
	*/
	private Set<String> includeFieldIds = new HashSet<String>();

  /**
    Set of field IDs to exclude.
  */
  private Set<String> excludeFieldIds = new HashSet<String>();
  
	/**
    Creates a new Fields object.
  */
  public Fields() {
     super();
  }

	/**
	 * Registers a field ID to include in processing.  
	 * @param fieldId Field ID.
	 */
	protected void includeFieldId(String fieldId) {
		
		if (!StringUtil.isEmpty(fieldId)) {
			includeFieldIds.add(fieldId);
		}
	}
	
	/**
	 * Registers a field ID to exclude in processing.  
	 * @param fieldId Field ID.
	 */
	protected void excludeFieldId(String fieldId) {
		
		if (!StringUtil.isEmpty(fieldId)) {
			excludeFieldIds.add(fieldId);
		}
	}
	
  /**
	  Writes parameter values as HTML hidden fields.
	  @see javax.servlet.jsp.tagext.TagSupport#doEndTag
	*/
	public int doEndTag() throws JspException {
	  
    // Set method name
    String methodName = "doEndTag";
    
    if (!includeFieldIds.isEmpty()) {
    	
			// Get JSP writer
			JspWriter jspWriter = pageContext.getOut();
			
  		for (String fieldId : includeFieldIds) {
    		
    		if (!excludeFieldIds.contains(fieldId)) {
    			
					try {
						
			      if (isHidden(fieldId)) {

			      	// Mask field value if one exists
			      	String value = this.getFormattedFieldValue((String)null, fieldId) == null ? "" : HIDDEN_VALUE;
			      	
			        // Write hidden value
			        jspWriter.println("<input type=\"hidden\" name=\"" + fieldId + "\" value=\"" + value + "\" />");
			      }
			      else {
			      	
	      			// Get formatted field values
	      			Collection<String> fieldValues = getFormattedFieldValues(null, fieldId);
	      			
	      			if (fieldValues != null) {
	      				
	      				for (String fieldValue : fieldValues) {
	    					
	  							// Write hidden field
	  				      jspWriter.println("<input type=\"hidden\" name=\"" + fieldId + "\" value=\"" + fieldValue + "\" />");
	      				}
	      			}
			      }
			    }
			    catch (UiException uiException) {
			    	
			      // Post error message
			      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
			      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Field Values", uiException);
	
			      throw new JspException(methodName + ":" + uiException.getMessage());
			    }
			    catch (IOException ioException) {
	
			      // Post error message
			      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
			      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Field Values", ioException);
	
			      throw new JspException(methodName + ":" + ioException.getMessage());
			    }
    		}
    	}
    }
		
		return super.doEndTag();
	}
	
  /**
	  Cleans up resources each time tag is used.
	  @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
	*/
	public void doFinally() {
	
	  // Clear attributes
	  includeFieldIds.clear();
	  excludeFieldIds.clear();
	  
	  super.doFinally();
	}
	
}
