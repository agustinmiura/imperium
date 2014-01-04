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
var imperium_ui_view_profile_ChangePassword = function() {
    // "use strict";
    var urlManager = imperium_ui_utils_UrlManager.module;
    
    var module = {
	init : function() {
	    this.setupForms();
	    this.bindToButtons();
	},
	onCancelButton: function(event) {
	    event.preventDefault();
	    window.location = urlManager.getRootUrl();
	},
	bindToButtons: function() {
	  var me = this;
	  $('button[name="cancel"]').on("click", function(event){
	      me.onCancelButton(event);
	  });  
	    
	},
	setupForms : function() {
	    var me = this;

	    var rules = {
		password : {
		    required : true,
		    minlength : 8
		},
		newPassword : {
		    required : true,
		    minlength : 8
		},
		newPasswordAgain : {
		    required : true,
		    minlength : 8,
		    equalTo : "#newPassword"
		}
	    };

	    var passwordMessage = {
		required : "Need input",
		minlength : "It must have at least 8 characters"
	    };
	    var messages = {
		password : passwordMessage,
		newPassword : passwordMessage,
		newPasswordAgain : {
		    required : "Need input",
		    minlength : "It must have at least 8 characters",
		    equalTo : "The passwords do not match"
		}
	    };

	    $("form").validate({
		rules : rules,
		messages : messages,
		errorClass : "text-error"
	    });
	}
    };

    return {
	module : module
    };

}();

jQuery(function($) {
    var module = imperium_ui_view_profile_ChangePassword.module;
    module.init();
});
