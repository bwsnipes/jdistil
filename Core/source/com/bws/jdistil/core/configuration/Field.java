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

import com.bws.jdistil.core.conversion.IConverter;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;
import com.bws.jdistil.core.validation.rules.IFieldRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
  Class defining field configuration information.
  @author - Bryan Snipes
*/
public class Field {

  /**
    String field type constant.
  */
  public static final Integer STRING = Integer.valueOf(0);

  /**
    Number field type constant.
  */
  public static final Integer NUMBER = Integer.valueOf(1);

  /**
    Byte field type constant.
  */
  public static final Integer BYTE = Integer.valueOf(2);

  /**
    Short field type constant.
  */
  public static final Integer SHORT = Integer.valueOf(3);

  /**
    Integer field type constant.
  */
  public static final Integer INTEGER = Integer.valueOf(4);

  /**
    Long field type constant.
  */
  public static final Integer LONG = Integer.valueOf(5);

  /**
    Float field type constant.
  */
  public static final Integer FLOAT = Integer.valueOf(6);

  /**
    Double field type constant.
  */
  public static final Integer DOUBLE = Integer.valueOf(7);

  /**
    Date field type constant.
  */
  public static final Integer DATE = Integer.valueOf(8);

  /**
    Time field type constant.
  */
  public static final Integer TIME = Integer.valueOf(9);

  /**
    Date/time field type constant.
  */
  public static final Integer DATE_TIME = Integer.valueOf(10);

  /**
    Boolean field type constant.
  */
  public static final Integer BOOLEAN = Integer.valueOf(11);

  /**
    Custom class constant.
  */
  public static final Integer CUSTOM_CLASS = Integer.valueOf(12);

  /**
    Unique ID.
  */
  private String id = null;

  /**
    Data type.
  */
  private Integer type = null;

  /**
    Default description.
  */
  private String defaultDescription = null;

  /**
    Data converter.
  */
  private IConverter converter = null;

  /**
    List of default rules.
  */
  private List<IFieldRule> defaultRules = new ArrayList<IFieldRule>();

  /**
    Creates a new field object using a given name, description and converter.
    @param id - Unique ID.
    @param type - Data type.
    @param defaultDescription - Default description.
    @param converter - Data converter.
  */
  public Field(String id, Integer type, String defaultDescription, IConverter converter) {
    super();

    // Validate required properties
    if (StringUtil.isEmpty(id)) {
      throw new IllegalArgumentException("Invalid null ID.");
    }
    if (type == null) {
      throw new IllegalArgumentException("Invalid null type.");
    }

    // Set properties
    this.id = id;
    this.type = type;
    this.defaultDescription = defaultDescription;
    this.converter = converter;
  }

  /**
    Adds a field rule.
    @param fieldRule - Field rule.
  */
  public void addRule(IFieldRule fieldRule) {

    // Add to rule list
    if (fieldRule != null) {
      defaultRules.add(fieldRule);
    }
  }

  /**
    Returns the unique field ID.
    @return String - Unique field ID.
  */
  public String getId() {
    return id;
  }

  /**
    Returns the data type.
    @return Integer - Data type.
  */
  public Integer getType() {
    return type;
  }

  /**
    Returns the description based on a given locale.
    @param locale - Locale.
    @return String - Description.
  */
  public String getDescription(Locale locale) {

    // Initialize return value
    String description = null;

    // Retrieve description resource names from core resource file
	  String descriptionResources = ResourceUtil.getString(Constants.DESCRIPTION_RESOURCES);
	
	  // Retrieve description
	  if (descriptionResources != null) {
	    description = ResourceUtil.getString(descriptionResources, locale, defaultDescription);
	  }
	
	  // Use default description if one is not found
	  if (description == null) {
	    description = defaultDescription;
	  }

    return description;
  }

  /**
    Returns the converter associated with the field.
    @return IConverter - Converter.
  */
  public IConverter getConverter() {
    return converter;
  }

  /**
    Returns a list of rules based on a given locale.
    @param locale - Locale.
    @return List - List of field rule objects.
  */
  public List<IFieldRule> getRules(Locale locale) {

    return Collections.unmodifiableList(defaultRules);
  }

}
