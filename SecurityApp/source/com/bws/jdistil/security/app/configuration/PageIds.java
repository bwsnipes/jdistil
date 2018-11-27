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
  Class defining all security application page IDs.
  @author Bryan Snipes
*/
public class PageIds extends com.bws.jdistil.core.configuration.PageIds {

  public static final String LOGON = "SCP1";
  public static final String CHANGE_PASSWORD = "SCP2";
  public static final String CHANGE_DOMAIN = "SCP3";
	public static final String DOMAINS = "SCP4";
	public static final String DOMAIN = "SCP5";
  public static final String ROLES = "SCP6";
  public static final String ROLE = "SCP7";
  public static final String USERS = "SCP8";
  public static final String USER = "SCP9";

  protected PageIds() {
    super();
  }

}
