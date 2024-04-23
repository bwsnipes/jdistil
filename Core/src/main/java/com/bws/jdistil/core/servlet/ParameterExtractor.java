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
package com.bws.jdistil.core.servlet;

import com.bws.jdistil.core.configuration.ConfigurationManager;
import com.bws.jdistil.core.configuration.Field;
import com.bws.jdistil.core.conversion.DateTimeConverter;
import com.bws.jdistil.core.conversion.IConverter;
import com.bws.jdistil.core.conversion.NumberConverter;
import com.bws.jdistil.core.util.StringUtil;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
  This class provides static methods used to retrieve data as different data
  types from a servlet request object using a given field ID. Instance values
  can also be retrieved by providing an instance number. Instance numbers are
  one based and the maximum instance count can be obtained for a given field ID.
  All numeric and date/time methods use core converters that use the default locale.
  Formatting outside the default locale should be handled outside of this class.
  @author - Bryan Snipes
*/
public class ParameterExtractor {

  /**
    Date time converter.
  */
  private static DateTimeConverter defaultDateTimeConverter = DateTimeConverter.getInstance();

  /**
    Number converter.
  */
  private static NumberConverter defaultNumberConverter = NumberConverter.getInstance();

  /**
    Creates a new instance of the ParameterExtractor class.&nbsp; Defined with
    private access to prohibit instance creation.
  */
  private ParameterExtractor() {
    super();
  }

  /**
    Returns a list of all instance numbers for a given field ID. 
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - List of instance numbers.
   */
  public static List<Integer> getInstances(ServletRequest request, String fieldId) {

    // Initialize return value
    List<Integer> instances = new ArrayList<Integer>();

    if (fieldId != null) {

      // Get parameter names
      Enumeration<?> parameterNames = request.getParameterNames();

      // Check all parameter names
      while (parameterNames.hasMoreElements()) {

        // Get next parameter name
        String parameterName = (String)parameterNames.nextElement();

        // Check for field ID prefix
        if (parameterName.startsWith(fieldId) && parameterName.length() > fieldId.length()) {

        	// Get index of instance separator
        	int index = parameterName.lastIndexOf("_");
        	
        	if (index > 0 && index < parameterName.length() - 1) {
        		
            // Get instance suffix
            String value = parameterName.substring(index + 1);

            // Convert to integer
            Integer instance = Integer.valueOf(value);

            // Add instance number to return value
            instances.add(instance);
        	}
        }
      }
    }

    return instances;
  }

  /**
    Returns an object using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Object - Field value.
  */
  public static Object getObject(ServletRequest request, String fieldId) {
    return getObject(request, fieldId, 0, null);
  }

  /**
    Returns an object using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Object - Field value.
  */
  public static Object getObject(ServletRequest request, String fieldId, int instance) {
    return getObject(request, fieldId, instance, null);
  }

  /**
    Returns an object using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Object - Field value.
  */
  public static Object getObject(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Object value = null;

    if (request != null && !StringUtil.isEmpty(fieldId)) {

      // Get field
      Field field = ConfigurationManager.getField(fieldId);

      if (field != null) {

        // Get field type
        Integer type = field.getType();

        // Set value based on type
        if (type.equals(Field.STRING)) {
          value = getString(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.NUMBER)) {
          value = getNumber(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.BYTE)) {
          value = getByte(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.SHORT)) {
          value = getShort(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.INTEGER)) {
          value = getInteger(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.LONG)) {
          value = getLong(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.FLOAT)) {
          value = getFloat(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.DOUBLE)) {
          value = getDouble(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.DATE)) {
          value = getDate(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.TIME)) {
          value = getTime(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.DATE_TIME)) {
          value = getDateTime(request, fieldId, instance, converter);
        }
        else if (type.equals(Field.BOOLEAN)) {
          value = getBoolean(request, fieldId, instance, converter);
        }
      }
    }

    return value;
  }

  /**
    Returns a list of objects using a given request, and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Object> getObjects(ServletRequest request, String fieldId) {
    return getObjects(request, fieldId, 0, null);
  }

  /**
    Returns a list of objects using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Object> getObjects(ServletRequest request, String fieldId, int instance) {
    return getObjects(request, fieldId, instance, null);
  }

  /**
    Returns a list of objects using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Object> getObjects(ServletRequest request, String fieldId, int instance, IConverter converter) {

    // Initialize return value
    List<Object> values = new ArrayList<Object>();

    if (request != null && !StringUtil.isEmpty(fieldId)) {

      // Get field
      Field field = ConfigurationManager.getField(fieldId);

      if (field != null) {

        // Get field type
        Integer type = field.getType();

        // Append instance if specified
        if (instance > 0) {
          fieldId = fieldId + "_" + String.valueOf(instance);
        }

        // Set value based on type
        if (type.equals(Field.STRING)) {

          // Get number values
          List<String> strings = getStrings(request, fieldId, instance, converter);

          // Add to return list
          if (strings != null) {
            values.addAll(strings);
          }
        }
        else if (type.equals(Field.NUMBER)) {

          // Get number values
          List<Number> numbers = getNumbers(request, fieldId, instance, converter);

          // Add to return list
          if (numbers != null) {
            values.addAll(numbers);
          }
        }
        else if (type.equals(Field.BYTE)) {

          // Get byte values
          List<Byte> bytes = getBytes(request, fieldId, instance, converter);

          // Add to return list
          if (bytes != null) {
            values.addAll(bytes);
          }
        }
        else if (type.equals(Field.SHORT)) {

          // Get short values
          List<Short> shorts = getShorts(request, fieldId, instance, converter);

          // Add to return list
          if (shorts != null) {
            values.addAll(shorts);
          }
        }
        else if (type.equals(Field.INTEGER)) {

          // Get integer values
          List<Integer> integers = getIntegers(request, fieldId, instance, converter);

          // Add to return list
          if (integers != null) {
            values.addAll(integers);
          }
        }
        else if (type.equals(Field.LONG)) {

          // Get long values
          List<Long> longs = getLongs(request, fieldId, instance, converter);

          // Add to return list
          if (longs != null) {
            values.addAll(longs);
          }
        }
        else if (type.equals(Field.FLOAT)) {

          // Get float values
          List<Float> floats = getFloats(request, fieldId, instance, converter);

          // Add to return list
          if (floats != null) {
            values.addAll(floats);
          }
        }
        else if (type.equals(Field.DOUBLE)) {

          // Get double values
          List<Double> doubles = getDoubles(request, fieldId, instance, converter);

          // Add to return list
          if (doubles != null) {
            values.addAll(doubles);
          }
        }
        else if (type.equals(Field.DATE)) {

          // Get date values
          List<Date> dates = getDates(request, fieldId, instance, converter);

          // Add to return list
          if (dates != null) {
            values.addAll(dates);
          }
        }
        else if (type.equals(Field.TIME)) {

          // Get time values
          List<Date> times = getTimes(request, fieldId, instance, converter);

          // Add to return list
          if (times != null) {
            values.addAll(times);
          }
        }
        else if (type.equals(Field.DATE_TIME)) {

          // Get date/time values
          List<Date> dateTimes = getDateTimes(request, fieldId, instance, converter);

          // Add to return list
          if (dateTimes != null) {
            values.addAll(dateTimes);
          }
        }
        else if (type.equals(Field.BOOLEAN)) {

          // Get boolean values
          List<Boolean> booleans = getBooleans(request, fieldId, instance, converter);

          // Add to return list
          if (booleans != null) {
            values.addAll(booleans);
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a string value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return String - Field value.
  */
  public static String getString(ServletRequest request, String fieldId) {
    return getString(request, fieldId, 0, null);
  }

  /**
    Returns a string value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return String - Field value.
  */
  public static String getString(ServletRequest request, String fieldId, int instance) {
    return getString(request, fieldId, instance, null);
  }


  /**
    Returns a string value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return String - Field value.
  */
  public static String getString(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    String value = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field value
      String fieldValue = request.getParameter(fieldId);

      // Assign value if valid
      if (!StringUtil.isEmpty(fieldValue)) {

        // Attempt to get field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use converter if one is available
        if (converter == null) {
          value = fieldValue;
        }
        else {
          value = (String)converter.parse(fieldValue, request.getLocale());
        }
      }
    }

    return value;
  }

  /**
    Returns a list of string values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<String> getStrings(ServletRequest request, String fieldId) {
    return getStrings(request, fieldId, 0, null);
  }

  /**
    Returns a list of string values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<String> getStrings(ServletRequest request, String fieldId, int instance) {
    return getStrings(request, fieldId, instance, null);
  }

  /**
    Returns a list of string values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<String> getStrings(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<String> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<String>();

        // Attempt to get field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Initialize value
          String value = fieldValue;

          // Use converter if one is available
          if (converter != null) {
            value = (String)converter.parse(fieldValue, request.getLocale());
          }

          // Add to values list
          if (!StringUtil.isEmpty(value)) {
            values.add(value);
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a date value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Date - Field value.
  */
  public static Date getDate(ServletRequest request, String fieldId) {
    return getDate(request, fieldId, 0, null);
  }

  /**
    Returns a date value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Date - Field value.
  */
  public static Date getDate(ServletRequest request, String fieldId, int instance) {
    return getDate(request, fieldId, instance, null);
  }

  /**
    Returns a date value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Date - Field value.
  */
  public static Date getDate(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Date value = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field value
      String fieldValue = request.getParameter(fieldId);

      // Assign value if valid
      if (!StringUtil.isEmpty(fieldValue)) {

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultDateTimeConverter;
        }

        // Parse to object
        value = (Date)converter.parse(fieldValue, request.getLocale());
      }
    }

    return value;
  }

  /**
    Returns a list of date values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Date> getDates(ServletRequest request, String fieldId) {
    return getDates(request, fieldId, 0, null);
  }

  /**
    Returns a list of date values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Date> getDates(ServletRequest request, String fieldId, int instance) {
    return getDates(request, fieldId, instance, null);
  }

  /**
    Returns a list of date values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Date> getDates(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Date> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Date>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultDateTimeConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to date
          Date value = (Date)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(value);
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a time value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Date - Field value.
  */
  public static Date getTime(ServletRequest request, String fieldId) {
    return getTime(request, fieldId, 0, null);
  }

  /**
    Returns a time value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Date - Field value.
  */
  public static Date getTime(ServletRequest request, String fieldId, int instance) {
    return getTime(request, fieldId, instance, null);
  }

  /**
    Returns a time value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Date - Field value.
  */
  public static Date getTime(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Date value = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field value
      String fieldValue = request.getParameter(fieldId);

      // Assign value if valid
      if (!StringUtil.isEmpty(fieldValue)) {

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultDateTimeConverter;
        }

        // Parse to object
        value = (Date)converter.parse(fieldValue, request.getLocale());
      }
    }

    return value;
  }

  /**
    Returns a list of time values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Date> getTimes(ServletRequest request, String fieldId) {
    return getTimes(request, fieldId, 0, null);
  }

  /**
    Returns a list of time values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Date> getTimes(ServletRequest request, String fieldId, int instance) {
    return getTimes(request, fieldId, instance, null);
  }

  /**
    Returns a list of time values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Date> getTimes(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Date> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Date>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultDateTimeConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to date
          Date value = (Date)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(value);
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a date/time value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Date - Field value.
  */
  public static Date getDateTime(ServletRequest request, String fieldId) {
    return getDateTime(request, fieldId, 0, null);
  }

  /**
    Returns a date/time value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Date - Field value.
  */
  public static Date getDateTime(ServletRequest request, String fieldId, int instance) {
    return getDateTime(request, fieldId, instance, null);
  }

  /**
    Returns a date/time value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Date - Field value.
  */
  public static Date getDateTime(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Date value = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field value
      String fieldValue = request.getParameter(fieldId);

      // Assign value if valid
      if (!StringUtil.isEmpty(fieldValue)) {

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultDateTimeConverter;
        }

        // Parse to object
        value = (Date)converter.parse(fieldValue, request.getLocale());
      }
    }

    return value;
  }

  /**
    Returns a list of date/time values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Date> getDateTimes(ServletRequest request, String fieldId) {
    return getDateTimes(request, fieldId, 0, null);
  }

  /**
    Returns a list of date/time values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Date> getDateTimes(ServletRequest request, String fieldId, int instance) {
    return getDateTimes(request, fieldId, instance, null);
  }

  /**
    Returns a list of date/time values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Date> getDateTimes(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Date> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Date>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultDateTimeConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to date
          Date value = (Date)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(value);
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a boolean value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Boolean - Field value.
  */
  public static Boolean getBoolean(ServletRequest request, String fieldId) {
    return getBoolean(request, fieldId, 0, null);
  }

  /**
    Returns a boolean value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Boolean - Field value.
  */
  public static Boolean getBoolean(ServletRequest request, String fieldId, int instance) {
    return getBoolean(request, fieldId, instance, null);
  }

  /**
    Returns a boolean value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Boolean - Field value.
  */
  public static Boolean getBoolean(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Boolean value = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field value
      String fieldValue = request.getParameter(fieldId);

      // Assign value if valid
      if (!StringUtil.isEmpty(fieldValue)) {

        // Attempt to get field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use converter if one is available
        if (converter == null) {
          value = Boolean.valueOf(fieldValue);
        }
        else {
          value = (Boolean)converter.parse(fieldValue, request.getLocale());
        }
      }
    }

    return value;
  }

  /**
    Returns a list of boolean values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Boolean> getBooleans(ServletRequest request, String fieldId) {
    return getBooleans(request, fieldId, 0, null);
  }

  /**
    Returns a list of boolean values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Boolean> getBooleans(ServletRequest request, String fieldId, int instance) {
    return getBooleans(request, fieldId, instance, null);
  }

  /**
    Returns a list of boolean values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Boolean> getBooleans(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Boolean> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Boolean>();

        // Attempt to get field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Initialize value
          Boolean value = null;

          // Use converter if one is available
          if (converter == null) {
            value = Boolean.valueOf(fieldValue);
          }
          else {
            value = (Boolean)converter.parse(fieldValue, request.getLocale());
          }

          // Add to values list
          if (value != null) {
            values.add(value);
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a number value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Number - Field value.
  */
  public static Number getNumber(ServletRequest request, String fieldId) {
    return getNumber(request, fieldId, 0, null);
  }

  /**
    Returns a number value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Number - Field value.
  */
  public static Number getNumber(ServletRequest request, String fieldId, int instance) {
    return getNumber(request, fieldId, instance, null);
  }

  /**
    Returns a number value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Number - Field value.
  */
  public static Number getNumber(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Number value = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field value
      String fieldValue = request.getParameter(fieldId);

      // Format value as number
      if (!StringUtil.isEmpty(fieldValue)) {

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Parse to object
        value = (Number)converter.parse(fieldValue, request.getLocale());
      }
    }

    return value;
  }

  /**
    Returns a list of number values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Number> getNumbers(ServletRequest request, String fieldId) {
    return getNumbers(request, fieldId, 0, null);
  }

  /**
    Returns a list of number values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Number> getNumbers(ServletRequest request, String fieldId, int instance) {
    return getNumbers(request, fieldId, instance, null);
  }

  /**
    Returns a list of number values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Number> getNumbers(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Number> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Number>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to object
          Number value = (Number)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(value);
          }
        }
      }
    }

    return values;
  }

  /**
    Returns an integer value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Integer - Field value.
  */
  public static Integer getInteger(ServletRequest request, String fieldId) {
    return getInteger(request, fieldId, 0, null);
  }

  /**
    Returns an integer value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Integer - Field value.
  */
  public static Integer getInteger(ServletRequest request, String fieldId, int instance) {
    return getInteger(request, fieldId, instance, null);
  }

  /**
    Returns an integer value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Integer - Field value.
  */
  public static Integer getInteger(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Integer value = null;

    // Get number value
    Number number = getNumber(request, fieldId, instance, converter);

    // Create integer value
    if (number != null) {
      value = Integer.valueOf(number.intValue());
    }

    return value;
  }

  /**
    Returns a list of integer values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Integer> getIntegers(ServletRequest request, String fieldId) {
    return getIntegers(request, fieldId, 0, null);
  }

  /**
    Returns a list of integer values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Integer> getIntegers(ServletRequest request, String fieldId, int instance) {
    return getIntegers(request, fieldId, instance, null);
  }

  /**
    Returns a list of integer values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Integer> getIntegers(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Integer> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Integer>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to object
          Number value = (Number)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(Integer.valueOf(value.intValue()));
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a double value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Double - Field value.
  */
  public static Double getDouble(ServletRequest request, String fieldId) {
    return getDouble(request, fieldId, 0, null);
  }

  /**
    Returns a double value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Double - Field value.
  */
  public static Double getDouble(ServletRequest request, String fieldId, int instance) {
    return getDouble(request, fieldId, instance, null);
  }

  /**
    Returns a double value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Double - Field value.
  */
  public static Double getDouble(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Double value = null;

    // Get number value
    Number number = getNumber(request, fieldId, instance, converter);

    // Create double value
    if (number != null) {
      value = Double.valueOf(number.doubleValue());
    }

    return value;
  }

  /**
    Returns a list of double values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Double> getDoubles(ServletRequest request, String fieldId) {
    return getDoubles(request, fieldId, 0, null);
  }

  /**
    Returns a list of double values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Double> getDoubles(ServletRequest request, String fieldId, int instance) {
    return getDoubles(request, fieldId, instance, null);
  }

  /**
    Returns a list of double values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Double> getDoubles(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Double> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Double>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to object
          Number value = (Number)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(Double.valueOf(value.doubleValue()));
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a long value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Long - Field value.
  */
  public static Long getLong(ServletRequest request, String fieldId) {
    return getLong(request, fieldId, 0, null);
  }

  /**
    Returns a long value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Long - Field value.
  */
  public static Long getLong(ServletRequest request, String fieldId, int instance) {
    return getLong(request, fieldId, instance, null);
  }

  /**
    Returns a long value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Long - Field value.
  */
  public static Long getLong(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Long value = null;

    // Get number value
    Number number = getNumber(request, fieldId, instance, converter);

    // Create long value
    if (number != null) {
      value = Long.valueOf(number.longValue());
    }

    return value;
  }

  /**
    Returns a list of long values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Long> getLongs(ServletRequest request, String fieldId) {
    return getLongs(request, fieldId, 0, null);
  }

  /**
    Returns a list of long values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Long> getLongs(ServletRequest request, String fieldId, int instance) {
    return getLongs(request, fieldId, instance, null);
  }

  /**
    Returns a list of long values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Long> getLongs(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Long> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Long>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to object
          Number value = (Number)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(Long.valueOf(value.longValue()));
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a short value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Short - Field value.
  */
  public static Short getShort(ServletRequest request, String fieldId) {
    return getShort(request, fieldId, 0, null);
  }

  /**
    Returns a short value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Short - Field value.
  */
  public static Short getShort(ServletRequest request, String fieldId, int instance) {
    return getShort(request, fieldId, instance, null);
  }

  /**
    Returns a short value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Short - Field value.
  */
  public static Short getShort(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Short value = null;

    // Get number value
    Number number = getNumber(request, fieldId, instance, converter);

    // Create short value
    if (number != null) {
      value = Short.valueOf(number.shortValue());
    }

    return value;
  }

  /**
    Returns a list of sort values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Short> getShorts(ServletRequest request, String fieldId) {
    return getShorts(request, fieldId, 0, null);
  }

  /**
    Returns a list of sort values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Short> getShorts(ServletRequest request, String fieldId, int instance) {
    return getShorts(request, fieldId, instance, null);
  }

  /**
    Returns a list of sort values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Short> getShorts(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Short> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Short>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to object
          Number value = (Number)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(Short.valueOf(value.shortValue()));
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a float value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Float - Field value.
  */
  public static Float getFloat(ServletRequest request, String fieldId) {
    return getFloat(request, fieldId, 0, null);
  }

  /**
    Returns a float value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Float - Field value.
  */
  public static Float getFloat(ServletRequest request, String fieldId, int instance) {
    return getFloat(request, fieldId, instance, null);
  }

  /**
    Returns a float value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Float - Field value.
  */
  public static Float getFloat(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Float value = null;

    // Get number value
    Number number = getNumber(request, fieldId, instance, converter);

    // Create float value
    if (number != null) {
      value = Float.valueOf(number.floatValue());
    }

    return value;
  }

  /**
    Returns a list of flost values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Float> getFloats(ServletRequest request, String fieldId) {
    return getFloats(request, fieldId, 0, null);
  }

  /**
    Returns a list of flost values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Float> getFloats(ServletRequest request, String fieldId, int instance) {
    return getFloats(request, fieldId, instance, null);
  }

  /**
    Returns a list of flost values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Float> getFloats(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Float> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Float>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to object
          Number value = (Number)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(Float.valueOf(value.floatValue()));
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a byte value using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return Byte - Field value.
  */
  public static Byte getByte(ServletRequest request, String fieldId) {
    return getByte(request, fieldId, 0, null);
  }

  /**
    Returns a byte value using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return Byte - Field value.
  */
  public static Byte getByte(ServletRequest request, String fieldId, int instance) {
    return getByte(request, fieldId, instance, null);
  }

  /**
    Returns a byte value using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return Byte - Field value.
  */
  public static Byte getByte(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    Byte value = null;

    // Get number value
    Number number = getNumber(request, fieldId, instance, converter);

    // Create byte value
    if (number != null) {
      value = Byte.valueOf(number.byteValue());
    }

    return value;
  }

  /**
    Returns a list of byte values using a given request and field ID.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @return List - Field values.
  */
  public static List<Byte> getBytes(ServletRequest request, String fieldId) {
    return getBytes(request, fieldId, 0, null);
  }

  /**
    Returns a list of byte values using a given request, field ID, and instance.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @return List - Field values.
  */
  public static List<Byte> getBytes(ServletRequest request, String fieldId, int instance) {
    return getBytes(request, fieldId, instance, null);
  }

  /**
    Returns a list of byte values using a given request, field ID, instance, and converter.
    @param request - Servlet request object.
    @param fieldId - Field ID.
    @param instance - Instance number.
    @param converter - Value converter.
    @return List - Field values.
  */
  public static List<Byte> getBytes(ServletRequest request, String fieldId, int instance,
      IConverter converter) {

    // Initialize return value
    List<Byte> values = null;

    if (fieldId != null) {

      // Append instance if specified
      if (instance > 0) {
        fieldId = fieldId + "_" + String.valueOf(instance);
      }

      // Retrieve field values
      String[] fieldValues = request.getParameterValues(fieldId);

      if (fieldValues != null) {

        // Create value list
        values = new ArrayList<Byte>();

        // Use field converter if one is not specified
        if (converter == null) {
          converter = getFieldConverter(fieldId);
        }

        // Use default converter if one is not specified
        if (converter == null) {
          converter = defaultNumberConverter;
        }

        // Process all field values
        for (String fieldValue : fieldValues) {

          // Parse to object
          Number value = (Number)converter.parse(fieldValue, request.getLocale());

          // Add to values list
          if (value != null) {
            values.add(Byte.valueOf(value.byteValue()));
          }
        }
      }
    }

    return values;
  }

  /**
    Returns a converter for a specified field ID and locale.
    @param fieldId - Field ID.
    @return IConverter - Field specific converter.
  */
  private static IConverter getFieldConverter(String fieldId) {

    // Initialize converter
    IConverter converter = null;
    
    if (!StringUtil.isEmpty(fieldId)) {
    
      // Retrieve field
      Field field = ConfigurationManager.getField(fieldId);
      
      // Get converter
      if (field != null) {
        converter = field.getConverter();
      }
    }
    
    return converter;
  }

}
