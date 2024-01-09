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

/**
  Exception class defined to handle dirty update notifications caused by an
  attempt to update data that has changed since the last data retrieval.
  @author Bryan Snipes
*/
public class DirtyUpdateException extends DataSourceException {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -978023033376404456L;
  
  /**
    Data object containing the most recent data.
  */
  private DataObject<?> mostRecentData = null;

  /**
    Creates a DirtyUpdateException object given a data object containing the
    most recent data.
    @param data - Most recent data.
  */
  public DirtyUpdateException(DataObject<?> data) {
    super("Data has been changed since the last data retrieval.");
    mostRecentData = data;
  }

  /**
    Creates a DirtyUpdateException object given a data object containing the
    most recent data and a specific message used to create the exception text.
    @param data - Most recent data.
    @param message - Specific dirty update message.
  */
  public DirtyUpdateException(DataObject<?> data, String message) {
    super(message);
    mostRecentData = data;
  }

  /**
    Returns a data object containing the most recent copy of data that caused
    the dirty update exception.&nbsp; This is a read only property that can only
    be set when the exception is created.
    @return DataObject - Most recent data.
  */
  public DataObject<?> getData() {
    return mostRecentData;
  }

}
