<%@ include file="/WEB-INF/views/fragment/jspHeader.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!--
/**
 * Copyright 2013 Agustín Miura <"agustin.miura@gmail.com">
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
-->
<head>
<title>Imperium application</title>
<%@ include file="/WEB-INF/views/fragment/baseCss.jsp"%>
<%@ include file="/WEB-INF/views/fragment/baseJs.jsp"%>
<%@ include file="/WEB-INF/views/fragment/baseWidgets.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/view/createApplication.css" />" />
<script type="text/javascript"
	src="<c:url value="/resources/app/view/application/ListDef.js" />"></script>
</head>
<body>
	<!-- Handle bars templates -->
	<script id="handlebarTemplate_grid_actions"
		type="text/x-handlebars-template">
        <div id="{{actionHolderId}}" class="btn-group">
            {{#if magicAction}} 
                <a class="btn" href="#" data-toggle="tooltip" title="Click to edit">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="magicAction" class="icon-magic"></i>
                </a> 
            {{/if}}
            {{#if bookAction}}
                <a class="btn" href="#" data-toggle="tooltip" title="Edit subjects, roles and permissions">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="bookAction" class="icon-book"></i>
                </a> 
            {{/if}}
            {{#if pencilAction}}
                <a class="btn" href="#">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="pencilAction" class="icon-pencil"></i>
                </a>
            {{/if}}
            {{#if editAction}}
                <a class="btn" href="#">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="editAction" class="icon-edit"></i>
                </a>
            {{/if}}
            {{#if previewAction}}
                <a class="btn" href="#">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="previewAction" class="icon-search"></i>
                </a>
            {{/if}}
            {{#if exclamationAction}}
                <a class="btn" href="#" data-toggle="tooltip" title="See the api key">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="exclamationAction" class="icon-exclamation-sign"></i>
                </a>
            {{/if}}
            {{#if asteriskAction}}
                <a class="btn" href="#" data-toggle="tooltip" title="Reset the api key">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="asteriskAction" class="icon-asterisk"></i>
                </a>
            {{/if}}
            {{#if removeAction}}
                <a class="btn" href="#" data-toggle="tooltip" title="Remove the application">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="removeAction" class="icon-remove"></i>
                </a> 
            {{/if}}
        </div>
    </script>
	<!-- Template for the Alert div -->
	<script id="handlebarTemplate_alert_div"
		type="text/x-handlebars-template">
        <div id="{{id}}" class="alert alert-block alert-error">
            <button class="close" data-dismiss="alert" type="button">
                x
            </button>
            <h4 class="alert-heading">
                {{heading}}    
            </h4>
            <p>
                {{message}}
            </p>
        </div>        
    </script>
	<!-- Handle bars templates -->
	<!-- Html fragments for modal forms -->
	<%@ include file="/WEB-INF/views/fragment/application/modal.jsp"%>

	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<%@ include file="/WEB-INF/views/fragment/topMenu.jsp"%>
				<%@ include file="/WEB-INF/views/fragment/userMenu.jsp"%>
				<%@ include file="/WEB-INF/views/fragment/adminMenu.jsp"%>
			</div>
		</div>
	</div>

	<div id="main-content" class="container-fluid">

		<div class="row-fluid">
			<div class="span4" id="left-side"></div>
			<div class="span6" id="center-side">
				<h4>
					<p class="my-text-center">Application list</p>
				</h4>
				<div id="messageDiv">
				</div>
				<div class="tab-pane active" id="tabContentApplication">
					</br>
					<table cellpadding="0" cellspacing="0" border="0"
						class="table table-striped table-bordered" id="userTable">
						<thead>
							<tr>
								<th>Name</th>
								<th>Description</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
							<tr class="odd gradeX">
								<td>Name</td>
								<td>Description</td>
								<td>Actions</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
</html>
