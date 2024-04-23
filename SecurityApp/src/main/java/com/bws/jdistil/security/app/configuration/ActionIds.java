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
  Interface defining all security application action IDs.
  @author - Bryan Snipes
*/
public class ActionIds extends com.bws.jdistil.core.configuration.ActionIds {

	public static final String VIEW_DOMAINS = "SCA-1";
	public static final String VIEW_DOMAIN_PREVIOUS_PAGE = "SCA-2";
	public static final String VIEW_DOMAIN_SELECT_PAGE = "SCA-3";
	public static final String VIEW_DOMAIN_NEXT_PAGE = "SCA-4";
	public static final String DELETE_DOMAIN = "SCA-5";
	public static final String ADD_DOMAIN = "SCA-6";
	public static final String EDIT_DOMAIN = "SCA-7";
	public static final String SAVE_DOMAIN = "SCA-8";
	public static final String CANCEL_DOMAIN = "SCA-9";
	public static final String VIEW_CHANGE_DOMAIN = "SCA-10";
	public static final String CHANGE_DOMAIN = "SCA-11";
	
	public static final String VIEW_LOGON = "SCA1";
	public static final String LOGON = "SCA2";
	public static final String LOGOFF = "SCA3";
	public static final String VIEW_CHANGE_PASSWORD = "SCA4";
	public static final String CHANGE_PASSWORD = "SCA5";
	public static final String VIEW_ROLES = "SCA6";
	public static final String DELETE_ROLE = "SCA7";
	public static final String ADD_ROLE = "SCA8";
	public static final String EDIT_ROLE = "SCA9";
	public static final String SELECT_GROUP = "SCA10";
	public static final String SAVE_ROLE = "SCA11";
	public static final String CANCEL_ROLE = "SCA12";
	public static final String VIEW_USERS = "SCA13";
	public static final String VIEW_USER_PREVIOUS_PAGE = "SCA14";
	public static final String VIEW_USER_SELECT_PAGE = "SCA15";
	public static final String VIEW_USER_NEXT_PAGE = "SCA16";
	public static final String DELETE_USER = "SCA17";
	public static final String ADD_USER = "SCA18";
	public static final String EDIT_USER = "SCA19";
	public static final String SAVE_USER = "SCA20";
	public static final String CANCEL_USER = "SCA21";
	
	protected ActionIds() {
		super();
	}

}
