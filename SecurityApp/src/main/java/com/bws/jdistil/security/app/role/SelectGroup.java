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
package com.bws.jdistil.security.app.role;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.Processor;
import com.bws.jdistil.core.servlet.Loader;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.servlet.ServletException;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.role.Field;
import com.bws.jdistil.security.role.FieldManager;
import com.bws.jdistil.security.role.GroupManager;
import com.bws.jdistil.security.role.Role;
import com.bws.jdistil.security.role.TaskManager;

/**
  Handles the selection of a different group by updating
  a role stored in the session and retrieving the dependent
  data needed to edit a role.
  @author Bryan Snipes
*/
public class SelectGroup extends Processor {

  /**
    Creates a new SelectGroup object.
  */
  public SelectGroup() {
    super();
  }

  /**
    Updates a role stored in the session and retrieves dependent data needed to edit a role.
    @see com.bws.jdistil.core.process.IProcessor#process
  */
  public void process(ProcessContext processContext) throws ProcessException {

  	// Set method name
  	String methodName = "process";
  	
    // Get current session
    HttpSession session = processContext.getRequest().getSession(true);
    
    // Retrieve role from session
    Role role = (Role)session.getAttribute(AttributeNames.ROLE);

    try {
    	
      // Populate role
      Loader.load(processContext.getSecurityManager(), processContext.getRequest(), processContext.getAction(), role);

      // Set role in request attributes
      processContext.getRequest().setAttribute(AttributeNames.ROLE, role);
      
      // Load tasks
      loadReferenceData(TaskManager.class, AttributeNames.TASKS, processContext);
      loadReferenceData(GroupManager.class, AttributeNames.GROUPS, processContext);
      
      // Retrieve group ID from request parameters
      Integer groupId = ParameterExtractor.getInteger(processContext.getRequest(), FieldIds.GROUP_ID);
      
      if (groupId !=  null) {
        
        // Retrieve fields based on the selected group ID
        List<Field> fields = retrieveFields(groupId, processContext);
        
        // Add fields to request attributes
        if (fields != null && fields.size() > 0) {
          processContext.getRequest().setAttribute(AttributeNames.FIELDS, fields);
        }
      }
      
      // Set next page
      setNextPage(PageIds.ROLE, processContext);
  	}
    catch (ServletException servletException) {
      
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.security.app.role");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Selecting Group", servletException);
      
      throw new ProcessException(methodName + ":" + servletException.getMessage());
    }
  }
  
  /**
    Retrieves a list of field data objects using an group ID.
    @param groupId Group ID.
    @param processContext Process context.
    @return List List of field data objects.
  */
  private List<Field> retrieveFields(Integer groupId, ProcessContext processContext) throws ProcessException {
    
    // Set method name
    String methodName = "retrieveProperties";
  
    // Initialize return value
    List<Field> fields = null;
    
    // Create field manager factory
    IFactory fieldManagerFactory = ConfigurationManager.getFactory(FieldManager.class);
  
    // Initialize field manager
    FieldManager fieldManager = null;

    try {
      // Create field manager
      fieldManager = (FieldManager)fieldManagerFactory.create();
  
      // Retrieve field data objects
      fields = fieldManager.findByGroup(groupId);
    }
    catch (DataSourceException dataSourceException) {
      
      // Post error message
      Logger logger = Logger.getLogger("com.bws.security.app.role");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Error loading fields.", dataSourceException);
  
      throw new ProcessException(methodName + ":" + dataSourceException.getMessage());
    }
    finally {
      
      // Recycle field manager
      fieldManagerFactory.recycle(fieldManager);
    }
    
    return fields;
  }
  
}
