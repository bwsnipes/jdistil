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
  Defines a value criterion definition based on a target field ID, 
  operator field ID, default operatorvalue, field ID, and default field value.
*/
public class ValueCriterionDefinition {

  /**
    Target field ID identifying the field used to build filter criteria.
  */
  private String targetFieldId = null;

  /**
    Operator field ID.
  */
  private String operatorId = null;
  
  /**
    Default operator.
  */
  private String defaultOperator = null;

  /**
    Field ID identifying the field used to retrieve a field value from a submitted request.
  */
  private String fieldId = null;
  
  /**
    Default field value.
  */
  private Object defaultFieldValue = null;

  /**
    Creates a new ValueCriterionDefinition object using a target field ID, 
    operator field ID, default operatorvalue, field ID, and default field value.
    @param targetFieldId - Target field ID.
    @param operatorId - Operator field ID.
    @param defaultOperator - Default operator.
    @param fieldId - Field ID.
    @param defaultFieldValue - Default field value.
  */
  public ValueCriterionDefinition(String targetFieldId, String operatorId, 
      String defaultOperator, String fieldId, Object defaultFieldValue) {
    
    super();

    // Validate properties
    if (StringUtil.isEmpty(targetFieldId)) {
      throw new IllegalArgumentException("Invalid null target field ID.");
    }
    if (StringUtil.isEmpty(operatorId) && StringUtil.isEmpty(defaultOperator)) {
      throw new IllegalArgumentException("Operator ID or default operator is required.");
    }
    if (StringUtil.isEmpty(fieldId) && defaultFieldValue == null) {
      throw new IllegalArgumentException("Field ID or field value is required.");
    }

    // Set required properties
    this.targetFieldId = targetFieldId;
    this.defaultFieldValue = defaultFieldValue;
    
    // Set optional properties
    if (!StringUtil.isEmpty(operatorId)) {
      this.operatorId = operatorId;
    }
    if (!StringUtil.isEmpty(defaultOperator)) {
      this.defaultOperator = defaultOperator;
    }
    if (!StringUtil.isEmpty(fieldId)) {
      this.fieldId = fieldId;
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
    Returns the operator ID.
    @return String - Operator ID.
  */
  public String getOperatorId() {
    return operatorId;
  }
  
  /**
    Returns the default operator.
    @return String - Default operator.
  */
  public String getDefaultOperator() {
    return defaultOperator;
  }

  /**
    Returns the field ID.
    @return String - Field ID.
  */
  public String getFieldId() {
    return fieldId;
  }
  
  /**
    Returns the default field value.
    @return Object - Default field value.
  */
  public Object getDefaultFieldValue() {
    return defaultFieldValue;
  }

}