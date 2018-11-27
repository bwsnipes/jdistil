<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="com.bws.jdistil.security.app.configuration.ActionIds" %>
<%@ page import="com.bws.jdistil.security.app.configuration.FieldIds" %>
<%@ page import="com.bws.jdistil.security.app.configuration.PageIds" %>
<%@ page import="com.bws.jdistil.security.configuration.AttributeNames" %>
<%@ page import="com.bws.jdistil.core.configuration.FieldValues" %>
<%@ page import="com.bws.jdistil.core.datasource.database.Operators" %>
<%@ page import="com.bws.jdistil.core.message.Messages" %>

<%@ taglib uri="com/bws/jdistil/core" prefix="core" %>
<%@ taglib uri="com/bws/jdistil/codes" prefix="codes" %>

<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
		<title><core:pageDescription pageId="<%= PageIds.DOMAINS %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.DOMAINS %>">
	
      <div class="page">			
    
	    <core:breadcrumbTrail isStartOfTrail="<%= true %>" >
	      <core:pageDescription pageId="<%= PageIds.DOMAINS %>" />
	            <core:breadcrumbAction actionId="<%= ActionIds.VIEW_DOMAINS %>"/>
      <core:breadcrumbAction actionId="<%= ActionIds.VIEW_DOMAIN_PREVIOUS_PAGE %>"/>
      <core:breadcrumbAction actionId="<%= ActionIds.VIEW_DOMAIN_SELECT_PAGE %>"/>
      <core:breadcrumbAction actionId="<%= ActionIds.VIEW_DOMAIN_NEXT_PAGE %>"/>
	    </core:breadcrumbTrail>
		
		  <core:form id="Domains" actionId="<%= ActionIds.VIEW_DOMAINS %>" >
		
				<core:processMessages />
	
	      <core:hidden fieldId="<%= FieldIds.DOMAIN_ID %>" fieldValue="<%= null %>" />
	      <core:hidden fieldId="<%= FieldIds.DOMAIN_SORT_FIELD %>" defaultValue="<%= FieldIds.DOMAIN_NAME %>" />
	      <core:hidden fieldId="<%= FieldIds.DOMAIN_SORT_DIRECTION %>" defaultValue="<%= FieldValues.SORT_ASCENDING %>" />

		<core:hidden fieldId="<%= FieldIds.DOMAIN_GROUP_STATE %>" defaultValue="<%= FieldValues.GROUP_HIDE %>" />

		<core:group id="<%= "filterGroup" %>" fieldId="<%= FieldIds.DOMAIN_GROUP_STATE %>" hideLabel="<%= "Hide Filter" %>" showLabel="<%= "Show Filter" %>" isHiddenByDefault="<%= Boolean.TRUE %>" >
			<table>

				<tr>
					<td><core:label fieldId="<%= FieldIds.DOMAIN_NAME_FILTER %>" /></td>
					<td>
            <core:operatorList fieldId="<%= FieldIds.DOMAIN_NAME_FILTER_OPERATOR %>" isTextMode="<%= true %>" defaultValue="<%= Operators.CONTAINS %>" />
            <core:text fieldId="<%= FieldIds.DOMAIN_NAME_FILTER %>" maxlength="30" />
          </td>
				</tr>

        <tr>
         	<td colspan="2" />
        </tr>
				<tr>
					<td>&nbsp;</td>
					<td align="right">
						<core:button actionId="<%= ActionIds.VIEW_DOMAINS %>" />
						<input type="button" value="<core:description value="Clear" />" onClick="clearFilter('Domains', 'filterGroup');return false;" />
					</td>
				</tr>
			</table>
		</core:group>

			<p>
				<label><core:description value="Page" />&nbsp;<core:currentPageNumber/>&nbsp;<core:description value="of" />&nbsp;<core:totalPages/></label>
				<core:pageNavigation currentPageNumberFieldId="<%= FieldIds.DOMAIN_CURRENT_PAGE_NUMBER %>" selectedPageNumberFieldId="<%= FieldIds.DOMAIN_SELECTED_PAGE_NUMBER %>"
				    previousPageActionId="<%= ActionIds.VIEW_DOMAIN_PREVIOUS_PAGE %>" selectPageActionId="<%= ActionIds.VIEW_DOMAIN_SELECT_PAGE %>" nextPageActionId="<%= ActionIds.VIEW_DOMAIN_NEXT_PAGE %>" />
				<label><core:totalItems/>&nbsp;<core:description value="Items" /></label>
			</p>

	      <core:table class="tableData" attributeName="<%= AttributeNames.DOMAINS %>">
	        <core:th>
	          <core:td width="100">
	            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_DOMAINS %>" displayFieldId="<%= FieldIds.DOMAIN_NAME %>" 
	            		sortFieldId="<%= FieldIds.DOMAIN_SORT_FIELD %>" sortDirectionId="<%= FieldIds.DOMAIN_SORT_DIRECTION %>" />
	          </core:td>
	          <core:td width="100">
	            <core:sortableColumnHeader actionId="<%= ActionIds.VIEW_DOMAINS %>" displayFieldId="<%=FieldIds.DOMAIN_IS_DEFAULT_DATASOURCE%>" 
	            		sortFieldId="<%=FieldIds.DOMAIN_SORT_FIELD%>" sortDirectionId="<%=FieldIds.DOMAIN_SORT_DIRECTION%>" />
	          </core:td>
	          <core:td width="100">
	            <core:sortableColumnHeader actionId="<%=ActionIds.VIEW_DOMAINS%>" displayFieldId="<%=FieldIds.DOMAIN_DATASOURCE_NAME%>" 
	            		sortFieldId="<%=FieldIds.DOMAIN_SORT_FIELD%>" sortDirectionId="<%=FieldIds.DOMAIN_SORT_DIRECTION%>" />
	          </core:td>
	          <core:td width="50" align="center">
	            <core:link actionId="<%=ActionIds.ADD_DOMAIN%>">
	              <core:actionData fieldId="<%=FieldIds.DOMAIN_ID%>" fieldValue="<%=null%>" />
	            </core:link>
	          </core:td>
	        </core:th>
	        <core:tr>
	          <core:td align="center">
	            <core:link actionId="<%=ActionIds.EDIT_DOMAIN%>" >
	              <core:display fieldId="<%=FieldIds.DOMAIN_NAME%>" />
	              <core:actionData fieldId="<%=FieldIds.DOMAIN_ID%>" />
		          </core:link>
	          </core:td>
          <core:td>
          	<core:display fieldId="<%=FieldIds.DOMAIN_IS_DEFAULT_DATASOURCE%>" />
          </core:td>
          <core:td>
	          <core:display fieldId="<%=FieldIds.DOMAIN_DATASOURCE_NAME%>" />
          </core:td>

	          <core:td align="center">
	            <core:link actionId="<%= ActionIds.DELETE_DOMAIN %>" confirmationMessageKey="<%= Messages.DELETE_CONFIRMATION %>" >
	              <core:actionData fieldId="<%= FieldIds.DOMAIN_ID %>" />
	            </core:link>
	          </core:td>
	        </core:tr>
	      </core:table>
	
		  </core:form>
		  
		</div>
		
	</core:body>
	
</html>