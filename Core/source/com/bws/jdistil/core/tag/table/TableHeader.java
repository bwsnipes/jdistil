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
  Table header component used to group table data elements in a header
  and define header characteristics.
  @author - Bryan Snipes
*/
public class TableHeader extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = 4290054837014059740L;

  /**
    Creates a new TableHeader object.
  */
  public TableHeader() {
    super();
  }

  /**
    Writes the start of an HTML table header.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Initialize return value
    int instruction = SKIP_BODY;

    if (isFirstRow()) {

      // Set method name
      String methodName = "doStartTag";

      // Get JSP writer
      JspWriter jspWriter = pageContext.getOut();

      // Get attributes
      String attributes = buildAttributes().toString();

      try {
        // Write start table header row
        jspWriter.println("<tr" + attributes + ">");
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Start Table Header Row", ioException);

        throw new JspException(methodName + ":" + ioException.getMessage());
      }

      // Set instruction
      instruction = EVAL_BODY_INCLUDE;
    }

    return instruction;
  }

  /**
    Writes the end of the table header.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    if (isFirstRow()) {

      // Set method name
      String methodName = "doEndTag";
  
      // Get JSP writer
      JspWriter jspWriter = pageContext.getOut();
  
      try {
  
        // Write end table header row
        jspWriter.println("</tr>");
      }
      catch (IOException ioException) {
  
        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing End Table Header Row", ioException);
  
        throw new JspException(methodName + ":" + ioException.getMessage());
      }
    }

    return EVAL_PAGE;
  }

  /**
    Returns a value indicating whether or not this is the first row of table data.
    @return boolean - First row indicator.
  */
  private boolean isFirstRow() {

    // Initialize return value
    boolean isFirstRow = false;

    // Attempt to get enclosing parent table
    Table table = (Table)findAncestorWithClass(this, Table.class);

    if (table != null) {

      // Get current row
      int currentRow = table.getCurrentRow();

      // Set first row indicator
      isFirstRow = currentRow == 0 || currentRow == 1;
    }

    return isFirstRow;
  }

}
