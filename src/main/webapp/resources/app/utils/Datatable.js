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
var imperium_ui_utils_Datatable = function() {

	var setDefaultPagination = function(tableConfig) {
		tableConfig["sPaginationType"] = "bootstrap";
		tableConfig["aLengthMenu"] = [ 25, 25 ];
		tableConfig["bPaginate"] = true;
		tableConfig["bLengthChange"] = false;
	};

	var checkValueCb = function(object, property, expectedValue) {
		var answer = false;
		if (property in object) {
			answer = (object[property] === expectedValue);
		}
		return answer;
	};

	var renderTableCb = function(tableSelector, config) {
		var addParametersToRequest = (("addParameterCb" in config) && (config["addParameterCb"] != null));

		var dataTableConfig = {};

		if (addParametersToRequest === true) {
			dataTableConfig["fnServerParams"] = config["addParameterCb"];
		}

		// set processing for the server side
		dataTableConfig["bProcessing"] = true;
		dataTableConfig["bServerSide"] = true;
		/**
		 * Set the url
		 */
		dataTableConfig["sAjaxSource"] = config["url"];
		dataTableConfig["sServerMethod"] = config["method"];
		/*
		 * Expect the data in the data attribute for the table
		 */
		dataTableConfig["sAjaxDataProp"] = "data";
		/**
		 * Set the column definition
		 */
		dataTableConfig["aoColumns"] = config["columnDefinition"];
		/**
		 * Enable or disable multiple sorting
		 */
		if ("multipleSort" in config) {
			dataTableConfig["bsort"] = true;
		}
		/**
		 * Change the labels for the table
		 */
		dataTableConfig["oLanguage"] = {
			"sInfo" : "_START_ to _END_ total:_TOTAL_",
			"oPaginate" : {
				"sPrevious" : "",
				"sNext" : ""
			}
		};
		
		dataTableConfig["sDom"] = '<"#top"f>rt<"#bottom"p><"clear">';
		/**
		 * Add the search input text in the table
		 */
		var showSearch = false;
		if (("showSearchInput" in config)
				&& (config["showSearchInput"]) === true) {
			showSearch = true;
			dataTableConfig["bFilter"] = true;
			dataTableConfig["bSearch"] = true;
		}

		/**
		 * @todo see pagination later
		 */
		if (checkValueCb(config, "pagination", true)) {
			setDefaultPagination(dataTableConfig);
		} else {
			setDefaultPagination(dataTableConfig);
		}

		var tableInstance = $(tableSelector).dataTable(dataTableConfig);

		if ((showSearch === true) && (config["liveSearch"]) === false) {
			tableInstance.fnFilterOnReturn();
		}
		/**
		 * Move the pagination to the left
		 */
		$("#bottom").find(".dataTables_paginate").css("float","left");
		/**
		 * Add custom toolbar
		 */
		$("#top").append('<button id="dataTableButton" class="btn btn-primary">Add application</button>');
		
		if ("onDrawCallback" in config) {
			dataTableConfig["fnDrawCallback"] = function(oSettings) {
				var cb = config["onDrawCallback"];
				cb(oSettings);
			};
		}

		return tableInstance;
	};

	var renderTableCbWithExtJsConfig = function(tableSelector, config) {

	};

	return {
		renderTable : renderTableCb,
		renderGrid : renderTableCbWithExtJsConfig
	};
}();
