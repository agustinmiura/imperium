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
var imperium_ui_dao_Permission = function() {

	var urlManager = imperium_ui_utils_UrlManager.module;
	
	var getPermissionCb = function(id, successCb, failureCb) {
		var url = "/imperium/permission/ajax/get/{id}";
		url = url.replace("{id}", id);

		var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
			successCb(data);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			failureCb(xmlHttpRequest);
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

	var editPermission = function(id, data, onSuccessCb, onFailureCb) {
//		@todo remove
//		var url = "/imperium/permission/ajax/edit/{id}";
//		url = url.replace("{id}", id);
		var url = urlManager.getUrlForPermissionEditSubmit(id);
		
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

	var addPermissionCb = function(data, successCb, failureCb) {
		var url = urlManager.getUrlForPermissionSubmit();

		var aSuccessCb = function(serverResponse, textStatus, xmlHttpRequest) {
			successCb(serverResponse);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			failureCb(xmlHttpRequest);
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
	 * Data must be an object with data["applicationId"]:id as String
	 * data["page"]:page to request as String data["maxSize"]:query size as
	 * String data["sort"]:sort criteria data["direction"]:direction of the sort
	 * data["action.prefix"]: Get the permissions where resource is like PARAM.*
	 * 
	 */
	var listWithActionPrefixCb = function(data, successCb, failureCb) {
		var paramsToCheck = [ "applicationId", "page", "maxSize", "sort",
				"direction", "action.prefix" ];

		var index;
		var value;
		for (index in paramsToCheck) {
			value = paramsToCheck[index];
			if ((value in data) === false) {
				var message = "The function listWithActionPrefix in the DAO";
				message = message + " is called without the parameter ";
				message = message + " " + value;
				throw new Error(message);
			}
		}

		var url = "/imperium/permission/list-where";

		var aSuccessCb = function(serverResponse) {
			successCb(serverResponse);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			failureCb(xmlHttpRequest);
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
	 * Data must be an object with data["applicationId"]:id as String
	 * data["page"]:page to request as String data["maxSize"]:query size as
	 * String data["sort"]:sort criteria data["direction"]:direction of the sort
	 * data["resource.prefix"]: Get the permissions where resource is like
	 * PARAM.*
	 * 
	 */
	var listWithResourcePrefixCb = function(data, successCb, failureCb) {
		var paramsToCheck = [ "applicationId", "page", "maxSize", "sort",
				"direction", "resource.prefix" ];

		var index;
		var value;
		for (index in paramsToCheck) {
			value = paramsToCheck[index];
			if ((value in data) === false) {
				var message = "The function listWithActionPrefix in the DAO";
				message = message + " is called without the parameter ";
				message = message + " " + value;
				throw new Error(message);
			}
		}

		var url = "/imperium/permission/list-where";

		var aSuccessCb = function(serverResponse) {
			successCb(serverResponse);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			failureCb(xmlHttpRequest);
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
	 * Send to the url "/imperium/permission/ajax/removal-from-app" type:POST
	 * data = { applicationId : application id as String permissions: ids to
	 * remove as String each one }
	 */
	var removeFromAppCb = function(data, successCb, failureCb) {
		var toSend = {
			applicationId:data.applicationId,
			permissions:data.permissions
		};
		
		var aSuccessCb = function(serverResponse) {
			successCb(serverResponse);
		};
		
		var options = {
			url : urlManager.getUrlForPermissionRemoval(),
			cache : false,
			type : "POST",
			success : aSuccessCb,
			failure : function(error) {
			},
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(toSend)
		};

		$.ajax(options);
	};

	return {
		getPermission : getPermissionCb,
		editPermission : editPermission,
		addPermission : addPermissionCb,
		listWithResourcePrefix : listWithResourcePrefixCb,
		listWithActionPrefix : listWithActionPrefixCb,
		removeFromApp : removeFromAppCb
	};
}();
