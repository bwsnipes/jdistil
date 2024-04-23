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
  Interface defining all codes application action IDs.
  @author - Bryan Snipes
*/
public class ActionIds extends com.bws.jdistil.core.configuration.ActionIds {

  public static final String VIEW_CODES = "CDA1";
  public static final String VIEW_CODES_PREVIOUS_PAGE = "CDA2";
  public static final String VIEW_CODES_SELECT_PAGE = "CDA3";
  public static final String VIEW_CODES_NEXT_PAGE = "CDA4";
  public static final String DELETE_CODE = "CDA5";
  public static final String ADD_CODE = "CDA6";
  public static final String EDIT_CODE = "CDA7";
  public static final String SAVE_CODE = "CDA8";
  public static final String CANCEL_CODE = "CDA9";

  protected ActionIds() {
  	super();
  }

}
