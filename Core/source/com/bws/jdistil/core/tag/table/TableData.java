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
package com.bws.jdistil.core.tag.table;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.bws.jdistil.core.tag.basic.Component;

/**
  Table data component used to group other components
  and define data characteristics.
  @author - Bryan Snipes
*/
public class TableData extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -3265163187036675051L;

  /**
    Creates a new TableData object.
  */
  public TableData() {
    super();
  }

  /**
    Writes the start of an HTML table data.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    // Get attributes
    String attributes = buildAttributes().toString();

    try {
      // Write start table header, footer, or data
      if (isTableHeader()) {
        jspWriter.println("<th" + attributes + ">");
      }
      else if (isTableFooter()) {
        jspWriter.println("<tfoot" + attributes + ">");
      }
      else {
        jspWriter.println("<td" + attributes + ">");
      }
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Start Table Data", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
    Writes the end of the table data.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    try {
      // Write end table header, footer, or data
      if (isTableHeader()) {
        jspWriter.println("</th>");
      }
      else if (isTableFooter()) {
        jspWriter.println("</tfoot>");
      }
      else {
        jspWriter.println("</td>");
      }
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing End Table Data", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_PAGE;
  }

  /**
    Returns a value indicating whether or not this tag is enclosed by a table header tag.
    @return boolean - Table header tag indicator.
  */
  private boolean isTableHeader() {

    // Attempt to get enclosing parent table header
    TableHeader tableHeader = (TableHeader)findAncestorWithClass(this, TableHeader.class);

    return tableHeader != null;
  }

  /**
    Returns a value indicating whether or not this tag is enclosed by a table footer tag.
    @return boolean - Table footer tag indicator.
  */
  private boolean isTableFooter() {

    // Attempt to get enclosing parent table header
    TableFooter tableFooter = (TableFooter)findAncestorWithClass(this, TableFooter.class);

    return tableFooter != null;
  }

}
