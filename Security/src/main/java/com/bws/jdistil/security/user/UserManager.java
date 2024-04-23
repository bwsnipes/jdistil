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
package com.bws.jdistil.security.user;

import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.database.AssociateBinding;
import com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager;
import com.bws.jdistil.core.datasource.database.ColumnBinding;
import com.bws.jdistil.core.datasource.database.DataObjectBinding;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.datasource.database.IdColumnBinding;
import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.datasource.database.ValueCondition;
import com.bws.jdistil.core.datasource.database.ValueConditions;
import com.bws.jdistil.core.security.IDomain;

import java.util.ArrayList;
import java.util.List;

/**
  User manager class used to retrieve user data objects.
  @author Bryan Snipes
*/
public class UserManager extends BoundDatabaseDataManager<Integer, User> {

  /**
    Creates a new UserManager object.
  */
  public UserManager() {
    super();
  }

  /**
    Creates and returns a data object binding.
    @see com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager#createDataObjectBinding
  */
  protected DataObjectBinding createDataObjectBinding() {

    // Set table name
    String tableName = "bws_user";

    // Create ID column binding
    IdColumnBinding idColumnBinding = new IdColumnBinding("user_id");

    // Create and populate column bindings
    List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();
    columnBindings.add(new ColumnBinding("logon_id", DbUtil.STRING, true, false, "LogonId"));
    columnBindings.add(new ColumnBinding("salt", DbUtil.STRING, false, false, "Salt"));
    columnBindings.add(new ColumnBinding("password", DbUtil.STRING, false, false, "Password"));
    columnBindings.add(new ColumnBinding("first_name", DbUtil.STRING, false, false, "FirstName"));
    columnBindings.add(new ColumnBinding("middle_initial", DbUtil.STRING, false, false, "MiddleInitial"));
    columnBindings.add(new ColumnBinding("last_name", DbUtil.STRING, false, false, "LastName"));
    columnBindings.add(new ColumnBinding("is_domain_admin", DbUtil.BOOLEAN, false, false, "IsDomainAdmin"));
    columnBindings.add(new ColumnBinding("is_deleted", DbUtil.BOOLEAN, false, true, "IsDeleted"));
    columnBindings.add(new ColumnBinding("version", DbUtil.LONG, false, false, "Version"));

    // Create associate bindings list
    List<AssociateBinding> associateBindings = new ArrayList<AssociateBinding>();

    // Create associate ID binding
    IdColumnBinding roleIdColumnBinding = new IdColumnBinding("role_id");
    
    // Add associate ID binding to list
    associateBindings.add(new AssociateBinding("RoleIds", true, "bws_user_role", idColumnBinding, roleIdColumnBinding));
    
    // Create user binding
    DataObjectBinding userBinding = new DataObjectBinding(User.class, tableName, idColumnBinding, columnBindings, null, associateBindings);

    return userBinding;
  }
  
  /**
	  Returns a for a logon ID.
	  @param logonId Logon ID.
	  @param domain Target domain.
	  @return User User data object.
	*/
	public User findByLogonId(String logonId, IDomain domain) throws DataSourceException {
	  
	  // Initialize return value
	  User user = null;
	  
	  if (logonId != null) {
	    
	    // Create value condition
	    ValueCondition valueCondition = new ValueCondition("bws_user", "logon_id", Operators.EQUALS, DbUtil.STRING, logonId);
	
	    // Create and populate value conditions
	    ValueConditions valueConditions = new ValueConditions(valueCondition);
	    
	    // Retrieve users
	    List<User> values = find(null, valueConditions, null, domain);
	    
	    if (values != null && values.size() > 0) {
	    	
	    	// Only one user returned since logon ID is unique
	    	user = values.get(0);
	    }
	  }
	  
	  return user;
	}
}
