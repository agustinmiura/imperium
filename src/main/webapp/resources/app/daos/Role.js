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
var imperium_ui_dao_Role = function() {

	var urlManager = imperium_ui_utils_UrlManager.module;
	
	var listRolesCb = function(applicationId, paginationParameters, successCb,
			failureCb) {
		var parameters = paginationParameters;

		var url = null;
		if ("page" in parameters) {
			var page = parameters["page"];
			url = "/imperium/roles/ajax/list/{app}?page= {page}";
			url = url.replace("{app}", applicationId);
			url = url.replace("{page}", page);
		} else {
			url = "/imperium/roles/ajax/list/{app}";
		}

		var aSuccessCb = function(data, onSuccessCb, onFailureCb) {
			successCb(data);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, error) {
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

	var getRoleCb = function(id, successCb, failureCb) {
		var url = "/imperium/role/ajax/get/{id}";
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

	var editRoleCb = function(id, data, successCb, failureCb) {
		//var url = "/imperium/role/ajax/edit/{id}";
		url = urlManager.getForRoleEditSubmit(id);

		var aSuccessCb = function(answerData, textStatus, xmlHttpRequest) {
			successCb(answerData);
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

	var addRoleCb = function(data, successCb, failureCb) {
		//var url = "/imperium/role/ajax/create";
		var url = urlManager.getUrlForRoleSubmit();
		
		var aSuccessCb = function(serverResponse, textStatus, xmlHttpRequest) {
			successCb(serverResponse);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			failureCb(xmlHttpRequest, textStatus, errorThrown);
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

	var getPermissionsCb = function(roleId, pagination, successCb, failureCb) {
		var url = "/imperium/json/role/get-permission";

		var aSuccessCb = function(answerData, textStatus, xmlHttpRequest) {
			successCb(answerData);
		};

		var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			onFailureCb(xmlHttpRequest);
		};

		var data = {
			roleId : roleId,
			page : pagination["page"],
			maxSize : pagination["maxSize"]
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

	updatePermissionsCb = function(data, successCb, failureCb) {
		var url = urlManager.getUrlToAddPermissionsToRole();

		var aSuccessCb = function(answerData, textStatus, xmlHttpRequest) {
			successCb(answerData);
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
	 * Send to the role controller a request to 
	 * /imperium/json/role/get-subject-qty
	 * for each role id get the subject qty;
	 * 
	 * data = {
	 * 		"roleList" where each element is a role id
	 * }
	 * 
	 * serverResponse = {
	 * 	success:true/false,
	 *  subjectQtyMap: map where each value is the subject qty
	 * }
	 */
	var getSubjectQtyCb = function(data, successCb, failureCb) {

		var url = "/imperium/json/role/get-subject-qty";

		var aSuccessCb = function(answerData, textStatus, xmlHttpRequest) {
			successCb(answerData);
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
	 * data:{
	 * 	roles:[]  ->List with ids of the roles to remove
	 * }
	 * successCb : cb to execute when the removal is successful,
	 * failureCb : cb to execute when the removal failed
	 */
	removeFromApplicationCb = function(data, successCb, failureCb) {
		var aSuccessCb = function(serverResponse) {
			successCb(serverResponse);
		};
		
		var aFailureCb = function(error) {
			failureCb(error);
		};
		
		var options = {
			//url:"/imperium/role/ajax/removal-from-app",
			url:urlManager.getUrlForRoleRemoveSubmit(),
			cache:false,
			type:"POST",
			success:aSuccessCb,
			failure:aFailureCb,
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		};
		
		$.ajax(options);
	};

	return {
		listRoles : listRolesCb,
		getRole : getRoleCb,
		addRole : addRoleCb,
		editRole : editRoleCb,
		getPermissions : getPermissionsCb,
		updatePermission : updatePermissionsCb,
		getSubjectQty : getSubjectQtyCb,
		removeFromApplication:removeFromApplicationCb
	};

}();
