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
  Interface used to represent items used as a data filter by the ListField object.
  @author - Bryan Snipes
*/
public interface ListFilter<T extends IListItem> {

  /**
    Returns a value indicating whether or not a specified item 
    should be filtered from a ListField object.
    @param value - Value to check for filtering.
    @return boolean - Filtered indicator.
  */
  public boolean isFiltered(T value);

}
