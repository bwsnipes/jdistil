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

/**
  Defines factory methods used to create and recycle objects.
  @author - Bryan Snipes
*/
public interface IFactory {

  /**
    Returns the target class.
  */
  public Class<?> getTargetClass();
  
  /**
    Returns an instance of the target class from the object pool.
    @return Object - Instance of the target class.
  */
  public Object create();

  /**
    Returns an object to the object pool.
    @param object - Object.
  */
  public void recycle(Object object);

}

