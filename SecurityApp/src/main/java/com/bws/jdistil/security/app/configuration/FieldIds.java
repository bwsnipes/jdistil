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
package com.bws.jdistil.security.app.configuration;

/**
  Class defining all field names used by the security application classes.
  @author Bryan Snipes
*/
public class FieldIds extends com.bws.jdistil.security.configuration.FieldIds {

  public static final String DOMAIN_SORT_FIELD = "SCF-6";
  public static final String DOMAIN_SORT_DIRECTION = "SCF-7";
  public static final String DOMAIN_CURRENT_PAGE_NUMBER = "SCF-8";
  public static final String DOMAIN_SELECTED_PAGE_NUMBER = "SCF-9";
  public static final String DOMAIN_GROUP_STATE = "SCF-10";
  public static final String DOMAIN_NAME_FILTER = "SCF-11";
  public static final String DOMAIN_NAME_FILTER_OPERATOR = "SCF-12";
  public static final String DOMAIN_SELECTED_ID = "SCF-13";
  
  public static final String USER_AUTHENTICATION_ID = "SCF24";
  public static final String USER_AUTHENTICATION_PASSWORD = "SCF25";
  public static final String USER_AUTHENTICATION_NEW_PASSWORD = "SCF26";
  public static final String USER_AUTHENTICATION_CONFIRM_PASSWORD = "SCF27";
  public static final String USER_AUTHENTICATION_DOMAIN_ID = "SCF28";
  public static final String FIRST_NAME_FILTER = "SCF29";
  public static final String FIRST_NAME_FILTER_OPERATOR = "SCF30";
  public static final String LAST_NAME_FILTER = "SCF31";
  public static final String LAST_NAME_FILTER_OPERATOR = "SCF32";
  public static final String USER_GROUP_STATE = "SCF33";
  public static final String USER_CURRENT_PAGE_NUMBER = "SCF34";
  public static final String USER_SELECTED_PAGE_NUMBER = "SCF35";
  public static final String USER_SORT_DIRECTION = "SCF36";
  public static final String USER_SORT_FIELD = "SCF37";
  public static final String ROLE_SORT_DIRECTION = "SCF38";
  public static final String ROLE_SORT_FIELD = "SCF39";
  
  protected FieldIds() {
    super();
  }
  
}
