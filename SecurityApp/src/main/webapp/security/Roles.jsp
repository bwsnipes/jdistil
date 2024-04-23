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
<%@ page import="com.bws.jdistil.core.configuration.FieldValues" %>
<%@ page import="com.bws.jdistil.core.message.Messages" %>

<%@ taglib uri="com/bws/jdistil/core" prefix="core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="core.css" />
		<script type="text/javascript" src="core.js"></script>
    <title><core:pageDescription pageId="<%= PageIds.ROLES %>" /></title>
	</head>
	
	<core:body pageId="<%= PageIds.ROLES %>">

		<div class="page">			
	
			<div class="breadcrumbTrail">
				<span class="breadcrumbSelected"><core:pageDescription pageId="<%= PageIds.ROLES %>" /></span>
			</div>

		  <core:form id="Roles" actionId="<%= ActionIds.VIEW_ROLES %>" >
	
			<core:processMessages />

      <core:hidden fieldId="<%= FieldIds.ROLE_ID %>" fieldValue="<%= null %>" />
      <core:hidden fieldId="<%= FieldIds.ROLE_SORT_FIELD %>" defaultValue="<%= FieldIds.ROLE_NAME %>" />
      <core:hidden fieldId="<%= FieldIds.ROLE_SORT_DIRECTION %>" defaultValue="<%= FieldValues.SORT_ASCENDING %>" />

      <core:table class="tableData" attributeName="<%= AttributeNames.ROLES %>">
        <core:th>
          <core:td width="150">
            <core:label fieldId="<%= FieldIds.ROLE_NAME %>" />
          </core:td>
          <core:td align="center">
            <core:link actionId="<%= ActionIds.ADD_ROLE %>">
              <core:actionData fieldId="<%= FieldIds.ROLE_ID %>" fieldValue="<%= null %>" />
            </core:link>
          </core:td>
        </core:th>
        <core:tr>
          <core:td>
            <core:link actionId="<%= ActionIds.EDIT_ROLE %>" >
              <core:display fieldId="<%= FieldIds.ROLE_NAME %>" />
              <core:actionData fieldId="<%= FieldIds.ROLE_ID %>" />
	          </core:link>
          </core:td>
          <core:td align="center">
            <core:link actionId="<%= ActionIds.DELETE_ROLE %>" confirmationMessageKey="<%= Messages.DELETE_CONFIRMATION %>" >
              <core:actionData fieldId="<%= FieldIds.ROLE_ID %>" />
            </core:link>
          </core:td>
        </core:tr>
      </core:table>
	
	  </core:form>
	
		</div>
	
	</core:body>
	
</html>