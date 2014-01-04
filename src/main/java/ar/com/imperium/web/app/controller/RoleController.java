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

import javax.management.relation.RoleInfoNotFoundException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.exception.domain.ApplicationNotFound;
import ar.com.imperium.exception.domain.RoleNotFound;
import ar.com.imperium.exception.domain.SubjectNotFound;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IPermissionService;
import ar.com.imperium.service.interfaces.IRoleService;
import ar.com.imperium.service.interfaces.ISubjectService;

import com.google.common.base.Joiner;

@Controller
public class RoleController
{
    /** The Constant logger. */
    private static final Logger logger = LoggerFactory
        .getLogger(RoleController.class);

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

    /* Role service */
    @Autowired
    @Qualifier("jpaComplexRoleService")
    private IRoleService roleService;

    /* Subject service */
    @Autowired
    @Qualifier("jpaComplexSubjectService")
    private ISubjectService subjectService;

    @Autowired
    @Qualifier("applicationPropertiesHelper")
    private IPropertiesHelper propertiesHelper;

    @RequestMapping(
        value = "/webapp/role/list-for-subject.json",
        method = RequestMethod.GET)
    public void handleJsonListForSubject(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);

        Long subjectId =
            servletHelper.readLongFromRequest(
                request,
                "subjectId",
                new Long(-1));
        Long applicationId =
            servletHelper.readLongFromRequest(
                request,
                "applicationId",
                new Long(-1));

        String queryString = dataTablesHelper.getSearchParam(dataTableParams);

        Application application = applicationService.findById(applicationId);

        Subject subject = subjectService.findById(subjectId);

        // subject is null
        if (subject == null) {
            throw new SubjectNotFound("id", subjectId, true);
        } else if (application == null) {
            throw new ApplicationNotFound("id", applicationId, true);
            // where role.name or role.description like or simple list
        } else {
            this.handleAjaxListForSubject(request, response);
        }
    }

    @RequestMapping(
        value = "/webapp/role/list-available-for-subject.json",
        method = RequestMethod.GET)
    public void handleJsonAvailableListForSubject(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);

        Long subjectId =
            servletHelper.readLongFromRequest(
                request,
                "subjectId",
                new Long(-1));
        Long applicationId =
            servletHelper.readLongFromRequest(
                request,
                "applicationId",
                new Long(-1));

        String queryString = dataTablesHelper.getSearchParam(dataTableParams);

        Application application = applicationService.findById(applicationId);

        Subject subject = subjectService.findById(subjectId);

        // subject is null
        if (subject == null) {
            throw new SubjectNotFound("id", subjectId, true);
        } else if (application == null) {
            throw new ApplicationNotFound("id", applicationId, true);
            // where role.name or role.description like
        } else {
            handleAjaxListAvailableForSubject(request, response);
        }
    }

    @RequestMapping(
        value = "/webapp/role/update-permission.json",
        method = RequestMethod.POST)
    public void handleJsonUpdatePermission(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String roleIdString = (String) params.get("roleId");
        Long roleId = Long.parseLong(roleIdString);

        List<String> addParamater = (List<String>) params.get("add");
        List<Long> add = arrayHelper.convertToLongList(addParamater);

        List<String> removeParameter = (List<String>) params.get("remove");
        List<Long> remove = arrayHelper.convertToLongList(removeParameter);

        Role role = roleService.findById(roleId);

        if (role == null) {
            throw new ImperiumException("Role not found", true);
        } else {
            roleService.updatePermissions(roleId, add, remove);

            Map<String, Object> answer = new HashMap<String, Object>();
            answer.put("success", true);
            answer.put("add", add);
            answer.put("remove", remove);
            answer.put("roleId", roleId);

            String jsonAnswer = jsonHelper.encodeMap(answer);
            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    @RequestMapping(value = "/webapp/role/removal-from-app.json",
    // value = "/role/ajax/removal-from-app",
        method = RequestMethod.POST)
    public void handleAjaxRemovalFromApplication(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {

        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String applicationIdString = (String) params.get("applicationId");
        Long applicationId = Long.parseLong(applicationIdString);

        List<String> roleStringList = (List<String>) params.get("roles");
        List<Role> toRemove = new ArrayList<Role>();
        Long eachId;

        Application application = applicationService.findById(applicationId);
        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        }

        List<Long> roleIdList = arrayHelper.convertToLongList(roleStringList);
        toRemove = roleService.findWithIds(roleIdList);
        /*
         * Map<String, ?> removeStatus = applicationService.updateRoles(
         * applicationId, new ArrayList<Long>(), roleIdList);
         */
        roleService.remove(toRemove);

        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("applicationId", applicationId);

        Joiner joiner = Joiner.on(",").skipNulls();
        String joinedIds = joiner.join(roleIdList);
        answer.put("roles", joinedIds);
        ServerResponse serverResponse =
            new ServerResponse(true, answer, -1, -1, "", -1, -1);

        String jsonAnswer = jsonHelper.toJson(serverResponse);
        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    /**
     * Show form for creation here
     * 
     * @param applicationId
     * @return
     * @throws Exception
     */
    @RequestMapping(
        value = "/webapp/role/create/{applicationId}",
        method = RequestMethod.GET)
    public ModelAndView seeAddForm(@PathVariable("applicationId")
    Long applicationId) throws Exception
    {
        Application application = applicationService.findById(applicationId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else {
            ModelAndView modelAndView = new ModelAndView("roles/form");

            modelAndView.addObject("applicationId", applicationId);
            modelAndView.addObject("name", application.getName());
            modelAndView.addObject("description", application.getDescription());
            modelAndView.addObject("roleId", new Long(-1));
            modelAndView.addObject("roleName", new Long(-1));
            modelAndView.addObject("roleDescription", new Long(-1));

            return modelAndView;
        }

    }

    @RequestMapping(
        value = "/webapp/role/list.json/{applicationId}",
        method = RequestMethod.GET)
    public void handleJsonListWithPagination(@PathVariable("applicationId")
    Long applicationId, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Application application = applicationService.findById(applicationId);

        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);
        String query = dataTablesHelper.getSearchParam(dataTableParams);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else if (query == null) {
            handleAjaxRoleListForApplication(applicationId, request, response);
        } else {
            handleAjaxRoleListForApplicationWhere(
                applicationId,
                request,
                response);
        }

    }

    @RequestMapping(
        value = "/webapp/role/do-create.json",
        method = RequestMethod.POST)
    public void handleRoleCreationWithAjax(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String name = (String) params.get("name");
        String description = (String) params.get("description");

        Long applicationId = null;
        if (params.get("applicationId") != null) {
            applicationId =
                Long.parseLong((String) params.get("applicationId"));
        }

        Application application = applicationService.findById(applicationId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            role.setApplication(application);

            Role created = roleService.create(role);

            ServerResponse serverResponse =
                new ServerResponse(true, created, -1, -1, "", -1, -1);

            String jsonAnswer = jsonHelper.toJson(serverResponse);
            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    @RequestMapping(
        value = "/webapp/role/submit-success",
        method = RequestMethod.GET)
    public String redirectAfterCreateEditSuccess(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {

        Long applicationId =
            servletHelper.readLongFromRequest(request, "applicationId");

        Long roleId = servletHelper.readLongFromRequest(request, "roleId");

        String url = "/webapp/application/show-edit-ui/" + applicationId;
        return "redirect:" + url;
    }

    /**
     * 
     * @param roleId
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(
        value = "/webapp/role/edition/{roleId}",
        method = RequestMethod.GET)
    public ModelAndView seeEditForm(@PathVariable("roleId")
    Long roleId, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long applicationId =
            servletHelper.readLongFromRequest(
                request,
                "applicationId",
                new Long(-1));

        Role role = roleService.findById(roleId);

        Application application = applicationService.findById(applicationId);

        if (role == null) {
            throw new RoleInfoNotFoundException();
        } else if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else {
            ModelAndView modelAndView = new ModelAndView("roles/form");

            Map<String, Object> modelMap = new HashMap<String, Object>();
            modelMap.put("applicationId", applicationId);
            modelMap.put("name", application.getName());
            modelMap.put("description", application.getDescription());
            modelMap.put("roleId", role.getId());
            modelMap.put("roleName", role.getName());
            modelMap.put("roleDescription", role.getDescription());

            modelAndView.addAllObjects(modelMap);

            return modelAndView;
        }
    }

    /**
     * 
     */

    /**
     * 
     * @param id
     * @param redirectAttrs
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/role/list/{id}", method = RequestMethod.GET)
    public ModelAndView handleShowRoleEdition(@PathVariable("id")
    Long id, RedirectAttributes redirectAttrs) throws Exception
    {
        Application application = applicationService.findById(id);

        ModelAndView answer = null;
        if (application == null) {
            throw new ApplicationNotFound("id", id, true);
        } else {
            answer = new ModelAndView("roles/list");
            answer.addObject("applicationId", id);
        }

        return answer;
    }

    private void handleAjaxListForSubject(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        Long applicationId =
            servletHelper.readLongFromRequest(request, "applicationId");
        Long subjectId =
            servletHelper.readLongFromRequest(request, "subjectId");

        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);

        // get query parameter
        String query = dataTablesHelper.getSearchParam(dataTableParams);

        // get pagination and verification string
        Integer offset = dataTablesHelper.getPaginationOffset(dataTableParams);
        Integer pageSize = dataTablesHelper.getPageSize(dataTableParams);
        Integer verificationNumber =
            dataTablesHelper.getVerificationString(dataTableParams);
        String verificationString = verificationNumber.toString();

        // get order by params
        Map<String, String> sortParams =
            dataTablesHelper.getSortingCriteria(dataTableParams);
        String sort = sortParams.get("name");
        String direction = sortParams.get("direction");

        // prepare query params
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("applicationId", applicationId);
        queryParams.put("subjectId", subjectId);
        if (query != null) {
            queryParams.put("query", query);
        }

        // find qty of subjects
        Long qty = roleService.findQtyForSubjectInApplication(queryParams);
        Integer integerQty = Integer.parseInt(qty.toString());

        // get page to return
        Integer page =
            paginationHelper.getPageFromOffset(offset, pageSize, qty);

        // get the roles
        // order by
        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", sort);
        order.put("direction", direction);

        // pagination
        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", page);
        pagination.put("maxSize", integerQty);

        // role list
        List<Role> roleList =
            roleService.findForSubjectInApplication(
                queryParams,
                pagination,
                order);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                roleList,
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

    private void handleAjaxListAvailableForSubject(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        Long applicationId =
            servletHelper.readLongFromRequest(request, "applicationId");
        Long subjectId =
            servletHelper.readLongFromRequest(request, "subjectId");

        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);

        // get query parameter
        String query = dataTablesHelper.getSearchParam(dataTableParams);

        // get pagination and verification string
        Integer verificationNumber =
            dataTablesHelper.getVerificationString(dataTableParams);

        // get order by params
        Map<String, String> sortParams =
            dataTablesHelper.getSortingCriteria(dataTableParams);
        String sort = sortParams.get("name");
        String direction = sortParams.get("direction");

        // prepare query params
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("applicationId", applicationId);
        queryParams.put("subjectId", subjectId);
        if (query != null) {
            queryParams.put("query", query);
        }

        // find qty of subjects
        Long qty =
            roleService.findAvailableQtyForSubjectInApplication(queryParams);
        Integer integerQty = Integer.parseInt(qty.toString());

        /**
         * A subject has only a few roles so we do not paginate
         */
        Integer page = new Integer(0);
        Integer maxSize = integerQty;

        // get the roles
        // order by
        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", sort);
        order.put("direction", direction);

        // pagination
        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", page);
        pagination.put("maxSize", maxSize);

        // Role List
        List<Role> roleList =
            roleService.findAvailableForSubjectInApplication(
                queryParams,
                pagination,
                order);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                roleList,
                integerQty,
                page,
                verificationNumber.toString());

        String jsonAnswer = jsonHelper.toJson(serverResponse);

        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();

    }

    private void handleAjaxRoleListForApplication(Long applicationId,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Application application = applicationService.findById(applicationId);
        List<Role> roleList = new ArrayList<Role>();
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

        Long qty = roleService.findQtyForApplication(application);
        integerQty = Integer.parseInt(qty.toString());

        Integer page =
            paginationHelper.getPageFromOffset(offset, pageSize, qty);

        roleList =
            roleService.findAllForApplication(
                application,
                page,
                pageSize,
                sort,
                direction);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                roleList,
                integerQty,
                offset,
                verificationString);

        /**
         * Add to the server response the map where the key is the role id and
         * the value the subject qty
         */
        Map<Long, Long> subjectQty =
            subjectService.findSubjectQtyForRole(roleList);
        serverResponse.addToExtraData("subjectQtyMap", subjectQty);

        String jsonAnswer = jsonHelper.toJson(serverResponse);

        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    private void handleAjaxRoleListForApplicationWhere(Long applicationId,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Application application = applicationService.findById(applicationId);
        List<Role> roleList = new ArrayList<Role>();
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
            roleService.findQtyForApplicationWhere(application, queryParams);
        integerQty = Integer.parseInt(qty.toString());

        Integer page =
            paginationHelper.getPageFromOffset(offset, pageSize, qty);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", page);
        pagination.put("maxSize", pageSize);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", sort);
        order.put("direction", direction);

        roleList =
            roleService.findAllForApplicationWhere(
                application,
                queryParams,
                pagination,
                order);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                roleList,
                integerQty,
                offset,
                verificationString);

        /**
         * Add to the server response the map where the key is the role id and
         * the value the subject qty
         */
        Map<Long, Long> subjectQty =
            subjectService.findSubjectQtyForRole(roleList);
        serverResponse.addToExtraData("subjectQtyMap", subjectQty);

        String jsonAnswer = jsonHelper.toJson(serverResponse);

        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    @RequestMapping(
        value = "/role/ajax/get/{roleId}",
        method = RequestMethod.GET)
    public void getRoleInformation(@PathVariable("roleId")
    Long roleId, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Role role = roleService.findById(roleId);

        if (role == null) {
            /**
             * @todo use an Imperium Exception
             */
            throw new Exception("Role not found");
        } else {
            ServerResponse serverResponse =
                new ServerResponse(true, role, -1, -1, "", -1, -1);

            String jsonAnswer = jsonHelper.toJson(serverResponse);

            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);

            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    @RequestMapping(
        value = "/webapp/role/edit.json/{id}",
        method = RequestMethod.POST)
    public void handleRoleEditionWithAjax(@PathVariable("id")
    Long id, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String name = (String) params.get("name");
        String description = (String) params.get("description");

        Long applicationId = null;
        if (params.get("applicationId") != null) {
            applicationId =
                Long.parseLong((String) params.get("applicationId"));
        }

        Role role = roleService.findById(id);

        if (role == null) {
            throw new ImperiumException("Role not found", true);
        } else {
            Map<String, Object> newValues = new HashMap<String, Object>();
            newValues.put("name", name);
            newValues.put("description", description);

            if (applicationId != null) {
                newValues.put("applicationId", applicationId);
            }

            Role updated = roleService.update(id, newValues);

            ServerResponse serverResponse =
                new ServerResponse(true, updated, -1, -1, "", -1, -1);

            String jsonAnswer = jsonHelper.toJson(serverResponse);
            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);
            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    @RequestMapping(
        value = "/json/role/get-permission",
        method = RequestMethod.POST)
    public void handleJsonGetPermissionsForRole(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String roleIdString = (String) params.get("roleId");
        Long roleId = Long.parseLong(roleIdString);

        String pageAsString = (String) params.get("page");
        Integer page = Integer.parseInt(pageAsString);

        String maxSizeString = (String) params.get("maxSize");
        Integer maxSize = Integer.parseInt(maxSizeString);

        Role role = roleService.findById(roleId);

        if (role == null) {
            throw new ImperiumException("Role not found", true);
        } else {
            Long qty = roleService.findPermissionQty(role);
            Integer integerQty = Integer.parseInt(qty.toString());

            Integer pageQty = paginationHelper.getPageQty(maxSize, integerQty);
            page = paginationHelper.getPageToUseWithIndexChanged(page, pageQty);
            Map<String, Object> pagination = new HashMap<String, Object>();
            pagination.put("page", page);
            pagination.put("maxSize", maxSize);

            Integer offset = paginationHelper.getOffsetFromPage(page, maxSize);

            List<Permission> permissionList =
                roleService.findPermissions(role, pagination);

            ServerResponse serverResponse =
                ServerResponse.createAnswerForDataTable(
                    permissionList,
                    integerQty,
                    page,
                    "a");

            String jsonAnswer = jsonHelper.toJson(serverResponse);

            servletHelper.setJsonResponse(response);
            response.setStatus(response.SC_OK);

            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }
    }

    // private Map<String, Object> getDefaultParams()
    // {
    // Integer pageSize =
    // Integer.parseInt(propertiesHelper.getProperty("default.pageSize"));
    // Integer page =
    // Integer.parseInt(propertiesHelper.getProperty("default.page"));
    // String sort = propertiesHelper.getProperty("role.default.sort.order");
    //
    // Map<String, Object> answer = new HashMap<String, Object>();
    // answer.put("page", page);
    // answer.put("pageSize", pageSize);
    // answer.put("sort", sort);
    //
    // return answer;
    // }

    @RequestMapping(
        value = "/json/role/get-subject-qty",
        method = RequestMethod.POST)
    public void handleJsonGetSubjectQtyForRoles(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        List<String> roleIdList = (List<String>) params.get("roleList");
        List<Long> idList = arrayHelper.convertToLongList(roleIdList);

        List<Role> roleList = roleService.findWithIds(idList);

        Map<Long, Long> subjectQtyMap =
            subjectService.findSubjectQtyForRole(roleList);

        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("roleList", roleList);
        answer.put("subjectQtyMap", subjectQtyMap);

        String jsonAnswer = jsonHelper.encodeMap(answer);
        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    @RequestMapping(
        value = "/webapp/role/show-permission-edit",
        method = RequestMethod.GET)
    public ModelAndView redirectToRoleEditPermissionUi(
        HttpServletRequest request, HttpServletResponse response,
        final RedirectAttributes redirectAttributes) throws Exception
    {

        Long applicationId =
            servletHelper.readLongFromRequest(
                request,
                "applicationId",
                new Long(-1));

        Long roleId =
            servletHelper.readLongFromRequest(request, "roleId", new Long(-1));

        Role role = roleService.findById(roleId);
        Application application = applicationService.findById(applicationId);

        if (application == null) {
            throw new ApplicationNotFound("id", applicationId);
        } else if (role == null) {
            throw new RoleNotFound("id", roleId);
        } else {
            // ModelAndView modelAndView =
            // new ModelAndView("roles/editPermission");
            ModelAndView modelAndView =
                new ModelAndView("roles/edit-permission");

            modelAndView.addObject("applicationId", applicationId);
            modelAndView.addObject("applicationName", application.getName());
            modelAndView.addObject("roleId", roleId);
            modelAndView.addObject("roleName", role.getName());
            modelAndView.addObject("roleDescription", role.getDescription());

            return modelAndView;
        }
    }
}
