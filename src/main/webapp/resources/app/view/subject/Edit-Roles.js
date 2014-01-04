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
var imperium_ui_view_application_EditRoles2 = function() {
    var urlManager = imperium_ui_utils_UrlManager.module;

    var module = {
	data : null,
	init : function() {
	    //getData

	    // init data for the module
	    module.setData();

	    // build available table
	    module.buildAvailableTable();

	    // build config table
	    module.buildConfigTable();

	    // enable selection in tables
	    module.enableSelectionInTable();

	    // setup button listeners
	    module.setupButtonListeners();

	    // set width of search inputs to 100
	    module.fixSearchInputs();
	},
	getSearchAvailableQuery : function() {
	    var input = $("#searchAvailableButton").closest("form").find(
		    "input");
	    return input.val();
	},
	getSearchSelectionQuery : function() {
	    var input = $("#searchSelectionConfig").closest("form").find(
		    "input");
	    return input.val();
	},
	/**
	 * Table method helpers
	 * 
	 * @param event
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
	reloadTable : function(tableSelector) {
	    var table = $(tableSelector).dataTable();
	    table.fnDraw();
	},
	selectAllInTableCb : function(tableSelector, selectionClass) {
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
	/**
	 * 
	 */
	onAddAllClicked : function(event) {
	    event.preventDefault();

	    this.selectAllInTableCb(this.data.selector.available, "success");

	    var rawSelection = this
		    .getAllRecordsFromTable(this.data.selector.available);

	    if (rawSelection.length >= 1) {
		var selection = this.filterSelection(rawSelection);
		this.performAddOperation(selection);
	    }
	},
	performAddOperation : function(selection) {
	    var me = this;

	    var toAdd = [];
	    jQuery.each(selection, function(index, value) {
		toAdd.push(value["id"].toString());
	    });

	    var dataToSend = {
		"subjectId" : this.data.subject.id.toString(),
		"add" : toAdd,
		"remove" : []
	    };

	    imperium_ui_dao_Subject.updateRoles(dataToSend, function(response) {
		me.reloadTable(me.data.selector.available);
		me.reloadTable(me.data.selector.selection);
	    }, function(failure) {

	    });
	},
	addSelected : function() {
	    var rawSelection = this.getTableSelection(
		    this.data.selector.available, "success");
	    var selection = this.filterSelection(rawSelection);
	    this.performAddOperation(selection);
	},
	onAddSelectedButton : function(event) {
	    event.preventDefault();
	    var rawSelection = this.getTableSelection(
		    this.data.selector.available, "success");

	    if (rawSelection.length >= 1) {
		this.addSelected();
	    }
	},
	onSearchAvailable : function(event) {
	    event.preventDefault();

	    var toSearch = this.getSearchAvailableQuery();

	    this.reloadTable(this.data.selector.available);
	},
	onSearchSelection : function(event) {
	    event.preventDefault();

	    this.reloadTable(this.data.selector.selection);
	},
	doRemoval : function(selection) {
	    var me = this;

	    var toRemove = [];
	    jQuery.each(selection, function(index, value) {
		toRemove.push(value["id"].toString());
	    });
	    var dataToSend = {
		"subjectId" : this.data.subject.id.toString(),
		"add" : [],
		"remove" : toRemove
	    };

	    imperium_ui_dao_Subject.updateRoles(dataToSend, function(response) {
		me.reloadTable(me.data.selector.available);
		me.reloadTable(me.data.selector.selection);
	    }, function(error) {

	    });
	},
	onRemoveButton : function(event) {
	    var me = this;
	    event.preventDefault();

	    var rawSelection = this.getTableSelection(
		    this.data.selector.selection, "error");

	    if (rawSelection.length >= 1) {
		bootbox.confirm(
			"Are you sure you want to remove the selection",
			function(result) {
			    if (result === true) {
				var selection = me
					.filterSelection(rawSelection);
				me.doRemoval(selection);
			    }
			});
	    }
	},
	onRemoveAll : function(event) {
	    var me = this;
	    event.preventDefault();

	    var rawSelection = this
		    .getAllRecordsFromTable(me.data.selector.selection);
	    if (rawSelection.length >= 1) {
		bootbox.confirm(
			"Are you sure you want to remove the selection",
			function(result) {
			    if (result === true) {
				var selection = me
					.filterSelection(rawSelection);
				me.selectAllInTableCb(
					me.data.selector.selection, "error");
				me.doRemoval(selection);
			    }
			});
	    }
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
		event.preventDefault();
		module.onSearchSelection(event);
	    });
	    $('button[name="goBack"]').on("click", function(event) {
		event.preventDefault();
		module.onGoBack();
	    });
	},
	onGoBack : function() {
	    window.location = urlManager.getUrlForAppShowEdit(
		    this.data.application.id, "SUBJECT");
	},
	enableSelectionInTable : function() {
	    $(this.data.selector.available + " tr").live("click",
		    function(event) {
			$(this).toggleClass("success");
		    });

	    $(this.data.selector.selection + " tr").live("click",
		    function(event) {
			$(this).toggleClass("error");
		    });
	},
	getData : function() {
	    var information = $("#information");
	    return {
		selector : {
		    available : "#showAvailableTable",
		    selection : "#selectionTable"
		},
		application : {
		    id : information.attr("data-application-id"),
		    name : information.attr("data-application-name"),
		    description : information
			    .attr("data-application-description")
		},
		subject : {
		    id : information.attr("data-subject-id"),
		    name : information.attr("data-subject-name")
		}
	    };
	},
	setData : function() {
	    module.data = module.getData();
	},
	buildAvailableTable : function() {
	    var me = this;

	    var tableSelector = module.data.selector.available;

	    var columnDefinition = [ {
		"mData" : "name",
		"bSortable" : true,
		"bVisible" : true,
		"sWidth" : "25%",
	    }, {
		"mData" : "description",
		"bSortable" : true,
		"bVisible" : true,
		"sWidth" : "25%"
	    } ];

	    var url = urlManager.getUrlForRoleAvailableJsonListForSubject();

	    var method = "GET";

	    var addQueryParams = function(aoData) {
		var toSearch = me.getSearchAvailableQuery();

		aoData.push({
		    "name" : "applicationId",
		    "value" : module.data.application.id.toString()
		});
		aoData.push({
		    "name" : "subjectId",
		    "value" : module.data.subject.id.toString()
		});
		aoData.push({
		    "name" : "sSearch",
		    "value" : toSearch
		});
	    };

	    var tableConfig = {
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerParams" : addQueryParams,
		"sAjaxSource" : url,
		"sServerMethod" : method,
		"sAjaxDataProp" : "data",
		"aoColumns" : columnDefinition,
		"sPaginationType" : "bootstrap",
		"aLengthMenu" : [ 10, 10 ],
		"bPaginate" : false,
		"bLengthChange" : false,
		// disable multiple page sorting
		"bsort" : false,
		// width of the columns is automatic
		"bAutoWidth" : false,
		// remove the search box here
		"bFilter" : false,
		"bSearch" : false,
		// custom paging info
		"oLanguage" : {
		    "sInfo" : "_START_ to _END_ total:_TOTAL_",
		    "oPaginate" : {
			"sPrevious" : "",
			"sNext" : ""
		    },
		    "sLengthMenu" : "_MENU_",
		    "sProcessing" : ""
		},
		"sScrollX" : "50%",
		"bScrollCollapse" : true,
		"sDom" : '<"#top">t<"#bottom"p><"#clear" class="hidden"rf>'
	    };

	    $(tableSelector).dataTable(tableConfig);
	},

	buildConfigTable : function() {
	    var me = this;

	    var tableSelector = module.data.selector.selection;

	    var columnDefinition = [ {
		"mData" : "name",
		"bSortable" : true,
		"bVisible" : true,
		"sWidth" : "25%",
	    }, {
		"mData" : "description",
		"bSortable" : true,
		"bVisible" : true,
		"sWidth" : "25%"
	    } ];

	    var url = urlManager.getUrlForRoleJsonListForSubject();

	    var method = "GET";

	    var addQueryParams = function(aoData) {
		aoData.push({
		    "name" : "applicationId",
		    "value" : module.data.application.id.toString()
		});
		aoData.push({
		    "name" : "subjectId",
		    "value" : module.data.subject.id.toString()
		});
		aoData.push({
		    "name" : "sSearch",
		    "value" : module.getSearchSelectionQuery()
		});
	    };

	    var tableConfig = {
		"bProcessing" : true,
		"bServerSide" : true,
		"fnServerParams" : addQueryParams,
		"sAjaxSource" : url,
		"sServerMethod" : method,
		"sAjaxDataProp" : "data",
		"aoColumns" : columnDefinition,
		"sPaginationType" : "bootstrap",
		"aLengthMenu" : [ 10, 10 ],
		"bPaginate" : false,
		"bLengthChange" : false,
		// disable multiple page sorting
		"bsort" : false,
		// width of the columns is automatic
		"bAutoWidth" : false,
		// remove the search box here
		"bFilter" : false,
		"bSearch" : false,
		// custom paging info
		"oLanguage" : {
		    "sInfo" : "_START_ to _END_ total:_TOTAL_",
		    "oPaginate" : {
			"sPrevious" : "",
			"sNext" : ""
		    },
		    "sLengthMenu" : "_MENU_",
		    "sProcessing" : ""
		},
		"sScrollX" : "50%",
		"bScrollCollapse" : true,
		"sDom" : '<"#top">t<"#bottom"p><"#clear" class="hidden"rf>'
	    };

	    $(tableSelector).dataTable(tableConfig);
	},

	fixSearchInputs : function() {
	    $(".search-query").width(100);
	    $("#clear").hide();
	}
    };

    return {
	init : module.init
    };
}();

jQuery(function($) {
    imperium_ui_view_application_EditRoles2.init();
});
