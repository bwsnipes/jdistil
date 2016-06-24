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
package com.bws.jdistil.core.conversion;

import java.util.Locale;

/**
  Interface defining methods required by all converters. Converters must
  support formatting specific object types as locale specific string values
  and parsing locale specific string values back into their original object 
  types. It is also possible to have converters that are not affected by locale.
  @author - Bryan Snipes
*/
public interface IConverter {

  /**
	  Returns a pattern used for conversion if one is used.
	  @param locale - Locale.
	  @return String - Pattern used for conversion.
	*/
	public String getPattern(Locale locale);

  /**
    Returns a string representation of a given object in a specified locale.
    @param object - Object to format.
    @param locale - Locale.
    @return String - String representation of object.
  */
  public String format(Object object, Locale locale);
  
  /**
    Returns an object created from a given string value for a specified locale..
    @param value - String value.
    @param locale - Locale.
    @return Object - Object created from string value.
  */
  public Object parse(String value, Locale locale);

}
