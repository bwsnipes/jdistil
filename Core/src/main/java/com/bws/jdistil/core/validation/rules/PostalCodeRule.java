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
package com.bws.jdistil.core.validation.rules;

/**
  Class used to validate postal code fields.
  @author - Bryan Snipes
*/
public class PostalCodeRule extends RegularExpressionRule {

	public static final String POSTAL_CODE_PATTERN = "^\\d{5}(-\\d{4})?$";
	 
	public static final String POSTAL_CODE_DISPLAY_FORMAT = "##### or #####-####";

	/**
    Creates an PostalCodeRule object.
  */
  public PostalCodeRule() {
    super(POSTAL_CODE_PATTERN, POSTAL_CODE_DISPLAY_FORMAT);
  }

}
