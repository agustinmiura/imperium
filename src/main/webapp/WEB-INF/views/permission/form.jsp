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
<link
    rel="stylesheet"
    type="text/css"
    href="<c:url value="/resources/css/view/createApplication.css" />" />
<link
    rel="stylesheet"
    type="text/css"
    href="<c:url value="/resources/css/view/sample.css" />" />
<script
    type="text/javascript"
    src="<c:url value="/resources/app/view/permission/Form.js" />"></script>
<script
    type="text/javascript"
    charset="utf-8">
    
</script>
</head>
<body>
    <div
        id="information"
        data-application-id="<c:out value="${applicationId}"/>"
        data-application-name="<c:out value="${name}"/>"
        data-application-description="<c:out value="${description}"/>"
        data-id="<c:out value="${id}"/>"
        data-resource="<c:out value="${resource}"/>"
        data-action="<c:out value="${action}"/>"
        class="notVisible"></div>

    </div>
    <!-- End of modal forms -->
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <a
                    class="brand"
                    href="#"><c:out value="${name}" /></a>
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
                class="span4 offset4"
                id="center-side">
                <div class="my-text-center">
                    <h4>
                        <c:if test="${id >=0}">
                            Permission edition
                        </c:if>
                        <c:if test="${id <0}">
                            Permission creation
                        </c:if>
                    </h4>
                </div>
                <form
                    id="permissionForm"
                    class="form-horizontal">

                    <div id="useListExplanation" class="control-group alert alert-info">
                        <!--  
                        <button
                            type="button"
                            class="close"
                            data-dismiss="alert">×</button>
                        -->
                        In the permission field you can enter
                        only an action name . For example "create" or
                        a list of separated values by comma like :
                        "create,read,update" 
                    </div>

                    <div class="control-group">
                        <label
                            class="control-label"
                            for="resource">Resource</label>
                        <div class="controls">
                            <input
                                type="text"
                                id="resource"
                                name="resource"
                                placeholder="Resource"> </input>
                        </div>
                    </div>

                    <div class="control-group">
                        <label
                            class="control-label"
                            for="action">Action</label>
                        <div class="controls">
                            <input
                                type="text"
                                id="action"
                                name="action"
                                placeholder="Enter action here"> </input>
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
        </div>
    </div>
</body>
</html>
