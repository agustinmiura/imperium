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
var imperium_ui_utils_Template = function() {
	'use strict';
	
	var getPath = function(name) {
		var url="/app/templates/{name}.js";
		url = url.replace("{name}", name);
		return url;
	};
	
	var getTemplate = function(name, successCb) {
		
		var url = getPath(name);
		
		var options = {
			method:'GET',
			cache:true,
			success:function(data) {
				successCb(data);
			}
		};
		$.ajax(options);
	};
	
	return {
		getTemplate:getTemplate
	};
}();
