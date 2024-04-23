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
package com.bws.jdistil.core.configuration;

import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;
import com.bws.jdistil.core.validation.rules.IActionRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
  Class defining action configuration information.
  @author - Bryan Snipes
*/
public class Action {

  /**
    Unique ID.
  */
  private String id = null;

  /**
    Default description.
  */
  private String defaultDescription = null;

  /**
    Map of field IDs.
  */
  private List<String> fieldIds = new ArrayList<String>();
  
  /**
    Set of required field IDs.
  */
  private List<String> requiredFieldIds = new ArrayList<String>();
  
  /**
    List of default rules.
  */
  private List<IActionRule> defaultRules = new ArrayList<IActionRule>();
  
  /**
    List of processor factories.
  */
  private List<IFactory> processorFactories = new ArrayList<IFactory>();

  /**
    Creates a new action object using a given ID.
    @param id - Unique ID.
    @param defaultDescription - Default description.
  */
  public Action(String id, String defaultDescription) {
  
    super();

    // Validate required properties
    if (StringUtil.isEmpty(id)) {
      throw new IllegalArgumentException("Invalid null ID.");
    }

    // Set properties
    this.id = id;
    this.defaultDescription = defaultDescription;
  }
  
  /**
    Returns the unique action ID.
    @return String - Unique action ID.
  */
  public String getId() {
    return id;
  }

  /**
    Returns the description based on a given locale.
    @param locale - Locale.
    @return String - Description.
  */
  public String getDescription(Locale locale) {

    // Initialize return value
    String description = null;

    // Retrieve description resource names from core resource file
	  String descriptionResources = ResourceUtil.getString(Constants.DESCRIPTION_RESOURCES);
	
	  // Retrieve description
	  if (descriptionResources != null) {
	    description = ResourceUtil.getString(descriptionResources, locale, defaultDescription);
	  }
	
	  // Use default description if one is not found
	  if (description == null) {
	    description = defaultDescription;
	  }

    return description;
  }

  /**
    Adds a new field.
    @param fieldId - Field ID.
    @param isRequired - Required field indicator.
  */
  public void addField(String fieldId, boolean isRequired) {
  
    if (!StringUtil.isEmpty(id)) {
  
      // Add field ID to map for quick lookup
      fieldIds.add(fieldId);
      
      // Add to required field IDs
      if (isRequired) {
        requiredFieldIds.add(fieldId);
      }
    }
  }
  
  /**
    Returns a list of all field IDs.
    @return List - List of field IDs.
  */
  public List<String> getFieldIds() {
    return Collections.unmodifiableList(fieldIds);
  }
  
  /**
    Returns a value indicating whether or not a field with a given ID exists.
    @param fieldId - Field ID.
    @return boolean - Exists indicator.
  */
  public boolean hasField(String fieldId) {
    return fieldIds.contains(fieldId);
  }
  
  /**
    Returns a value indicating whether or not a field is required.
    @param fieldId - Field ID.
    @return boolean - Validation required indicator.
  */
  public boolean isFieldRequired(String fieldId) {
    return !StringUtil.isEmpty(fieldId) && requiredFieldIds.contains(fieldId);
  }
  
  /**
    Adds an action rule.
    @param actionRule - Action rule.
  */
  public void addRule(IActionRule actionRule) {
  
    // Add to rule list
    if (actionRule != null) {
      defaultRules.add(actionRule);
    }
  }
  
  /**
    Returns a list of rules based on a given locale.
    @param locale - Locale.
    @return List - List of action rule objects.
  */
  public List<IActionRule> getRules(Locale locale) {
  
  	return Collections.unmodifiableList(defaultRules);
  }
  
  /**
    Adds a processor factory.
    @param processorFactory - Processor factory.
  */
  public void addProcessorFactory(IFactory processorFactory) {
  
    // Add to factory list
    if (processorFactory != null) {
      processorFactories.add(processorFactory);
    }
  }
  
  /**
    Returns a list of processor factories.
    @return List - List of processor factories.
  */
  public List<IFactory> getProcessorFactories() {
    return Collections.unmodifiableList(processorFactories);
  }

  /**
   * Returns a hash code.
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Compares this breadcrumb with another instance for equality.
   * @see java.lang.Object#equals
   */
  public boolean equals(Object object) {
    
    // Initialize return value
    boolean isEqual = false;
    
    if (object != null && object instanceof Action) {
     
      // Cast to action
      Action action = (Action)object;
      
      // Compare action IDs
      isEqual = action.getId().equals(id);
    }
    
    return isEqual;
  }
  
}
