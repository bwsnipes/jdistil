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
  Defines a value criterion based on a field ID, operator, and field value.
*/
public class ValueCriterion {

  /**
    Field ID.
  */
  private String fieldId = null;

  /**
    Operator.
  */
  private String operator = null;

  /**
    Field value.
  */
  private Object fieldValue = null;

  /**
    Creates a new ValueCriterion object using a field ID, operator, and field value.
    @param fieldId - Field ID.
    @param operator - Operator.
    @param fieldValue - Field value.
  */
  protected ValueCriterion(String fieldId, String operator, Object fieldValue) {
    super();

    // Validate properties
    if (StringUtil.isEmpty(fieldId)) {
      throw new IllegalArgumentException("Invalid null field ID.");
    }
    if (StringUtil.isEmpty(operator)) {
      throw new IllegalArgumentException("Invalid null operator.");
    }
    if (fieldValue == null) {
      throw new IllegalArgumentException("Invalid null field value.");
    }

    // Set properties
    this.fieldId = fieldId;
    this.operator = operator;
    this.fieldValue = fieldValue;
  }

  /**
    Returns the field ID.
    @return String - Field ID.
  */
  public String getFieldId() {
    return fieldId;
  }

  /**
    Returns the operator.
    @return String - Operator.
  */
  public String getOperator() {
    return operator;
  }

  /**
    Returns the field value.
    @return Object - Field value.
  */
  public Object getFieldValue() {
    return fieldValue;
  }

}