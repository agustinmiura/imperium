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
<script src="/imperium/resources/app/view/security/Login.js"></script>
</head>
<body>
    <!-- Information from the server -->
    <div
        id="information"
        class="hidden"
        data-from-invalid-login="<c:out value="${FROM_INVALID_CREDENTIALS}"/>"></div>
    <div class="container">
        <div class="row">
            <div class="span4 offset4 well">
                <legend>Please Sign In</legend>
                <div id="errorMessage" class="alert alert-error">
<!--                 Message here -->
<!--                     <a -->
<!--                         class="close" -->
<!--                         data-dismiss="alert" -->
<!--                         href="#">×</a>Incorrect Username or Password! -->
                </div>
                <form
                    method="post"
                    action="/imperium/webapp/security/doLogin"
                    accept-charset="UTF-8">
                    <input
                        type="text"
                        id="username"
                        class="span4"
                        name="username"
                        placeholder="Username">
                        <input
                            type="password"
                            id="password"
                            class="span4"
                            name="password"
                            placeholder="Password">
                            <!--                             <label class="checkbox"> -->
                            <!--                                 <input -->
                            <!--                                     type="checkbox" -->
                            <!--                                     name="remember" -->
                            <!--                                     value="1"> Remember Me  -->
                            <!--                             </label> -->
                            <button
                                type="submit"
                                name="submit"
                                class="btn btn-info btn-block">Sign in</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
