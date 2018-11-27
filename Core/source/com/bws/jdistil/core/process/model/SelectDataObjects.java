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
package com.bws.jdistil.core.process.model;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.FieldIds;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
  Provides support for viewing and selecting data objects as values associated with another data object.
  @author Bryan Snipes
*/
public class SelectDataObjects<I, T extends DataObject<I>> extends ViewDataObjects<I, T> {

	/**
		Selected associates field ID.
	*/
	private String selectedAssociatesFieldId = null;
	
	/**
		Selected associates attribute name.
	*/
	private String selectedAssociatesAttributeName = null;
	
	/**
	  Add associate action ID.
	*/
	private String addAssociateActionId = null;
	
	/**
		Remove associate field ID.
	*/
	private String removeAssociateActionId = null;
	
	/**
		Close action ID.
	*/
	private String closeActionId = null;
	
  /**
    Creates a new SelectDataObjects object with filtering support and no paging support.
    @param dataManagerClass Data manager class.
    @param filterCriteriaDefinition Filter criteria definition.
    @param attributeId Attribute ID.
    @param nextPageId Next page ID.
	  @param selectedAssociatesAttributeName Name used to store selected associate data objects in request attributes.
	  @param selectedAssociatesFieldId Field ID identifying associates selected on add and remove actions.
	  @param addAssociateActionId Add associate action ID.
	  @param removeAssociateActionId Remove associate action ID.
	  @param closeActionId Close page action ID.
  */
  public SelectDataObjects(Class<? extends IDataManager<I, T>> dataManagerClass, FilterCriteriaDefinition filterCriteriaDefinition, 
  		String attributeId, String nextPageId, String selectedAssociatesAttributeName, String selectedAssociatesFieldId, 
  		String addAssociateActionId, String removeAssociateActionId, String closeActionId) {
    
    super(dataManagerClass, filterCriteriaDefinition, attributeId, nextPageId);

    // Set attributes
    this.selectedAssociatesAttributeName = selectedAssociatesAttributeName; 
    this.selectedAssociatesFieldId = selectedAssociatesFieldId; 
    this.addAssociateActionId = addAssociateActionId;
    this.removeAssociateActionId = removeAssociateActionId;
    this.closeActionId = closeActionId;
  }

  /**
    Creates a new SelectDataObjects object with filtering and paging support.
    @param dataManagerClass Data manager class.
    @param filterCriteriaDefinition Filter criteria definition.
    @param attributeId Attribute ID.
    @param nextPageId Next page ID.
    @param pageSize Page size.
    @param currentPageNumberFieldId Current page number field ID.
    @param selectedPageNumberFieldId Selected page number field ID.
    @param previousPageActionId Previous page action ID.
    @param nextPageActionId Next page action ID.
    @param selectPageActionId Select page action ID.
    @param retrieveDataActionIds Collection of data retrieval action IDs.
    @param refreshDataActionIds Collection of data refresh action IDs.
    @param cachePagingIds Cache paging IDs indicator.
	  @param selectedAssociatesAttributeName Name used to store selected associate data objects in request attributes.
	  @param selectedAssociatesFieldId Field ID identifying associates selected on add and remove actions.
	  @param addAssociateActionId Add associate action ID.
	  @param removeAssociateActionId Remove associate action ID.
	  @param closeActionId Close page action ID.
  */
  public SelectDataObjects(Class<? extends IDataManager<I, T>> dataManagerClass, FilterCriteriaDefinition filterCriteriaDefinition, 
  		String attributeId, String nextPageId, int pageSize, String currentPageNumberFieldId, String selectedPageNumberFieldId, 
  		String previousPageActionId, String nextPageActionId, String selectPageActionId, Set<String> retrieveDataActionIds, 
  		Set<String> refreshDataActionIds, boolean cachePagingIds, String selectedAssociatesAttributeName, String selectedAssociatesFieldId, 
  		String addAssociateActionId, String removeAssociateActionId, String closeActionId) {

    super(dataManagerClass, filterCriteriaDefinition, attributeId, nextPageId, pageSize, currentPageNumberFieldId, selectedPageNumberFieldId, 
    		previousPageActionId,	nextPageActionId, selectPageActionId, retrieveDataActionIds, refreshDataActionIds, cachePagingIds);

    // Set attributes
    this.selectedAssociatesAttributeName = selectedAssociatesAttributeName; 
    this.selectedAssociatesFieldId = selectedAssociatesFieldId; 
    this.addAssociateActionId = addAssociateActionId;
    this.removeAssociateActionId = removeAssociateActionId;
    this.closeActionId = closeActionId;
  }

  /**
    Retrieves a list of data objects and stores them into the request attributes.
    @see com.bws.jdistil.core.process.IProcessor#process
  */
  public void process(ProcessContext processContext) throws ProcessException {

    // Get request
    HttpServletRequest request = processContext.getRequest();

  	// Get parent field ID
  	String parentFieldId = ParameterExtractor.getString(request, FieldIds.PARENT_FIELD_ID);

  	// Get action ID
    String actionId = processContext.getAction().getId();
    
    if (actionId.equals(closeActionId)) {
    	
    	// Retrieve parent page ID
    	String parentPageId = ParameterExtractor.getString(request, FieldIds.PARENT_PAGE_ID);
    	
    	if (!StringUtil.isEmpty(parentPageId)) {
    		
      	// Get parent page
      	Page parentPage = ConfigurationManager.getPage(parentPageId);
      	
      	if (parentPage != null) {
      		
        	// Set parent page as next page
        	processContext.setNextPage(parentPage);
      	}
    	}
    }
    else {
    	
    	// Get currently selected associate field IDs
      @SuppressWarnings("unchecked")
			List<I> associateFieldIds = (List<I>)ParameterExtractor.getObjects(request, parentFieldId);

      if (associateFieldIds == null) {

        // Create associate field IDs if necessary
      	associateFieldIds = new ArrayList<I>();
      }
      
    	// Get selected associate IDs 
			List<I> selectedAssociateIds = getSelectedAssociateIds(request);
    	
    	if (selectedAssociateIds != null) {
    	
      	if (actionId.equals(addAssociateActionId)) {
        	
      		// Add selected associate IDs to collection
      		associateFieldIds.addAll(selectedAssociateIds);
        }
        else if (actionId.equals(removeAssociateActionId)) {
        		
      		// Remove selected associate IDs to collection
      		associateFieldIds.removeAll(selectedAssociateIds);
        }
    	}
    	
    	// Store associate field IDs in request attributes
    	request.setAttribute(parentFieldId, associateFieldIds);

    	if (!associateFieldIds.isEmpty()) {
      	
      	// Retrieve selected associates
      	List<T> selectedAssociates = findDataObjects(getDataManagerClass(), associateFieldIds, processContext);
      	 
      	if (selectedAssociates != null) {
      		
      		// Store selected associates in request attributes
      		request.setAttribute(selectedAssociatesAttributeName, selectedAssociates);
      	}
      }

      super.process(processContext);
    }
  }
  
  /**
   * Returns a list of selected associate IDs.
   * @param request Servlet request object.
   * @return List<I> List of selected associate IDs.
   */
  private List<I> getSelectedAssociateIds(ServletRequest request) {
  	
    // Initialize selected associate IDs
    List<I> selectedAssociateIds = new ArrayList<I>();
    
    // Get instances
    List<Integer> instances = ParameterExtractor.getInstances(request, selectedAssociatesFieldId);
    
    if (instances != null) {
    	
    	for (Integer instance : instances) {
    		
    		// Get selected associate ID
    		@SuppressWarnings("unchecked")
				I selectedAssociateId = (I)ParameterExtractor.getObject(request, selectedAssociatesFieldId, instance.intValue());
    		
    		if (selectedAssociateId != null) {
    			
    			// Add selected associate ID to return list
    			selectedAssociateIds.add(selectedAssociateId);
    		}
    	}
    }
    
    return selectedAssociateIds;
  }
  
  /**
    Retrieves a list of data objects using a data manager and process context.
    @param dataManager Data manager.
    @param processContext Process context.
    @return List List of data objects.
  */
	protected List<T> retrieveData(IDataManager<I, T> dataManager, ProcessContext processContext)
      throws ProcessException {

		// Retrieve data objects
		List<T> dataObjects = super.retrieveData(dataManager, processContext);
		
  	if (dataObjects != null && !dataObjects.isEmpty()) {
  		
  		// Get servlet request
  		ServletRequest request = processContext.getRequest();
  		
    	// Get parent field ID
    	String parentFieldId = ParameterExtractor.getString(request, FieldIds.PARENT_FIELD_ID);

    	// Get associate field IDs
  		@SuppressWarnings("unchecked")
			List<I> associateFieldIds = (List<I>)request.getAttribute(parentFieldId);
  		
  		if (associateFieldIds != null) {

  			for (int index = dataObjects.size() - 1; index >= 0; index--) {
  			
  				// Get next data object
  				T dataObject = dataObjects.get(index);
  				
  				if (associateFieldIds.contains(dataObject.getId())) {
  					
  					// Remove data object if selected
  					dataObjects.remove(index);
  				}
  			}
  		}
  	}

    return dataObjects;
  }

  /**
    Retrieves a list of data object IDs using a data manager and process context.
    Default implementation retrieves all data object IDs.
    @param dataManager Data manager.
    @param processContext Process context.
    @return List List of data object IDs.
  */
  protected List<I> retrieveIds(IDataManager<I, T> dataManager, ProcessContext processContext)
      throws ProcessException {

  	// Retrieve IDs
  	List<I> dataObjectIds = super.retrieveIds(dataManager, processContext);
  	
  	if (dataObjectIds != null && !dataObjectIds.isEmpty()) {
  		
  		// Get servlet request
  		ServletRequest request = processContext.getRequest();
  		
    	// Get parent field ID
    	String parentFieldId = ParameterExtractor.getString(request, FieldIds.PARENT_FIELD_ID);

    	// Get associate field IDs
  		@SuppressWarnings("unchecked")
			List<I> associateFieldIds = (List<I>)request.getAttribute(parentFieldId);
  		
  		if (associateFieldIds != null) {
  			
    		// Remove all selected IDs
    		dataObjectIds.removeAll(associateFieldIds);
  		}
  	}
  	
  	return dataObjectIds;
  }

}
