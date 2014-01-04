//
// Usage example:
// 
// getDataFromServerCb : function(data, rawData, successCb, failureCb) {
//			var url = "/imperium/test/numbers";
//			var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
//				var filteredList = [];
//				var index;
//				var value;
//				for (index in data) {
//					value = data[index];
//					filteredList.push(value.name);
//					rawData[value.id] = value.name;
//				}
//				successCb(filteredList);
//			};
//			var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
//				failureCb(xmlHttpRequest);
//			};
//			var options = {
//					url : url,
//					cache : false,
//					type : "POST",
//					success : aSuccessCb,
//					failure : aFailureCb,
//					contentType : "application/json; charset=UTF-8",
//					data : JSON.stringify(data)
//			};
//			$.ajax(options);
//		},
//		setupTypeAheadWithWidget : function() {
//			var me = this;
//			var fullData = {};
//			var sourceCb = function(query, processCb) {
//				var data = {
//					query : query,
//					start : "0",
//					maxSize : "25"
//				};
//				me.getDataFromServerCb(data, fullData, processCb, processCb);
//			};
//			var matcherCb = function(item) {
//				console.log("Matches the item " + item + " ?");
//				return true;
//			};
//			var highlighterCb = function(item) {
//				console.log("Change the html of the item");
//				return "<h1>" + item + "</h1>";
//			};
//			var updateCb = function(item) {
//				console.log("Update cb");
//				return "user option :" + item;
//			};
//			var config = {
//				selector : "#dynamicTypeAheadWidget",
//				minLength : 3,
//				sourceCb : {
//					cb : sourceCb,
//					waitTime : 0.5
//				},
//				matcherCb : matcherCb,
//				highlighterCb : highlighterCb,
//				updateCb : updateCb
//			};
//			var typeAheadConfig = imperium_ui_widget_TypeAhead.create(config);
//		}
var imperium_ui_widget_TypeAhead = function() {

	var module = {
		create : function(config) {
			var selector = config.selector;

			/**
			 * Set the min size for a word before sending the request to the
			 * server
			 */
			var typeAheadConfig = {
				minLength : config.minLength
			};

			var sourceCbConfig = config.sourceCb;
			var sourceCb = sourceCbConfig.cb;
			var waitTime = sourceCbConfig.waitTime;
			waitTime = waitTime * 1000;
			/**
			 * Set the typeahead bootstrap widget to call to the server after
			 * the user stops typing for ${waitTime} seconds
			 */
			var lazySourceCb = _.debounce(sourceCb, waitTime);
			typeAheadConfig.source = function(query, process) {
				lazySourceCb(query, process);
			};

			/**
			 * If the user sends the cb in the attribute matcherCb which
			 * receives the item wants to use a custom search criteria for the
			 * options
			 */
			if (("matcherCb" in config) && (config.matcherCb != null)) {
				var cb = config.mather;
				typeAheadConfig.matcher = function(item) {
					return cb(item);
				};
			}
			/**
			 * Change the html of the item to show in the input text
			 */
			if (("highlighterCb" in config) && (config.highlighterCb != null)) {
				var cb = config.highlighterCb;
				typeAheadConfig.highlighter = function(item) {
					return cb(item);
				};
			}

			/**
			 * Set the string to suggest to the user
			 */
			if (("updateCb" in config) && (config.formatCb != null)) {
				var cb = config.formatCb;
				typeAheadConfig.updater = function(item) {
					return cb(item);
				};
			}
			return $(selector).typeahead(typeAheadConfig);
		}
	};

	return {
		create : module.create
	};
}();