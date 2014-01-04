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
var imperium_ui_view_role_AssignPermissions = function() {
	var module = {
		init : function() {
		},
		render : function() {
			var tableLoaded = {
				selection : false,
				selected : false
			};

			var tableLoadedCb = function(key) {
				tableLoaded[key] = true;
				if ((tableLoaded["selection"] === true)
						&& (tableLoaded["selected"] === true)) {
				}
			};

//			var tableConfiguration = {
//				"bPaginate" : true,
//				"bFilter" : false,
//				"oLanguage" : {
//					"sInfo" : "_START_ to _END_ total:_TOTAL_",
//					"oPaginate" : {
//						"sPrevious" : "",
//						"sNext" : ""
//					}
//				}
//			};

			var createTableConfigCb = function() {
				var colDefinition = [ {
					"mData" : "resource"
				}, {
					"mData" : "action"
				} ];

				return {
					"bPaginate" : true,
					"bFilter" : true,
					"aoColumns" : colDefinition,
					"oLanguage" : {
						"sInfo" : "_START_ to _END_ total:_TOTAL_",
						"oPaginate" : {
							"sPrevious" : "",
							"sNext" : "",
							"sSearch" : ""
						},
						"sLengthMenu" : "_MENU_"
					},
					"fnInitComplete" : function(settings, serverResponse) {
						tableLoadedCb("selected");
					}
				};
			};

			var createTableConfigForAjaxTable = function() {
				var colDefinition = [ {
					"mData" : "resource",
					"bSortable" : true,
					"bVisible" : true
				}, {
					"mData" : "action",
					"bSortable" : true,
					"bVisible" : true
				}, {
					"mData" : "description",
					"bSortable" : false,
					"bVisible" : false
				}
				// {
				// "mData" : "description",
				// "bSortable" : false,
				// "mRender" : function(data, type, row) {
				// return "<a></a>";
				// var itemId = row["id"];
				// var data = {
				// id : itemId,
				// type : "role",
				// entityId : "grid_action_menu_role_id_"
				// + itemId
				// };
				// var html = imperium_ui_utils_Ui
				// .generateContentFromTemplate(
				// "#handlebarTemplate_grid_actions",
				// data);
				// return html;
				// }
				// }
				];

				var addParameterCb = function(aoData) {
					var formData = $("#filterToSelectForm").serializeArray();
					var queryParam = (formData[0]).value;

					if (queryParam !== "") {
						aoData.push({
							"name" : "sSearch",
							"value" : queryParam
						});
					}

				};

				var url = "/imperium/permission/ajax/list/1";

				var config = {
					"bProcessing" : true,
					"bServerSide" : true,
					"fnServerParams" : addParameterCb,
					// Get the data via ajax from a txt file
					// "sAjaxSource" : "/app/sampleData/subjectData2.txt",
					"sAjaxSource" : url,
					"sServerMethod" : "GET",
					"sAjaxDataProp" : "data",
					"aoColumns" : colDefinition,
					// pagination controls
					"sPaginationType" : "bootstrap",
					"aLengthMenu" : [ 25, 10 ],
					"bPaginate" : true,
					"bLengthChange" : true,
					// sorting
					// disable multiple page sorting
					"bsort" : false,
					// set the first sorting criteria
					// asc or desc
					"aaSorting" : [ [ 0, "asc" ] ],
					// width of the columns is automatic
					"bAutoWidth" : true,
					// remove the search box here
					"bFilter" : false,
					"bSearch" : false,
					// custom paging info
					"oLanguage" : {
						"sInfo" : "_START_ to _END_ total:_TOTAL_",
						"oPaginate" : {
							"sPrevious" : "",
							"sNext" : ""
						},
						"sLengthMenu" : "_MENU_",
						"sProcessing" : ""
					},
					"bDeferRender" : true,
					"fnInitComplete" : function(tableSettings, serverResponse) {
						tableLoadedCb("selection");
					}
				};

				return config;
			};

			/**
			 * @todo remove
			 */

			/**
			 * Table cb helpers
			 */
			var enableMultipleSelectionCb = function(tableSelector, cbToExecute) {
				var selector = tableSelector + " tr";

				var onClickCb = function(jqueryEvent) {
					$(this).toggleClass("success");
					cbToExecute(jqueryEvent);
				};

				$(selector).live("click", onClickCb);
			};

			/**
			 * 
			 */
			var enableMultipleSelectorWithClass = function(tableSelector,
					classToApply, cbToExecute) {
				var selector = tableSelector + " tr";

				var onClickCb = function(jqueryEvent) {
					$(this).toggleClass(classToApply);
					cbToExecute(jqueryEvent);
				};

				$(selector).live("click", onClickCb);
			};

			/**
			 * Returns an array of element where each element has: { data:array
			 * or object with data html:HTMLTableRowElement with the row
			 * information }
			 */
			var getTableSelection = function(tableSelector, selectedClass) {
				var selector = tableSelector + " tr." + selectedClass;
				var selection = $(selector);

				var table = $(tableSelector).dataTable();
				var data = table.fnGetData();

				var answer = [];

				var trElement;
				var eachElement;
				var dataIndex;
				var eachCb = function(index, element) {
					dataIndex = table.fnGetPosition(element);

					trElement = $(element);

					eachElement = {
						data : data[dataIndex],
						html : trElement[0]
					};
					answer.push(eachElement);
				};
				selection.each(eachCb);
				return answer;
			};

			var getAllRecordsFromTable = function(tableSelector) {
				var table = $(tableSelector).dataTable();
				var nodes = table.fnGetNodes();
				var data = table.fnGetData();

				var answer = [];
				var eachElement = {};
				for ( var i in nodes) {
					eachElement = {
						data : data[i],
						html : nodes[i]
					};
					answer.push(eachElement);
				}
				return answer;
			};

			var addRowToTableCb = function(tableSelection, row) {
				var table = $(tableSelection).dataTable();
				table.fnAddData(row);
			};

			var removeRowFromTableCb = function(tableSelection, index, cb,
					redrawTable) {
				var table = $(tableSelection).dataTable();
				var answer = table.fnDeleteRow(index, cb, redrawTable);
				return answer;
			};
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

			/**
			 * @param String
			 *            tableSelector selector to choose the table node with
			 *            jquery
			 * @param term
			 *            String term to use to filter in the table
			 */
			var filterTable = function(tableSelector, term) {
				var selection = getTableSelection("#userSelection", "#error");

				var index;
				var eachData;
				for (index in selection) {
					index = indexOfElementCb("#userSelection", eachData["data"]);

					if (index !== -1) {
						addRowToTableCb("#userSelection", eachData["data"]);
					}
				}
			};

			/**
			 * Functions to call before the addion and after if returns true
			 * goes on with the add else not
			 */
			var beforeAddCb = function(tableSelection, onTrueCb, onFalseCb) {

				return true;
			};

			/**
			 * Bind cb to buttons
			 */
			var subAddCb = function(selection) {
				var index;
				var eachData;
				for (index in selection) {
					eachData = selection[index];

					var index = indexOfElementCb("#userSelection",
							eachData["data"]);

					if (index === -1) {
						addRowToTableCb("#userSelection", eachData["data"]);
					}
				}
			};

			var filterSelectionCb = function(selection) {
				var answer = [];

				var eachData;
				var eachCb = function(index, value) {
					eachData = value["data"];
					jQuery.each(eachData, function(index, value) {
						eachData[index] = value.toString();
					});
					answer.push(value["data"]);
				};

				jQuery.each(selection, eachCb);

				return answer;
			};

			var addUrl = "";

			var onAddAllButtonCb = function(jqueryEvent) {

				var rawSelection = getAllRecordsFromTable("#showAvailableTable");
				var selection = filterSelectionCb(rawSelection);

				var data = {
					"roleId" : "1",
					"add" : [ "1", "2", "3", "4", "5", "6" ],
					"remove" : [ "7", "8", "9" ],
					"addSelection" : selection
				};

				var successCb = function(data) {
					subAddCb(rawSelection);
				};

				var failureCb = function() {

				};

				imperium_ui_dao_Role.updatePermission(data, successCb,
						failureCb);
			};

			var onAddSelectedButtonCb = function(jqueryEvent) {
				var rawSelection = getTableSelection("#showAvailableTable",
						"success");
				var selection = filterSelectionCb(rawSelection);

				var data = {
					"roleId" : "1",
					"add" : [ "1", "2", "3", "4", "5", "6" ],
					"remove" : [ "7", "8", "9" ],
					"addSelection" : selection
				};

				var successCb = function(data) {
					subAddCb(rawSelection);
				};

				var failureCb = function() {
				};

				imperium_ui_dao_Role.updatePermission(data, successCb,
						failureCb);
				// var selection = getTableSelection("#showAvailableTable",
				// "success");
				//
				// beforeAddCb(selection, null, null);
				//
				// subAddCb(selection);
			};

			var onSearchButtonCb = function(jqueryEvent) {
				jqueryEvent.preventDefault();
				var table = $("#showAvailableTable").dataTable();
				table.fnDraw();
			};

			var confirmRemoval = true;
			var removalMessage = "Do you want to remove the selected records";

			var subRemoveCb = function(selection) {
				var index;
				var eachData;
				for (index in selection) {
					eachData = selection[index];

					index = indexOfElementCb("#userSelection", eachData["data"]);

					if (index !== -1) {
						removeRowFromTableCb("#userSelection", index, null,
								true);
					}
				}
			};

			var onRemoveSelectionCb = function(jqueryEvent) {
				var rawSelection = getTableSelection("#userSelection", "error");

				if (rawSelection.length === 0) {
					return;
				}

				var selection = filterSelectionCb(rawSelection);

				var data = {
					"roleId" : "1",
					"add" : [ "1", "2", "3", "4", "5", "6" ],
					"remove" : [ "7", "8", "9" ],
					"removeSelection" : selection
				};

				var successCb = function(data) {
					subRemoveCb(rawSelection);
				};

				var failureCb = function() {

				};

				imperium_ui_dao_Role.updatePermission(data, successCb,
						failureCb);
				// if (confirmRemoval === true) {
				// bootbox.confirm(removalMessage, function(result) {
				// if (result === true) {
				// subRemoveCb(selection);
				// }
				// });
				// }

			};

			var onSearchUserSelectionCb = function(jqueryEvent) {
				jqueryEvent.preventDefault();
				
				
				var target = jqueryEvent["target"];
				var values = $(target).parent("form").serializeArray();
				var query = values[0];
				query = query["value"];
				
				var table = $("#permissionSelector #selectorPluginId10 #userSelection").dataTable();
				table.fnFilter(query, null, false, false, false, false);
			};
			
			// selector:#plugincontainer #pluginid #button
			$("#permissionSelector #selectorPluginId10 #addAllButton").on(
					"click", onAddAllButtonCb);

			$("#permissionSelector #selectorPluginId10 #addSelectedButton").on(
					"click", onAddSelectedButtonCb);

			$("#permissionSelector #selectorPluginId10 #doSearchButton").on(
					"click", onSearchButtonCb);

			$("#permissionSelector #selectorPluginId10 #removeSelectedButton")
					.on("click", onRemoveSelectionCb);

			$("#permissionSelector #selectorPluginId10 #doUserSelectionSearchButton")
					.on("click", onSearchUserSelectionCb);
			
			var toSearchTable = $("#showAvailableTable").dataTable(
					createTableConfigForAjaxTable());

			var selectionTable = $("#userSelection").dataTable(
					createTableConfigCb());

			var onSelection = function(jQueryEvent) {
			};

			enableMultipleSelectorWithClass("#showAvailableTable", "success",
					onSelection);
			enableMultipleSelectorWithClass("#userSelection", "error",
					onSelection);

		},
		addExistingPermissions : function() {

			var addRowToTableCb = function(tableSelection, row) {
				var table = $(tableSelection).dataTable();
				table.fnAddData(row);
			};

			var roleId = "1";
			var pagination = {
				page : "1",
				maxSize : "1000",
			};

			var aSuccessCb = function(serverResponse) {

				var data = serverResponse["data"];
				var index;
				var eachPermission;
				for (index in data) {
					eachPermission = data[index];
					addRowToTableCb("#userSelection", eachPermission);
				}
			};

			var aFailureCb = function(data) {
			};

			imperium_ui_dao_Role.getPermissions(roleId, pagination, aSuccessCb,
					aFailureCb);

		},

		hideSearchForm : function() {
			$('#userSelectionContainer div[class="dataTables_filter"]').hide();
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_role_AssignPermissions["module"];
	module.init();
	module.render();
	// module.addExistingPermissions();
	module.hideSearchForm();
});
