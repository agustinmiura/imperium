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
var imperium_ui_view_permission_List = function() {
	"use strict";

	var module = {

		data : {},

		init : function() {
			this.bindEvents();
			this.prepareForms();
			this.prepareModals();
			this.initData();

			/**
			 * @todo remove
			 */
			globalVariables.module = this;
		},
		prepareModals : function() {
			var me = this;

			var moduleData = me["data"];

			var confirmDialogSelector = null;
			confirmDialogSelector = '#modalConfirmPermission button[data-option="true"]';
			$(confirmDialogSelector).on("click",
					moduleData["permissionToRemove"], me.doPermissionRemoval);

			confirmDialogSelector = '#modalConfirmPermission button[data-option="false"]';
			$(confirmDialogSelector).on("click", me.onCancelPermissionRemoval);

		},
		prepareForms : function() {
			var rules = {
				action : {
					required : true,
					minlength : 5
				},
				resource : {
					required : true,
					minlength : 3
				}

			};
			var validationConfig = {
				rules : rules,
				errorClass : "label label-important"
			};

			$("#myModal form").validate(validationConfig);

			$('#myModal form input[name="action"]').on("keypress",
					module.onEditPermissionInputChange);

			$('#myModal form input[name="resource"]').on("keypress",
					module.onEditPermissionInputChange);
		},

		initData : function() {
			this.data["applicationId"] = $("#permissionGridContainer").data(
					"application");

		},
		doPermissionRemoval : function(event) {
			var moduleData = module["data"];
			var removalData = moduleData["permissionToRemove"];

			var applicationId = moduleData["applicationId"];
			var id = removalData["id"];
			var table = removalData["table"];
			var row = removalData["row"];

			var permissions = [id];
			var toSend = imperium_ui_utils_Ui.mapElementsToString(permissions);

			var data = {
				applicationId : applicationId.toString(),
				permissions : toSend,
			};

			var aSuccessCb = function(serverResponse) {
				if (serverResponse["success"] === true) {
					$(table).find(row).remove();
					module.onCancelPermissionRemoval();
				}
			};

			var aFailureCb = function(xmlHttpRequest) {
			};

			imperium_ui_dao_Application.removePermissionsFromApp(data,
					aSuccessCb, aFailureCb);
		},
		onCancelPermissionRemoval : function(arg0, arg1, arg2) {
			$("#modalConfirmPermission").modal("hide");
		},
		onEditPermissionInputChange : function(event) {
			var target = $(event.target);
			$("#myModal form").validate().element(target);
		},
		/**
		 * Submit information to edit
		 */
		onEditSubmitForm : function(event) {
			var isValidForm = $("#myModal form").validate().form();
			if (isValidForm === true) {
				module.onSubEditSubmitForm(event);
			}
		},
		onSubEditSubmitForm : function(event) {
			var me = this;

			var data = event["data"];
			var formNode = data["formNode"];
			var form = $(formNode).find("form");
			var formData = form.serializeArray();

			var moduleData = me["data"];
			var applicationId = $(form).attr("data-applicationid");
			var permissionId = $(form).attr("data-permissionid");

			var jsonData = imperium_ui_utils_Ui.convertArrayToMap(formData);
			jsonData["applicationId"] = applicationId;
			jsonData["permissionId"] = permissionId;

			var permissionId = $(form).attr("data-permissionid");

			var onSuccessCb = function(serverResponse) {
				var data = serverResponse["data"];
				var success = serverResponse["success"];
				if (success === true) {
					$(module).trigger("permissionEdited",
							serverResponse["data"]);

					$("#myModal").modal("hide");
				}
			};

			var failureCb = function(error) {
			};

			imperium_ui_dao_Permission.editPermission(permissionId, jsonData,
					onSuccessCb, failureCb);
		},
		clearEditForm : function(data) {
			// remove error messages
			$("#myModal form").validate().resetForm();
			// remove internal data
			$("#myModal form").attr("data-applicationid", "");
			$("#myModal form").attr("data-permissionid", "");

		},
		setDataInEditForm : function(data) {
			var me = module;

			var moduleData = me["data"];
			var applicationId = moduleData["applicationId"];

			var formNode = data["formNode"];

			var resourceField = $(formNode).find("form input[name='resource']");
			var actionField = $(formNode).find("form input[name='action']");
			var permissionToEdit = me.data["permissionToEdit"];

			resourceField.val(permissionToEdit["resource"]);
			actionField.val(permissionToEdit["action"]);
			$(formNode).find("form").attr("data-applicationId", applicationId);
			$(formNode).find("form").attr("data-permissionid",
					permissionToEdit["id"]);

		},
		removePermission : function(event, permissionId) {
			var me = this;

			var target = event["target"];
			var rowToRemove = $(target).closest("tr");
			var table = $(rowToRemove).closest("table");

			var moduleData = me["data"];
			moduleData["permissionToRemove"] = {
				id : permissionId,
				row : rowToRemove,
				table : table
			};
			// change the ui here
			// var target = event["target"];
			// var rowToRemove = $(target).closest("tr");
			// var table= $(rowToRemove).closest("table");
			// $(table).find(rowToRemove).remove();
			//			
			var options = {
				keyboard : true
			};
			$("#modalConfirmPermission").modal(options);
		},
		showEditForm : function(event, permissionId) {

			var me = this;
			var moduleData = me.data;

			var applicationId = this.data["applicationId"];

			var successCb = function(serverResponse) {
				var data = serverResponse.data;
				data["applicationId"] = applicationId;

				moduleData["permissionToEdit"] = data;

				var options = {
					keyboard : true
				};
				$("#myModal").modal(options);
			};

			var failureCb = function(requestObject) {
			};

			imperium_ui_dao_Permission.getPermission(permissionId, successCb,
					failureCb);
		},
		/**
		 * Events
		 */
		previousPageRequest : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var data = module.data;
			var pagination = data.pagination;

			var beforePage = pagination.beforePage;

			module.renderGrid({
				page : beforePage
			});
		},
		nextPageRequest : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var data = module.data;
			var pagination = data.pagination;

			var nextPage = pagination.nextPage;

			App.renderGrid({
				page : nextPage
			});
		},
		onEditAction : function(event) {
			var permissionId = $(event.target).data("id");
			module.showEditForm(event, permissionId);
		},
		onRemoveAction : function(event) {
			var permissionId = $(event.target).data("id");
			module.removePermission(event, permissionId);
		},
		onBookAction : function(event) {

		},
		onSpecialEditionAction : function(event) {
		},
		/**
		 * Bind events
		 */
		createDataForEditForm : function(jQueryEvent) {
			var answer = {
				event : jQueryEvent,
				formNode : jQueryEvent.target
			};

			return answer;
		},
		bindEvents : function() {
			var me = this;
			var form = $("#myModal");

			var extractFormNodeCb = function(jQueryEvent) {
				return (jQueryEvent.event);
			};
			/*
			 * Listeners on dom
			 */
			form.on("hidden", function(jQueryEvent) {
				var data = me.createDataForEditForm(jQueryEvent);
				me.clearEditForm(data);
				// $(module).trigger("editFormClosed", data);
			});
			form.on("shown", function(jQueryEvent) {
				var data = me.createDataForEditForm(jQueryEvent);
				me.setDataInEditForm(data);
			});

			var form = $("#myModal");
			var data = {
				formNode : form
			};
			$("#myModal #modalSubmit").on("click", data, me.onEditSubmitForm);

			$(module).on("permissionEdited", me.onPermissionEdited);
		},
		bindToPagination : function() {
			var me = this;

			$("#paginationContainer #previous a").on('click',
					me.previousPageRequest);
			$("#paginationContainer #next a").on('click', me.nextPageRequest);

		},
		bindToTable : function() {
			var me = this;

			var templateSelector = "#permissionGridContainer #tableContainer "
					+ " table a [data-action='{action}'] ";

			var editSelector = templateSelector.replace("{action}",
					"magicAction");

			var removeSelector = templateSelector.replace("{action}",
					"removeAction");

			var bookSelector = templateSelector.replace("{action}",
					"bookAction");

			var pencilSelector = templateSelector.replace("{action}",
					"pencilAction");

			$(editSelector).on("click", me.onEditAction);
			$(removeSelector).on("click", me.onRemoveAction);
			$(bookSelector).on("click", me.onBookAction);
			$(pencilSelector).on("click", me.onSpecialEditionAction);
		},
		onPermissionEdited : function(event, data) {
			var id = data["id"];
			var resource = data["resource"];
			var action = data["action"];
			var description = data["description"];

			var tableSelector = "#permissionGridContainer #tableContainer";
			tableSelector = tableSelector + " table tbody ";
			var tableBody = $(tableSelector);

			var rowSelector = 'tr[data-id="{id}"]';
			rowSelector = rowSelector.replace("{id}", id);

			var row = $(tableBody).find(rowSelector);
			if (row !== undefined) {
				var resourceNode = row
						.children('td[data-attribute="resource"]');
				var actionNode = row.children('td[data-attribute="action"]');

				var descriptionNode = row
						.children('td[data-attribute="description"]');

				$(resourceNode).html(resource);
				$(actionNode).html(action);
				$(descriptionNode).html(description);
			}
		},
		/**
		 * Render
		 */
		render : function() {
			var parameters = {
				page : 0
			};
			this.renderGrid(parameters);
		},
		renderGrid : function(parameters) {
			var me = this;

			var applicationId = $("#permissionGridContainer").data(
					"application");
			var listPermissionUrl = null;
			if ("page" in parameters) {
				var page = parameters["page"];
				listPermissionUrl = "/imperium/permission/ajax/list/{app}?page={page}";
				listPermissionUrl = listPermissionUrl.replace("{app}",
						applicationId);
				listPermissionUrl = listPermissionUrl.replace("{page}", page);
			} else {
				listPermissionUrl = "/ajax/permission/list/{app}";
			}

			var fetchDataCb = function(cb) {
				var url = listPermissionUrl;
				var onSuccessCb = function(data, textStatus, xmlHttpRequest) {
					var answer = {
						"fromServer" : data
					};
					cb(null, answer);
				};
				var onFailureCb = function(xmlHttpRequest, textStatus, error) {
					cb(xmlHttpRequest, null);
				};

				var options = {
					url : url,
					type : "GET",
					success : onSuccessCb,
					failure : onFailureCb
				};

				$.ajax(options);
			};

			var fetchTemplateCb = function(answer, cb) {
				var permissionList = answer.fromServer.data;

				var successCb = function(templateContent) {
					var gridTemplate = Handlebars.compile(templateContent);
					var html = gridTemplate({
						permissions : permissionList
					});
					answer["htmlToInsert"] = html;
					cb(null, answer);
				};

				var options = {
					url : "/imperium/resources/app/templates/permissionList.handlebars",
					cache : false,
					success : successCb
				};

				$.ajax(options);
			};

			var getPaginationCb = function(answer, cb) {
				var fromServer = answer["fromServer"];

				var permissionList = fromServer.data;
				var total = fromServer.total;
				var currentPage = fromServer.page;
				var pageSize = permissionList.length;

				var pagination = imperium_ui_utils_Pagination
						.calculatePagination(currentPage, pageSize, total);

				answer['pagination'] = pagination;

				cb(null, answer);
			};

			var fetchPaginationTemplate = function(answer, cb) {

				var successCb = function(templateContent) {
					var pagingTemplate = Handlebars.compile(templateContent);

					var templateData = {
						previousLink : '#',
						currentPageLink : '#',
						currentPage : answer.pagination.currentPage,
						nextLink : '#'
					};

					var html = pagingTemplate(templateData);
					answer['pagingHtml'] = html;

					cb(null, answer);
				};

				var options = {
					url : "/imperium/resources/app/templates/applicationGrid.pagination.handlebars",
					cache : true,
					success : successCb
				};

				$.ajax(options);
			};

			var functionArray = [ fetchDataCb, fetchTemplateCb,
					getPaginationCb, fetchPaginationTemplate ];

			var renderPagination = function(answer) {
				$("#paginationContainer ul").html(answer.pagingHtml);
				me.bindToPagination();
				me.bindToTable();
			};

			var finalCb = function(error, result) {
				me.data.permissionList = result.fromServer.data;
				me.data.pagination = result.pagination;

				$("#tableContainer tbody").html(result.htmlToInsert);

				renderPagination(result);
			};

			async.waterfall(functionArray, finalCb);
		}
	};

	return {
		module : module
	};
}();
