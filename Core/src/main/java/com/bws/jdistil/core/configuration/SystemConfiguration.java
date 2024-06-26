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
package com.bws.jdistil.core.configuration;

import java.util.Set;

/**
  Configuration responsible for loading framework dependent application resources.
  @author - Bryan Snipes
*/
public class SystemConfiguration extends Configuration {

  /**
    Creates a new SystemConfiguration object.
  */
  public SystemConfiguration() {
    super();
  }

  /**
   * Registers fields used by an application.
   * @param fields - Set of fields to be populated.
   */
  public void registerFields(Set<Field> fields) {
  	
    // Create core fields
    Field parentFieldId = new Field(FieldIds.PARENT_FIELD_ID, Field.STRING, "Parent Field ID", null);
    Field parentActionId = new Field(FieldIds.PARENT_ACTION_ID, Field.STRING, "Parent Actione ID", null);
    Field parentPageId = new Field(FieldIds.PARENT_PAGE_ID, Field.STRING, "Parent Page ID", null);
    
    // Register core fields
    fields.add(parentFieldId);
    fields.add(parentActionId);
    fields.add(parentPageId);
  }
  
}
