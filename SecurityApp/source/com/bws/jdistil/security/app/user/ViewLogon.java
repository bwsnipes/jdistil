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

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.domain.DomainManager;

/**
  Displays user logon view.
  @author Bryan Snipes
*/
public class ViewLogon extends Processor {

  /**
    Creates a new ViewLogon object.
  */
  public ViewLogon() {
    super();
  }

	public void process(ProcessContext processContext) throws ProcessException {

		// Load all domains as reference data
		loadReferenceData(DomainManager.class, AttributeNames.DOMAINS, processContext);
		
		// Set logon as next page
		Page logonPage = ConfigurationManager.getPage(PageIds.LOGON);
		processContext.setNextPage(logonPage);
	}

}
