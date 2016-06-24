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

import com.bws.jdistil.core.factory.IFactory;
import com.bws.jdistil.core.factory.IFactoryProvider;
import com.bws.jdistil.core.factory.PojoFactory;
import com.bws.jdistil.core.factory.PojoFactoryProvider;
import com.bws.jdistil.core.resource.ResourceUtil;
import com.bws.jdistil.core.util.Instantiator;
import com.bws.jdistil.core.util.ListUtil;
import com.bws.jdistil.core.util.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
  Configuration manager used by core framework classes to retrieve
  action, field, page, and object binding related information.
  @author Bryan Snipes
*/
public class ConfigurationManager {

  /**
    Map of factory provider objects keyed by package or class name.
  */
  private static Map<String, Class<? extends IFactoryProvider>> factoryProviderLookup = new HashMap<String, Class<? extends IFactoryProvider>>();
  
  /**
    Map of factory objects keyed by class name.
  */
  private static Map<String, IFactory> factoryLookup = new HashMap<String, IFactory>();

  /**
    Map of action objects keyed by action ID.
  */
  private static Map<String, Action> actionLookup = new HashMap<String, Action>();
  
  /**
    Map of field objects keyed by field ID.
  */
  private static Map<String, Field> fieldLookup = new HashMap<String, Field>();

  /**
    Map of page objects keyed by page ID.
  */
  private static Map<String, Page> pageLookup = new HashMap<String, Page>();
  
  /**
    Map of object bindings keyed by class.
  */
  private static Map<Class<?>, ObjectBinding> objectBindingLookup = new HashMap<Class<?>, ObjectBinding>();
  
  /**
    Static initializer used to load configuration information.
  */
  static {

    // Load configurations
    loadConfigurations();
  }

  /**
    Creates a new ConfigurationManager object. Defined with private access
    to avoid object instantiation.
  */
  private ConfigurationManager() {
    super();
  }

  /**
    Loads all application configurations.
  */
  private static final void loadConfigurations() {

  	// Load system configuration
  	loadConfiguration(new SystemConfiguration());
  	
    // Retrieve application configuration
    String applicationConfiguration = ResourceUtil.getString(Constants.APPLICATION_CONFIGURATION);

    if (!StringUtil.isEmpty(applicationConfiguration)) {

      // Parse to list of application configuration class names
      List<String> applicationConfigurationClassNames = ListUtil.parse(applicationConfiguration, ListUtil.COMMA, null);

      for (String applicationConfigurationClassName : applicationConfigurationClassNames) {

        // Create configuration
        Object object = Instantiator.create((String)applicationConfigurationClassName);

        if (object instanceof IConfiguration) {

          // Cast to configuration
        	IConfiguration configuration = (IConfiguration)object;

        	// Load configuration
        	loadConfiguration(configuration);
        }
      }
    }
  }

  /**
   * Loads a configuration.
   * @param configuration Configuration.
   */
  private static void loadConfiguration(IConfiguration configuration) {
  	
    // Create resource sets
    Map<String, Class<? extends IFactoryProvider>> factoryProviders = new HashMap<String, Class<? extends IFactoryProvider>>();
    Set<Action> actions = new HashSet<Action>();
    Set<Field> fields = new HashSet<Field>();
    Set<Page> pages = new HashSet<Page>();
    Set<ObjectBinding> objectBindings = new HashSet<ObjectBinding>();

    
    // Load factory provider configuration data
    configuration.registerFactoryProviders(factoryProviders);

    // Load factory provider map
    if (factoryProviders != null) {

      // Add factory to map
      factoryProviderLookup.putAll(factoryProviders);
    }

    
    // Load action configuration data
    configuration.registerActions(actions);

    // Load action map
    if (actions != null) {

      // Get action iterator
      Iterator<Action> actionIterator = actions.iterator();

      while (actionIterator.hasNext()) {

        // Get next action
        Action action = actionIterator.next();

        // Add action to map
        actionLookup.put(action.getId(), action);
      }
    }

    
    // Load field configuration data
    configuration.registerFields(fields);
    
    // Load field map
    if (fields != null) {

      // Get field iterator
      Iterator<Field> fieldIterator = fields.iterator();

      while (fieldIterator.hasNext()) {

        // Get next field
        Field field = fieldIterator.next();

        // Add field to map
        fieldLookup.put(field.getId(), field);
      }
    }

    
    // Load page configuration data
    configuration.registerPages(pages);

    // Load page map
    if (pages != null) {

      // Get page iterator
      Iterator<Page> pageIterator = pages.iterator();

      while (pageIterator.hasNext()) {

        // Get next page
        Page page = pageIterator.next();

        // Add page to map
        pageLookup.put(page.getId(), page);
      }
    }


    // Load object binding configuration data
    configuration.registerObjectBindings(objectBindings);

    // Load object binding map
    if (objectBindings != null) {

      // Get object binding iterator
      Iterator<ObjectBinding> objectBindingIterator = objectBindings.iterator();

      while (objectBindingIterator.hasNext()) {

        // Get next object binding
        ObjectBinding objectBinding = objectBindingIterator.next();

        // Add object binding to map
        objectBindingLookup.put(objectBinding.getTargetClass(), objectBinding);
      }
    }

  }
  
	/**
	  Returns the welcome page.
	  @return Page Login page.
	*/
	public static Page getWelcomePage() {
	
	  // Initialize return value
	  Page welcomePage = null;
	
	  // Get welcome page ID
	  String welcomePageId = ResourceUtil.getString(Constants.WELCOME_PAGE_ID);
	
	  // Get welcome page ID
	  if (!StringUtil.isEmpty(welcomePageId)) {
	    welcomePage = getPage(welcomePageId);
	  }
	
	  return welcomePage;
	}
	
	/**
    Returns the logon page.
    @return Page Logon page.
  */
  public static Page getLogonPage() {

    // Initialize return value
    Page logonPage = null;

    // Get logon page ID
    String logonPageId = ResourceUtil.getString(Constants.LOGON_PAGE_ID);

    // Get logon page ID
    if (!StringUtil.isEmpty(logonPageId)) {
      logonPage = getPage(logonPageId);
    }

    return logonPage;
  }

  /**
    Returns the error page.
    @return Page Error page.
  */
  public static Page getErrorPage() {

    // Initialize return value
    Page errorPage = null;

    // Get error page ID
    String errorPageId = ResourceUtil.getString(Constants.ERROR_PAGE_ID);

    // Get error page ID
    if (!StringUtil.isEmpty(errorPageId)) {
      errorPage = getPage(errorPageId);
    }

    return errorPage;
  }

  /**
    Returns a factory based on a target class.
    @param targetClass Target class.
    @return IFactory Factory object.
  */
  public static IFactory getFactory(Class<?> targetClass) {
  
    // Initialize factory
    IFactory factory = null;
    
    if (targetClass != null) {

    	// Get target class name
    	String targetClassName = targetClass.getName();
    	
    	// Attempt to lookup factory class
    	factory = factoryLookup.get(targetClassName);
    	
    	if (factory == null) {
    		
    		// Lookup a factory provider
    		IFactoryProvider factoryProvider = getFactoryProvider(targetClassName);
    		
    		if (factoryProvider != null) {
    			
    			// Create factory
    			factory = factoryProvider.create(targetClass);
    			
    			if (factory != null) {
    				
      			// Add factory to lookup
      			factoryLookup.put(targetClassName, factory);
    			}
    		}
    	}

      if (factory == null) {
      	
        // Create basic POJO factory if one is not defined
        factory = new PojoFactory(targetClass);
      }
    }
  
    return factory;
  }

  /**
  	Returns a factory provider based on a target class.
	  @param targetClassName Target class name.
	  @return IFactoryProvider Factory provider object.
	*/
	private static IFactoryProvider getFactoryProvider(String targetClassName) {
	
	  // Initialize factory provider
	  IFactoryProvider factoryProvider = null;
	  
	  if (!StringUtil.isEmpty(targetClassName)) {
	
	  	// Attempt to lookup factory provider by class name
	  	Class<? extends IFactoryProvider> factoryProviderClass = factoryProviderLookup.get(targetClassName);
	  	
	  	if (factoryProviderClass == null) {
	  		
	  		int index = targetClassName.lastIndexOf(".");
	  		
	  		if (index > 0) {
	  			
	  			// Get package name
	    		String targetPackageName = targetClassName.substring(0, index);
	    		
	    		// Attempt to lookup factory provider by package name
	    		factoryProviderClass = factoryProviderLookup.get(targetPackageName);
	    		
	    		while (factoryProviderClass == null) {
	    			
	      		index = targetPackageName.lastIndexOf(".");
	
	      		if (index > 0) {
	      			
	      			// Get sub-package name as target package name
	        		targetPackageName = targetPackageName.substring(0, index);
	        		
	        		// Attempt to lookup factory provider by sub-package name
	        		factoryProviderClass = factoryProviderLookup.get(targetPackageName);
	      		}
	      		else {
	      			
	      			break;
	      		}
	    		}
	  		}
	  	}
	  	
	
	    if (factoryProviderClass != null) {
	    	
	    	factoryProvider = (IFactoryProvider)Instantiator.create(factoryProviderClass);
	    	
	    }
	    else {
	      
	      // Create basic POJO factory provider if a factory provider is not defined
	      factoryProvider = new PojoFactoryProvider();
	    }
	  }
	
	  return factoryProvider;
	}

  /**
    Returns the action based on a action ID.
    @param actionId Action ID.
    @return Action Action object.
  */
  public static Action getAction(String actionId) {

    // Initialize return value
    Action action = null;

    // Lookup action
    if (!StringUtil.isEmpty(actionId)) {
      action = actionLookup.get(actionId);
    }

    return action;
  }

  /**
    Returns the field based on a field ID.
    @param fieldId Field ID.
    @return Field Field object.
  */
  public static Field getField(String fieldId) {

    // Initialize return value
    Field field = null;

    // Lookup field
    if (!StringUtil.isEmpty(fieldId)) {
      field = fieldLookup.get(fieldId);
    }

    return field;
  }

  /**
    Returns the page based on a page ID.
    @param pageId Page ID.
    @return Page Page object.
  */
  public static Page getPage(String pageId) {
  
    // Initialize return value
    Page page = null;
  
    // Lookup page
    if (!StringUtil.isEmpty(pageId)) {
      page = pageLookup.get(pageId);
    }
  
    return page;
  }
  
  /**
    Returns the object binding based on a class.
    @param targetClass Target class.
    @return ObjectBinding Object binding object.
  */
  public static ObjectBinding getObjectBinding(Class<?> targetClass) {
  
    // Initialize return value
    ObjectBinding objectBinding = null;
  
    // Lookup object binding
    if (targetClass != null) {
      objectBinding = objectBindingLookup.get(targetClass);
    }
  
    return objectBinding;
  }
  
}
