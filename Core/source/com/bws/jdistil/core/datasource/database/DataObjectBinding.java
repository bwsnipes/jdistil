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
package com.bws.jdistil.core.datasource.database;

import com.bws.jdistil.core.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  Defines binding information for a data object. This includes a data object class,
  table name, column bindings for each data object property and possibly one or more
  dependent property and associate bindings.
  @author Bryan Snipes
*/
public class DataObjectBinding {

  /**
    Data object class.
  */
  private Class<?> dataObjectClass = null;

  /**
    Table name.
  */
  private String tableName = null;

  /**
    Parent ID column binding.
  */
  private IdColumnBinding parentIdColumnBinding = null;

  /**
    ID column binding.
  */
  private IdColumnBinding idColumnBinding = null;

  /**
    List of column bindings.
  */
  private List<ColumnBinding> columnBindings = new ArrayList<ColumnBinding>();

  /**
    Map of dependent bindings keyed by property name.
  */
  private Map<String, DependentBinding> dependentBindings = new HashMap<String, DependentBinding>();

  /**
    Map of associate bindings keyed by property name.
  */
  private Map<String, AssociateBinding> associateBindings = new HashMap<String, AssociateBinding>();

  /**
    Map of column bindings keyed by column name.
  */
  private Map<String, ColumnBinding> columnLookup = new HashMap<String, ColumnBinding>();

  /**
    Map of column bindings keyed by property name.
  */
  private Map<String, ColumnBinding> propertyLookup = new HashMap<String, ColumnBinding>();

  /**
    Creates a new DataObjectBinding object.
    @param dataObjectClass Data object class.
    @param tableName Table name.
    @param idColumnBinding ID column binding.
    @param columnBindings List of column bindings.
    @param dependentBindings List of dependent bindings.
    @param associateBindings List of associate bindings.
  */
  public DataObjectBinding(Class<?> dataObjectClass, String tableName, IdColumnBinding idColumnBinding,
  		Collection<ColumnBinding> columnBindings, Collection<DependentBinding> dependentBindings, Collection<AssociateBinding> associateBindings) {

    this(dataObjectClass, tableName, null, idColumnBinding, columnBindings, dependentBindings, associateBindings);
  }

  /**
    Creates a new DataObjectBinding object.
    @param dataObjectClass Data object class.
    @param tableName Table name.
    @param parentIdColumnBinding Parent ID column binding.
    @param idColumnBinding ID column binding.
    @param columnBindings List of column bindings.
    @param dependentBindings List of dependent bindings.
    @param associateBindings List of associate bindings.
  */
  public DataObjectBinding(Class<?> dataObjectClass, String tableName,
      IdColumnBinding parentIdColumnBinding, IdColumnBinding idColumnBinding,
      Collection<ColumnBinding> columnBindings, Collection<DependentBinding> dependentBindings,
      Collection<AssociateBinding> associateBindings) {

    super();

    // Validate required parameters
    if (dataObjectClass == null) {
      throw new IllegalArgumentException("Invalid null data object class.");
    }
    if (StringUtil.isEmpty(tableName)) {
      throw new IllegalArgumentException("Invalid null table name.");
    }
    if (idColumnBinding == null) {
      throw new IllegalArgumentException("Invalid null ID column binding.");
    }
    if (columnBindings == null || columnBindings.size() == 0) {
      throw new IllegalArgumentException("Invalid null column bindings.");
    }

    // Set required properties
    this.dataObjectClass = dataObjectClass;
    this.tableName = tableName;
    this.parentIdColumnBinding = parentIdColumnBinding;
    this.idColumnBinding = idColumnBinding;
    this.columnBindings.addAll(columnBindings);

    if (associateBindings != null) {
    	
      // Add all associate bindings
    	for (AssociateBinding associateBinding : associateBindings) {
        this.associateBindings.put(associateBinding.getPropertyName(), associateBinding);
    	}
    }
    
    if (dependentBindings != null) {

      // Add all dependent bindings
    	for (DependentBinding dependentBinding : dependentBindings) {
        this.dependentBindings.put(dependentBinding.getPropertyName(), dependentBinding);
    	}
    }

    // Populate lookups
    for (ColumnBinding columnBinding : columnBindings) {
      columnLookup.put(columnBinding.getColumnName(), columnBinding);
      propertyLookup.put(columnBinding.getPropertyName(), columnBinding);
    }
  }

  /**
    Returns the data object class.
    @return Class Data object class.
  */
  public Class<?> getDataObjectClass() {
    return dataObjectClass;
  }

  /**
    Returns the table name.
    @return String Table name.
  */
  public String getTableName() {
    return tableName;
  }

  /**
    Returns the parent ID column binding.
    @return IdColumnBinding Parent ID column binding.
  */
  public IdColumnBinding getParentIdColumnBinding() {
    return parentIdColumnBinding;
  }

  /**
    Returns the ID column binding.
    @return IdColumnBinding ID column binding.
  */
  public IdColumnBinding getIdColumnBinding() {
    return idColumnBinding;
  }

  /**
    Returns a collection of column bindings.
    @return Collection Collection of column bindings.
  */
  public Collection<ColumnBinding> getColumnBindings() {
    return columnBindings;
  }

  /**
    Returns a collection of dependent bindings.
    @return Collection Collection of dependent bindings.
  */
  public Collection<DependentBinding> getDependentBindings() {
    return dependentBindings.values();
  }

  /**
    Returns a collection of associate bindings.
    @return Collection Collection of associate bindings.
  */
  public Collection<AssociateBinding> getAssociateBindings() {
    return associateBindings.values();
  }

  /**
	  Returns a column binding for a given column name.
	  @param columnName Column name.
	  @return ColumnBinding Column binding.
	*/
	public ColumnBinding getColumnBindingByColumnName(String columnName) {
	  return columnLookup.get(columnName);
	}
	
	/**
	  Returns a column binding for a given property name.
	  @param propertyName Property name.
	  @return ColumnBinding Column binding.
	*/
	public ColumnBinding getColumnBindingByPropertyName(String propertyName) {
	  return propertyLookup.get(propertyName);
	}
	
	/**
	  Returns an associate binding for a given property name.
	  @param propertyName Property name.
	  @return AssociateBinding Associate binding.
	*/
	public AssociateBinding getAssociateBindingByPropertyName(String propertyName) {
	  return associateBindings.get(propertyName);
	}
	
	/**
	  Returns an dependent binding for a given property name.
	  @param propertyName Property name.
	  @return DependentBinding Dependent binding.
	*/
	public DependentBinding getDependentBindingByPropertyName(String propertyName) {
	  return dependentBindings.get(propertyName);
	}
	
}
