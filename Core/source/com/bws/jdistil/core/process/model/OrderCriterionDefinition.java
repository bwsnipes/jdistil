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

import com.bws.jdistil.core.util.StringUtil;

/**
  Defines an order criterion based on a field ID and order direction.
*/
public class OrderCriterionDefinition {

  /**
    Target field ID.
  */
  private String targetFieldId = null;

  /**
    Order direction field ID.
  */
  private String directionId = null;
  
  /**
    Default order direction.
  */
  private String defaultDirection = null;

  /**
    Creates a new OrderCriterionDefinition object using a field ID, 
    order direction field ID, and default order direction value.
    @param targetFieldId - Target field ID.
    @param directionId - Order direction field ID.
    @param defaultDirection - Default order direction.
  */
  public OrderCriterionDefinition(String targetFieldId, String directionId, String defaultDirection) {
    super();

    // Validate properties
    if (StringUtil.isEmpty(targetFieldId)) {
      throw new IllegalArgumentException("Invalid null target field ID.");
    }
    if (StringUtil.isEmpty(directionId) && StringUtil.isEmpty(defaultDirection)) {
      throw new IllegalArgumentException("Direction ID or default direction is required.");
    }
    
    // Set properties
    this.targetFieldId = targetFieldId;

    // Set order direction properties
    if (!StringUtil.isEmpty(directionId)) {
      this.directionId = directionId;
    }
    if (!StringUtil.isEmpty(defaultDirection)) {
      this.defaultDirection = defaultDirection;
    }
  }

  /**
    Returns the target field ID.
    @return String - Target field ID.
  */
  public String getTargetFieldId() {
    return targetFieldId;
  }
  
  /**
    Returns the order direction field ID.
    @return String - Order direction field ID.
  */
  public String getDirectionId() {
    return directionId;
  }

  /**
    Returns the default order direction.
    @return String - Default order direction.
  */
  public String getDefaultDirection() {
    return defaultDirection;
  }
  
}