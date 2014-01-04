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
var imperium_ui_utils_Ui = function() {
	"use strict";
	
	var convertArrayToMapCb = function(anArray) {
		var answer = {};
		
		var eachName=null;
		var eachValue=null;
		var eachInput=null;
		var index=null;
		
		for (index in anArray) {
			eachInput = anArray[index];
			eachName = eachInput["name"];
			eachValue = eachInput["value"];
			
			answer[eachName] = eachValue;
		}
		
		return answer;
	};
	/**
	 * Apply the toString method to each element
	 * of an array
	 */
	var mapElementsToString=function(anArray) {
		var answer = [];
		var mapCb = function(index, element) {
			answer.push(element.toString());
		};
		$.each(anArray, mapCb);
		
		return answer;
	};
	
	/**
	 * Read the content of a handle bars template,
	 * compile it and apply with the data
	 */
	var generateContentFromTemplate= function(templateSelector, data) {
		var source = $(templateSelector).html();
		var templateCb = Handlebars.compile(source);
		return templateCb(data);
	};
	
	return {
		convertArrayToMap:convertArrayToMapCb,
		mapElementsToString:mapElementsToString,
		generateContentFromTemplate:generateContentFromTemplate
	};
}();
