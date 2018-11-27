<!--
  Copyright (C) 2015 Bryan W. Snipes

  This file is part of the JDistil web application framework.

  JDistil is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  JDistil is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with JDistil.  If not, see <http://www.gnu.org/licenses/>.
-->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="com.bws.jdistil.security.app.configuration.ActionIds" %>
<%@ page import="com.bws.jdistil.security.configuration.AttributeNames" %>
<%@ page import="com.bws.jdistil.security.app.configuration.PageIds" %>
<%@ page import="com.bws.jdistil.security.app.configuration.FieldIds" %>

<%@ taglib uri="com/bws/jdistil/core" prefix="core" %>
<%@ taglib uri="com/bws/jdistil/security" prefix="security" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
    <title><core:pageDescription pageId="<%= PageIds.USER %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.USER %>">

		<div class="page">
			
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.USER %>" /></span>
			</div>

		  <core:form id="User" actionId="<%= ActionIds.SAVE_USER %>" >
		
				<core:processMessages />
				
	      <core:hidden fieldId="<%= FieldIds.USER_ID %>" attributeName="<%= AttributeNames.USER %>" />
	      <core:hidden fieldId="<%= FieldIds.USER_VERSION %>" attributeName="<%= AttributeNames.USER %>" />
	
	      <core:hidden fieldId="<%= FieldIds.FIRST_NAME_FILTER %>" />
	      <core:hidden fieldId="<%= FieldIds.FIRST_NAME_FILTER_OPERATOR %>" />
	      <core:hidden fieldId="<%= FieldIds.LAST_NAME_FILTER %>" />
	      <core:hidden fieldId="<%= FieldIds.LAST_NAME_FILTER_OPERATOR %>" />
	
				<core:hidden fieldId="<%= FieldIds.USER_CURRENT_PAGE_NUMBER %>" />
				<core:hidden fieldId="<%= FieldIds.USER_SORT_FIELD %>" />
				<core:hidden fieldId="<%= FieldIds.USER_SORT_DIRECTION %>" />
				<core:hidden fieldId="<%= FieldIds.USER_GROUP_STATE %>" />
				
		    <table>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_LOGON_ID %>" /></td>
		        <td><core:text fieldId="<%= FieldIds.USER_LOGON_ID %>" attributeName="<%= AttributeNames.USER %>" maxlength="10" /></td>
		      </tr>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_FIRST_NAME %>" /></td>
		        <td><core:text fieldId="<%= FieldIds.USER_FIRST_NAME %>" attributeName="<%= AttributeNames.USER %>" maxlength="15" /></td>
		      </tr>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_MIDDLE_INITIAL %>" /></td>
		        <td><core:text fieldId="<%= FieldIds.USER_MIDDLE_INITIAL %>" attributeName="<%= AttributeNames.USER %>" maxlength="1" /></td>
		      </tr>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_LAST_NAME%>" /></td>
		        <td><core:text fieldId="<%= FieldIds.USER_LAST_NAME %>" attributeName="<%= AttributeNames.USER %>" maxlength="15" /></td>
		      </tr>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_AUTHENTICATION_NEW_PASSWORD %>" /></td>
		        <td><core:password fieldId="<%= FieldIds.USER_AUTHENTICATION_NEW_PASSWORD %>" attributeName="<%= AttributeNames.USER %>" maxlength="10" /></td>
		      </tr>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD %>" /></td>
		        <td><core:password fieldId="<%= FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD %>" attributeName="<%= AttributeNames.USER %>" maxlength="10" /></td>
		      </tr>
		      <security:domainAdmin>
			      <tr>
			        <td><core:label fieldId="<%= FieldIds.USER_IS_DOMAIN_ADMIN %>" /></td>
			        <td><core:check fieldId="<%= FieldIds.USER_IS_DOMAIN_ADMIN %>" attributeName="<%= AttributeNames.USER %>" /></td>
			      </tr>
			    </security:domainAdmin>
				</table>
				<br/>
				<table>
		      <tr>
		        <td>
							<core:multipleList fieldId="<%= FieldIds.ROLE_ID %>" itemsAttributeName="<%= AttributeNames.ROLES %>">
								<core:multipleListGroup attributeName="<%= AttributeNames.USER %>" fieldId="<%= FieldIds.USER_ROLE_IDS %>" />
							</core:multipleList>
		        </td>
		      </tr>
	        <tr>
	          <td>&nbsp;</td>
	        </tr>
	        <tr>
	          <td align="center">
		          <core:button actionId="<%= ActionIds.SAVE_USER %>" />
		          <core:button actionId="<%= ActionIds.CANCEL_USER %>" />
		        </td>
		      </tr>
	      </table>
			
		  </core:form>

		</div>

	</core:body>
	
</html>