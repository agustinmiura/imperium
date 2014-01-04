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
var imperium_ui_view_role_EditRoles = function() {
	var urlManager = imperium_ui_utils_UrlManager.module;
	
	var module = {
		data : undefined,
		init : function() {
			this.setData();
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
		},
		render : function() {

			var buttonConfig = this.createButtonConfig();
			var availableConfig = this.createConfigForAvailableRolesTable();
			var currentRolesConfig = this.createConfigRolesInSubjectTable();

			this.setupSelectionWidget(availableConfig, currentRolesConfig,
					buttonConfig);
		},
		setupSelectionWidget : function(availableConfig, currentRolesConfig,
				buttonConfig) {
			var config = {
				toSearchConfig : availableConfig,
				selectionConfig : currentRolesConfig
			};

			config["beforeAddCb"] = buttonConfig["beforeAddCb"];
			config["prepareDataToSendCb"] = buttonConfig["prepareDataToSendCb"];
			config["onSuccessBeforeAddCb"] = buttonConfig["onSuccessBeforeAddCb"];
			config["onFailureBeforeAddCb"] = buttonConfig["onFailureBeforeAddCb"];

			config["beforeRemoveCb"] = buttonConfig["beforeRemoveCb"];
			config["prepareDataToRemoveCb"] = buttonConfig["prepareDataToRemoveCb"];
			config["onSuccessBeforeRemoveCb"] = buttonConfig["onSuccessBeforeRemoveCb"];
			config["onFailureBeforeRemoveCb"] = buttonConfig["onFailureBeforeRemoveCb"];

			var selector = "#assignSubjectToRoleContainer #widgetContainer";

			imperium_ui_widget_ChooserWithoutPagination.init();
			var selectorWidget = imperium_ui_widget_ChooserWithoutPagination
					.render(selector, config);

			module.selectorWidget = selectorWidget;

		},
		createColumnDefinitionForCurrentSelection : function() {
			return [ {
				"mData" : "name",
				"sWidth" : "25%"
			}, {
				"mData" : "description",
				"sWidth" : "25%"
			} ];
		},
		createCbForCurrentSelection : function() {
			var cb = function(aoData) {
				aoData.push({
					"name" : "applicationId",
					"value" : module.data.application.id.toString()
				});
				aoData.push({
					"name" : "subjectId",
					"value" : module.data.subject.id.toString()
				});
			};
			return cb;
		},
		createConfigRolesInSubjectTable : function() {
			var columnDefinition = this
					.createColumnDefinitionForCurrentSelection();
			var addParameterCb = module.createCbForCurrentSelection();

			var url = urlManager.getUrlForRoleJsonListForSubject();
			return {
				url : url,
				method : "GET",
				columnDefinition : columnDefinition,
				addParameterCb : addParameterCb
			};

		},
		createColumnDefinitionForAvailable : function() {
			return [ {
				"mData" : "name",
				"bSortable" : true,
				"bVisible" : true,
				"sWidth" : "25%",
			}, {
				"mData" : "description",
				"bSortable" : true,
				"bVisible" : true,
				"sWidth" : "25%"
			} ];
		},
		createAddParameterCbForAvailable : function() {
			var cb = function(aoData) {
				/**
				 * @todo here push the application id and subject id
				 */
				aoData.push({
					"name" : "applicationId",
					"value" : module.data.application.id.toString()
				});
				aoData.push({
					"name" : "subjectId",
					"value" : module.data.subject.id.toString()
				});
			};
			return cb;
		},
		createConfigForAvailableRolesTable : function() {
			var columnDefinition = this.createColumnDefinitionForAvailable();
			var addParameterCb = this.createAddParameterCbForAvailable();

			var url = urlManager.getUrlForRoleAvailableJsonListForSubject();
			var config = {
				url : url,
				method : "GET",
				columnDefinition : columnDefinition,
				addParameterCb : addParameterCb
			};

			return config;
		},
		createButtonConfig : function() {
			var answer = {

			};

			answer["beforeAddCb"] = function(data, onSuccessCb, onFailureCb) {

				imperium_ui_dao_Subject.updateRoles(data, onSuccessCb,
						onFailureCb);
			};

			answer["prepareDataToSendCb"] = function(selection) {
				var add = [];
				var processCb = function(index, value) {
					add.push(value["id"]);
				};
				jQuery.each(selection, processCb);

				return {
					"subjectId" : module.data.subject.id.toString(),
					"add" : add,
					"remove" : []
				};
			};

			answer["onFailureBeforeAddCb"] = function(error) {
			};

			answer["onSuccessBeforeAddCb"] = function(data) {
				if (data["success"] === true) {
					var add = data["add"];
					var subjectId = data["subjectId"];

					var availableSelector = "#widgetContainer #showAvailableContainer #showAvailableTable";
					var selectionSelector = "#widgetContainer #userSelectionContainer #userSelection";

					// imperium_ui_widget_ChooserWithoutPagination
					// .addToSelectionIds(selectionSelector, add);
					imperium_ui_widget_ChooserWithoutPagination
							.reloadTable(availableSelector);

					imperium_ui_widget_ChooserWithoutPagination
							.reloadTable(selectionSelector);
				}
			};

			answer["beforeRemoveCb"] = function(data, onSuccessCb, onFailureCb) {
				imperium_ui_dao_Subject.updateRoles(data, onSuccessCb,
						onFailureCb);
			};

			answer["prepareDataToRemoveCb"] = function(selection) {
				var remove = [];
				var processCb = function(index, value) {
					remove.push(value["id"]);
				};
				jQuery.each(selection, processCb);

				return {
					"subjectId" : module.data.subject.id.toString(),
					"add" : [],
					"remove" : remove
				};
			};

			answer["onSuccessBeforeRemoveCb"] = function(data) {
				if (data["success"] === true) {
					var remove = data["remove"];
					var subjectId = data["subjectId"];

					imperium_ui_widget_ChooserWithoutPagination
							.reloadTable("#userSelection");
					imperium_ui_widget_ChooserWithoutPagination
							.reloadTable("#showAvailableTable");
				}
			};

			answer["onFailureBeforeRemoveCb"] = answer["onFailureBeforeAddCb"];

			answer["confirmRemoval"] = true;

			return answer;
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_role_EditRoles.module;
	module.init();
	module.render();

	/**
	 * @todo remove
	 */
	globalModule = module;
});
