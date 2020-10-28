package com.bws.jdistil.project.fragment.process;

/**
  Package level exception defined as a base exception for the creator package.
  @author - Bryan Snipes
*/
public class ProcessException extends Exception {

  /**
  Serial version UID.
*/
  private static final long serialVersionUID = 4156469643897724238L;

  /**
    Creates a new CreatorException object.
  */
  public ProcessException() {
    super();
  }
  
  /**
    Creates a CreatorException intialize with a given message.
    @param message - Exception message.
  */
  public ProcessException(String message) {
    super(message);
  }
  
}
