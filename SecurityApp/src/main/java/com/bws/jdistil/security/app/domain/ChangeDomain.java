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

import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.core.security.IDomain;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.Descriptions;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.domain.Domain;
import com.bws.jdistil.security.domain.DomainManager;

/**
  Handles changing a user's domain.
  @author Bryan Snipes
*/
public class ChangeDomain extends Processor {

  /**
    Creates a new ChangeDomain object.
  */
  public ChangeDomain() {
    super();
  }

  /**
   * Handles changing a user's domain.
   * @param processContext Process context.
   */
  @Override
	public void process(ProcessContext processContext) throws ProcessException {
		
    // Check for errors before changing domain
    if (!processContext.getErrorMessages().isEmpty()) {
    	
			// Return to current page
			processContext.setNextPage(processContext.getCurrentPage());
    }
    else {
    	
    	// Get current session
  		HttpSession session = processContext.getRequest().getSession(true);

  		// Remove domain from session
			session.removeAttribute(AttributeNames.DOMAIN);

			// Get submitted domain information
  		Integer domainId = ParameterExtractor.getInteger(processContext.getRequest(), FieldIds.DOMAIN_SELECTED_ID);
  		
  		if (domainId != null && !domainId.equals(IDomain.DEFAULT_ID)) {
  			
    		// Retrieve domain
    		Domain domain = findDataObject(DomainManager.class, domainId, processContext);
    		
    		if (domain != null) {
    			
    			// Store user in session
    			session.setAttribute(AttributeNames.DOMAIN, domain);

    			// Handle successful domain change
    			handleSuccess(processContext);
    		}
    		else {
    
      		// Get current locale
      		Locale locale = processContext.getRequest().getLocale();
      
  	  		// Get incorrect domain error message message
          String errorMessage = Descriptions.getDescription("Invalid domain.", locale);

          // Create process error message and add to process context
    			ProcessMessage processMessage = new ProcessMessage(ProcessMessage.ERROR, errorMessage);
    			processContext.addMessage(processMessage);
    			
    			// Return to current page
    			processContext.setNextPage(processContext.getCurrentPage());
  			}
  		}
  		else {
  			
  			// Handle successful domain change
  			handleSuccess(processContext);
  		}
		}
	}

	/**
	 * Default implementation for handling a successful domain change which can be overridden by descendant classes.
	 * Navigates to the application defined welcome page.
	 * @param processContext Process context.
	 */
	protected void handleSuccess(ProcessContext processContext) throws ProcessException {
		
		// Get welcome action
		Action welcomeAction = ConfigurationManager.getWelcomeAction();
		
		if (welcomeAction != null) {

			// Forward processing to welcome action
			forward(welcomeAction, processContext);
		}
		else {
			
			throw new ProcessException("Error navigating to welcome page: No welcome page action defined.");
		}
	}
	
}
