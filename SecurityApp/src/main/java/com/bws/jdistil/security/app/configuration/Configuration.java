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
package com.bws.jdistil.security.app.configuration;

import java.util.Set;

import com.bws.jdistil.core.configuration.Action;
import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.configuration.ObjectBinding;
import com.bws.jdistil.core.configuration.Page;
import com.bws.jdistil.core.validation.rules.AtLeastOneFieldRule;
import com.bws.jdistil.security.app.domain.ChangeDomain;
import com.bws.jdistil.security.app.domain.DeleteDomain;
import com.bws.jdistil.security.app.domain.EditDomain;
import com.bws.jdistil.security.app.domain.SaveDomain;
import com.bws.jdistil.security.app.domain.ViewChangeDomain;
import com.bws.jdistil.security.app.domain.ViewDomains;
import com.bws.jdistil.security.app.role.DeleteRole;
import com.bws.jdistil.security.app.role.EditRole;
import com.bws.jdistil.security.app.role.SaveRole;
import com.bws.jdistil.security.app.role.SelectGroup;
import com.bws.jdistil.security.app.role.ViewRoles;
import com.bws.jdistil.security.app.user.ChangePassword;
import com.bws.jdistil.security.app.user.DeleteUser;
import com.bws.jdistil.security.app.user.EditUser;
import com.bws.jdistil.security.app.user.Logoff;
import com.bws.jdistil.security.app.user.Logon;
import com.bws.jdistil.security.app.user.SaveUser;
import com.bws.jdistil.security.app.user.ViewChangePassword;
import com.bws.jdistil.security.app.user.ViewLogon;
import com.bws.jdistil.security.app.user.ViewUsers;
import com.bws.jdistil.security.domain.Domain;
import com.bws.jdistil.security.user.User;

/**
  Defines configuration for the security application.
  @author Bryan Snipes
*/
public class Configuration extends com.bws.jdistil.security.configuration.Configuration {

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
    Field domainSortField = new Field(FieldIds.DOMAIN_SORT_FIELD, Field.STRING, "Sort Field", null);
    Field domainSortDirection = new Field(FieldIds.DOMAIN_SORT_DIRECTION, Field.STRING, "Sort Direction", null);
    Field domainCurrentPageNumber = new Field(FieldIds.DOMAIN_CURRENT_PAGE_NUMBER, Field.STRING, "Current Page Number", null);
    Field domainSelectedPageNumber = new Field(FieldIds.DOMAIN_SELECTED_PAGE_NUMBER, Field.STRING, "Selected Page Number", null);
    Field domainGroupState = new Field(FieldIds.DOMAIN_GROUP_STATE, Field.STRING, "Group State", null);
    Field domainNameFilter = new Field(FieldIds.DOMAIN_NAME_FILTER, Field.STRING, "Name", null);
    Field domainNameFilterOperator = new Field(FieldIds.DOMAIN_NAME_FILTER_OPERATOR, Field.STRING, "Operator", null);
    Field domainSelectedId = new Field(FieldIds.DOMAIN_SELECTED_ID, Field.INTEGER, "Domain", null);
    Field userAuthenticationId = new Field(FieldIds.USER_AUTHENTICATION_ID, Field.STRING, "Logon ID", null);
    Field userAuthenticationPassword = new Field(FieldIds.USER_AUTHENTICATION_PASSWORD, Field.STRING, "Password", null);
    Field userAuthenticationNewPassword = new Field(FieldIds.USER_AUTHENTICATION_NEW_PASSWORD, Field.STRING, "New Password", null);
    Field userAuthenticationConfirmPassword = new Field(FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD, Field.STRING, "Confirm Password", null);
    Field userAuthenticationDomainId = new Field(FieldIds.USER_AUTHENTICATION_DOMAIN_ID, Field.INTEGER, "Domain", null);
    Field userGroupState = new Field(FieldIds.USER_GROUP_STATE, Field.STRING, "Group State", null);
    Field userCurrentPageNumber = new Field(FieldIds.USER_CURRENT_PAGE_NUMBER, Field.INTEGER, "Current Page Number", null);
    Field userSelectedPageNumber = new Field(FieldIds.USER_SELECTED_PAGE_NUMBER, Field.INTEGER, "Selected Page Number", null);
    Field userSortDirection = new Field(FieldIds.USER_SORT_DIRECTION, Field.STRING, "Sort Direction", null);
    Field userSortField = new Field(FieldIds.USER_SORT_FIELD, Field.STRING, "Sort Field", null);
    Field firstNameFilter = new Field(FieldIds.FIRST_NAME_FILTER, Field.STRING, "First Name", null);
    Field firstNameFilterOperator = new Field(FieldIds.FIRST_NAME_FILTER_OPERATOR, Field.STRING, "Operator", null);
    Field lastNameFilter = new Field(FieldIds.LAST_NAME_FILTER, Field.STRING, "Last Name", null);
    Field lastNameFilterOperator = new Field(FieldIds.LAST_NAME_FILTER_OPERATOR, Field.STRING, "Operator", null);
    Field roleSortDirection = new Field(FieldIds.ROLE_SORT_DIRECTION, Field.STRING, "Sort Direction", null);
    Field roleSortField = new Field(FieldIds.ROLE_SORT_FIELD, Field.STRING, "Sort Field", null);
    
    // Register fields
    fields.add(domainSortField);
    fields.add(domainSortDirection);
    fields.add(domainCurrentPageNumber);
    fields.add(domainSelectedPageNumber);
    fields.add(domainGroupState);
    fields.add(domainNameFilter);
    fields.add(domainNameFilterOperator);
    fields.add(domainSelectedId);
    fields.add(userAuthenticationId);
    fields.add(userAuthenticationPassword);
    fields.add(userAuthenticationNewPassword);
    fields.add(userAuthenticationConfirmPassword);
    fields.add(userAuthenticationDomainId);
    fields.add(userGroupState);
    fields.add(userCurrentPageNumber);
    fields.add(userSelectedPageNumber);
    fields.add(userSortDirection);
    fields.add(userSortField);
    fields.add(firstNameFilter);
    fields.add(firstNameFilterOperator);
    fields.add(lastNameFilter);
    fields.add(lastNameFilterOperator);
    fields.add(roleSortDirection);
    fields.add(roleSortField);
  }
  
  /**
    Registers actions used by an application.
    @param actions Set of actions to be populated.
  */
  @Override
  public void registerActions(Set<Action> actions) {
  
    super.registerActions(actions);
    
    // Create actions
    Action viewDomains = new Action(ActionIds.VIEW_DOMAINS, "Apply");
    Action addDomain = new Action(ActionIds.ADD_DOMAIN, "Add");
    Action editDomain = new Action(ActionIds.EDIT_DOMAIN, "Edit");
    Action deleteDomain = new Action(ActionIds.DELETE_DOMAIN, "Delete");
    Action saveDomain = new Action(ActionIds.SAVE_DOMAIN, "Save");
    Action cancelDomain = new Action(ActionIds.CANCEL_DOMAIN, "Cancel");
    Action viewChangeDomain = new Action(ActionIds.VIEW_CHANGE_DOMAIN, "View");
    Action changeDomain = new Action(ActionIds.CHANGE_DOMAIN, "Save");
    Action viewDomainPreviousPage = new Action(ActionIds.VIEW_DOMAIN_PREVIOUS_PAGE, "Previous Page");
    Action viewDomainSelectPage = new Action(ActionIds.VIEW_DOMAIN_SELECT_PAGE, "Select Page");
    Action viewDomainNextPage = new Action(ActionIds.VIEW_DOMAIN_NEXT_PAGE, "Next Page");
    Action viewRoles = new Action(ActionIds.VIEW_ROLES, "Apply");
    Action deleteRole = new Action(ActionIds.DELETE_ROLE, "Delete");
    Action addRole = new Action(ActionIds.ADD_ROLE, "Add");
    Action editRole = new Action(ActionIds.EDIT_ROLE, "Edit");
    Action selectGroup = new Action(ActionIds.SELECT_GROUP, "Select");
    Action saveRole = new Action(ActionIds.SAVE_ROLE, "Save");
    Action cancelRole = new Action(ActionIds.CANCEL_ROLE, "Cancel");
    Action viewUsers = new Action(ActionIds.VIEW_USERS, "Apply");
    Action viewUserPreviousPage = new Action(ActionIds.VIEW_USER_PREVIOUS_PAGE, "Previous");
    Action viewUserSelectPage = new Action(ActionIds.VIEW_USER_SELECT_PAGE, "Select");
    Action viewUserNextPage = new Action(ActionIds.VIEW_USER_NEXT_PAGE, "Next");
    Action deleteUser = new Action(ActionIds.DELETE_USER, "Delete");
    Action addUser = new Action(ActionIds.ADD_USER, "Add");
    Action editUser = new Action(ActionIds.EDIT_USER, "Edit");
    Action saveUser = new Action(ActionIds.SAVE_USER, "Save");
    Action cancelUser = new Action(ActionIds.CANCEL_USER, "Cancel");
    Action viewLogon = new Action(ActionIds.VIEW_LOGON, "View");
    Action logon = new Action(ActionIds.LOGON, "Logon");
    Action logoff = new Action(ActionIds.LOGOFF, "Logoff");
    Action viewChangePassword = new Action(ActionIds.VIEW_CHANGE_PASSWORD, "View");
    Action changePassword = new Action(ActionIds.CHANGE_PASSWORD, "Save");

    // Add action processor factories
    viewDomains.addProcessorFactory(ConfigurationManager.getFactory(ViewDomains.class));
    viewDomainPreviousPage.addProcessorFactory(ConfigurationManager.getFactory(ViewDomains.class));
    viewDomainSelectPage.addProcessorFactory(ConfigurationManager.getFactory(ViewDomains.class));
    viewDomainNextPage.addProcessorFactory(ConfigurationManager.getFactory(ViewDomains.class));
    deleteDomain.addProcessorFactory(ConfigurationManager.getFactory(DeleteDomain.class));
    addDomain.addProcessorFactory(ConfigurationManager.getFactory(EditDomain.class));
    editDomain.addProcessorFactory(ConfigurationManager.getFactory(EditDomain.class));
    saveDomain.addProcessorFactory(ConfigurationManager.getFactory(SaveDomain.class));
    cancelDomain.addProcessorFactory(ConfigurationManager.getFactory(ViewDomains.class));
    viewChangeDomain.addProcessorFactory(ConfigurationManager.getFactory(ViewChangeDomain.class));
    changeDomain.addProcessorFactory(ConfigurationManager.getFactory(ChangeDomain.class));
    viewRoles.addProcessorFactory(ConfigurationManager.getFactory(ViewRoles.class));
    deleteRole.addProcessorFactory(ConfigurationManager.getFactory(DeleteRole.class));
    addRole.addProcessorFactory(ConfigurationManager.getFactory(EditRole.class));
    editRole.addProcessorFactory(ConfigurationManager.getFactory(EditRole.class));
    selectGroup.addProcessorFactory(ConfigurationManager.getFactory(SelectGroup.class));
    saveRole.addProcessorFactory(ConfigurationManager.getFactory(SaveRole.class));
    cancelRole.addProcessorFactory(ConfigurationManager.getFactory(ViewRoles.class));
    viewUsers.addProcessorFactory(ConfigurationManager.getFactory(ViewUsers.class));
    viewUserPreviousPage.addProcessorFactory(ConfigurationManager.getFactory(ViewUsers.class));
    viewUserSelectPage.addProcessorFactory(ConfigurationManager.getFactory(ViewUsers.class));
    viewUserNextPage.addProcessorFactory(ConfigurationManager.getFactory(ViewUsers.class));
    deleteUser.addProcessorFactory(ConfigurationManager.getFactory(DeleteUser.class));
    addUser.addProcessorFactory(ConfigurationManager.getFactory(EditUser.class));
    editUser.addProcessorFactory(ConfigurationManager.getFactory(EditUser.class));
    saveUser.addProcessorFactory(ConfigurationManager.getFactory(SaveUser.class));
    cancelUser.addProcessorFactory(ConfigurationManager.getFactory(ViewUsers.class));
    viewLogon.addProcessorFactory(ConfigurationManager.getFactory(ViewLogon.class));
    logon.addProcessorFactory(ConfigurationManager.getFactory(Logon.class));
    logoff.addProcessorFactory(ConfigurationManager.getFactory(Logoff.class));
    viewChangePassword.addProcessorFactory(ConfigurationManager.getFactory(ViewChangePassword.class));
    changePassword.addProcessorFactory(ConfigurationManager.getFactory(ChangePassword.class));
    
    // Add action fields
    editDomain.addField(FieldIds.DOMAIN_ID, true);    
    deleteDomain.addField(FieldIds.DOMAIN_ID, true);    
    saveDomain.addField(FieldIds.DOMAIN_ID, false);    
    saveDomain.addField(FieldIds.DOMAIN_VERSION, false);    
    saveDomain.addField(FieldIds.DOMAIN_NAME, true);    
    saveDomain.addField(FieldIds.DOMAIN_IS_DEFAULT_DATASOURCE, false);    
    saveDomain.addField(FieldIds.DOMAIN_DATASOURCE_NAME, false);
    changeDomain.addField(FieldIds.DOMAIN_SELECTED_ID, false);
    editRole.addField(FieldIds.ROLE_ID, true);
    selectGroup.addField(FieldIds.ROLE_ID, false);
    selectGroup.addField(FieldIds.ROLE_NAME, true);
    selectGroup.addField(FieldIds.ROLE_RESTRICTED_TASK_IDS, false);
    selectGroup.addField(FieldIds.ROLE_RESTRICTED_FIELD_IDS, false);
    selectGroup.addField(FieldIds.ROLE_READ_ONLY_FIELD_IDS, false);
    selectGroup.addField(FieldIds.ROLE_VERSION, false);
    deleteRole.addField(FieldIds.ROLE_ID, true);
    saveRole.addField(FieldIds.ROLE_ID, false);
    saveRole.addField(FieldIds.ROLE_NAME, true);
    saveRole.addField(FieldIds.ROLE_RESTRICTED_TASK_IDS, false);
    saveRole.addField(FieldIds.ROLE_RESTRICTED_FIELD_IDS, false);
    saveRole.addField(FieldIds.ROLE_READ_ONLY_FIELD_IDS, false);
    saveRole.addField(FieldIds.ROLE_VERSION, false);
    editUser.addField(FieldIds.USER_ID, true);
    deleteUser.addField(FieldIds.USER_ID, true);
    saveUser.addField(FieldIds.USER_ID, false);
    saveUser.addField(FieldIds.USER_FIRST_NAME, true);
    saveUser.addField(FieldIds.USER_MIDDLE_INITIAL, false);
    saveUser.addField(FieldIds.USER_LAST_NAME, true);
    saveUser.addField(FieldIds.USER_LOGON_ID, true);
    saveUser.addField(FieldIds.USER_AUTHENTICATION_NEW_PASSWORD, false);
    saveUser.addField(FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD, false);
    saveUser.addField(FieldIds.USER_IS_DOMAIN_ADMIN, false);
    saveUser.addField(FieldIds.USER_ROLE_IDS, false);
    saveUser.addField(FieldIds.USER_VERSION, false);
    logon.addField(FieldIds.USER_AUTHENTICATION_ID, true);
    logon.addField(FieldIds.USER_AUTHENTICATION_PASSWORD, true);
    changePassword.addField(FieldIds.USER_AUTHENTICATION_PASSWORD, true);
    changePassword.addField(FieldIds.USER_AUTHENTICATION_NEW_PASSWORD, true);
    changePassword.addField(FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD, true);

    // Add action rules
    saveDomain.addRule(new AtLeastOneFieldRule(FieldIds.DOMAIN_IS_DEFAULT_DATASOURCE, FieldIds.DOMAIN_DATASOURCE_NAME));
    
    // Register actions
    actions.add(viewDomains);
    actions.add(viewDomainPreviousPage);
    actions.add(viewDomainSelectPage);
    actions.add(viewDomainNextPage);
    actions.add(deleteDomain);
    actions.add(addDomain);
    actions.add(editDomain);
    actions.add(saveDomain);
    actions.add(cancelDomain);
    actions.add(viewChangeDomain);
    actions.add(changeDomain);
    actions.add(viewRoles);
    actions.add(deleteRole);
    actions.add(addRole);
    actions.add(editRole);
    actions.add(selectGroup);
    actions.add(saveRole);
    actions.add(cancelRole);
    actions.add(viewUsers);
    actions.add(viewUserPreviousPage);
    actions.add(viewUserSelectPage);
    actions.add(viewUserNextPage);
    actions.add(deleteUser);
    actions.add(addUser);
    actions.add(editUser);
    actions.add(saveUser);
    actions.add(cancelUser);
    actions.add(viewLogon);
    actions.add(logon);
    actions.add(logoff);
    actions.add(viewChangePassword);
    actions.add(changePassword);
  }
  
  /**
    Registers pages used by an application.
    @param pages Set of pages to be populated.
  */
  @Override
  public void registerPages(Set<Page> pages) {
  
    super.registerPages(pages);
    
    // Create pages
    Page logon = new Page(PageIds.LOGON, "/security/Logon.jsp", "Logon", false);
    Page changePassword = new Page(PageIds.CHANGE_PASSWORD, "/security/ChangePassword.jsp", "Change Password", true);
    Page changeDomain = new Page(PageIds.CHANGE_DOMAIN, "/security/ChangeDomain.jsp", "Change Domain", true);
    Page domains = new Page(PageIds.DOMAINS, "/security/Domains.jsp", "Domains", true);    
    Page domain = new Page(PageIds.DOMAIN, "/security/Domain.jsp", "Domain", true);    
    Page roles = new Page(PageIds.ROLES, "/security/Roles.jsp", "Roles", true);
    Page role = new Page(PageIds.ROLE, "/security/Role.jsp", "Role", true);
    Page users = new Page(PageIds.USERS, "/security/Users.jsp", "Users", true);
    Page user = new Page(PageIds.USER, "/security/User.jsp", "User", true);
    
    // Register pages
    pages.add(logon);
    pages.add(changePassword);
    pages.add(changeDomain);
    pages.add(domains);
    pages.add(domain);
    pages.add(roles);
    pages.add(role);
    pages.add(users);
    pages.add(user);
  }
  
  /**
    Registers object bindings used by an application.
    @param objectBindings Set of object bindings to be populated.
  */
  @Override
  public void registerObjectBindings(Set<ObjectBinding> objectBindings) {
    
    super.registerObjectBindings(objectBindings);
    
    for (ObjectBinding objectBinding : objectBindings) {
      
      if (objectBinding.getTargetClass().equals(Domain.class)) {
        
        // Add search fields to existing user object binding
        ObjectBinding userBinding = new ObjectBinding(User.class);
        userBinding.addFieldBinding(FieldIds.DOMAIN_NAME_FILTER, "Name");
      }
      else if (objectBinding.getTargetClass().equals(User.class)) {
        
        // Add search fields to existing user object binding
        ObjectBinding userBinding = new ObjectBinding(User.class);
        userBinding.addFieldBinding(FieldIds.FIRST_NAME_FILTER, "FirstName");
        userBinding.addFieldBinding(FieldIds.LAST_NAME_FILTER, "LastName");
      }
    }
  }
  
}
