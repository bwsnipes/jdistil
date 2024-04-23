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

import com.bws.jdistil.core.util.StringUtil;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
  Defines a binding between the properties of a target class and field IDs.
  @author - Bryan Snipes
*/
public class ObjectBinding implements Serializable {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 1088086582324938025L;

  /**
    Target class.
  */
  private Class<?> targetClass = null;

  /**
    Field bindings.
  */
  private Set<FieldBinding> fieldBindings = new HashSet<FieldBinding>();

  /**
    Field binding lookup.
  */
  private Map<String, FieldBinding> fieldBindingLookup = new HashMap<String, FieldBinding>();
  
  /**
    Creates a new ObjectBinding object.
    @param targetClass - Target class.
  */
  public ObjectBinding(Class<?> targetClass) {
    super();
    
    this.targetClass = targetClass;
  }

  /**
    Returns the target class.
    @return Class - Target class.
  */
  public Class<?> getTargetClass() {
    return targetClass;
  }

  /**
    Adds a field binding.
    @param fieldId - Field ID.
    @param propertyName - Property name.
  */
  public void addFieldBinding(String fieldId, String propertyName) {
    addFieldBinding(fieldId, propertyName, false);
  }
  
  /**
    Adds a field binding.
    @param fieldId - Field ID.
    @param propertyName - Property name.
    @param isCollection - Collection indicator.
  */
  public void addFieldBinding(String fieldId, String propertyName, boolean isCollection) {
    
    // Create field binding
    FieldBinding fieldBinding = new FieldBinding(fieldId, propertyName, isCollection);
    
    // Add field binding to list and lookup
    fieldBindings.add(fieldBinding);
    fieldBindingLookup.put(fieldBinding.getFieldId(), fieldBinding);
  }
  
  /**
    Returns the field bindings.
    @return Set - Set of field bindings.
  */
  public Set<FieldBinding> getFieldBindings() {
    return Collections.unmodifiableSet(fieldBindings);
  }

  /**
    Returns the field binding associated with a specified field ID.
    @param fieldId - Field ID.
    @return FieldBinding - Field binding.
  */
  public FieldBinding getFieldBinding(String fieldId) {
    
    // Initialize return value
    FieldBinding fieldBinding = null;
    
    if (!StringUtil.isEmpty(fieldId)) {
      fieldBinding = fieldBindingLookup.get(fieldId);
    }
    
    return fieldBinding;
  }
  
}
