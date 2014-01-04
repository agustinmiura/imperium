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

import ar.com.imperium.common.ArrayHelper;
import ar.com.imperium.common.DataTablesPluginHelper;
import ar.com.imperium.common.IPropertiesHelper;
import ar.com.imperium.common.PaginationHelper;
import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.SpringFrameworkHelper;
import ar.com.imperium.common.json.IEntityEncoder;
import ar.com.imperium.common.json.IJsonHelper;
import ar.com.imperium.service.interfaces.IApiService;

@Controller
public class ApiController
{

    private static final Logger logger = LoggerFactory
        .getLogger(ApiController.class);

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

    @Autowired
    @Qualifier("apiServiceImpl")
    private IApiService apiService;

    @Autowired
    @Qualifier("applicationPropertiesHelper")
    private IPropertiesHelper propertiesHelper;

    @RequestMapping("/api/helloWorld")
    public void handleHelloWorld(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {

        Map<String, Object> serverResponse = new HashMap<String, Object>();
        serverResponse.put("success", true);

        List<String> aList = new ArrayList<String>();
        aList.add("one");
        aList.add("two");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("boolean", true);
        data.put("aString", "String");
        data.put("aList", aList);

        serverResponse.put("data", data);

        String jsonAnswer = jsonHelper.toJson(serverResponse);
        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

}
