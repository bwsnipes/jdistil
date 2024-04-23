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
package com.bws.jdistil.core.process;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.security.ISecurityManager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
  Class used to store data passed from a controlling servlet, such as the
  DefaultServlet, to classes implementing the Processor interface.
  @author - Bryan Snipes
  @see com.bws.jdistil.core.process.Processor
*/
public class ProcessContext {

  /**
    HTTP request object.
  */
  private HttpServletRequest request = null;

  /**
    HTTP response object.
  */
  private HttpServletResponse response = null;

  /**
    Servlet action.
  */
  private Action action = null;

  /**
    Current page.
  */
  private Page currentPage = null;

  /**
    Next page.
  */
  private Page nextPage = null;

  /**
    Security manager.
  */
  private ISecurityManager securityManager = null;
  
  /**
    Process messages.
  */
  private List<ProcessMessage> messages = new ArrayList<ProcessMessage>();

  /**
    Creates an empty ProcessContext object.
  */
  protected ProcessContext() {
    super();
  }

  /**
    Creates and initializes ProcessContext object.
    @param request - HTTP servlet request.
    @param response - HTTP servlet response.
    @param action - Submitted action.
    @param currentPage - Submitted page.
    @param securityManager - Security manager.
  */
  public ProcessContext(HttpServletRequest request, HttpServletResponse response, Action action, Page currentPage, ISecurityManager securityManager) {

    super();
    this.request = request;
    this.response = response;
    this.action = action;
    this.currentPage = currentPage;
    this.securityManager = securityManager;
  }

  /**
    Returns the servlet request.
    @return HttpServletRequest - Servlet request.
  */
  public HttpServletRequest getRequest() {
    return request;
  }

  /**
    Returns the servlet response.
    @return HttpServletResponse - Servlet response.
  */
  public HttpServletResponse getResponse() {
    return response;
  }

  /**
    Returns the servlet action.
    @return Action - Servlet action.
  */
  public Action getAction() {
    return action;
  }

  /**
    Returns the current page.
    @return Page - Current page.
  */
  public Page getCurrentPage() {
    return currentPage;
  }

  /**
    Returns the next page.
    @return Page - Next page.
  */
  public Page getNextPage() {
    return nextPage;
  }

  /**
    Sets the next page.
    @param newNextPage - New next page.
  */
  public void setNextPage(Page newNextPage) {
    nextPage = newNextPage;
  }

  /**
    Returns the security manager.
    @return ISecurityManager - Security manager.
  */
  public ISecurityManager getSecurityManager() {
		return securityManager;
	}

	/**
    Adds a process message.
    @param message - Process message.
  */
  public void addMessage(ProcessMessage message) {
    
    if (message != null) {
      messages.add(message);
    }
  }

  /**
    Returns a list of process messages based on the specified message type.
    @param type - Message type.
    @return List - List of processing errors.
  */
  public List<ProcessMessage> getMessages(String type) {
    
    // Initialize return value
    List<ProcessMessage> targetMessages = new ArrayList<ProcessMessage>();
    
    for (ProcessMessage message : messages) {
    
      if (message.getType().equals(type)) {
        targetMessages.add(message);
      }
    }
    
    return targetMessages;
  }
  
  /**
    Returns a list of informational messages.
    @return List - List of informational messages.
  */
  public List<ProcessMessage> getInformationalMessages() {
    return getMessages(ProcessMessage.INFORMATIONAL);
  }

  /**
    Returns a list of confirmation messages.
    @return List - List of confirmation messages.
  */
  public List<ProcessMessage> getConfirmationMessages() {
    return getMessages(ProcessMessage.CONFIRMATION);
  }
  
  /**
    Returns a list of warning messages.
    @return List - List of warning messages.
  */
  public List<ProcessMessage> getWarningMessages() {
    return getMessages(ProcessMessage.WARNING);
  }
  
  /**
    Returns a list of error messages.
    @return List - List of error messages.
  */
  public List<ProcessMessage> getErrorMessages() {
    return getMessages(ProcessMessage.ERROR);
  }
  
}
