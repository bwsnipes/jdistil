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
package com.bws.jdistil.core.security;

/**
  Interface defining domain specific information.
  @author Bryan Snipes
*/
public interface IDomain {

  /**
    Default domain ID.
  */
  public static final Integer DEFAULT_ID = new Integer(0);

  /**
    Default domain name.
  */
  public static final String DEFAULT_NAME = "Default";
	
  /**
    Returns the unique domain ID.
    @return Integer Unique domain ID.
  */
  public Integer getId();

  /**
    Returns the name.
    @return String name.
  */
  public String getName();

  /**
	  Returns the data source name.
	  @return String Data source name.
	*/
	public String getDataSourceName();

}
