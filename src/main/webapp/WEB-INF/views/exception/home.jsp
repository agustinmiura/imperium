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
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/view/createApplication.css" />" />
<script type="text/javascript"
	src="<c:url value="/resources/app/view/application/ListDef.js" />"></script>
</head>
<body>
	<div class="navbar navbar-inverse navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid"></div>
		</div>
	</div>

	<div id="main-content" class="container-fluid">

		<div class="row-fluid">
			<div class="span4" id="left-side"></div>
			<div class="span6" id="center-side">
				<div class="alert alert-error alert-block">
					<c:out value="${exceptionClass}"></c:out>
					happened
					<p>
						Detail message :
						<c:out value="${message}"></c:out>
					</p>
					<p>
						Stack trace:
						<c:out value="${stackTrace}" />
					</p>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
</html>
