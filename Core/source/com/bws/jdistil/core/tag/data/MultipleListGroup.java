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
package com.bws.jdistil.core.tag.data;

import java.util.Collection;
import javax.servlet.jsp.JspException;

/**
  Component used to specify a target list for use with a MultipleListField component.
  @author Bryan Snipes
*/
public class MultipleListGroup extends ValueComponent {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -2257585405714091724L;

  /**
    Creates a new MultipleListGroup object.
  */
  public MultipleListGroup() {
    super();
  }

  /**
    Writes selectable data using multiple HTML select fields.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Check for enclosing multiple list field component
    MultipleListField multipleListField = (MultipleListField)findAncestorWithClass(this, MultipleListField.class);

    if (multipleListField != null) {

      // Get field ID
      String fieldId = getFieldId();

      // Initialize hidden and read only indicators
      boolean isHidden = isHidden(fieldId);
      boolean isReadOnly = isReadOnly(fieldId);

      // Retrieve field value
      Collection<Object> values = getSelectedValues();

      // Register component with multiple list field
      multipleListField.registerGroup(fieldId, isReadOnly, isHidden, values);
    }
    
    return SKIP_BODY;
  }

  /**
   * Returns the collection of selected values.
   * @return Collection<Object> Collection of objects.
   */
  private Collection<Object> getSelectedValues() {
    return getFieldValues();
  }

}
