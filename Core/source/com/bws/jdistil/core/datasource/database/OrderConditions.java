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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  Class representing multiple SQL order conditions.
  @author - Bryan Snipes
*/
public class OrderConditions implements IOrderCondition {

  /**
    List of order condition.
  */
  private List<OrderCondition> values = new ArrayList<OrderCondition>();

  /**
    Creates a new OrderConditions object using an order condition.
    @param orderCondition - Order condition.
  */
  public OrderConditions(OrderCondition orderCondition) {

    // Add order condition
    values.add(orderCondition);
  }

  /**
    Adds an order condition.
    @param orderCondition - Value condition.
  */
  public void add(OrderCondition orderCondition) {

    // Add order condition
    if (orderCondition != null) {
      values.add(orderCondition);
    }
  }

  /**
    Returns the SQL text represented by the order conditions using a map of aliases.
    @see com.bws.jdistil.core.datasource.database.ISqlGenerator#generateSql
  */
  public String generateSql(Map<String, String> aliases) {

    // Initialize return value
    StringBuffer sqlText = new StringBuffer();

    for (OrderCondition orderCondition : values) {

      // Append condition
      if (sqlText.length() == 0) {
        sqlText.append(orderCondition.generateSql(aliases));
      }
      else {
        sqlText.append(", ").append(orderCondition.generateSql(aliases));
      }
    }

    return sqlText.toString();
  }

  /**
		Returns a value indicating whether or not the order condition references a given table name.
	  @param tableName Table name.
	  @return boolean Referenced table indicator.
	*/
	public boolean isTableReferenced(String tableName) {
	
		// Initialize return value
		boolean isTableReferenced = false;
		
    for (OrderCondition orderCondition : values) {

      if (orderCondition.isTableReferenced(tableName)) {
      	
        // Set table referenced indicator and stop processing
      	isTableReferenced = true;
      	break;
      }
    }

    return isTableReferenced;
	}
}
