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
package com.bws.jdistil.core.util;

import java.util.Locale;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;

/**
  This class provides a locale specific description for a given value.
  @author - Bryan Snipes
*/
public class Descriptions {

  /**
    Creates a new instance of the Descriptions class.
  */
  private Descriptions() {
    super();
  }

  /**
    Returns a locale specific description for a given value. The original 
    value is returned if a locale specific description is not available.
    @param value Value to describe.
    @param locale Locale.
    @return String Locale specific description or original value if no description exists.
  */
  public static String getDescription(String value, Locale locale) {

    // Initialize description using provided value
    String description = value;
    
    // Attempt to retrieve description resources name from core properties
    String descriptionResources = ResourceUtil.getString(Constants.DESCRIPTION_RESOURCES);

    if (descriptionResources != null) {

      // Attempt to retrieve locale specific description
    	String localeDescription = ResourceUtil.getString(descriptionResources, locale, value);
    	
    	if (!StringUtil.isEmpty(localeDescription)) {
    		description = localeDescription;
    	}
    }
  
    return description;
  }

}
