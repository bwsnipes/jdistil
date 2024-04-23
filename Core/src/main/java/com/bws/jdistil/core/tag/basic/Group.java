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

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.bws.jdistil.core.configuration.FieldValues;
import com.bws.jdistil.core.servlet.ParameterExtractor;
import com.bws.jdistil.core.util.Descriptions;
import com.bws.jdistil.core.util.StringUtil;

/**
  Component used to hide and show other components.
  @author - Bryan Snipes
*/
public class Group extends Component {

  /**
   * Serial version UID.
   */
	private static final long serialVersionUID = -7716136388224811239L;
	
  /**
    Unique component ID.
  */
  private String id = null;
  
  /**
    Field ID used to store display state.
  */
  private String fieldId = null;

  /**
    Hide label.
  */
  private String hideLabel = null;
  
  /**
  	Show label.
  */
  private String showLabel = null;
  
  /**
  	Indicates the group should be hidden by default.
  */
  private boolean isHiddenByDefault = true;
  
	/**
    Creates a new Group object.
  */
  public Group() {
    super();
  }

	/**
    Sets the unique component ID.
    @param id - Unique component ID.
  */
  public void setId(String id) {
		this.id = id;
	}

	/**
    Sets the field ID used to store display state.
    @param fieldId - Field ID used to store display state.
  */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }
  
  /**
    Sets the hide label displayed when the group is visible.
    @param hideLabel - Hide label displayed when the group is visible.
  */
  public void setHideLabel(String hideLabel) {
		this.hideLabel = hideLabel;
	}

  /**
    Sets the show label displayed when the group is hidden.
    @param showLabel - Show label displayed when the group is hidden.
  */
	public void setShowLabel(String showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 	Sets the value indicating the group should be hidden by default.
	  @param isHiddenByDefault Hidden by default indicator.
	*/
	public void setIsHiddenByDefault(boolean isHiddenByDefault) {
		this.isHiddenByDefault = isHiddenByDefault;
	}

	/**
    Writes an HTML body.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Create link ID using component ID
    String linkId = id + "link";

    // Initialize hidden state
    boolean isViewHidden = isHiddenByDefault;
    
    // Attempt to get group state from request parameters
    String groupState = ParameterExtractor.getString(pageContext.getRequest(), fieldId);
    
    if (!StringUtil.isEmpty(groupState)) {
    	
      // Set hidden view indicator based on group state
    	if (groupState.equalsIgnoreCase(FieldValues.GROUP_SHOW)) {
    		
    		isViewHidden = false;
    	}
    	else if (groupState.equalsIgnoreCase(FieldValues.GROUP_HIDE)) {
    	
    		isViewHidden = true;
    	}
    }
    
    // Get current locale
    Locale locale = pageContext.getRequest().getLocale();
    
    // Attempt to translate hide and show labels to locale specific descriptions
    hideLabel = Descriptions.getDescription(hideLabel, locale);
    showLabel = Descriptions.getDescription(showLabel, locale);

    // Set label and display style
    String label = isViewHidden ? showLabel : hideLabel;
    String display = isViewHidden ? "display:none;" : "display:inline-block;";
    
    try {
      // Write start of group
      jspWriter.println("<div class=\"group\">");
      jspWriter.println("  <a id=\"" + linkId + "\" class=\"groupLink\" href=\"javascript:toggleGroup('" + fieldId + "', '" + linkId + "', '" + id + "', '" + hideLabel + "', '" + showLabel + "');\">" + label + "</a>");
      jspWriter.println("    <div id=\"" + id + "\" class=\"groupView\" style=\"" + display + "\">");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Group", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
    Completes the group output.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    try {
      
      // Write end of group
      jspWriter.println("  </div>");
      jspWriter.println("</div>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.basic");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Group", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_PAGE;
  }

}
