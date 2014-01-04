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
var imperium_ui_view_application_EditComponents = function() {
	// 'use strict';
	var module = {
		urlManager : undefined,
		application : undefined,
		data : undefined,
		tables : {
			role : undefined,
			permission : undefined,
			subject : undefined
		},
		init : function() {
			this.urlManager = imperium_ui_utils_UrlManager.module;
			this.setApplication();
			this.render();
			this.bindToTableActions();
			this.setData();
			this.setActiveTab();
			this.fixSubjectTable();
			this.fixRoleTable();
			this.fixPermissionTable();
			this.bindToButtons();
		},
		fixSubjectTable : function() {
			// move bottom to the left
			$("#tabContentSubject").find("#bottom").css("float", "left");
			// remove from the top the button and add it again
			$("#tabContentSubject")
					.find("#top")
					.append(
							'<button id="addSubjectButton" class="btn btn-primary">Add subject</button>');

		},
		fixRoleTable : function() {
			// move pagination to the left
			$("#tabContentRole").find("#bottom").css("float", "left");
			// remove all buttons from the table
			$("#tabContentRole").find("button").remove();
			// create the html for the button
			var template = $("#handlebarTemplate_button").html();
			var templateCb = Handlebars.compile(template);
			var buttonHtml = templateCb({
				id : "addRoleButton",
				text : "Add role"
			});
			// add the button
			$("#tabContentRole").find("#top").append(buttonHtml);

		},
		fixPermissionTable : function() {
			// move pagination to the left
			$("#tabContentPermission").find("#bottom").css("float", "left");
			// remove all the buttons from the table
			$("#tabContentPermission").find("button").remove();
			// create html for the button
			var template = $("#handlebarTemplate_button").html();
			var templateCb = Handlebars.compile(template);
			var buttonHtml = templateCb({
				id : "addPermissionButton",
				text : "Add permission"
			});
			// add the button
			$("#tabContentPermission").find("#top").append(buttonHtml);
		},
		setData : function() {
			var information = $("#information");
			this.data = {
				from : information.attr("data-from")
			};
		},
		setActiveTab : function() {
			var from = this.data.from;

			var div;
			if (from === 'SUBJECT') {
				div = $('#tabPanelIndex').find('li[id="itemSubject"]');
			} else if (from === 'ROLE') {
				div = $('#tabPanelIndex').find('li[id="itemRole"]');
			} else {
				div = $('#tabPanelIndex').find('li[id="itemPermission"]');
			}
			div.find("a").tab("show");
		},
		/**
		 * After the role table has been render fire this function
		 */
		onRoleTableDraw : function(oSettings) {
		},
		bindToButtons : function() {
			$("#addRoleButton").on("click", this.onCreateRole);
			$("#addPermissionButton").on("click", this.onCreatePermission);
			$("#addSubjectButton").on("click", this.onCreateSubject);

		},
		bindToTableActions : function() {
			module.bindToRoleActions();
			module.bindToPermissionActions();
			module.bindToSubjectActions();
		},
		onRoleActionClicked : function(event) {
			var target = event.target;
			var className = target.className;
			if (className === 'btn') {
				// on button clicked
				this.onRoleActionButtonClicked(target);
			} else {
				// i element clicked
				this.onRoleImageActionClicked(target);
			}
		},
		onRoleActionButtonClicked : function(target) {
			var imageElement = $(target).find('i');
			this.onRoleImageActionClicked(imageElement);
		},
		onRoleImageActionClicked : function(target) {
			var action = $(target).attr('data-action');
			var id = $(target).attr('data-id');

			var application = this.application;

			if (action === "magicAction") {
				this.onSubRoleEdition(application, id);
			} else if (action === "editAction") {
				this.onSubGoToEditRolePermission(application, id)
			} else if (action === "removeAction") {
				this.onSubRoleRemoval(application, id);
			}
		},
		bindToRoleActions : function() {
			var me = this;
			// edit, edit subjecets and remove
			$("#tabPanelContainer #tabContentRole table").on("click",
					'tr a[class="btn"]', function(event) {
						me.onRoleActionClicked(event);
					});
		},
		onPermissionActionButtonClicked : function(element) {
			var imageElement = $(element).find('i');
			this.onPermissionImageActionClicked(imageElement);
		},
		onPermissionImageActionClicked : function(element) {
			var application = this.application;
			var action = $(element).attr('data-action');
			var id = $(element).attr('data-id');

			if (action === "magicAction") {
				this.onSubPermissionEdition(application, id);
			} else if (action === "removeAction") {
				this.onSubPermissionRemoval(application, id);
			}
		},
		onPermissionActionClicked : function(event) {
			var target = event.target;
			var className = target.className;
			if (className === 'btn') {
				// on button clicked
				this.onPermissionActionButtonClicked(target);
			} else {
				// i element clicked
				this.onPermissionImageActionClicked(target);
			}
		},
		bindToPermissionActions : function() {
			var me = this;
			$("#tabPanelContainer #tabContentPermission table").on("click",
					'tr a[class="btn"]', function(event) {
						me.onPermissionActionClicked(event);
					});
		},
		onSubjectActionLinkClicked: function(element) {
			var imageElement = $(element).find('i');
			this.onSubjectImageClicked(imageElement);
		},
		onSubjectImageClicked: function(element) {
			var action = $(element).attr('data-action');
			var id = $(element).attr('data-id');
			
			var application = this.application;
			if (action==='magicAction') {
				this.doSubjectEdition(application, id);
			} else if (action==='editAction') {
				this.onShowEditRolesOfSubject(application, id);
			} else if (action==='removeAction') {
				this.doSubjectRemoval(application, id)
			}
		},
		onSubjectActionClicked: function(event) {
			var target = event.target;
			var className = target.className;
			if (className==='btn') {
				this.onSubjectActionLinkClicked(target);
			} else {
				this.onSubjectImageClicked(target);
			}
		},
		bindToSubjectActions : function() {
			var me = this;
			$("#tabPanelContainer #tabContentSubject table").on("click",
					 'tr a[class="btn"]', function(event) {
				me.onSubjectActionClicked(event);
			});
		},
		setApplication : function() {
			var informationNode = $("#information");
			var applicationId = informationNode.attr("data-application-id");
			var name = informationNode.attr("data-name");
			var description = informationNode.attr("data-description");

			module.application = {
				id : applicationId,
				name : name,
				description : description
			};
		},
		doRoleRemoval : function(roleId) {
		},
		onSubRoleEdition : function(application, id) {
			var url = module.urlManager.getUrlForRoleEdit(id,
					module.application.id);
			window.location = url;
		},
		onSubGoToEditRolePermission : function(application, id) {
			var url = this.urlManager.getUrlForPermissionOfRoleEdition(id,
					application.id);
			window.location = url;
		},
		performRoleRemoval : function(applicationId, roleId) {
			var successCb = function(serverResponse) {
				var data = [ roleId.toString() ];
				module.onRoleRemoved(data);
			};
			var failureCb = function() {
			};
			var data = {
				applicationId : applicationId.toString(),
				roles : [ roleId.toString() ]
			};
			imperium_ui_dao_Role.removeFromApplication(data, successCb,
					failureCb);

		},
		onCreateSubject : function(event) {
			var applicationId = module.application.id;
			var url = module.urlManager.getUrlToShowSubjectForm();
			url = url + "?applicationId=" + applicationId;
			window.location = url;
		},
		onCreatePermission : function(event) {
			var url = module.urlManager
					.getUrlToShowPermissionForm(module.application.id);

			window.location = url;
		},
		onRoleRemoved : function(data) {
			/**
			 * Reload the roles from application table
			 */
			var roleTableSelector = "#tabContentRole table";
			var table = $(roleTableSelector).dataTable();
			table.fnDraw();
		},
		onRoleCannotBeRemoved : function(roleData) {
			var message = "The role {information} is being used";
			message = message + " by another subject ";
			message = message.replace("{information}", roleData.name);
			bootbox.alert(message);

		},
		onSubRoleRemoval : function(application, id) {
			var successCb = function(serverResponse) {
				var subjectQtyMap = serverResponse.subjectQtyMap;
				var idString = id.toString();

				var existData = (idString in subjectQtyMap);
				var afterCb = function(answer) {
					if (answer === true) {
						module.performRoleRemoval(application.id.toString(),
								idString);
					}
				};

				if (existData === true) {
					var message = "Are you sure you want to remove the ";
					message = message + " selected role from the application ";
					message = message + " and subjects where is being used ";
					bootbox.confirm(message, afterCb);
				}

			};
			var failureCb = function(error) {
			};
			var roleList = [ id ];
			var data = {
				roleList : roleList
			};
			imperium_ui_dao_Role.getSubjectQty(data, successCb, failureCb);

		},
		doSubjectEdition : function(application, id) {
			window.location = this.urlManager.getUrlForSubjectEdition(id,
					application.id);
		},
		doSubjectRemoval : function(application, id) {
			var data = {
				application : application,
				subject : {
					id : id
				}
			};

			var aSuccessCb = function(response) {
				var subjectTableSelector = "#tabContentSubject table ";
				var table = $(subjectTableSelector).dataTable();
				table.fnDraw();
			};

			var aFailureCb = function(error) {
			};

			var afterCb = function(response) {
				if (response === true) {
					imperium_ui_dao_Subject
							.remove(data, aSuccessCb, aFailureCb);
				}
			};
			var message = "Are you sure you want to remove the selected ";
			message = message + " subject from the application and the roles";
			message = message + " being used ? ";
			bootbox.confirm(message, afterCb);

		},
		onShowEditRolesOfSubject : function(application, id) {
			var url = this.urlManager.getUrlForRoleOfSubjectEdition(id,
					application.id);

			window.location = url;
		},
		onEditPermissions : function(event) {
			var application = {
				id : module.application.id,
				name : module.application.name,
				description : module.application.description
			};

			var target = $(event.target);
			var id = target.attr("data-id");

			module.onSubGoToEditRolePermission(application, id);
		},
		onSubPermissionEdition : function(application, id) {
			var url = this.urlManager.getUrlForPermissionEdit(id);
			url = url + "?applicationId={applicationId}";
			url = url.replace("{applicationId}", application.id);

			window.location = url;
		},
		onSubPermissionRemoval : function(application, id) {
			var successCb = function(serverResponse) {
				var table = $("#permissionTable").dataTable();
				table.fnDraw();
			};
			var failureCb = function(failure) {
			};

			var data = {
				applicationId : application.id,
				permissions : [ id.toString() ]
			};
			var afterCb = function(result) {
				if (result === true) {
					imperium_ui_dao_Permission.removeFromApp(data, successCb,
							failureCb);
				}
			};

			var message = "Are you sure you want to remove the ";
			message = message + "permission from the roles where ";
			message = message + "and the application where is being used?";
			bootbox.confirm(message, afterCb);

		},
		onCreateRole : function(event) {
			window.location = module.urlManager
					.getUrlForShowCreateRole(module.application.id);
		},
		/* Render functions */
		render : function() {
			this.renderRoleTable();
			this.renderSubjectTable();
			this.renderPermissionTable();
		},
		renderRoleTable : function() {
			var roleTableSelector = ("#tabContentRole #roleTable");
			/**
			 * null or a cb to add parameters before each request
			 */
			var addParameterCb = null;

			var columnDefinition = [
					{
						"mData" : "name",
						"bSortable" : true,
						"bVisible" : true
					},
					{
						"mData" : "description",
						"bSortable" : true,
						"bVisible" : true
					},
					{
						"mData" : "description",
						"bSortable" : false,
						"sWidth" : 210,
						"mRender" : function(data, type, row) {
							var itemId = row["id"];
							var data = {
								id : itemId,
								type : "role",
								entityId : "grid_action_menu_role_id_" + itemId,
								magicAction : true,
								removeAction : true,
								bookAction : false,
								pencilAction : false,
								editAction : true
							};
							var html = imperium_ui_utils_Ui
									.generateContentFromTemplate(
											"#handlebarTemplate_grid_actions",
											data);
							return html;
						}
					} ];

			var url = this.urlManager
					.getUrlForRoleJsonList(this.application.id);

			var method = "GET";
			var showSearchInput = true;

			/*
			 * Callback when the table has been drawn
			 */
			var onDrawCallback = function(oSettings) {
				module.onRoleTableDraw(oSettings);
			};

			var config = {
				addParameterCb : addParameterCb,
				columnDefinition : columnDefinition,
				url : url,
				method : method,
				showSearchInput : showSearchInput,
				liveSearch : false,
				onDrawCallback : onDrawCallback
			};

			var roleTable = imperium_ui_utils_Datatable.renderTable(
					roleTableSelector, config);
			module.tables.role = roleTable;

		},
		renderSubjectTable : function() {
			var subjectTableSelector = ("#tabContentSubject #subjectTable");
			/**
			 * null or a cb to add parameters before each request
			 */
			var addParameterCb = null;

			var columnDefinition = [
					{
						"mData" : "name",
						"bSortable" : true,
						"bVisible" : true
					},
					{
						"mData" : "application",
						"bSortable" : false,
						"sWidth" : 210,
						"mRender" : function(data, type, row) {
							var itemId = row["id"];
							var data = {
								id : itemId,
								type : "subject",
								entityId : "grid_action_menu_subject_id_"
										+ itemId,
								magicAction : true,
								removeAction : true,
								bookAction : false,
								pencilAction : false,
								editAction : true
							};
							var html = imperium_ui_utils_Ui
									.generateContentFromTemplate(
											"#handlebarTemplate_grid_actions",
											data);
							return html;
						}
					} ];
			var url = this.urlManager
					.getUrlForSubjectJsonList(module.application.id);

			var method = "GET";
			var showSearchInput = true;

			var config = {
				addParameterCb : addParameterCb,
				columnDefinition : columnDefinition,
				url : url,
				method : method,
				showSearchInput : showSearchInput,
				liveSearch : false
			};

			var subjectTable = imperium_ui_utils_Datatable.renderTable(
					subjectTableSelector, config);
			module.tables.subject = subjectTable;
		},
		renderPermissionTable : function() {
			var subjectTableSelector = ("#tabContentPermission #permissionTable");
			/**
			 * null or a cb to add parameters before each request
			 */
			var addParameterCb = null;

			var columnDefinition = [
					{
						"mData" : "resource",
						"bSortable" : true,
						"bVisible" : true
					},
					{
						"mData" : "action",
						"bSortable" : true,
						"bVisible" : true
					},
					{
						"mData" : "description",
						"bSortable" : false,
						"bVisible" : true
					},
					{
						"mData" : "application",
						"bSortable" : false,
						"sWidth" : 210,
						"mRender" : function(data, type, row) {
							var itemId = row["id"];
							var data = {
								id : itemId,
								type : "subject",
								entityId : "grid_action_menu_subject_id_"
										+ itemId,
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
			var url = this.urlManager
					.getUrlForPermissionJsonList(module.application.id);

			var method = "GET";
			var showSearchInput = true;

			var config = {
				addParameterCb : addParameterCb,
				columnDefinition : columnDefinition,
				url : url,
				method : method,
				showSearchInput : showSearchInput,
				liveSearch : false
			};

			var permissionTable = imperium_ui_utils_Datatable.renderTable(
					subjectTableSelector, config);
			module.tables.permission = permissionTable;
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_application_EditComponents["module"];
	module.init();
});
