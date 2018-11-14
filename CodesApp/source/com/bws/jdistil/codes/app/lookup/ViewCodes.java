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
package com.bws.jdistil.codes.app.lookup;

import com.bws.jdistil.codes.app.configuration.ActionIds;
import com.bws.jdistil.codes.app.configuration.FieldIds;
import com.bws.jdistil.codes.app.configuration.PageIds;
import com.bws.jdistil.codes.configuration.AttributeNames;
import com.bws.jdistil.codes.lookup.CategoryManager;
import com.bws.jdistil.codes.lookup.Code;
import com.bws.jdistil.codes.lookup.CodeManager;
import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.model.FilterCriteriaDefinition;
import com.bws.jdistil.core.process.model.OrderCriterionDefinition;
import com.bws.jdistil.core.process.model.ValueCriterionDefinition;
import com.bws.jdistil.core.process.model.ViewDataObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
  Retrieves codes associated with a specified category.
  @author - Bryan Snipes
*/
public class ViewCodes extends ViewDataObjects<Integer, Code> {

  /**
    Filter criteria definition.
  */
  private static final FilterCriteriaDefinition filterCriteriaDefinition = new FilterCriteriaDefinition(Code.class);
  
  /**
    Set of retrieve data action IDs.
  */
  private static final Set<String> retrieveDataActionIds = new HashSet<String>();
  
  /**
    Set of refresh data action IDs.
  */
  private static final Set<String> refreshDataActionIds = new HashSet<String>();
  
  static {
  
    // Create value criterion definition
    List<ValueCriterionDefinition> valueCriterionDefinitions = new ArrayList<ValueCriterionDefinition>();
    valueCriterionDefinitions.add(new ValueCriterionDefinition(FieldIds.CATEGORY_ID, null, Operators.EQUALS, FieldIds.CATEGORY_ID_FILTER, null));
    
    // Create order criterion definition
    OrderCriterionDefinition orderCriterionDefinition = new OrderCriterionDefinition(FieldIds.CODE_SORT_FIELD, FieldIds.CODE_SORT_DIRECTION, null);
    
    // Populate filter criteria definition
    filterCriteriaDefinition.setIsFilterDataRequired(false);
    filterCriteriaDefinition.setValueCriterionDefinitions(valueCriterionDefinitions);
    filterCriteriaDefinition.setOrderCriterionDefinition(orderCriterionDefinition);

    // Populate retrieve data action IDs
    retrieveDataActionIds.add(ActionIds.VIEW_CODES);

    // Populate refresh data action IDs
    refreshDataActionIds.add(ActionIds.DELETE_CODE);
    refreshDataActionIds.add(ActionIds.SAVE_CODE);
    refreshDataActionIds.add(ActionIds.CANCEL_CODE);
  }
  
  /**
    Creates a new ViewCodes object.
  */
  public ViewCodes() {
    super(CodeManager.class, filterCriteriaDefinition, AttributeNames.CODES, PageIds.CODES, 10,
    		FieldIds.CODE_CURRENT_PAGE_NUMBER, FieldIds.CODE_SELECTED_PAGE_NUMBER,
        ActionIds.VIEW_CODES_PREVIOUS_PAGE, ActionIds.VIEW_CODES_NEXT_PAGE, ActionIds.VIEW_CODES_SELECT_PAGE,
        retrieveDataActionIds, refreshDataActionIds, false);
  }

  /**
    Populates the request attributes with reference data.
    @param processContext - Process context.
  */
  protected void populateReferenceData(ProcessContext processContext) throws ProcessException {

  	super.populateReferenceData(processContext);
  	
    // Load reference data
    loadReferenceData(CategoryManager.class, AttributeNames.CATEGORIES, processContext.getRequest());
  }
  
}
