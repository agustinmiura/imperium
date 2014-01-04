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
var imperium_ui_view_permission_Form = function() {
	// 'use strict';

	var module = {
		urlManager : undefined,

		data : undefined,

		status : "CREATE",

		init : function() {
			module.urlManager = imperium_ui_utils_UrlManager.module;
			module.setData();
			module.setStatus();
			module.setupValidationPlugin();
			module.bindEvents();
			module.setupForm();
			module.setupTypeAhead();

			if (module.isEdition() === true) {
				module.setupUiForEdition();
			}

		},
		setupValidationPlugin : function() {
			imperium_ui_utils_Util.addRegularExpressionValidation();
		},
		setupUiForEdition : function() {
			var me = module;

			var permission = me.data.permission;

			var form = $("form");
			var resourceField = form.find('input[name="resource"]');
			var actionField = form.find('input[name="action"]');

			resourceField.val(permission.resource);
			actionField.val(permission.action);

		},
		bindEvents : function() {
			var me = this;
			$("#formContainer #submit").on("click", this.onPermissionSubmit);
			$("#cancelButton").on("click", function(event) {
				me.onCancel(event, me.data.application.id);
			});
		},
		isEdition : function() {
			return (module.status === "EDIT");
		},
		onCancel : function(event, applicationId) {
			event.preventDefault();
			window.location = this.urlManager.getUrlForAppShowEdit(
					applicationId, "PERMISSION");
		},
		onPermissionSubmit : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var form = $("#permissionForm");
			var isValid = form.valid();

			if (isValid === true) {
				if (module.isEdition() === true) {
					module.submitForEdition();
				} else {
					module.submitForCreation();
				}
			}
		},
		setupFormForAdd : function() {
			var me = this;

			var rules = {
				action : {
					required : true,
					regex : /^[a-zA-Z0-9]+(,[a-zA-Z0-9]+)*$/
				},
				resource : {
					required : true
				}
			};

			var messages = {
				action : {
					required : "Please enter content",
					regex : "The input for the actions is invalid"
				},
				resource : {
					required : "Please enter content"
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

			$("form").validate(validationConfig);

		},
		setupFormForEdit : function() {
			var me = this;

			var rules = {
				action : {
					required : true
				},
				resource : {
					required : true
				}
			};

			var messages = {
				action : {
					required : "Please enter content"
				},
				resource : {
					required : "Please enter content"
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

			$("form").validate(validationConfig);
			
			$("#useListExplanation").remove();

		},
		setupForm : function() {
			if (module.isEdition() === false) {
				module.setupFormForAdd();
			} else {
				module.setupFormForEdit();
			}
		},
		setupResourceTypeAhead : function() {
			var me = this;

			var sourceCb = function(query, process) {
				var applicationId = me.data.application.id;
				var data = {
					page : "0",
					maxSize : "25",
					sort : "id",
					direction : "ASC",
					applicationId : applicationId.toString()
				};
				data["resource.prefix"] = query;

				var url = me.urlManager.getUrlForPermissionJsonListWhere();
				$.ajax({
					url : url,
					cache : false,
					type : "POST",
					success : function(serverResponse) {
						var index;
						var value;
						var answer = [];
						var list = serverResponse.permissionList;
						for (index in list) {
							value = list[index];
							answer.push(value.resource);
							;
						}
						process(answer);
					},
					failure : function() {
					},
					contentType : "application/json; charset=UTF-8",
					data : JSON.stringify(data)
				});
			};

			var lazySourceCb = _.debounce(sourceCb, 0.5 * 1000);

			var typeAheadConfig = {
				minLength : 1,
				source : function(query, process) {
					lazySourceCb(query, process);
				}
			};

			$("#resource").typeahead(typeAheadConfig);
		},

		setupTypeAhead : function() {
			this.setupResourceTypeAhead();
		},

		submitForm : function() {

			var form = $("#permissionForm");
			var isValid = form.valid();

			if (isValid === true) {
				if (module.isEdition() === true) {
					module.submitForEdition();
				} else {
					module.submitForCreation();
				}
			}
		},

		submitForEdition : function() {
			var me = this;
			var jsonData = this.getSubmitData();

			var applicationId = module.data.application.id;
			var successCb = function(serverResponse) {
				window.location = me.urlManager.getUrlForAppShowEdit(
						applicationId, "PERMISSION");
			};

			var data = {
				resource : jsonData.resource,
				action : jsonData.action
			};
			imperium_ui_dao_Permission.editPermission(jsonData.id, data,
					successCb, function() {
					});
		},

		getSubmitData : function() {
			var me = this;

			var form = $("#permissionForm");

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
			jsonData.applicationId = me.data.application.id.toString();
			jsonData.id = me.data.permission.id.toString();

			return jsonData;
		},

		onSuccessSubmit : function(serverResponse) {
			var me = this;
			var data = serverResponse.data;
			var extraData = serverResponse.extraData;
			var applicationId = extraData.applicationId;

			var url = me.urlManager.getUrlForAppShowEdit(applicationId,
					"PERMISSION");
			window.location = url;
		},

		createErrorDiv : function(errorMessage) {
			var div = '<div id="errorDiv" class="alert alert-error">{message}</div>';
			return div.replace("{message}", errorMessage);
		},

		removeErrorDiv : function() {
			$("#errorDiv").remove();
		},
		
		showSubmitError : function(errorObject) {
			var attribute = errorObject.attribute;
			var message = errorObject.message;
			
			var errorDiv = this.createErrorDiv(message);
		
			var divContainer = $('div[class="my-text-center"]');
			divContainer.append(errorDiv);
		},

		extractErrorMessage : function(serverResponse) {
			var answer = null;
			var hasErrorMessage = ("map" in serverResponse);
			if (hasErrorMessage === true) {
				var map = serverResponse.map;
				var keys = Object.keys(map);
				var index;
				var eachKey;
				for (index in keys) {
					eachKey = keys[index];
					message = map[eachKey];
					answer={
						attribute:eachKey,
						message:message
					};
					break;
				}
			}
			return answer;
		},

		onFailureSubmit : function(serverResponse) {
			this.removeErrorDiv();
			var errorObject = this.extractErrorMessage(serverResponse);
			if (errorObject.constructor === Object ) {
				this.showSubmitError(errorObject);
			}
		},

		submitForCreation : function() {
			var me = this;

			var jsonData = this.getSubmitData();

			var successCb = function(serverResponse) {
				if (serverResponse.success === true) {
					me.onSuccessSubmit(serverResponse);
				} else {
					me.onFailureSubmit(serverResponse);
				}

			};

			var failureCb = function(serverResponse) {
				me.onFailureSubmit(serverResponse);
			};

			imperium_ui_dao_Permission.addPermission(jsonData, successCb,
					failureCb);
		},

		setStatus : function() {
			var isEditing = (module.data.permission.id >= 0);
			if (isEditing === true) {
				module.status = "EDIT";
			}
		},

		getData : function() {
			var information = $("#information");

			var permissionId = information.attr("data-id");
			permissionId = parseInt(permissionId);
			var resource = information.attr("data-resource");
			var action = information.attr("data-action");

			var applicationId = information.attr("data-application-id");
			applicationId = parseInt(applicationId);
			var name = information.attr("data-application-name");
			var description = information.attr("data-application-description");

			return {
				permission : {
					id : permissionId,
					resource : resource,
					action : action
				},
				application : {
					id : applicationId,
					name : name,
					description : description
				}
			};
		},

		setData : function() {
			module.data = module.getData();
		}

	};

	return {
		module : module
	};

	return module;
}();

jQuery(function($) {
	var module = imperium_ui_view_permission_Form.module;
	module.init();
});
