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
package com.bws.jdistil.core.util;

import java.util.List;
import java.util.ArrayList;

/**
  An enhanced list that can be used to retrieve groups of items based on a
  specified segment size.&nbsp; By default the segment size is equal to one.&nbsp;
  Methods are provided for retrieving and setting the segment size, and
  retrieving the current, next, previous, first and last segments.
  @author - Bryan Snipes
*/
public class SegmentedList<E> extends ArrayList<E> implements List<E> {

  /**
   * Serial version UID.
   */
  private static final long serialVersionUID = -3718019088607373221L;

  /**
    Current segment.
  */
  private int currentSegment = 0;

  /**
    Segment size.
  */
  private int segmentSize = 1;

  /**
    Class name attribute used in messaging.
  */
  protected String className = getClass().getName();

  /**
    Creates a new SegmentedList object using a segment size.
    @param segmentSize - Segment size.
  */
  public SegmentedList(int segmentSize) {
    this.segmentSize = segmentSize;
  }

  /**
    Creates a new SegmentedList object using a segment size and list of objects.
    @param segmentSize - Segment size.
    @param objects - List of objects
  */
  public SegmentedList(int segmentSize, List<E> objects) {
    this.segmentSize = segmentSize;

    if (objects != null) {
      addAll(objects);
    }
  }

  /**
    Sets the segment size.
    @param newSegmentSize - New segment size.
  */
  public void setSegmentSize(int newSegmentSize) {
    segmentSize = newSegmentSize;
  }

  /**
    Returns the segment size.
    @return int - Segment size.
  */
  public int getSegmentSize() {
    return segmentSize;
  }

  /**
    Returns the current segment number.
    @return int - Current segment number.
  */
  public int getCurrentSegment() {
    return currentSegment;
  }

  /**
    Returns the total number of segments.
    @return int - Total number of segments.
  */
  public int getTotalSegments() {

    // Initialize return value
    int totalSegments = 0;

    // Get list size
    int size = size();
    
    // Calculate segment size
    int mod = size % segmentSize;

    if (mod > 0) {
      totalSegments = size / segmentSize + 1;
    }
    else {
      totalSegments = size / segmentSize;
    }

    return totalSegments;
  }

  /**
    Returns a list of data for the first segment.
    @return List - First segment of data.
  */
  public List<E> getFirstSegment() {
    currentSegment = 1;
    return getSegment(currentSegment);
  }

  /**
    Returns a list of data for the last segment.
    @return List - Last segment of data.
  */
  public List<E> getLastSegment() {
    currentSegment = getTotalSegments();
    return getSegment(currentSegment);
  }

  /**
    Returns a list of data for the next segment based on the current segment.
    @return List - Next segment of data.
  */
  public List<E> getNextSegment() {

    // Increment current segment
    currentSegment++;

    // Get total segments as bound
    int totalSegments = getTotalSegments();
    
    // Check segment bounds
    if (currentSegment > totalSegments) {
      currentSegment = totalSegments;
    }

    return getSegment(currentSegment);
  }

  /**
    Returns a list of data for the previous segment based on the current segment.
    @return List - Previous segment of data.
  */
  public List<E> getPreviousSegment() {

    // Decrement current segment
    currentSegment--;

    // Check segment bounds
    if (currentSegment < 1) {
      currentSegment = 1;
    }

    return getSegment(currentSegment);
  }

  /**
    Returns a list of data for a specified segment.
    @param segment - Integer identifying the segment.
    @return List - Data for the specified segment.
  */
  public List<E> getSegment(int segment) {

    // Initialize return value
    List<E> segmentList = new ArrayList<E>();

    // Reset requested segment based on min segment number
    if (segment < 1) {
      segment = 1;
    }

    // Reset requested segment based on max segment number
    if (segment > getTotalSegments()) {
      segment = getTotalSegments();
    }

    if (segment > 0) {
      
      // Calculate start and end segment indicies
      int start = (segment - 1) * segmentSize;
      int end = start + segmentSize;

      // Ensure end is within bounds
      if (end > size()) {
        end = size();
      }
      // Add all items within range to return list
      segmentList = subList(start, end);
    }

    // Reset current segment
    currentSegment = segment;

    return segmentList;
  }

}
