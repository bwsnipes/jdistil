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
package com.bws.jdistil.core.tag.basic;

import com.bws.jdistil.core.configuration.FieldIds;
import com.bws.jdistil.core.servlet.http.Controller;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * Form component used to encapsulate other components used to submit data.
 * @author - Bryan Snipes
 */
public class Form extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -5306393092789915530L;

  /**
   * Action ID.
   */
  private String actionId = null;

  /**
   * Creates a new Form object.
   */
  public Form() {
    super();
  }

  /**
   * Returns the action ID.
   * @return String - Action ID.
   */
  public String getActionId() {
    return actionId;
  }
  
  /**
   * Sets the action ID.
   * @param newActionId - New action ID.
   */
  public void setActionId(String newActionId) {
    actionId = newActionId;
  }

  /**
   * Writes a form using an HTML form and HTML hidden fields.
   * @see javax.servlet.jsp.tagext.Tag#doStartTag
   */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get attributes
    String attributes = buildAttributes().toString();

    // Initialize page ID
    String pageId = null;
    
    // Attempt to get enclosing parent body
    Body body = (Body)findAncestorWithClass(this, Body.class);
    
    // Set page ID defined in the body
    if (body != null) {
      pageId = body.getPageId();
    }

    // Convert page ID and action ID as string
    String targetPageId = StringUtil.convertNull(pageId);
    String targetActionId = StringUtil.convertNull(actionId);
    
    // Create and populate page attributes
    StringBuffer pageAttributes = new StringBuffer();
    appendAttribute("type", "hidden", pageAttributes);
    appendAttribute("name", Controller.PAGE_ID, pageAttributes);
    appendAttribute("value", targetPageId, pageAttributes);

    // Create and populate action attributes
    StringBuffer actionAttributes = new StringBuffer();
    appendAttribute("type", "hidden", actionAttributes);
    appendAttribute("name", Controller.ACTION_ID, actionAttributes);
    appendAttribute("value", targetActionId, actionAttributes);

    try {
      // Write form as HTML form
      jspWriter.println("<form" + attributes + ">");

      // Write transaction page ID and action ID as hidden fields
      jspWriter.println("<input" + pageAttributes + "/>");
      jspWriter.println("<input" + actionAttributes + "/>");

      // Attempt to retrieve transaction token
      String transactionToken = (String)pageContext.getSession().getAttribute(FieldIds.TRANSACTION_TOKEN_ID);

      if (!StringUtil.isEmpty(transactionToken)) {
      	
        // Create and populate transaction token attributes
        StringBuffer transactionTokenAttributes = new StringBuffer();
        appendAttribute("type", "hidden", transactionTokenAttributes);
        appendAttribute("name", FieldIds.TRANSACTION_TOKEN_ID, transactionTokenAttributes);
        appendAttribute("value", transactionToken, transactionTokenAttributes);

        // Write transaction token ID if defined
        jspWriter.println("<input" + transactionTokenAttributes + "/>");
      }
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Form", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
   * Completes the form output.
   * @see javax.servlet.jsp.tagext.Tag#doStartTag
   */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    try {
      
      // Write end of form
      jspWriter.println("</form>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Form", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_PAGE;
  }

  /**
   * Returns a string buffer containing all attributes and their associated values.
   * @see com.bws.jdistil.core.tag.basic.Component#buildAttributes
   */
  protected StringBuffer buildAttributes() throws JspException {

    // Set default action and method attributes if custom values are not specified
    if (!isAttributeDefined("action")) {
      setDynamicAttribute(null, "action", "Controller");
    }
    if (!isAttributeDefined("method")) {
      setDynamicAttribute(null, "method", "post");
    }

    return super.buildAttributes();
  }

}
