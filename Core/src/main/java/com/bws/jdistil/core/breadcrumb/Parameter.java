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
package com.bws.jdistil.core.breadcrumb;

public class Parameter {

  /**
   * Parameter name.
   */
  private String name = null;
  
  /**
   * Parameter values.
   */
  private String[] values = null;
  
  /**
   * Creates a new parameter object using a specifiec name and value.
   * @param name Parameter name.
   * @param values Parameter values.
   */
  public Parameter(String name, String[] values) {
    super();
    
    // Validate input parameters
    if (name == null) {
      throw new IllegalArgumentException("Name is required.");
    }
    if (values == null || values.length == 0) {
      throw new IllegalArgumentException("Value is required.");
    }
    
    // Set properties
    this.name = name;
    this.values = values;
  }
  
  /**
   * Returns the parameter name.
   * @return String Parameter name.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Returns the parameter values.
   * @return String Parameter value.
   */
  public String[] getValues() {
    return values;
  }
  
}
