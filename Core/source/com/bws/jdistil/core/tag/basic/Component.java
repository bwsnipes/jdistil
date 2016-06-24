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

import com.bws.jdistil.core.security.ISecurityManager;
import com.bws.jdistil.core.security.SecurityManagerFactory;
import com.bws.jdistil.core.util.StringUtil;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TryCatchFinally;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Abstract base component containing attributes common to all UI components.
  @author - Bryan Snipes
*/
public abstract class Component extends BodyTagSupport implements DynamicAttributes, TryCatchFinally {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -4238114965773454310L;

  /**
    Dynamic attributes.
  */
  private Map<String, Object> dynamicAttributes = new HashMap<String, Object>();

  /**
    Local security manager created by this component instance.
  */
  private ISecurityManager localSecurityManager = null;

  /**
    Creates a new Component object.
  */
  public Component() {
    super();
  }

  /**
    Default to evaluating the tag body.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {
    return EVAL_BODY_BUFFERED;
  }
  
  /**
    Default to evaluating the rest of the page.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {
    return EVAL_PAGE;
  }
  
  /**
    Handles tag exceptions.
    @see javax.servlet.jsp.tagext.TryCatchFinally#doCatch
  */
  public void doCatch(Throwable throwable) throws Throwable {

		// Set method name
		String methodName = "doCatch";
		
		// Post error message
		Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
		logger.logp(Level.SEVERE, getClass().getName(), methodName, "Handling Component Exception", throwable);
		  
		throw throwable;
  }
  
  /**
    Cleans up resources each time tag is used.
    @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
  */
  public void doFinally() {

    // Clear dynamic attributes
    dynamicAttributes.clear();

    // Recycle local security manager
    if (localSecurityManager != null) {
      SecurityManagerFactory.getInstance().recycle(localSecurityManager);
    }
  }

  /**
    Returns a security manager to be used by this component and any inner components
    contained in the body of this component. Creation of a new security manager
    only occurs if one cannot be obtained from it's parent component.
    @return ISecurityManager - Security manager.
  */
  protected ISecurityManager getSecurityManager() {

    // Use local security manager if previously created
    ISecurityManager securityManager = localSecurityManager;

    if (securityManager == null) {

      // Check for parent component
      Component parent = (Component)findAncestorWithClass(this, Component.class);

      // Attempt to retrieve from parent
      if (parent != null) {
        securityManager = parent.getSecurityManager();
      }

      if (securityManager == null) {

        // Create security manager
        securityManager = (ISecurityManager)SecurityManagerFactory.getInstance().create();

        // Save as local security manager
        localSecurityManager = securityManager;
      }
    }

    return securityManager;
  }

  /**
    Returns a dynamic attribute value using a specified attribute name.
    @param localName - Attribute name.
    @return Object - Attribute value.
  */
  public Object getDynamicAttribute(String localName) throws JspException {
  
    // Initialize return value
    Object value = null;
    
    if (localName != null) {
  
      // Convert name to lower case
      localName = localName.toLowerCase();
  
      // Add attribute
      value = dynamicAttributes.get(localName);
    }
    
    return value;
  }
  
  /**
    Sets a dynamic attribute.
    @see javax.servlet.jsp.tagext.DynamicAttributes#setDynamicAttribute
  */
  public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {

    if (localName != null) {

      // Convert name to lower case
      localName = localName.toLowerCase();

      // Add attribute
      dynamicAttributes.put(localName, value);
    }
  }

  /**
    Returns a value indicating whether or not a specified attribute is defined.
    @param attributeName - Attribute name.
    @return boolean - Attribute indicator.
  */
  protected boolean isAttributeDefined(String attributeName) {

    // Initialize return value
    boolean isDefined = false;

    // Check for attribute
    if (attributeName != null) {
      isDefined = dynamicAttributes.containsKey(attributeName.toLowerCase());
    }

    return isDefined;
  }

  /**
    Returns a string buffer containing all attributes and their associated values.
    @return StringBuffer - Attribute values.
  */
  protected StringBuffer buildAttributes() throws JspException {

    // Initialize return value
    StringBuffer attributes = new StringBuffer();

    // Get attribute names
    Iterator<String> names = dynamicAttributes.keySet().iterator();

    while (names.hasNext()) {

      // Get next name
      String name = names.next();

      // Lookup value
      Object value = dynamicAttributes.get(name);

      // Append attribute
      appendAttribute(name, value, attributes);
    }

    return attributes;
  }

  /**
    Appends attribute information to an attributes string buffer using an
    attribute name and value.
    @param name - Attribute name.
    @param value - Attribute value.
    @param attributes - Attributes string buffer.
  */
  protected void appendAttribute(String name, Object value, StringBuffer attributes) {

    if (!StringUtil.isEmpty(name) && attributes != null) {

      // Append attribute spacer
      attributes.append(" ");

      // Append attribute data
      if (value == null) {
        attributes.append(name);
      }
      else {
        attributes.append(name).append("=").append("\"").append(value).append("\"");
      }
    }
  }

}
