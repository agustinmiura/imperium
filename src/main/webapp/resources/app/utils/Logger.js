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
var imperium_ui_utils_Logger = function() {
	var logger = null;

	var module = {
		inspect : function(arr, level) {
			var dumped_text = "";
			if (!level)
				level = 0;

			//The padding given at the beginning of the line.
			var level_padding = "";
			for ( var j = 0; j < level + 1; j++)
				level_padding += "    ";

			if (typeof (arr) == 'object') { //Array/Hashes/Objects 
				for ( var item in arr) {
					var value = arr[item];

					if (typeof (value) == 'object') { //If it is an array,
						dumped_text += level_padding + "'" + item + "' ...\n";
						dumped_text += dump(value, level + 1);
					} else {
						dumped_text += level_padding + "'" + item + "' => \""
								+ value + "\"\n";
					}
				}
			} else { //Stings/Chars/Numbers etc.
				dumped_text = "===>" + arr + "<===(" + typeof (arr) + ")";
			}
			return dumped_text;
		},
		getLogger : function() {
			if (logger === null) {
				logger = log4javascript.getDefaultLogger();
			}
			return logger;
		}
	};

	return {
		inspect : module.inspect,
		getLogger : module.getLogger
	};
}();
