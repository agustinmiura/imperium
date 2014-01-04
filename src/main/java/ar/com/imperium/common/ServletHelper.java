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
package ar.com.imperium.common;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component("servletHelper")
public class ServletHelper
{
    public static final String RESPONSE_TYPE_JSON =
        "application/json; charset=UTF-8";

    public void setJsonResponse(HttpServletResponse response) throws Exception
    {
        response.setContentType(RESPONSE_TYPE_JSON);
    }

    public String readJsonRequest(HttpServletRequest request) throws Exception
    {
        BufferedReader bufferedReader = request.getReader();
        StringBuilder sBuilder = new StringBuilder();
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            sBuilder.append(string);
        }
        return sBuilder.toString();
    }

    public String readStringFromRequest(HttpServletRequest request,
        String paramName) throws Exception
    {
        return request.getParameter(paramName);
    }

    public String getValidOption(String parameter, String[] validOptions)
        throws Exception
    {
        List<String> validList = Arrays.asList(validOptions);
        return getValidOption(parameter, validList);
    }

    /**
     * If parameter is null then returns the first element in valid options If
     * parameter is != than null and doesn't exist in valid options return the
     * first element in the valid options array
     * 
     * @param request
     * @param paramName
     * @param validOptions
     * @return
     * @throws Exception
     */
    public String getValidOption(String parameter, List<String> validOptions)
        throws Exception
    {
        if (validOptions.size() == 0) {
            throw new RuntimeException("Exception in getValidOption method");
        }

        boolean validOption = true;
        if (parameter == null) {
            validOption = false;
        } else {
            validOption = false;
            for (String eachOption : validOptions) {
                if (eachOption.equals(parameter)) {
                    validOption = true;
                    break;
                }
            }
        }
        String answer = null;
        if (validOption) {
            answer = parameter;
        } else {
            answer = validOptions.get(0);
        }
        return answer;
    }

    public String readStringFromRequest(HttpServletRequest request,
        String paramName, String defaultValue) throws Exception
    {
        String param = request.getParameter(paramName);
        String answer = (param != null) ? (param) : defaultValue;
        return answer;
    }

    public Long readLongFromRequest(HttpServletRequest request, String paramName)
        throws Exception
    {
        String longAsString = request.getParameter(paramName);
        return Long.parseLong(longAsString);
    }

    public Long readLongFromRequest(HttpServletRequest request,
        String paramName, Long defaultValue)
    {
        Long answer = null;
        String longAsString = request.getParameter(paramName);
        if (longAsString != null) {
            answer = Long.parseLong(longAsString);
        } else {
            answer = defaultValue;
        }
        return answer;
    }

    public Integer readIntegerFromRequest(HttpServletRequest request,
        String paramName) throws Exception
    {
        String integerAsString = request.getParameter(paramName);
        return Integer.parseInt(integerAsString);
    }

    public Integer readIntegerFromRequest(HttpServletRequest request,
        String paramName, Integer defaultValue) throws Exception
    {
        String integerAsString = request.getParameter(paramName);
        Integer answer = null;
        if (integerAsString != null) {
            answer = Integer.parseInt(integerAsString);
        } else {
            answer = defaultValue;
        }
        return answer;
    }

    public Object readParameterFromFlashMap(HttpServletRequest request,
        String name) throws Exception
    {
        Object answer = null;

        Map<String, Object> flashAttributes =
            (Map<String, Object>) RequestContextUtils.getInputFlashMap(request);

        if (flashAttributes != null) {
            answer = flashAttributes.get(name);
        }

        return answer;
    }

    public String readStringFromFlashMap(HttpServletRequest request, String name)
        throws Exception
    {
        Object answer = readParameterFromFlashMap(request, name);
        String stringAnswer = null;
        if (answer != null) {
            stringAnswer = (String) answer;
        }
        return stringAnswer;
    }

    /**
     * Reads the session from the request but if the session is invalid it does
     * not read it
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public HttpSession getSessionFromRequest(HttpServletRequest request)
        throws Exception
    {
        HttpSession session = request.getSession(false);
        return session;
    }

    /**
     * Read the parameter from the session
     * 
     * @param request
     * @param name
     * @return
     * @throws Exception
     */
    public Object readFromSession(HttpServletRequest request, String name)
        throws Exception
    {
        HttpSession session = getSessionFromRequest(request);
        Object answer = null;
        if (session != null) {
            answer = session.getAttribute(name);
        }
        return answer;

    }
    /**
     * If the webapplication is in
     * localhost:8080/myWebapp
     * gets the string /myWebapp
     * 
     * 
     * @param request
     * @return
     */
    public String getApplicationContext(HttpServletRequest request) 
    {
    	ServletContext context = request.getServletContext();
    	return context.getContextPath();
    }

}
