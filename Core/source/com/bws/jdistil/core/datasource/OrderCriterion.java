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
  Defines an order criterion based on a property name and order direction.
*/
public class OrderCriterion {

  /**
    Property name.
  */
  private String propertyName = null;

  /**
    Order direction.
  */
  private String direction = null;

  /**
    Creates a new OrderCriterion object using a property name and order direction.
    @param propertyName - Property name.
    @param direction - Order direction.
  */
  protected OrderCriterion(String propertyName, String direction) {
    super();

    // Validate properties
    if (StringUtil.isEmpty(propertyName)) {
      throw new IllegalArgumentException("Invalid null property name.");
    }

    // Set properties
    this.propertyName = propertyName;

    // Set direction if specified
    if (!StringUtil.isEmpty(direction)) {
      this.direction = direction;
    }
  }

  /**
    Returns the property name.
    @return String - Property name.
  */
  public String getPropertyName() {
    return propertyName;
  }

  /**
    Returns the order direction.
    @return String - Order direction.
  */
  public String getDirection() {
    return direction;
  }

}