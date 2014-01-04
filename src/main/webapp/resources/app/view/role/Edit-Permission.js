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
var imperium_ui_view_application_Edit_Permission = function() {
    var logger = imperium_ui_utils_Logger.getLogger();

    var urlManager = imperium_ui_utils_UrlManager.module;
    
    var module = {
	data : null,
	init : function() {
	    this.getData();
	    this.setData();
	    this.buildAvailableTable();
	    this.buildSelectionTable();
	    this.enableSelectionInTable();
	    this.setupButtonListeners();
	},
	getData : function() {
	    var information = $("#information");
	    return {
		application : {
		    id : information.attr("data-application-id")
		},
		role : {
		    id : information.attr("data-role-id")
		}
	    };
	},
	setData : function() {
	    this.data = this.getData();
	},
	onAvailableClicked : function(event, context) {
	    $(context).toggleClass("success");
	},
	onSelectionClicked : function(event, context) {
	    $(context).toggleClass("error");
	},
	enableSelectionInTable : function() {
	    var me = this;
	    // selection for available table
	    $("#showAvailableTable  tr").live("click", function(event) {
		var buttonContext = this;
		globalContext = buttonContext;
		me.onAvailableClicked(event, buttonContext);
	    });
	    // selection for selected table
	    $("#selectionTable tr").live("click", function(event) {
		var buttonContext = this;
		me.onSelectionClicked(event, buttonContext);
	    });
	},
	/**
	 * Helper methods
	 */
	getTableSelection : function(tableSelector, selectedClass) {
	    var selector = tableSelector + " tr." + selectedClass;
	    var selection = $(selector);

	    var table = $(tableSelector).dataTable();
	    var data = table.fnGetData();

	    var answer = [];

	    var trElement;
	    var eachElement;
	    var dataIndex;
	    var eachCb = function(index, element) {
		dataIndex = table.fnGetPosition(element);

		trElement = $(element);

		eachElement = {
		    data : data[dataIndex],
		    html : trElement[0]
		};
		answer.push(eachElement);
	    };
	    selection.each(eachCb);
	    return answer;
	},
	selectAllInTable : function(tableSelector, selectionClass) {
	    $(tableSelector).find("tr").toggleClass(selectionClass, true);
	},
	getAllRecordsFromTable : function(tableSelector) {
	    var table = $(tableSelector).dataTable();
	    var nodes = table.fnGetNodes();
	    var data = table.fnGetData();

	    var answer = [];
	    var eachElement = {};
	    for ( var i in nodes) {
		eachElement = {
		    data : data[i],
		    html : nodes[i]
		};
		answer.push(eachElement);
	    }
	    return answer;
	},
	indexOfElementWith : function(tableSelector, toSearch, properties) {
	    var table = $(tableSelector).dataTable();
	    var data = table.fnGetData();

	    var answer = -1;

	    var propertyName;
	    var foundElement;
	    var index = -1;
	    var eachData;
	    for (index in data) {
		eachData = data[index];

		foundElement = imperium_ui_utils_Util.areEqual(eachData,
			toSearch, properties);

		if (foundElement === true) {
		    answer = index;
		    break;
		}
	    }

	    if (typeof index === "string") {
		answer = parseInt(answer);
	    }

	    return answer;
	},
	addRowToTableCb : function(tableSelection, row) {
	    var table = $(tableSelection).dataTable();
	    table.fnAddData(row);
	},
	removeRowFromTableCb : function(tableSelection, index, cb, redrawTable) {
	    var table = $(tableSelection).dataTable();
	    var answer = table.fnDeleteRow(index, cb, redrawTable);
	    return answer;
	},
	getDataFromTable : function(tableSelector, index) {
	    var table = $(tableSelector).dataTable();
	    var data = table.fnGetData();
	    var answer = null;
	    if (index in data) {
		answer = data[index];
	    }
	    return answer;
	},
	/**
	 * Filter the records in a table with static content using the defined
	 * query
	 */
	filterTableWith : function(tableSelector, query) {
	    var table = $(tableSelector).dataTable();
	    table.fnFilter(query, null, false, false, false, false);
	},
	reloadTable : function(tableSelector) {
	    var table = $(tableSelector).dataTable();
	    table.fnDraw();
	},
	filterSelection : function(rawSelection) {
	    var answer = [];
	    var index;
	    var rawValue;
	    var eachData;

	    var dataIndex;
	    var dataValue;

	    for (index in rawSelection) {
		rawValue = rawSelection[index];
		eachData = rawValue["data"];

		for (dataIndex in eachData) {
		    dataValue = eachData[dataIndex];
		    dataValue[dataIndex] = dataValue.toString();
		}

		answer.push(eachData);
	    }

	    return answer;
	},
	subAddAll : function(event) {
	    var me = this;
	    var rawSelection = this
		    .getAllRecordsFromTable("#showAvailableTable");
	    var selection = this.filterSelection(rawSelection);

	    var toAdd = [];
	    jQuery.each(selection, function(index, value) {
		toAdd.push(value["id"].toString());
	    });
	    var dataToSend = {
		"roleId" : this.data.role.id.toString(),
		"add" : toAdd,
		"remove" : []
	    };

	    imperium_ui_dao_Role.updatePermission(dataToSend,
		    function(response) {
			me.filterTableWith("#availableTable table", "");
			me
				.filterTableWith(
					"#selectionTableContainer table", "");
		    }, function(error) {
			console.log("Error sending the data");
		    });

	},
	/**
	 * Listeners for buttons
	 */
	onAddAllClicked : function(event) {
	    event.preventDefault();
	    var rawSelection = this.getAllRecordsFromTable(
		    "#showAvailableTable", "success");
	    if (rawSelection.length >= 1) {
		this.subAddAll(event);
	    }
	},
	addSelected : function(event) {
	    var me = this;

	    var rawSelection = this.getTableSelection("#availableTable table",
		    "success");

	    var selection = this.filterSelection(rawSelection);
	    /**
	     * Prepare data to send
	     */
	    var toAdd = [];
	    jQuery.each(selection, function(index, value) {
		toAdd.push(value["id"].toString());
	    });
	    var dataToSend = {
		"roleId" : me.data.role.id.toString(),
		"add" : toAdd,
		"remove" : []
	    };
	    /**
	     * 
	     */
	    imperium_ui_dao_Role.updatePermission(dataToSend,
		    function(response) {
			me.reloadTable("#availableTable table");
			me.reloadTable("#selectionTableContainer table");
		    }, function(error) {
			console.log("Error sending the data");
		    });
	},
	onAddSelectedButton : function(event) {
	    event.preventDefault();
	    var rawSelection = this.getTableSelection("#availableTable table",
		    "success");

	    if (rawSelection.length >= 1) {
		this.addSelected(event);
	    }
	},
	onSearchAvailable : function(event) {
	    event.preventDefault();

	    var target = event.target;
	    var input = $(target).closest("form").find("input");
	    var searchValue = input.val();
	    this.filterTableWith("#showAvailableTable", searchValue);
	},
	removeSelected : function(event) {
	    var me = this;

	    var rawSelection = this.getTableSelection("#selectionTable",
		    "error");
	    var selection = this.filterSelection(rawSelection);
	    /**
	     * Prepare data to remove
	     */
	    var toRemove = [];
	    jQuery.each(selection, function(index, value) {
		toRemove.push(value["id"].toString());
	    });
	    var dataToSend = {
		"roleId" : me.data.role.id.toString(),
		"add" : [],
		"remove" : toRemove
	    };
	    imperium_ui_dao_Role.updatePermission(dataToSend,
		    function(response) {
			me.filterTableWith("#availableTable table", "");
			me
				.filterTableWith(
					"#selectionTableContainer table", "");
			// me.reloadTable("#selectionTableContainer table");
		    }, function(error) {
			console.log("Error sending the data");
		    });

	},
	subDoRemoveSelected : function(event) {
	    this.removeSelected(event);
	},
	onRemoveButton : function(event) {
	    event.preventDefault();
	    var me = this;

	    var rawSelection = this.getTableSelection("#selectionTable",
		    "error");
	    if (rawSelection.length >= 1) {
		bootbox.confirm("Are you sure you want to remove the records",
			function(result) {
			    if (result === true) {
				me.subDoRemoveSelected(event);
			    }
			});
	    }
	},
	subDoRemoveAll : function() {
	    var me = this;
	    var rawSelection = this.getAllRecordsFromTable("#selectionTable");
	    var selection = this.filterSelection(rawSelection);

	    /**
	     * Prepare data to remove
	     */
	    var toRemove = [];
	    jQuery.each(selection, function(index, value) {
		toRemove.push(value["id"].toString());
	    });
	    var dataToSend = {
		"roleId" : me.data.role.id.toString(),
		"add" : [],
		"remove" : toRemove
	    };

	    imperium_ui_dao_Role.updatePermission(dataToSend,
		    function(response) {
			me.filterTableWith("#availableTable table", "");
			me
				.filterTableWith(
					"#selectionTableContainer table", "");
		    }, function(error) {
			console.log("Error sending the data");
		    });

	},
	askRemoveAll : function() {
	    var me = this;

	    bootbox.confirm("Are you sure you want to remove all the records",
		    function(result) {
			if (result === true) {
			    me.subDoRemoveAll();
			}
		    });
	},
	onRemoveAll : function(event) {
	    event.preventDefault();

	    var me = this;

	    var rawSelection = this.getAllRecordsFromTable("#selectionTable");

	    if (rawSelection.length >= 1) {
		me.askRemoveAll();
	    }
	},
	onGoBack: function(event) {
	    event.preventDefault();
	    window.location = urlManager.getUrlForAppShowEdit(module.data.application.id, "ROLE");
	},
	subDoSearchSelection : function(value) {
	    this.filterTableWith("#selectionTableContainer table", value);
	},
	onSearchSelection : function(event) {
	    event.preventDefault();

	    var target = event.target;
	    var input = $(target).closest("form").find('input');
	    var searchValue = input.val();
	    this.subDoSearchSelection(searchValue);
	},
	setupButtonListeners : function() {
	    var me = this;
	    $("#addAllButton").on("click", function(event) {
		me.onAddAllClicked(event);
	    });
	    $("#addSelectedButton").on("click", function(event) {
		me.onAddSelectedButton(event);
	    });
	    $("#searchAvailableButton").on("click", function(event) {
		me.onSearchAvailable(event);
	    });
	    $("#removeButton").on("click", function(event) {
		me.onRemoveButton(event);
	    });
	    $("#removeAllButton").on("click", function(event) {
		me.onRemoveAll(event);
	    });
	    $("#searchSelectionConfig").on("click", function(event) {
		me.onSearchSelection(event);
	    });
	    $('button[name="goBack"]').on("click", function(event) {
		me.onGoBack(event);
	    });
	},
	buildAvailableTable : function() {
	    var me = this;

	    var tableSelector = $("#showAvailableTable");

	    var columnDefinition = [ {
		"mData" : "resource",
		"bSortable" : true,
		"bVisible" : true
	    }, {
		"mData" : "action",
		"bSortable" : true,
		"bVisible" : true
	    } ];

	    var url = "http://localhost:8080/imperium/webapp/permission/list-available-role.json";
	    var method = "GET";
	    var addParameterCb = function(aoData) {
		aoData.push({
		    "name" : "applicationId",
		    "value" : me.data.application.id.toString()
		});
		aoData.push({
		    "name" : "roleId",
		    "value" : me.data.role.id.toString()
		});
	    };

	    var tableConfig = {
		"fnServerParams" : addParameterCb,
		"bProcessing" : true,
		"bServerSide" : true,
		"sAjaxSource" : url,
		"sServerMethod" : method,
		"sAjaxDataProp" : "data",
		"aoColumns" : columnDefinition,
		"bsort" : true,
		"oLanguage" : {
		    "sInfo" : "_START_ to _END_ total:_TOTAL_",
		    "oPaginate" : {
			"sPrevious" : "",
			"sNext" : ""
		    },
		    "sProcessing" : ""
		},
		// add pagination
		"sPaginationType" : "bootstrap",
		"aLengthMenu" : [ 10, 10 ],
		"bPaginate" : true,
		"bLengthChange" : false,
		// change layout of the table
		"sDom" : '<"#top">t<"#bottom"p><"#clear" class="hidden"rf>'
	    };

	    var tableInstance = $(tableSelector).dataTable(tableConfig);
	    tableInstance.fnFilterOnReturn();

	    /**
	     * Hide clear div to remove "processing..." message and page X of X
	     * with total X
	     */
	    $("#clear").hide();

	    /**
	     * Move left to the pagination
	     */
	    $("#bottom").find(".dataTables_paginate").css("float", "left");
	},
	buildSelectionTable : function() {
	    var me = this;

	    var tableSelector = $("#selectionTable");

	    var columnDefinition = [ {
		"mData" : "resource",
		"bSortable" : true,
		"bVisible" : true
	    }, {
		"mData" : "action",
		"bSortable" : true,
		"bVisible" : true
	    } ];

	    var url = "/imperium/webapp/permission/list-for-role.json";
	    var method = "GET";
	    var addParameterCb = function(aoData) {
		aoData.push({
		    "name" : "applicationId",
		    "value" : me.data.application.id.toString()
		});
		aoData.push({
		    "name" : "roleId",
		    "value" : me.data.role.id.toString()
		});
	    };

	    var tableConfig = {
		"fnServerParams" : addParameterCb,
		"bProcessing" : true,
		"bServerSide" : true,
		"sAjaxSource" : url,
		"sServerMethod" : method,
		"sAjaxDataProp" : "data",
		"aoColumns" : columnDefinition,
		"bsort" : true,
		"oLanguage" : {
		    "sInfo" : "_START_ to _END_ total:_TOTAL_",
		    "oPaginate" : {
			"sPrevious" : "",
			"sNext" : ""
		    },
		    "sProcessing" : ""
		},
		// add pagination
		"sPaginationType" : "bootstrap",
		"aLengthMenu" : [ 10, 10 ],
		"bPaginate" : true,
		"bLengthChange" : false,
		// change layout of the table
		"sDom" : '<"#top">t<"#bottom"p><"#clear" class="hidden"rf>'
	    };

	    var tableInstance = $(tableSelector).dataTable(tableConfig);
	    tableInstance.fnFilterOnReturn();

	    /**
	     * Hide clear div to remove "processing..." message and page X of X
	     * with total X
	     */
	    $("#selectionTableContainer").find("#clear").hide();
	    /**
	     * Move left to the pagination
	     */
	    $("#selectionTableContainer").find("#bottom").find(
		    ".dataTables_paginate").css("float", "left");
	    /**
	     * Fix the search input elements to fit in a 1024*768 dimension
	     * screen
	     */
	    $(".search-query").width(100);

	},

    };

    return {
	module : module
    };
}();
jQuery(function($) {
    var module = imperium_ui_view_application_Edit_Permission.module;
    module.init();
});
