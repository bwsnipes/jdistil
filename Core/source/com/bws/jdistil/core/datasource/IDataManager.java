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
package com.bws.jdistil.core.datasource;

import java.util.List;

/**
  Interface defining the required methods for all data manager classes.
  @author Bryan Snipes
*/
public interface IDataManager<I, T extends DataObject<I>> {

  /**
    Saves a data object.
    @param dataObject Data object to save.
  */
  public void save(T dataObject) throws DataSourceException;

  /**
    Saves a data object and checks for dirty data based on a given indicator.
    @param dataObject Data object to save.
    @param checkDirty Indicates whether or not to check for dirty data.
  */
  public void save(T dataObject, boolean checkDirty) throws DataSourceException;

  /**
    Deletes a data object.
    @param dataObject Data object.
  */
  public void delete(T dataObject) throws DataSourceException;

  /**
    Returns all data objects.
    @return List List data objects.
  */
  public List<T> find() throws DataSourceException;

  /**
    Returns all data object IDs.
    @return List List data object IDs.
  */
  public List<I> findIds() throws DataSourceException;
  
  /**
    Returns a data object using a given data object ID.
    @param id Data object ID.
    @return DataObject Data object.
  */
  public T find(I id) throws DataSourceException;

  /**
    Returns a list of data objects using a list of data object IDs.
    @param ids List of data object IDs.
    @return List Data objects.
  */
  public List<T> find(List<I> ids) throws DataSourceException;

  /**
    Returns a list of data objects based on specified filter criteria information.
    @param filterCriteria Filter criteria.
    @return List List of data objects.
  */
  public List<T> find(FilterCriteria filterCriteria) throws DataSourceException;
  
  /**
    Returns a list of data object IDs based on specified search criteria information.
    @param filterCriteria Filter criteria.
    @return List List of data object IDs.
  */
  public List<I> findIds(FilterCriteria filterCriteria) throws DataSourceException;
  
}
