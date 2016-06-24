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

<%@ page import="com.bws.jdistil.codes.app.configuration.ActionIds" %>
<%@ page import="com.bws.jdistil.codes.configuration.AttributeNames" %>
<%@ page import="com.bws.jdistil.codes.app.configuration.PageIds" %>
<%@ page import="com.bws.jdistil.codes.app.configuration.FieldIds" %>

<%@ taglib uri="com/bws/jdistil/core" prefix="core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
    <title><core:pageDescription pageId="<%= PageIds.CODE %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.CODE %>" >

		<div class="page">
			
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.CODE %>" /></span>
			</div>

		  <core:form id="Code" actionId="<%= ActionIds.SAVE_CODE %>" >
		
				<core:processMessages />
	
	      <core:hidden fieldId="<%= FieldIds.CODE_ID %>" attributeName="<%= AttributeNames.CODE %>" />
	      <core:hidden fieldId="<%= FieldIds.CODE_VERSION %>" attributeName="<%= AttributeNames.CODE %>" />
	
	      <core:hidden fieldId="<%= FieldIds.CATEGORY_ID_FILTER %>" />
				
	      <core:hidden fieldId="<%= FieldIds.CODE_CURRENT_PAGE_NUMBER %>" />
				<core:hidden fieldId="<%= FieldIds.CODE_SORT_FIELD %>" />
				<core:hidden fieldId="<%= FieldIds.CODE_SORT_DIRECTION %>" />
	      <core:hidden fieldId="<%= FieldIds.CODE_GROUP_STATE %>" />
	
		    <table>
		      <tr>
		        <td>
		          <core:label fieldId="<%= FieldIds.CATEGORY_ID %>" />
		        </td>
		        <td>
		        	<core:list fieldId="<%= FieldIds.CATEGORY_ID %>" attributeName="<%= AttributeNames.CODE %>" itemsAttributeName="<%= AttributeNames.CATEGORIES %>" />
		        </td>
		      </tr>
		      <tr>
		        <td>
		          <core:label fieldId="<%= FieldIds.CODE_NAME %>" />
		        </td>
		        <td>
		          <core:text fieldId="<%= FieldIds.CODE_NAME %>" attributeName="<%= AttributeNames.CODE %>" maxlength="50" />
		        </td>
		      </tr>
		      <tr>
		        <td>
		          <core:label fieldId="<%= FieldIds.IS_DEFAULT_CODE %>" />
		        </td>
		        <td>
		          <core:check fieldId="<%= FieldIds.IS_DEFAULT_CODE %>" attributeName="<%= AttributeNames.CODE %>" />
		        </td>
		      </tr>
	        <tr>
	          <td colspan="2">&nbsp;</td>
	        </tr>
		      <tr>
		        <td colspan="2" align="center">
		          <core:button actionId="<%= ActionIds.SAVE_CODE %>" />
		          <core:button actionId="<%= ActionIds.CANCEL_CODE %>" />
		        </td>
		      </tr>
		    </table>
			
		  </core:form>

		</div>
		
	</core:body>
	
</html>