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

import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.json.IJsonHelper;

@Controller
public class TestController
{
    private static final Logger logger = LoggerFactory
        .getLogger(TestController.class);

    @Autowired
    @Qualifier("gsonJsonHelper")
    private IJsonHelper jsonHelper;

    @Autowired
    @Qualifier("servletHelper")
    private ServletHelper servletHelper;

    @RequestMapping(value = "/test/numbers", method = RequestMethod.POST)
    public void handleContent(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        String jsonRequest = servletHelper.readJsonRequest(request);
        Map<String, Object> params = jsonHelper.decodeJson(jsonRequest);

        String query = (String) params.get("query");
        Integer start = Integer.parseInt((String) params.get("start"));
        Integer maxSize = Integer.parseInt((String) params.get("maxSize"));

        /**
         * Return from the server plan numbers in json If we return a json
         * object like: [{1},{2}] ... functions
         */
        // List<String> answer = new ArrayList<String>();
        // String base = "number";
        // Integer index = 0;
        // Integer end = 100;
        // for (index = 0; index < end; index++) {
        // answer.add(base+index.toString());
        // }

        /**
         * Return from the server objects
         */
        List<Map<String, Object>> answer = new ArrayList<Map<String, Object>>();
        String base = "number";
        Integer index = 0;
        Integer end = 100;
        Map<String, Object> map = null;
        for (index = 0; index < end; index++) {
            map = new HashMap<String, Object>();
            map.put("id", index);
            map.put("name", "name" + index.toString());
            map.put("value", "value" + index.toString());
            answer.add(map);
        }

        String jsonAnswer = jsonHelper.toJson(answer);
        servletHelper.setJsonResponse(response);
        response.setStatus(response.SC_OK);
        PrintWriter out = response.getWriter();
        out.print(jsonAnswer);
        out.flush();
    }

}
