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

import com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager;
import com.bws.jdistil.core.datasource.database.ColumnBinding;
import com.bws.jdistil.core.datasource.database.DataObjectBinding;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.datasource.database.IdColumnBinding;

import java.util.ArrayList;
import java.util.List;

/**
  Group manager class used to retrieve group data objects.
  @author Bryan Snipes
*/
public class GroupManager extends BoundDatabaseDataManager<Integer, Group> {

  /**
    Creates a new GroupManager object.
  */
  public GroupManager() {
    super();
  }

  /**
    Creates and returns a data object binding.
    @see com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager#createDataObjectBinding
  */
  protected DataObjectBinding createDataObjectBinding() {

    // Set table name
    String tableName = "bws_field_group";

    // Create ID column binding
    IdColumnBinding idColumnBinding = new IdColumnBinding("group_id");

    // Create and populate column bindings
    List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();
    columnBindings.add(new ColumnBinding("name", DbUtil.STRING, true, false, "Name"));

    // Create group binding
    DataObjectBinding groupBinding = new DataObjectBinding(Group.class, tableName, idColumnBinding, columnBindings, null, null);

    return groupBinding;
  }

  /**
   * Disabling domain awareness so groups are shared across domains.
   * @see com.bws.jdistil.core.datasource.database.DatabaseDataManager#isDomainAware()
   */
  @Override
  protected boolean isDomainAware() {
  	return false;
  }
  
}
