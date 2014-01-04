<%@ include file="/WEB-INF/views/fragment/jspHeader.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
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
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Imperium application</title>
</head>
<body>
    <p>
        Sample boolean :
        <c:out value="${sessionScope.sampleBoolean}" />
    </p>
    <p>
        Sample value in map :
        <c:out value="${sessionScope.test['boolean']}" />
    </p>
    <p>
        Sample value again in map :
        <c:out value="${test['boolean']}" />
    </p>
    <p>
        <c:if test="${sessionScope.test['boolean']}">It's true</c:if>
    </p>
</body>
</html>
