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
package com.bws.jdistil.security.configuration;

import java.util.Set;

import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.configuration.ObjectBinding;
import com.bws.jdistil.security.configuration.FieldIds;
import com.bws.jdistil.security.role.Action;
import com.bws.jdistil.security.role.Group;
import com.bws.jdistil.security.role.Role;
import com.bws.jdistil.security.role.Task;
import com.bws.jdistil.security.user.User;

/**
  Defines configuration for the codes components.
  @author - Bryan Snipes
*/
public class Configuration extends com.bws.jdistil.core.configuration.Configuration {

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
    Field actionId = new Field(FieldIds.ACTION_ID, Field.INTEGER, "Action", null);
    Field actionSecureId = new Field(FieldIds.ACTION_SECURE_ID, Field.STRING, "Secure ID", null);
    Field groupId = new Field(FieldIds.GROUP_ID, Field.INTEGER, "Group", null);
    Field groupName = new Field(FieldIds.GROUP_NAME, Field.STRING, "Name", null);
    Field fieldId = new Field(FieldIds.FIELD_ID, Field.INTEGER, "Field", null);
    Field fieldName = new Field(FieldIds.FIELD_NAME, Field.STRING, "Name", null);
    Field fieldSecureId = new Field(FieldIds.FIELD_SECURE_ID, Field.STRING, "Secure ID", null);
    Field roleId = new Field(FieldIds.ROLE_ID, Field.INTEGER, "Role", null);
    Field roleName = new Field(FieldIds.ROLE_NAME, Field.STRING, "Name", null);
    Field roleRestrictedTaskIds = new Field(FieldIds.ROLE_RESTRICTED_TASK_IDS, Field.INTEGER, "Restricted Tasks", null);
    Field roleRestrictedFieldIds = new Field(FieldIds.ROLE_RESTRICTED_FIELD_IDS, Field.INTEGER, "Restricted Fields", null);
    Field roleReadOnlyFieldIds = new Field(FieldIds.ROLE_READ_ONLY_FIELD_IDS, Field.INTEGER, "Read Only Fields", null);
    Field roleVersion = new Field(FieldIds.ROLE_VERSION, Field.LONG, "Version", null);
    Field taskId = new Field(FieldIds.TASK_ID, Field.INTEGER, "Task", null);
    Field taskName = new Field(FieldIds.TASK_NAME, Field.STRING, "Name", null);
    Field userId = new Field(FieldIds.USER_ID, Field.INTEGER, "User", null);
    Field userFirstName = new Field(FieldIds.USER_FIRST_NAME, Field.STRING, "First Name", null);
    Field userMiddleName = new Field(FieldIds.USER_MIDDLE_INITIAL, Field.STRING, "Middle Initial", null);
    Field userLastName = new Field(FieldIds.USER_LAST_NAME, Field.STRING, "Last Name", null);
    Field userLogonId = new Field(FieldIds.USER_LOGON_ID, Field.STRING, "Logon ID", null);
    Field userRoleIds = new Field(FieldIds.USER_ROLE_IDS, Field.INTEGER, "Roles", null);
    Field userVersion = new Field(FieldIds.USER_VERSION, Field.LONG, "Version", null);
  
    // Register fields
    fields.add(actionId);
    fields.add(actionSecureId);
    fields.add(groupId);
    fields.add(groupName);
    fields.add(fieldId);
    fields.add(fieldName);
    fields.add(fieldSecureId);
    fields.add(roleId);
    fields.add(roleName);
    fields.add(roleRestrictedTaskIds);
    fields.add(roleRestrictedFieldIds);
    fields.add(roleReadOnlyFieldIds);
    fields.add(roleVersion);
    fields.add(taskId);
    fields.add(taskName);
    fields.add(userId);
    fields.add(userFirstName);
    fields.add(userMiddleName);
    fields.add(userLastName);
    fields.add(userLogonId);
    fields.add(userRoleIds);
    fields.add(userVersion);
  }
  
  /**
    Registers object bindings used by an application.
    @param objectBindings - Set of object bindings to be populated.
  */
  @Override
  public void registerObjectBindings(Set<ObjectBinding> objectBindings) {
    
    super.registerObjectBindings(objectBindings);
    
    // Create action binding
    ObjectBinding actionBinding = new ObjectBinding(Action.class);
    actionBinding.addFieldBinding(FieldIds.ACTION_ID, "Id");
    actionBinding.addFieldBinding(FieldIds.ACTION_SECURE_ID, "SecureId");
    actionBinding.addFieldBinding(FieldIds.TASK_ID, "TaskId");
    
    // Create group binding
    ObjectBinding groupBinding = new ObjectBinding(Group.class);
    groupBinding.addFieldBinding(FieldIds.GROUP_ID, "Id");
    groupBinding.addFieldBinding(FieldIds.GROUP_NAME, "Name");

    // Create field binding
    ObjectBinding fieldBinding = new ObjectBinding(com.bws.jdistil.security.role.Field.class);
    fieldBinding.addFieldBinding(FieldIds.FIELD_ID, "Id");
    fieldBinding.addFieldBinding(FieldIds.FIELD_NAME, "Name");
    fieldBinding.addFieldBinding(FieldIds.FIELD_SECURE_ID, "SecureId");
    fieldBinding.addFieldBinding(FieldIds.GROUP_ID, "GroupId");

    // Create role binding
    ObjectBinding roleBinding = new ObjectBinding(Role.class);
    roleBinding.addFieldBinding(FieldIds.ROLE_ID, "Id");
    roleBinding.addFieldBinding(FieldIds.ROLE_NAME, "Name");
    roleBinding.addFieldBinding(FieldIds.ROLE_RESTRICTED_TASK_IDS, "RestrictedTaskIds", true);
    roleBinding.addFieldBinding(FieldIds.ROLE_RESTRICTED_FIELD_IDS, "RestrictedFieldIds", true);
    roleBinding.addFieldBinding(FieldIds.ROLE_READ_ONLY_FIELD_IDS, "ReadOnlyFieldIds", true);
    roleBinding.addFieldBinding(FieldIds.ROLE_VERSION, "Version");
    
    // Create task binding
    ObjectBinding taskBinding = new ObjectBinding(Task.class);
    taskBinding.addFieldBinding(FieldIds.TASK_ID, "Id");
    taskBinding.addFieldBinding(FieldIds.TASK_NAME, "Name");
    
    // Create user binding
    ObjectBinding userBinding = new ObjectBinding(User.class);
    userBinding.addFieldBinding(FieldIds.USER_ID, "Id");
    userBinding.addFieldBinding(FieldIds.USER_LOGON_ID, "LogonId");
    userBinding.addFieldBinding(FieldIds.USER_FIRST_NAME, "FirstName");
    userBinding.addFieldBinding(FieldIds.USER_MIDDLE_INITIAL, "MiddleInitial");
    userBinding.addFieldBinding(FieldIds.USER_LAST_NAME, "LastName");
    userBinding.addFieldBinding(FieldIds.USER_ROLE_IDS, "RoleIds", true);
    userBinding.addFieldBinding(FieldIds.USER_VERSION, "Version");
    
    // Register object bindings
    objectBindings.add(actionBinding);
    objectBindings.add(groupBinding);
    objectBindings.add(fieldBinding);
    objectBindings.add(roleBinding);
    objectBindings.add(taskBinding);
    objectBindings.add(userBinding);
  }
  
}
