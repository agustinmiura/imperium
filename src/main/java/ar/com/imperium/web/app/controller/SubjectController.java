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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.com.imperium.common.ArrayHelper;
import ar.com.imperium.common.DataTablesPluginHelper;
import ar.com.imperium.common.IPropertiesHelper;
import ar.com.imperium.common.PaginationHelper;
import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.SpringFrameworkHelper;
import ar.com.imperium.common.json.IEntityEncoder;
import ar.com.imperium.common.json.IJsonHelper;
import ar.com.imperium.common.json.ServerResponse;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.domain.ApplicationNotFound;
import ar.com.imperium.exception.domain.SubjectNotFound;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IPermissionService;
import ar.com.imperium.service.interfaces.ISubjectService;

@Controller
public class SubjectController
{
    private static final Logger logger = LoggerFactory
        .getLogger(SubjectController.class);

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
    @Qualifier("arrayHelper")
    private ArrayHelper arrayHelper;

    @Autowired
    @Qualifier("servletHelper")
    private ServletHelper servletHelper;

    @Autowired
    @Qualifier("dataTablesHelper")
    private DataTablesPluginHelper dataTablesHelper;

    /** The application service. */
    @Autowired
    @Qualifier("jpaComplexApplicationService")
    private IApplicationService applicationService;

    /* Permission service */
    @Autowired
    @Qualifier("jpaComplexPermissionService")
    private IPermissionService permissionService;

    /**
     * Subject service
     */
    @Autowired
    @Qualifier("jpaComplexSubjectService")
    private ISubjectService subjectService;

    @Autowired
    @Qualifier("applicationPropertiesHelper")
    private IPropertiesHelper propertiesHelper;

    @RequestMapping(
        value = "/webapp/subject/update-role.json",
        method = RequestMethod.POST)
    public void handleJsonUpdateRoles(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String subjectIdString = (String) params.get("subjectId");
        Long subjectId = Long.parseLong(subjectIdString);

        @SuppressWarnings("unchecked")
        List<String> addParameter = (List<String>) params.get("add");
        List<Long> add = arrayHelper.convertToLongList(addParameter);

        @SuppressWarnings("unchecked")
        List<String> removeParameter = (List<String>) params.get("remove");
        List<Long> remove = arrayHelper.convertToLongList(removeParameter);

        Subject subject = subjectService.findById(subjectId);

        if (subject == null) {
            throw new SubjectNotFound("id", subjectId, true);
        } else {
            Map<String, Object> subjectAnswer =
                subjectService.updateRoles(subject, add, remove);

            Map<String, Object> answer = new HashMap<String, Object>();
            answer.put("success", true);
            answer.put("add", subjectAnswer.get("add"));
            answer.put("remove", subjectAnswer.get("remove"));
            answer.put("subjectId", subjectId);

            String jsonAnswer = jsonHelper.encodeMap(answer);
            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    @RequestMapping(
        value = "/webapp/subject/edit-roles",
        method = RequestMethod.GET)
    public ModelAndView handleShowEditRoles(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {

        Long applicationId =
            servletHelper.readLongFromRequest(
                request,
                "applicationId",
                new Long(-1));

        Long subjectId =
            servletHelper.readLongFromRequest(
                request,
                "subjectId",
                new Long(-1));

        Application application = applicationService.findById(applicationId);
        Subject subject = subjectService.findById(subjectId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else if (subject == null) {
            throw new SubjectNotFound("id", subjectId);
        } else {
            ModelAndView modelAndView = new ModelAndView("subject/edit-Role");
            modelAndView.addObject("subject", subject.getAsMap());
            modelAndView.addObject("application", application.getAsMap());
            modelAndView.addObject("applicationName", application.getName());
            return modelAndView;
        }
    }

    @RequestMapping(
        value = "/webapp/subject/edition.json",
        method = RequestMethod.POST)
    public void handleJsonEdition(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        @SuppressWarnings("unchecked")
        Map<String, Object> applicationValues =
            (Map<String, Object>) params.get("application");

        @SuppressWarnings("unchecked")
        Map<String, Object> subjectValues =
            (Map<String, Object>) params.get("subject");

        Long applicationId =
            Long.parseLong((String) applicationValues.get("id"));
        Long subjectId = Long.parseLong((String) subjectValues.get("id"));

        Application application = applicationService.findById(applicationId);
        Subject subject = subjectService.findById(subjectId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else if (subject == null) {
            throw new SubjectNotFound("id", subjectId);
        } else {

            Map<String, Object> newValues = new HashMap<String, Object>();
            newValues.put("name", subjectValues.get("name"));

            subject.setApplication(application);
            Subject updatedSubject =
                subjectService.update(subjectId, newValues);

            Map<String, Object> answer = new HashMap<String, Object>();
            answer.put("success", true);
            answer.put("data", updatedSubject.getAsMap());

            String jsonAnswer = jsonHelper.toJson(answer);
            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    @RequestMapping(
        value = "/webapp/subject/edition",
        method = RequestMethod.GET)
    public ModelAndView handleSubjectEdition(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        Long applicationId =
            servletHelper.readLongFromRequest(
                request,
                "applicationId",
                new Long(-1));
        Long subjectId =
            servletHelper.readLongFromRequest(
                request,
                "subjectId",
                new Long(-1));

        Application application = applicationService.findById(applicationId);
        Subject subject = subjectService.findById(subjectId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else if (subject == null) {
            throw new SubjectNotFound("id", subjectId);
        } else {

            Map<String, Object> applicationMap = application.getAsMap();
            Map<String, Object> subjectMap = subject.getAsMap();

            ModelAndView modelAndView = new ModelAndView("subject/form");
            modelAndView.addObject("application", applicationMap);
            modelAndView.addObject("subject", subjectMap);

            return modelAndView;
        }
    }

    @RequestMapping(
        value = "/webapp/subject/create",
        method = RequestMethod.GET)
    public ModelAndView seeAddForm(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        Long applicationId =
            servletHelper.readLongFromRequest(request, "applicationId");

        Application application = applicationService.findById(applicationId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else {
            ModelAndView modelAndView = new ModelAndView("subject/form");

            Map<String, Object> map = new HashMap<String, Object>();

            Map<String, Object> applicationMap = application.getAsMap();
            map.put("application", applicationMap);

            Subject subject = new Subject();
            subject.setId(new Long(-1));
            subject.setName("-1");
            Map<String, Object> subjectMap = subject.getAsMap();
            map.put("subject", subjectMap);

            modelAndView.addAllObjects(map);

            return modelAndView;
        }
    }

    /**
     * 
     */

    /**
     * 
     * @param applicationId
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/webapp/subject/list.json/{applicationId}")
    public void handleListWithPagination(@PathVariable("applicationId")
    Long applicationId, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Application application = applicationService.findById(applicationId);

        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);
        String query = dataTablesHelper.getSearchParam(dataTableParams);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId, true);
        } else if (query == null) {
            handleAjaxSubjectListForApplication(
                applicationId,
                request,
                response);
        } else {
            this.handleAjaxSubjectListForApplicationWhere(
                applicationId,
                request,
                response);
        }
    }

    /**
     * 
     */

    /**
     * 
     * @param applicationId
     * @param request
     * @param response
     * @throws Exception
     */
    private void handleAjaxSubjectListForApplication(Long applicationId,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Application application = applicationService.findById(applicationId);
        List<Subject> subjectList = new ArrayList<Subject>();
        Integer integerQty = null;

        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);

        Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
        Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
        Integer verificationNumber =
            dataTablesHelper.getVerificationString(dataTableParams);
        String verificationString = verificationNumber.toString();

        Map<String, String> sortParams =
            dataTablesHelper.getSortingCriteria(dataTableParams);
        String sort = sortParams.get("name");
        String direction = sortParams.get("direction");

        Long qty = subjectService.findQtyForApplication(application);
        integerQty = Integer.parseInt(qty.toString());

        Integer page =
            paginationHelper.getPageFromOffset(offset, pageSize, qty);

        subjectList =
            subjectService.findAllForApplication(
                application,
                page,
                pageSize,
                sort,
                direction);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                subjectList,
                integerQty,
                offset,
                verificationString);

        String jsonAnswer = jsonHelper.toJson(serverResponse);

        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    private void handleAjaxSubjectListForApplicationWhere(Long applicationId,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Application application = applicationService.findById(applicationId);
        List<Subject> subjectList = new ArrayList<Subject>();
        Integer integerQty = null;

        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);

        Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
        Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
        Integer verificationNumber =
            dataTablesHelper.getVerificationString(dataTableParams);
        String verificationString = verificationNumber.toString();

        Map<String, String> sortParams =
            dataTablesHelper.getSortingCriteria(dataTableParams);
        String sort = sortParams.get("name");
        String direction = sortParams.get("direction");

        String query = dataTablesHelper.getSearchParam(dataTableParams);

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("query", query);

        Long qty =
            subjectService.findQtyForApplicationWhere(application, queryParams);
        integerQty = Integer.parseInt(qty.toString());

        Integer page =
            paginationHelper.getPageFromOffset(offset, pageSize, qty);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", page);
        pagination.put("maxSize", pageSize);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", sort);
        order.put("direction", direction);

        subjectList =
            subjectService.findAllForApplicationWhere(
                application,
                queryParams,
                pagination,
                order);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                subjectList,
                integerQty,
                offset,
                verificationString);

        String jsonAnswer = jsonHelper.toJson(serverResponse);

        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    // private Map<String, Object> getDefaultParams()
    // {
    // Integer pageSize =
    // Integer.parseInt(propertiesHelper.getProperty("default.pageSize"));
    // Integer page =
    // Integer.parseInt(propertiesHelper.getProperty("default.page"));
    // String sort =
    // propertiesHelper.getProperty("permission.default.sort.order");
    //
    // Map<String, Object> answer = new HashMap<String, Object>();
    // answer.put("page", page);
    // answer.put("pageSize", pageSize);
    // answer.put("sort", sort);
    //
    // return answer;
    // }

    @RequestMapping(value = "/subject/create.json", method = RequestMethod.POST)
    public void handleCreateSubmit(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String name = (String) params.get("name");
        Map<String, Object> applicationValues =
            (Map<String, Object>) params.get("application");

        Long applicationId =
            Long.parseLong((String) applicationValues.get("id"));
        Application application = applicationService.findById(applicationId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else {
            Subject subject = new Subject();
            subject.setName(name);
            subject.setApplication(application);

            Subject created = subjectService.create(subject);

            ServerResponse serverResponse =
                new ServerResponse(true, created.getAsMap(), -1, -1, "", -1, -1);

            Map<String, Object> applicationMap = application.getAsMap();
            serverResponse.addToExtraData("application", applicationMap);

            String jsonAnswer = jsonHelper.toJson(serverResponse);
            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    @RequestMapping(
        value = "/webapp/subject/removal-from-app.json",
        method = RequestMethod.POST)
    public void handleRemovalFromApp(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        Map<String, Object> applicationValues =
            (Map<String, Object>) params.get("application");
        Map<String, Object> subjectValues =
            (Map<String, Object>) params.get("subject");
        Long applicationId =
            Long.parseLong((String) applicationValues.get("id"));
        Long subjectId = Long.parseLong((String) subjectValues.get("id"));

        Application application = applicationService.findById(applicationId);
        Subject subject = subjectService.findById(subjectId);
        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else if (subject == null) {
            throw new SubjectNotFound("id", subjectId);
        } else {
            subjectService.delete(subject);
            Map<String, Object> answer = new HashMap<String, Object>();
            answer.put("success", true);
            answer.put("application", application.getAsMap());
            answer.put("subject", subject.getAsMap());

            String jsonAnswer = jsonHelper.toJson(answer);

            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);

            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

}
