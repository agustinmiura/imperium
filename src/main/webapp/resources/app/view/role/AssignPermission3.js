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
 * configure using one dynamic table that requests
 * and gets the available options and another
 * static table .
 * 
 */
var imperium_ui_view_role_AssignPermissions = function() {
	/**
	 * Helpers for the tables
	 */

	/**
	 * Add data to the table
	 */
	var addRowToTableCb = function(tableSelection, row) {
		var table = $(tableSelection).dataTable();
		table.fnAddData(row);
	};

	/**
	 * Search for an item in the table
	 */
	var indexOfElementCb = function(tableSelection, dataToSearch) {
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

	};
	
	var createSelectionAddParameterCb = function() {
		var cb = function(aoData){
			aoData.push({
				"name":"roleId",
				"value":"7"
			});
			aoData.push({
				"name":"applicationId",
				"value":"5"
			});
			
		};
		return cb;
	};
	
	/**
	 * private methods
	 */
	var createSelectionConfigCb = function() {
		var config = {};

		var columnDefinition = [ {
			"mData" : "resource",
			"sWidth":"25%"
		}, {
			"mData" : "action",
			"sWidth":"25%"
		} ];
		config["columnDefinition"] = columnDefinition;
		
		var addParameterCb = createSelectionAddParameterCb();
		config["addParameterCb"] = addParameterCb;
		
		config["url"]="/imperium/permission/ajax/list-for-role";
		config["method"]="GET";
		
		return config;
	};

	var createConfigForButtons = function() {
		var answer = {};

		var onSuccessCb = function(data) {
			var index;
			var eachData;
			for (index in selection) {
				eachData = selection[index];
				var index = indexOfElementCb("#userSelection", eachData["data"]);
				if (index === -1) {
					addRowToTableCb("#userSelection", eachData["data"]);
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
				"roleId" : "7",
				"add" : add,
				"remove" : []
			};
		};

		var onFailureBeforeAddCb = function(error) {
		};

		var onSuccessBeforeAddCb = function(data) {
			/**
			 * The permissions have been added to the role
			 */
			if (data["success"] === true) {
				var add = data["add"];
				var roleId = data["roleId"];
				imperium_ui_widget_Chooser.reloadTable();
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
				"roleId":"7",
				"add":[],
				"remove":remove
			};
		};
		/*
		var onSuccessBeforeRemoveCb = function(data) {
			if (data["success"] === true) {
				var remove = data["remove"];
				var roleId = data["roleId"];

				var result = imperium_ui_widget_Chooser.removeFromSelectionIds(
						"#permissionSelector #selectorPluginId10", remove);
			}
		};
		*/
		var onSuccessBeforeRemoveCb = function(data) {
			if (data["success"]===true) {
				var remove = data["remove"];
				var roleId = data["roleId"];
				imperium_ui_widget_Chooser.reloadTable("#userSelection");
				imperium_ui_widget_Chooser.reloadTable("#showAvailableTable");
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
	};

	/**
	 * public methods
	 */
	var initCb = function() {
	};

	var initSelectionWidget = function() {
		var toSearchColumnDefinition = [ {
			"mData" : "resource",
			"bSortable" : true,
			"sWidth":"10%",
			"bVisible" : true
		}, {
			"mData" : "action",
			"bSortable" : true,
			"bVisible" : true,
			"sWidth":"25%"
		}, {
			"mData" : "description",
			"bSortable" : false,
			"bVisible" : false,
			"sWidth":"25%"
		} ];

		var addParameterCb = function(aoData) {
			var tempArray = [1, 2, 3, 4, 5 ,6];
			
			var index;
			var eachValue;
			for (index in tempArray) {
				eachValue = tempArray[index];
				aoData.push({
					"name":index.toString(),
					"value":eachValue
				});
			}
			aoData.push({
				"name":"applicationId",
				"value":"5"
			});
			aoData.push({
				"name":"roleId",
				"value":"7"
			});
			
		};
		
		var toSearchConfig = {
			url:"/imperium/permission/ajax/list-available-role",
			method:"GET",
			columnDefinition : toSearchColumnDefinition,
			addParameterCb : addParameterCb	
		};
		
		var selectionConfig = createSelectionConfigCb();

		var config = {
			toSearchConfig : toSearchConfig,
			selectionConfig : selectionConfig
		};

		/**
		 * Add config for the add selected button
		 */
		var buttonConfig = createConfigForButtons();
		config["beforeAddCb"] = buttonConfig["beforeAddCb"];
		config["prepareDataToSendCb"] = buttonConfig["prepareDataToSendCb"];
		config["onSuccessBeforeAddCb"] = buttonConfig["onSuccessBeforeAddCb"];
		config["onFailureBeforeAddCb"] = buttonConfig["onFailureBeforeAddCb"];
		
		config["beforeRemoveCb"]=buttonConfig["beforeRemoveCb"];
		config["prepareDataToRemoveCb"]=buttonConfig["prepareDataToRemoveCb"];
		config["onSuccessBeforeRemoveCb"]=buttonConfig["onSuccessBeforeRemoveCb"];
		config["onFailureBeforeRemoveCb"]=buttonConfig["onFailureBeforeRemoveCb"];
		
		var selector = "#permissionSelector #selectorPluginId10";

		imperium_ui_widget_Chooser.init();
		var selectorWidget = imperium_ui_widget_Chooser
				.render(selector, config);
	};

	var renderCb = function() {
		initSelectionWidget();
	};

	return {
		init : initCb,
		render : renderCb
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_role_AssignPermissions;
	module.init();
	module.render();
});
