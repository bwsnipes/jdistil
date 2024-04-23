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
    @param propertyName - Property name.
    @param operator - Operator.
    @param propertyValue - Property value.
  */
  public void addValueCriterion(String propertyName, String operator, Object propertyValue) {
  
    // Create and store search condition
    if (!StringUtil.isEmpty(propertyName) && !StringUtil.isEmpty(operator) && propertyValue != null) {
      valueCriteria.add(new ValueCriterion(propertyName, operator, propertyValue));
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
    @param propertyName - Property name.
  */
  public void addOrderCriterion(String propertyName) {
    addOrderCriterion(propertyName, null);
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
    @param propertyName - Property name.
    @param direction - Order direction.
  */
  public void addOrderCriterion(String propertyName, String direction) {
  
    // Create and store search condition
    if (!StringUtil.isEmpty(propertyName)) {
      orderCriteria.add(new OrderCriterion(propertyName, direction));
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