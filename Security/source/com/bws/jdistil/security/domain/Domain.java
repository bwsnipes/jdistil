package com.bws.jdistil.security.domain;

import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.tag.data.IListItem;

/**
 * Data object used to store domain data.
 */
public class Domain extends DataObject<Integer> implements IDomain, IListItem, Comparable<Domain> {

  /**
    Serial version UID.
  */
	private static final long serialVersionUID = 1513848342013425507L;

	/**
   * Name.
   */
  private String name = null;  

  /**
   * Default datasource indicator.
   */
  private Boolean isDefaultDatasource = Boolean.FALSE;

  /**
   * Datasource name.
   */
  private String datasourceName = null;  

  /**
     Deleted indicator.
   */
  private boolean isDeleted;
  
  /**
   * Creates a new domain object.
   */
  public Domain() {
    super();
  }

  /**
   * Returns the name.
   * @return String Name.
   * @see IDomain#getClass()
   */
  @Override
  public String getName() {
    return name;
  }
  
  /**
   * Sets the name.
   * @param name Name.
   */
  public void setName(String name) {
    updateModifiedStatus(this.name, name);
    this.name = name;
  }

  /**
   * Returns the default datasource indicator.
   * @return Boolean Default datasource indicator.
  */
  public Boolean getIsDefaultDatasource() {
    return isDefaultDatasource;
  }
  
  /**
   * Sets the default datasource indicator.
   * @param isDefaultDatasource Default datasource indicator.
  */
  public void setIsDefaultDatasource(Boolean isDefaultDatasource) {
    updateModifiedStatus(this.isDefaultDatasource, isDefaultDatasource);
    this.isDefaultDatasource = isDefaultDatasource;
  }

  /**
   * Returns the datasource name.
   * @return String Datasource name.
   * @see IDomain#getDatasourceName()
   */
  @Override
  public String getDatasourceName() {
    return datasourceName;
  }
  
  /**
   * Sets the datasource name.
   * @param datasourceName Datasource name.
   */
  public void setDatasourceName(String datasourceName) {
    updateModifiedStatus(this.datasourceName, datasourceName);
    this.datasourceName = datasourceName;
  }

  /**
    Returns the default value indicator.
    @see IListItem#getIsDefault()
  */
  public Boolean getIsDefault() {
  	return Boolean.FALSE;
  }
  
  /**
   * Returns the deleted indicator.
   * @see IListItem#getIsDeleted()
  */
  public Boolean getIsDeleted() {
    return isDeleted;
  }

  /**
   * Sets the deleted indicator.
   * @param isDeleted - Deleted indicator.
  */
  public void setIsDeleted(boolean isDeleted) {
    updateModifiedStatus(this.isDeleted, isDeleted);
    this.isDeleted = isDeleted;
  }

  /**
    Returns the user ID as the item value.
    @see IListItem#getValue()
  */
  public Object getValue() {
    return getId();
  }
  
  /**
    Returns the domain's name as the item description.
    @see IListItem#getDescription()
  */
  public String getDescription() {
    return name;
  }
  
  /**
    Compares two domain objects based on their names.
    @see Comparable#compareTo(Object)
  */
  public int compareTo(Domain domain) {
  
    // Initialize return value
    int status = 0;
  
    if (domain != null) {
  
      // Get user descriptions
      String description1 = getDescription();
      String description2 = domain.getDescription();
  
      // Compare user descriptions
      if (description1 != null && description2 == null) {
        status = 1;
      }
      else if (description1 == null && description2 != null) {
        status = -1;
      }
      else if (description1 != null && description2 != null) {
        status = description1.compareTo(description2);
      }
    }
  
    return status;
  }

}
