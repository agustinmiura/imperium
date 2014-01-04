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
var imperium_ui_view_subject_Form = function() {
	"use strict";

	var urlManager = imperium_ui_utils_UrlManager.module;

	var module = {
		data : undefined,
		status : "CREATE",
		init : function() {

			this.setData();
			this.setStatus();
			if (this.isEdition() === true) {
				this.setupForEdition();
			}
			this.bindEvents();
			this.setupForm();
		},
		onSubmitSubject : function(event) {
			event.preventDefault();
			event.stopPropagation();
			var form = $(event.target).closest("form");
			this.submitForm(form);
		},
		onCancel : function(event, applicationId) {
			event.preventDefault();
			window.location = urlManager.getUrlForAppShowEdit(applicationId, "subject");
		},
		bindEvents : function() {
			var me = this;
			var cb = function(event) {
				me.onSubmitSubject(event);
			};
			$("#subjectForm #submit").on("click", cb);
			$("#cancelButton").on("click", function(event) {
				me.onCancel(event, me.data.application.id);
			});
		},
		setupForm : function() {
			var rules = {
				name : {
					required : true
				}
			};

			var messages = {
				name : {
					required : "Please provide a name"
				}
			};

			var me = this;
			var submitCb = function(form) {
				me.submitForm(form);
			};

			var config = {
				rules : rules,
				messages : messages,
				submitHandler : submitCb
			};

			$("#subjectForm").validate(config);
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
			;

			answer.application = this.data.application;
			answer.id = this.data.subject.id;

			return answer;
		},
		submitOnCreate : function(form) {
			// application/redirect-to-edit/
			var data = this.getSubmitData(form);

			var aSuccessCb = function(serverResponse) {
				var applicationId = serverResponse.extraData.application.id;
				var url = urlManager.getUrlForAppShowEdit(applicationId,
						"subject");
				window.location = url;
			};
			var errorCb = function(error) {

			};
			imperium_ui_dao_Subject.create(data, aSuccessCb, errorCb);
		},
		submitOnEdit : function(form) {
			var me = this;

			var submitData = this.getSubmitData(form);
			submitData.subject = {
				id : submitData.id,
				name : submitData.name
			};

			var aSuccessCb = function(serverResponse) {
				window.location = urlManager
						.getUrlForAppShowEdit(me.data.application.id, "subject");
			};
			var aFailureCb = function(error) {
			};
			imperium_ui_dao_Subject.update(submitData, aSuccessCb, aFailureCb);

		},
		doSubmit : function(form) {
			var isEdition = this.isEdition();
			if (isEdition === false) {
				this.submitOnCreate(form);
			} else {
				this.submitOnEdit(form);
			}
		},
		submitForm : function(form) {
			var isValid = form.valid();
			if (isValid === true) {
				this.doSubmit(form);
			}
		},
		setupForEdition : function() {
			var subjectName = this.data.subject.name;
			var field = $('form input[name="name"]');
			field.val(subjectName);

		},
		isEdition : function() {
			return (this.status === "EDIT");
		},
		setStatus : function() {
			var subjectId = this.data.subject.id;
			var isEditing = (subjectId >= 0);
			if (isEditing === true) {
				this.status = "EDIT";
			}
		},
		setData : function() {
			var information = $("#information");

			var applicationId = information.attr("data-application-id");
			var applicationName = information.attr("data-application-name");
			var applicationDescription = information
					.attr("data-application-description");

			var subjectId = information.attr("data-subject-id");
			var subjectName = information.attr("data-subject-name");

			this.data = {
				application : {
					id : applicationId,
					name : applicationName,
					description : applicationDescription
				},
				subject : {
					id : subjectId,
					name : subjectName
				}
			};
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_subject_Form.module;
	module.init();
});
