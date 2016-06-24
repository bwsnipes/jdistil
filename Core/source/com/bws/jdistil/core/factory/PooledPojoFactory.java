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

import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
  Factory implemented using an object pool.
  @author - Bryan Snipes
*/
public class PooledPojoFactory implements IFactory {

  /**
    Default pool size.
  */
  private static final int DEFAULT_SIZE = 100;

  /**
    Target class.
  */
  private Class<?> targetClass = null;

  /**
    Pool size.
  */
  private int size = DEFAULT_SIZE;

  /**
    Initializer used to initialize objects returned to pool.
  */
  private IInitializer initializer = null;

  /**
    Available objects.
  */
  private Vector<Object> availableObjects = new Vector<Object>(DEFAULT_SIZE);

  /**
    Locked objects.
  */
  private Hashtable<Integer, LockReference> lockedObjects = new Hashtable<Integer, LockReference>(DEFAULT_SIZE);

  /**
    Reference queue used to cleanup unreleased objects.
  */
  private ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();

  /**
    Factory object poller.
  */
  private static final Poller poller = new Poller();

  /**
    Creates a new PooledPojoFactory using a target class.
    @param targetClass - Target class.
  */
  public PooledPojoFactory(Class<?> targetClass) {
    this(targetClass, DEFAULT_SIZE, null);
  }

  /**
    Creates a new PooledPojoFactory object using a target class, pool size, and initializer.
    @param targetClass - Target class.
    @param size - Pool size.
    @param initializer - Object initializer.
  */
  public PooledPojoFactory(Class<?> targetClass, int size, IInitializer initializer) {
    super();

    // Set properties
    this.targetClass = targetClass;
    this.size = size;
    this.initializer = initializer;

    // Populate pool with objects
    populatePool();
    
    // Register with the poller
    poller.register(this);
  }

  /**
    Populates the available objects pool.
  */
  private void populatePool() {

    for (int index = 0; index < size; index++) {

      // Create object instance
      Object object = Instantiator.create(targetClass);

      // Add to available objects
      availableObjects.add(object);
    }
  }

  /**
    Returns the target class.
  */
  public Class<?> getTargetClass() {
    return targetClass;
  }
  
  /**
    Returns an instance of the target class from the object pool.
    @return Object - Instance of the target class.
    @see com.bws.jdistil.core.factory.IFactory#create
  */
  public Object create() {

    // Initialize return value
  	Object object = null;

    try {
      // Attempt to retrieve first object
      object = availableObjects.remove(0);
    }
    catch (ArrayIndexOutOfBoundsException indexException) {

      // Create object if pool was empty
      object = Instantiator.create(targetClass);
    }

    // Create lock reference
    LockReference lockReference = new LockReference(object, referenceQueue);

    // Add to locked objects
    lockedObjects.put(lockReference.getId(), lockReference);

    return object;
  }

  /**
    Returns an object to the object pool.
    @param object - Object.
    @see com.bws.jdistil.core.factory.IFactory#recycle
  */
  public void recycle(Object object) {

    if (object != null) {

      // Create target lock reference
      LockReference targetReference = new LockReference(object, referenceQueue);

      // Remove from locked objects
      LockReference lockReference = (LockReference)lockedObjects.remove(targetReference.getId());

      // Add to available objects if not exceeding size
      if (lockReference != null && availableObjects.size() < size) {

        // Get referent
      	Object referent = lockReference.get();

        if (referent != null) {

          // Re-initialize object state
          initialize(referent);

          // Add to available objects
          availableObjects.add(referent);
        }
      }
    }
  }
  
  /**
    Initializes a given object.
    @param object - Object to initialize.
  */
  private void initialize(Object object) {
    
    if (initializer != null) {

      synchronized(initializer) {
        initializer.initialize(object);
      }
    }
  }
  
  /**
    Polls the reference queue for objects reclaimed by the garbage collector
    and adjusts the available objects queue accordingly.
  */
  private void poll() {

    // Set method name
    String methodName = "poll";
    
    // Initialize recycle violation indicator
    boolean isRecycleViolation = false;
    
    // Poll reference queue
    Reference<? extends Object> reference = referenceQueue.poll();
    
    while (reference != null) {

      // Set recycle violation
      isRecycleViolation = true;
      
      // Add to available objects if not exceeding size
      if (availableObjects.size() < size) {
        
        // Attempt to get referent
      	Object referent = reference.get();
        
        if (referent != null) {

          // Re-initialize object state
          initialize(referent);
          
          // Add referent back to available list
          availableObjects.add(referent);
        }
        else {
          
          // Add new object to available list
          availableObjects.add(Instantiator.create(targetClass));
        }
      }
      
      // Poll reference queue again
      reference = referenceQueue.poll();
    }

    if (isRecycleViolation) {

      // Post warning
      Logger logger = Logger.getLogger("com.bws.jdistil.core.factory");
      logger.logp(Level.WARNING, getClass().getName(), methodName, "Factory Object Not Recycled");
    }
  }


/**
  Extension of a weak reference used to compare referents for equality.
*/
private class LockReference extends WeakReference<Object> {

  /**
    Lock reference ID.
  */
  private Integer id = null;

  /**
    Creates a new LockReference object using a referent.
    @param referent - Referent object.
  */
  public LockReference(Object referent) {
    super(referent);

    // Set ID using referent's hash code
    id = new Integer(referent.hashCode());
  }

  /**
    Creates a new LockReference object using a referent and reference queue.
    @param referent - Referent object.
    @param referenceQueue - Reference queue.
  */
  public LockReference(Object referent, ReferenceQueue<Object> referenceQueue) {
    super(referent, referenceQueue);

    // Set ID using referent's hash code
    id = new Integer(referent.hashCode());
  }

  /**
    Returns the lock reference ID.
    @return Integer - Lock reference ID.
  */
  public Integer getId() {
    return id;
  }

  /**
    Compares this object with another lock reference for equality based on referents.
    @see java.lang.Object#equals
  */
  public boolean equals(Object object) {

    // Initialize return value
    boolean isEqual = false;

    if (object != null && object instanceof LockReference) {

      // Cast to lock reference
			LockReference lockReference = (LockReference)object;

      // Get referents
      Object referent1 = get();
      Object referent2 = lockReference.get();

      // Set equal indicator
      isEqual = referent1 == referent2;
    }

    return isEqual;
  }

  /**
    Returns the hash code using the referent.
    @see java.lang.Object#hashCode
  */
  public int hashCode() {
    return get().hashCode();
  }

}

/**
  Polls all registered pool factories.
*/
private static class Poller extends Thread {

  /**
    List of registered pooled pojo factories.
  */
  private Vector<PooledPojoFactory> pooledPojoFactories = new Vector<PooledPojoFactory>();

  /**
    Creates a new Poller object.
  */
  public Poller() {
    super();
    
    setDaemon(true);
    setPriority(Thread.MIN_PRIORITY);
    start();
  }

  /**
    Registers a pool factory.
    @param pooledPojoFactory - Pooled pojo factory.
  */
  public void register(PooledPojoFactory pooledPojoFactory) {
    
    if (pooledPojoFactory != null) {

    	synchronized(pooledPojoFactories) {

        // Add factory to collection
    		pooledPojoFactories.add(pooledPojoFactory);
      }
    }
  }
  
  /**
    Polls the reference queue of each registered factory
    for objects reclaimed by the garbage collector.
  */
  public void run() {

    while (true) {

      if (pooledPojoFactories != null && !pooledPojoFactories.isEmpty()) {
        
        synchronized(pooledPojoFactories) {

          // Invoke poll on each pool factory
          for (PooledPojoFactory poolFactory : pooledPojoFactories) {
            poolFactory.poll();
          }
        }
        
        try {
          Thread.sleep(30000);
        }
        catch (InterruptedException interruptedException) {
          // Do nothing
        }
      }
    }
  }

}

}

