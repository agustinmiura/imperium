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
 * configure using one dynamic table that requests and gets the available
 * options and another static table .
 * 
 */
var imperium_ui_view_role_EditPermission = function() {
	var urlManager = imperium_ui_utils_UrlManager.module;
	
	var module = {
		application : undefined,
		role : undefined,
		init : function() {
			module.setData();
		},
		setData : function() {
			var data = module.getData();
			module.application = data.application;
			module.role = data.role;
		},
		getData : function() {
			var informationNode = $("#information");
			return {
				application : {
					id : informationNode.attr("data-application-id")
				},
				role : {
					id : informationNode.attr("data-role-id"),
					name : informationNode.attr("data-role-name"),
					description : informationNode.attr("data-role-description")
				}
			};
		},
		render : function() {
			module.initSelectionWidget();
		},
		initSelectionWidget : function() {
			var toSearchColumnDefinition = [ {
				"mData" : "resource",
				"bSortable" : true,
				"sWidth" : "10%",
				"bVisible" : true
			}, {
				"mData" : "action",
				"bSortable" : true,
				"bVisible" : true,
				"sWidth" : "25%"
			}, {
				"mData" : "description",
				"bSortable" : false,
				"bVisible" : false,
				"sWidth" : "25%"
			} ];

			var addParameterCb = module.createAddParameterCb();
			var url = urlManager.getUrlToListPermissionsAvailableForRole();
			var toSearchConfig = {
				url : url,
				method : "GET",
				columnDefinition : toSearchColumnDefinition,
				addParameterCb : addParameterCb
			};

			var selectionConfig = module.createSelectionConfig();

			var config = {
				toSearchConfig : toSearchConfig,
				selectionConfig : selectionConfig
			};

			var buttonConfig = module.createConfigForButtons();

			config["beforeAddCb"] = buttonConfig["beforeAddCb"];
			config["prepareDataToSendCb"] = buttonConfig["prepareDataToSendCb"];
			config["onSuccessBeforeAddCb"] = buttonConfig["onSuccessBeforeAddCb"];
			config["onFailureBeforeAddCb"] = buttonConfig["onFailureBeforeAddCb"];

			config["beforeRemoveCb"] = buttonConfig["beforeRemoveCb"];
			config["prepareDataToRemoveCb"] = buttonConfig["prepareDataToRemoveCb"];
			config["onSuccessBeforeRemoveCb"] = buttonConfig["onSuccessBeforeRemoveCb"];
			config["onFailureBeforeRemoveCb"] = buttonConfig["onFailureBeforeRemoveCb"];

			var selector = "#permissionSelector #selectorPluginId10";

			imperium_ui_widget_Chooser.init();
			var selectorWidget = imperium_ui_widget_Chooser.render(selector,
					config);
		},
		createAddParameterCb : function() {
			return function(aoData) {
				aoData.push({
					"name" : "applicationId",
					"value" : module.application.id.toString()
				});
				aoData.push({
					"name" : "roleId",
					"value" : module.role.id.toString()
				});

			};

		},
		createSelectionConfig : function() {
			var config = {};

			var columnDefinition = [ {
				"mData" : "resource",
				"sWidth" : "25%"
			}, {
				"mData" : "action",
				"sWidth" : "25%"
			} ];

			var url = urlManager.getUrlToListPermissionsForRole();
			var addParameterCb = module.createAddParameterCb();
			config["columnDefinition"] = columnDefinition;
			config["addParameterCb"] = addParameterCb;
			config["url"] = url;
			config["method"] = "GET";
			return config;
		},
		createConfigForButtons : function() {
			var answer = {};

			var onSuccessCb = function(data) {
				var index;
				var eachData;
				for (index in selection) {
					eachData = selection[index];
					var index = module.getIndexOfElement("#userSelection",
							eachData["data"]);
					if (index === -1) {
						module
								.addRowToTable("#userSelection",
										eachData["data"]);
					}
				}
			};

			var beforeAddCb = function(dataToSend, onSuccessCb, onFailureCb) {
				imperium_ui_dao_Role.updatePermission(dataToSend, onSuccessCb,
						onFailureCb);
			};

			var prepareDataToSendCb = function(selection) {
				var add = [];
				var processCb = function(index, value) {
					add.push(value["id"]);
				};
				jQuery.each(selection, processCb);

				return {
					"roleId" : module.role.id.toString(),
					"add" : add,
					"remove" : []
				};
			};

			var onFailureBeforeAddCb = function(error) {
			};

			var onSuccessBeforeAddCb = function(data) {
				if (data["success"] === true) {
					var add = data["add"];
					var roleId = data["roleId"];
					
					/**
					 * Reload the tables
					 */
					imperium_ui_widget_Chooser.reloadTable("#userSelection");
					imperium_ui_widget_Chooser
							.reloadTable("#showAvailableTable");
				}

			};

			var beforeRemoveCb = beforeAddCb;
			var prepareDataToRemoveCb = function(selection) {
				var remove = [];
				var processCb = function(index, value) {
					remove.push(value["id"]);
				};
				jQuery.each(selection, processCb);

				return {
					"roleId" : module.role.id.toString(),
					"add" : [],
					"remove" : remove
				};
			};

			var onSuccessBeforeRemoveCb = function(data) {
				if (data["success"] === true) {
					var remove = data["remove"];
					var roleId = data["roleId"];

					imperium_ui_widget_Chooser.reloadTable("#userSelection");
					imperium_ui_widget_Chooser
							.reloadTable("#showAvailableTable");
				}
			};

			var onFailureBeforeRemoveCb = onFailureBeforeAddCb;

			answer["beforeAddCb"] = beforeAddCb;
			answer["prepareDataToSendCb"] = prepareDataToSendCb;
			answer["onSuccessBeforeAddCb"] = onSuccessBeforeAddCb;
			answer["onFailureBeforeAddCb"] = onFailureBeforeAddCb;

			answer["beforeRemoveCb"] = beforeRemoveCb;
			answer["prepareDataToRemoveCb"] = prepareDataToRemoveCb;
			answer["onSuccessBeforeRemoveCb"] = onSuccessBeforeRemoveCb;
			answer["onFailureBeforeRemoveCb"] = onFailureBeforeRemoveCb;
			answer["confirmRemoval"] = true;

			return answer;
		},
		addRowToTable : function(tableSelection, row) {
			var table = $(tableSelection).dataTable();
			return table.fnAddData(row);
		},
		getIndexOfElement : function(tableSelection, dataToSearch) {
			var table = $(tableSelection).dataTable();
			var data = table.fnGetData();

			var answer = -1;

			var properties = [ "resource", "action" ];

			var propertyName;
			var foundElement;
			var index;
			var eachData;
			for (index in data) {
				eachData = data[index];

				foundElement = true;

				for ( var i in properties) {
					propertyName = properties[i];

					if (eachData[propertyName] !== dataToSearch[propertyName]) {
						foundElement = false;
						break;
					}
				}
				if (foundElement === true) {
					answer = index;
					break;
				}
			}

			if (typeof index === "string") {
				answer = parseInt(answer);
			}

			return answer;
		}
	};

	return {
		module : module
	};

}();

jQuery(function($) {
	var module = imperium_ui_view_role_EditPermission["module"];
	module.init();
	module.render();
});
