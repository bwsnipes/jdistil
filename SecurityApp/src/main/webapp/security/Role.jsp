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
    <title><core:pageDescription pageId="<%= PageIds.ROLE %>" /></title>
		
		<script language="javascript">
		
			function selectGroup() {
				submitAction('Role', '<%= ActionIds.SELECT_GROUP %>', null);
			}
			
		</script>
	</head>
	
	<core:body pageId="<%= PageIds.ROLE %>">

		<div class="page">
			
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.ROLE %>" /></span>
			</div>

		  <core:form id="Role" actionId="<%= ActionIds.SAVE_ROLE %>" >
		
				<core:processMessages />
				
	      <core:hidden fieldId="<%= FieldIds.ROLE_ID %>" attributeName="<%= AttributeNames.ROLE %>" />
	      <core:hidden fieldId="<%= FieldIds.ROLE_VERSION %>" attributeName="<%= AttributeNames.ROLE %>" />
	
				<core:hidden fieldId="<%= FieldIds.ROLE_SORT_FIELD %>" />
				<core:hidden fieldId="<%= FieldIds.ROLE_SORT_DIRECTION %>" />
	
				<table>
					<tr>
						<td>
							<core:label fieldId="<%= FieldIds.ROLE_NAME %>" />
							<core:text fieldId="<%= FieldIds.ROLE_NAME %>" attributeName="<%= AttributeNames.ROLE %>" maxlength="50" />
						</td>
					</tr>
					<tr>
						<td>
							<core:multipleList fieldId="<%= FieldIds.TASK_ID %>" itemsAttributeName="<%= AttributeNames.TASKS %>">
								<core:multipleListGroup attributeName="<%= AttributeNames.ROLE %>" fieldId="<%= FieldIds.ROLE_RESTRICTED_TASK_IDS %>" />
							</core:multipleList>
						</td>
					</tr>
				</table>
				<br/>
		    <table>
		    	<tr>
		    		<td>
			       	<core:label fieldId="<%= FieldIds.GROUP_ID %>" />
							<core:list fieldId="<%= FieldIds.GROUP_ID %>" itemsAttributeName="<%= AttributeNames.GROUPS %>" onChange="javascript:selectGroup()"/>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td>
							<core:multipleList fieldId="<%= FieldIds.FIELD_ID %>" itemsAttributeName="<%= AttributeNames.FIELDS %>">
								<core:multipleListGroup attributeName="<%= AttributeNames.ROLE %>" fieldId="<%= FieldIds.ROLE_READ_ONLY_FIELD_IDS %>" />
								<core:multipleListGroup attributeName="<%= AttributeNames.ROLE %>" fieldId="<%= FieldIds.ROLE_RESTRICTED_FIELD_IDS %>" />
							</core:multipleList>
		    		</td>
		    	</tr>
	        <tr>
	          <td>&nbsp;</td>
	        </tr>
	        <tr>
	          <td align="center">
							<core:button actionId="<%= ActionIds.SAVE_ROLE %>" />
							<core:button actionId="<%= ActionIds.CANCEL_ROLE %>" />
						</td>
					</tr>
		    </table>
						
		  </core:form>

		</div>

	</core:body>
	
</html>