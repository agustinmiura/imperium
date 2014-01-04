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
var imperium_ui_view_user_Form = function() {
	var urlManager = imperium_ui_utils_UrlManager.module;

	var module = {
		data : undefined,
		state : "CREATE",
		init : function() {
			this.setData();
			this.setStatus();
			if (this.inEditForm() === true) {
				this.setupForEdition();
			}
			this.bindEvents();
			this.setupForm();
		},
		setupForm : function() {
			var rules = {
				name : {
					required : true,
				},
				password : {
					required : true,
					minlength : 8
				},
				passwordAgain : {
					required : true,
					minlength : 8,
					equalTo : "#password"
				}
			};

			var messages = {
				name : {
					required : "Name is required"
				},
				password : {
					required : "The password is required",
					minlength : "It must contain at least 8 characters"
				},
				passwordAgain : {
					required : "Enter the password again please",
					minlength : "It must contain at least 8 characters",
					equalTo : "The passwords do not match"
				}
			};

			$("form").validate({
				rules : rules,
				messages : messages,
				errorClass : "text-error"
			});
		},
		setupForEdition : function() {
			var nameInput = $('form input[name="name"]');
			nameInput.val(this.data.user.name);
			nameInput.attr("readonly", true);

			$('form input[name="password"]').val("XXXXXXXXXXXXXXXXXX");
			$('form input[name="passwordAgain"]').val("XXXXXXXXXXXXXXXXXX");

			$('form select[name="type"]').val(this.data.user.type);

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
			answer.id = this.data.user.id.toString();
			return answer;
		},
		onSuccessUserCreate : function(userData) {
			window.location = urlManager.getUrlForUserGridAndShowLast();
		},
		onSuccessEdit : function(userData) {
			window.location = urlManager.getUrlForUserGridAndShowLast();
		},
		createUser : function(userData) {
			var me = this;
			imperium_ui_dao_User.create(userData, function(serverResponse) {
				if (serverResponse.success === true) {
					me.onSuccessUserCreate(serverResponse.data);
				}
			}, function(xmlHttpRequest) {
				console.log("Failed the creation");
			});
		},
		editUser : function(userData) {
			var me = this;
			imperium_ui_dao_User.edit(userData, function(serverResponse) {
				me.onSuccessEdit(userData.data);
			}, function(xmlHttpRequest) {
				console.log("Failed the edition");
			});
		},
		submitForm : function(form) {
			var submitData = this.getSubmitData(form);
			if (this.inEditForm() === false) {
				this.createUser(submitData);
			} else {
				this.editUser(submitData);
			}
		},
		onSubmit : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var form = $(event.target).closest("form");
			if (form.valid()) {
				this.submitForm(form);
			}
		},
		onCancel : function(event) {
			event.preventDefault();
			window.location = urlManager.getUrlForUserGridAndShowLast();
		},
		bindEvents : function() {
			var me = this;
			$("#userForm").on("submit", function(event) {
				me.onSubmit(event);
			});

			$("#cancelButton").on("click", function(event) {
				me.onCancel(event);
			});
		},
		setData : function() {
			var information = $("#information");
			this.data = {
				user : {
					id : information.attr("data-user-id"),
					name : information.attr("data-user-name"),
					type : information.attr("data-user-type")
				}
			};
		},
		setStatus : function() {
			var inEdition = (this.data.user.id >= 0);
			if (inEdition === true) {
				this.status = "EDIT";
			}
		},
		inEditForm : function() {
			return (this.status === "EDIT");
		}
	};
	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_user_Form.module;
	module.init();

});
