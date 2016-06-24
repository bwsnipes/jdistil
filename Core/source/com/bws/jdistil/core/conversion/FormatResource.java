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

import java.util.ListResourceBundle;

/**
  Resource bundle class used to define all default formats used by the core converters.
  @author - Bryan Snipes
*/
public class FormatResource extends ListResourceBundle {

  /**
    Two dimensional array of message keys and formats.
  */
  static final Object[][] contents = {
    {Formats.DATE_FORMAT, "MM/dd/yyyy"},
    {Formats.TIME_FORMAT, "HH:mm"},
    {Formats.DATE_TIME_FORMAT, "MM/dd/yyyy hh:mm a"},
    {Formats.NUMBER_FORMAT, "#,###"},
    {Formats.DECIMAL_FORMAT, "#,##0.00"},
    {Formats.CURRENCY_FORMAT, "#,##0.00"},
    {Formats.PERCENTAGE_FORMAT, "0.00%"}
  };

  /**
    Returns all standard messages as a two dimensional array of format keys
    and formats.
    @return Object[][] - Two dimensional array of format keys and formats.
  */
  public Object[][] getContents() {
    return contents;
  }

}
