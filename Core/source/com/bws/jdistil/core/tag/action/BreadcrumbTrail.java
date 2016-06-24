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
package com.bws.jdistil.core.tag.action;

import com.bws.jdistil.core.breadcrumb.Breadcrumb;
import com.bws.jdistil.core.breadcrumb.Parameter;
import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.servlet.http.Controller;
import com.bws.jdistil.core.tag.basic.Body;
import com.bws.jdistil.core.tag.basic.Component;
import com.bws.jdistil.core.tag.basic.Form;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
  Component used to display a trail of breadcrumb links.
  @author - Bryan Snipes
*/
public class BreadcrumbTrail extends Component {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -2494175352591976892L;

  /**
    Breadcrumb trail data attribute name.
  */
  private static final String BREADCRUMB_TRAIL_DATA = "JDISTIL_BREADCRUMB_TRAIL_DATA";
  
  /**
    Start of trail indicator.
  */
  private Boolean isStartOfTrail = Boolean.FALSE;
  
  /**
	  List of registered action IDs.
	*/
	private Set<String> registeredActionIds = new HashSet<String>();
	
  /**
    Creates a new BreadcrumbTrail object.
  */
  public BreadcrumbTrail() {
    super();
  }

  /**
    Sets the start of trail indicator.
    @param isStartOfTrail - Start of trail indicator.
  */
  public void setIsStartOfTrail(Boolean isStartOfTrail) {
    this.isStartOfTrail = isStartOfTrail;
  }
  
  /**
    Registers an action ID.
    @param actionId Action ID.
  */
  protected void registerActionId(String actionId) {
  	
  	if (actionId != null) {
  		
  		// Register action ID
  		registeredActionIds.add(actionId);
  	}
  }
  
  /**
    Writes a trail of breadcrumb links.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Validate tag placement
    if (isEmbeddedInForm()) {
      throw new JspException("Invalid tag placement: Breadcrumb trail cannot be embedded in a form tag.");
    }
    
    return EVAL_BODY_BUFFERED;
  }

  /**
    Cleans up resources each time tag is used.
	  @see javax.servlet.jsp.tagext.Tag#doEndTag
	*/
	public int doEndTag() throws JspException {
	  
    // Set method name
    String methodName = "doEndTag";

    // Get process context
    ProcessContext processContext = (ProcessContext)pageContext.getRequest().getAttribute(Controller.PROCESS_CONTEXT);
    
    // Only process requests where no errors were encountered
    if (processContext != null && processContext.getErrorMessages().isEmpty()) {
      
      // Retrieve current action ID used to navigate to this page
      String actionId = processContext.getAction().getId();
      
      // Get current page ID
      String pageId = getPageId();
      
      if (!StringUtil.isEmpty(pageId)) {
      
        // Get page and action
        Page page = ConfigurationManager.getPage(pageId);
        Action action = ConfigurationManager.getAction(actionId);

        if (page != null && action != null) {
            
          // Get breadcrumb trail data in session context
					@SuppressWarnings("unchecked")
					List<Breadcrumb> breadcrumbs = (List<Breadcrumb>)pageContext.getAttribute(BREADCRUMB_TRAIL_DATA, PageContext.SESSION_SCOPE);
          
          if (breadcrumbs == null) {

            // Create breadcrumbs if not already created
            breadcrumbs = new ArrayList<Breadcrumb>();

            // Set breadcrumb trail data in session context
            pageContext.setAttribute(BREADCRUMB_TRAIL_DATA, breadcrumbs, PageContext.SESSION_SCOPE);
          }
            
          // Only track registered actions 
          if (registeredActionIds.contains(actionId)) {
          	
            // Get breadcrumb description
            String description = getBreadcrumbDescription(action);
            
            // Create new breadcrumb
            Breadcrumb breadcrumb = new Breadcrumb(page, action, description);
            
            // Attempt to find breadcrumb in breadcrumb trail
            int position = breadcrumbs.indexOf(breadcrumb);
            
            if (position >= 0) {
              
              // Replace existing breadcrumb
              breadcrumbs.set(position, breadcrumb);
              
              // Remove breadcrumbs in trail beyond current breadcrumb
              for (int index = breadcrumbs.size() - 1; index > position; index--) {
                breadcrumbs.remove(index);
              }
            }
            else {
                
              // Clear breadcrumb trail if start of new breadcrumb trail
              if (isStartOfTrail) {
                breadcrumbs.clear();
              }
              
              // Add new breadcrumb to trail
              breadcrumbs.add(breadcrumb);
            }
            
            // Update breadcrumb information using request data
            breadcrumb.update(pageContext.getRequest());
          }
          
          if (!breadcrumbs.isEmpty()) {
          	
            // Get JSP writer
            JspWriter jspWriter = pageContext.getOut();

            try {
              // Write breadcrumb trail
              jspWriter.println("<div class=\"breadcrumbTrail\">");
              jspWriter.println("<ul>");

              for (int index = 0; index < breadcrumbs.size(); index++) {
                
              	// Get next breadcrumb
              	Breadcrumb nextBreadcrumb = breadcrumbs.get(index);
              	
              	// Write separator
              	if (index > 0) {
                  jspWriter.print("<li>");
                  jspWriter.print("<div class=\"breadcrumbSeparator\"/>");
                  jspWriter.println("</li>");
              	}

              	if (index == breadcrumbs.size() - 1) {
              		
                  // Write last breadcrumb text value
                  jspWriter.print("<li>");
                  jspWriter.print("<span class=\"breadcrumbSelected\">");
                  jspWriter.print(nextBreadcrumb.getDescription());
                  jspWriter.print("</span>");
                  jspWriter.println("</li>");
              	}
              	else {
              		
                  // Create breadcrumb specific form ID
                  String formId = "BreadcrumbForm" + String.valueOf(index + 1);
                  
                  // Write breadcrumb link
                  jspWriter.print("<li>");
                  jspWriter.print("<a class=\"breadcrumb\" href=\"#\" onClick=\"javascript:submitAction('" + formId + "', '', '');return false;\">");
                  jspWriter.print(nextBreadcrumb.getDescription());
                  jspWriter.print("</a>");
                  jspWriter.println("</li>");
              	}
              }

              // Write end of breadcrumb trail
              jspWriter.println("</ul>");
              jspWriter.println("</div>");

              for (int index = 0; index < breadcrumbs.size() - 1; index++) {
                
              	// Get next breadcrumb
              	Breadcrumb nextBreadcrumb = breadcrumbs.get(index);
              	
                // Create breadcrumb specific form ID
                String formId = "BreadcrumbForm" + String.valueOf(index + 1);
                
                // Get breadcrumb page and action
                page = nextBreadcrumb.getPage();
                action = nextBreadcrumb.getAction();
                
                // Write start of form
                jspWriter.println("<form id=\"" + formId + "\" action=\"Controller\" method=\"put\" >");
                
                // Write breadcrumb request hidden field
                jspWriter.println("<input type=\"hidden\" name=\"PAGE_ID\" value=\"" + page.getId() + "\" />");
                jspWriter.println("<input type=\"hidden\" name=\"ACTION_ID\" value=\"" + action.getId() + "\" />");

                // Get next breadcrumb parameters
                for (Parameter parameter : nextBreadcrumb.getParameters()) {
                  
                  // Get parameter name and values
                  String name = parameter.getName();
                  String[] values = parameter.getValues();
                  
                  // Write parameter values as hidden fields
                  for (String value : values) {
                    jspWriter.println("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\" />");
                  }
                }

                // Write end of form
                jspWriter.println("</form>");
              }
            }
            catch (IOException ioException) {

              // Post error message
              Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.action");
              logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Breadcrumb Trail", ioException);

              throw new JspException(methodName + ":" + ioException.getMessage());
            }
          }
        }
      }
    }
		
		// Reset attributes
	  isStartOfTrail = Boolean.FALSE;
		
	  // Clear registered action IDs
	  registeredActionIds.clear();
	  
		return EVAL_PAGE;
	}
	
  /**
   * Returns a value indicating whether or not this tag is embedded in a form tag.
   * @return boolean Embedded in form indicator.
   */
  private boolean isEmbeddedInForm() {
    
    // Attempt to get enclosing parent form
    Form form = (Form)findAncestorWithClass(this, Form.class);
    
    return form != null;
  }
  
  /**
   * Returns the page ID defined in an enclosing body tag.
   * @return String - Page ID.
   */
  private String getPageId() {
    
    // Initialize page ID
    String pageId = null;
    
    // Attempt to get enclosing parent body
    Body body = (Body)findAncestorWithClass(this, Body.class);
    
    // Set page ID defined in the body
    if (body != null) {
      pageId = body.getPageId();
    }
    
    return pageId;
  }
  
  /**
    Returns the breadcrumb description.
    @return String - Breadcrumb description.
  */
  private String getBreadcrumbDescription(Action action) {
  
    // Initialize return value
    String description = null;
  
    if (bodyContent != null) {

      // Get body text
    	description = StringUtil.convertNull(bodyContent.getString());

      // Clear body content buffer
      bodyContent.clearBody();
    }
    
    if (StringUtil.isEmpty(description) && action != null) {
      
      // Get current locale
      Locale locale = pageContext.getRequest().getLocale();

      // Get local specific description
      if (action != null) {
        description = action.getDescription(locale);
      }
    }
  
    if (description != null) {
    	description = description.trim();
    }
    
    return description;
  }
  
}
