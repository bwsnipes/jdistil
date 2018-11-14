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
package com.bws.jdistil.core.servlet.http;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.message.Messages;
import com.bws.jdistil.core.process.IProcessor;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityException;
import com.bws.jdistil.core.security.SecurityManagerFactory;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.StringUtil;
import com.bws.jdistil.core.validation.ValidatorFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
  Default servlet class defining the default processing logic used to invoke
  business objects based on servlet actions contained in submitted requests.
  @author - Bryan Snipes
*/
public class Controller extends HttpServlet {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 3328448970708197962L;

  /**
    Parameter name used to retrieve a page ID from a request.
  */
  public static final String PAGE_ID = "PAGE_ID";

  /**
    Parameter name used to retrieve an action ID from a request.
  */
  public static final String ACTION_ID = "ACTION_ID";

  /**
    Parameter name used to add process contexts to request attributes.
  */
  public static final String PROCESS_CONTEXT = "PROCESS_CONTEXT";

  /**
    Creates a new Controller object.
  */
  public Controller() {
    super();
  }

  /**
    Handles all http GET and POST requests made to the servlet.
    @see HttpServlet#service
  */
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    // Set method name
    String methodName = "service";
    
    // Get current session
    HttpSession session = request.getSession(true);
    
    // Get security manager factory
    IFactory securityManagerFactory = SecurityManagerFactory.getInstance();

    // Create security manager
    ISecurityManager securityManager = (ISecurityManager)securityManagerFactory.create();

    // Retrieve process context
    ProcessContext processContext = createProcessContext(request, response, securityManager);

    // Add process context to request attributes
    request.setAttribute(PROCESS_CONTEXT, processContext);

    try {

      // Validate process context
      if (processContext.getAction() == null) {
      	
  	    // Get locale specific missing action message
  	    Locale locale = processContext.getRequest().getLocale();
  	    String missingActionMessage = Messages.formatMessage(locale, Messages.MISSING_ACTION, null);
  	    
        // Add missing action error
        processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, missingActionMessage));
      }

      // Handle any processing errors before continuing
      if (processContext.getErrorMessages().size() > 0) {
        throw new HttpException("Invalid processing data.");
      }

      // Get action from context
      Action action = processContext.getAction();

      // Clean session
      SessionCleaner.clean(action.getId(), session);

      // Set authorization required indicator
      boolean isAuthorizationRequired = securityManager.isAuthorizationRequired(action.getId(), session);
      
      // Check for logged on user if a user is required
      if (isAuthorizationRequired && !securityManager.isAuthenticated(session)) {

        // Get login page
        Page loginPage = ConfigurationManager.getLogonPage();

        // Navigate to logon page
        callPage(loginPage, processContext);
      }
      else {
      	
      	if (isAuthorizationRequired) {
      	
      		// Check action security
      		checkActionSecurity(securityManager, action, request, session);
      	}
      	
        // Validate the request
        validate(processContext);

        // Execute action
        invoke(action, processContext);

        // Get next page
        Page nextPage = processContext.getNextPage();

        // Navigate to the next page
        callPage(nextPage, processContext);
      }
    }
    catch (Exception exception) {
      
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.servlet.http");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Default Servlet Error", exception);

      // Add exception content to process context messages
      addErrorMessages(processContext, exception);

      // Get error page
      Page errorPage = ConfigurationManager.getErrorPage();

      // Display error page
      if (errorPage == null) {
        displayDefaultErrorPage(processContext);
      }
      else {
        callPage(errorPage, processContext);
      }
    }
    finally {

      // Recycle security manager
    	if (securityManagerFactory != null) {
        securityManagerFactory.recycle(securityManager);
    	}
    }
  }

  private void addErrorMessages(ProcessContext processContext, Exception exception) {
  
  	if (exception != null) {
  		
  		// Get exception message
  		String message = exception.getLocalizedMessage();
  		
  		// Add exception message
  		if (message != null) {
    		processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, message));
  		}
  		
  		// Get stack trace elements
  		StackTraceElement[] elements = exception.getStackTrace();
  		
  		if (elements != null) {
  			
  			// Get environment specific line separator 
  			String lineSeparator = System.getProperty("line.separator");
  			
  			// Create stack trace message
  			StringBuffer stackTraceMessage = new StringBuffer();
  			
  			// Add exception stack trace message indicator
  			stackTraceMessage.append("Stack Trace: ");
  			
  			// Add each stack trace statement to the error message
  			for (StackTraceElement element : elements) {
  				stackTraceMessage.append(element.toString()).append(lineSeparator);
  			}
  			
  			// Add stack trace message to messages
				processContext.addMessage(new ProcessMessage(ProcessMessage.ERROR, stackTraceMessage.toString()));
  		}
  	}
  }
  
  /**
    Creates and returns a process context object populated using data from a
    given servlet request.
    @param request - Servlet request.
    @param response - Servlet response.
    @param securityManager - Security manager.
    @return ProcessContext - Process context object.
  */
  protected ProcessContext createProcessContext(HttpServletRequest request, HttpServletResponse response, ISecurityManager securityManager) throws IOException {

    // Retrieve current action ID and page ID
    String actionId = ParameterExtractor.getString(request, ACTION_ID);
    String currentPageId = ParameterExtractor.getString(request, PAGE_ID);

    // Initialize context data
    Action action = null;
    Page currentPage = null;

    if (!StringUtil.isEmpty(actionId)) {

      // Retrieve action
      action = ConfigurationManager.getAction(actionId);

      // Retrieve current page
      if (!StringUtil.isEmpty(currentPageId)) {
        currentPage = ConfigurationManager.getPage(currentPageId);
      }
    }

    // Create and return process context object
    return new ProcessContext(request, response, action, currentPage, securityManager);
  }

  /**
	  Performs a security check against the submitted action.
	  @param securityManager - Security manager.
	  @param action Submitted action.
	  @param request - Http servlet request.
	  @param session - Http session.
	*/
	protected void checkActionSecurity(ISecurityManager securityManager, Action action, HttpServletRequest request, HttpSession session) throws ProcessException {
		
    // Set method name
    String methodName = "checkActionSecurity";

    // Get action ID
    String actionId = action.getId();
    
    try {
    	
      if (!securityManager.isAuthorized(actionId, session)) {
      	
      	// Get current locale
      	Locale locale = request.getLocale();

      	// Get locale specific action description
      	String actionDescription = action.getDescription(locale);
      	
      	throw new ProcessException("Action Security Violation: " + actionDescription + " is not accessible.");
      }
    }
    catch (SecurityException securityException) {
      
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.servlet.http");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Checking Action Security", securityException);
    }
	}

  /**
    Validates a request's data.
    @param processContext - Process context object.
  */
  protected void validate(ProcessContext processContext) throws ProcessException {

    // Get validator factory
    IFactory validatorFactory = ValidatorFactory.getInstance();

    // Create validator
    IProcessor validator = (IProcessor)validatorFactory.create();

    // Perform validation
    try {
      validator.process(processContext);
    }
    finally {
    	
    	if (validatorFactory != null) {
        validatorFactory.recycle(validator);
    	}
    }
  }

  /**
    Invokes all processors for a given action
    @param action - Action.
    @param processContext - Process context.
  */
  protected void invoke(Action action, ProcessContext processContext) throws ProcessException {

    if (action != null) {

      // Get processor factories
      List<IFactory> factories = action.getProcessorFactories();

      if (factories != null) {
      	
        for (IFactory factory : factories) {

          // Create processor
          IProcessor processor = (IProcessor)factory.create();

          // Invoke process
          try {
            processor.process(processContext);
          }
          finally {
            factory.recycle(processor);
          }
        }
      }
    }
  }

  /**
    Calls a specified page using a request and response.
    @param page - Page object.
    @param processContext - Process context.
  */
  protected void callPage(Page page, ProcessContext processContext) {

    // Set method name
    String methodName = "callPage";
    
    // Get request and response
    HttpServletRequest request = processContext.getRequest();
    HttpServletResponse response = processContext.getResponse();

    if (page != null && !response.isCommitted()) {

    	try {
          // Retrieve request dispatcher
        	RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(page.getName());

          // Forward page
          requestDispatcher.forward(request, response);
	    }
	    catch (IOException ioException) {
	
	      // Post error message
	      Logger logger = Logger.getLogger("com.bws.jdistil.core.servlet.http");
	      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Calling Page", ioException);
	    }
	    catch (ServletException servletException) {
	
	      // Post error message
	      Logger logger = Logger.getLogger("com.bws.jdistil.core.servlet.http");
	      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Calling Page", servletException);
	    }
    }
  }

  /**
    Displays all captured processing errors using the default error page.
    @param processContext - Process context.
  */
  protected void displayDefaultErrorPage(ProcessContext processContext) throws IOException {

    if (!processContext.getResponse().isCommitted()) {

			// Get environment specific line separator 
			String lineSeparator = System.getProperty("line.separator");

			// Set content type
    	processContext.getResponse().setContentType("text/html");

      // Get response print writer
      PrintWriter printWriter = processContext.getResponse().getWriter();

      // Writer errors HTML
      printWriter.println("<html>");
      printWriter.println("  <head>");
      printWriter.println("    <title>Processing Errors</title>");
      printWriter.println("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
      printWriter.println("    <link rel=\"stylesheet\" href=\"core.css\" type=\"text/css\">");
      printWriter.println("  </head>");
      printWriter.println();
      printWriter.println("  <body>");
      printWriter.println("    <div class=\"error\">");
      printWriter.println("    <div style=\"margin: 10px 0px 0px 20px\">The following errors were encountered:</div>");
      printWriter.println("      <ul>");

      for (ProcessMessage errorMessage : processContext.getErrorMessages()) {
        
      	// Remove line breaks
      	String formattedErrorMessage = errorMessage.getText();
      	formattedErrorMessage = formattedErrorMessage.replaceAll(lineSeparator, "<br/>");
      	formattedErrorMessage = formattedErrorMessage.replaceAll("(\r\n|\n)", "<br/>");
      	
      	// Write error message
      	printWriter.println("        <li>" + formattedErrorMessage + "</li>");
      }

      printWriter.println("      </ul>");
      printWriter.println("    </div>");
      printWriter.println("  </body>");
      printWriter.println("</html>");
    }
  }

}
