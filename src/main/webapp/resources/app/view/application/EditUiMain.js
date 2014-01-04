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
var imperium_ui_view_application_EditUiMain = function() {
	/**
	 * @todo restore this
	 */
	// "use strict";
	var module = {
		init : function() {
			this.bindEvents();
		},

		render : function() {
			this.renderStaticTable();
			this.renderSubjectsWithCors();
		},

		bindToRoleTableEvents: function() {
			var me = this;

			$(
			"#tabPanelContainer #tabContentRole table i[data-action='magicAction']")
			.live("click", this.onMagicAction);
		
			$(
			"#tabPanelContainer #tabContentRole table i[data-action='removeAction']")
			.live("click", this.onMagicAction);
			
			$(
			"#tabPanelContainer #tabContentRole table i[data-action='editAction']")
			.live("click", this.onMagicAction);
			
			$(
			"#tabPanelContainer #tabContentRole table i[data-action='editAction']")
			.live("click", this.onMagicAction);
		},
		
		onMagicAction: function(event) {
//			var target = (event.target);
//			var id = $(target).data("id");
//			var type=$(target).data("type");
//			var action = $(target).data("action");
			
		},
	
		bindEvents : function() {
			$("#getSelectedRowsButton").on("click", this.showSelectedRows);
			$("#removeSelectedButton").on("click", this.onClickRemoveRows);
			$("#addARowButton").on("click", this.onAddARow);
			$("#updateARowButton").on("click", this.onUpdateRow);
			// bind when the tabs are shown
			$('#tabPanelIndex a[data-toggle="tab"]').on("shown",
					this.onShownTab);
			
			this.bindToRoleTableEvents();
		},

		renderStaticTable : function() {
			var configuration = {
				"sPaginationType" : "bootstrap",
				"aLengthMenu" : [ 10, 25 ]
			};

			var dataTable = $("#example").dataTable(configuration);

			// remove the live search behaviour
			dataTable.fnFilterOnReturn();
		},

		getStaticSelection : function() {
			return $("#example tr.success");
		},

		getTable : function() {
			return $("#subjectFromCorsRequest").dataTable();
		},

		getSelectedNodes : function() {
			return $('#subjectFromCorsRequest tr.success');
		},

		onShownTab : function(event) {
			var selectors = [ "", "", "" ];
		},

		onUpdateRow : function(event) {

			var table = module.getStaticTable();
			var selection = module.getStaticSelection();

			var newData = [ "a", "b", "c", "d", "e" ];
			var selected = selection[0];
			table.fnUpdate(newData, selected, undefined, true);
		},

		onAddARow : function(event) {
			var table = module.getStaticTable();
			table.fnAddData([ "engine", "browser", "platform", "version",
					"grade" ]);
		},

		onClickRemoveRows : function(event) {
			var selectedRows = module.getStaticSelection();
			var table = module.getStaticTable();

			var size = selectedRows.size();
			var i;
			var eachRow;
			for (i = 0; i < size; i++) {
				eachRow = selectedRows[i];
				table.fnDeleteRow(eachRow);
			}
		},

		showSelectedRows : function(event) {
			var me = this;
			var selectedNodes = module.getSelectedNodes();
		},

		getTableConfigForRole : function() {
			var addParameterCb = function(aoData) {
				var index;
				var start = 0;
				var max = 10;
				var i;

				for (i = start; i < max; i++) {
					aoData.push({
						"name" : "name_" + i,
						"value" : "value_" + i
					});
				}
			};

			var colDefinition = [
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
						"mData" : "description",
						"bSortable" : false,
						"sWidth" : 210,
						"mRender" : function(data, type, row) {
							var itemId = row["id"];
							var data = {
								id : itemId,
								type : "role",
								entityId : "grid_action_menu_role_id_" + itemId
							};
							var html = imperium_ui_utils_Ui
									.generateContentFromTemplate(
											"#handlebarTemplate_grid_actions",
											data);
							return html;
						}
					} ];

			var url = "http://localhost:8080/imperium/webapp/role/list.json/15";

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
				"bFilter" : true,
				"bSearch" : true,
				// custom paging info
				"oLanguage" : {
					"sInfo" : "_START_ to _END_ total:_TOTAL_",
					"oPaginate" : {
						"sPrevious" : "",
						"sNext" : ""
					}
				},
				"bDeferRender" : true
			};

			return config;

		},

		getTableConfigForPermission : function() {
			var addParameterCb = function(aoData) {
				var index;
				var start = 0;
				var max = 10;
				var i;

				for (i = start; i < max; i++) {
					aoData.push({
						"name" : "name_" + i,
						"value" : "value_" + i
					});
				}
			};

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
				"bVisible" : true
			}, {
				"mData" : "application",
				"bSortable" : false,
				"bVisible" : false
			} ];

			var url = "http://localhost:8080/imperium/permission/ajax/list/1";

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
				"bFilter" : true,
				"bSearch" : true,
				// custom paging info
				"oLanguage" : {
					"sInfo" : "_START_ to _END_ total:_TOTAL_",
					"oPaginate" : {
						"sPrevious" : "",
						"sNext" : ""
					}
				},
				"bDeferRender" : true
			};

			return config;
		},

		renderSubjectsWithCors : function() {

			var addParameterCb = function(aoData) {
				var index;
				var start = 0;
				var max = 10;
				var i;

				for (i = start; i < max; i++) {
					aoData.push({
						"name" : "name_" + i,
						"value" : "value_" + i
					});
				}
			};

			var colDefinition = [ {
				"mData" : "name",
				"bSortable" : true,
				"sWidth" : "300px",
				"bVisible" : true
			}, {
				"mData" : "application",
				"bSortable" : true,
				"bVisible" : true
			} ];

			var url = "http://localhost:8080/imperium/subject/ajax/list/1";

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
				"bFilter" : true,
				"bSearch" : true,
				// custom paging info
				"oLanguage" : {
					"sInfo" : "_START_ to _END_ total:_TOTAL_",
					"oPaginate" : {
						"sPrevious" : "",
						"sNext" : ""
					}
				},
				"bDeferRender" : true
			};

			var onClickRowCb = function() {
				$(this).toggleClass("success");
			};

			var tableConfig = this.getTableConfigForRole();
			var roleTable = $("#tabPanelContainer #roleTable").dataTable(
					tableConfig);
			roleTable.fnFilterOnReturn();

			var permissionConfig = this.getTableConfigForPermission();
			var permissionTable = $("#tabPanelContainer #permissionTable")
					.dataTable(permissionConfig);
			permissionTable.fnFilterOnReturn();

			var subjectTable = $("#tabPanelContainer #subjectTable").dataTable(
					config);
			subjectTable.fnFilterOnReturn();
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_application_EditUiMain["module"];
	module.init();
	module.render();
});
