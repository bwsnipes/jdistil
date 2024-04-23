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

/**
  Defines a value criterion based on a property name, operator, and property value.
*/
public class ValueCriterion {

  /**
    Property name.
  */
  private String propertyName = null;

  /**
    Operator.
  */
  private String operator = null;

  /**
    Property value.
  */
  private Object propertyValue = null;

  /**
    Creates a new ValueCriterion object using a property name, operator, and property value.
    @param propertyName - Property name.
    @param operator - Operator.
    @param propertyValue - Property value.
  */
  protected ValueCriterion(String propertyName, String operator, Object propertyValue) {
    super();

    // Validate properties
    if (StringUtil.isEmpty(propertyName)) {
      throw new IllegalArgumentException("Invalid null property name.");
    }
    if (StringUtil.isEmpty(operator)) {
      throw new IllegalArgumentException("Invalid null operator.");
    }
    if (propertyValue == null) {
      throw new IllegalArgumentException("Invalid null property value.");
    }

    // Set properties
    this.propertyName = propertyName;
    this.operator = operator;
    this.propertyValue = propertyValue;
  }

  /**
    Returns the property name.
    @return String - Property name.
  */
  public String getPropertyName() {
    return propertyName;
  }

  /**
    Returns the operator.
    @return String - Operator.
  */
  public String getOperator() {
    return operator;
  }

  /**
    Returns the property value.
    @return Object - Property value.
  */
  public Object getPropertyValue() {
    return propertyValue;
  }

}