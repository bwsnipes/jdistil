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
package com.bws.jdistil.core.tag.data;

/**
  Interface used to represent items used as reference data by the ListField object.
  @author - Bryan Snipes
*/
public interface IListItem  {

  /**
    Returns the option value.
    @return Object - Option value.
  */
  public Object getValue();

  /**
    Returns the option description.
    @return String - Option description.
  */
  public String getDescription();

  /**
    Returns the default value indicator.
    @return Boolean - Default value indicator.
  */
  public Boolean getIsDefault();

  /**
    Returns the deleted indicator.
    @return Boolean - Deleted indicator.
  */
  public Boolean getIsDeleted();
  
}
