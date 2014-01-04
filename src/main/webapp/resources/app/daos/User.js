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
var imperium_ui_dao_User = function() {

	var urlManager = imperium_ui_utils_UrlManager.module;

	var createCb = function(data, successCb, failureCb) {
		$.ajax({
			url : urlManager.getUrlForUserCreateSubmit(),
			cache : false,
			type : "POST",
			success : function(answerData, textStatus, xmlHttpRequest) {
				successCb(answerData);
			},
			failure : function(xmlHttpRequest, textStatus, errorThrown) {
				failureCb(xmlHttpRequest);
			},
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		});
	};

	var editCb = function(data, successCb, failureCb) {
		$.ajax({
			url : urlManager.getUrlForUserEditSubmit(),
			cache : false,
			type : "POST",
			success : function(answerData, textStatus, xmlHttpRequest) {
				successCb(answerData);
			},
			failure : function(xmlHttpRequest, textStatus, errorThrown) {
				failureCb(xmlHttpRequest);
			},
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		});

	};

	var removeCb = function(data, successCb, failureCb) {
		$.ajax({
			url : urlManager.getUrlForUserRemoval(),
			cache : false,
			type : "POST",
			success : function(answerData, textStatus, xmlHttpRequest) {
				successCb(answerData);
			},
			failure : function(xmlHttpRequest, textStatus, errorThrown) {
				failureCb(xmlHttpRequest);
			},
			contentType : "application/json; charset=UTF-8",
			data : JSON.stringify(data)
		});
	};

	return {
		create : createCb,
		edit : editCb,
		remove : removeCb
	};
}();
