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
package com.bws.jdistil.core.process.model;

import com.bws.jdistil.core.datasource.FilterCriteria;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.Introspector;
import com.bws.jdistil.core.util.StringUtil;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
  Creates a filter criteria object using parameter data contained in a given HTTP request
  and predefined value criterion definitions and order criteria definitions.
*/
public class FilterCriteriaDefinition {

  /**
    Data object class.
  */
  private Class<?> dataObjectClass = null;

	/**
    Indicates whether or not filter data is required.
  */
  private boolean isFilterDataRequired = false;
  
  /**
    List of value criterion definitions.
  */
  private List<ValueCriterionDefinition> valueCriterionDefinitions = null;

  /**
    Order criterion definition.
  */
  private OrderCriterionDefinition orderCriterionDefinition = null;
  
  /**
    Creates a new FilterCriteriaDefinition object.
  */
  public FilterCriteriaDefinition(Class<?> dataObjectClass) {
    super();
    
    // Validate required parameters
    if (dataObjectClass == null) {
      throw new IllegalArgumentException("Invalid null data object class.");
    }

    this.dataObjectClass = dataObjectClass;
  }

  /**
    Returns the required filter data indicator.
    @return boolean - Filter data required indicator.
  */
  public boolean getIsFilterDataRequired() {
    return isFilterDataRequired;
  }
  
  /**
    Sets the required filter data indicator.
    @param isFilterDataRequired - Filter data required indicator.
  */
  public void setIsFilterDataRequired(boolean isFilterDataRequired) {
    this.isFilterDataRequired = isFilterDataRequired;
  }
  
  /**
    Returns the list of value criterion definitions.
    @return List - List of value criterion definitions.
  */
  public List<ValueCriterionDefinition> getValueCriterionDefinitions() {
    return Collections.unmodifiableList(valueCriterionDefinitions);
  }
  
  /**
    Sets the list of value criterion definitions.
    @param valueCriterionDefinitions - List of value criterion definitions.
  */
  public void setValueCriterionDefinitions(List<ValueCriterionDefinition> valueCriterionDefinitions) {
    this.valueCriterionDefinitions = valueCriterionDefinitions;
  }
  
  /**
    Returns an order criterion definition.
    @return OrderCriterionDefinition - Order criterion definition.
  */
  public OrderCriterionDefinition getOrderCriterionDefinition() {
    return orderCriterionDefinition;
  }
  
  /**
    Sets the order criterion definition.
    @param orderCriterionDefinition - Order criterion definition.
  */
  public void setOrderCriterionDefinition(OrderCriterionDefinition orderCriterionDefinition) {
    this.orderCriterionDefinition = orderCriterionDefinition;
  }
  
  /**
    Creates and returns a filter criteria object using a given HTTP request.
    @param request - HTTP request.
    @return FilterCriteria - Filter criteria object.
  */
  public FilterCriteria create(HttpServletRequest request) {

    // Initialize return value
    FilterCriteria filterCriteria = new FilterCriteria();
    
    if (request != null) {
      
      if (valueCriterionDefinitions != null) {
        
        for (ValueCriterionDefinition valueCriterionDefinition : valueCriterionDefinitions) {

          // Get field IDs
          String targetFieldId = valueCriterionDefinition.getTargetFieldId();
          String operatorId = valueCriterionDefinition.getOperatorId();
          String fieldId = valueCriterionDefinition.getFieldId();
          
          // Get default operator and default field value
          String operator = valueCriterionDefinition.getDefaultOperator();
          Object fieldValue = valueCriterionDefinition.getDefaultFieldValue();
          
          // Get property name associated with field ID
          String propertyName = Introspector.getPropertyName(dataObjectClass, targetFieldId);
          
          // Set operator and field value using parameter value
          if (!StringUtil.isEmpty(operatorId)) {
            operator = ParameterExtractor.getString(request, operatorId);
          }
          if (!StringUtil.isEmpty(fieldId)) {
            fieldValue = ParameterExtractor.getObject(request, fieldId);
          }

          // Add value criterion if a value was specified
          if (fieldValue != null) {
            filterCriteria.addValueCriterion(propertyName, operator, fieldValue);
          }
        }
      }

      if (orderCriterionDefinition != null) {

        // Get reference field IDs
        String targetFieldId = orderCriterionDefinition.getTargetFieldId();
        String directionId = orderCriterionDefinition.getDirectionId();

        // Initialize field ID
        String fieldId = targetFieldId;
        
        // Set field ID using parameter value
        if (!StringUtil.isEmpty(targetFieldId)) {
          fieldId = ParameterExtractor.getString(request, targetFieldId);
        }
        
        // Get property name associated with field ID
        String propertyName = Introspector.getPropertyName(dataObjectClass, fieldId);
        
        // Initialize order direction using default value
        String direction = orderCriterionDefinition.getDefaultDirection();
        
        // Set direction using parameter value
        if (!StringUtil.isEmpty(directionId)) {
          direction = ParameterExtractor.getString(request, directionId);
        }
        
        // Add value criterion
        filterCriteria.addOrderCriterion(propertyName, direction);
      }
    }
    
    return filterCriteria;
  }
  
}