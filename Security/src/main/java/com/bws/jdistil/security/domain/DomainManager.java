package com.bws.jdistil.security.domain;

import com.bws.jdistil.core.datasource.database.AssociateBinding;
import com.bws.jdistil.core.datasource.database.BoundDatabaseDataManager;
import com.bws.jdistil.core.datasource.database.ColumnBinding;
import com.bws.jdistil.core.datasource.database.DataObjectBinding;
import com.bws.jdistil.core.datasource.database.DbUtil;
import com.bws.jdistil.core.datasource.database.IdColumnBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain manager class used to retrieve domain data objects.
 */
public class DomainManager extends BoundDatabaseDataManager<Integer, Domain> {

  /**
   * Creates a new domain manager object.
   */
  public DomainManager() {
    super();
  }

  /**
   * Creates and returns a data object binding.
   * @see BoundDatabaseDataManager#createDataObjectBinding()
   */
  protected DataObjectBinding createDataObjectBinding() {

    // Set table name
    String tableName = "bws_domain";

    // Create ID column binding
    IdColumnBinding idColumnBinding = new IdColumnBinding("domain_id");

    // Create and populate column bindings
    List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();
    columnBindings.add(new ColumnBinding("name", DbUtil.STRING, false, false, "Name"));
    columnBindings.add(new ColumnBinding("is_default_datasource", DbUtil.BOOLEAN, false, false, "IsDefaultDatasource"));
    columnBindings.add(new ColumnBinding("datasource_name", DbUtil.STRING, false, false, "DatasourceName"));
    columnBindings.add(new ColumnBinding("is_deleted", DbUtil.BOOLEAN, false, true, "IsDeleted"));
    columnBindings.add(new ColumnBinding("version", DbUtil.LONG, false, false, "Version"));
    
    // Create associate bindings list
    List<AssociateBinding> associateBindings = new ArrayList<AssociateBinding>();


    // Create domain binding
    DataObjectBinding binding = new DataObjectBinding(Domain.class, tableName, idColumnBinding, columnBindings, null, associateBindings);

    return binding;
  }
  
  /**
   * Disabling domain awareness since these are the actual domains being managed.
   * @see com.bws.jdistil.core.datasource.database.DatabaseDataManager#isDomainAware()
   */
  @Override
  protected boolean isDomainAware() {
  	return false;
  }
  
}
