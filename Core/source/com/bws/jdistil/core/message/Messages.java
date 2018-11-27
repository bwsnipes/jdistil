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

import java.text.MessageFormat;
import java.util.Locale;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.StringUtil;

/**
  Class defining message constants used to retrieve messages from the messages resource bundle.
  @see MessageResource
  @author - Bryan Snipes
*/
public class Messages {

  /**
    Required field message constant.
  */
  public static final String REQUIRED_FIELD = "M1";

  /**
    Non-unique field message constant.
  */
  public static final String NON_UNIQUE = "M2";

  /**
    Invalid field format message constant.
  */
  public static final String INVALID_FORMAT = "M3";

  /**
    Invalid numeric field message constant.
  */
  public static final String INVALID_NUMBER = "M4";

  /**
    Greater than value message constant.
  */
  public static final String GREATER_THAN_VALUE = "M5";

  /**
    Greater than or equal value message constant.
  */
  public static final String GREATER_THAN_OR_EQUAL_VALUE = "M6";

  /**
    Less than value message constant.
  */
  public static final String LESS_THAN_VALUE = "M7";

  /**
    Less than or equal value message constant.
  */
  public static final String LESS_THAN_OR_EQUAL_VALUE = "M8";

  /**
    Invalid email address message constant.
  */
  public static final String INVALID_EMAIL = "M9";

  /**
    Greater than today's date message constant.
  */
  public static final String GREATER_THAN_TODAYS_DATE = "M10";

  /**
    Less than today's date message constant.
  */
  public static final String LESS_THAN_TODAYS_DATE = "M11";

  /**
    Less than characters message constant.
  */
  public static final String LESS_THAN_CHARACTERS = "M12";

  /**
    Invalid precision message constant.
  */
  public static final String INVALID_PRECISION = "M13";

  /**
    Invalid scale message constant.
  */
  public static final String INVALID_SCALE = "M14";
  
  /**
    Delete confirmation message constant.
  */
  public static final String DELETE_CONFIRMATION = "M15";

  /**
	  Begins With operator description message constant.
	*/
	public static final String BEGINS_WITH_OPERATOR = "M16";
	
  /**
	  Contains operator description message constant.
	*/
	public static final String CONTAINS_OPERATOR = "M17";
	
	/**
		Ends With operator description message constant.
	*/
	public static final String ENDS_WITH_OPERATOR = "M18";
	
	/**
		Equals operator description message constant.
	*/
	public static final String EQUAL_OPERATOR = "M19";
	
	/**
		Not Equals operator description message constant.
	*/
	public static final String NOT_EQUAL_OPERATOR = "M20";
	
	/**
		Greater Than operator description message constant.
	*/
	public static final String GREATER_THAN_OPERATOR = "M21";
	
	/**
		Greater Than or Equal operator description message constant.
	*/
	public static final String GREATER_THAN_EQUAL_OPERATOR = "M22";
	
	/**
		Less Than operator description message constant.
	*/
	public static final String LESS_THAN_OPERATOR = "M23";
	
	/**
		Less Than or Equal operator description message constant.
	*/
	public static final String LESS_THAN_OR_EQUAL_OPERATOR = "M24";
	
	/**
  	Duplicate update description message constant.
  */
  public static final String DUPLICATE_UPDATE = "M25";
  
	/**
  	Dirty update description message constant.
  */
  public static final String DIRTY_UPDATE = "M26";
  
	/**
  	Deleted update description message constant.
  */
  public static final String DELETED_UPDATE = "M27";
  
	/**
  	Missing action description message constant.
  */
  public static final String MISSING_ACTION = "M28";
  
  /**
    At least one required field message constant.
  */
  public static final String AT_LEAST_ONE_REQUIRED_FIELD = "M29";
  
	/**
    Creates a new Messages object. Defined with private access to prevent instantiation.
  */
  private Messages() {
    super();
  }
  
  /**
    Returns a formatted standard predefined message.&nbsp The detailKey is used
    to retrieve a standard predefined message that is formatted using information
    supplied in the value parameter.&nbsp Provided for convience when using
    standard messages requiring only one value parameter.
    @param locale - Locale.
    @param messageKey - Key used to retrieve a standard predefined message.
    @param value - Value used to format a standard predefined message.
    @return String - Formatted standard predefined message.
    @see MessageResource
  */
  public static String formatMessage(Locale locale, String messageKey, Object value) {
  
    // Build values array
    Object[] values = new Object[1];
    values[0] = value;
  
    return formatMessage(locale, messageKey, values);
  }
  
  /**
    Returns a formatted standard predefined message. The detailKey is used
    to retrieve a standard predefined message that is formatted using information
    supplied in the values parameter.
    @param locale - Locale.
    @param type - Message type used to retrieve a standard predefined message.
    @param values - Array of values used to format a standard predefined message.
    @return String - Formatted standard predefined message.
    @see MessageResource
  */
  public static String formatMessage(Locale locale, String type, Object[] values) {
  
    // Initialize return value
    String formattedMessage = "";
  
    // Retrieve message using message key
    String message = getMessage(type, locale);

    // Format message using values
    if (!StringUtil.isEmpty(message)) {
      formattedMessage = MessageFormat.format(message, values);
    }
  
    return formattedMessage;
  }
  
  /**
    Returns a locale specific message for a given message type.
    @param type Message type.
	  @param locale Target locale.
    @return String Locale specific message.
  */
  public static String getMessage(String type, Locale locale) {
  
    // Initialize message
    String message = null;
    
    // Attempt to retrieve message resources name from core properties
    String messageResources = ResourceUtil.getString(Constants.MESSAGE_RESOURCES);

    if (messageResources != null) {

      // Attempt to retrieve locale specific message
    	message = ResourceUtil.getString(messageResources, locale, type);
    }
      
    if (StringUtil.isEmpty(message)) {
    	
      // Retrieve default message
    	message = ResourceUtil.getString(MessageResource.class.getName(), locale, type);
    }
  
    return message;
  }

}
