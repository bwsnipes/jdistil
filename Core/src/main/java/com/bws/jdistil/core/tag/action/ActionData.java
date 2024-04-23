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

import javax.servlet.jsp.JspException;

import com.bws.jdistil.core.tag.data.ValueComponent;

/**
  Action data component used in the bodies of tags descending from ActionComponent
  to specify form field data to populate before submitting the form.
  @author - Bryan Snipes
*/
public class ActionData extends ValueComponent {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = 4688725438850236121L;

  /**
    Creates an ActionData object.
  */
  public ActionData() {
    super();
  }

  /**
    Registers action data with an enclosing action component.
    @see javax.servlet.jsp.tagext.Tag#doStartTag
  */
  public int doStartTag() throws JspException {

    // Check for enclosing action component
    ActionComponent actionComponent = (ActionComponent)findAncestorWithClass(this, ActionComponent.class);

    if (actionComponent != null) {

      // Register action data
      actionComponent.addActionData(this);
    }

    return SKIP_BODY;
  }

}
