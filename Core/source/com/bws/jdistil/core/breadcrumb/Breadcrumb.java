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
package com.bws.jdistil.core.breadcrumb;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.util.StringUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;

public class Breadcrumb {

  /**
   * Associated page.
   */
  private Page page = null;
  
  /**
   * Associated action.
   */
  private Action action = null;
  
  /**
   * Breadcrumb description.
   */
  private String description = null;
  
  /**
   * Associated parameters.
   */
  private Set<Parameter> parameters = new HashSet<Parameter>();
  
  /**
   * Creates a new breadcrumb using a page, action, and description.
   * @param page Page.
   * @param action Action.
   * @param description Description.
   */
  public Breadcrumb(Page page, Action action, String description) {
    super();
    
    // Validate input parameters
    if (page == null) {
      throw new IllegalArgumentException("Page is required.");
    }
    if (action == null) {
      throw new IllegalArgumentException("Action is required.");
    }
    if (description == null || StringUtil.isEmpty(description)) {
      throw new IllegalArgumentException("Description is required.");
    }
    
    // Set attributes
    this.page = page;
    this.action = action;
    this.description = description;
  }
  
  /**
   * Updates breadcrumb information using an action and HTTP servlet request.
   * @param request HTTP servlet request.
   */
  public void update(ServletRequest request) {
    
    if (request != null) {

    	// Clear existing parameters
    	parameters.clear();
    	
      for (String fieldId : action.getFieldIds()) {

        // Get field values from submitted request parameters
        String[] fieldValues = request.getParameterValues(fieldId);
        
        if (fieldValues != null) {

          // Create and add parameter to collection
        	parameters.add(new Parameter(fieldId, fieldValues));
        }
      }
    }
  }
  
  /**
   * Returns the associated page.
   * @return Page Associated page.
   */
  public Page getPage() {
    return page;
  }
 
  /**
   * Returns the associated action.
   * @return Action Associated action.
   */
  public Action getAction() {
    return action;
  }
 
  /**
   * Returns the associated description.
   * @return String Associated action.
   */
  public String getDescription() {
    return description;
  }
 
  /**
   * Returns the associated parameters
   * @return Collection Collection of associated parameters.
   */
  public Set<Parameter> getParameters() {
    return Collections.unmodifiableSet(parameters);
  }
  
  /**
   * Returns a hash code.
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return page.hashCode();
  }

  /**
   * Compares this breadcrumb with another instance for equality.
   * @see java.lang.Object#equals
   */
  public boolean equals(Object object) {
    
    // Initialize return value
    boolean isEqual = false;
    
    if (object != null && object instanceof Breadcrumb) {
     
      // Cast to breadcrumb
      Breadcrumb breadcrumb = (Breadcrumb)object;
      
      // Compare page and action
      isEqual = breadcrumb.getPage().equals(page);
    }
    
    return isEqual;
  }
  
}
