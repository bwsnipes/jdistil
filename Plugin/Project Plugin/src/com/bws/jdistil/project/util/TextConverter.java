package com.bws.jdistil.project.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used to convert text to various formats.
 */
public class TextConverter {

	/**
	 * Camel back text pattern.
	 */
	private static final Pattern CAMEL_BACK_PATTERN = Pattern.compile("[A-Z][^A-Z]*");

	/**
	 * Creates a new text converter. Defined with private access to prevent instantiation.
	 */
	private TextConverter() {
		super();
	}
	
	/**
	 * Converts a string value in common format to a constant type string value.
	 * @param value Common string value.
	 * @return String Constant type string value.
	 */
	public static String convertCommonToConstant(String value) {
		
		// Initialize return value
		String convertedValue = null;
		
		if (value != null) {
		
			// Convert to uppercase
			convertedValue = value.toUpperCase();
			
			// Replace spaces with underscores
			convertedValue = convertedValue.replace(" ", "_");
		}
		
		return convertedValue;
	}
	
	/**
	 * Converts a string value in common format to a camel back type string value.
	 * @param value Common string value.
	 * @param lowercaseFirstCharacter Indicates whether or not the first character of the returned value should be lowercase.
	 * @return String Camel back type string value.
	 */
	public static String convertCommonToCamel(String value, boolean lowercaseFirstCharacter) {
		
		// Initialize return value
		String convertedValue = null;
		
		if (value != null) {
		
			// Create string buffer
			StringBuffer buffer = new StringBuffer();
			
			// Convert to lowercase
			value = value.toLowerCase();
			
			// Break value by whitespace into elements
			String[] elements = value.split("\\s+");
			
			for (String element : elements) {
				
				// Get first character of element
				String firstCharacter = element.substring(0, 1);
				
				// Convert first character to uppercase if specified and append to buffer
				if (buffer.length() == 0 && lowercaseFirstCharacter) {
					buffer.append(firstCharacter);
				}
				else {
					buffer.append(firstCharacter.toUpperCase());
				}
				
				// Append remaning elements to buffer
				if (element.length() > 1) {
					buffer.append(element.substring(1));
				}
			}
			
			// Set converted value
			convertedValue = buffer.toString();
		}
		
		return convertedValue;
	}
	
	/**
	 * Converts a string value as a sentence.
	 * @param value Value to convert.
	 * @param uppercaseFirstCharacter Indicates whether or not the first character of the returned value should be uppercase.
	 * @return Converted value.
	 */
	public static String convertAsSentence(String value, boolean uppercaseFirstCharacter) {
		
		// Initialize return value
		String formattedValue = null;
		
		if (value != null) {
		
			formattedValue = value.toLowerCase();
			
			if (uppercaseFirstCharacter) {
				
				String firstCharacter = formattedValue.substring(0, 1);
				String remainingCharacters = formattedValue.substring(1);
				
				formattedValue = firstCharacter.toUpperCase() + remainingCharacters;
			}
		}
		
		return formattedValue;
	}
	
	/**
	 * Converts a string value in constant type format to a common string value.
	 * @param value Constant type string value.
	 * @return String Common string value.
	 */
	public static String convertConstantToCommon(String value) {
		
		// Initialize return value
		String convertedValue = null;
		
		if (value != null) {
		
			// Create string buffer
			StringBuffer buffer = new StringBuffer();
			
			// Convert to lowercase
			value = value.toLowerCase();
			
			// Break value by underscore into elements
			String[] elements = value.split("_+");
			
			for (String element : elements) {
				
				// Get first character of element
				String firstCharacter = element.substring(0, 1);
				
				// Convert first character to uppercase and append to buffer
				buffer.append(firstCharacter.toUpperCase());
				
				// Append remaining elements to buffer
				if (element.length() > 1) {
					buffer.append(element.substring(1));
				}
				
				// Append space separator
				buffer.append(" ");
			}
			
			// Set converted value
			convertedValue = buffer.toString().trim();
		}
		
		return convertedValue;
	}
	
	/**
	 * Converts a string value in camel type format to a common string value.
	 * @param value Camel type string value.
	 * @return String Common string value.
	 */
	public static String convertCamelToCommon(String value) {
		
		// Initialize return value
		String convertedValue = null;
		
		if (value != null) {
		
			// Create string buffer
			StringBuffer buffer = new StringBuffer();
			
			// Get first character of value
			String firstCharacter = value.substring(0, 1);
			
			if (value.length() == 1) {
				
				// Only need the first character
				buffer.append(firstCharacter.toUpperCase());
			}
			else {

				// Ensure first character is uppercase
				value = firstCharacter.toUpperCase() + value.substring(1);
				
				// Create matcher using scenario file pattern
				Matcher matcher = CAMEL_BACK_PATTERN.matcher(value);

				while (matcher.find()) {

					// Get next matched element
					String element = matcher.group();
					
					// Append element
					buffer.append(element);
					
					// Append separator
					buffer.append(" ");
				}
			}
			
			// Set converted value
			convertedValue = buffer.toString().trim();
		}
		
		return convertedValue;
	}
	
  /**
   * Converts a string value to its pluralized equivalent.
   * @param value String value.
   * @return String Pluralized string value.
   */
  public static String convertToPlural(String value) {
    
    // Initialize return value
    String pluralValue = value;
    
    if (value != null && value.trim().length() > 0) {
    
      // Get length of value
      int length = value.length();
      
      // Convert value to lowercase
      String lowerCaseValue = value.toLowerCase();

      // Get last character
      char lastCharacter = lowerCaseValue.charAt(length - 1);
      
      if (lastCharacter == 'f') {
        pluralValue = value.substring(0, length - 1) + "ves";
      }
      else if (lastCharacter == 'h' || lastCharacter == 's' || lastCharacter == 'x') {
        pluralValue = value + "es";
      }
      else if (lastCharacter == 'y') {
        pluralValue = value.substring(0, length - 1) + "ies";
      }
      else {
        pluralValue = value + "s";
      }
    }
    
    return pluralValue;
  }
  
  /**
   * Returns the base class name from a fully qualified class name.
   * @param className Fully qualified class name.
   * @return String Base class name.
   */
  public static String getBaseClassName(String className) {
    
    // Initialize return value
    String baseClassName = className;
    
    if (className != null && className.trim().length() > 0) {
    
      // Get last package separator
      int index = className.lastIndexOf(".");
      
      if (index > 0) {
      	baseClassName = className.substring(index + 1);
      }
    }
    
    return baseClassName;
  }
  
  /**
   * Returns the package name from a fully qualified class name.
   * @param className Fully qualified class name.
   * @return String Package name.
   */
  public static String getPackageName(String className) {
    
    // Initialize return value
    String packageName = "";
    
    if (className != null && className.trim().length() > 0) {
    
      // Get last package separator
      int index = className.lastIndexOf(".");
      
      if (index > 0) {
      	packageName = className.substring(0, index);
      }
    }
    
    return packageName;
  }
  
}
