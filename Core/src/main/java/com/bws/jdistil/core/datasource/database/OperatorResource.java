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
package com.bws.jdistil.core.datasource.database;

import java.util.ListResourceBundle;

/**
  Resource bundle class used to define local specific operator descriptions.
  @author - Bryan Snipes
*/
public class OperatorResource extends ListResourceBundle {

  /**
    Two dimensional array of message keys and messages.
  */
  static final Object[][] contents = {
    {Operators.BEGINS_WITH, "Begins With"},
    {Operators.CONTAINS, "Contains"},
    {Operators.ENDS_WITH, "Ends With"},
    {Operators.EQUALS, "Equal"},
    {Operators.NOT_EQUALS, "Not Equal"},
    {Operators.GREATER_THAN, "Greater Than"},
    {Operators.GREATER_THAN_OR_EQUAL, "Greater Than or Equal"},
    {Operators.LESS_THAN, "Less Than"},
    {Operators.LESS_THAN_OR_EQUAL, "Less Than or Equal"},
    {Operators.IS_NULL, "Is Null"},
    {Operators.IS_NOT_NULL, "Is Not Null"},
  };

  /**
    Returns all standard messages as a two dimensional array of message keys
    and messages.
    @return Object[][] - Two dimensional array of message keys and messages.
  */
  public Object[][] getContents() {
    return contents;
  }

}
