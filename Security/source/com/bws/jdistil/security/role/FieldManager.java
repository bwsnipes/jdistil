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
package com.bws.jdistil.security.role;

import java.util.ArrayList;
import java.util.List;

import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager;
import com.bws.jdistil.core.datasource.database.ColumnBinding;
import com.bws.jdistil.core.datasource.database.DataObjectBinding;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.datasource.database.IdColumnBinding;
import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.datasource.database.ValueCondition;
import com.bws.jdistil.core.datasource.database.ValueConditions;

/**
  Field manager class used to retrieve field data objects.
  @author Bryan Snipes
*/
public class FieldManager extends BoundDatabaseDataManager<Integer, Field> {

  /**
    Creates a new FieldManager object.
  */
  public FieldManager() {
    super();
  }

  /**
    Creates and returns a data object binding.
    @see com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager#createDataObjectBinding
  */
  protected DataObjectBinding createDataObjectBinding() {

    // Set table name
    String tableName = "bws_field";

    // Create ID column binding
    IdColumnBinding idColumnBinding = new IdColumnBinding("field_id");

    // Create and populate column bindings
    List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();
    columnBindings.add(new ColumnBinding("name", DbUtil.STRING, false, false, "Name"));
    columnBindings.add(new ColumnBinding("secure_id", DbUtil.STRING, true, false, "SecureId"));
    columnBindings.add(new ColumnBinding("group_id", DbUtil.INTEGER, false, false, "GroupId"));

    // Create field binding
    DataObjectBinding fieldBinding = new DataObjectBinding(Field.class, tableName, idColumnBinding, columnBindings, null, null);

    return fieldBinding;
  }

  /**
   * Disabling domain awareness so fields are shared across domains.
   * @see com.bws.jdistil.core.datasource.database.DatabaseDataManager#isDomainAware()
   */
  @Override
  protected boolean isDomainAware() {
  	return false;
  }
  
  /**
    Returns a list of properties for an group ID.
    @param groupId Group ID.
    @return List List of field data objects.
  */
  public List<Field> findByGroup(Integer groupId) throws DataSourceException {
    
    // Initialize return value
    List<Field> properties = null;
    
    if (groupId != null) {
      
      // Create value condition
      ValueCondition valueCondition = new ValueCondition("bws_field", "group_id", Operators.EQUALS, DbUtil.INTEGER, groupId);

      // Create and populate value conditions
      ValueConditions valueConditions = new ValueConditions(valueCondition);
      
      // Retrieve tasks
      properties = find(null, valueConditions, null);
    }
    
    return properties;
  }

}
