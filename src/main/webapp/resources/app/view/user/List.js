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
var imperium_ui_view_user_List = function() {

    var urlManager = imperium_ui_utils_UrlManager.module;

    var module = {
	init : function() {
	    this.renderUserTable();
	    this.bindToButton();
	    this.bindToTableAction();
	    this.fixUi();
	},
	fixUi : function() {
	    $("#dataTableButton").text("Create user");
	},
	doUserEdition : function(id) {
	    var url = urlManager.getUrlForUserEdit(id);
	    window.location = url;
	},
	onUserRemoved : function(userId) {
	},
	subDoRemoval : function(id) {
	    var me = this;

	    var data = {
		id : id
	    };

	    var okCb = function(serverResponse) {
		window.location = urlManager.getUrlForUserGridAndShowLast();
	    };
	    var failureCb = function(failure) {
		console.log("Failure remove");
	    };
	    imperium_ui_dao_User.remove(data, okCb, failureCb);
	},
	doUserRemoval : function(id) {
	    var me = this;

	    var message = "Are you sure you want to remove the ";
	    message = message + "selected user ? ";

	    bootbox.confirm(message, function(answer) {
		if (answer === true) {
		    me.subDoRemoval(id);
		}
	    });
	},
	onActionButtonClicked: function(element) {
		var imageElement = $(element).find('i');
		this.onImageActionClicked(imageElement);
	},
	onImageActionClicked: function(element) {
		var action = $(element).attr('data-action');
		var id = $(element).attr('data-id');
			
		if (action==="magicAction") {
			this.doUserEdition(id);
		//
		}  else if (action==='removeAction') {
			this.doUserRemoval(id);
		}
	},
	onActionClicked: function(event) {
		var target = event.target;
		var className = target.className;
		if (className==='btn') {
			//on button clicked 
			this.onActionButtonClicked(target);
		} else {
			// i element clicked
			this.onImageActionClicked(target);
		}
	},
	onCreateUser : function() {
	    window.location = urlManager.getUrlForUserCreate();
	},
	bindToTableAction : function() {
	    var me = this;
	    
	    $("table").on("click", 'tr a[class="btn"]', function(event) {
			me.onActionClicked(event);
		});
	},
	bindToButton : function() {
	    var me = this;
	    $("#dataTableButton").on("click", function(event) {
		me.onCreateUser();
	    });
	},
	renderUserType : function(type) {

	},
	renderUserTable : function() {
	    var tableSelector = ("table");

	    var columnDefinition = [
		    {
			"mData" : "name",
			"bSortable" : true,
			"bVisible" : true
		    },
		    {
			"mData" : "typeAsString",
			"bSortable" : false,
			"bVisible" : true
		    },
		    {
			"mData" : "action",
			"bSortable" : false,
			"sWidth" : 190,
			"mRender" : function(data, type, row) {
			    var itemId = row["id"];
			    var data = {
				id : itemId,
				type : "role",
				entityId : "grid_action_menu_user_id_" + itemId,
				magicAction : true,
				removeAction : true,
				bookAction : false,
				pencilAction : false,
				editAction : false
			    };
			    var html = imperium_ui_utils_Ui
				    .generateContentFromTemplate(
					    "#handlebarTemplate_grid_actions",
					    data);
			    return html;
			}
		    } ];

	    var url = urlManager.getUrlForUserList();
	    
	    var config = {
		addParameterCb : null,
		columnDefinition : columnDefinition,
		url : url,
		method : "GET",
		showSearchInput : true,
		liveSearch : false
	    };
	    var table = imperium_ui_utils_Datatable.renderTable(tableSelector,
		    config);
	}
    };

    return {
	module : module
    };
}();

jQuery(function($) {
    var module = imperium_ui_view_user_List["module"];
    module.init();
});
