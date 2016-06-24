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
package com.bws.jdistil.core.process;

import com.bws.jdistil.core.util.StringUtil;

/**
  Class used to store different types of process messages.
  @author - Bryan Snipes
*/
public class ProcessMessage {

  /**
    Informational message type.
  */
  public static final String INFORMATIONAL = "info";

  /**
    Confirmation message type.
  */
  public static final String CONFIRMATION = "confirm";
  
  /**
    Warning message type.
  */
  public static final String WARNING = "warning";
  
  /**
    Error message type.
  */
  public static final String ERROR = "error";
  
  /**
    Message type.
  */
  private String type = null;
  
  /**
    Message text.
  */
  private String text = null;
  
  /**
    Creates a ProcessMessage using a message type and message text.
    @param type - Message type.
    @param text - Message text.
  */
  public ProcessMessage(String type, String text) {
    super();
    
    // Validate properties
    if (StringUtil.isEmpty(type)) {
      throw new IllegalArgumentException("Invalid null message type.");
    }
    if (StringUtil.isEmpty(text)) {
      throw new IllegalArgumentException("Invalid null message text.");
    }
    
    // Set properties
    this.type = type;
    this.text = text;
  }

  /**
    Returns the message type.
    @return String - Message type.
  */
  public String getType() {
    return type;
  }

  /**
    Returns the message text.
    @return String - Message text.
  */
  public String getText() {
    return text;
  }

}
