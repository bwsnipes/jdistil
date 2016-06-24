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
package com.bws.jdistil.core.datasource;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.FieldBinding;
import com.bws.jdistil.core.configuration.ObjectBinding;
import com.bws.jdistil.core.util.Introspector;

import java.io.Serializable;
import java.util.Set;

/**
  Base class used to store information.
  @author - Bryan Snipes
*/
public abstract class DataObject<I> implements Serializable {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -5546658892474312647L;

  /**
    Unique ID.
  */
  private I id = null;

  /**
    Indicates whether or not the data object has been modified.
  */
  private boolean isModified;

  /**
    Latest version of data object.
  */
  private Long version = new Long(1L);

  /**
    Creates a new DataObject object.
  */
  public DataObject() {
    super();
  }

  /**
    Returns the ID.
    @return Object - Unique ID.
  */
  public I getId() {
    return id;
  }

  /**
    Sets the unique ID.
    @param newId - New unique ID.
  */
  public void setId(I newId) {
    id = newId;
  }

  /**
    Returns the version of the data object.
    @return Long - Data object version.
  */
  public Long getVersion() {
    return version;
  }
  
  /**
    Sets the version of the data object.
    @param newVersion - New version of data object.
  */
  public void setVersion(Long newVersion) {
    version = newVersion;
  }
  
  /**
    Sets the modified status of the data object.
    @param newModifiedStatus - New modified status.
  */
  protected void setModified(boolean newModifiedStatus) {
    isModified = newModifiedStatus;
  }

  /**
    Indicates whether or not the data object has been modified.
    @return boolean - Modification status.
  */
  public boolean isModified() {
    return isModified;
  }

  /**
    Updates the modified status of the data object by checking for property
    changes given two property values.
    @param oldValue - Original property value.
    @param newValue - New property value.
  */
  protected void updateModifiedStatus(Object oldValue, Object newValue) {

    // Only compare values if the data object is not already modified
    if (!isModified) {

      // Compare old and new values for equality
      if (oldValue == null) {
        isModified = newValue != null;
      }
      else {
        isModified = newValue == null || !oldValue.equals(newValue);
      }
    }
  }

  /**
    Clears all properties.
  */
  public void clear() {

    // Get object binding
    ObjectBinding objectBinding = ConfigurationManager.getObjectBinding(getClass());

    if (objectBinding != null) {
      
      // Get field bindings
      Set<FieldBinding> fieldBindings = objectBinding.getFieldBindings();
      
      // Process all field bindings
      for (FieldBinding fieldBinding : fieldBindings) {
        
        // Get field ID
        String fieldId = fieldBinding.getFieldId();

        // Clear value
        Introspector.setFieldValue(this, fieldId, null);
      }
    }
  }

  /**
    Returns the string representation of the data object.
    @return String - Data object's string representation.
  */
  public String toString() {

    // Initialize return value
    StringBuffer text = new StringBuffer();

    // Default data
    text.append("Class: ").append(getClass().getName()).append("\n");
    text.append("Modified: ").append(new Boolean(isModified()).toString()).append("\n");

    return text.toString();
  }

}
