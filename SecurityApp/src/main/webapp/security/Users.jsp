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
<%@ page import="com.bws.jdistil.security.app.configuration.FieldIds" %>
<%@ page import="com.bws.jdistil.security.app.configuration.PageIds" %>
<%@ page import="com.bws.jdistil.core.configuration.FieldValues" %>
<%@ page import="com.bws.jdistil.core.datasource.database.Operators" %>
<%@ page import="com.bws.jdistil.core.message.Messages" %>

<%@ taglib uri="com/bws/jdistil/core" prefix="core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
    <title><core:pageDescription pageId="<%= PageIds.USERS %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.USERS %>">

		<div class="page">			
	
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.USERS %>" /></span>
			</div>

		  <core:form id="Users" actionId="<%= ActionIds.VIEW_USERS %>" >
	
			<core:processMessages />

      <core:hidden fieldId="<%= FieldIds.USER_ID %>" fieldValue="<%= null %>" />
      <core:hidden fieldId="<%= FieldIds.USER_SORT_FIELD %>" defaultValue="<%= FieldIds.USER_LAST_NAME %>" />
      <core:hidden fieldId="<%= FieldIds.USER_SORT_DIRECTION %>" defaultValue="<%= FieldValues.SORT_ASCENDING %>" />
      <core:hidden fieldId="<%= FieldIds.USER_GROUP_STATE %>" defaultValue="<%= FieldValues.GROUP_HIDE %>" />

			<core:group id="<%= "filterGroup" %>" fieldId="<%= FieldIds.USER_GROUP_STATE %>" hideLabel="<%= "Hide Filter" %>" showLabel="<%= "Show Filter" %>" isHiddenByDefault="<%= Boolean.TRUE %>" >
				<table>
					<tr>
						<td><core:label fieldId="<%= FieldIds.USER_FIRST_NAME %>" /></td>
						<td>
						   <core:operatorList fieldId="<%= FieldIds.FIRST_NAME_FILTER_OPERATOR %>" isTextMode="<%= true %>" defaultValue="<%= Operators.BEGINS_WITH %>" />
						   <core:text fieldId="<%= FieldIds.FIRST_NAME_FILTER %>" maxlength="15" />
						</td>
					</tr>
					<tr>
						<td><core:label fieldId="<%= FieldIds.USER_LAST_NAME %>" /></td>
						<td>
	             <core:operatorList fieldId="<%= FieldIds.LAST_NAME_FILTER_OPERATOR %>" isTextMode="<%= true %>" defaultValue="<%= Operators.BEGINS_WITH %>" />
						   <core:text fieldId="<%= FieldIds.LAST_NAME_FILTER %>" maxlength="15" />
						</td>
					</tr>
	        <tr>
	         	<td colspan="2" />
	        </tr>
					<tr>
						<td>&nbsp;</td>
						<td align="right">
							<core:button actionId="<%= ActionIds.VIEW_USERS %>" />
							<input type="button" value="<core:description value="Clear" />" onClick="clearFilter('Users', 'filterGroup');return false;" />
						</td>
					</tr>
				</table>
			</core:group>
			<p>
				<label><core:description value="Page" />&nbsp;<core:currentPageNumber/>&nbsp;<core:description value="of" />&nbsp;<core:totalPages/></label>
				<core:pageNavigation currentPageNumberFieldId="<%= FieldIds.USER_CURRENT_PAGE_NUMBER %>" selectedPageNumberFieldId="<%= FieldIds.USER_SELECTED_PAGE_NUMBER %>" 
						previousPageActionId="<%= ActionIds.VIEW_USER_PREVIOUS_PAGE %>"	selectPageActionId="<%= ActionIds.VIEW_USER_SELECT_PAGE %>" nextPageActionId="<%= ActionIds.VIEW_USER_NEXT_PAGE %>" />
				<label><core:totalItems/>&nbsp;<core:description value="Items" /></label>
			</p>
      <core:table class="tableData" attributeName="<%= AttributeNames.USERS %>">
        <core:th>
          <core:td width="100">
            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_USERS %>" displayFieldId="<%= FieldIds.USER_LOGON_ID %>" 
            		sortFieldId="<%= FieldIds.USER_SORT_FIELD %>" sortDirectionId="<%= FieldIds.USER_SORT_DIRECTION %>" />
          </core:td>
          <core:td width="150">
            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_USERS %>" displayFieldId="<%= FieldIds.USER_FIRST_NAME %>" 
            		sortFieldId="<%= FieldIds.USER_SORT_FIELD %>" sortDirectionId="<%= FieldIds.USER_SORT_DIRECTION %>" />
          </core:td>
          <core:td width="150">
            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_USERS %>" displayFieldId="<%= FieldIds.USER_LAST_NAME %>" 
            		sortFieldId="<%= FieldIds.USER_SORT_FIELD %>" sortDirectionId="<%= FieldIds.USER_SORT_DIRECTION %>" />
          </core:td>
          <core:td width="50" align="center">
            <core:link actionId="<%= ActionIds.ADD_USER %>">
              <core:actionData fieldId="<%= FieldIds.USER_ID %>" fieldValue="<%= null %>" />
            </core:link>
          </core:td>
        </core:th>
        <core:tr>
          <core:td align="center">
            <core:link actionId="<%= ActionIds.EDIT_USER %>" >
              <core:display fieldId="<%= FieldIds.USER_LOGON_ID %>" />
              <core:actionData fieldId="<%= FieldIds.USER_ID %>" />
	          </core:link>
          </core:td>
          <core:td>
	          <core:display fieldId="<%= FieldIds.USER_FIRST_NAME %>" />
          </core:td>
          <core:td>
	          <core:display fieldId="<%= FieldIds.USER_LAST_NAME %>" />
          </core:td>
          <core:td align="center">
            <core:link actionId="<%= ActionIds.DELETE_USER %>" confirmationMessageKey="<%= Messages.DELETE_CONFIRMATION %>" >
              <core:actionData fieldId="<%= FieldIds.USER_ID %>" />
            </core:link>
          </core:td>
        </core:tr>
      </core:table>
	
	  </core:form>
	
		</div>
	
	</core:body>
	
</html>