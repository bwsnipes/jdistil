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
  Table row component used to group table data elements in a row
  and define row characteristics.
  @author - Bryan Snipes
*/
public class TableRow extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -870762141483620139L;

  /**
    Creates a new TableRow object.
  */
  public TableRow() {
    super();
  }

  /**
    Writes the start of an HTML table row.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Initialize body instruction to skip body
    int bodyInstruction = SKIP_BODY;

    if (isDataAvailable()) {

      // Set method name
      String methodName = "doStartTag";

      // Get JSP writer
      JspWriter jspWriter = pageContext.getOut();

      // Get attributes
      String attributes = buildAttributes().toString();

      try {
        // Write start table row
        jspWriter.println("<tr" + attributes + ">");
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Start Table Row", ioException);

        throw new JspException(methodName + ":" + ioException.getMessage());
      }

      // Set body instruction
      bodyInstruction = EVAL_BODY_INCLUDE;
    }

    return bodyInstruction;
  }

  /**
    Writes the end of the table row.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    if (isDataAvailable()) {

      // Set method name
      String methodName = "doEndTag";

      // Get JSP writer
      JspWriter jspWriter = pageContext.getOut();

      try {
        // Write end table row
        jspWriter.println("</tr>");
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing End Table Row", ioException);

        throw new JspException(methodName + ":" + ioException.getMessage());
      }
    }

    return EVAL_PAGE;
  }

  /**
    Returns a value indicating whether or not data is available.
    @return boolean - Available data indicator.
  */
  private boolean isDataAvailable() {

    // Attempt to get enclosing parent table
    Table table = (Table)findAncestorWithClass(this, Table.class);

    return table != null && table.getCurrentValue() != null;
  }

}
