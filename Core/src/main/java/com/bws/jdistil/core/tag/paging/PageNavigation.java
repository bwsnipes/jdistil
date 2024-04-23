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
package com.bws.jdistil.core.tag.paging;

import com.bws.jdistil.core.configuration.AttributeNames;
import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.tag.basic.Form;
import com.bws.jdistil.core.util.StringUtil;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Component used to display page navigation links.
  @author - Bryan Snipes
*/
public class PageNavigation extends TagSupport {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = 8610872787937029501L;

  /**
    Pagination variables defined constant.
  */
  private static final String PAGINATION_VARIABLES_DEFINED = "PAGINATION_VARIABLES_DEFINED";
  
  /**
    Current page number field ID.
  */
  private String currentPageNumberFieldId = null;
  
  /**
    Selected page number field ID.
  */
  private String selectedPageNumberFieldId = null;
  
  /**
    Previous page action ID.
  */
  private String previousPageActionId = null;
  
  /**
    Select page action ID.
  */
  private String selectPageActionId = null;
  
  /**
    Next page action ID.
  */
  private String nextPageActionId = null;
  
  /**
    Creates a new PageNavigation object.
  */
  public PageNavigation() {
    super();
  }

  /**
    Set current page number field ID.
    @param newCurrentPageNumberFieldId - New current page number field ID.
  */
  public void setCurrentPageNumberFieldId(String newCurrentPageNumberFieldId) {
  	currentPageNumberFieldId = newCurrentPageNumberFieldId;
  }
  
  /**
    Set selected page number field ID.
    @param newCurrentPageNumberFieldId - New selected page number field ID.
  */
  public void setSelectedPageNumberFieldId(String newCurrentPageNumberFieldId) {
  	selectedPageNumberFieldId = newCurrentPageNumberFieldId;
  }
  
  /**
    Set previous page action ID.
    @param newPreviousPageActionId - New previous page action ID.
  */
  public void setPreviousPageActionId(String newPreviousPageActionId) {
    previousPageActionId = newPreviousPageActionId;
  }
  
  /**
    Set select page action ID.
    @param newSelectPageActionId - New select page action ID.
  */
  public void setSelectPageActionId(String newSelectPageActionId) {
    selectPageActionId = newSelectPageActionId;
  }
  
  /**
    Set next page action ID.
    @param newNextPageActionId - New next page action ID.
  */
  public void setNextPageActionId(String newNextPageActionId) {
    nextPageActionId = newNextPageActionId;
  }
  
  /**
    Writes the page navigation links.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Attempt to retrieve total pages and current page number from request attributes
    Integer totalPages = (Integer)pageContext.getRequest().getAttribute(AttributeNames.TOTAL_PAGES);
    Integer currentPageNumber = (Integer)pageContext.getRequest().getAttribute(AttributeNames.CURRENT_PAGE_NUMBER);

    // Must have pages information to display navigation
    if (totalPages != null && totalPages.intValue() > 1 && currentPageNumber != null && currentPageNumber.intValue() > 0) {

      // Get ID of form component enclosing this component
      String formId = getFormId();
      
      // Get number formatter based on current locale
      Locale locale = pageContext.getRequest().getLocale();
      NumberFormat numberFormatter = NumberFormat.getIntegerInstance(locale);
      
      // Check for pagination variables defined indicator
      boolean paginationVariablesDefined = pageContext.getAttribute(PAGINATION_VARIABLES_DEFINED) != null;
      
      try {
      
        if (!paginationVariablesDefined) {

          // Write hidden pagination variables if not already defined
          jspWriter.println("<input type=\"hidden\" name=\"" + currentPageNumberFieldId + "\" value=\"" + currentPageNumber + "\" />");
          jspWriter.println("<input type=\"hidden\" name=\"" + selectedPageNumberFieldId + "\" value=\"\" />");

          // Set pagination variables defined indicator
          pageContext.setAttribute(PAGINATION_VARIABLES_DEFINED, Boolean.TRUE);
        }
        
        // Calculate page range
        PageRange pageRange = calculatePageRange(currentPageNumber, totalPages);
  
        // Get start page number and end page number from page range
        int startPageNumber = pageRange.getStartPageNumber();
        int endPageNumber = pageRange.getEndPageNumber();
        
        // Get navigation image sources
        String previousPageImageSource = ResourceUtil.getString(Constants.PREVIOUS_PAGE_IMAGE_SOURCE);
        String nextPageImageSource = ResourceUtil.getString(Constants.NEXT_PAGE_IMAGE_SOURCE);

        if (currentPageNumber.intValue() != 1) {
          
          // Write start of previous page link
          jspWriter.print("<a class=\"pagingButton\" href=\"#\" onClick=\"javascript:submitAction('" + formId + "', '" + previousPageActionId + "', '');return false;\" >");

          // Writre previous page image
          if (StringUtil.isEmpty(previousPageImageSource)) {
            jspWriter.print("&lt;");
          }
          else {
            jspWriter.print("<img src=\"" + previousPageImageSource + "\" />");
          }

          // Write end of previous page link
          jspWriter.print("</a>");
        }

        for (int pageNumber = startPageNumber; pageNumber <= endPageNumber; pageNumber++) {

          if (pageNumber == currentPageNumber.intValue()) {
            
            // Write select page link 
            jspWriter.print("<a class=\"pagingButton\" href=\"#\" onClick=\"javascript:setValue('" + formId + "', '" + selectedPageNumberFieldId + "', '" + pageNumber + "'); javascript:submitAction('" + formId + "', '" + selectPageActionId + "', '');return false;\" >");
            jspWriter.print("<strong>");
            jspWriter.print(numberFormatter.format(pageNumber));
            jspWriter.print("</strong>");
            jspWriter.print("</a>");
          }
          else {
            
            // Write select page link
            jspWriter.print("<a class=\"pagingButton\" href=\"#\" onClick=\"javascript:setValue('" + formId + "', '" + selectedPageNumberFieldId + "', '" + pageNumber + "'); javascript:submitAction('" + formId + "', '" + selectPageActionId + "', '');return false;\" >");
            jspWriter.print(numberFormatter.format(pageNumber));
            jspWriter.print("</a>");
          }
        }

        if (currentPageNumber.intValue() != totalPages.intValue()) {

          // Write start of next page link
          jspWriter.print("<a class=\"pagingButton\" href=\"#\" onClick=\"javascript:submitAction('" + formId + "', '" + nextPageActionId + "', '');return false;\" >");
  
          // Write next page image
          if (StringUtil.isEmpty(nextPageImageSource)) {
            jspWriter.print("&gt;");
          }
          else {
            jspWriter.print("<img src=\"" + nextPageImageSource + "\" />");
          }
  
          // Write end of next page link
          jspWriter.print("</a>");
        }

      }
      catch (IOException ioException) {
  
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.paging");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Page Navigation", ioException);
  
        throw new JspException(methodName + ":" + ioException.getMessage());
      }
    }

    return SKIP_BODY;
  }

  /**
    Returns the ID of the form component enclosing this component.
    @return String - Form ID.
  */
  private String getFormId() {
  
    // Initialize method name
    String methodName = "getFormId";
    
    // Initialize form ID
    String formId = "";
    
    // Check for enclosing form
    Form form = (Form)findAncestorWithClass(this, Form.class);
    
    try {
      
      // Attempt to get form ID if an enclosing form is found
      if (form != null) {
        formId = StringUtil.convertNull(form.getDynamicAttribute("id"));
      }
    }
    catch (JspException jspException) {
      
      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.paging");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Getting Form ID", jspException);
    }
    
    return formId.trim();
  }
  
  /**
    Calculates and returns the page range based on the current page number and total number of pages.
    @param currentPageNumber - Current page number.
    @param totalPages - Total number of pages.
    @return PageRange - Page range.
  */
  private PageRange calculatePageRange(int currentPageNumber, int totalPages) {
    
    // Initialize start page and end page
    int startPage = 0;
    int endPage = 0;

    if (currentPageNumber >= 0 || totalPages >= 0) {
      
      // Calculate start page and end page based on a range of 10 pages
      startPage = currentPageNumber - 5;
      endPage = currentPageNumber + 4;
      
      // Adjust calculated start page and end page based on range validation
      if (startPage <= 0 && endPage > totalPages) {
        
        startPage = 1;
        endPage = totalPages;
      }
      else if (startPage < 1) {
        
        endPage = endPage + Math.abs(startPage) + 1;

        if (endPage > totalPages) {
          endPage = totalPages;
        }
        
        startPage = 1;
      }
      else if (endPage > totalPages) {
        
        startPage = startPage - Math.abs(totalPages - endPage);

        if (startPage < 1) {
          startPage = 1;
        }
        
        endPage = totalPages;
      }
    }
    
    return new PageRange(startPage, endPage);
  }

/**
  Represents a page range using a starting page number and ending page number.
  @author Bryan Snipes
*/
private class PageRange {

  /**
    Start page number.
  */
  private int startPageNumber = 0;
  
  /**
    End page number.
  */
  private int endPageNumber = 0;
  
  /**
    Creates a new PageRange using a start page number and end page number.
    @param startPageNumber - Start page number.
    @param endPageNumber - End page number.
  */
  public PageRange(int startPageNumber, int endPageNumber) {
    super();
  
    // Set properties
    this.startPageNumber = startPageNumber;
    this.endPageNumber = endPageNumber;
  }
  
  /**
    Returns the starting page number.
    @return int - Start page number.
  */
  public int getStartPageNumber() {
    return startPageNumber;
  }
  
  /**
    Returns the ending page number.
    @return int - End page number.
  */
  public int getEndPageNumber() {
    return endPageNumber;
  }
  
}

}
