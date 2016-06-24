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
package com.bws.jdistil.core.factory;

import com.bws.jdistil.core.util.Instantiator;

/**
  Factory implemented using pojo's.
  @author - Bryan Snipes
*/
public class PojoFactory implements IFactory {

  /**
    Target class.
  */
  private Class<?> targetClass = null;

  /**
    Creates a new PojoFactory using a target class.
    @param targetClass - Target class.
  */
  public PojoFactory(Class<?> targetClass) {
    super();

    // Set properties
    this.targetClass = targetClass;
  }

  /**
    Returns the target class.
  */
  public Class<?> getTargetClass() {
    return targetClass;
  }

  /**
    Returns an instance of the target class.
    @return Object - Instance of the target class.
    @see com.bws.jdistil.core.factory.IFactory#create
  */
  public Object create() {
    return Instantiator.create(targetClass);
  }

  /**
    Returns an object.
    @param object - Object.
    @see com.bws.jdistil.core.factory.IFactory#recycle
  */
  public void recycle(Object object) {
    // Do nothing
  }
  
}

