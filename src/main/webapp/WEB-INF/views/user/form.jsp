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
    src="<c:url value="/resources/app/view/user/Form.js" />"></script>
<script
    type="text/javascript"
    charset="utf-8">
	
</script>
</head>
<body>
    <div
        id="information"
        data-user-id="<c:out value="${user.id}"/>"
        data-user-name="<c:out value="${user.name}"/>"
        data-user-type="<c:out value="${user.type}"/>"
        class="notVisible"></div>
    </div>
    <!-- End of modal forms -->
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
        id="main-content"
        class="container">
        <div class="row">
            <div
                class="span5 offset4"
                id="center-side">
                <h4 class="my-text-center">
                    <c:if test="${user.id >=0}">
                                    Edit user
                                    </c:if>
                    <c:if test="${user.id <0}">
                                    Create an user
                                    </c:if>
                </h4>
                <form
                    method="post"
                    id="userForm"
                    class="form-horizontal">
                    <div class="control-group">
                        <label
                            class="control-label"
                            for="name">Name</label>
                        <div class="controls">
                            <input
                                type="text"
                                id="name"
                                name="name"
                                placeholder="User name here"> </input>
                        </div>
                    </div>
                    <div class="control-group">
                        <label
                            class="control-label"
                            for="type">Type</label>
                        <div class="controls">
                            <select
                                name="type"
                                id="type">
                                <c:forEach
                                    items="${userTypes}"
                                    var="userType">
                                    <option value="${userType.type}">${userType.representation}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label
                            class="control-label"
                            for="password">Password</label>
                        <div class="controls">
                            <input
                                type="password"
                                id="password"
                                name="password"> </input>
                        </div>
                    </div>
                    <div class="control-group">
                        <label
                            class="control-label"
                            for="passwordAgain">Password again</label>
                        <div class="controls">
                            <input
                                type="password"
                                id="passwordAgain"
                                name="passwordAgain"> </input>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls">
                            <button
                                id="submit"
                                type="submit"
                                class="btn">Submit</button>
                            <button
                                id="cancelButton"
                                type="reset"
                                class="btn">Cancel</button>
                        </div>
                    </div>
                </form>
            </div>
            <div
                class="span2"
                id="right-side"></div>
        </div>
    </div>
    <div class="footer">
        <div class="container">
            <p class="muted credit"></p>
        </div>
    </div>
</body>
</html>
