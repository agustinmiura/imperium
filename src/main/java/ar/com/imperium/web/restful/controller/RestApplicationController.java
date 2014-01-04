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
package ar.com.imperium.web.restful.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import ar.com.imperium.common.IPropertiesHelper;
import ar.com.imperium.service.interfaces.IApplicationService;

@Controller
public class RestApplicationController
{

    private static final Logger logger = LoggerFactory
        .getLogger(RestApplicationController.class);

    @Autowired
    @Qualifier("jpaComplexApplicationService")
    private IApplicationService applicationService;

    @Autowired
    @Qualifier("applicationPropertiesHelper")
    private IPropertiesHelper propertiesHelper;

    private Map<String, Object> getDefaultValues()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(
            "page",
            new Integer(propertiesHelper.getProperty("default.page")));
        map.put(
            "pageSize",
            new Integer(propertiesHelper.getProperty("default.pageSize")));
        map.put(
            "sort",
            propertiesHelper.getProperty("application.default.sort.order"));

        return map;
    }

}
