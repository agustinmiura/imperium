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
var imperium_ui_view_application_ListDef = function() {
	var urlManager = imperium_ui_utils_UrlManager.module;

	var module = {
		init : function() {
			this.setActionListeners();
			this.renderAppTable();
			this.setButtonListeners();
		},
		doApplicationEdition : function(id) {
			var url = urlManager.getUrlForApplicationEdition(id);
			window.location = url;
		},
		editComponents : function(id) {
			window.location = urlManager.getUrlForAppShowEdit(id, "subject");
		},
		doApplicationRemoval : function(id) {
			var me = this;
			var message = "Are you sure you want to remove the application";
			bootbox.confirm(message, function(answer) {
				if (answer === true) {
					me.subRemoveApplication(id);
				}
			});
		},
		/**
		 * Object with the following attributes { success:true/false,
		 * application:{ id:101, description:"description string",
		 * name:"applicationName" } }
		 */
		onSuccessRemoval : function(response) {
			window.location = imperium_ui_utils_UrlManager.module
					.getUrlForAppListWithLast();
		},
		/**
		 * Object with the following attributes { success:true/false,
		 * application:{ id:101, description:"description string",
		 * name:"applicationName" } }
		 */
		onFailureRemoval : function(response) {
			var source = $("#handlebarTemplate_alert_div").html();
			var template = Handlebars.compile(source);

			var context = {
				id : "errorDiv",
				heading : "An Exception happened",
				message : "The application cannot be removed"
			};
			var html = template(context);

			$("#errorDiv").remove();
			$("#messageDiv").append(html);
		},
		subRemoveApplication : function(applicationId) {
			var me = this;

			imperium_ui_dao_Application.remove(applicationId,
					function(response) {
						if (response.success === true) {
							me.onSuccessRemoval(response);
						} else {
							me.onFailureRemoval(response);
						}
					}, function(response) {
						me.onFailureRemoval(response);
					});
		},
		setupPreviewForm : function(application) {
			var form = $("#applicationPreviewModal").find('form');
			form.find('input[name="name"]').val(application.name);
			form.find('input[name="description"]').val(application.description);
			form.find('input[name="apiKey"]').val(application.apiKey);
		},
		showApplicationInfo : function(applicationInfo) {
			var name = applicationInfo.name;
			var description = applicationInfo.description;
			var apiKey = applicationInfo.apiKey;
			var application = {
				id : applicationInfo.id,
				name : name,
				description : description,
				apiKey : apiKey
			};
			this.setupPreviewForm(application);
			$("#applicationPreviewModal").modal();
		},
		doPreview : function(id) {
			var me = this;
			imperium_ui_dao_Application.get(id, function(response) {
				me.showApplicationInfo(response);
			}, function(error) {

			});
		},
		onErrorWithShowKey : function(error) {
		},
		getTableHtml : function() {
			var fragments = [ "<table>", "<tr>", "<td>", "Key:", "</td>",
					"<td>", "{key}", "</td>", "</tr>", "</table>" ];
			var answer = "";
			var index;
			for (index in fragments) {
				answer = answer + fragments[index];
			}
			return answer;
		},
		onSubshowkey : function(response) {
			var apiKey = response.apiKey;
			var content = this.getTableHtml();
			content = content.replace("{key}", apiKey);
			bootbox.alert(content, function(ok) {
			});
		},
		showApiKey : function(id) {
			var me = this;

			imperium_ui_dao_Application.get(id, function(response) {
				me.onSubshowkey(response);
			}, function(error) {
				me.onErrorWithShowKey(error);
			});
		},
		onErrorWithResetApiKey : function() {
		},
		showResetKeyResult : function(application) {
			bootbox.alert("The key has been reset");
		},
		doResetApiKey : function(id) {
			var me = this;
			imperium_ui_dao_Application.resetKey(id, function(response) {
				me.showResetKeyResult(response);
			}, function(error) {
				me.onErrorWithResetApiKey(error, id);
			});
		},
		resetApiKey : function(id) {
			var me = this;

			bootbox.confirm("Are you sure you want to reset the api key",
					function(result) {
						if (result === true) {
							me.doResetApiKey(id);
						}
					});
		},
		onCreateApplication : function(event) {
			event.preventDefault();
			window.location = urlManager.getUrlForAppCreate();
		},
		setActionListeners : function() {
			var me = this;
			var me = this;
			$("table").on("click", 'tr a[class="btn"]', function(event) {
				me.onActionClicked(event);
			});
		},
		onActionClicked: function(event) {
			var target = event.target;
			var className = target.className;
			if (className==='btn') {
				//on button clicked 
				this.onActionButtonClicked(target);
			} else {
				// i element clicked
				this.onImageActionClicked(target);
			}
		},
		onActionButtonClicked: function(element) {
			var imageElement = $(element).find('i');
			this.onImageActionClicked(imageElement);
		},
		onImageActionClicked: function(element) {
			var action = $(element).attr('data-action');
			var id = $(element).attr('data-id');
				
			if (action==="magicAction") {
				this.doApplicationEdition(id);
			//
			} else if (action==="bookAction") {
				this.editComponents(id);
			//
			} else if (action==='exclamationAction') {
				this.showApiKey(id);
			//done
			} else if (action==='asteriskAction') {
				this.doResetApiKey(id);
			//done
			} else if (action==='removeAction') {
				this.doApplicationRemoval(id);
			}
		},
		setButtonListeners : function() {
			var me = this;
			$("#dataTableButton").on("click", function(event) {
				me.onCreateApplication(event);
			});

		},
		renderAppTable : function() {
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
			var config = {
				addParameterCb : null,
				columnDefinition : columnDefinition,
				url : url,
				method : "GET",
				showSearchInput : true,
				liveSearch : false
			};
			var table = imperium_ui_utils_Datatable.renderTable(tableSelector,
					config);

		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = imperium_ui_view_application_ListDef.module;
	module.init();
});
