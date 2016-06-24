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

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
  Defines all value condition methods. Serves as a tag interface for
  ValueCondition and ValueConditions classes.
  @author Bryan Snipes
*/
public interface IValueCondition extends ISqlGenerator {

  /**
    Sets parameter values using a given prepared statement.
    @param sqlStatement Prepared statement.
    @param index Parameter index.
    @return int Next parameter index.
  */
  public int setParameters(PreparedStatement sqlStatement, int index) throws SQLException;
  
  /**
   	Returns a value indicating whether or not the value condition references a given table name.
    @param tableName Table name.
    @return boolean Referenced table indicator.
  */
  public boolean isTableReferenced(String tableName);
  
  /**
   	Returns a value indicating whether or not the value condition references a given column name.
    @param tableName Table name.
    @param columnName Column name.
    @return boolean Referenced column indicator.
  */
  public boolean isColumnReferenced(String tableName, String columnName);
  
}
  