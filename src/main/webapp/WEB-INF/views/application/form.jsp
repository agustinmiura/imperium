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
<!-- <link -->
<!--     rel="stylesheet" -->
<!--     type="text/css" -->
<%--     href="<c:url value="/resources/css/view/sample.css" />" /> --%>
<script
    type="text/javascript"
    src="<c:url value="/resources/app/view/application/Form.js" />"></script>
<script
    type="text/javascript"
    charset="utf-8">
	
</script>
</head>
<body>
    <div
        id="information"
        data-application-id="<c:out value="${application.id}"/>"
        data-application-name="<c:out value="${application.name}"/>"
        data-application-description="<c:out value="${application.description}"/>"
        class="notVisible"></div>
    </div>

    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
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
                    <c:if test="${application.id >=0}">
                    Application edit
                    </c:if>
                    <c:if test="${application.id<0}">
                    Application create
                    </c:if>
                </h4>
                <%@ include
                    file="/WEB-INF/views/fragment/application/addForm.jsp"%>
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
