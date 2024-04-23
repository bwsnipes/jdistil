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
 * Interface supporting the definition of application configurations.
 * @author - Bryan Snipes
 */
public interface IConfiguration {
  
	/**
	  Registers factory providers used by an application by package or class name.
	  @param factoryProviders - Set of factory providers keyed by package or class name.
	*/
	public void registerFactoryProviders(Map<String, Class<? extends IFactoryProvider>> factoryProviders);
	
	/**
	  Registers fields used by an application.
	  @param fields - Set of fields to be populated.
	*/
	public void registerFields(Set<Field> fields);
	
	/**
	  Registers actions used by an application.
	  @param actions - Set of actions to be populated.
	*/
	public void registerActions(Set<Action> actions);
	
	/**
	  Registers pages used by an application.
	  @param pages - Set of pages to be populated.
	*/
	public void registerPages(Set<Page> pages);
	
	/**
	  Registers object bindings used by an application.
	  @param objectBindings - Set of object bindings to be populated.
	*/
	public void registerObjectBindings(Set<ObjectBinding> objectBindings);

}
