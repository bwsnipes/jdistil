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
package com.bws.jdistil.security.app.role;

import com.bws.jdistil.core.process.model.FilterCriteriaDefinition;
import com.bws.jdistil.core.process.model.OrderCriterionDefinition;
import com.bws.jdistil.core.process.model.ViewDataObjects;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.role.Role;
import com.bws.jdistil.security.role.RoleManager;

/**
  Retrieves roles.
  @author - Bryan Snipes
*/
public class ViewRoles extends ViewDataObjects<Integer, Role> {

  /**
    Filter criteria definition.
  */
  private static final FilterCriteriaDefinition filterCriteriaDefinition = new FilterCriteriaDefinition(Role.class);
  
  static {
  
    // Create order criterion definition
    OrderCriterionDefinition orderCriterionDefinition = 
        new OrderCriterionDefinition(FieldIds.ROLE_SORT_FIELD, FieldIds.ROLE_SORT_DIRECTION, null);
    
    // Populate filter criteria definition
    filterCriteriaDefinition.setIsFilterDataRequired(false);
    filterCriteriaDefinition.setOrderCriterionDefinition(orderCriterionDefinition);
  }

  
  /**
    Creates a new ViewRoles object.
  */
  public ViewRoles() {
    super(RoleManager.class, filterCriteriaDefinition, AttributeNames.ROLES, PageIds.ROLES);
  }

}
