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

import com.bws.jdistil.core.process.model.DeleteDataObject;
import com.bws.jdistil.security.app.configuration.FieldIds;
import com.bws.jdistil.security.user.User;
import com.bws.jdistil.security.user.UserManager;

/**
  Deletes a user using submitted data.
  @author - Bryan Snipes
*/
public class DeleteUser extends DeleteDataObject<Integer, User> {

  /**
    Creates a new DeleteUser object.
  */
  public DeleteUser() {
    super(UserManager.class, FieldIds.USER_ID, ViewUsers.class);
  }

}
