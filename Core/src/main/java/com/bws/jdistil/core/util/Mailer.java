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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.bws.jdistil.core.configuration.Constants;
import com.bws.jdistil.core.resource.ResourceUtil;

/*
  Utility class providing email services.
  @author - Bryan Snipes
*/
public class Mailer implements Runnable {

  // Create properties
  private String smtpHostName = null;
  private InternetAddress sender = null;
  private InternetAddress[] toRecipients = null;
  private InternetAddress[] ccRecipients = null;
  private InternetAddress[] bccRecipients = null;
  private String subject = null;
  private String text = null;
  private List<File> attachedFiles = null;

  /**
    Creates a new Mailer object.
  */
  protected Mailer() {
    super();
  }

 /*
    Retrieves the SMTP host name and creates a dispatch thread used to deliver
    an email created from a specified sender's email address, recipient addresses,
    carbon copy recipient addresses, blind carbon copy recipient addressess,
    subject header and message body.
    @param sender - Sender's email address.
    @param toRecipients - Array of recipient email addresses.
    @param ccRecipients - Array of carbon copy recipient email addresses.
    @param bccRecipients - Array of blind carbon copy recipient email addresses.
    @param subject - Subject header.
    @param text - Message text.
  */
  public static void send(InternetAddress sender, InternetAddress[] toRecipients,
      InternetAddress[] ccRecipients, InternetAddress[] bccRecipients, String subject,
      String text) throws UtilException {

    // Send message with no attachments
    send(sender, toRecipients, ccRecipients, bccRecipients, subject, text, null);
  }

 /*
    Retrieves the SMTP host name and creates a dispatch thread used to deliver
    an email created from a specified sender's email address, recipient addresses,
    carbon copy recipient addresses, blind carbon copy recipient addressess,
    subject header, message body, and a list of attached files.
    @param sender - Sender's email address.
    @param toRecipients - Array of recipient email addresses.
    @param ccRecipients - Array of carbon copy recipient email addresses.
    @param bccRecipients - Array of blind carbon copy recipient email addresses.
    @param subject - Subject header.
    @param text - Message text.
    @param attachedFiles - List of attached files.
  */
  public static void send(InternetAddress sender, InternetAddress[] toRecipients,
      InternetAddress[] ccRecipients, InternetAddress[] bccRecipients, String subject,
      String text, List<File> attachedFiles) throws UtilException {

    // Set method name
    String methodName = "Mailer.send: ";

    // Lookup host name
    String smtpHostName = ResourceUtil.getString(Constants.SMTP_HOST_NAME);

    if (StringUtil.isEmpty(smtpHostName)) {
      throw new UtilException(methodName + "Unable to find SMTP host name.");
    }

    // Create new mailer
    Mailer mailer = new Mailer();

    // Set properties
    mailer.smtpHostName = smtpHostName;
    mailer.sender = sender;
    mailer.toRecipients = toRecipients;
    mailer.ccRecipients = ccRecipients;
    mailer.bccRecipients = bccRecipients;
    mailer.subject = subject;
    mailer.text = text;
    mailer.attachedFiles = attachedFiles;

    // Create and start thread
    Thread dispatchThread = new Thread(mailer);
    dispatchThread.start();
  }

  /**
    Sends an email message using dispatcher properties.
  */
  public void run() {

    // Create properties file used to retrieve mail session
    Properties properties = new Properties();
    properties.put("mail.smtp.host", smtpHostName);

    // Create mail session
    Session session = Session.getDefaultInstance(properties, null);

    try {
      // Create MIME type message
      MimeMessage message = new MimeMessage(session);

      // Set sender address
      message.setFrom(sender);

      // Set recipient addresses
      if (toRecipients != null) {
        message.setRecipients(Message.RecipientType.TO, toRecipients);
      }
      if (ccRecipients != null) {
        message.addRecipients(Message.RecipientType.CC, ccRecipients);
      }
      if (bccRecipients != null) {
        message.addRecipients(Message.RecipientType.BCC, bccRecipients);
      }

      // Set subject and send date
      message.setSubject(subject);
      message.setSentDate(new Date());

      // Create mime multipart to handle text and any attachments
      MimeMultipart mimeMultipart = new MimeMultipart();

      // Create mime body part for message text
      MimeBodyPart textBodyPart = new MimeBodyPart();
      textBodyPart.setText(text);

      // Add text body part to mime multipart
      mimeMultipart.addBodyPart(textBodyPart);

      if (attachedFiles != null) {

        // Add all attachments to mime multipart
        for (File file : attachedFiles) {

          // Create mime body part for attachment
          MimeBodyPart fileBodyPart = new MimeBodyPart();

          // Create a file data source
          FileDataSource fileDataSource = new FileDataSource(file);

          // Set content and file name of body part
          fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
          fileBodyPart.setFileName(fileDataSource.getName());

          // Add file body part to mime multipart
          mimeMultipart.addBodyPart(fileBodyPart);
        }

      }

      // Set message content with mime multipart
      message.setContent(mimeMultipart);

      // Send message
      Transport.send(message);
    }
    catch (MessagingException messagingException) {
      System.out.println("Sending email: " + messagingException.getMessage());
    }
  }

}
