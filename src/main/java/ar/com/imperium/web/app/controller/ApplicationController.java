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
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.com.imperium.common.DataTablesPluginHelper;
import ar.com.imperium.common.PaginationHelper;
import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.SpringFrameworkHelper;
import ar.com.imperium.common.json.IEntityEncoder;
import ar.com.imperium.common.json.IJsonHelper;
import ar.com.imperium.common.json.ServerResponse;
import ar.com.imperium.common.security.IRandomService;
import ar.com.imperium.domain.Application;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.exception.domain.ApplicationNotFound;
import ar.com.imperium.service.interfaces.IApplicationService;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationController.
 */
@Controller
public class ApplicationController {
	public static final String FROM_SUBJECT = "SUBJECT";
	public static final String FROM_ROLE = "ROLE";
	public static final String FROM_PERMISSION = "PERMISSION";

	public static final String PARAM_NAME_FROM = "from";

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(ApplicationController.class);

	/** The application service. */
	@Autowired
	@Qualifier("jpaComplexApplicationService")
	private IApplicationService applicationService;

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
	@Qualifier("paginationHelper")
	private PaginationHelper paginationHelper;

	@Autowired
	@Qualifier("dataTablesHelper")
	private DataTablesPluginHelper dataTablesHelper;

	@Autowired
	@Qualifier("randomService")
	private IRandomService randomService;

	@RequestMapping(value = "/webapp/application/see-list", method = RequestMethod.GET)
	public ModelAndView seeList() throws Exception {
		ModelAndView modelAndView = new ModelAndView("application/listDef");
		return modelAndView;
	}

	private List<Map<String, Object>> mapApplication(List<Application> appList)
			throws Exception {
		List<Map<String, Object>> answer = new ArrayList<Map<String, Object>>();

		for (Application app : appList) {
			answer.add(app.getAsMap());
		}
		return answer;
	}

	private void subhandleSeeJsonList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

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

		// get app qty
		Long total = applicationService.getApplicationQty();
		Integer integerQty = Integer.parseInt(total.toString());

		// calculate the page
		Integer page = paginationHelper.getPageFromOffset(offset, pageSize,
				total);

		List<Application> applicationList = new ArrayList<Application>();
		applicationList = applicationService.findAll(page, pageSize, sort);

		List<Map<String, Object>> mappedList = mapApplication(applicationList);

		ServerResponse serverResponse = ServerResponse
				.createAnswerForDataTable(mappedList, integerQty, offset,
						verificationString);

		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();

	}

	private void subhandleSeeJsonListWhere(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);
		// get pagination , and verification parameters
		Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
		Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
		Integer verificationNumber = dataTablesHelper
				.getVerificationString(dataTableParams);
		String verificationString = verificationNumber.toString();
		// get query parameter
		String query = dataTablesHelper.getSearchParam(dataTableParams);
		// get sorting criteria
		Map<String, String> sortParams = dataTablesHelper
				.getSortingCriteria(dataTableParams);
		String sort = sortParams.get("name");
		String direction = sortParams.get("direction");

		// get app qty
		Long total = applicationService.getApplicationQty();
		Integer integerQty = Integer.parseInt(total.toString());

		// calculate the page
		Integer page = paginationHelper.getPageFromOffset(offset, pageSize,
				total);

		List<Application> applicationList = new ArrayList<Application>();
		// applicationList = applicationService.findAll(page, pageSize, sort);
		// query params
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("query", query);
		// pagination
		Map<String, Object> pagination = new HashMap<String, Object>();
		pagination.put("page", page);
		pagination.put("maxSize", pageSize);
		// order
		Map<String, Object> order = new HashMap<String, Object>();
		order.put("order", sort);
		order.put("direction", direction);

		applicationList = applicationService.findAllWhere(queryParams,
				pagination, order);

		List<Map<String, Object>> mappedList = mapApplication(applicationList);

		ServerResponse serverResponse = ServerResponse
				.createAnswerForDataTable(mappedList, integerQty, offset,
						verificationString);

		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	@RequestMapping(value = "/webapp/application/see-list.json", method = RequestMethod.GET)
	public void handleJsonUserList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> dataTableParams = dataTablesHelper
				.getDatatableParameters(request);
		String query = dataTablesHelper.getSearchParam(dataTableParams);

		boolean doSimpleSearch = ((query == null) || (query.length() == 0));

		if (doSimpleSearch) {
			this.subhandleSeeJsonList(request, response);
		} else {
			subhandleSeeJsonListWhere(request, response);
		}
	}

	@RequestMapping(value = "/webapp/application/reset-key.json", method = RequestMethod.GET)
	public void handleJsonShowKey(HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttrs)
			throws Exception {
		Long applicationId = servletHelper.readLongFromRequest(request, "id",
				new Long(-1));

		Application application = applicationService.findById(applicationId);

		if (application == null) {
			throw new ApplicationNotFound("id", applicationId);
		} else {
			application = applicationService
					.findOneWithDetailById(applicationId);

			applicationService.resetApiKey(applicationId);

			Map<String, Object> asMap = application.getAsMap();
			asMap.put("success", true);

			String jsonAnswer = jsonHelper.toJson(asMap);
			servletHelper.setJsonResponse(response);
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			out.print(jsonAnswer);
		}
	}

	@RequestMapping(value = "/webapp/application/get.json", method = RequestMethod.GET)
	public void handleJsonShowInformation(HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttrs)
			throws Exception {
		Long applicationId = servletHelper.readLongFromRequest(request, "id",
				new Long(-1));

		Application application = applicationService.findById(applicationId);

		if (application == null) {
			throw new ApplicationNotFound("id", applicationId);
		} else {
			application = applicationService
					.findOneWithDetailById(applicationId);

			Map<String, Object> asMap = application.getAsMap();
			asMap.put("success", true);
			asMap.put("apiKey", application.getApiKey());

			String jsonAnswer = jsonHelper.toJson(asMap);
			servletHelper.setJsonResponse(response);
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			out.print(jsonAnswer);
			out.flush();
		}
	}

	@RequestMapping(value = "/webapp/application/create.json", method = RequestMethod.POST)
	public void handleApplicationCreationWithCors(HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttrs)
			throws Exception {
		String jsonRequest = servletHelper.readJsonRequest(request);
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

		String name = (String) params.get("name");
		String description = (String) params.get("description");

		Application application = new Application(name, description,
				randomService.generateRandomString(64));
		Application created = applicationService.create(application);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("applicationId", created.getId());
		data.put("name", created.getName());
		data.put("description", created.getDescription());

		ServerResponse serverResponse = new ServerResponse(true, data, -1, -1,
				"", -1, -1);

		String jsonAnswer = jsonHelper.toJson(serverResponse);
		servletHelper.setJsonResponse(response);
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	private void subhandleApplicationEdition(Map<String, Object> params,
			HttpServletResponse response) throws Exception {
		String name = (String) params.get("name");
		String description = (String) params.get("description");

		String idAsString = (String) params.get("id");
		Long id = Long.parseLong(idAsString);

		Application app = applicationService.findOneWithDetailById(id);
		app.setDescription(description);
		app.setName(name);

		Application managed = applicationService.update(app);

		Map<String, Object> asMap = managed.getAsMap();
		asMap.put("success", true);

		String jsonAnswer = jsonHelper.toJson(asMap);
		servletHelper.setJsonResponse(response);
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	@RequestMapping(value = "/webapp/application/edit.json", method = RequestMethod.POST)
	public void handleApplicationJsonEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String jsonRequest = servletHelper.readJsonRequest(request);
		Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

		String idAsString = (String) params.get("id");
		Long id = Long.parseLong(idAsString);

		Application application = applicationService.findById(id);

		if (application == null) {
			throw new ApplicationNotFound("id", id);
		} else {
			subhandleApplicationEdition(params, response);
		}
	}

	@RequestMapping(value = "/webapp/application/get/{id}", method = RequestMethod.GET)
	public void getApplicationInformation(@PathVariable Long id,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Application app = applicationService.findById(id);

		if (app == null) {
			throw new ApplicationNotFound("id", id);
		} else {
			app = applicationService.findById(id);
			ServerResponse serverResponse = new ServerResponse(true, app, -1,
					-1, "", -1, -1);

			String jsonAnswer = applicationEncoder.encode(serverResponse);

			servletHelper.setJsonResponse(response);
			response.setStatus(HttpServletResponse.SC_OK);

			PrintWriter out = response.getWriter();
			out.print(jsonAnswer);
			out.flush();
		}
	}

	/**
	 * See add form.
	 * 
	 * @return the model and view
	 */
	@RequestMapping(value = "/webapp/application/create", method = RequestMethod.GET)
	public ModelAndView seeAddForm() throws Exception {
		ModelAndView modelAndView = new ModelAndView("application/form");

		Application application = new Application("-1", "-1", "");
		application.setId(new Long(-1));
		Map<String, Object> asMap = application.getAsMap();

		modelAndView.addObject("application", asMap);
		return modelAndView;
	}

	@RequestMapping(value = "/webapp/application/show-edit-ui/{id}", method = RequestMethod.GET)
	public ModelAndView goToEditApplication(@PathVariable("id") Long id,
			HttpServletRequest request, RedirectAttributes redirectAttrs)
			throws Exception {
		Application app = applicationService.findById(id);
		// get the tab to set as active in the view
		String from = servletHelper.readStringFromRequest(request,
				ApplicationController.PARAM_NAME_FROM);
		String[] validOptions = new String[] {
				ApplicationController.FROM_SUBJECT,
				ApplicationController.FROM_ROLE,
				ApplicationController.FROM_PERMISSION };
		String validFrom = servletHelper.getValidOption(from, validOptions);
		String fromValue = servletHelper.readStringFromRequest(request,
				validFrom);
		// show the ui
		if (app == null) {
			throw new ApplicationNotFound("id", id);
		} else {
			ModelAndView answer = new ModelAndView("application/editComponents");

			Map<String, Object> toAdd = new HashMap<String, Object>();
			toAdd.put("id", app.getId());
			toAdd.put("name", app.getName());
			toAdd.put("description", app.getDescription());
			toAdd.put("from", validFrom);

			answer.addAllObjects(toAdd);

			return answer;
		}

	}

	/**
	 * Handle delete.
	 * 
	 * @param id
	 *            the id
	 * @param redirectAttrs
	 *            the redirect attrs
	 * @return the string
	 * @throws ImperiumException
	 *             the imperium exception
	 * @throws ApplicationNotFound
	 *             the application not found
	 */
	/*
	 * @RequestMapping( value = "/webapp/application/delete/{id}", method =
	 * RequestMethod.POST) public String handleDelete(@PathVariable("id") Long
	 * id, RedirectAttributes redirectAttrs) throws ImperiumException,
	 * ApplicationNotFound {
	 * 
	 * Application application = applicationService.findById(id); if
	 * (application == null) { throw new ApplicationNotFound("id", id); } else {
	 * applicationService.delete(application); }
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * map.put("operation", "application/delete"); map.put("result", "success");
	 * map.put("message", "Delete the application with id :" + id);
	 * 
	 * springHelper.addValuesForFlashRedirect(map, redirectAttrs); return
	 * "redirect:/application/delete/success"; }
	 */
	/*
	 * @RequestMapping( value = "/webapp/application/get.json", method =
	 * RequestMethod.GET) public void
	 * handleJsonShowInformation(HttpServletRequest request, HttpServletResponse
	 * response, RedirectAttributes redirectAttrs) throws Exception { Long
	 * applicationId = servletHelper.readLongFromRequest(request, "id", new
	 * Long(-1));
	 * 
	 * Application application = applicationService.findById(applicationId);
	 * 
	 * if (application == null) { throw new ApplicationNotFound("id",
	 * applicationId); } else { application =
	 * applicationService.findOneWithDetailById(applicationId);
	 * 
	 * Map<String, Object> asMap = application.getAsMap(); asMap.put("success",
	 * true); asMap.put("apiKey", application.getApiKey());
	 * 
	 * String jsonAnswer = jsonHelper.toJson(asMap);
	 * servletHelper.setJsonResponse(response);
	 * response.setStatus(HttpServletResponse.SC_OK); PrintWriter out =
	 * response.getWriter(); out.print(jsonAnswer); out.flush(); } }
	 */

	/**
	 * Handle removal application
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/webapp/application/removal.json", method = RequestMethod.GET)
	public void handleJsonRemoval(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long id = servletHelper
				.readLongFromRequest(request, "id", new Long(-1));
		Application application = applicationService.findById(id);
		Map<String, Object> answer = new HashMap<String, Object>();
		answer.put("success", false);

		if (application == null) {
			answer.put("sucess", false);
			answer.put("message", "Cannot remove application with id :" + id);
		} else {
			Map<String, Object> applicationAsMap = application.getAsMap();
			answer.put("application", applicationAsMap);
			applicationService.delete(application);
			answer.put("success", true);
		}

		String jsonAnswer = jsonHelper.toJson(answer);
		servletHelper.setJsonResponse(response);
		if ((Boolean) answer.get("success")) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		PrintWriter out = response.getWriter();
		out.print(jsonAnswer);
		out.flush();
	}

	/**
	 * On application not found.
	 * 
	 * @param e
	 *            the e
	 * @return the model and view
	 */
	@ExceptionHandler(ApplicationNotFound.class)
	public ModelAndView onApplicationNotFound(ApplicationNotFound e) {
		ModelAndView modelAndView = new ModelAndView("exception/home");

		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter
				.format("The application with the attribute %s with value %s doesn't exist ",
						e.getAttributeName(), e.getValue());

		Map<String, String> map = new HashMap<String, String>();
		map.put("exceptionClass", e.getClass().getCanonicalName());
		map.put("message", sb.toString());

		formatter.close();

		modelAndView.addAllObjects(map);
		return modelAndView;
	}

	@RequestMapping(value = "/webapp/application/edit", method = RequestMethod.GET)
	public ModelAndView showEditForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = servletHelper
				.readLongFromRequest(request, "id", new Long(-1));

		Application application = applicationService.findById(id);

		if (application == null) {
			throw new ApplicationNotFound("id", id);
		} else {
			ModelAndView modelAndView = new ModelAndView("application/form");
			Map<String, Object> asMap = application.getAsMap();
			modelAndView.addObject("application", asMap);
			return modelAndView;
		}
	}

	/**
	 * Edits the.
	 * 
	 * @return the string
	 * @throws ImperiumException
	 *             the imperium exception
	 */
	@RequestMapping(value = "application/edit/{id}", method = RequestMethod.POST)
	public String edit(@PathVariable("id") Long id,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description,
			RedirectAttributes redirectAttrs) throws ImperiumException {
		Application application = applicationService.findById(id);

		if (application == null) {
			throw new ApplicationNotFound("id", id);
		} else {
			application.setName(name);
			application.setDescription(description);
			applicationService.update(application);

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("operation", "application/edit");
			map.put("result", "success");
			map.put("message", "Updated the application with id :" + id);

			springHelper.addValuesForFlashRedirect(map, redirectAttrs);

			return "redirect:/application/update/success";
		}
	}
}
