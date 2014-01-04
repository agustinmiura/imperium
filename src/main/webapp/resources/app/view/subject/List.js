
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
var imperium_ui_view_subject_List = function() {
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

		bindEvents : function() {
			$("#getSelectedRowsButton").on("click", this.showSelectedRows);
			$("#removeSelectedButton").on("click", this.onClickRemoveRows);
			$("#addARowButton").on("click", this.onAddARow);
			$("#updateARowButton").on("click", this.onUpdateRow);
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

		onUpdateRow : function(event) {
			var table = module.getStaticTable();
			var selection = module.getStaticSelection();

			var newData = [ "a", "b", "c", "d", "e" ];
			var selected = selection[0];
			table.fnUpdate(newData, selected, undefined, true);

			globalTable = table;
			globalSelection = selection;
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

			var colDefinition = [
					{
						"mData" : "name",
						"bSortable" : true,
						"bVisible" : true
					},
					{
						"mData" : "application",
						"bSortable" : true,
						"bVisible" : true
					},
					{
						"mData" : "application",
						"bSortable" : false,
						"bVisible" : true,
						"mRender" : function(data, type, row) {
							globalData = data;
							globalType = type;
							globalRow = row;
							return '<div class="btn-group"><a class="btn" href="#"><i class="icon-magic"></i></a> <a class="btn" href="#"><i class="icon-remove"></i></a></div>';
						}
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
				"aLengthMenu" : [ 10, 25 ],
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
				"bFilter" : true
			};

			var onClickRowCb = function() {
				$(this).toggleClass("success");
			};

			var dataTable = $("#subjectFromCorsRequest").dataTable(config);
			this.table = dataTable;

			/**
			 * remove the live search behaviour and search
			 */
			dataTable.fnFilterOnReturn();

			$("#subjectFromCorsRequest tr").live("click", onClickRowCb);
			$("#example tr").live("click", onClickRowCb);
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_subject_List["module"];
	module.init();
	module.render();
});
