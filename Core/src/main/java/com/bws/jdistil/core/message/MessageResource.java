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
package com.bws.jdistil.core.message;

import java.util.ListResourceBundle;

/**
  Resource bundle class used to define all standard messages used throughout
  an application.
  @author - Bryan Snipes
*/
public class MessageResource extends ListResourceBundle {

  /**
    Two dimensional array of message keys and messages.
  */
  static final Object[][] contents = {
    {Messages.REQUIRED_FIELD, "{0} is a required field."},
    {Messages.NON_UNIQUE, "{0} is not unique."},
    {Messages.INVALID_FORMAT, "{0} has an invalid format. {1}"},
    {Messages.INVALID_NUMBER, "{0} is an invalid number."},
    {Messages.GREATER_THAN_VALUE, "{0} must be greater than {1}."},
    {Messages.GREATER_THAN_OR_EQUAL_VALUE, "{0} must be greater than or equal to {1}."},
    {Messages.LESS_THAN_VALUE, "{0} must be less than {1}."},
    {Messages.LESS_THAN_OR_EQUAL_VALUE, "{0} must be less than or equal to {1}."},
    {Messages.INVALID_EMAIL, "{0} is an invalid email address."},
    {Messages.GREATER_THAN_TODAYS_DATE, "{0} must be greater than today's date."},
    {Messages.LESS_THAN_TODAYS_DATE, "{0} must be less than today's date."},
    {Messages.LESS_THAN_CHARACTERS, "{0} must be less than {1} characters."},
    {Messages.INVALID_PRECISION, "{0} must contain less than {1} total digits."},
    {Messages.INVALID_SCALE, "{0} must contain less than {1} decimal places."},
    {Messages.DELETE_CONFIRMATION, "Do you want to delete the selected item(s)?"},
    {Messages.DUPLICATE_UPDATE, "Data already exists."},
    {Messages.DIRTY_UPDATE, "Data has been modified by another user."},
    {Messages.DELETED_UPDATE, "Data has been deleted by another user."},
    {Messages.MISSING_ACTION, "Missing action."},
    {Messages.AT_LEAST_ONE_REQUIRED_FIELD, "At least one of {0} is a required field."},
  };

  /**
    Returns all standard messages as a two dimensional array of message keys
    and messages.
    @return Object[][] - Two dimensional array of message keys and messages.
  */
  public Object[][] getContents() {
    return contents;
  }

}
