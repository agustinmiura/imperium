var typeAhead = function() {

	var module = {
		init : function() {
			var me = this;

			console.log("Init cb6");

			// me.setupStaticTypeAhead();

//			me.setupDynamicTypeAhead();

			me.setupTypeAheadWithWidget();
			
			me.setupButton();
		},
		setupButton: function() {
			$("#testButton").on("click", function(event) {
				
			});
		},
		setupStaticTypeAhead : function() {
			/**
			 * @todo remove
			 */
			console.log("Static type ahead");

			var index = 0;
			var max = 100;
			var source = [];
			while (index < max) {
				source.push(index.toString());
				index++;
			}

			var config = {
				source : source
			};

			$("#typeAhead").typeahead(config);

		},
		setupDynamicTypeAhead : function() {
			var me = this;

			var fullData = {};
			/**
			 * This source cb send the data to the server inmediately
			 */
			var sourceCb = function(query, processCb) {
				var data = {
					query : query,
					start : "0",
					maxSize : "25"
				};
				me.getDataFromServerCb(data, fullData, processCb, processCb);
			};
			/**
			 * Get the data after 300 miliseconds the user stopped typing the
			 * input
			 */
			var lazySourceCb = _.debounce(sourceCb, 300);

			var updaterCb = function(item) {
				console.log("Item here:" + item);
				return item;
			};

			/**
			 * Custom cb to match items in the typeahead
			 */
			var matcherCb = function(item) {
				/**
				 * @todo remove
				 */
				console.log("Matcher cb for :" + item);

				return true;
			};

			var highlighterCb = function(item) {
				var fullItem = fullData[1];

				return item + " " + fullItem;
			};

			var config = {
				source : function(query, process) {
					lazySourceCb(query, process);
				},
				minLength : 3,
				updater : updaterCb,
				matcher : matcherCb,
				highlighter : highlighterCb
			};

			$("#dynamicTypeAhead").typeahead(config);

		},
//		setupDynamicTypeAhead2 : function() {
//			var me = this;
//			
//			var sourceCb = function(query, processCb) {
//				var data = {
//					query : query,
//					start : "0",
//					maxSize : "25"
//				};
//				me.getDataFromServerCb(data, fullData, processCb, processCb);
//			};
//
//			var selector = "#dynamicTypeAheadWidget";
//			var config = {
//				/**
//				 * Min char qty before sending data to the server
//				 */
//				minLength : 3,
//				sourceCb : {
//					/**
//					 * Use this function to send data to the server
//					 */
//					cb : sourceCb,
//					/**
//					 * Time in seconds to wait before the user stop typing to
//					 * send data to the server
//					 */
//					waitTime : 0.3
//				},
//				/**
//				 * If it is null or undefined then use the default matching
//				 * criteria else define a custom cb to select which item is the
//				 * correct
//				 */
//				matcherCb : null,
//				/**
//				 * Custom function that receives the item and formats how to
//				 * show it to the user
//				 */
//				highlighterCb : null
//			};
//
//		},
		/**
		 * Request of query to the server
		 * 
		 * @param query
		 * @param successCb
		 * @param failureCb
		 */
		getDataFromServerCb : function(data, rawData, successCb, failureCb) {
			var url = "/imperium/test/numbers";
			var aSuccessCb = function(data, textStatus, xmlHttpRequest) {
				var filteredList = [];
				var index;
				var value;
				for (index in data) {
					value = data[index];
					filteredList.push(value.name);
					rawData[value.id] = value.name;
				}
				successCb(filteredList);
			};
			var aFailureCb = function(xmlHttpRequest, textStatus, errorThrown) {
				failureCb(xmlHttpRequest);
			};
			var options = {
					url : url,
					cache : false,
					type : "POST",
					success : aSuccessCb,
					failure : aFailureCb,
					contentType : "application/json; charset=UTF-8",
					data : JSON.stringify(data)
			};
			$.ajax(options);
		},
		setupTypeAheadWithWidget : function() {
			var me = this;
			var fullData = {};
			var sourceCb = function(query, processCb) {
				var data = {
					query : query,
					start : "0",
					maxSize : "25"
				};
				me.getDataFromServerCb(data, fullData, processCb, processCb);
			};
			var matcherCb = function(item) {
				console.log("Matches the item " + item + " ?");
				return true;
			};
			var highlighterCb = function(item) {
				console.log("Change the html of the item");
				return "<h1>" + item + "</h1>";
			};
			var updateCb = function(item) {
				console.log("Update cb");
				return "user option :" + item;
			};
			var config = {
				selector : "#dynamicTypeAheadWidget",
				minLength : 3,
				sourceCb : {
					cb : sourceCb,
					waitTime : 0.5
				},
				matcherCb : matcherCb,
				highlighterCb : highlighterCb,
				updateCb : updateCb
			};
			var typeAheadConfig = imperium_ui_widget_TypeAhead.create(config);
		}
	};

	return {
		module : module
	};
}();

jQuery(function($) {
	var module = typeAhead.module;
	module.init();
});