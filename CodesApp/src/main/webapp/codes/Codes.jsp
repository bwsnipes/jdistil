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
<%@ page import="com.bws.jdistil.codes.app.configuration.PageIds" %>
<%@ page import="com.bws.jdistil.codes.app.configuration.FieldIds" %>
<%@ page import="com.bws.jdistil.codes.configuration.AttributeNames" %>
<%@ page import="com.bws.jdistil.codes.lookup.CategoryManager" %>
<%@ page import="com.bws.jdistil.core.configuration.FieldValues" %>
<%@ page import="com.bws.jdistil.core.message.Messages" %>

<%@ taglib uri="com/bws/jdistil/core" prefix="core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
		<title><core:pageDescription pageId="<%= PageIds.CODES %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.CODES %>">

		<div class="page">			
	
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.CODES %>" /></span>
			</div>

		  <core:form id="Codes" actionId="<%= ActionIds.VIEW_CODES %>" >
		
				<core:processMessages />
	
	      <core:hidden fieldId="<%= FieldIds.CODE_ID %>" fieldValue="<%= null %>" />
	      <core:hidden fieldId="<%= FieldIds.CODE_SORT_FIELD %>" defaultValue="<%= FieldIds.CODE_NAME %>" />
	      <core:hidden fieldId="<%= FieldIds.CODE_SORT_DIRECTION %>" defaultValue="<%= FieldValues.SORT_ASCENDING %>" />
	      <core:hidden fieldId="<%= FieldIds.CODE_GROUP_STATE %>" defaultValue="<%= FieldValues.GROUP_HIDE %>" />
	
		    <core:group id="<%= "filterGroup" %>" fieldId="<%= FieldIds.CODE_GROUP_STATE %>" hideLabel="<%= "Hide Filter" %>" showLabel="<%= "Show Filter" %>" isHiddenByDefault="<%= Boolean.TRUE %>" >
			    <table>
			      <tr>
			        <td>
			          <core:label fieldId="<%= FieldIds.CATEGORY_ID %>" />
			        </td>
			        <td>
			          <core:list fieldId="<%= FieldIds.CATEGORY_ID_FILTER %>" attributeName="<%= AttributeNames.CATEGORY %>" itemsAttributeName="<%= AttributeNames.CATEGORIES %>" />
			        </td>
		        <tr>
		         	<td colspan="2" />
		        </tr>
						<tr>
							<td>&nbsp;</td>
							<td align="right">
								<core:button actionId="<%= ActionIds.VIEW_CODES %>" />
								<input type="button" value="<core:description value="Clear" />" onClick="clearFilter('Codes', 'filterGroup');return false;" />
							</td>
						</tr>
					</table>
				</core:group>
				<p>
					<label><core:description value="Page" />&nbsp;<core:currentPageNumber/>&nbsp;<core:description value="of" />&nbsp;<core:totalPages/></label>
					<core:pageNavigation currentPageNumberFieldId="<%= FieldIds.CODE_CURRENT_PAGE_NUMBER %>" selectedPageNumberFieldId="<%= FieldIds.CODE_SELECTED_PAGE_NUMBER %>"
							previousPageActionId="<%= ActionIds.VIEW_CODES_PREVIOUS_PAGE %>" selectPageActionId="<%= ActionIds.VIEW_CODES_SELECT_PAGE %>" nextPageActionId="<%= ActionIds.VIEW_CODES_NEXT_PAGE %>" />
					<label><core:totalItems/>&nbsp;<core:description value="Items" /></label>
				</p>
	      <core:table class="tableData" attributeName="<%= AttributeNames.CODES %>">
	        <core:th>
	          <core:td width="150">
	            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_CODES %>" displayFieldId="<%= FieldIds.CODE_NAME %>" 
	            		sortFieldId="<%= FieldIds.CODE_SORT_FIELD %>" sortDirectionId="<%= FieldIds.CODE_SORT_DIRECTION %>" />
	          </core:td>
	          <core:td width="150">
	            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_CODES %>" displayFieldId="<%= FieldIds.CATEGORY_ID %>" 
	            		sortFieldId="<%= FieldIds.CODE_SORT_FIELD %>" sortDirectionId="<%= FieldIds.CODE_SORT_DIRECTION %>" />
	          </core:td>
	          <core:td>
	            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_CODES %>" displayFieldId="<%= FieldIds.IS_DEFAULT_CODE %>" 
	            		sortFieldId="<%= FieldIds.CODE_SORT_FIELD %>" sortDirectionId="<%= FieldIds.CODE_SORT_DIRECTION %>" />
	          </core:td>
	          <core:td width="50" align="center">
	            <core:link actionId="<%= ActionIds.ADD_CODE %>">
	              <core:actionData fieldId="<%= FieldIds.CODE_ID %>" fieldValue="<%= null %>" />
	            </core:link>
	          </core:td>
	        </core:th>
	        <core:tr>
	          <core:td>
	            <core:link actionId="<%= ActionIds.EDIT_CODE %>" >
	              <core:display fieldId="<%= FieldIds.CODE_NAME %>" />
	              <core:actionData fieldId="<%= FieldIds.CODE_ID %>" />
		          </core:link>
	          </core:td>
	          <core:td>
	            <core:displayAssociate fieldId="<%= FieldIds.CATEGORY_ID %>" attributeName="<%= AttributeNames.CODES %>"
	               associateDataManagerClass="<%= CategoryManager.class %>" associateDisplayFieldId="<%= FieldIds.CATEGORY_NAME %>" />
	          </core:td>
	          <core:td>
	            <core:display fieldId="<%= FieldIds.IS_DEFAULT_CODE %>" />
	          </core:td>
	          <core:td align="center">
	            <core:link actionId="<%= ActionIds.DELETE_CODE %>" confirmationMessageKey="<%= Messages.DELETE_CONFIRMATION %>" >
	              <core:actionData fieldId="<%= FieldIds.CODE_ID %>" />
	            </core:link>
	          </core:td>
	        </core:tr>
	      </core:table>
		
		  </core:form>
		  
		</div>
		
	</core:body>
	
</html>