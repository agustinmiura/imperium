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
<script src="/imperium/resources/app/view/subject/Edit-Roles.js"></script>
</head>
<body>
    <div
        id="information"
        data-application-id="<c:out value="${application.id}"/>"
        data-application-name="<c:out value="${application.name}"/>"
        data-application-description="<c:out value="${application.description}"/>"
        data-subject-id="<c:out value="${subject.id}"/>"
        data-subject-name="<c:out value="${subject.name}"/>"
        class="notVisible"
    ></div>

    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <%@ include file="/WEB-INF/views/fragment/topMenu.jsp"%>
                <%@ include file="/WEB-INF/views/fragment/userMenu.jsp"%>
                <%@ include
                    file="/WEB-INF/views/fragment/editApplicationMenu.jsp"
                %>
                <%@ include file="/WEB-INF/views/fragment/adminMenu.jsp"%>
            </div>
        </div>
    </div>
    <div
        id="main-content"
        class="container"
    >
        <div
            class="span12"
            id="center-side"
        >
            <button
                type="submit"
                name="goBack"
                class="btn pull-left"
            >
                <i class="icon-arrow-left"></i> Go back
            </button>

            <h4 class="my-text-center">
                Assign roles to "
                <c:out value="${subject.name}" />
                "
            </h4>
            <div id="widgetContainer">
                <div
                    id="titleContainer"
                    class="row"
                >
                    <div class="span6">
                        <h4 class="my-text-center">
                            Roles available
                            <h4 />
                    </div>
                    <div class="span6">
                        <h4 class="my-text-center">
                            Roles selected
                            <h4 />
                    </div>
                </div>
                <div
                    id="buttonContainer"
                    class="row"
                >
                    <div class="span6 special-div">
                        <button
                            id="addAllButton"
                            class="btn"
                        >Add all</button>
                        <button
                            id="addSelectedButton"
                            class="btn "
                        >Add selected</button>
                        <form class="form-search pull-right">
                            <div class="input-append">
                                <input
                                    type="text"
                                    class="search-query"
                                >
                                    <button
                                        id="searchAvailableButton"
                                        type="submit"
                                        class="btn"
                                    >Search</button>
                            </div>
                        </form>
                    </div>
                    <div class="span6 special-div">
                        <button
                            id="removeButton"
                            class="btn"
                        >Remove</button>
                        <button
                            id="removeAllButton"
                            class="btn"
                        >Remove all</button>
                        <form class="form-search pull-right">
                            <div class="input-append">
                                <input
                                    type="text"
                                    class="search-query"
                                >
                                    <button
                                        id="searchSelectionConfig"
                                        type="submit"
                                        class="btn"
                                    >Search</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div
                    id="tableContainer"
                    class="row"
                >
                    <div
                        id="availableTable"
                        class="span6"
                    >
                        <table
                            class="table table-striped table-bordered"
                            id="showAvailableTable"
                        >
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
                        id="selectionTableContainer"
                        class="span6"
                    >
                        <table
                            class="table table-striped table-bordered"
                            id="selectionTable"
                        >
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
    </div>
</body>
</html>
