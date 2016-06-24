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

import java.util.Map;
import java.util.Set;

import com.bws.jdistil.core.factory.IFactoryProvider;

/**
  Abstract base class used by applications to define configuration.
  Each application should extend this class and register application specific
  factories, fields, actions, pages and object bindings. The application 
  specific class name should be defined in the core properties file.
  @author - Bryan Snipes
*/
public abstract class Configuration implements IConfiguration {

  /**
    Creates a new Configuration object.
  */
  public Configuration() {
    super();
  }

  /**
    Registers factory providers used by an application by package or class name.
    @param factoryProviders - Set of factory providers keyed by package or class name.
  */
  public void registerFactoryProviders(Map<String, Class<? extends IFactoryProvider>> factoryProviders) {
  	
  	// Default implementation in case descendant does not need to implement.
  }
  
  /**
    Registers fields used by an application.
    @param fields - Set of fields to be populated.
  */
  public void registerFields(Set<Field> fields) {
  	
  	// Default implementation in case descendant does not need to implement.
  }
  
  /**
    Registers actions used by an application.
    @param actions - Set of actions to be populated.
  */
  public void registerActions(Set<Action> actions) {
  	
  	// Default implementation in case descendant does not need to implement.
  }

  /**
    Registers pages used by an application.
    @param pages - Set of pages to be populated.
  */
  public void registerPages(Set<Page> pages) {
  	
  	// Default implementation in case descendant does not need to implement.
  }

  /**
    Registers object bindings used by an application.
    @param objectBindings - Set of object bindings to be populated.
  */
  public void registerObjectBindings(Set<ObjectBinding> objectBindings) {
  	
  	// Default implementation in case descendant does not need to implement.
  }

}
