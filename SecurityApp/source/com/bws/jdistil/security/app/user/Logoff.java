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
package com.bws.jdistil.security.app.user;

import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.security.configuration.AttributeNames;

/**
  Handles user logoff.
  @author Bryan Snipes
*/
public class Logoff extends Processor {

  /**
    Creates a new Logoff object.
  */
  public Logoff() {
    super();
  }

	public void process(ProcessContext processContext) throws ProcessException {
		
		// Get current session
		HttpSession session = processContext.getRequest().getSession(true);
		
		// Store user and roles from session
		session.removeAttribute(AttributeNames.USER);
		session.removeAttribute(AttributeNames.ROLES);
	
		// Handle successful logoff
		handleSuccess(processContext);
	}

	/**
	 * Default implementation for handling a successful logoff which can be overriden by descendant classes.
	 * Navigates to the application defined welcome page.
	 * @param processContext Process context.
	 */
	protected void handleSuccess(ProcessContext processContext) {
		
		// Set welcome page as next page 
		processContext.setNextPage(ConfigurationManager.getWelcomePage());
	}

}
