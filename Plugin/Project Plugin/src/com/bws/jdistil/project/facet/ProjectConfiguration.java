package com.bws.jdistil.project.facet;

/**
 * Encapsulates all configuartion information needed by the project plugin.
 * @author Bryan Snipes
 */
public class ProjectConfiguration {

  /**
   * Base package name.
   */
  private String basePackageName = null;
  
  /**
   * Creates a new project configuration object.
   */
  public ProjectConfiguration() {
    super();
  }
  
  /**
   * Returns the base package name.
   * @return String Base package name.
   */
  public String getBasePackageName() {
    return basePackageName;
  }
  
  /**
   * Sets the base package name.
   * @param basePackageName Bases package name.
   */
  public void setBasePackageName(String basePackageName) {
    this.basePackageName = basePackageName;
  }
  
}
