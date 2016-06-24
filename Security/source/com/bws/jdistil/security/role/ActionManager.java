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

import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager;
import com.bws.jdistil.core.datasource.database.ColumnBinding;
import com.bws.jdistil.core.datasource.database.DataObjectBinding;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.datasource.database.IdColumnBinding;
import com.bws.jdistil.core.datasource.database.Join;
import com.bws.jdistil.core.datasource.database.JoinCondition;
import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.datasource.database.ValueCondition;
import com.bws.jdistil.core.datasource.database.ValueConditions;

import java.util.ArrayList;
import java.util.List;

/**
  Action manager class used to retrieve action data objects.
  @author Bryan Snipes
*/
public class ActionManager extends BoundDatabaseDataManager<Integer, Action> {

  /**
    Creates a new ActionManager object.
  */
  public ActionManager() {
    super();
  }

  /**
    Creates and returns a data object binding.
    @see com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager#createDataObjectBinding
  */
  protected DataObjectBinding createDataObjectBinding() {

    // Set table name
    String tableName = "bws_action";

    // Create ID column binding
    IdColumnBinding idColumnBinding = new IdColumnBinding("action_id");

    // Create and populate column bindings
    List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();
    columnBindings.add(new ColumnBinding("secure_id", DbUtil.STRING, true, false, "SecureId"));
    columnBindings.add(new ColumnBinding("task_id", DbUtil.INTEGER, false, false, "TaskId"));

    // Create action binding
    DataObjectBinding actionBinding = new DataObjectBinding(Action.class, tableName, idColumnBinding, columnBindings, null, null);

    return actionBinding;
  }

  /**
    Returns a list of actions for a list of task IDs.
    @param taskIds List of task IDs.
    @return List List of action data objects.
  */
  public List<Action> findByTask(List<Integer> taskIds) throws DataSourceException {
    
    // Initialize return value
    List<Action> actions = null;
    
    if (taskIds != null && !taskIds.isEmpty()) {
      
      // Create join list
      List<Join> joins = new ArrayList<Join>(1);
      
      // Create join conditin
      JoinCondition joinCondition = new JoinCondition("bws_task_action", "action_id", Operators.EQUALS, "bws_action", "action_id");
      
      // Create and add join to list
      joins.add(new Join(Join.INNER_JOIN, "bws_task_action", "bws_action", joinCondition));
      
      // Initialize value conditions
      ValueConditions valueConditions = null;
      
      for (Integer taskId : taskIds) {
      
        // Create value condition
        ValueCondition valueCondition = new ValueCondition("bws_task", "task_id", Operators.EQUALS, DbUtil.INTEGER, taskId);
        
        // Add condition to value conditions
        if (valueConditions == null) {
          valueConditions = new ValueConditions(valueCondition);
        }
        else {
          valueConditions.add(Operators.OR, valueCondition);
        }
      }
      
      // Retrieve tasks
      actions = find(joins, valueConditions, null);
    }
    
    return actions;
  }
  
}
