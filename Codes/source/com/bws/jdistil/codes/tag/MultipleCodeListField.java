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
package com.bws.jdistil.codes.tag;

import com.bws.jdistil.codes.lookup.Code;
import com.bws.jdistil.codes.lookup.CodeManager;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.datasource.DataSourceException;
import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.tag.UiException;
import com.bws.jdistil.core.tag.data.IListItem;
import com.bws.jdistil.core.tag.data.MultipleListField;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Component used to write selectable data using a combination of HTML select field.
  @author Bryan Snipes
*/
public class MultipleCodeListField extends MultipleListField {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -7696102797785561255L;

  /**
    Category ID.
  */
  protected Integer categoryId = null;
  
  /**
    Creates a new MultipleCodeListField object.
  */
  public MultipleCodeListField() {
    super();
  }

  /**
    Sets the category ID.
    @param newCategoryId New items attribute name.
  */
  public void setCategoryId(Integer newCategoryId) {
    categoryId = newCategoryId;
  }
  
  /**
   * Returns a list of list items objects.
   * @return List List of list item objects.
   */
  protected List<IListItem> getItems() throws UiException {

    // Set method name
    String methodName = "getItems";

    // Initialize return value
    @SuppressWarnings("unchecked")
		List<IListItem> items = (List<IListItem>)pageContext.getRequest().getAttribute(categoryId.toString());
    
    if (items == null) {

      // Retrieve values
      if (categoryId != null) {
        
        // Initialize code manager
        CodeManager codeManager = null;

        // Check for a registered factory
        IFactory codeManagerFactory = ConfigurationManager.getFactory(CodeManager.class);

        // Create code manager
        codeManager = (CodeManager)codeManagerFactory.create();

        // Initialize codes list
        List<Code> codes = null;
        
        try {
          // Retrieve codes
          codes = codeManager.findByCategory(categoryId);
        }
        catch (DataSourceException dataSourceException) {
          
          // Post error message
          Logger logger = Logger.getLogger("com.bws.jdistil.codes.tag");
          logger.logp(Level.SEVERE, getClass().getName(), methodName, "Building Multiple List Field", dataSourceException);

          throw new UiException(methodName + ":" + dataSourceException.getMessage());
        }
        finally {

          // Recycle code manager
          if (codeManagerFactory != null) {
            codeManagerFactory.recycle(codeManager);
          }
        }
          
        if (codes != null) {
          
          // Create list of items
          items = new ArrayList<IListItem>();
          
          // Add codes to list of items
          for (Code code : codes) {
            items.add(code);
          }
          
          // Store items for future reference
          pageContext.getRequest().setAttribute(categoryId.toString(), items);
        }
      }
    }

    return items;
  }

}
