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
var imperium_ui_view_application_ListDef2 = function() {
	var urlManager = imperium_ui_utils_UrlManager.module;

	var module = {
		init : function() {
			this.initTable();
			this.setButtonListeners();
		},
		setButtonListeners: function() {
			var me = this;
			$("#dataTableButton").on("click", function(event) {
				event.preventDefault();
				console.log("On click for the data table button");
			});
		},
		initTable : function() {
			/**
			 * @todo remove
			 */
			console.log("Init table");
			/**
			 * 
			 */
			var tableSelector = $("table");

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
						"mData" : "action",
						"bSortable" : false,
						"sWidth" : 190,
						"mRender" : function(data, type, row) {
							var itemId = row["id"];
							var data = {
								id : itemId,
								type : "role",
								entityId : "grid_action_menu_application_id_"
										+ itemId,
								magicAction : true,
								removeAction : true,
								bookAction : true,
								previewAction : false,
								pencilAction : false,
								editAction : false,
								exclamationAction : true,
								asteriskAction : true
							};
							var html = imperium_ui_utils_Ui
									.generateContentFromTemplate(
											"#handlebarTemplate_grid_actions",
											data);
							return html;
						}
					} ];

			var url = "/imperium/webapp/application/see-list.json";

			// column definition
			var dataTableConfig = {};
			// set processing for the server side
			dataTableConfig["bProcessing"] = true;
			dataTableConfig["bServerSide"] = true;
			/**
			 * Set the url
			 */
			dataTableConfig["sAjaxSource"] = url;
			dataTableConfig["sServerMethod"] = "GET";
			/*
			 * Expect the data in the data attribute for the table
			 */
			dataTableConfig["sAjaxDataProp"] = "data";
			/**
			 * Set the column definition
			 */
			dataTableConfig["aoColumns"] = columnDefinition;
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
			/**
			 * Add the search input text in the table
			 */
			dataTableConfig["bFilter"] = true;
			dataTableConfig["bSearch"] = true;

			var config = {
				// addParameterCb : null,
				columnDefinition : columnDefinition,
				url : url,
				method : "GET",
				showSearchInput : true,
				liveSearch : false
			};
			/**
			 * Bootstrap pagination
			 */
			dataTableConfig["sPaginationType"] = "bootstrap";
			dataTableConfig["aLengthMenu"] = [ 25, 25 ];
			dataTableConfig["bPaginate"] = true;
			dataTableConfig["bLengthChange"] = false;
			/**
			 * Change the layout in the table 
			 * Functions 
			 */
			dataTableConfig["sDom"] = '<"#top"f>rt<"#bottom"p><"clear">';
			var tableInstance = $(tableSelector).dataTable(dataTableConfig);
			tableInstance.fnFilterOnReturn();
			/**
			 * Move left to the pagination
			 */
			$("#bottom").find(".dataTables_paginate").css("float","left");
			/**
			 * Add the top button
			 */
			$("#top").append('<button id="dataTableButton" class="btn btn-primary">Add application</button>');
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_application_ListDef2.module;
	module.init();
});
