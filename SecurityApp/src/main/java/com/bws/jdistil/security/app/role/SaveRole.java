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
package com.bws.jdistil.security.app.role;

import com.bws.jdistil.core.process.model.SaveDataObject;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.configuration.AttributeNames;
import com.bws.jdistil.security.role.Role;
import com.bws.jdistil.security.role.RoleManager;

/**
  Saves a role.
  @author - Bryan Snipes
*/
public class SaveRole extends SaveDataObject<Integer, Role> {

  /**
    Creates a new SaveRole object.
  */
  public SaveRole() {
    super(Role.class, RoleManager.class, FieldIds.ROLE_ID, AttributeNames.ROLE, 
        true, ViewRoles.class, EditRole.class, true);
  }

}
