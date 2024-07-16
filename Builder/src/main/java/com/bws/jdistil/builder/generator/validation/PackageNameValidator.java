package com.bws.jdistil.builder.generator.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Verifies package names.
 */
public class PackageNameValidator {

  /**
   * Java identifier name pattern used for package validation.
   */
  private static final Pattern javaIdentifierNamePattern = Pattern.compile("[a-zA-Z]\\w*");

  /**
   * Error message.
   */
  private String errorMessage = null;
  
  /**
   * Creates a new package name validator.
   */
  public PackageNameValidator() {
    super();
  }
  
  /**
   * Returns a value indicating whether or not a specified package name is valid.
   * @param fieldName Field name.
   * @param packageName Package name.
   */
  public boolean isValid(String fieldName, String packageName) {

    // Initialize return value
    boolean isValid = true;

    // Clear error message
    errorMessage = null;
    
    if (packageName == null || packageName.trim().length() == 0) {

      // Set valid indicator and error message
      isValid = false;
      errorMessage = fieldName + " is required.";
    }
    else if (packageName.startsWith(".") || packageName.endsWith(".")) {

      // Set valid indicator and error message
      isValid = false;
      errorMessage = "Invalid package name. A package name cannot start or end with a dot.";
    }
    else {
      
      // Get package name components
      String[] packageNameComponents = packageName.split("\\.");
      
      for (String packageNameComponent : packageNameComponents) {
        
        // Create java identifier matcher
        Matcher javaIdentifierMatcher = javaIdentifierNamePattern.matcher(packageNameComponent);
        
        // Validate each package name component
        if (!javaIdentifierMatcher.matches()) {
          
          // Set valid indicator and error message
          isValid = false;
          errorMessage = "Invalid package name. '" + packageNameComponent + "' is not a valid Java identifier.";

          break;
        }
      }
    }
    
    return isValid;
  }

  /**
   * Returns the error message associated with the last validation. 
   * @return String Error message.
   */
  public String getErrorMessage() {
    return errorMessage;
  }
  
}
