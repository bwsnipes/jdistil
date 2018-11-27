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

<%@ page import="com.bws.jdistil.core.security.IDomain" %>
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
    <title><core:pageDescription pageId="<%= PageIds.CHANGE_DOMAIN %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.CHANGE_DOMAIN %>">

		<div class="page">
			
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.CHANGE_DOMAIN %>" /></span>
			</div>
	
		  <core:form id="ChangeDomain" actionId="<%= ActionIds.CHANGE_DOMAIN %>" >
		
				<core:processMessages />
				
		    <table>
		      <tr>
		        <td><core:label fieldId="<%= FieldIds.DOMAIN_SELECTED_ID %>" /></td>
		        <td><core:list fieldId="<%= FieldIds.DOMAIN_SELECTED_ID %>" attributeName="<%= AttributeNames.DOMAIN %>" itemsAttributeName="<%= AttributeNames.DOMAINS %>" instruction="<%= IDomain.DEFAULT_NAME %>" /></td>
		      </tr>
	        <tr>
	          <td colspan="2">&nbsp;</td>
	        </tr>
	        <tr>
	          <td colspan="2" align="center">
		          <core:button actionId="<%= ActionIds.CHANGE_DOMAIN %>" />
		        </td>
		      </tr>
	      </table>
			
		  </core:form>

		</div>
		
	</core:body>
	
</html>