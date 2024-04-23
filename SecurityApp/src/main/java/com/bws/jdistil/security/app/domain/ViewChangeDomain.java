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
package com.bws.jdistil.security.app.domain;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.domain.Domain;
import com.bws.jdistil.security.domain.DomainManager;

/**
  Displays change user domain view.
  @author Bryan Snipes
*/
public class ViewChangeDomain extends Processor {

  /**
    Creates a new ViewChangeDomain object.
  */
  public ViewChangeDomain() {
    super();
  }

  /**
   * Displays the change domain page.
   */
	public void process(ProcessContext processContext) throws ProcessException {
  	
		// Retrieve all available domains
		List<Domain> domains = findDataObjects(DomainManager.class, processContext);
		
		if (domains != null) {
		
			// Get current session
			HttpServletRequest request = processContext.getRequest();
			
			// Add domains to request attributes
			request.setAttribute(AttributeNames.DOMAINS, domains);
			
    	// Get current session
  		HttpSession session = processContext.getRequest().getSession(true);
			
  		// Get current domain
  		Domain domain = (Domain)session.getAttribute(AttributeNames.DOMAIN);
  		
  		if (domain != null) {

  			// Set current domain ID in request attributes
  			request.setAttribute(FieldIds.DOMAIN_SELECTED_ID, domain.getId());
  		}
		}
		
		// Set change domain as next page
		Page changeDomainPage = ConfigurationManager.getPage(PageIds.CHANGE_DOMAIN);
		processContext.setNextPage(changeDomainPage);
	}
	
}
