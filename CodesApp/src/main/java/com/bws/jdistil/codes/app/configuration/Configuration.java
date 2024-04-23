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
package com.bws.jdistil.codes.app.configuration;

import java.util.Set;

import com.bws.jdistil.codes.app.configuration.FieldIds;
import com.bws.jdistil.codes.app.lookup.DeleteCode;
import com.bws.jdistil.codes.app.lookup.EditCode;
import com.bws.jdistil.codes.app.lookup.SaveCode;
import com.bws.jdistil.codes.app.lookup.ViewCodes;
import com.bws.jdistil.codes.lookup.Code;
import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.configuration.ObjectBinding;
import com.bws.jdistil.core.configuration.Page;

/**
  Defines configuration for the codes application.
  @author - Bryan Snipes
*/
public class Configuration extends com.bws.jdistil.codes.configuration.Configuration {

  /**
    Creates a new Configuration object.
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
    Field codeGroupState = new Field(FieldIds.CODE_GROUP_STATE, Field.STRING, "Group State", null);
    Field codeCurrentPageNumber = new Field(FieldIds.CODE_CURRENT_PAGE_NUMBER, Field.INTEGER, "Current Page Number", null);
    Field codeSelectedPageNumber = new Field(FieldIds.CODE_SELECTED_PAGE_NUMBER, Field.INTEGER, "Selected Page Number", null);
    Field codeSortDirection = new Field(FieldIds.CODE_SORT_DIRECTION, Field.STRING, "Sort Direction", null);
    Field codeSortField = new Field(FieldIds.CODE_SORT_FIELD, Field.STRING, "Sort Field", null);
    Field categoryIdFilter = new Field(FieldIds.CATEGORY_ID_FILTER, Field.INTEGER, "Category", null);
    
    // Register fields
    fields.add(codeGroupState);
    fields.add(codeCurrentPageNumber);
    fields.add(codeSelectedPageNumber);
    fields.add(codeSortDirection);
    fields.add(codeSortField);
    fields.add(categoryIdFilter);
  }
  
  /**
    Registers actions used by an application.
    @param actions - Set of actions to be populated.
  */
  @Override
  public void registerActions(Set<Action> actions) {

    super.registerActions(actions);
    
    // Create actions
    Action viewCodes = new Action(ActionIds.VIEW_CODES, "Apply");
    Action viewCodesPreviousPage = new Action(ActionIds.VIEW_CODES_PREVIOUS_PAGE, "Previous");
    Action viewCodesSelectPage = new Action(ActionIds.VIEW_CODES_SELECT_PAGE, "Select");
    Action viewCodesNextPage = new Action(ActionIds.VIEW_CODES_NEXT_PAGE, "Next");
    Action deleteCode = new Action(ActionIds.DELETE_CODE, "Delete");
    Action addCode = new Action(ActionIds.ADD_CODE, "Add");
    Action editCode = new Action(ActionIds.EDIT_CODE, "Edit");
    Action saveCode = new Action(ActionIds.SAVE_CODE, "Save");
    Action cancelCode = new Action(ActionIds.CANCEL_CODE, "Cancel");

    // Add action processor factories
    viewCodes.addProcessorFactory(ConfigurationManager.getFactory(ViewCodes.class));
    viewCodesPreviousPage.addProcessorFactory(ConfigurationManager.getFactory(ViewCodes.class));
    viewCodesSelectPage.addProcessorFactory(ConfigurationManager.getFactory(ViewCodes.class));
    viewCodesNextPage.addProcessorFactory(ConfigurationManager.getFactory(ViewCodes.class));
    addCode.addProcessorFactory(ConfigurationManager.getFactory(EditCode.class));
    editCode.addProcessorFactory(ConfigurationManager.getFactory(EditCode.class));
    deleteCode.addProcessorFactory(ConfigurationManager.getFactory(DeleteCode.class));
    saveCode.addProcessorFactory(ConfigurationManager.getFactory(SaveCode.class));
    cancelCode.addProcessorFactory(ConfigurationManager.getFactory(ViewCodes.class));

    // Add action fields
    editCode.addField(FieldIds.CODE_ID, true);
    deleteCode.addField(FieldIds.CODE_ID, true);
    saveCode.addField(FieldIds.CODE_ID, false);
    saveCode.addField(FieldIds.CATEGORY_ID, true);
    saveCode.addField(FieldIds.CODE_NAME, true);
    saveCode.addField(FieldIds.IS_DEFAULT_CODE, false);
    saveCode.addField(FieldIds.CODE_VERSION, false);
    
    // Register actions
    actions.add(viewCodes);
    actions.add(viewCodesPreviousPage);
    actions.add(viewCodesSelectPage);
    actions.add(viewCodesNextPage);
    actions.add(deleteCode);
    actions.add(addCode);
    actions.add(editCode);
    actions.add(saveCode);
    actions.add(cancelCode);
  }

  /**
    Registers pages used by an application.
    @param pages - Set of pages to be populated.
  */
  @Override
  public void registerPages(Set<Page> pages) {

    super.registerPages(pages);
    
    // Create pages
    Page codes = new Page(PageIds.CODES, "/codes/Codes.jsp", "Codes", true);
    Page code = new Page(PageIds.CODE, "/codes/Code.jsp", "Code", true);
    
    // Register pages
    pages.add(codes);
    pages.add(code);
  }

  /**
    Registers object bindings used by an application.
    @param objectBindings Set of object bindings to be populated.
  */
  @Override
  public void registerObjectBindings(Set<ObjectBinding> objectBindings) {
    
    super.registerObjectBindings(objectBindings);
    
    for (ObjectBinding objectBinding : objectBindings) {
      
      if (objectBinding.getTargetClass().equals(Code.class)) {
        
        // Add search fields to existing code object binding
        ObjectBinding codeBinding = new ObjectBinding(Code.class);
        codeBinding.addFieldBinding(FieldIds.CATEGORY_ID_FILTER, "CategoryId");
      }
    }
  }

}
