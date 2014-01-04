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
var globalVariables = {};

var imperium_ui_view_role_List = function() {
	"use strict";

	var module = {

		data : {},

		init : function() {
			this.bindEvents();
			this.prepareForms();
			this.prepareModals();
			this.initData();
		},

		initData : function() {
			var me = this;
			var moduleData = me["data"];
			moduleData["applicationId"] = $("#roleGridContainer").data(
					"application");
		},

		bindEvents : function() {
			var me = this;
			var form = $("#myModal");

			form.on("hidden", function(jQueryEvent) {
				me.clearEditForm({
					event : jQueryEvent,
					formNode : jQueryEvent.data
				});
			});

			form.on("shown", function(jQueryEvent) {
				me.setDataInEditForm({
					event : jQueryEvent,
					formNode : jQueryEvent.target
				});
			});

			var data = {
				formNode : $("#myModal")
			};
			$("#myModal #modalSubmit").on("click", data, me.onEditSubmitForm);
		
			$(module).on("roleEdited", me.onRoleEdited);
		},

		bindToPagination : function() {
			var me = this;

			$("#paginationContainer #previous a").on('click',
					me.previousPageRequest);
			$("#paginationContainer #next a").on('click', me.nextPageRequest);

		},

		bindToTable : function() {
			var me = this;

			var templateSelector = "#roleGridContainer #tableContainer "
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
		onEditSubmitForm : function(event) {
			var isValidForm = $("#myModal form").validate().form();
			if (isValidForm === true) {
				module.onSubEditSubmitForm(event);
			}
		},
		onSubEditSubmitForm: function(event) {
			var me = this;

			var data = event["data"];
			var formNode = data["formNode"];
			var form = $(formNode).find("form");
			var formData = form.serializeArray();

			var moduleData = me["data"];
			var applicationId = $(form).attr("data-applicationid");
			var roleId = $(form).attr("data-roleid");

			var jsonData = imperium_ui_utils_Ui.convertArrayToMap(formData);
			jsonData["applicationId"] = applicationId;
			jsonData["roleId"] = roleId;

			var onSuccessCb = function(serverResponse) {
				var data = serverResponse["data"];
				var success = serverResponse["success"];
				if (success === true) {
					$(module).trigger("roleEdited",
							serverResponse["data"]);
					$("#myModal").modal("hide");
				}
			};

			var failureCb = function(error) {
			};

			imperium_ui_dao_Role.editRole(roleId, jsonData,
					onSuccessCb, failureCb);
		},
		onEditAction : function(event) {
			var roleId = $(event.target).data("id");
			module.showEditForm(event, roleId);
		},
		onRemoveAction : function(event) {
		},
		onBookAction : function(event) {
		},
		onSpecialEditionAction : function(event) {
		},
		onRoleEdited: function(event, data) {
			var id = data["id"];
			var name = data["name"];
			var description = data["description"];
			
			var tableSelector = "#roleGridContainer #tableContainer";
			tableSelector = tableSelector + " table tbody ";
			var tableBody = $(tableSelector);
			
			
			var rowSelector = 'tr[data-id="{id}"]';
			rowSelector = rowSelector.replace("{id}", id);
			
			var row = $(tableBody).find(rowSelector);
			
			if (row!==undefined) {
				var nameNode = row.find('td[data-attribute="name"]');
				var descriptionNode = row.find('td[data-attribute="description"]');
				nameNode.html(name);
				descriptionNode.html(description);
			}
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

			var nameNode = $(formNode).find("form input[name='name']");
			var descriptionNode = $(formNode).find(
					"form input[name='description']");

			var roleToEdit = moduleData["roleToEdit"];

			nameNode.val(roleToEdit["name"]);
			descriptionNode.val(roleToEdit["description"]);

			$(formNode).find("form").attr("data-applicationId", applicationId);
			$(formNode).find("form").attr("data-roleid", roleToEdit["id"]);
		},
		showEditForm : function(event, roleId) {
			var me = this;
			var moduleData = me["data"];

			var applicationId = this.data["applicationId"];

			var successCb = function(serverResponse) {
				var data = serverResponse["data"];
				moduleData["roleToEdit"] = data;

				var options = {
					keyboard : true
				};

				$("#myModal").modal(options);
			};
			var failureCb = function(error) {
			};

			imperium_ui_dao_Role.getRole(roleId, successCb, failureCb);
		},
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

			module.renderGrid({
				page : nextPage
			});
		},

		prepareModals : function() {

		},

		prepareForms : function() {
			var rules = {
				name : {
					required : true,
					minlength : 5
				},
				description : {
					required : true,
					minlength : 3
				}
			};

			var validationConfig = {
				rules : rules,
				errorClass : "label label-important"
			};

			$("#myModal form").validate(validationConfig);

			$('#myModal form input[name="name"]').on("keypress",
					module.onEditRoleInputChange);

			$('#myModal form input[name="description"]').on("keypress",
					module.onEditRoleInputChange);
		},

		onEditRoleInputChange : function(jqueryEvent) {
			var target = $(event.target);
			$("#myModal form").validate().element(target);
		},

		renderGrid : function(parameters) {
			var me = this;
			var moduleData = me["data"];

			var applicationId = moduleData["applicationId"];

			var url = null;
			if ("page" in parameters) {
				var page = parameters["page"];
				url = "/imperium/role/ajax/list/{app}?page={page}";
				url = url.replace("{app}", applicationId);
				url = url.replace("{page}", page);
			} else {
				url = "/imperium/roles/ajax/list/{app}";
			}

			var fetchDataCb = function(cb) {
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
				var roleList = answer.fromServer.data;

				var successCb = function(templateContent) {
					var gridTemplate = Handlebars.compile(templateContent);
					var html = gridTemplate({
						roles : roleList
					});
					answer["htmlToInsert"] = html;

					cb(null, answer);
				};

				var options = {
					url : "/imperium/resources/app/templates/roleList.handlebars",
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
			/*
			 * var functionArray = [ fetchDataCb, fetchTemplateCb,
			 * getPaginationCb, fetchPaginationTemplate ];
			 */

			var renderPagination = function(answer) {
				$("#paginationContainer ul").html(answer.pagingHtml);
				me.bindToPagination();
				me.bindToTable();
			};

			var finalCb = function(error, result) {
				moduleData["roleList"] = result.fromServer.data;
				moduleData["pagination"] = result.pagination;

				$("#tableContainer tbody").html(result.htmlToInsert);
				renderPagination(result);
			};

			async.waterfall(functionArray, finalCb);
		},

		render : function() {
			var parameters = {
				page : 0
			};

			this.renderGrid(parameters);
		}

	};

	return {
		module : module
	};
}();
