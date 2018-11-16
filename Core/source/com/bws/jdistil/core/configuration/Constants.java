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
package com.bws.jdistil.core.configuration;

/**
  Class defining all system level constants.
  @author - Bryan Snipes
*/
public class Constants {

  /**
    Constant defining the environment system property name.
  */
  public static final String ENV_PROPERTY_NAME = "env";

  /**
    Constant defining the resource properties file.
  */
  public static final String PROPERTY_FILE = "core";

  /**
    Constant defining the JNDI initial context factory property key.
  */
  public static final String INITIAL_CONTEXT_FACTORY = "initial.context.factory";

  /**
    Constant defining the datasource name property key.
  */
  public static final String DATASOURCE_NAME = "datasource.name";

  /**
    Constant defining the multiple tenant property key.
  */
  public static final String MULTIPLE_TENANTS = "multiple.tenants";
  
  /**
    Constant defining the SMTP host name property key.
  */
  public static final String SMTP_HOST_NAME = "smtp.host.name";

  /**
    Constant defining the sequencer table name property key.
  */
  public static final String SEQUENCER_TABLE_NAME = "sequencer.table.name";

  /**
    Constant defining the default welcome page ID property key.
  */
  public static final String WELCOME_PAGE_ID = "welcome.page.id";
  
  /**
    Constant defining the login page ID property key.
  */
  public static final String LOGON_PAGE_ID = "logon.page.id";
  
  /**
    Constant defining the error page ID property key.
  */
  public static final String ERROR_PAGE_ID = "error.page.id";
  
  /**
    Constant defining the application configuration resource property key.
  */
  public static final String APPLICATION_CONFIGURATION = "application.configuration";
  
  /**
  	Constant defining the description resources property key.
  */
  public static final String DESCRIPTION_RESOURCES = "description.resources";
  
  /**
    Constant defining the message resources property key.
  */
  public static final String MESSAGE_RESOURCES = "message.resources";

  /**
    Constant defining the operator resources property key.
  */
  public static final String OPERATOR_RESOURCES = "operator.resources";
  
  /**
    Constant defining the format resources property key.
  */
  public static final String FORMAT_RESOURCES = "format.resources";
  
  /**
    Constant defining the security manager factory property key.
  */
  public static final String SECURITY_MANAGER_FACTORY = "security.manager.factory";

  /**
    Constant defining the validation factory property key.
  */
  public static final String VALIDATOR_FACTORY = "validator.factory";

  /**
    Constant defining the default page size property key.
  */
  public static final String DEFAULT_PAGE_SIZE = "default.page.size";

  /**
    Constant defining the previous page image source property key.
  */
  public static final String PREVIOUS_PAGE_IMAGE_SOURCE = "previous.page.image.source";

  /**
    Constant defining the next page image source property key.
  */
  public static final String NEXT_PAGE_IMAGE_SOURCE = "next.page.image.source";

  /**
    Constant defining the sort ascending image source property key.
  */
  public static final String SORT_ASCENDING_IMAGE_SOURCE = "sort.ascending.image";
  
  /**
    Constant defining the sort descending image source property key.
  */
  public static final String SORT_DESCENDINGE_IMAGE_SOURCE = "sort.descending.image";
  
  /**
    Creates a new Constants object.
  */
  protected Constants() {
    super();
  }
  
}
