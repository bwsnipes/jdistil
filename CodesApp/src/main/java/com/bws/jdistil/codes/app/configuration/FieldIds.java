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
package com.bws.jdistil.codes.app.configuration;

/**
  Class defining all field names used by the codes application classes.
  @author - Bryan Snipes
*/
public class FieldIds extends com.bws.jdistil.codes.configuration.FieldIds {

  public static final String CATEGORY_ID_FILTER = "CDF7";
  public static final String CODE_GROUP_STATE = "CDF8";
  public static final String CODE_CURRENT_PAGE_NUMBER = "CDF9";
  public static final String CODE_SELECTED_PAGE_NUMBER = "CDF10";
  public static final String CODE_SORT_DIRECTION = "CDF11";
  public static final String CODE_SORT_FIELD = "CDF12";
  
  protected FieldIds() {
    super();
  }
  
}
