package com.bws.jdistil.security.app.domain;

import com.bws.jdistil.security.app.configuration.ActionIds;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.domain.Domain;
import com.bws.jdistil.security.domain.DomainManager;
import com.bws.jdistil.core.datasource.database.Operators;
import com.bws.jdistil.core.process.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Retrieves data supporting the Domain view.
 */
public class ViewDomains extends ViewDataObjects<Integer, Domain> {

  /**
   * Filter criteria definition.
   */
  private static final FilterCriteriaDefinition filterCriteriaDefinition = new FilterCriteriaDefinition(Domain.class);

  /**
   * Set of retrieve data action IDs.
   */ 
  private static final Set<String> retrieveDataActionIds = new HashSet<String>();
  
  /**
   * Set of refresh data action IDs.
   */
  private static final Set<String> refreshDataActionIds = new HashSet<String>();

  static {
    // Create value criterion definition
    List<ValueCriterionDefinition> valueCriterionDefinitions = new ArrayList<ValueCriterionDefinition>();
  	valueCriterionDefinitions.add(new ValueCriterionDefinition(FieldIds.DOMAIN_NAME, FieldIds.DOMAIN_NAME_FILTER_OPERATOR, Operators.CONTAINS, FieldIds.DOMAIN_NAME_FILTER, null));

    // Create order criterion definition
    OrderCriterionDefinition orderCriterionDefinition = new OrderCriterionDefinition(FieldIds.DOMAIN_SORT_FIELD, FieldIds.DOMAIN_SORT_DIRECTION, null);
    
    // Populate filter criteria definition
    filterCriteriaDefinition.setIsFilterDataRequired(false);
    filterCriteriaDefinition.setValueCriterionDefinitions(valueCriterionDefinitions);
    filterCriteriaDefinition.setOrderCriterionDefinition(orderCriterionDefinition);

    // Populate retrieve data action IDs
    retrieveDataActionIds.add(ActionIds.VIEW_DOMAINS);

    // Populate refresh data action IDs
    refreshDataActionIds.add(ActionIds.DELETE_DOMAIN);
    refreshDataActionIds.add(ActionIds.SAVE_DOMAIN);
    refreshDataActionIds.add(ActionIds.CANCEL_DOMAIN);
  }

  /**
   *  Creates a new ViewDomains object.
   */
  public ViewDomains() {
    super(DomainManager.class, filterCriteriaDefinition, AttributeNames.DOMAINS, PageIds.DOMAINS,
    		10, FieldIds.DOMAIN_CURRENT_PAGE_NUMBER, FieldIds.DOMAIN_SELECTED_PAGE_NUMBER, 
    		ActionIds.VIEW_DOMAIN_PREVIOUS_PAGE, ActionIds.VIEW_DOMAIN_NEXT_PAGE, ActionIds.VIEW_DOMAIN_SELECT_PAGE,
        retrieveDataActionIds, refreshDataActionIds, false);
  }

}
