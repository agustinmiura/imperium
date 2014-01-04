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
var imperium_ui_dao_Subject = function() {

	var urlManager = imperium_ui_utils_UrlManager.module;
	
	var updateRolesCb = function(data, successCb, failureCb) {
		var url = urlManager.getUrlForUpdateRoleForSubject();
		var method = "POST";

		var aSuccessCb = function(answerData, textStatus, xmlHttpRequest) {
			successCb(answerData);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			failureCb(xmlHttpRequest);
		};

		var data = {
			subjectId : data["subjectId"],
			add : data["add"],
			remove : data["remove"]
		};

		var options = {
			url : url,
			cache : false,
			type : method,
			success : aSuccessCb,
			failure : aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		};

		$.ajax(options);
	};

	var createCb = function(data, successCb, failureCb) {
		var url = "/imperium/subject/create.json";

		var aSuccessCb = function(serverResponse, textStatus, xmlHttpRequest) {
			successCb(serverResponse);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {

		};

		var toSend = {
			application : data["application"],
			name : data["name"]
		};

		$.ajax({
			url : url,
			cache : false,
			type : "POST",
			success : aSuccessCb,
			failure : aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		});

	};

	var updateCb = function(data, successCb, failureCb) {
		var url = urlManager.getUrlForSubjectSubmit();
		var aSuccessCb = function(serverResponse, textStatus, xmlHttpRequest) {
			successCb(serverResponse);
		};
		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
		};
		var toSend = {
			application : data.application,
			subject : data.subject
		};
		$.ajax({
			url : url,
			cache : false,
			type : "POST",
			success : aSuccessCb,
			failure : aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		});
	};

	var removeCb = function(data, successCb, failureCb) {
		//@todo remove
		//var url = "/imperium/weanubject/removal-from-app.json";
		var url = urlManager.getUrlForSubjectRemoval();
		
		var aSuccessCb = function(serverResponse) {
			successCb(serverResponse);
		};

		var aFailureCb = function(error) {
			failureCb(error);
		};

		var toSend = {
			application : data.application,
			subject : data.subject
		};

		$.ajax({
			url : url,
			cache : false,
			type : "POST",
			success : aSuccessCb,
			failure : aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(toSend)
		});
	};

	return {
		updateRoles : updateRolesCb,
		create : createCb,
		update : updateCb,
		remove : removeCb
	};

}();
