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

/**
  Defines the binding between a table column and a data object ID property.
  @author - Bryan Snipes
*/
public class IdColumnBinding extends ColumnBinding {

  /**
    Creates a new IdColumnBinding object.
    @param columnName - Column name.
  */
  public IdColumnBinding(String columnName) {
    this(columnName, DbUtil.INTEGER, "Id");
  }

  /**
    Creates a new IdColumnBinding object.
    @param columnName - Column name.
    @param columnType Column type.
    @param propertyName - Property name.
  */
  public IdColumnBinding(String columnName, int columnType, String propertyName) {
    super(columnName, columnType, false, false, propertyName);
  }
  
}
