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
package com.bws.jdistil.core.tag;

import com.bws.jdistil.core.CoreException;

/**
  Package level exception defined as a base exception for the UI package.
  @author - Bryan Snipes
*/
public class UiException extends CoreException {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -8451058034586255505L;

  /**
    Creates an empty UiException object.
  */
  public UiException() {
    super();
  }

  /**
    Creates a UiException intialize with a given message.
    @param message - Exception message.
  */
  public UiException(String message) {
    super(message);
  }

}
