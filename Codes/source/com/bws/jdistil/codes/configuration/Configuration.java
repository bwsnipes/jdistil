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
package com.bws.jdistil.codes.configuration;

import java.util.Set;

import com.bws.jdistil.codes.configuration.FieldIds;
import com.bws.jdistil.codes.lookup.Category;
import com.bws.jdistil.codes.lookup.Code;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.configuration.ObjectBinding;
import com.bws.jdistil.core.conversion.BooleanConverter;

/**
  Defines configuration for the codes components.
  @author - Bryan Snipes
*/
public class Configuration extends com.bws.jdistil.core.configuration.Configuration {

  /**
    Creates a new ConfigurationData object.
  */
  public Configuration() {
    super();
  }

  /**
    Register fields.
    @param fields - Set of fields to be populated.
  */
  @Override
  public void registerFields(Set<Field> fields) {
    
    super.registerFields(fields);
    
    // Create fields
    Field categoryId = new Field(FieldIds.CATEGORY_ID, Field.INTEGER, "Category", null);
    Field categoryName = new Field(FieldIds.CATEGORY_NAME, Field.STRING, "Name", null);
    Field codeId = new Field(FieldIds.CODE_ID, Field.INTEGER, "Code", null);
    Field codeName = new Field(FieldIds.CODE_NAME, Field.STRING, "Name", null);
    Field isDefaultCode = new Field(FieldIds.IS_DEFAULT_CODE, Field.BOOLEAN, "Default", BooleanConverter.getInstance());
    Field codeVersion = new Field(FieldIds.CODE_VERSION, Field.LONG, "Version", null);
    
    // Register fields
    fields.add(categoryId);
    fields.add(categoryName);
    fields.add(codeId);
    fields.add(codeName);
    fields.add(isDefaultCode);
    fields.add(codeVersion);
  }

  /**
    Registers object bindings used by an application.
    @param objectBindings - Set of object bindings to be populated.
  */
  @Override
  public void registerObjectBindings(Set<ObjectBinding> objectBindings) {
    
    super.registerObjectBindings(objectBindings);
    
    // Create category binding
    ObjectBinding categoryBinding = new ObjectBinding(Category.class);
    categoryBinding.addFieldBinding(FieldIds.CATEGORY_ID, "Id");
    categoryBinding.addFieldBinding(FieldIds.CATEGORY_NAME, "Name");
    
    // Create code binding
    ObjectBinding codeBinding = new ObjectBinding(Code.class);
    codeBinding.addFieldBinding(FieldIds.CODE_ID, "Id");
    codeBinding.addFieldBinding(FieldIds.CODE_NAME, "Name");
    codeBinding.addFieldBinding(FieldIds.IS_DEFAULT_CODE, "IsDefault");
    codeBinding.addFieldBinding(FieldIds.CATEGORY_ID, "CategoryId");
    codeBinding.addFieldBinding(FieldIds.CODE_VERSION, "Version");
    
    // Register object bindings
    objectBindings.add(categoryBinding);
    objectBindings.add(codeBinding);
  }
  
}
