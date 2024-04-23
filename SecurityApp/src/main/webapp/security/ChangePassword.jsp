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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
    <title><core:pageDescription pageId="<%= PageIds.CHANGE_PASSWORD %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.CHANGE_PASSWORD %>">

		<div class="page">
			
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.CHANGE_PASSWORD %>" /></span>
			</div>
	
		  <core:form id="ChangePassword" actionId="<%= ActionIds.CHANGE_PASSWORD %>" >
		
				<core:processMessages />
				
		    <table>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_AUTHENTICATION_PASSWORD %>" /></td>
		        <td><core:password fieldId="<%= FieldIds.USER_AUTHENTICATION_PASSWORD %>" maxlength="10" /></td>
		      </tr>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.USER_AUTHENTICATION_NEW_PASSWORD %>" /></td>
		        <td><core:password fieldId="<%= FieldIds.USER_AUTHENTICATION_NEW_PASSWORD %>" maxlength="10" /></td>
		      </tr>
	        <tr>
	          <td><core:label fieldId="<%= FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD %>" /></td>
	          <td><core:password fieldId="<%= FieldIds.USER_AUTHENTICATION_CONFIRM_PASSWORD %>" maxlength="10" /></td>
	        </tr>
	        <tr>
	          <td colspan="2">&nbsp;</td>
	        </tr>
	        <tr>
	          <td colspan="2" align="center">
		          <core:button actionId="<%= ActionIds.CHANGE_PASSWORD %>" />
		        </td>
		      </tr>
	      </table>
			
		  </core:form>

		</div>
		
	</core:body>
	
</html>