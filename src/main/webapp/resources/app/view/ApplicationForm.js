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
var imperium_ui_view_ApplicationForm = function() {
	'use strict';
	var module = {
		urlManager : null,
		init : function() {
			this.urlManager = imperium_ui_utils_UrlManager.module;
			// this.bindEvents();
			this.setValidation();
		},
		initForUpdate : function() {
		},
		setValidation : function() {
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
				me.submitForm();
			};

			var validationConfig = {
				rules : rules,
				messages : messages,
				submitHandler : submitCb
			};

			$('#formContainer form').validate(validationConfig);
		},
		bindEvents : function() {
			$("#submit").on('click', this.onCreateApplication);
		},
		redirectToApplicationList : function(data) {
			window.location.href = module.urlManager.getUrlForAppListWithLast();
		},
		submitForm : function() {
			var userInput = $('#formContainer form').serializeArray();
			var index;

			var jsonData = {

			};

			var eachName;
			var eachValue;
			var eachInput;
			for (index in userInput) {
				eachInput = userInput[index];
				eachName = eachInput['name'];
				eachValue = eachInput['value'];
				jsonData[eachName] = eachValue;
			}

			var successCb = function(serverResponse, textStatus, xmlHttpRequest) {
				var applicationId = serverResponse.data.applicationId;
				if (serverResponse.success === true) {
					module.redirectToApplicationList(serverResponse.data);
				} else {
				}
			};

			var failureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			};

//			var url = '/imperium/cors/application/create';
			var url = this.urlManager.getUrlForAppCreateSubmit();
			
			var options = {
				url : url,
				type : 'POST',
				contentType : "application/json; charset=UTF-8",
				data : JSON.stringify(jsonData),
				success : successCb,
				failure : failureCb
			};

			$.ajax(options);
		},
		onCreateApplication : function(event) {
			event.preventDefault();
			event.stopPropagation();
			module.submitForm();
		}
	};
	return {
		module : module
	};
}();
