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
  Defines an order criterion based on a field ID and order direction.
*/
public class OrderCriterion {

  /**
    Field ID.
  */
  private String fieldId = null;

  /**
    Order direction.
  */
  private String direction = null;

  /**
    Creates a new OrderCriterion object using a field ID and order direction.
    @param fieldId - Field ID.
    @param direction - Order direction.
  */
  protected OrderCriterion(String fieldId, String direction) {
    super();

    // Validate properties
    if (StringUtil.isEmpty(fieldId)) {
      throw new IllegalArgumentException("Invalid null field ID.");
    }

    // Set properties
    this.fieldId = fieldId;

    // Set direction if specified
    if (!StringUtil.isEmpty(direction)) {
      this.direction = direction;
    }
  }

  /**
    Returns the field ID.
    @return String - Field ID.
  */
  public String getFieldId() {
    return fieldId;
  }

  /**
    Returns the order direction.
    @return String - Order direction.
  */
  public String getDirection() {
    return direction;
  }

}