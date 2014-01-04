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
<script src="/imperium/resources/app/view/subject/EditRoles.js"></script>
</head>
<body>
    <div
        id="information"
        data-application-id="<c:out value="${application.id}"/>"
        data-application-name="<c:out value="${application.name}"/>"
        data-application-description="<c:out value="${application.description}"/>"
        data-subject-id="<c:out value="${subject.id}"/>"
        data-subject-name="<c:out value="${subject.name}"/>"
        class="notVisible"></div>
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <%@ include file="/WEB-INF/views/fragment/topMenu.jsp"%>
                <%@ include file="/WEB-INF/views/fragment/userMenu.jsp"%>
                <%@ include file="/WEB-INF/views/fragment/adminMenu.jsp"%>
            </div>
        </div>
    </div>
    <div
        id="mainContent"
        class="container-fluid">
        <div class="row-fluid">
            <div class="span10">
                <h4>
                    Assign roles to the subject
                    <c:out value="${subject.name}" />
                </h4>
            </div>
        </div>
        <div id="assignSubjectToRoleContainer">
            <div id="widgetContainer">
                <div class="row-fluid">
                    <div class="span2">
                        <div class="btn-group">
                            <button
                                id="addAllButton"
                                class="btn">Add all</button>
                            <button
                                id="addSelectedButton"
                                class="btn">Add selected</button>
                        </div>
                    </div>
                    <div class="span6 offset4">
                        <button
                            id="removeSelectedButton"
                            class="btn">Remove selected</button>
                    </div>
                </div>
                <div class="row-fluid show-grid">
                    <div
                        class="span6"
                        id="filterToSearchContainer">
                        <form
                            id="filterToSelectForm"
                            class="form-search">
                            <input
                                type="text"
                                class="search-query"
                                name="toSearchText">
                                <button
                                    type="submit"
                                    id="doSearchButton"
                                    class="btn">Search</button>
                        </form>
                    </div>
                    <div
                        class="span6"
                        id="filterSelectionContainer">
                        <form id="filterSelectionForm">
                            <input
                                type="text"
                                class="search-query"
                                name="query">
                                <button
                                    type="submit"
                                    id="doUserSelectionSearchButton"
                                    class="btn">Search</button>
                        </form>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span6">
                        <h4>Roles available</h4>
                    </div>
                    <div class="span6">
                        <h4>Selected</h4>
                    </div>
                </div>
                <div class="row-fluid">
                    <div
                        class="span6"
                        id="showAvailableContainer">
                        <table
                            class="table table-striped table-bordered"
                            id="showAvailableTable">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div
                        class="span6"
                        id="userSelectionContainer">
                        <table
                            id="userSelection"
                            class="table table-striped table-bordered">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Description</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
</body>
</html>
