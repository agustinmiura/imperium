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
<link
    rel="stylesheet"
    type="text/css"
    href="<c:url value="/resources/css/view/createApplication.css" />" />
<script
    type="text/javascript"
    src="<c:url value="/resources/app/view/role/List.js" />"></script>
<script
    type="text/javascript"
    charset="utf-8">
	jQuery(function($) {
		"use strict";
		var module = imperium_ui_view_role_List.module;
		module.init();
		module.render();
	});
</script>
</head>
<body>
    <!-- Confirmation dialogs goes here -->
    <%@ include file="/WEB-INF/views/fragment/confirmationDialogFragment.jsp"%>

    <!-- Modal forms goes here -->
    <div
        id="myModal"
        class="modal hide fade"
        tabindex="-1"
        role="dialog"
        aria-labelledby="myModalLabel"
        aria-hidden="true">
        <div class="modal-header">
            <h3>Edit role</h3>
        </div>
        <div class="modal-body">
            <form class="form-horizontal">
                <div class="control-group">
                    <label
                        class="control-label"
                        for="name">Name</label>
                    <div class="controls">
                        <input
                            type="text"
                            id="name"
                            name="name"
                            placeholder="Enter role name here"> </input>
                    </div>
                </div>
                <div class="control-group">
                    <label
                        class="control-label"
                        for="description">Description</label>
                    <div class="controls">
                        <input
                            type="text"
                            id="description"
                            name="description"
                            placeholder="Enter description here"> </input>
                    </div>
                </div>
            </form>
        </div>
        <div class="modal-footer">
            <button
                id="modalSubmit"
                class="btn btn-primary">Save changes</button>
            <button
                class="btn"
                data-dismiss="modal">Close</button>
        </div>
    </div>

    <!-- End of modal forms -->
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <a
                    class="brand"
                    href="#">Role list</a>
                <%@ include file="/WEB-INF/views/fragment/topMenu.jsp"%>
                <ul class="nav pull-right">
                    <li class="dropbown">
                        <a href="#">Logout</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div
        id="main-content"
        class="container-fluid">
        <div class="row-fluid">
            <div
                class="span2"
                id="left-side">
                <%@ include file="/WEB-INF/views/fragment/leftMenu.jsp"%>
            </div>
            <div
                class="span8"
                id="center-side">
                <div class="hero-unit">
                    <h1>Role list</h1>
                    <br />
                    <p>For application : applicationName here</p>
                    <div
                        id="roleGridContainer"
                        data-application="<c:out value ="${applicationId}"/>">
                        <!-- Here goes the grid -->
                        <div id="searchFormContainer">
                            <form class="form-inline">
                                <input
                                    id="search"
                                    type="text"
                                    placeholder="Type your search"></input>
                                <button
                                    id="searchButton"
                                    type="submit"
                                    class="btn">Search</button>
                            </form>
                        </div>
                        <div id="tableContainer">
                            <table
                                class="table table-striped table-bordered table-hover table-condensed">
                                <caption></caption>
                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                        <div id="paginationContainer">
                            <ul class="pager">
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div
                class="span2"
                id="right-side">
                <div class="well">Banner content</div>
            </div>
        </div>
    </div>
    <div class="footer">
        <div class="container">
            <p class="muted credit"></p>
        </div>
    </div>
</body>
</html>
