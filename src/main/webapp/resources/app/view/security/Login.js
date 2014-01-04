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
var imperium_ui_view_security_Login = function() {
	//"use strict";
	var module = {
		data:{
			fromInvalidLogin:false
		},
		init : function() {
			this.setData();
			this.setupForms();
			this.setErrorMessage();
		},
		setErrorMessage: function() {
			var errorContainer = $("#errorMessage");
			if (this.data.fromInvalidLogin===true) {
				errorContainer.text("Invalid user name or password");
			} else {
				errorContainer.removeClass("alert");
				errorContainer.removeClass("alert-error");
				errorContainer.addClass("hidden");
			}
		},
		setData: function() {
			var information = $("#information");
			var fromInvalidLogin = information.attr("data-from-invalid-login");
			
			this.data = {
				fromInvalidLogin:(fromInvalidLogin==="true")
			};
		},
		setupForms : function() {
			var me = this;

			var rules = {
				username : {
					required : true
				},
				password : {
					required : true,
					minlength : 8
				}
			};

			var messages = {
				username : {
					minlength : "It must have at least 4 characters"
				},
				password : {
					required : "Please enter the password",
					minlength : "It must have at least 8 characters"
				}
			};

			$("form").validate({
				rules : rules,
				messages : messages,
				errorClass:"text-error"
			});
		}
	};

	return {
		module : module
	};

}();

jQuery(function($) {
	var module = imperium_ui_view_security_Login.module;
	module.init();
	
	globalModule = module;
});
