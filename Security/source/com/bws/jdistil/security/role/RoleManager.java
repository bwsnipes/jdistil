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

import com.bws.jdistil.core.datasource.database.AssociateBinding;
import com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager;
import com.bws.jdistil.core.datasource.database.ColumnBinding;
import com.bws.jdistil.core.datasource.database.DataObjectBinding;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.datasource.database.IdColumnBinding;

import java.util.ArrayList;
import java.util.List;

/**
  Role manager class used to retrieve role data objects.
  @author Bryan Snipes
*/
public class RoleManager extends BoundDatabaseDataManager<Integer, Role> {

  /**
    Creates a new RoleManager object.
  */
  public RoleManager() {
    super();
  }

  /**
    Creates and returns a data object binding.
    @see com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager#createDataObjectBinding
  */
  protected DataObjectBinding createDataObjectBinding() {

    // Set table name
    String tableName = "bws_role";

    // Create ID column binding
    IdColumnBinding idColumnBinding = new IdColumnBinding("role_id");

    // Create and populate column bindings
    List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();
    columnBindings.add(new ColumnBinding("name", DbUtil.STRING, true, false, "Name"));
    columnBindings.add(new ColumnBinding("is_deleted", DbUtil.BOOLEAN, false, true, "IsDeleted"));
    columnBindings.add(new ColumnBinding("version", DbUtil.LONG, false, false, "Version"));

    // Create associate bindings list
    List<AssociateBinding> associateBindings = new ArrayList<AssociateBinding>();

    // Create ID column bindings for associate tables
    IdColumnBinding taskIdColumnBinding = new IdColumnBinding("task_id");
    IdColumnBinding fieldIdColumnBinding = new IdColumnBinding("field_id");

    // Populate associate bindings list
    associateBindings.add(new AssociateBinding("RestrictedTaskIds", true, "bws_restricted_task", idColumnBinding, taskIdColumnBinding));
    associateBindings.add(new AssociateBinding("RestrictedFieldIds", true, "bws_restricted_field", idColumnBinding, fieldIdColumnBinding));
    associateBindings.add(new AssociateBinding("ReadOnlyFieldIds", true, "bws_read_only_field", idColumnBinding, fieldIdColumnBinding));
    
    // Create role binding
    DataObjectBinding roleBinding = new DataObjectBinding(Role.class, tableName, 
        idColumnBinding, columnBindings, null, associateBindings);

    return roleBinding;
  }

}
