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
package com.bws.jdistil.codes.configuration;

/**
  Class defining all field names used by the codes classes.
  @author - Bryan Snipes
*/
public class FieldIds extends com.bws.jdistil.core.configuration.FieldIds {
  
  public static final String CATEGORY_ID = "CDF1";
  public static final String CATEGORY_NAME = "CDF2";
  public static final String CODE_ID = "CDF3";
  public static final String CODE_NAME = "CDF4";
  public static final String IS_DEFAULT_CODE = "CDF5";
  public static final String CODE_VERSION = "CDF6";
  
  protected FieldIds() {
    super();
  }
  
}
