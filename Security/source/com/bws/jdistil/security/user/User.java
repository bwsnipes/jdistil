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
package com.bws.jdistil.security.user;

import com.bws.jdistil.core.datasource.DataObject;
import com.bws.jdistil.core.tag.data.IListItem;
import com.bws.jdistil.core.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
  Data object used to store user data.
  @author Bryan Snipes
*/
public class User extends DataObject<Integer> implements IListItem, Comparable<User> {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = 5299432667045774056L;

  /**
    Logon ID.
  */
  private String logonId = null;

  /**
    Salt value.
  */
  private String salt = null;
  
  /**
    Password.
  */
  private String password = null;
  
  /**
    First name.
  */
  private String firstName = null;
  
  /**
    Middle initial.
  */
  private String middleInitial = null;
  
  /**
    Last name.
  */
  private String lastName = null;
  
  /**
    Deleted indicator.
  */
  private Boolean isDeleted = Boolean.FALSE;
  
  /**
    List of role IDs.
  */
  private List<Integer> roleIds = new ArrayList<Integer>();
  
  /**
    Creates a new User object.
  */
  public User() {
    super();
  }

  /**
    Returns the logon ID.
    @return String Logon ID.
  */
  public String getLogonId() {
    return logonId;
  }
  
  /**
    Sets the logon ID.
    @param newLogonId New logon ID.
  */
  public void setLogonId(String newLogonId) {
    updateModifiedStatus(logonId, newLogonId);
    logonId = newLogonId;
  }

  /**
    Returns the password.
    @return String Password.
  */
  public String getPassword() {
    return password;
  }
  
  /**
    Sets the password.
    @param newPassword New password.
  */
  public void setPassword(String newPassword) {
    updateModifiedStatus(password, newPassword);
    password = newPassword;
  }

  /**
    Returns the salt.
    @return String Salt.
  */
  public String getSalt() {
    return salt;
  }
  
  /**
    Sets the salt.
    @param newSalt New salt.
  */
  public void setSalt(String newSalt) {
    updateModifiedStatus(salt, newSalt);
    salt = newSalt;
  }
  
  /**
    Returns the first name.
    @return String First name.
  */
  public String getFirstName() {
    return firstName;
  }

  /**
    Sets the first name.
    @param newFirstName New first name.
  */
  public void setFirstName(String newFirstName) {
    updateModifiedStatus(firstName, newFirstName);
    firstName = newFirstName;
  }

  /**
    Returns the middle initial.
    @return String Middle initial.
  */
  public String getMiddleInitial() {
    return middleInitial;
  }
  
  /**
    Sets the middle initial.
    @param newMiddleInitial New middle initial.
  */
  public void setMiddleInitial(String newMiddleInitial) {
    updateModifiedStatus(middleInitial, newMiddleInitial);
    middleInitial = newMiddleInitial;
  }

  /**
    Returns the last name.
    @return String Last name.
  */
  public String getLastName() {
    return lastName;
  }
  
  /**
    Sets the last name.
    @param newLastName New last name.
  */
  public void setLastName(String newLastName) {
    updateModifiedStatus(lastName, newLastName);
    lastName = newLastName;
  }
  
  /**
    Returns the list of role IDs.
    @return List List of role IDs.
  */
  public List<Integer> getRoleIds() {
    return roleIds;
  }
  
  /**
    Sets the list of role IDs.
    @param newRoleIds New list of role IDs.
  */
  public void setRoleIds(List<Integer> newRoleIds) {
    
    // Clear existing values
    roleIds.clear();
    
    // Set new role IDs
    if (newRoleIds != null) {
      roleIds.addAll(newRoleIds);
    }
  }
  
  /**
    Returns the default value indicator.
    @return Boolean - Default value indicator.
  */
  public Boolean getIsDefault() {
  	return Boolean.FALSE;
  }
  
  /**
    Returns the deleted indicator.
    @return Boolean Deleted indicator.
  */
  public Boolean getIsDeleted() {
    return isDeleted;
  }
  
  /**
    Sets the deleted indicator.
    @param newIsDeleted New deleted indicator.
  */
  public void setIsDeleted(Boolean newIsDeleted) {
    updateModifiedStatus(isDeleted, newIsDeleted);
    isDeleted = newIsDeleted;
  }
  
  /**
    Returns the user ID as the item value.
    @see com.bws.jdistil.core.tag.data.IListItem#getValue
  */
  public Object getValue() {
    return getId();
  }
  
  /**
    Returns the user's name as the item description.
    @see com.bws.jdistil.core.tag.data.IListItem#getDescription
  */
  public String getDescription() {
    
    // Initialize return value
    String name = "";
    
    // Ensure first name and last name are available
    if (!StringUtil.isEmpty(firstName) && !StringUtil.isEmpty(lastName)) {
      
      // Check for middle initial
      if (!StringUtil.isEmpty(middleInitial)) {
        name = firstName + " " + middleInitial + " " + lastName;
      }
      else {
        name = firstName + " " + lastName;
      }
    }
    
    return name;
  }
  
  /**
    Compares two user objects based on their names.
    @see Comparable#compareTo
  */
  public int compareTo(User user) {

    // Initialize return value
    int status = 0;

    if (user != null) {

      // Get user descriptions
      String description1 = getDescription();
      String description2 = user.getDescription();

      // Compare user descriptions
      if (description1 != null && description2 == null) {
        status = 1;
      }
      else if (description1 == null && description2 != null) {
        status = -1;
      }
      else if (description1 != null && description2 != null) {
        status = description1.compareTo(description2);
      }
    }

    return status;
  }

}
