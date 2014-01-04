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
    src="<c:url value="/resources/app/view/profile/ChangePassword.js" />"></script>
<script
    type="text/javascript"
    charset="utf-8">
	
</script>
</head>
<body>
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
        class="container-fluid">
        <div class="row-fluid">
            <div
                class="span2"
                id="left-side">
            </div>
            <div
                class="span8"
                id="center-side">
                <div class="text-center">
                    <h4>Change password form</h4>
                </div>
                <div id="formContainer">
                    <form
                        id="changePasswordForm"
                        method="post"
                        action="<c:url value="/webapp/profile/do-change-password"/>"
                        class="form-horizontal">
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
                                for="newPassword">New password</label>
                            <div class="controls">
                                <input
                                    type="password"
                                    id="newPassword"
                                    name="newPassword"> </input>
                            </div>
                        </div>
                        <div class="control-group">
                            <label
                                class="control-label"
                                for="newPasswordAgain">New password
                                again</label>
                            <div class="controls">
                                <input
                                    type="password"
                                    id="newPasswordAgain"
                                    name="newPasswordAgain"> </input>
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                <button
                                    id="submit"
                                    type="submit"
                                    class="btn">Submit</button>
                                <button
                                    type="reset"
                                    name="cancel"
                                    class="btn">Cancel</button>
                            </div>
                        </div>
                    </form>
                </div>
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
