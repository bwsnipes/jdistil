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

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.AttributeNames;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.datasource.FilterCriteria;
import com.bws.jdistil.core.datasource.IDataManager;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.SegmentedList;
import com.bws.jdistil.core.util.StringUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
  Retrieves a list of data objects and stores them into the request attributes.
  @author Bryan Snipes
*/
public class ViewDataObjects<I, T extends DataObject<I>> extends Processor {

  /**
    Data manager class.
  */
  private Class<? extends IDataManager<I, T>> dataManagerClass = null;

  /**
    Filter criteria definition.
  */
  private FilterCriteriaDefinition filterCriteriaDefinition = null;
  
  /**
    Attribute ID used to store data objects in request attributes.
  */
  private String attributeId = null;

  /**
    Next page ID.
  */
  private String nextPageId = null;

  /**
    Paging support indicator.
  */
  private boolean isPagingSupported = false;

  /**
    Cache paging IDs indicator.
  */
  private boolean cachePagingIds = false;
  
  /**
    Page size.
  */
  private int pageSize = 20;

  /**
    Current page number field ID.
  */
  private String currentPageNumberFieldId = null;

  /**
    Selected page number field ID.
  */
  private String selectedPageNumberFieldId = null;
  
  /**
    Previous page action ID.
  */
  private String previousPageActionId = null;

  /**
    Next page action ID.
  */
  private String nextPageActionId = null;

  /**
    Select page action ID.
  */
  private String selectPageActionId = null;

  /**
    Set of action IDs indicating data retrieval.
  */
  private Set<String> retrieveDataActionIds = new HashSet<String>();

  /**
    Set of action IDs indicating data refresh.
  */
  private Set<String> refreshDataActionIds = new HashSet<String>();

  /**
    Creates a new ViewDataObjects object with filtering support and no paging support.
    @param dataManagerClass Data manager class.
    @param filterCriteriaDefinition Filter criteria definition.
    @param attributeId Attribute ID.
    @param nextPageId Next page ID.
  */
  public ViewDataObjects(Class<? extends IDataManager<I, T>> dataManagerClass, 
      FilterCriteriaDefinition filterCriteriaDefinition, String attributeId, String nextPageId) {
    
    super();

    // Validate parameters
    if (dataManagerClass == null) {
      throw new java.lang.IllegalArgumentException("Data manager class is required.");
    }
    if (StringUtil.isEmpty(attributeId)) {
      throw new java.lang.IllegalArgumentException("Attribute name is required.");
    }
    if (StringUtil.isEmpty(nextPageId)) {
      throw new java.lang.IllegalArgumentException("Next page ID is required.");
    }

    // Set properties
    this.dataManagerClass = dataManagerClass;
    this.filterCriteriaDefinition = filterCriteriaDefinition;
    this.attributeId = attributeId;
    this.nextPageId = nextPageId;

    // Set paging indicator
    this.isPagingSupported = false;
  }

  /**
    Creates a new ViewDataObjects object with filtering and paging support.
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
  */
  public ViewDataObjects(Class<? extends IDataManager<I, T>> dataManagerClass, FilterCriteriaDefinition filterCriteriaDefinition, 
      String attributeId, String nextPageId, int pageSize, String currentPageNumberFieldId, String selectedPageNumberFieldId, 
      String previousPageActionId, String nextPageActionId, String selectPageActionId,
      Set<String> retrieveDataActionIds, Set<String> refreshDataActionIds, boolean cachePagingIds) {

    super();

    // Validate parameters
    if (dataManagerClass == null) {
      throw new java.lang.IllegalArgumentException("Data manager class is required.");
    }
    if (StringUtil.isEmpty(attributeId)) {
      throw new java.lang.IllegalArgumentException("Attribute name is required.");
    }
    if (StringUtil.isEmpty(nextPageId)) {
      throw new java.lang.IllegalArgumentException("Next page ID is required.");
    }
    if (pageSize <= 1) {
      throw new java.lang.IllegalArgumentException("Page size must be greater than one.");
    }
    if (StringUtil.isEmpty(currentPageNumberFieldId)) {
      throw new java.lang.IllegalArgumentException("Current page number field ID is required.");
    }
    if (StringUtil.isEmpty(selectedPageNumberFieldId)) {
      throw new java.lang.IllegalArgumentException("Selected page number field ID is required.");
    }
    if (StringUtil.isEmpty(previousPageActionId)) {
      throw new java.lang.IllegalArgumentException("Previous page action ID is required.");
    }
    if (StringUtil.isEmpty(nextPageActionId)) {
      throw new java.lang.IllegalArgumentException("Next page action ID is required.");
    }
    if (StringUtil.isEmpty(selectPageActionId)) {
      throw new java.lang.IllegalArgumentException("Select page action ID is required.");
    }
    if (retrieveDataActionIds == null || retrieveDataActionIds.size() == 0) {
      throw new java.lang.IllegalArgumentException("At least one retrieve data action ID is required.");
    }

    // Set data access properties
    this.dataManagerClass = dataManagerClass;
    this.filterCriteriaDefinition = filterCriteriaDefinition;
    this.attributeId = attributeId;
    this.nextPageId = nextPageId;
    this.cachePagingIds = cachePagingIds;

    // Set paging indicator and page size
    this.isPagingSupported = true;
    this.pageSize = pageSize;

    // Set paging field properties
    this.currentPageNumberFieldId = currentPageNumberFieldId;
    this.selectedPageNumberFieldId = selectedPageNumberFieldId;

    // Set paging action properties
    this.previousPageActionId = previousPageActionId;
    this.nextPageActionId = nextPageActionId;
    this.selectPageActionId = selectPageActionId;

    this.retrieveDataActionIds.addAll(retrieveDataActionIds);

    if (refreshDataActionIds != null) {
      this.refreshDataActionIds.addAll(refreshDataActionIds);
    }
  }

  /**
    Retrieves a list of data objects and stores them into the request attributes.
    @see com.bws.jdistil.core.process.IProcessor#process
  */
  @SuppressWarnings("unchecked")
  public void process(ProcessContext processContext) throws ProcessException {

    // Get request
    HttpServletRequest request = processContext.getRequest();
    
    // Initialize data objects
    List<T> dataObjects = null;
    
    if (!isPagingSupported) {

      // Retrieve data objects
      dataObjects = retrieveData(processContext);
    }
    else {

      // Get session
      HttpSession session = (HttpSession)request.getSession(true);

      // Initialize data object IDs
      SegmentedList<I> dataObjectIds = null;

      // Get action
      Action action = processContext.getAction();

      if (isRetrieveDataAction(action) || isRefreshDataAction(action) || !cachePagingIds) {

        // Retrieve data object IDs
        dataObjectIds = retrieveIds(processContext);
      }
      else {
        
        // Retrieve data object IDs from session
        dataObjectIds = (SegmentedList<I>)session.getAttribute(AttributeNames.PAGING_IDS);
      }

      // Store data object IDs in seesion
      if (cachePagingIds) {
        session.setAttribute(AttributeNames.PAGING_IDS, dataObjectIds);
      }
      
      // Calculate total items, total pages, and page number
      int totalItems = calculateTotalItems(dataObjectIds);
      int totalPages = calculateTotalPages(totalItems);
      int pageNumber = calculatePageNumber(totalPages, action, request);
      
      if (dataObjectIds != null && !dataObjectIds.isEmpty()) {
      	
        // Get data object IDs for next page
        List<I> targetIds = dataObjectIds.getSegment(pageNumber);
        
        // Retrieve  paging data
        dataObjects = retrieveDataObjects(targetIds);
      }
      
      // Store paging data in request attributes
      request.setAttribute(AttributeNames.TOTAL_ITEMS, Integer.valueOf(totalItems));
      request.setAttribute(AttributeNames.TOTAL_PAGES, Integer.valueOf(totalPages));
      request.setAttribute(AttributeNames.CURRENT_PAGE_NUMBER, Integer.valueOf(pageNumber));
    }

    // Add data objects to request attributes
    if (dataObjects != null) {
      request.setAttribute(attributeId, dataObjects);
    }

    // Populate reference data
    populateReferenceData(processContext);
    
    // Set next page
    setNextPage(nextPageId, processContext);
  }

  /**
    Populates the request attributes with reference data.
    @param processContext Process context.
  */
  protected void populateReferenceData(ProcessContext processContext) throws ProcessException {
  
    // Do nothing by default
  }
  
  /**
    Returns a value indicating whether or not a given action is a retrieve data action.
    @param action Action object.
    @return boolean Retrieve data action indicator.
  */
  protected boolean isRetrieveDataAction(Action action) {
    return retrieveDataActionIds != null && retrieveDataActionIds.contains(action.getId());
  }

  /**
    Returns a value indicating whether or not a given action is a refresh data action.
    @param action Action object.
    @return boolean Refresh data action indicator.
  */
  protected boolean isRefreshDataAction(Action action) {
    return refreshDataActionIds != null && refreshDataActionIds.contains(action.getId());
  }

  /**
    Returns a value indicating whether or not a given action is a previous page action.
    @param action Action object.
    @return boolean Previous page action indicator.
  */
  protected boolean isPreviousPageAction(Action action) {
    return previousPageActionId.equals(action.getId());
  }

  /**
    Returns a value indicating whether or not a given action is a next page action.
    @param action Action object.
    @return boolean Next page action indicator.
  */
  protected boolean isNextPageAction(Action action) {
    return nextPageActionId.equals(action.getId());
  }

  /**
    Returns a value indicating whether or not a given action is a select page action.
    @param action Action object.
    @return boolean Select page action indicator.
  */
  protected boolean isSelectPageAction(Action action) {
    return selectPageActionId.equals(action.getId());
  }

  /**
    Returns a search criteria object if filter field IDs or a sort field ID is defined.
    @return FilterCriteria Filter criteria.
  */
  private FilterCriteria getFilterCriteria(ProcessContext processContext) {
  
    // Initialize return value
    FilterCriteria filterCriteria = null;
    
    // Create filter criteria if a definition is specified
    if (filterCriteriaDefinition != null) {
      filterCriteria = filterCriteriaDefinition.create(processContext.getRequest());
    }
    
    return filterCriteria;
  }
  
  /**
    Retrieves a list of data objects.
    @param processContext Process context.
    @return List List of data objects.
  */
  @SuppressWarnings("unchecked")
	private List<T> retrieveData(ProcessContext processContext)
      throws ProcessException {

    // Initialize return value
    List<T> dataObjects = null;

    // Initialize data manager
    IDataManager<I, T> dataManager = null;

    // Check for a registered factory
    IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);

    // Create data manager
    dataManager = (IDataManager<I, T>)dataManagerFactory.create();

    try {
      // Retrieve data
      dataObjects = retrieveData(dataManager, processContext);
    }
    finally {

      // Recycle data manager
      if (dataManagerFactory != null) {
        dataManagerFactory.recycle(dataManager);
      }
    }

    return dataObjects;
  }
  
  /**
    Retrieves a list of data objects using a data manager and process context.
    @param dataManager Data manager.
    @param processContext Process context.
    @return List List of data objects.
  */
	protected List<T> retrieveData(IDataManager<I, T> dataManager, ProcessContext processContext)
      throws ProcessException {

    // Set method name
    String methodName = "retrieveData";

    // Initialize return value
    List<T> dataObjects = null;

    // Get filter criteria
    FilterCriteria filterCriteria = getFilterCriteria(processContext);
    
    try {

      // Retrieve data
      if (filterCriteria == null || filterCriteria.getValueCriteria().isEmpty()) {
        
        if (filterCriteriaDefinition != null && !filterCriteriaDefinition.getIsFilterDataRequired()) {
          dataObjects = dataManager.find();
        }
      }
      else {
        dataObjects = dataManager.find(filterCriteria);
      }
    }
    catch (DataSourceException dataSourceException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.process.model");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Retrieving Data", dataSourceException);

      throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
    }

    return dataObjects;
  }

  /**
    Retrieves a segmented list of data object IDs.
    @param processContext Process context.
    @return SegmentedList Segmented list of data object IDs.
  */
  @SuppressWarnings("unchecked")
	private SegmentedList<I> retrieveIds(ProcessContext processContext) throws ProcessException {

    // Initialize return value
    SegmentedList<I> dataObjectIds = null;

    // Initialize data manager
    IDataManager<I, T> dataManager = null;

    // Check for a registered factory
    IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);

    // Create data manager
    dataManager = (IDataManager<I, T>)dataManagerFactory.create();

    try {
      // Retrieve data object IDs
      List<I> ids = retrieveIds(dataManager, processContext);

      // Create segmented list of data object IDs
      if (ids != null) {
        dataObjectIds = new SegmentedList<I>(pageSize, ids);
      }
    }
    finally {

      // Recycle data manager
      if (dataManagerFactory != null) {
        dataManagerFactory.recycle(dataManager);
      }
    }
    
    return dataObjectIds;
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

    // Set method name
    String methodName = "retrieveIds";

    // Initialize return value
    List<I> dataObjectIds = null;

    // Get filter criteria
    FilterCriteria filterCriteria = null;
    
    try {

      if (filterCriteriaDefinition != null) {
        
        // Create filter criteria
        filterCriteria = filterCriteriaDefinition.create(processContext.getRequest());
        
        // Retrieve data object IDs using filter criteria
        if (filterCriteria.hasValueCriteria() || !filterCriteriaDefinition.getIsFilterDataRequired()) {
          dataObjectIds = dataManager.findIds(filterCriteria);
        }
      }
      else {

        // Retrieve data object IDs
        dataObjectIds = dataManager.findIds();
      }
    }
    catch (DataSourceException dataSourceException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.process.model");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Retrieving Data Object IDs", dataSourceException);

      throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
    }

    return dataObjectIds;
  }

  /**
    Returns data objects using a list of data object IDs.
    @param dataObjectIds List of data object IDs.
    @return List<T> List of data objects.
  */
  @SuppressWarnings("unchecked")
	private List<T> retrieveDataObjects(List<I> dataObjectIds) throws ProcessException {
  
    // Initialize return value
    List<T> dataObjects = null;
    
    if (dataObjectIds != null && dataObjectIds.size() > 0) {
    
      // Initialize data manager
      IDataManager<I, T> dataManager = null;
    
      // Check for a registered factory
      IFactory dataManagerFactory = ConfigurationManager.getFactory(dataManagerClass);
    
      // Create data manager
      dataManager = (IDataManager<I, T>)dataManagerFactory.create();
    
      try {
        dataObjects = retrieveDataObjects(dataManager, dataObjectIds);
      }
      finally {
        
        // Recycle data manager
        if (dataManagerFactory != null) {
          dataManagerFactory.recycle(dataManager);
        }
      }
    }
    
    return dataObjects;
  }
  
  /**
    Returns data objects using a specified data manager and a list of data object IDs.
    @param dataManager Data manager.
    @param dataObjectIds List of data object IDs.
    @return List<T> List of data objects.
  */
	protected List<T> retrieveDataObjects(IDataManager<I, T> dataManager, List<I> dataObjectIds)
      throws ProcessException {
  
    // Set method name
    String methodName = "retrieveDataObjects";
    
    // Initialize data objects list
    List<T> dataObjects = null;
      
    try {
      // Retrieve paging data
      dataObjects = dataManager.find(dataObjectIds);
    }
    catch (DataSourceException dataSourceException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.process.model");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Retrieving Page Data", dataSourceException);

      throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
    }

    return dataObjects;
  }
  
  /**
    Returns the total number of items using a list of data object IDs.
    @param dataObjectIds List of data object IDs.
    @return int Total number of items.
  */
  private int calculateTotalItems(List<I> dataObjectIds) throws ProcessException {
  
    // Initialize return value
    int totalItems = 0;
    
    // Set total items and total pages
    if (dataObjectIds != null) {
      totalItems = dataObjectIds.size();
    }
  
    return totalItems;
  }
  
  /**
    Returns the total number of pages using the total number of data objects and page size.
    @param totalItems Total number of items.
    @return int Total number of pages.
  */
  private int calculateTotalPages(int totalItems) throws ProcessException {
  
    // Initialize return value
    int totalPages = 0;
    
    if (totalItems > 0) {
  
      // Set total pages
      totalPages = totalItems / pageSize;
      
      // Account for last page
      if ((totalItems % pageSize) > 0) {
        totalPages++;
      }
    }
  
    return totalPages;
  }
  
  /**
    Calculates and returns the targeted page number based on the total number of pages, 
    selected action, and HTTP servlet request.
    @param totalPages Total number of pages.
    @param action Action object.
    @param request HTTP servlet request.
    @return Integer Targeted page number.
  */
  private Integer calculatePageNumber(int totalPages, Action action, HttpServletRequest request) {
  
    // Initialize next page number
    int pageNumber = 0;
  
    if (isSelectPageAction(action)) {
      
      // Get selected page number
      Integer selectedPageNumber = ParameterExtractor.getInteger(request, selectedPageNumberFieldId);
  
      // Set next page number to selected page number
      if (selectedPageNumber != null) {
        pageNumber = selectedPageNumber.intValue();
      }
    }
    else {
  
      // Get current page number
      Integer currentPageNumber = ParameterExtractor.getInteger(request, currentPageNumberFieldId);
  
      if (currentPageNumber != null) {
  
        // Set next page number based on paging action
        if (isRefreshDataAction(action)) {
          pageNumber = currentPageNumber.intValue();
        }
        else if (isPreviousPageAction(action)) {
          pageNumber = currentPageNumber.intValue() - 1;
        }
        else if (isNextPageAction(action)) {
          pageNumber = currentPageNumber.intValue() + 1;
        }
      }
    }
  
    // Ensure next page number doesn't exceed paging bounds
    if (pageNumber <= 0) {
      pageNumber = 1;
    }
    else if (pageNumber > totalPages) {
      pageNumber = totalPages;
    }
  
    return pageNumber;
  }

  /**
    Returns the data manager class.
    @return Class Data manager class.
  */
  public Class<? extends IDataManager<I, T>> getDataManagerClass() {
		return dataManagerClass;
	}

  /**
	  Returns the filter criteria definition.
	  @return FilterCriteriaDefinition Filter criteria definition.
	*/
	public FilterCriteriaDefinition getFilterCriteriaDefinition() {
		return filterCriteriaDefinition;
	}

  /**
	  Returns the attribute ID.
	  @return String Attribute ID.
	*/
	public String getAttributeId() {
		return attributeId;
	}

  /**
	  Returns the next page ID.
	  @return String Next page ID.
	*/
	public String getNextPageId() {
		return nextPageId;
	}

  /**
	  Returns the paging support indicator.
	  @return boolean Paging support indicator.
	*/
	public boolean getIsPagingSupported() {
		return isPagingSupported;
	}

  /**
	  Returns the cache paging IDs indicator.
	  @return boolean Cache paging IDs indicator.
	*/
	public boolean getIsCachePagingIds() {
		return cachePagingIds;
	}

  /**
	  Returns the page size.
	  @return int Page size.
	*/
	public int getPageSize() {
		return pageSize;
	}

	/**
    Returns the current page number field ID.
    @return String Current page number field ID.
  */
  public String getCurrentPageNumberFieldId() {
		return currentPageNumberFieldId;
	}

	/**
    Returns the selected page number field ID.
    @return String Selected page number field ID.
  */
	public String getSelectedPageNumberFieldId() {
		return selectedPageNumberFieldId;
	}

	/**
	  Returns the previous page action ID.
	  @return String Previous page action ID.
	*/
	public String getPreviousPageActionId() {
		return previousPageActionId;
	}

  /**
	  Returns the next page action.
	  @return String Next page action ID.
	*/
	public String getNextPageActionId() {
		return nextPageActionId;
	}

  /**
	  Returns the select page action ID.
	  @return String Select page action ID.
	*/
	public String getSelectPageActionId() {
		return selectPageActionId;
	}

  /**
	  Returns the set of retrieve data action IDs.
	  @return Set Set of retrieve data action IDs.
	*/
	public Set<String> getRetrieveDataActionIds() {
		return retrieveDataActionIds;
	}

  /**
	  Returns the set of refresh data action IDs.
	  @return Set Set of refresh data action IDs.
	*/
	public Set<String> getRefreshDataActionIds() {
		return refreshDataActionIds;
	}

}
