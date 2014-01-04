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

/**
 * jQuery $(handler) execute the function on Load
 */
jQuery(function($) {
	'use strict';

	var applicationDao = {
		getApplications : function(successCb, failureCb) {
			var url = "/cors/application";

			var onSuccessCb = function(data, textStatus, xmlHttpRequest) {
				var validStatus = (textStatus === "success") && (data.success);

				if (validStatus) {
					successCb(data);
				}
			};
			var onFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
			};
			var options = {
				url : url,
				async : true,
				type : "GET",
				success : onSuccessCb,
				failure : onFailureCb,
			};
			$.ajax(options);
		}
	};

	var App = {

		init : function() {
			this.cacheElements();
			this.bindEvents();
			this.prepareForms();
		},
		cacheElements : function() {
			var me = this;
			me.tableNode = $('#tableContainer tbody');
			me.templates = {};
			me.data = {};
		},
		nextPageRequest : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var data = App.data;
			var pagination = data.pagination;

			var nextPage = pagination.nextPage;

			App.renderGrid({
				page : nextPage
			});
		},
		previousPageRequest : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var data = App.data;
			var pagination = data.pagination;

			var beforePage = pagination.beforePage;

			App.renderGrid({
				page : beforePage
			});
		},
		/**
		 * Set the application values in the form
		 */
		onEditAppFormOpened : function(event) {
			var form = $("#myModal");
			var data = form.data("application");

			var id = data["id"];
			var name = data["name"];
			var description = data["description"];

			var nameField = form.find('input[name="name"]');
			var descriptionField = form.find('input[name="description"]');

			nameField.val(name);
			descriptionField.val(description);
		},
		/**
		 * Remove the application information
		 */
		onEditApplFormClosed : function(event) {
			var form = $("#myModal");
			form.data("application", {});
		},
		setDataInEditForm : function(applicationData) {
			var me = this;

			var editForm = $("#myModal");

			editForm.data('application', applicationData);

		},

		onShowEditIndexPage : function(applicationId) {
			var me = this;
			// var url = "/imperium/role/list/{id}";
			var url = "application/show-edit-ui/{id}";
			url = url.replace("{id}", applicationId);

			window.location.replace(url);
		},

		onShowEditForm : function(applicationId) {

			var me = this;

			var successCb = function(serverResponse) {
				var data = serverResponse.data;
				me.setDataInEditForm(data);

				var options = {
					keyboard : true
				};
				$("#myModal").modal(options);
			};

			var failureCb = function(xmlHttpRequest) {
			};

			imperium_ui_dao_Application.getApplication(applicationId,
					successCb, failureCb);
		},

		onSubEditSubmitForm : function(event) {
			var data = event.data;
			var formNode = data["formNode"];
			var input = formNode.find("form").serializeArray();

			var applicationId = formNode.data("application").id;

			var eachName;
			var eachValue;
			var eachInput;
			var index;

			var jsonData = {};

			for (index in input) {
				eachInput = input[index];
				eachName = eachInput["name"];
				eachValue = eachInput["value"];

				jsonData[eachName] = eachValue;
			}
			/**
			 * Fire the event applicationEdited and hide the modal form
			 */
			var onSuccessCb = function(data) {
				if (data.success) {
					$(App).trigger("applicationEdited", data.data);
					$("#myModal").modal("hide");
				}
			};

			var onFailureCb = function(error) {
			};

			imperium_ui_dao_Application.editApplication(applicationId,
					jsonData, onSuccessCb, onFailureCb);
		},

		/**
		 * Submit information to edit
		 */
		onEditSubmitForm : function(event) {

			var isValid = $("#myModal form").validate().form();

			if (isValid) {
				App.onSubEditSubmitForm(event);
			}
		},
		applicationAction : function(event) {
			event.preventDefault();
			event.stopPropagation();

			var target = event.target;
			var applicationId = $(target).attr("applicationId");
			var action = $(target).attr("action");

			if (applicationId !== undefined && action !== undefined) {
				switch (action) {
				case "magicAction":
					App.onShowEditForm(applicationId);
					break;
				case "removeAction":
					break;
				case "bookAction":
					break;
				case "pencilAction":
					App.onShowEditIndexPage(applicationId);
					break;
				}
			}

		},
		bindToPagination : function() {
			var me = this;
			/*
			 * $("#paginationContainer #previous a").on('click',
			 * me.previousPageRequest); $("#paginationContainer #next
			 * a").on('click', me.nextPageRequest);
			 */
			/*
			 * $("#pageContainer li").on("click", function(event) { var
			 * itemClicked = $(event["target"]).parent("li"); var pageRequested =
			 * itemClicked.attr("data-lp");
			 * 
			 * var pageToRequest = pageRequested - 1;
			 * 
			 * App.renderGrid({ page : pageToRequest }); });
			 */
		},
		bindToTable : function() {
			var me = this;

			$("#applicationGridContainer #tableContainer table a").on('click',
					me.applicationAction);
		},
		bindEvents : function() {
			var me = this;

			var form = $("#myModal");

			form.on("hidden", me.onEditApplFormClosed);
			form.on("shown", me.onEditAppFormOpened);

			var data = {
				formNode : form
			};
			$("#myModal #modalSubmit").on("click", data, me.onEditSubmitForm);

			$(App).on("applicationEdited", me.onApplicationEdited);

			$("#addButton").on("click", me.onShowCreateForm);

			var addForm = $("#addFormContainer");
			$("#addFormContainer #modalSubmit").on("click", addForm,
					me.onSubmitAddForm);
		},
		/**
		 * Add validation to the edit application form
		 */
		prepareForms : function() {

			var me = this;

			var rules = {
				name : {
					required : true,
					minlength : 5
				},
				description : {
					required : true,
					minlength : 5
				}
			};

			var validationConfig = {
				rules : rules,
				errorClass : "label label-important"
			};

			$("#myModal form").validate(validationConfig);

			/**
			 * Hook to the on change method of the modal form
			 */
			$('#myModal form input[name="name"]').on("keypress",
					me.onEditApplicationInputChange);

			$('#myModal form input[name="description"]').on("keypress",
					me.onEditApplicationInputChange);
		},

		onSubmitAddForm : function(event) {
			var form = $("#addFormContainer form");
			var formData = form.serializeArray();
			var jsonData = imperium_ui_utils_Ui.convertArrayToMap(formData);
			var onSuccessCb = function(serverResponse) {
			};
			var onFailureCb = function() {
			};
			imperium_ui_dao_Application.createApplication(jsonData,
					onSuccessCb, onFailureCb);
		},

		onShowCreateForm : function(event) {
			var options = {
				keyboard : true
			};

			var url = "/imperium/application/create/{id}";
			// url = url.replace("{id}", );

			var locationObject = window.location;
			// window.location = "/imperium/application/create/{}";
			// window.location="/imperium/application/create";
			$("#addFormContainer").modal(options);
		},
		/**
		 * The input of the edit form modal application changed
		 */
		onEditApplicationInputChange : function(event) {
			var target = $(event.target);
			$("#myModal form").validate().element(target);
		},

		/**
		 * Find the row for the application and set the name and description
		 */
		onApplicationEdited : function(event, data) {
			var id = data.id;
			var name = data.name;
			var description = data.description;

			var tableBody = $("#applicationGridContainer #tableContainer table tbody");

			var rowSelector = 'tr[id="{id}"]';
			rowSelector = rowSelector.replace('{id}', id);
			var row = tableBody.children(rowSelector);

			if (row !== undefined) {
				var nameNode = row.children('td[attribute="name"]');
				var descriptionNode = row
						.children('td[attribute="description"]');

				nameNode.html(name);
				descriptionNode.html(description);
			}
		},
		render : function() {
			var parameters = {
				page : 0
			};

			this.renderGrid(parameters);
			/*
			 * $("#pageContainer").bootpag({ total:10, maxVisible: });
			 */
		},
		updateGridRenderFlag : function() {
			var me = this;
		},
		renderPagination : function(parameters) {
			var me = this;

			var pagination = parameters["pagination"];
			$("#pageContainer").remove();
			$("#paginationContainer2").append('<div id="pageContainer"></div>');
			$("#pageContainer").css("float", "right");
			$("#pageContainer").css("margin-top", "-9px");

			var currentPage = pagination["currentPage"];
			currentPage = (currentPage === 0) ? (currentPage + 1)
					: (currentPage);

			$("#pageContainer").bootpag({
				total : pagination["pageQty"],
				page : currentPage,
				maxVisible : 10,
				leaps : false
			}).on("page", function(event, page) {
				App.renderGrid({
					page : page
				});
			});

			var content = "Total pages :" + pagination["pageQty"];
			$("#totalContainer p").html(content);
		},
		renderGrid : function(parameters) {
			var me = this;

			var pageRequested = null;
			var listAppUrl = null;
			if ("page" in parameters) {
				pageRequested = parameters["page"];
				listAppUrl = 'cors/application?page={page}&withPageIndexChanged=1';
				listAppUrl = listAppUrl.replace('{page}', pageRequested);

			} else {
				listAppUrl = '/cors/application';
				pageRequested = 0;
			}

			/**
			 * Get application list from server
			 */
			var fetchDataCb = function(cb) {
				var url = listAppUrl;
				var onSuccessCb = function(data, textStatus, xmlHttpRequest) {
					var answer = {
						'fromServer' : data
					};

					cb(null, answer);
				};
				var onFailureCb = function(xmlHttpRequest, textStatus, error) {
					cb(xmlHttpRequest, null);
				};
				var options = {
					url : url,
					async : true,
					type : "GET",
					success : onSuccessCb,
					failure : onFailureCb,
				};
				$.ajax(options);
			};
			/**
			 * Fetch the template and generate the html
			 */
			var fetchTemplateCb = function(answer, cb) {
				var applicationList = answer.fromServer.data;

				var successCb = function(templateContent) {
					var gridTemplate = Handlebars.compile(templateContent);
					var html = gridTemplate({
						applications : applicationList
					});

					answer['htmlToInsert'] = html;

					cb(null, answer);
				};

				var options = {
					url : "/imperium/resources/app/templates/applicationGrid.handlebars",
					cache : false,
					success : successCb
				};

				$.ajax(options);
			};
			/**
			 * Calculate pagination
			 */
			var getPaginationCb = function(answer, cb) {
				var fromServer = answer.fromServer;

				var applicationList = fromServer.data;
				var total = fromServer.total;
				var currentPage = pageRequested;
				var pageSize = 25;

				var pagination = imperium_ui_utils_Pagination
						.calculatePagination(currentPage, pageSize, total);

				answer['pagination'] = pagination;

				cb(null, answer);
			};
			/*
			 * var fetchPaginationTemplate = function(answer, cb) {
			 * 
			 * var successCb = function(templateContent) { var pagingTemplate =
			 * Handlebars.compile(templateContent);
			 * 
			 * var templateData = { previousLink : '#', currentPageLink : '#',
			 * currentPage : answer.pagination.currentPage, nextLink : '#' };
			 * 
			 * var html = pagingTemplate(templateData); answer['pagingHtml'] =
			 * html;
			 * 
			 * cb(null, answer); };
			 * 
			 * var options = { url :
			 * "/imperium/resources/app/templates/applicationGrid.pagination.handlebars",
			 * cache : true, success : successCb };
			 * 
			 * $.ajax(options); };
			 */
			var functionArray = [ fetchDataCb, fetchTemplateCb, getPaginationCb /*
																				 * ,
																				 * fetchPaginationTemplate
																				 */];

			var renderPagination = function(answer) {
				$("#paginationContainer ul").html(answer.pagingHtml);
				me.bindToTable();
				me.renderPagination(answer);
			};

			/**
			 * Put the html data in the ui and add it and render the pagination
			 * toolbar to the application
			 */
			var finalCb = function(error, result) {
				me.data.applicationList = result.fromServer.data;
				me.data.pagination = result.pagination;

				$("#tableContainer tbody").html(result.htmlToInsert);
				renderPagination(result);
			};

			async.waterfall(functionArray, finalCb);
		}
	};

	App.init();
	App.render();
});
