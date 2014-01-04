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
package ar.com.imperium.web.app.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.com.imperium.common.DataTablesPluginHelper;
import ar.com.imperium.common.IPropertiesHelper;
import ar.com.imperium.common.PaginationHelper;
import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.SpringFrameworkHelper;
import ar.com.imperium.common.ValidationHelper;
import ar.com.imperium.common.helper.LoggingHelper;
import ar.com.imperium.common.helper.PermissionHelper;
import ar.com.imperium.common.json.IEntityEncoder;
import ar.com.imperium.common.json.IJsonHelper;
import ar.com.imperium.common.json.ServerResponse;
import ar.com.imperium.common.string.StringSplitter;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.exception.domain.ApplicationNotFound;
import ar.com.imperium.exception.domain.PermissionNotFound;
import ar.com.imperium.repository.jpa.PermissionRepository;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IPermissionService;
import ar.com.imperium.service.interfaces.IRoleService;

@Controller
public class PermissionController {
	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(PermissionController.class);

	@Autowired
	@Qualifier("paginationHelper")
	private PaginationHelper paginationHelper;

	/** The spring helper. */
	@Autowired
	@Qualifier("springHelper")
	private SpringFrameworkHelper springHelper;

	@Autowired
	@Qualifier("gsonJsonHelper")
	private IJsonHelper jsonHelper;

	@Autowired
	@Qualifier("depthApplicationSerializer")
	private IEntityEncoder applicationEncoder;

	@Autowired
	@Qualifier("servletHelper")
	private ServletHelper servletHelper;

	@Autowired
	@Qualifier("validationHelper")
	private ValidationHelper validationHelper;

	@Autowired
	@Qualifier("permissionHelper")
	private PermissionHelper permissionHelper;
	
	@Autowired
	@Qualifier("dataTablesHelper")
	private DataTablesPluginHelper dataTablesHelper;

	@Autowired
	@Qualifier("stringSplitter")
	private StringSplitter stringSplitter;
	
	/** The application service. */
	@Autowired
	@Qualifier("jpaComplexApplicationService")
	private IApplicationService applicationService;

	/** Role service **/
	@Autowired
	@Qualifier("jpaComplexRoleService")
	private IRoleService roleService;

	/* Permission service */
	@Autowired
	@Qualifier("jpaComplexPermissionService")
	private IPermissionService permissionService;

	@Autowired
	@Qualifier("applicationPropertiesHelper")
	private IPropertiesHelper propertiesHelper;

	@RequestMapping(value = "/webapp/permission/list-available-role.json", method = RequestMethod.GET)
	public void handleJsonListAvailableForRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);
		String query = dataTablesHelper.getSearchParam(dataTableParams);

		Long applicationId = servletHelper.readLongFromRequest(request,
				"applicationId", new Long(-1));

		Long roleId = servletHelper.readLongFromRequest(request, "roleId",
				new Long(-1));

		Role role = roleService.findById(roleId);
		Application application = applicationService.findById(applicationId);

		if (application == null) {
			throw new ImperiumException("Application not found with id :"
					+ applicationId, true);
		} else if (role == null) {
			throw new ImperiumException("Role not found with id :" + roleId,
					true);
		}
		// list available for application and not assigned in the role
		this.subJsonListAvailableForRole(request, response);
	}

	@RequestMapping(value = "/webapp/permission/list-for-role.json", method = RequestMethod.GET)
	public void handleJsonListForRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);
		String query = dataTablesHelper.getSearchParam(dataTableParams);

		Long applicationId = servletHelper.readLongFromRequest(request,
				"applicationId", new Long(-1));

		Long roleId = servletHelper.readLongFromRequest(request, "roleId",
				new Long(-1));

		Role role = roleService.findById(roleId);
		Application application = applicationService.findById(applicationId);

		if (application == null) {
			throw new ImperiumException("Application not found with id "
					+ applicationId, true);
		} else if (role == null) {
			throw new ImperiumException("Role not found with id:" + roleId,
					true);
		}

		if (query != null) {
			subJsonListForRoleWhere(request, response);
		} else {
			subJsonListForRole(request, response);
		}
	}

	@RequestMapping(value = "/webapp/permission/edition/{id}", method = RequestMethod.GET)
	public ModelAndView seeEditForm(@PathVariable("id") Long id,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long applicationId = servletHelper.readLongFromRequest(request,
				"applicationId", new Long(-1));

		Permission permission = permissionService.findById(id);
		Application application = applicationService.findById(applicationId);

		if (permission == null) {
			throw new PermissionNotFound("id", id);
		} else if (application == null) {
			throw new ApplicationNotFound("id", applicationId);
		} else {
			ModelAndView modelAndView = new ModelAndView("permission/form");

			Map<String, Object> modelMap = new HashMap<String, Object>();
			modelMap.put("applicationId", applicationId);
			modelMap.put("name", application.getName());
			modelMap.put("description", application.getDescription());
			modelMap.put("resource", permission.getResource());
			modelMap.put("action", permission.getAction());

			modelAndView.addAllObjects(modelMap);

			return modelAndView;
		}
	}

	private Map<String, String> getErrorsForCreate(Map<String, Object> params)
			throws Exception {

		Map<String, String> answer = new HashMap<String, String>();

		Long applicationId = (Long) params.get("applicationId");
		String resource = (String) params.get("resource");
		String action = (String) params.get("action");

		Application application = applicationService.findById(applicationId);

		Permission permission = null;

		if (application == null) {
			answer.put("application", "Application with id not found");
		} else {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("applicationId", application.getId());
			queryParams.put("resource", resource);
			queryParams.put("action", action);
			permission = permissionService
					.findAllWhereNameResourceApplication(queryParams);
			if (permission != null) {
				answer.put("resource", "The resource already exist");
			}
		}

		return answer;
	}

	@ExceptionHandler(ImperiumException.class)
	public void handleException(ImperiumException exception,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String message = exception.getMessage();
		
		Map<String, Object> serverResponse = new HashMap<String, Object>();
		serverResponse.put("sucess", false);
		serverResponse.put("exceptionMessage", message);
		serverResponse.put("hasMap", true);
		serverResponse.put("map", exception.getErrorMap());
		if (exception.hasErrorMap()) {
			Map<String, String> errorMap = exception.getErrorMap();
			Set<String> keySet = errorMap.keySet();
			for (String stringKey : keySet) {
				serverResponse.put(stringKey, errorMap.get(stringKey));
			}
		}
		
		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	private void subCreateSinglePermission(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> params)
			throws Exception {

		String resource = (String) params.get("resource");
		String action = (String) params.get("action");
		String idString = (String) params.get("applicationId");
		Long applicationId = Long.parseLong(idString);

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("applicationId", applicationId);
		paramsMap.put("resource", resource);
		paramsMap.put("action", action);
		Map<String, String> errors = this.getErrorsForCreate(paramsMap);

		if (!errors.isEmpty()) {
			Set<String> keySet = errors.keySet();
			String firstKey = keySet.iterator().next();
			String message = errors.get(firstKey);
			throw new ImperiumException(message, true);
		}

		Permission permission = new Permission();
		permission.setAction(action);
		permission.setResource(resource);
		Application application = applicationService.findById(applicationId);
		permission.setApplication(application);

		Permission created = permissionService.create(permission);

		ServerResponse serverResponse = new ServerResponse(true, created, -1,
				-1, "", -1, -1);
		serverResponse.addToExtraData("applicationId", applicationId);

		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param params
	 * @throws Exception
	 */
	private void subCreateListPermissions(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> params)
			throws Exception {
		
		String resource = (String) params.get("resource");
		String action = (String) params.get("action");
		String idString = (String) params.get("applicationId");
		Long applicationId = Long.parseLong(idString);
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("applicationId", applicationId);
		paramsMap.put("resource", resource);
		paramsMap.put("action", action);
		
		Map<String, String> errorMap = this.getErrorsForCreateManyPermissions(paramsMap);
		if (!errorMap.isEmpty()) {
			throw new ImperiumException("Error creating permissions", true, errorMap);
		}
		
		Application application = applicationService.findById(applicationId);
		List<Permission> permissionList = new ArrayList<Permission>();
		List<String> actionNamesList = stringSplitter.getListFromSplit(action, ",");
		Permission eachPermission;
		for (String eachName : actionNamesList) {
			eachPermission = new Permission(resource, eachName);
			eachPermission.setApplication(application);
			permissionList.add(eachPermission);
		}
		permissionService.create(permissionList);
		
		List<Map<String, Object>> mapList = permissionHelper.getMapListFromPermissions(permissionList);
		ServerResponse serverResponse= new ServerResponse(true, mapList, -1,
				-1, "", -1, -1);
		serverResponse.addToExtraData("createdList", true);
		serverResponse.addToExtraData("applicationId", applicationId);
		serverResponse.addToExtraData("list", mapList);
		
		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
		
	}

	private Map<String, String> getErrorsForCreateManyPermissions(Map<String, Object> params) throws Exception 
	{
		Map<String, String> answer = new HashMap<String, String>();
		
		Long applicationId = (Long) params.get("applicationId");
		String resource = (String) params.get("resource");
		String action = (String) params.get("action");
		
		Application application = applicationService.findById(applicationId);
		
		if (application == null) {
			answer.put("application", "Application with id not found");
		} else {
			List<String> actionList = stringSplitter.getListFromSplit(action, ",");
			List<Permission> permissionList = permissionService.findAllWithResourceAction(applicationId, resource, actionList);
			List<Map<String, Object>> mapList = permissionHelper.getMapListFromPermissions(permissionList);
			if (permissionList.size()>=1) {
				String permissionsString = permissionHelper.convertToString(permissionList);
				answer.put("resource", "The following permissions already exist :"+permissionsString);
			}
		}
		
		return answer;
	}
	
	private void subCreatePermission(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String jsonRequest = servletHelper.readJsonRequest(request);
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

		String action = (String) params.get("action");
		String idString = (String) params.get("applicationId");
		
		if (validationHelper.isValidWordList(action)) {
			this.subCreateListPermissions(request, response, params);
		} else if (validationHelper.isValidWord(action)) {
			this.subCreateSinglePermission(request, response, params);
		} else {
			throw new ImperiumException("The action input is invalid ");
		}
	}

	@RequestMapping(value = "/webapp/permission/create.json", method = RequestMethod.POST)
	public void handlePermissionCreation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		this.subCreatePermission(request, response);
	}

	@RequestMapping(value = "/webapp/permission/list-where.json", method = RequestMethod.POST)
	public void handlePermissionListWhere(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String jsonRequest = servletHelper.readJsonRequest(request);
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

		String resourcePrefix = (String) params.get("resource.prefix");
		String actionPrefix = (String) params.get("action.prefix");

		if (resourcePrefix != null) {
			// list all permissions where resources contains PREFIX*
			handlePermissionListWithResourcePrefix(jsonRequest, response);
		} else if (actionPrefix != null) {
			// list all permissions where resources actions contains PREFIX*
			handlePermissionListWithActionPrefix(jsonRequest, response);
		}
	}

	@RequestMapping(value = "/webapp/permission/create/{applicationId}", method = RequestMethod.GET)
	public ModelAndView seeAddForm(
			@PathVariable("applicationId") Long applicationId) throws Exception {
		Application application = applicationService.findById(applicationId);

		if (application == null) {
			throw new ApplicationNotFound("id", applicationId);
		} else {
			ModelAndView modelAndView = new ModelAndView("permission/form");

			modelAndView.addObject("applicationId", applicationId);
			modelAndView.addObject("name", application.getName());
			modelAndView.addObject("description", application.getDescription());

			modelAndView.addObject("id", new Long(-1));
			modelAndView.addObject("resource", new Long(-1));
			modelAndView.addObject("action", new Long(-1));

			return modelAndView;
		}
	}

	@RequestMapping("/webapp/permission/list.json/{applicationId}")
	public void handleJsonListWithPagination(
			@PathVariable("applicationId") Long applicationId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Application application = applicationService.findById(applicationId);

		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);
		String query = dataTablesHelper.getSearchParam(dataTableParams);

		Map<String, Object> answer = new HashMap<String, Object>();
		List<Permission> permissionList = null;
		Integer total;
		if (application == null) {
			throw new ApplicationNotFound("id", applicationId);
		} else if (query != null) {

			handleAjaxPermissionListForApplicationWhere(applicationId, request,
					response);

		} else {

			handleAjaxPermissionListForApplication(applicationId, request,
					response);

		}
	}

	/**
     * 
     */

	/**
	 * @param id
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/permission/ajax/get/{id}", method = RequestMethod.GET)
	public void getPermissionInformation(@PathVariable Long id,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Permission permission = permissionService.findById(id);

		if (permission == null) {
			throw new RuntimeException("The permission doesnt exist");
		} else {
			ServerResponse serverResponseInstance = new ServerResponse(true,
					permission, -1, -1, "", -1, -1);

			String jsonAnswer = jsonHelper.toJson(serverResponseInstance);

			servletHelper.setJsonResponse(response);
			response.setStatus(response.SC_OK);

			PrintWriter out = response.getWriter();
			out.print(jsonAnswer);
			out.flush();

		}

	}

	@RequestMapping(value = "/webapp/permission/removal-from-app.json", method = RequestMethod.POST)
	public void handlePermissionRemovalFromAppWithAjax(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String jsonRequest = servletHelper.readJsonRequest(request);
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

		String applicationIdString = (String) params.get("applicationId");
		Long applicationId = Long.parseLong(applicationIdString);

		List<String> permissionsIds = (List<String>) params.get("permissions");
		List<Permission> toRemove = new ArrayList<Permission>();
		Long eachId;
		Permission eachPermission;
		StringBuffer sBuffer = new StringBuffer();
		for (String longString : permissionsIds) {
			eachId = Long.parseLong(longString);
			eachPermission = Permission.createWithId(eachId);
			sBuffer.append(longString);
			sBuffer.append(",");
			toRemove.add(eachPermission);
		}

		Application application = applicationService.findById(applicationId);
		if (application == null) {
			throw new ApplicationNotFound("id", applicationId, true);
		}

		applicationService.removePermissions(application, toRemove);

		Map<String, String> answer = new HashMap<String, String>();
		answer.put("applicationId", applicationIdString);
		answer.put("permissions", sBuffer.toString());
		ServerResponse serverResponse = new ServerResponse(true, answer, -1,
				-1, "", -1, -1);

		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	@RequestMapping(value = "/webapp/permission/edit.json/{id}", method = RequestMethod.POST)
	public void handleApplicationEditionWithAjax(@PathVariable("id") Long id,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String jsonRequest = servletHelper.readJsonRequest(request);
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

		String resource = (String) params.get("resource");
		String action = (String) params.get("action");

		Permission permission = permissionService.findById(id);

		ServerResponse serverResponse = null;
		if (permission == null) {
			serverResponse = ServerResponse.createFailureResponse(-1, -1,
					"The permission doesn't exist");
		} else {

			permission.setAction(action);
			permission.setResource(resource);
			permissionService.update(permission);

			serverResponse = new ServerResponse(true, permission, -1, -1, "",
					-1, -1);
		}

		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	private void handleAjaxPermissionListForApplicationWhere(
			Long applicationId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Application application = applicationService.findById(applicationId);
		List<Permission> permissionList = new ArrayList<Permission>();
		Integer integerQty = null;

		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);

		Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
		Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
		Integer verificationNumber = dataTablesHelper
				.getVerificationString(dataTableParams);
		String verificationString = verificationNumber.toString();

		Map<String, String> sortParams = dataTablesHelper
				.getSortingCriteria(dataTableParams);
		String sort = sortParams.get("name");
		String direction = sortParams.get("direction");

		String query = dataTablesHelper.getSearchParam(dataTableParams);

		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("query", query);

		Long qty = permissionService.findQtyForApplicationWhere(application,
				queryParams);
		integerQty = Integer.parseInt(qty.toString());

		Integer page = paginationHelper
				.getPageFromOffset(offset, pageSize, qty);

		Map<String, Object> pagination = new HashMap<String, Object>();
		pagination.put("page", page);
		pagination.put("maxSize", pageSize);

		Map<String, Object> order = new HashMap<String, Object>();
		order.put("sort", sort);
		order.put("direction", direction);

		permissionList = permissionService.findAllForApplicationWhere(
				application, queryParams, pagination, order);

		ServerResponse serverResponse = ServerResponse
				.createAnswerForDataTable(permissionList, integerQty, offset,
						verificationString);

		String jsonAnswer = jsonHelper.toJson(serverResponse);

		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();

	}

	/**
	 * Return a json with the permissions for an application
	 * 
	 * @param applicationId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private void handleAjaxPermissionListForApplication(Long applicationId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Application application = applicationService.findById(applicationId);
		List<Permission> permissionList = new ArrayList<Permission>();
		Integer integerQty = null;

		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);

		Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
		Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
		Integer verificationNumber = dataTablesHelper
				.getVerificationString(dataTableParams);
		String verificationString = verificationNumber.toString();

		Map<String, String> sortParams = dataTablesHelper
				.getSortingCriteria(dataTableParams);
		String sort = sortParams.get("name");
		String direction = sortParams.get("direction");

		Long qty = permissionService.findQtyForApplication(application);
		integerQty = Integer.parseInt(qty.toString());

		Integer page = paginationHelper
				.getPageFromOffset(offset, pageSize, qty);

		permissionList = permissionService.findAllForApplication(application,
				page, pageSize, sort, direction);

		ServerResponse serverResponse = ServerResponse
				.createAnswerForDataTable(permissionList, integerQty, offset,
						verificationString);

		String jsonAnswer = jsonHelper.toJson(serverResponse);

		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	private void subJsonListForRoleWhere(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);

		String query = dataTablesHelper.getSearchParam(dataTableParams);

		Long roleId = servletHelper.readLongFromRequest(request, "roleId",
				new Long(-1));

		Role role = roleService.findById(roleId);

		Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
		Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
		Integer verificationNumber = dataTablesHelper
				.getVerificationString(dataTableParams);
		String verificationString = verificationNumber.toString();

		Map<String, String> sortParams = dataTablesHelper
				.getSortingCriteria(dataTableParams);
		String sort = sortParams.get("name");
		String direction = sortParams.get("direction");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("query", query);
		queryParams.put("roleId", role.getId());

		Long qty = roleService.findQtyForPermissionsForRole(queryParams);
		Integer integerQty = Integer.parseInt(qty.toString());
		List<Permission> permissionList = new ArrayList<Permission>();
		if (qty >= 1) {
			Integer page = paginationHelper.getPageFromOffset(offset, pageSize,
					qty);

			Map<String, Object> pagination = new HashMap<String, Object>();
			pagination.put("page", page);
			pagination.put("maxSize", pageSize);

			Map<String, Object> order = new HashMap<String, Object>();
			order.put("sort", sort);
			order.put("direction", direction);

			permissionList = roleService.findPermissionForRole(queryParams,
					pagination, order);
		}

		ServerResponse serverResponse = ServerResponse
				.createAnswerForDataTable(permissionList, integerQty, offset,
						verificationString);

		String jsonAnswer = jsonHelper.toJson(serverResponse);

		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	private void subJsonListForRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);

		String query = dataTablesHelper.getSearchParam(dataTableParams);
		Long applicationId = servletHelper.readLongFromRequest(request,
				"applicationId", new Long(-1));

		Long roleId = servletHelper.readLongFromRequest(request, "roleId",
				new Long(-1));

		Role role = roleService.findById(roleId);
		Application application = applicationService.findById(applicationId);

		Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
		Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
		Integer verificationNumber = dataTablesHelper
				.getVerificationString(dataTableParams);
		String verificationString = verificationNumber.toString();

		Map<String, String> sortParams = dataTablesHelper
				.getSortingCriteria(dataTableParams);
		String sort = sortParams.get("name");
		String direction = sortParams.get("direction");

		Long qty = roleService.findPermissionQty(role);
		Integer integerQty = Integer.parseInt(qty.toString());
		List<Permission> permissionList = new ArrayList<Permission>();
		if (qty >= 1) {
			Integer page = paginationHelper.getPageFromOffset(offset, pageSize,
					qty);

			Map<String, Object> pagination = new HashMap<String, Object>();
			pagination.put("page", page);
			pagination.put("maxSize", pageSize);

			Map<String, Object> order = new HashMap<String, Object>();
			order.put("sort", sort);
			order.put("direction", direction);

			permissionList = roleService.findPermissions(role, pagination,
					order);
		}

		ServerResponse serverResponse = ServerResponse
				.createAnswerForDataTable(permissionList, integerQty, offset,
						verificationString);

		String jsonAnswer = jsonHelper.toJson(serverResponse);

		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();

	}

	private void subJsonListAvailableForRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);

		String query = dataTablesHelper.getSearchParam(dataTableParams);
		Long applicationId = servletHelper.readLongFromRequest(request,
				"applicationId", new Long(-1));

		Long roleId = servletHelper.readLongFromRequest(request, "roleId",
				new Long(-1));

		Role role = roleService.findById(roleId);
		Application application = applicationService.findById(applicationId);

		Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
		Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
		Integer verificationNumber = dataTablesHelper
				.getVerificationString(dataTableParams);
		String verificationString = verificationNumber.toString();

		Map<String, String> sortParams = dataTablesHelper
				.getSortingCriteria(dataTableParams);
		String sort = sortParams.get("name");
		String direction = sortParams.get("direction");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("applicationId", application.getId());
		queryParams.put("roleId", role.getId());
		if (query != null) {
			queryParams.put("query", query);
		}

		Long qty = permissionService
				.findQtyForAvailableForRoleInApplication(queryParams);

		Integer integerQty = Integer.parseInt(qty.toString());

		List<Permission> permissionList = new ArrayList<Permission>();
		if (qty >= 1) {
			Integer page = paginationHelper.getPageFromOffset(offset, pageSize,
					qty);

			Map<String, Object> pagination = new HashMap<String, Object>();
			pagination.put("page", page);
			pagination.put("maxSize", pageSize);

			Map<String, Object> order = new HashMap<String, Object>();
			order.put("sort", sort);
			order.put("direction", direction);

			permissionList = permissionService
					.findAvailableForRoleInApplication(queryParams, pagination,
							order);
		}

		ServerResponse serverResponse = ServerResponse
				.createAnswerForDataTable(permissionList, integerQty, offset,
						verificationString);

		String jsonAnswer = jsonHelper.toJson(serverResponse);

		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	@RequestMapping(value = "/permission/list/{applicationId}", method = RequestMethod.GET)
	public ModelAndView handlePermissionListWithPagination(
			@PathVariable("applicationId") Long applicationId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Application application = applicationService.findById(applicationId);

		if (application == null) {
			throw new ApplicationNotFound("id", applicationId);
		} else {
			ModelAndView answer = new ModelAndView("permission/list");
			answer.addObject("applicationId", application.getId());
			answer.addObject("applicationName", application.getName());
			return answer;
		}

	}

	private void handlePermissionListWithResourcePrefix(String jsonRequest,
			HttpServletResponse response) throws Exception {
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);
		String applicationIdString = (String) params.get("applicationId");
		Long applicationId = Long.parseLong(applicationIdString);

		Integer page = Integer.parseInt((String) params.get("page"));
		Integer maxSize = Integer.parseInt((String) params.get("maxSize"));

		String sort = (String) params.get("sort");
		String direction = (String) params.get("direction");

		String resourcePrefix = (String) params.get("resource.prefix");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("applicationId", applicationId);
		queryParams.put("resource.prefix", resourcePrefix);

		Map<String, Object> pagination = new HashMap<String, Object>();
		pagination.put("page", page);
		pagination.put("maxSize", maxSize);

		Map<String, Object> order = new HashMap<String, Object>();
		order.put("sort", sort);
		order.put("direction", direction);

		// process the request
		List<Permission> permissionList = permissionService
				.findAllWhereResource(queryParams, pagination, order);

		// return the json to the server
		Map<String, Object> answer = new HashMap<String, Object>();
		answer.put("success", true);
		answer.put("permissionList", permissionList);
		answer.put("applicationId", applicationId);
		answer.put("resource.prefix", resourcePrefix);

		String jsonAnswer = jsonHelper.toJson(answer);
		servletHelper.setJsonResponse(response);
		response.setStatus(response.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	private void handlePermissionListWithActionPrefix(String jsonRequest,
			HttpServletResponse response) throws Exception {
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

		String applicationIdString = (String) params.get("applicationId");
		Long applicationId = Long.parseLong(applicationIdString);

		Integer page = Integer.parseInt((String) params.get("page"));
		Integer maxSize = Integer.parseInt((String) params.get("maxSize"));

		String sort = (String) params.get("sort");
		String direction = (String) params.get("direction");

		String resourcePrefix = (String) params.get("resource.prefix");
		String actionPrefix = (String) params.get("action.prefix");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("applicationId", applicationId);
		queryParams.put("action.prefix", actionPrefix);

		Map<String, Object> pagination = new HashMap<String, Object>();
		pagination.put("page", page);
		pagination.put("maxSize", maxSize);

		Map<String, Object> order = new HashMap<String, Object>();
		order.put("sort", sort);
		order.put("direction", direction);
		// process the request
		List<Permission> permissionList = permissionService.findAllWhereAction(
				queryParams, pagination, order);
		// return the json to the server
		Map<String, Object> answer = new HashMap<String, Object>();
		answer.put("answer", true);
		answer.put("permissionList", permissionList);
		answer.put("applicationId", applicationId);
		answer.put("action.prefix", actionPrefix);

		String jsonAnswer = jsonHelper.toJson(answer);
		servletHelper.setJsonResponse(response);
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

}
