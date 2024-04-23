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
package com.bws.jdistil.core.tag.data;

import com.bws.jdistil.core.process.ProcessContext;
import com.bws.jdistil.core.process.ProcessMessage;
import com.bws.jdistil.core.servlet.http.Controller;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  Writes all process messages contained in the current process context.
  @author Bryan Snipes
*/
public class ProcessMessages extends TagSupport {

  /**
    Serial version UID.
  */
  private static final long serialVersionUID = -8086892603546189841L;
  
  /**
    Creates a new ProcessMessages object.
  */
  public ProcessMessages() {
     super();
  }

  /**
    Writes an HTML table of process messages contained in the current process context.
    @see javax.servlet.jsp.tagext.TagSupport#doStartTag
  */
  public int doStartTag() throws JspException {

    // Set method name
    String methodName = "doStartTag";

    // Retrieve process context from request attributes
    ProcessContext processContext = (ProcessContext)pageContext.getRequest().getAttribute(Controller.PROCESS_CONTEXT);

    if (processContext != null) {

      try {
        // Get JSP writer
        JspWriter jspWriter = pageContext.getOut();

        // Write messages
        writeMessages(jspWriter, processContext.getInformationalMessages(), "info");
        writeMessages(jspWriter, processContext.getConfirmationMessages(), "confirm");
        writeMessages(jspWriter, processContext.getWarningMessages(), "warning");
        writeMessages(jspWriter, processContext.getErrorMessages(), "error");
      }
      catch (IOException ioException) {

        // Post error message
        Logger logger = Logger.getLogger("com.bws.jdistil.core.tag.data");
        logger.logp(Level.SEVERE, getClass().getName(), methodName, "Rendering Processing Errors", ioException);
      }
    }

    return SKIP_BODY;
  }

  /**
    Writes a list of process messages using the message type as the HTML class attribute.
    @param jspWriter - JSP writer.
    @param messages - List of process messages.
    @param messageClass - Message class.
  */
  private void writeMessages(JspWriter jspWriter, List<ProcessMessage> messages, String messageClass) throws IOException {
    
    if (messages != null && !messages.isEmpty()) {
    	
    	jspWriter.println("<div class=\"" +  messageClass + "\">");
      jspWriter.println("<ul>");
    	
      // Write all process messages
      for (ProcessMessage errorMessage : messages){
        jspWriter.print("<li>");
        jspWriter.print(errorMessage.getText());
        jspWriter.println("</li>");
      }

      jspWriter.println("</ul>");
      jspWriter.println("</div>");
    }
  }
  
  /**
    Returns value indicating to evaluate the rest of the page.
    @see javax.servlet.jsp.tagext.TagSupport#doEndTag
  */
  public int doEndTag() {
    return EVAL_PAGE;
  }

}
