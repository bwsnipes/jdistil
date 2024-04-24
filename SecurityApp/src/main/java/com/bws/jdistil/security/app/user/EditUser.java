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
package com.bws.jdistil.security.app.user;

import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessException;
import com.bws.jdistil.core.process.model.EditDataObject;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.app.configuration.PageIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.role.RoleManager;
import com.bws.jdistil.security.user.User;
import com.bws.jdistil.security.user.UserManager;

/**
  Retrieves information needed to add/edit a user.
  @author - Bryan Snipes
*/
public class EditUser extends EditDataObject<Integer, User> {

  /**
    Creates a new EditUser object.
  */
  public EditUser() {
    super(User.class, UserManager.class, FieldIds.USER_ID, AttributeNames.USER, PageIds.USER, ViewUsers.class, false);
  }
  
  /**
    Populates the request attributes with reference data needed to add/edit a data object.
    @see com.bws.jdistil.core.process.model.EditDataObject#populateReferenceData
  */
  public void populateReferenceData(ProcessContext processContext) throws ProcessException {
  
  	super.populateReferenceData(processContext);

  	// Load reference data
    loadReferenceData(RoleManager.class, AttributeNames.ROLES, processContext);
  }
  
}
