<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="com.bws.jdistil.security.app.configuration.ActionIds" %>
<%@ page import="com.bws.jdistil.security.app.configuration.PageIds" %>
<%@ page import="com.bws.jdistil.security.app.configuration.FieldIds" %>
<%@ page import="com.bws.jdistil.security.configuration.AttributeNames" %>

<%@ taglib uri="com/bws/jdistil/core" prefix="core" %>
<%@ taglib uri="com/bws/jdistil/codes" prefix="codes" %>

<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
		<title><core:pageDescription pageId="<%= PageIds.DOMAIN %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.DOMAIN %>">
	
		<div class="page">
			
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.DOMAIN %>" /></span>
			</div>

		  <core:form id="Domain" actionId="<%= ActionIds.SAVE_DOMAIN %>" >
		
				<core:processMessages />
				
	      <core:hidden fieldId="<%= FieldIds.PARENT_FIELD_ID %>" fieldValue="<%= null %>" />
	      <core:hidden fieldId="<%= FieldIds.PARENT_ACTION_ID %>" fieldValue="<%= ActionIds.VIEW_DOMAINS %>" />
	      <core:hidden fieldId="<%= FieldIds.PARENT_ACTION_ID %>" fieldValue="<%= ActionIds.SAVE_DOMAIN %>" />
	      <core:hidden fieldId="<%= FieldIds.PARENT_PAGE_ID %>" fieldValue="<%= PageIds.DOMAIN %>" />
	      
	      <core:hidden fieldId="<%= FieldIds.DOMAIN_ID %>" attributeName="<%= AttributeNames.DOMAIN %>" />
	      <core:hidden fieldId="<%= FieldIds.DOMAIN_VERSION %>" attributeName="<%= AttributeNames.DOMAIN %>" />

	      <core:fields>
	      	<core:includeActionFields actionId="<%= ActionIds.VIEW_DOMAINS %>" />
	      </core:fields>
			
		    <table>
				<tr>
					<td><core:label fieldId="<%= FieldIds.DOMAIN_NAME %>" /></td>
					<td><core:text fieldId="<%= FieldIds.DOMAIN_NAME %>" attributeName="<%= AttributeNames.DOMAIN %>" maxlength="30" /></td>
				</tr>
				<tr>
					<td><core:label fieldId="<%=FieldIds.DOMAIN_IS_DEFAULT_DATASOURCE%>" /></td>
	        <td><core:check fieldId="<%= FieldIds.DOMAIN_IS_DEFAULT_DATASOURCE %>" attributeName="<%= AttributeNames.DOMAIN %>" /></td>
				</tr>
				<tr>
					<td><core:label fieldId="<%=FieldIds.DOMAIN_DATASOURCE_NAME%>" /></td>
					<td><core:text fieldId="<%=FieldIds.DOMAIN_DATASOURCE_NAME%>" attributeName="<%= AttributeNames.DOMAIN %>" maxlength="30" /></td>
				</tr>

	        <tr>
	          <td colspan="2">&nbsp;</td>
	        </tr>
		      <tr>
		        <td colspan="2" align="center">
		          <core:button actionId="<%= ActionIds.SAVE_DOMAIN %>" />
		          <core:button actionId="<%= ActionIds.CANCEL_DOMAIN %>" />
		        </td>
		      </tr>
	      </table>
			
		  </core:form>

		</div>
		
	</core:body>
	
</html>