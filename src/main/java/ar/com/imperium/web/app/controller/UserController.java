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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ar.com.imperium.common.DataTablesPluginHelper;
import ar.com.imperium.common.PaginationHelper;
import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.json.IJsonHelper;
import ar.com.imperium.common.json.ServerResponse;
import ar.com.imperium.common.security.IHashService;
import ar.com.imperium.domain.User;
import ar.com.imperium.domain.UserType;
import ar.com.imperium.exception.domain.UserNotFound;
import ar.com.imperium.service.interfaces.IUserService;

@Controller
public class UserController
{

    private static final Logger logger = LoggerFactory
        .getLogger(UserController.class);

    @Autowired
    @Qualifier("paginationHelper")
    private PaginationHelper paginationHelper;

    @Autowired
    @Qualifier("gsonJsonHelper")
    private IJsonHelper jsonHelper;

    @Autowired
    @Qualifier("servletHelper")
    private ServletHelper servletHelper;

    @Autowired
    @Qualifier("dataTablesHelper")
    private DataTablesPluginHelper dataTablesHelper;

    @Autowired
    @Qualifier("jpaUserService")
    private IUserService userService;

    @Autowired
    @Qualifier("dummyHashService")
    private IHashService hashService;

    @RequestMapping(
        value = "/webapp/user/remove.json",
        method = RequestMethod.POST)
    public void removeUser(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String idString = (String) params.get("id");
        Long id = Long.parseLong(idString);

        User user = userService.findById(id);
        if (user == null) {
            throw new UserNotFound("id", id);
        } else {
            userService.delete(user);
            Map<String, Object> answer = new HashMap<String, Object>();
            answer.put("success", true);
            answer.put("data", user.getAsMap());

            String jsonAnswer = jsonHelper.toJson(answer);
            servletHelper.setJsonResponse(response);
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();
        }

    }

    @RequestMapping(
        value = "/webapp/user/create.json",
        method = RequestMethod.POST)
    public void createUser(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        // String passwordAgain = (String) params.get("passwordAgain");
        String name = (String) params.get("name");
        String password = (String) params.get("password");

        String stringType = (String) params.get("type");
        Integer type = Integer.parseInt(stringType);

        User user = new User(name, password, type);
        User managedUser = userService.create(user);

        Map<String, Object> data = managedUser.getAsMap();
        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("success", true);
        answer.put("data", data);

        String jsonAnswer = jsonHelper.toJson(answer);
        servletHelper.setJsonResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    @RequestMapping(value = "/webapp/user/create", method = RequestMethod.GET)
    public ModelAndView showCreateForm(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        ModelAndView modelAndView = new ModelAndView("user/form");

        User user = new User("-1", "-1", UserType.USER.getType());
        user.setId(new Long(-1));

        List<Map<String, Object>> typeList =
            new ArrayList<Map<String, Object>>();
        for (UserType userType : UserType.values()) {
            typeList.add(userType.getAsMap());
        }
        modelAndView.addObject("userTypes", typeList);

        modelAndView.addObject("user", user.getAsMap());
        return modelAndView;
    }

    @RequestMapping(
        value = "/webapp/user/edit.json",
        method = RequestMethod.POST)
    public void editUser(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        // String name = (String) params.get("name");
        // String passwordAgain = (String) params.get("passwordAgain");
        String password = (String) params.get("password");

        String idString = (String) params.get("id");
        Long id = Long.parseLong(idString);

        User user = userService.findById(id);

        if (user == null) {
            throw new UserNotFound("id", id);
        } else {
            String passwordToSet = hashService.hashString(password);
            user.setPassword(passwordToSet);
            User managedUser = userService.update(user);

            Map<String, Object> answer = new HashMap<String, Object>();
            answer.put("success", true);
            answer.put("user", managedUser.getAsMap());

            String jsonAnswer = jsonHelper.toJson(answer);
            servletHelper.setJsonResponse(response);
            response.setStatus(HttpServletResponse.SC_OK);

            PrintWriter out = response.getWriter();
            out.print(jsonAnswer);
            out.flush();

        }
    }

    @RequestMapping(value = "/webapp/user/edit", method = RequestMethod.GET)
    public ModelAndView showEditForm(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        ModelAndView modelAndView = new ModelAndView("user/form");

        Long userId =
            servletHelper.readLongFromRequest(request, "id", new Long(-1));

        User user = userService.findById(userId);

        if (user == null) {
            throw new UserNotFound("id", userId);
        } else {
            List<Map<String, Object>> typeList =
                new ArrayList<Map<String, Object>>();
            for (UserType userType : UserType.values()) {
                typeList.add(userType.getAsMap());
            }
            modelAndView.addObject("userTypes", typeList);

            modelAndView.addObject("user", user.getAsMap());
            return modelAndView;
        }
    }

    @RequestMapping(value = "/webapp/user/list", method = RequestMethod.GET)
    public ModelAndView showListUi(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        ModelAndView modelAndView = new ModelAndView("user/list");
        return modelAndView;
    }

    @RequestMapping(
        value = "/webapp/user/list.json",
        method = RequestMethod.GET)
    public void handleShowJsonList(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {

        Map<String, Object> dataTableParams =
            dataTablesHelper.getDatatableParameters(request);
        String query = dataTablesHelper.getSearchParam(dataTableParams);

        boolean doSimpleSearch = ((query == null) || (query.length() == 0));

        if (doSimpleSearch) {
            handleJsonUserList(request, response);
        } else {
            handleJsonUserListWhere(request, response);
        }
    }

    private void handleJsonUserList(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {

        List<User> userList = new ArrayList<User>();

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
        if (sort==null) {
            sort = "name";
        }
        if (direction==null) {
            direction = "asc";
        }

        Long qty = userService.getTotal();
        Integer integerQty = Integer.parseInt(qty.toString());

        Integer page =
            paginationHelper.getPageFromOffset(offset, pageSize, qty);

        Map<String, Object> pagination = new HashMap<String, Object>();
        Map<String, Object> order = new HashMap<String, Object>();

        pagination.put("page", page);
        pagination.put("maxSize", pageSize);

        order.put("sort", sort);
        order.put("direction", direction);

        userList = userService.listAll(pagination, order);
        String userName =
            (String) ((Map<String, Object>) servletHelper.readFromSession(
                request,
                SecurityController.PARAM_NAME_USER)).get("name");
        removeLoggedUserFromList(userList, userName);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                convertUserList(userList),
                integerQty,
                offset,
                verificationString);

        String jsonAnswer = jsonHelper.toJson(serverResponse);
        servletHelper.setJsonResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    private void handleJsonUserListWhere(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
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

        Long qty = userService.getTotalWhere(queryParams);
        Integer integerQty = Integer.parseInt(qty.toString());

        Integer page =
            paginationHelper.getPageFromOffset(offset, pageSize, qty);

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", page);
        pagination.put("maxSize", pageSize);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", sort);
        order.put("direction", direction);

        List<User> userList =
            userService.listAllWhere(queryParams, pagination, order);
        String userName =
            (String) ((Map<String, Object>) servletHelper.readFromSession(
                request,
                SecurityController.PARAM_NAME_USER)).get("name");
        removeLoggedUserFromList(userList, userName);

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                convertUserList(userList),
                integerQty,
                offset,
                verificationString);

        String jsonAnswer = jsonHelper.toJson(serverResponse);

        servletHelper.setJsonResponse(response);
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

    private List<Map<String, Object>> convertUserList(List<User> userList)
        throws Exception
    {
        List<Map<String, Object>> answer = new ArrayList<Map<String, Object>>();
        for (User eachUser : userList) {
            answer.add(eachUser.getAsMap());
        }
        return answer;
    }

    private void removeLoggedUserFromList(List<User> userList, String userName)
        throws Exception
    {
        User toRemove = null;
        for (User eachUser : userList) {
            if (eachUser.getName().compareTo(userName) == 0) {
                toRemove = eachUser;
                break;
            }
        }
        if (toRemove != null) {
            userList.remove(toRemove);
        }
    }
}
