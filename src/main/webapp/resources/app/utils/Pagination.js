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
var imperium_ui_utils_Pagination = function() {
	'use strict';
	var getPageQty = function(pageSize, total) {
		return (Math.ceil((total * 1.0) / (pageSize * 1.0)));
	};

	var calculatePagination = function(currentPage, pageSize, total) {
		var pageQty = getPageQty(pageSize, total);
		var beforePage = (currentPage >= 1) ? (currentPage - 1) : 0;
		var nextPage = ((currentPage + 1) >= pageQty) ? (pageQty - 1)
				: (currentPage + 1);

		var answer = {
			firstPage : 0,
			lastPage : (pageQty - 1),
			beforePage : beforePage,
			nextPage : nextPage,
			currentPage : currentPage,
			pageQty : pageQty,
			pageSize : pageSize
		};
		return answer;
	};
	return {
		calculatePagination : calculatePagination
	};
}();
