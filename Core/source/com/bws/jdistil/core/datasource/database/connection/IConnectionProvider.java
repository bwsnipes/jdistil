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

import com.bws.jdistil.core.datasource.DataSourceException;

public interface IConnectionProvider {

	/**
	 * Returns a connection from a JNDI connection pool. 
	 * @param dataSourceName JNDI data source name.
	 * @return Connection Database connection.
	 * @throws DataSourceException
	 */
	public Connection openConnection(String dataSourceName) throws DataSourceException;

}
