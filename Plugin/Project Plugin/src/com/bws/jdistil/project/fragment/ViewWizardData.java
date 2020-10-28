package com.bws.jdistil.project.fragment;

/**
 * View wizard data. 
 */
public class ViewWizardData {
	
  /**
   * Paging enabled indicator.
   */
  public boolean isPagingEnabled = false;

  /**
   * Page size.
   */
  public String pageSize = null;

  /**
   * Filter attribute names.
   */
  public String[] filterAttributeNames = null;
  
  /**
   * Column attribute names.
   */
  public String[] columnAttributeNames = null;
  
  /**
	 * Creates a new view wizard data.
	 */
	public ViewWizardData() {
		super();
	}

	/**
	 * Returns the paging enabled indicator.
	 * @return boolean Paging enabled indicator.
	 */
  public boolean getIsPagingEnabled() {
    return isPagingEnabled;
  }

  /**
   * Sets the paging enabled indicator.
   * @param isPagingEnabled Paging enabled indicator.
   */
  public void setPagingEnabled(boolean isPagingEnabled) {
    this.isPagingEnabled = isPagingEnabled;
  }

  /**
   * Returns the page size.
   * @return String Page size.
   */
  public String getPageSize() {
    return pageSize;
  }

  /**
   * Sets the page size.
   * @param pageSize Page size.
   */
  public void setPageSize(String pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Returns the filter attribute names.
   * @return String[] Filter attribute names.
   */
  public String[] getFilterAttributeNames() {
    return filterAttributeNames;
  }

  /**
   * Sets the filter attribute names.
   * @param filterAttributeNames Filter attribute names.
   */
  public void setFilterAttributeNames(String[] filterAttributeNames) {
    this.filterAttributeNames = filterAttributeNames;
  }

  /**
   * Returns the column attribute names.
   * @return String[] Column attribute names.
   */
  public String[] getColumnAttributeNames() {
    return columnAttributeNames;
  }

  /**
   * Sets the column attribute names.
   * @param columnAttributeNames Column attribute names.
   */
  public void setColumnAttributeNames(String[] columnAttributeNames) {
    this.columnAttributeNames = columnAttributeNames;
  }

}