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
var imperium_ui_dao_Application = function() {

	var urlManager = imperium_ui_utils_UrlManager.module;

	var getApplicationCb = function(id, onSuccessCb, onFailureCb) {
		var url = urlManager.getUrlForAppShowInfo(id);
		url = url.replace("{id}", id);

		var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
			onSuccessCb(data);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			onFailureCb(xmlHttpRequest);
		};

		var options = {
			url : url,
			cache : false,
			type : "GET",
			success : aSuccessCb,
			failure : aFailureCb
		};

		$.ajax(options);
	};

	var editApplication = function(data, onSuccessCb, onFailureCb) {
		var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
			onSuccessCb(data);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			onFailureCb(xmlHttpRequest);
		};

		var options = {
			url : urlManager.getUrlForAppEditSubmit(),
			cache : false,
			type : "POST",
			success : aSuccessCb,
			failure : aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		};

		$.ajax(options);
	};

	var createApplicationCb = function(data, onSuccessCb, onFailureCb) {
		var url = urlManager.getUrlForAppCreateSubmit();

		var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
			onSuccessCb(data);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			onFailureCb(xmlHttpRequest);
		};

		var options = {
			url : url,
			cache : false,
			type : "POST",
			success : aSuccessCb,
			failure : aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		};

		$.ajax(options);
	};

	/**
	 * data js Object data["idsToRemove"]=[1,2,3,4....]
	 * data["applicationId"]=Application Id
	 */
	var removePermissionsCb = function(data, onSuccessCb, onFailureCb) {
		var url = "/imperium/permission/ajax/removal-from-app";

		var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
			onSuccessCb(data);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			onFailureCb(xmlHttpRequest);
		};

		var options = {
			url : url,
			cache : false,
			type : "POST",
			success : aSuccessCb,
			failure : aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		};

		$.ajax(options);
	};

	var resetKeyCb = function(id, onSuccessCb, onFailureCb) {
		var url = urlManager.getUrlForAppResetKey(id);

		var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
			onSuccessCb(data);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			onFailureCb(xmlHttpRequest);
		};

		var options = {
			url : url,
			cache : false,
			type : "GET",
			success : aSuccessCb,
			failure : aFailureCb,
		};

		$.ajax(options);

	};

	var removeCb = function(id, successCb, failureCb) {
		var url = urlManager.getUrlForApplicationRemoval(id);
		var options = {
			url:url,
			cache:false,
			success:successCb,
			failure:failureCb
		};
		
		$.ajax(options);
	};

	return {
		get : getApplicationCb,
		edit : editApplication,
		removePermissionsFromApp : removePermissionsCb,
		create : createApplicationCb,
		resetKey : resetKeyCb,
		remove:removeCb
	};

}();
