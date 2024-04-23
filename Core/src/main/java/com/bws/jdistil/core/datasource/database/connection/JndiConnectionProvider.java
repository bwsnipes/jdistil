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
package com.bws.jdistil.core.datasource.database.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;

/**
 * Connection provider that provides database connections from a JNDI connection pool.
 * @author - Bryan Snipes
 */
public class JndiConnectionProvider implements IConnectionProvider {

	/**
	 * Returns a connection from a JNDI connection pool. 
	 * @param dataSourceName JNDI data source name.
	 * @return Connection Database connection.
	 * @throws DataSourceException
	 */
	@Override
	public Connection openConnection(String dataSourceName) throws DataSourceException {
		
    // Set method name
    String methodName = "openConnection";

    // Initialize return value
    Connection connection = null;

    // Retrieve context factory name from core property file
    String contextFactory = ResourceUtil.getString(Constants.INITIAL_CONTEXT_FACTORY);

    // Create context properties
    Properties properties = new Properties();
    
    // Set context factory property
    if (!StringUtil.isEmpty(contextFactory)) {
      properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
    }

    try {
      // Create initial context
      InitialContext initialContext = new InitialContext(properties);

      // Retrieve data source
      DataSource dataSource = (DataSource)initialContext.lookup(dataSourceName);

      // Open connection
      connection = dataSource.getConnection();
    }
    catch (NamingException namingException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.connection");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Opening DB Connection", namingException);

      throw new DataSourceException(methodName + ": " + namingException.getMessage());
    }
    catch (SQLException sqlException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.datasource.database.connection");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Opening DB Connection", sqlException);

      throw new DataSourceException(methodName + ": " + sqlException.getMessage());
    }

    return connection;
	}
	
}
