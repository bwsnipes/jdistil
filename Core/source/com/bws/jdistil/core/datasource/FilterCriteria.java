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
package com.bws.jdistil.core.datasource;

import com.bws.jdistil.core.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
  Stores value criterion and order criterion that can be used to retrieve data
  using a data manager.
*/
public class FilterCriteria {

  /**
    List of value criterion.
  */
  private List<ValueCriterion> valueCriteria = new ArrayList<ValueCriterion>();

  /**
    List of order criterion.
  */
  private List<OrderCriterion> orderCriteria = new ArrayList<OrderCriterion>();
  
  /**
    Creates a new FilterCriteria object.
  */
  public FilterCriteria() {
    super();
  }

  /**
    Adds a new value criterion.
    @param fieldId - Field ID.
    @param operator - Operator.
    @param fieldValue - Field value.
  */
  public void addValueCriterion(String fieldId, String operator, Object fieldValue) {
  
    // Create and store search condition
    if (!StringUtil.isEmpty(fieldId) && !StringUtil.isEmpty(operator) && fieldValue != null) {
      valueCriteria.add(new ValueCriterion(fieldId, operator, fieldValue));
    }
  }
  
  /**
    Returns a value indicating whether or not value criteria exists.
    @return boolean - Value criterion indicator.
  */
  public boolean hasValueCriteria() {
    return !valueCriteria.isEmpty();
  }
  
  /**
    Returns a list of all value criterion.
    @return List - List of value criterion.
  */
  public List<ValueCriterion> getValueCriteria() {
    return Collections.unmodifiableList(valueCriteria);
  }
  
  /**
    Adds a new order criterion using the default ascending order direction.
    @param fieldId - Field ID.
  */
  public void addOrderCriterion(String fieldId) {
    addOrderCriterion(fieldId, null);
  }
  
  /**
    Returns a value indicating whether or not order criteria exists.
    @return boolean - Order criterion indicator.
  */
  public boolean hasOrderCriteria() {
    return !orderCriteria.isEmpty();
  }

  /**
    Adds a new order criterion.
    @param fieldId - Field ID.
    @param direction - Order direction.
  */
  public void addOrderCriterion(String fieldId, String direction) {
  
    // Create and store search condition
    if (!StringUtil.isEmpty(fieldId)) {
      orderCriteria.add(new OrderCriterion(fieldId, direction));
    }
  }
  
  /**
    Returns a list of all order criterion.
    @return List - List of order criterion.
  */
  public List<OrderCriterion> getOrderCriteria() {
    return Collections.unmodifiableList(orderCriteria);
  }
  
}