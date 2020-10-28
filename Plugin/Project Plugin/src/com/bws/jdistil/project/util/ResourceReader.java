package com.bws.jdistil.project.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceReader {

  /**
   * Creates a new resource reader.
   */
  public ResourceReader() {
    super();
  }
  
  /**
   * Reads and returns the contents of a specified resource.
   * @param resourceName Resource name.
   * @return String Resource content.
   * @throws IOException
   */
  public String readResource(String fileName) throws IOException {
    
    // Initialize return value
    StringBuffer content = new StringBuffer();

    // Get resource input stream
    InputStream inputStream = getClass().getResourceAsStream(fileName);
    
    // Get buffered reader
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

    // Initialize read variables
    int length = 300;
    char[] buffer = new char[length];
    
    // Read first line
    int totalChars = bufferedReader.read(buffer, 0, length);
    
    while (totalChars != -1) {
      
      // Append characters to content
      content.append(buffer, 0, totalChars);
      
      // Read next line
      totalChars = bufferedReader.read(buffer, 0, length);
    }

    return content.toString();
  }
  
}
