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
var imperium_ui_utils_UrlManager = function() {

	var rootUrl = "/imperium";

	var module = {
		getRootUrl: function() {
			return rootUrl;
		},
		getUrlForAppShowInfo : function(id) {
			var url = rootUrl + "/webapp/application/get.json?id={id}";
			return url.replace("{id}", id);
		},
		getUrlForAppList : function() {
			return rootUrl + "/webapp/application/list.json";
		},
		getUrlForAppListWithPage : function(page) {
			var url = rootUrl + "/webapp/application/list.json";
			url = url + "?page={page}&withPageIndexChanged=1";
			return url.replace("{page}", page);
		},
		getUrlForAppListWithLast : function() {
			return rootUrl + "/webapp/application/see-list?showLast=1";
		},
		getUrlForAppCreate : function() {
			return rootUrl + "/webapp/application/create";
		},
		getUrlForAppCreateSubmit : function() {
			return rootUrl + "/webapp/application/create.json";
		},
		getUrlForAppEditSubmit: function() {
			return rootUrl + "/webapp/application/edit.json";
		},
		getUrlForAppShowEdit : function(id, from) {
			var url = rootUrl + "/webapp/application/show-edit-ui/{id}";
			url = (url.replace("{id}", id));
			if (from!==undefined&&from!==null) {
				url = url+"?from="+from;
			}
			return url;
		},
		getUrlForAppResetKey: function(id) {
			var url = rootUrl + "/webapp/application/reset-key.json?id={id}";
			return (url.replace("{id}", id));
		},
		getUrlForApplicationRemoval: function(id) {
			var url = "/webapp/application/removal.json?id={id}";
			return (rootUrl+url.replace("{id}", id));
		},
		getUrlForRoleJsonList : function(applicationId) {
			var url = rootUrl + "/webapp/role/list.json/{applicationId}";
			return url.replace("{applicationId}", applicationId);
		},
		getUrlForSubjectJsonList : function(applicationId) {
			var url = rootUrl + "/webapp/subject/list.json/{applicationId}";
			return url.replace("{applicationId}", applicationId);
		},
		getUrlForPermissionJsonList : function(applicationId) {
			var url = rootUrl + "/webapp/permission/list.json/{applicationId}";
			return url.replace("{applicationId}", applicationId);
		},
		getUrlForShowCreateRole : function(applicationId) {
			var url = rootUrl + "/webapp/role/create/{applicationId}";
			return url.replace("{applicationId}", applicationId);
		},
		getUrlForRoleSubmit : function() {
			return rootUrl + "/webapp/role/do-create.json";
		},
		getUrlForRoleSubmitSuccess : function(roleId, applicationId) {
			var url = rootUrl + "/webapp/role/submit-success";
			url = url + "?applicationId={applicationId}";
			url = url + "&roleId={roleId}";

			url = url.replace("{roleId}", roleId);
			return url.replace("{applicationId}", applicationId);
		},
		getUrlForRoleEdit : function(roleId, applicationId) {
			var url = rootUrl + "/webapp/role/edition/{roleId}";
			url = url + "?applicationId={applicationId}";

			url = url.replace("{roleId}", roleId);
			return url.replace("{applicationId}", applicationId);
		},
		getForRoleEditSubmit : function(roleId) {
			var url = rootUrl + "/webapp/role/edit.json/{id}";
			return url.replace("{id}", roleId);
		},
		getUrlForRoleRemoveSubmit : function() {
			return rootUrl + "/webapp/role/removal-from-app.json";
		},
		getUrlToShowPermissionForm : function(applicationId) {
			var url = rootUrl + "/webapp/permission/create/{applicationId}";
			return url.replace("{applicationId}", applicationId);
		},
		getUrlToRedirectToApplicationEdit : function(applicationId) {
			var url = rootUrl
					+ "/webapp/application/redirect-to-edit/{applicationId}";
			return url.replace(url, applicationId);
		},
		getUrlForPermissionSubmit : function() {
			return (rootUrl + "/webapp/permission/create.json");
		},
		getUrlForPermissionJsonListWhere : function() {
			return (rootUrl + "/webapp/permission/list-where.json");
		},
		getUrlForPermissionEdit : function(permissionId) {
			var url = (rootUrl + "/webapp/permission/edition/{id}");
			return url.replace("{id}", permissionId);
		},
		getUrlForPermissionEditSubmit : function(permissionId) {
			var url = rootUrl + "/webapp/permission/edit.json/{id}";
			return url.replace("{id}", permissionId);
		},
		getUrlForPermissionRemoval : function() {
			return rootUrl + "/webapp/permission/removal-from-app.json";
		},
		getUrlToShowSubjectForm : function() {
			return rootUrl + "/webapp/subject/create";
		},
		getUrlForSubjectRemoval : function() {
			return rootUrl + "/webapp/subject/removal-from-app.json";
		},
		getUrlForSubjectEdition : function(subjectId, applicationId) {
			var url = rootUrl + "/webapp/subject/edition?subjectId={subjectId}";
			url = url + "&applicationId={applicationId}";
			url = url.replace("{subjectId}", subjectId);
			return url.replace("{applicationId}", applicationId);
		},
		getUrlForSubjectSubmit : function() {
			return rootUrl + "/webapp/subject/edition.json";
		},
		getUrlForPermissionOfRoleEdition : function(roleId, applicationId) {
			var url = rootUrl + "/webapp/role/show-permission-edit";
			url = url + "?roleId={roleId}&applicationId={applicationId}";
			url = url.replace("{roleId}", roleId);

			return url.replace("{applicationId}", applicationId);
		},
		getUrlToListPermissionsForRole : function() {
			return rootUrl + "/webapp/permission/list-for-role.json";
		},
		getUrlToListPermissionsAvailableForRole : function() {
			return rootUrl + "/webapp/permission/list-available-role.json";
		},
		getUrlToAddPermissionsToRole : function() {
			return rootUrl + "/webapp/role/update-permission.json";
		},
		getUrlForRoleOfSubjectEdition : function(subjectId, applicationId) {
			var url = rootUrl + "/webapp/subject/edit-roles";
			url = url + "?applicationId={applicationId}&subjectId={subjectId}";
			url = url.replace("{subjectId}", subjectId);

			return (url.replace("{applicationId}", applicationId));
		},
		getUrlForRoleJsonListForSubject : function() {
			return rootUrl + "/webapp/role/list-for-subject.json";
		},
		getUrlForRoleAvailableJsonListForSubject : function() {
			return rootUrl + "/webapp/role/list-available-for-subject.json";
		},
		getUrlForUpdateRoleForSubject : function() {
			return rootUrl + "/webapp/subject/update-role.json";
		},
		getUrlForUserList : function() {
			return rootUrl + "/webapp/user/list.json";
		},
		getUrlForUserCreate : function() {
			return rootUrl + "/webapp/user/create";
		},
		getUrlForUserCreateSubmit : function() {
			return rootUrl + "/webapp/user/create.json";
		},
		getUrlForUserGridAndShowLast : function() {
			return rootUrl + "/webapp/user/list?showLast=true";
		},
		getUrlForUserEdit : function(userId) {
			var url = rootUrl + "/webapp/user/edit?id={id}";
			return url.replace("{id}", userId);
		},
		getUrlForUserEditSubmit : function() {
			return rootUrl + "/webapp/user/edit.json";
		},
		getUrlForUserRemoval : function() {
			return rootUrl + "/webapp/user/remove.json";
		},
		getUrlForApplicationEdition : function(id) {
			var url = rootUrl + "/webapp/application/edit?id={id}";
			return url.replace("{id}", id);
		}
	};

	return {
		module : module
	};
}();
