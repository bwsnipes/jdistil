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
package com.bws.jdistil.security.app.user;

import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.process.model.FilterCriteriaDefinition;
import com.bws.jdistil.core.process.model.OrderCriterionDefinition;
import com.bws.jdistil.core.process.model.ValueCriterionDefinition;
import com.bws.jdistil.core.process.model.ViewDataObjects;
import com.bws.jdistil.security.app.configuration.ActionIds;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.user.User;
import com.bws.jdistil.security.user.UserManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
  Retrieves users.
  @author - Bryan Snipes
*/
public class ViewUsers extends ViewDataObjects<Integer, User> {

  /**
    Filter criteria definition.
  */
  private static final FilterCriteriaDefinition filterCriteriaDefinition = new FilterCriteriaDefinition();
  
  /**
    Set of retrieve data action IDs.
  */
  private static final Set<String> retrieveDataActionIds = new HashSet<String>();
  
  /**
    Set of refresh data action IDs.
  */
  private static final Set<String> refreshDataActionIds = new HashSet<String>();

  static {
  
    // Create value criterion definitions
    List<ValueCriterionDefinition> valueCriterionDefinitions = new ArrayList<ValueCriterionDefinition>();
    valueCriterionDefinitions.add(new ValueCriterionDefinition(FieldIds.USER_FIRST_NAME, FieldIds.FIRST_NAME_FILTER_OPERATOR, Operators.BEGINS_WITH, FieldIds.FIRST_NAME_FILTER, null));
    valueCriterionDefinitions.add(new ValueCriterionDefinition(FieldIds.USER_LAST_NAME, FieldIds.LAST_NAME_FILTER_OPERATOR, Operators.BEGINS_WITH, FieldIds.LAST_NAME_FILTER, null));
    
    // Create order criterion definition
    OrderCriterionDefinition orderCriterionDefinition = new OrderCriterionDefinition(FieldIds.USER_SORT_FIELD, FieldIds.USER_SORT_DIRECTION, null);
    
    // Populate filter criteria definition
    filterCriteriaDefinition.setIsFilterDataRequired(false);
    filterCriteriaDefinition.setValueCriterionDefinitions(valueCriterionDefinitions);
    filterCriteriaDefinition.setOrderCriterionDefinition(orderCriterionDefinition);

    // Populate retrieve data action IDs
    retrieveDataActionIds.add(ActionIds.VIEW_USERS);

    // Populate refresh data action IDs
    refreshDataActionIds.add(ActionIds.DELETE_USER);
    refreshDataActionIds.add(ActionIds.SAVE_USER);
    refreshDataActionIds.add(ActionIds.CANCEL_USER);
  }
  
  /**
    Creates a new ViewUsers object.
  */
  public ViewUsers() {
    super(UserManager.class, filterCriteriaDefinition, AttributeNames.USERS, PageIds.USERS, 10,
    		FieldIds.USER_CURRENT_PAGE_NUMBER, FieldIds.USER_SELECTED_PAGE_NUMBER,
        ActionIds.VIEW_USER_PREVIOUS_PAGE, ActionIds.VIEW_USER_NEXT_PAGE, ActionIds.VIEW_USER_SELECT_PAGE,
        retrieveDataActionIds, refreshDataActionIds, false);
  }

}
