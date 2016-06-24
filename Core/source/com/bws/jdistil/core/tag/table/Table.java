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

import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.tag.basic.Component;
import com.bws.jdistil.core.util.Introspector;
import com.bws.jdistil.core.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
  Table component simplifies iterative processing and provides
  support for other UI components as table elements.
  @author - Bryan Snipes
*/
public class Table extends Component {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -6420727165553650006L;

  /**
    Attribute name bound to table data or object containing table data.
  */
  private String attributeName = null;

  /**
    Field ID bound to object containing table data.
  */
  private String fieldId = null;

  /**
    List of data objects containing table data.
  */
  private ArrayList<DataObject<?>> values = new ArrayList<DataObject<?>>();
  
  /**
    Iterator of data objects containing table data.
  */
  private Iterator<DataObject<?>> valueIterator = null;

  /**
    Current row.
  */
  private int currentRow = 0;

  /**
    Current data object.
  */
  private DataObject<?> currentValue = null;

  /**
    Creates a new Table object.
  */
  public Table() {
    super();
  }

  /**
    Sets the attribute name.
    @param newAttributeName - New attribute name.
  */
  public void setAttributeName(String newAttributeName) {
    attributeName = newAttributeName;
  }

  /**
    Sets the field ID.
    @param newFieldId - New field ID.
  */
  public void setFieldId(String newFieldId) {
    fieldId = newFieldId;
  }

  /**
    Writes the start of an HTML table.
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
      // Write start table
      jspWriter.println("<table" + attributes + ">");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing Start Table", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    if (!StringUtil.isEmpty(attributeName)) {

      // Get target object starting with object bound to attribute name
      Object target = pageContext.getRequest().getAttribute(attributeName);

      if (target != null) {

        if (!StringUtil.isEmpty(fieldId)) {

          // Cast to data object containing collection of dependent data objects
          DataObject<?> dataObject = (DataObject<?>)target;

          // Get field value as target
          target = Introspector.getFieldValue(dataObject, fieldId);
        }

        // Clear values list
        values.clear();

        // Target must be collection
        if (target instanceof Collection) {

          // Get target value iterator
          Iterator<?> targetValues = ((Collection<?>)target).iterator();

          while (targetValues.hasNext()) {

            // Get next target value
            Object targetValue = targetValues.next();

            // Add target value
            if (targetValue instanceof DataObject) {
              values.add((DataObject<?>)targetValue);
            }
          }
        }

        // Set value iterator
        valueIterator = values.iterator();

        if (valueIterator.hasNext()) {

          // Set current row
          currentRow = 1;

          // Get first value
          currentValue = valueIterator.next();
        }
      }
    }

    return EVAL_BODY_INCLUDE;
  }

  /**
    Iterates through data objects containing table data
    allowing body evaluation to occur for each object.
    @see javax.servlet.jsp.tagext.BodyTag#doAfterBody
  */
  public int doAfterBody() throws JspException {

    // Initial status
    int status = SKIP_BODY;

    if (valueIterator != null && valueIterator.hasNext()) {

      // Increment current row
      currentRow++;

      // Get next value
      currentValue = valueIterator.next();

      // Set status to evaluate body again
      status = EVAL_BODY_AGAIN;
    }

    return status;
  }

  /**
    Writes the end of the table.
    @see javax.servlet.jsp.tagext.Tag#doEndTag
  */
  public int doEndTag() throws JspException {

    // Set method name
    String methodName = "doEndTag";

    // Get JSP writer
    JspWriter jspWriter = pageContext.getOut();

    try {
      // Write end table
      jspWriter.println("</table>");
    }
    catch (IOException ioException) {

      // Post error message
      Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.table");
      logger.logp(Level.SEVERE, getClass().getName(), methodName, "Writing End Table", ioException);

      throw new JspException(methodName + ":" + ioException.getMessage());
    }

    return EVAL_PAGE;
  }

  /**
    Cleans up resources each time tag is used.
    @see javax.servlet.jsp.tagext.TryCatchFinally#doFinally
  */
  public void doFinally() {

    super.doFinally();

    // Clear properties
    attributeName = null;
    fieldId = null;
    values.clear();
    valueIterator = null;
    currentRow = 0;
    currentValue = null;
  }

	/**
    Returns the list of data objects containing table data.
    @return ArrayList - List of data objects containing table data.
  */
  public ArrayList<DataObject<?>> getDataObjects() {
		return values;
	}

	/**
    Returns the first row indicator.
    @return boolean - First row indicator.
  */
  public boolean isFirstRow() {
    return currentRow == 1;
  }

  /**
    Returns the last row indicator.
    @return boolean - Last row indicator.
  */
  public boolean isLastRow() {
    return valueIterator == null || !valueIterator.hasNext();
  }

  /**
    Returns the current row for use in body evaluation.
    @return int - Current row.
  */
  public int getCurrentRow() {
    return currentRow;
  }

  /**
    Returns the current data object for use in body evaluation.
    @return DataObject - Data object.
  */
  public DataObject<?> getCurrentValue() {
    return currentValue;
  }

}
