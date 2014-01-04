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
var imperium_ui_view_application_EditUiMain2 = function() {

	var module = {
		init : function() {
			this.render();
		},
		render : function() {
			this.renderRoleTable();
			this.renderSubjectTable();
			this.renderPermissionTable();
		},
		renderRoleTable : function() {
			var roleTableSelector = ("#tabContentRole #roleTable");
			/**
			 * null or a cb to add parameters before each request
			 */
			var addParameterCb = null;

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

			var url = "/imperium/role/ajax/list/1";
			var method = "GET";
			var showSearchInput = true;

			var config = {
				addParameterCb : addParameterCb,
				columnDefinition : columnDefinition,
				url : url,
				method : method,
				showSearchInput : showSearchInput,
				liveSearch : false
			};

			var roleTable = imperium_ui_utils_Datatable.renderTable(
					roleTableSelector, config);
		},
		renderSubjectTable : function() {
			var subjectTableSelector = ("#tabContentSubject #subjectTable");
			/**
			 * null or a cb to add parameters before each request
			 */
			var addParameterCb = null;

			var columnDefinition = [
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
						"sWidth" : 210,
						"mRender" : function(data, type, row) {
							var itemId = row["id"];
							var data = {
								id : itemId,
								type : "subject",
								entityId : "grid_action_menu_subject_id_"
										+ itemId
							};
							var html = imperium_ui_utils_Ui
									.generateContentFromTemplate(
											"#handlebarTemplate_grid_actions",
											data);
							return html;
						}
					} ];

			var url = "/imperium/subject/ajax/list/1";
			var method = "GET";
			var showSearchInput = true;

			var config = {
				addParameterCb : addParameterCb,
				columnDefinition : columnDefinition,
				url : url,
				method : method,
				showSearchInput : showSearchInput,
				liveSearch : false
			};

			var subjectTable = imperium_ui_utils_Datatable.renderTable(
					subjectTableSelector, config);
		},
		renderPermissionTable : function() {
			var subjectTableSelector = ("#tabContentPermission #permissionTable");
			/**
			 * null or a cb to add parameters before each request
			 */
			var addParameterCb = null;

			var columnDefinition = [
					{
						"mData" : "resource",
						"bSortable" : true,
						"bVisible" : true
					},{
						"mData" : "action",
						"bSortable" : true,
						"bVisible" : true
					},{
						"mData":"description",
						"bSortable":false,
						"bVisible":true
					},{
						"mData" : "application",
						"bSortable" : false,
						"sWidth" : 210,
						"mRender" : function(data, type, row) {
							var itemId = row["id"];
							var data = {
								id : itemId,
								type : "subject",
								entityId : "grid_action_menu_subject_id_"
										+ itemId
							};
							var html = imperium_ui_utils_Ui
									.generateContentFromTemplate(
											"#handlebarTemplate_grid_actions",
											data);
							return html;
						}
					} ];

			var url = "/imperium/permission/ajax/list/1";
			var method = "GET";
			var showSearchInput = true;

			var config = {
				addParameterCb : addParameterCb,
				columnDefinition : columnDefinition,
				url : url,
				method : method,
				showSearchInput : showSearchInput,
				liveSearch : false
			};

			var permissionTable = imperium_ui_utils_Datatable.renderTable(
					subjectTableSelector, config);
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_application_EditUiMain2["module"];
	module.init();
});
