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
 * Widget that has two tables
 * one to show the available options to the user
 * and a second table to show the user selection
 * 
 * 
 */
var imperium_ui_widget_Chooser = function() {
	var me = imperium_ui_widget_Chooser;
	/**
	 * Instance vars
	 */
	var tableLoaded = {
		selection : false,
		selected : false
	};

	var config = null;
	var selector = null;

	/**
	 * table cb helpers
	 */
	var enableMultipleSelectorWithClassCb = function(tableSelector,
			classToApply, cbToExecute) {
		var selector = tableSelector + " tr";

		var onClickCb = function(jqueryEvent) {
			$(this).toggleClass(classToApply);
			if (cbToExecute != null) {
				cbToExecute(jqueryEvent);
			}
		};

		$(selector).live("click", onClickCb);
	};

	/**
	 * Apply the toString method to all the attributues of the objects in the
	 * table
	 */
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

	/**
	 * get table selection
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
	/**
	 * Select all the records in a table
	 */
	var selectAllInTableCb = function(tableSelector, selectionClass) {
		$(tableSelector).find("tr").toggleClass(selectionClass, true);
	};

	/**
	 * Hide the search input in a data table
	 */
	var hideSearchFormInTable = function(selector) {
		$(selector).find('div[class="dataTables_filter"]').hide();
	};

	/**
	 * Get all records from a table
	 */
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

	/**
	 * Find an element with attributes in a table
	 * 
	 * tableSelector:String with the selector of the table toSearch: object with
	 * the attributes values attributes: attributes to test in the object
	 */
	var indexOfElementWith = function(tableSelector, toSearch, properties) {
		var table = $(tableSelector).dataTable();
		var data = table.fnGetData();

		var answer = -1;

		var propertyName;
		var foundElement;
		var index = -1;
		var eachData;
		for (index in data) {
			eachData = data[index];

			foundElement = imperium_ui_utils_Util.areEqual(eachData, toSearch,
					properties);

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
	 * Enable multiple selection
	 */
	var enableMultipleSelectionCb = function() {
		var selector = me.selector;

		enableMultipleSelectorWithClassCb(selector + " #showAvailableTable",
				"success", null);
		enableMultipleSelectorWithClassCb(selector + " #userSelection",
				"error", null);
	};

	/**
	 * Add data to the table
	 */
	var addRowToTableCb = function(tableSelection, row) {
		var table = $(tableSelection).dataTable();
		table.fnAddData(row);
	};

	/**
	 * Remove data from the table
	 */
	var removeRowFromTableCb = function(tableSelection, index, cb, redrawTable) {
		var table = $(tableSelection).dataTable();
		var answer = table.fnDeleteRow(index, cb, redrawTable);
		return answer;
	};

	/**
	 * Get data from a table
	 */
	var getDataFromTable = function(tableSelector, index) {
		var table = $(tableSelector).dataTable();
		var data = table.fnGetData();
		var answer = null;
		if (index in data) {
			answer = data[index];
		}
		return answer;
	};

	/**
	 * Filter the records in a table with static content using the defined query
	 */
	var filterTableWith = function(tableSelector, query) {
		var table = $(tableSelector).dataTable();
		table.fnFilter(query, null, false, false, false, false);
	};

	/**
	 * Reload the content of a data table doing a request
	 */
	var reloadTable = function(tableSelector) {
		var table = $(tableSelector).dataTable();
		table.fnDraw();
	};

	/**
	 * Configure add selectedButton
	 */
	var configureAddSelectedButton = function() {
		var selector = me.selector;
		var config = me.config;

		var toSearchTableSelector = selector + " #showAvailableTable";

		var beforeAddCb = config["beforeAddCb"];
		var prepareDataToSendCb = config["prepareDataToSendCb"];
		var onSuccessBeforeAddCb = config["onSuccessBeforeAddCb"];
		var onFailureBeforeAddCb = config["onFailureBeforeAddCb"];

		var onAddSelectedButtonClick = function(jqueryEvent) {
			var rawSelection = getTableSelection(toSearchTableSelector,
					"success");
			var selection = filterSelectionCb(rawSelection);
			var dataToSend = prepareDataToSendCb(selection);
			beforeAddCb(dataToSend, onSuccessBeforeAddCb, onFailureBeforeAddCb);
		};

		$(selector + " #addSelectedButton").on("click",
				onAddSelectedButtonClick);
	};
	/**
	 * Configure add All button
	 */
	var configureAddAllButton = function() {
		var selector = me.selector;
		var config = me.config;

		var toSearchTableSelector = selector + " #showAvailableTable";

		var beforeAddCb = config["beforeAddCb"];
		var prepareDataToSendCb = config["prepareDataToSendCb"];
		var onSuccessBeforeAddCb = config["onSuccessBeforeAddCb"];
		var onFailureBeforeAddCb = config["onFailureBeforeAddCb"];

		var onAddSelectedButtonClick = function(jqueryEvent) {
			selectAllInTableCb(toSearchTableSelector, "success");

			var rawSelection = getAllRecordsFromTable(toSearchTableSelector,
					"success");
			var selection = filterSelectionCb(rawSelection);
			var dataToSend = prepareDataToSendCb(selection);
			beforeAddCb(dataToSend, onSuccessBeforeAddCb, onFailureBeforeAddCb);
		};
		var data = {
			selector : selector
		};
		$(selector + " #addAllButton").on("click", data,
				onAddSelectedButtonClick);
	};
	/**
	 * Configure remove selected button from the selection
	 */
	var configureRemoveSelectedButton = function() {
		var selector = me.selector;
		var config = me.config;

		var selectionTableSelector = selector + " #userSelection";

		var beforeRemoveCb = config["beforeRemoveCb"];
		var prepareDataToRemoveCb = config["prepareDataToRemoveCb"];
		var onSuccessBeforeRemoveCb = config["onSuccessBeforeRemoveCb"];
		var onFailureBeforeRemoveCb = config["onFailureBeforeRemoveCb"];

		var subRemoveCb = function() {
			var rawSelection = getTableSelection(selectionTableSelector,
					"error");
			var selection = filterSelectionCb(rawSelection);
			var dataToRemove = prepareDataToRemoveCb(selection);
			beforeRemoveCb(dataToRemove, onSuccessBeforeRemoveCb,
					onFailureBeforeRemoveCb);
		};

		var subRemoveCbWithConfirm = function() {
			var afterCb = function(result) {
				if (result === true) {
					subRemoveCb();
				}
			};
			bootbox.confirm("Do you want to remove the selected records",
					afterCb);
		};

		var onRemoveSelectedButtonClick = function(jqueryEvent) {
			var rawSelection = getTableSelection(selectionTableSelector,
					"error");

			if ((rawSelection.length >= 1)
					&& (config["confirmRemoval"] === true)) {
				subRemoveCbWithConfirm();
			} else if ((rawSelection.length >= 1)) {
				subRemoveCbWithConfirm();
			}
		};
		var data = {
			config : config
		};
		$(selector).find("#removeSelectedButton").on("click", data,
				onRemoveSelectedButtonClick);
	};

	/**
	 * 
	 */
	var configureFilterSelectionButton = function() {
		var selector = me.selector;

		var onSearchSelectionClick = function(jqueryEvent) {
			jqueryEvent.preventDefault();
			var target = jqueryEvent["target"];
			var values = $(target).parent("form").serializeArray();
			var query = values[0];
			query = query["value"];
			filterTableWith(selector + " #userSelection", query);
		};
		$(selector + " #filterSelectionContainer #doUserSelectionSearchButton")
				.on("click", onSearchSelectionClick);
	};

	var configureFilterToSearchButton = function() {
		var selector = me.selector;

		var onFilterToSearchTableClick = function(jqueryEvent) {
			jqueryEvent.preventDefault();
			var target = jqueryEvent["target"];
			var values = $(target).parent("form").serializeArray();
			var query = values[0];
			query = query["value"];

			reloadTable(selector + " #showAvailableTable");
		};

		$(selector + " #filterToSearchContainer #doSearchButton").on("click",
				onFilterToSearchTableClick);
	};

	/**
	 * Methods
	 */
	var tableLoadedCb = function(key) {
		tableLoaded[key] = true;
		if ((tableLoaded["selection"] === true)
				&& (tableLoaded["selected"] === true)) {
			enableMultipleSelectionCb();
			configureAddSelectedButton();
			configureAddAllButton();
			configureRemoveSelectedButton();
			configureFilterSelectionButton();
			configureFilterToSearchButton();
			onWidgetRenderCb();
		}
	};
	/**
	 * 
	 */
	var onWidgetRenderCb = function() {
	};
	/**
	 * Create a cb that reads the content from the input in and filters the
	 * available options
	 */
	var createAddParameterCb = function(selector) {
		var cb = function(aoData) {
			var form = $(selector).find("#filterToSearchContainer form");
			var formData = form.serializeArray();
			var queryParam = (formData[0]).value;

			if (queryParam !== "") {
				aoData.push({
					"name" : "sSearch",
					"value" : queryParam
				});
			}
		};

		return cb;
	};

	var createAddParameterCbCustomized = function(selector, userCb) {
		var cb = function(aoData) {
			var form = $(selector).find("#filterToSearchContainer form");
			var formData = form.serializeArray();
			if (formData.length >= 1) {
				var queryParam = (formData[0]).value;
				if (queryParam !== "") {
					aoData.push({
						"name" : "sSearch",
						"value" : queryParam
					});
				}
			}
			userCb(aoData);
		};

		return cb;
	};

	var createAvailableOptionsTableConfigCb = function(selector, config) {
		me.selector = selector;
		me.config = config;

		var toSearchConfig = config["toSearchConfig"];

		var columnDefinition = toSearchConfig["columnDefinition"];

		var addParameterCb = createAddParameterCb(selector);
		var url = toSearchConfig["url"];
		var method = toSearchConfig["method"];

		var config = {
			"bProcessing" : true,
			"bServerSide" : true,
			"fnServerParams" : addParameterCb,
			"sAjaxSource" : url,
			"sServerMethod" : method,
			"sAjaxDataProp" : "data",
			"aoColumns" : columnDefinition,
			"sPaginationType" : "bootstrap",
			"aLengthMenu" : [ 25, 10 ],
			"bPaginate" : true,
			"bLengthChange" : true,
			// disable multiple page sorting
			"bsort" : false,
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

		/**
		 * Check if the user set a cb to execute before sending a request to
		 * fill the search options
		 */
		var beforeCb = null;
		if ("addParameterCb" in toSearchConfig) {
			beforeCb = createAddParameterCbCustomized(selector,
					toSearchConfig["addParameterCb"]);
			config["fnServerParams"] = beforeCb;
		}

		return config;
	};

	var createSelectedOptionsTableConfigCb = function(selector, config) {
		var selectionConfig = config["selectionConfig"];

		var columnDefinition = selectionConfig["columnDefinition"];

		var config = {
			"bPaginate" : true,
			"bFilter" : true,
			"aoColumns" : columnDefinition,
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

		return config;
	};

	var initCb = function() {
		me = imperium_ui_widget_Chooser;
	};

	/**
	 * Render cb here
	 */
	var renderCb = function(selector, config) {
		me.selector = selector;
		me.config = config;

		var toSearchConfig = createAvailableOptionsTableConfigCb(selector,
				config);

		var selectionConfig = createSelectedOptionsTableConfigCb(selector,
				config);

		var toSearchTable = $(selector).find("#showAvailableTable").dataTable(
				toSearchConfig);

		var selectionTable = $(selector).find("#userSelection").dataTable(
				selectionConfig);
		hideSearchFormInTable(selector + " #userSelectionContainer ");
	};
	/**
	 * Iterate the list of ids and add them to the selection of the widget only
	 * if they have not been already added
	 * 
	 * returns an object["added"]=ids added ["notAdded"]= elements already added
	 */
	var addToSelectionIdsCb = function(selector, ids) {
		var selectionTable = $(selector).find("#userSelection").dataTable();

		var added = [];
		var notAdded = [];

		var attributes = [ "id" ];
		var index;
		var eachId;
		var indexInToSearch;
		var indexInSelection;
		var toSearch;
		var toAdd;
		var addElement = false;
		for (index in ids) {
			eachId = ids[index];
			indexInSelection = -1;
			/**
			 * All the fields in datatable are strings
			 */
			toSearch = {
				id : eachId.toString()
			};
			indexInSelection = indexOfElementWith(selector + " #userSelection",
					toSearch, attributes);
			indexInToSearch = indexOfElementWith(selector
					+ " #showAvailableTable", toSearch, attributes);
			/**
			 * If the element can be selected and hasn't been added in selection
			 */
			addElement = ((indexInToSearch !== -1) && (indexInSelection === -1));
			if (addElement === true) {
				toAdd = getDataFromTable(selector + " #showAvailableTable",
						indexInToSearch);
				addRowToTableCb(selector + " #userSelection", toAdd);

				added.push(eachId.toString());
			} else {
				notAdded.push(eachId.toString());
			}

		}
		return {
			addded : added,
			notAdded : notAdded
		};

	};

	var removeFromSelectionIdsCb = function(selector, ids) {

		var removed = [];
		var notRemoved = [];

		var attributes = [ "id" ];
		var index;
		var eachId;
		var indexInSelection;
		var toRemove;
		var removeResult;
		for (index in ids) {
			eachId = ids[index];
			indexInSelection = -1;

			/**
			 * All the fields in datatable are strings
			 */
			toSearch = {
				id : eachId.toString()
			};

			indexInSelection = indexOfElementWith(selector + " #userSelection",
					toSearch, attributes);

			if (indexInSelection !== -1) {
				toRemove = getDataFromTable(selector + " #userSelection",
						indexInSelection);
				removeResult = removeRowFromTableCb(selector
						+ " #userSelection", indexInSelection, null, true);
				removed.push(toRemove);
			} else {
				notRemove.push(toRemove);
			}
		}

		return {
			removed : removed,
			notRemoved : notRemoved
		};

	};

	return {
		init : initCb,
		render : renderCb,
		addToSelectionIds : addToSelectionIdsCb,
		removeFromSelectionIds : removeFromSelectionIdsCb
	};
}();
