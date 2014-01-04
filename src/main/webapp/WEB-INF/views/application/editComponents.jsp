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
<title>Application edition for roles, subjects and permissions</title>
<%@ include file="/WEB-INF/views/fragment/baseCss.jsp"%>
<%@ include file="/WEB-INF/views/fragment/baseJs.jsp"%>
<%@ include file="/WEB-INF/views/fragment/baseWidgets.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/view/createApplication.css" />" />
<script type="text/javascript"
	src="<c:url value="/resources/app/view/application/EditComponents.js" />">
    
</script>
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
                <a class="btn" href="#" data-toggle="tooltip" title="Click to edit components">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="bookAction" class="icon-book"></i>
                </a> 
            {{/if}}
            {{#if pencilAction}}
                <a class="btn" href="#">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="pencilAction" class="icon-pencil"></i>
                </a>
            {{/if}}
            {{#if editAction}}
                <a class="btn" href="#" data-toggle="tooltip" title="Click to edit components">
                    <i data-id="{{id}}" data-type="{{type}}" data-action="editAction" class="icon-edit"></i>
                </a>
            {{/if}}
            {{#if removeAction}}
                <a class="btn" href="#" data-toggle="toggle" title="Click to remove">
                    <i data-id="{{id}}" edata-type="{{type}}" data-action="removeAction" class="icon-remove"></i>
                </a> 
            {{/if}}
        </div>
    </script>
	<!-- Template for the button -->
	<script id="handlebarTemplate_button" type="text/x-handlebars-template">
        <button id="{{id}}" class="btn btn-primary">
            {{text}}
        </button>
    </script>
	<!-- Handle bars templates -->
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<%@ include file="/WEB-INF/views/fragment/topMenu.jsp"%>
				<%@ include file="/WEB-INF/views/fragment/userMenu.jsp"%>
				<%@ include file="/WEB-INF/views/fragment/editApplicationMenu.jsp"%>
				<%@ include file="/WEB-INF/views/fragment/adminMenu.jsp"%>
			</div>
		</div>
	</div>

	<!-- Information about the application -->
	<div id="information" class="hidden"
		data-application-id="<c:out value="${id}"/>"
		data-name="<c:out value="${name}"/>"
		data-description="<c:out value="${description}"/>"
		data-from="<c:out value="${from}"/>"></div>
	<div id="main-content" class="container-fluid">
		<div class="row-fluid">
			<div class="span2" id="left-side"></div>
			<div class="row-fluid">
				<div class="span2"></div>
				<div class="span8">
					<h4>Edition of the application</h4>
				</div>
				<div class="span2"></div>
			</div>
			<div class="row-fluid">
				<div class="span2"></div>
				<div class="span10">
					<ul class="nav nav-tabs" id="tabPanelIndex">
						<li id="itemSubject"><a href="#tabContentSubject"
							data-toggle="tab">Subject</a></li>
						<li id="itemRole"><a href="#tabContentRole" data-toggle="tab">Role</a>
						</li>
						<li id="itemPermission"><a href="#tabContentPermission"
							data-toggle="tab">Permission</a></li>
					</ul>
					<div id="tabPanelContainer" class="tab-content">

						<!-- Hold the role table -->
						<div class="tab-pane" id="tabContentRole">

							<!-- Button to redirect to role creation -->
							<div class="row-fluid">
								<button id="createRole" class="btn">Add role</button>
							</div>

							<table cellpadding="0" cellspacing="0" border="0"
								class="table table-striped table-bordered" id="roleTable">
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
						<!-- Hold the permission table -->
						<div class="tab-pane" id="tabContentPermission">

							<!-- Button to redirect to permission creation -->
							<div class="row-fluid">
								<button id="createPermission" class="btn">Add
									permission</button>
							</div>

							<table cellpadding="0" cellspacing="0" border="0"
								class="table table-striped table-bordered" id="permissionTable">
								<thead>
									<tr>
										<th>Resource</th>
										<th>Action</th>
										<th>Description</th>
										<th>Actions</th>
									</tr>
								</thead>
								<tbody>
									<tr class="odd gradeX">
										<td>Sample resource</td>
										<td>Sample action</td>
										<td>Sample description</td>
										<td>Sample action</td>
									</tr>
								</tbody>
							</table>
						</div>
						<!-- Hold the subject table -->
						<div class="tab-pane" id="tabContentSubject">
							<table cellpadding="0" cellspacing="0" border="0"
								class="table table-striped table-bordered" id="subjectTable">
								<thead>
									<tr>
										<th>Name</th>
										<th>Actions</th>
									</tr>
								</thead>
								<tbody>
									<tr class="odd gradeX">
										<td>Sample name</td>
										<td>Sample actions</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
