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
var urlManager = imperium_ui_utils_UrlManager.module;
var imperium_ui_view_ApplicationForm = function() {
	// 'use strict';
	var module = {
		data : undefined,
		status : "CREATE",
		init : function() {
			this.setData();
			this.setStatus();
			this.setValidation();
			this.bindEvents();
			if (this.inEditForm() === true) {
				this.setupForEdition();
			}
		},
		initForUpdate : function() {
		},
		setupForEdition : function() {
			var application = this.data.application;
			$('form input[name="name"]').val(application.name);
			$('form input[name="description"]').val(application.description);
		},
		inEditForm : function() {
			return (this.status === "EDIT");
		},
		setStatus : function() {
			var inEdition = (this.data.application.id >= 0);
			if (inEdition === true) {
				this.status = "EDIT";
			}
		},
		setData : function() {
			var information = $("#information");
			this.data = {
				application : {
					id : information.attr("data-application-id"),
					name : information.attr("data-application-name"),
					description : information
							.attr("data-application-description")
				}
			};
		},
		setValidation : function() {
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

			var validationConfig = {
				rules : rules,
				messages : messages
			};

			$('form').validate(validationConfig);
		},
		bindEvents : function() {
			var me = this;
			$("form").on("submit", function(event) {
				me.onSubmit(event);
			});
			$("#cancelButton").on("click", function(event) {
				me.onCancel(event);
			});
		},
		redirectToApplicationList : function(data) {
			window.location.href = urlManager.getUrlForAppListWithLast();
		},
		onCancel: function(event) {
			event.preventDefault();
			this.redirectToApplicationList({});
		},
		onSubmit : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var form = $(event.target).closest("form");
			if (form.valid()) {
				this.submitForm(form);
			}
		},
		getSubmitData : function(form) {
			var answer = {};

			var data = form.serializeArray();
			var index;
			var object;
			var name;
			var value;
			for (index in data) {
				object = data[index];
				name = object.name;
				value = object.value;
				answer[name] = value;
			}
			answer.id = this.data.application.id.toString();
			
			return answer;
		},
		submitForm : function(form) {
			var submitData = this.getSubmitData(form);
			if (this.inEditForm() === false) {
				this.createApplication(submitData);
			} else {
				this.editApplication(submitData);
			}
		},
		createApplication : function(submitData) {
			var successCb = function(response) {
				window.location = "/imperium/webapp/application/see-list";
			};
			imperium_ui_dao_Application.create(submitData, successCb, function(
					error) {
				/**
				 * @todo show the error to the user
				 */
				console.log("Create an application");
			});
		},
		editApplication : function(submitData) {
			var successCb = function(response) {
				window.location = "/imperium/webapp/application/see-list";
			};
			imperium_ui_dao_Application.edit(submitData, successCb, function(
					error) {
				/**
				 * @todo remove
				 */
				console.log("Edit application here");
			});
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_ApplicationForm.module;
	module.init();
});
