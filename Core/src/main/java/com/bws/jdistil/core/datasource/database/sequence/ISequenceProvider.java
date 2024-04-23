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
package com.bws.jdistil.core.datasource.database.sequence;

import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.security.IDomain;

/**
  Interface supporting the retrieval of sequential values for a given table and column.
  @author - Bryan Snipes
*/
public interface ISequenceProvider {

  /**
   * Returns the next available sequential value for a specified table and column.
   * @param tableName - Table name.
   * @param columnName - Column name.
   * @param domain - Target domain.
   * @return int - Next available sequential value.
   * @throws DataSourceException
   */
  public int nextValue(String tableName, String columnName, IDomain domain) throws DataSourceException;
  
}
