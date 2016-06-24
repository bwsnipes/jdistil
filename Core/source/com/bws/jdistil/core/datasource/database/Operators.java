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
package com.bws.jdistil.core.datasource.database;

import java.util.Locale;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;

/**
  Static class representing SQL condition operators.
  @author - Bryan Snipes
*/
public class Operators {

  /**
    Equals operator constant.
  */
  public static final String EQUALS = " = ";

  /**
    Not equals operator constant.
  */
  public static final String NOT_EQUALS = " <> ";

  /**
    Like operator constant.
  */
  public static final String LIKE = " like ";
  
  /**
    Begins with operator constant. Specific use of the 'like' operator.
  */
  public static final String BEGINS_WITH = "XXX*";
  
  /**
    Ends with operator constant. Specific use of the 'like' operator.
  */
  public static final String ENDS_WITH = "*XXX";
  
  /**
    Contains operator constant. Specific use of the 'like' operator.
  */
  public static final String CONTAINS = "*XXX*";
  
  /**
    Greater than operator constant.
  */
  public static final String GREATER_THAN = " > ";

  /**
    Greater than or equal operator constant.
  */
  public static final String GREATER_THAN_OR_EQUAL = " >= ";

  /**
    Less than operator constant.
  */
  public static final String LESS_THAN = " < ";

  /**
    Less than or equal operator constant.
  */
  public static final String LESS_THAN_OR_EQUAL = " <= ";

  /**
    Null value operator constant.
  */
  public static final String IS_NULL = " is null ";
  
  /**
    Not null value operator constant.
  */
  public static final String IS_NOT_NULL = " is not null ";
  
  /**
    Logical 'And' operator constant.
  */
  public static final String AND = " and ";

  /**
    Logical 'Or' operator constant.
  */
  public static final String OR = " or ";

  /**
    Creates a new Operators object.
  */
  private Operators() {
    super();
  }

  /**
    Returns a value indicating whether or not a given operator is valid.
    @param operator Operator.
    @return boolean Valid operator indicator.
  */
  public static boolean isValid(String operator) {
    return isValidComparative(operator) || isValidLogical(operator);
  }

  /**
    Returns a value indicating whether or not a given operator is a valid comparative operator.
    @param operator Operator.
    @return boolean Valid comparative operator indicator.
  */
  public static boolean isValidComparative(String operator) {
    return operator != null &&
        (operator.equalsIgnoreCase(EQUALS) || operator.equalsIgnoreCase(NOT_EQUALS) ||
        operator.equalsIgnoreCase(GREATER_THAN) || operator.equalsIgnoreCase(GREATER_THAN_OR_EQUAL) ||
        operator.equalsIgnoreCase(LESS_THAN) || operator.equalsIgnoreCase(LESS_THAN_OR_EQUAL) ||
        operator.equalsIgnoreCase(LIKE) || operator.equalsIgnoreCase(BEGINS_WITH) || 
        operator.equalsIgnoreCase(ENDS_WITH) || operator.equalsIgnoreCase(CONTAINS) || 
        operator.equalsIgnoreCase(IS_NULL) || operator.equalsIgnoreCase(IS_NOT_NULL));
  }

  /**
    Returns a value indicating whether or not a given operator is a valid logical operator.
    @param operator Operator.
    @return boolean Valid logical operator indicator.
  */
  public static boolean isValidLogical(String operator) {
    return operator != null && (operator.equalsIgnoreCase(AND) || operator.equalsIgnoreCase(OR));
  }

  /**
    Returns a value indicating whether or not a given operator is a null comparative operator.
    @param operator Operator.
    @return boolean Valid null comparative operator indicator.
  */
  public static boolean isNullComparative(String operator) {
    return operator != null && operator.equalsIgnoreCase(IS_NULL) || operator.equalsIgnoreCase(IS_NOT_NULL);
  }
  
  /**
	  Returns a locale specific description for a given operator type.
	  @param type Operator type.
	  @param locale Target locale.
	  @return String Locale specific operator description.
	*/
	public static String getDescription(String type, Locale locale) {

    // Initialize description
    String description = null;
    
    if (isValid(type)) {
    	
      // Attempt to retrieve operator resources name from core properties
      String operatorResources = ResourceUtil.getString(Constants.OPERATOR_RESOURCES);

      if (operatorResources != null) {

        // Attempt to retrieve locale specific description
      	description = ResourceUtil.getString(operatorResources, locale, type);
      }

      if (StringUtil.isEmpty(description)) {
        	
        // Retrieve default description
      	description = ResourceUtil.getString(OperatorResource.class.getName(), locale, type);
      }
    }
  
    return description;
	}
	
}
