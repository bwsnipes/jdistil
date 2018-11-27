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
package com.bws.jdistil.codes.lookup;

import com.bws.jdistil.core.configuration.FieldValues;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager;
import com.bws.jdistil.core.datasource.database.ColumnBinding;
import com.bws.jdistil.core.datasource.database.DataObjectBinding;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.datasource.database.IdColumnBinding;
import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.datasource.database.OrderCondition;
import com.bws.jdistil.core.datasource.database.ValueCondition;
import com.bws.jdistil.core.datasource.database.ValueConditions;
import com.bws.jdistil.core.security.IDomain;

import java.util.ArrayList;
import java.util.List;

/**
  Code manager class used to retrieve code data objects.
  @author Bryan Snipes
*/
public class CodeManager extends BoundDatabaseDataManager<Integer, Code> {

  /**
    Creates a new CodeManager object.
  */
  public CodeManager() {
    super();
  }

  /**
    Creates and returns a data object binding.
    @see com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager#createDataObjectBinding
  */
  protected DataObjectBinding createDataObjectBinding() {

    // Set table name
    String tableName = "bws_code";

    // Create ID column binding
    IdColumnBinding idColumnBinding = new IdColumnBinding("code_id");

    // Create parent ID column binding
    IdColumnBinding parentIdColumnBinding = new IdColumnBinding("category_id", DbUtil.INTEGER, "CategoryId");

    // Create and populate column bindings
    List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();
    columnBindings.add(new ColumnBinding("name", DbUtil.STRING, true, false, "Name"));
    columnBindings.add(new ColumnBinding("is_default", DbUtil.BOOLEAN, false, false, "IsDefault"));
    columnBindings.add(new ColumnBinding("category_id", DbUtil.INTEGER, true, false, "CategoryId", "bws_category", "category_id", "name"));
    columnBindings.add(new ColumnBinding("is_deleted", DbUtil.BOOLEAN, false, true, "IsDeleted"));
    columnBindings.add(new ColumnBinding("version", DbUtil.LONG, false, false, "Version"));

    // Create code binding
    DataObjectBinding codeBinding = new DataObjectBinding(Code.class, tableName, parentIdColumnBinding, idColumnBinding, columnBindings, null, null);

    return codeBinding;
  }

  /**
   * Returns a list of codes for a category ID.
   * @param categoryId Category ID.
   * @param domain Target domain.
   * @return List List of field data objects.
   * @throws DataSourceException
   */
  public List<Code> findByCategory(Integer categoryId, IDomain domain) throws DataSourceException {
    
    // Initialize return value
    List<Code> codes = null;
    
    if (categoryId != null) {
      
      // Create specific value conditions
      ValueCondition categoryCondition = new ValueCondition("bws_code", "category_id", Operators.EQUALS, DbUtil.INTEGER, categoryId);
      ValueCondition deletedCondition = new ValueCondition("bws_code", "is_deleted", Operators.IS_NOT_NULL, DbUtil.BOOLEAN, null);
  
      // Create value conditions using specific conditions
      ValueConditions valueConditions = new ValueConditions(categoryCondition);
      valueConditions.add(Operators.AND, deletedCondition);
      
      // Create order condition
      OrderCondition orderCondition = new OrderCondition("bws_code", "name", FieldValues.SORT_ASCENDING);
      
      // Retrieve codes
      codes = find(null, valueConditions, orderCondition, domain);
    }
    
    return codes;
  }

}
