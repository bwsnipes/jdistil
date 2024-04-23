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

import java.util.Locale;
import java.util.List;
import java.util.Map;

/**
  Interface specifying methods to implement when defining classes capable of
  performing action data validation.
  @author Bryan Snipes
*/
public interface IActionRule {

  /**
    Validates action data and adds a formatted error message to a given list of messages.
    @param actionId - Action ID.
    @param data - Map of action data.
    @param locale - Locale.
    @param messages - List of error messages.
    @return boolean - Valid action data indicator.
  */
  public boolean isValid(String actionId, Map<String, String[]> data, Locale locale, List<String> messages);

}

