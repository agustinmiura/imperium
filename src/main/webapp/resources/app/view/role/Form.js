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
var imperium_ui_view_role_Form = function() {
	// "use strict";

	var module = {
		urlManager : undefined,
		data : undefined,
		/**
		 * status can be "CREATE" or "EDIT"
		 */
		status : "CREATE",
		init : function() {
			module.urlManager = imperium_ui_utils_UrlManager.module;

			var data = module.getData();
			module.data = data;

			module.setStatus();

			if (module.isEdition()) {
				module.setupUiForEdition();
			}
			module.setupForm();
			module.bindEvents();

		},
		onCreateRole : function(event) {

			event.preventDefault();
			event.stopPropagation();

			module.submitForm();
		},
		onCancel : function(event, applicationId) {
			event.preventDefault();
			window.location = this.urlManager.getUrlForAppShowEdit(
					applicationId, "ROLE");
		},
		bindEvents : function() {
			var me = this;

			$("#formContainer #submit").on("click", this.onCreateRole);
			$("#cancelButton").on("click", function(event) {
				me.onCancel(event, me.data.application.id);
			});
		},
		setupForm : function() {
			var me = this;

			var rules = {
				name : {
					required : true
				},
				description : {
					required : true
				}
			};

			var messages = {
				name : {
					required : "Please provide a name"
				},
				description : {
					required : "Please provide a description"
				}
			};

			var submitCb = function(form) {
				module.submitForm();
			};

			var validationConfig = {
				rules : rules,
				messages : messages,
				submitHandler : submitCb
			};

			$('#roleForm').validate(validationConfig);

		},
		submitForm : function() {
			var form = $("#roleForm");
			var isValid = form.valid();

			if (isValid === true) {
				if (this.isEdition()) {
					module.submitForEdition();
				} else {
					module.submitForCreate();
				}
			}

		},
		getSubmitData : function() {
			var me = this;

			var form = $("#roleForm");

			var serializedForm = form.serializeArray();
			var jsonData = {};

			var eachName;
			var eachValue;
			var index;
			var eachInput;

			for (index in serializedForm) {
				eachInput = serializedForm[index];
				eachName = eachInput["name"];
				eachValue = eachInput["value"];
				jsonData[eachName] = eachValue;
			}
			jsonData.applicationId = me.data.application.id;

			return jsonData;
		},
		submitForEdition : function() {
			var me = this;

			var form = $("#roleForm");

			var jsonData = me.getSubmitData();

			var successCb = function(serverResponse) {
				if (serverResponse.success === true) {
					me.onSuccessSubmit(serverResponse);
				} else {
					me.onFailureSubmit();
				}
			};

			var failureCb = function(xmlHttpRequest, textStatus, error) {
				me.onFailureSubmit();
			};

			imperium_ui_dao_Role.editRole(me.data.role.id, jsonData, successCb,
					failureCb);
		},
		submitForCreate : function() {
			var me = this;

			var jsonData = me.getSubmitData();

			var successCb = function(serverResponse) {
				if (serverResponse.success === true) {
					me.onSuccessSubmit(serverResponse);
				} else {
					me.onFailureSubmit();
				}
			};

			var failureCb = function(xmlHttpRequest, textStatus, error) {
				me.onFailureSubmit();
			};

			imperium_ui_dao_Role.addRole(jsonData, successCb, failureCb);
		},
		onSuccessSubmit : function(serverResponse) {
			var data = serverResponse.data;

			var roleId = this.data.role.id;
			var applicationId = this.data.application.id;
			var url = module.urlManager.getUrlForAppShowEdit(applicationId,
					"ROLE");

			window.location = url;
		},
		onFailureSubmit : function() {
		},
		setupUiForEdition : function() {
			var me = module;

			var role = me.data.role;

			var form = $("form");
			var nameField = form.find('input[name="name"]');
			var descriptionField = form.find('input[name="description"]');

			nameField.val(role.name);
			descriptionField.val(role.description);

		},
		isEdition : function() {
			return (module.status === "EDIT");
		},
		setStatus : function() {
			var isEditing = (module.data.role.id >= 0);
			if (isEditing) {
				module.status = "EDIT";
			}
		},
		getData : function() {
			var information = $('#information');

			var roleId = information.attr("data-role-id");
			var roleName = information.attr("data-role-name");
			var roleDescription = information.attr("data-role-description");

			var applicationId = information.attr("data-application-id");
			var applicationName = information.attr("data-application-name");
			var applicationDescription = information
					.attr("data-application-description");

			return {
				role : {
					id : roleId,
					name : roleName,
					description : roleDescription
				},
				application : {
					id : applicationId,
					name : applicationName,
					description : applicationDescription
				}
			};
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_role_Form.module;
	module.init();
});
