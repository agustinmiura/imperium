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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.common.collect.ImmutableMap;

import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.security.IHashService;
import ar.com.imperium.domain.User;
import ar.com.imperium.exception.domain.ApplicationNotFound;
import ar.com.imperium.service.interfaces.IUserService;

@Controller
public class SecurityController
{
    public static final String PARAM_NAME_USER = "USER";
    public static final String PARAM_NAME_USER_MAP = "USER_MAP";
    public static final String PARAM_NAME_FROM_INVALID_CREDENTIALS =
        "FROM_INVALID_CREDENTIALS";

    @Autowired
    @Qualifier("servletHelper")
    private ServletHelper servletHelper;

    @Autowired
    @Qualifier("jpaUserService")
    private IUserService userService;

    /**
     * @todo use Sha512 hasher
     */
    @Autowired
    @Qualifier("dummyHashService")
    private IHashService hashService;

    private static final Logger logger = LoggerFactory
        .getLogger(SecurityController.class);

    private boolean isFromInvalidLogin(HttpServletRequest request)
    {
        Map<String, Object> flashAttributes =
            (Map<String, Object>) RequestContextUtils.getInputFlashMap(request);

        boolean answer = false;

        if (flashAttributes != null) {
            Object isFromInvalidParameter =
                flashAttributes
                    .get(SecurityController.PARAM_NAME_FROM_INVALID_CREDENTIALS);

            answer =
                (Boolean) ((isFromInvalidParameter != null) ? (isFromInvalidParameter)
                    : false);
        }

        return answer;
    }

    @RequestMapping(
        value = "/webapp/security/login",
        method = RequestMethod.GET)
    public ModelAndView seeLoginForm(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        boolean fromInvalidLogin = isFromInvalidLogin(request);

        User currentUser =
            (User) servletHelper.readFromSession(
                request,
                SecurityController.PARAM_NAME_USER);

        ModelAndView modelAndView = null;
        if (currentUser != null) {
            modelAndView = new ModelAndView("application/see-list");
        } else if (fromInvalidLogin == true) {
            modelAndView = new ModelAndView("security/login");
            modelAndView.addObject(
                SecurityController.PARAM_NAME_FROM_INVALID_CREDENTIALS,
                true);
        } else {
            modelAndView = new ModelAndView("security/login");
            modelAndView.addObject(
                SecurityController.PARAM_NAME_FROM_INVALID_CREDENTIALS,
                false);
        }
        return modelAndView;
    }

    @ExceptionHandler(javax.persistence.PersistenceException.class)
    public ModelAndView onJpaException(PersistenceException e) 
    {
    	ModelAndView modelAndView = new ModelAndView("exception/home");
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("exceptionClass",e.getClass().getCanonicalName());
    	map.put("message", e.getMessage());
    	map.put("stackTrace", e.getStackTrace().toString());
    	
    	modelAndView.addAllObjects(map);
    	
    	return modelAndView;
    	
    }
    
    @RequestMapping(
        value = "/webapp/security/doLogin",
        method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request,
        HttpServletResponse response,
        final RedirectAttributes redirectAttributes) throws Exception
    {

        HttpSession session = request.getSession();
        User userInSession =
            (User) session.getAttribute(SecurityController.PARAM_NAME_USER);

        String name =
            servletHelper.readStringFromRequest(request, "username", "");
        String password =
            servletHelper.readStringFromRequest(request, "password", "");

        password = hashService.hashString(password);
        String answer = null;
        // user is logged in redirect to main
        if (userInSession != null) {
            answer = "redirect:/webapp/application/see-list";
            // user with valid credentials
        } else if ((userService.findOneByUserAndPassword(name, password)) != null) {
            User user = (userService.findOneByUserAndPassword(name, password));

            session.setAttribute(
                SecurityController.PARAM_NAME_USER,
                user.getAsMap());

            answer = "redirect:/webapp/application/see-list";
        } else {
            redirectAttributes.addFlashAttribute(
                SecurityController.PARAM_NAME_FROM_INVALID_CREDENTIALS,
                true);
            answer = "redirect:/webapp/security/login";
        }
        return answer;
    }

    @RequestMapping(
        value = "/webapp/security/logout",
        method = RequestMethod.GET)
    public String doLogout(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        HttpSession session = request.getSession();
        Map<String, Object> userInfo =
            (Map<String, Object>) session.getAttribute("USER");
        // do logout
        if (userInfo != null) {
            session.setAttribute("USER", null);
            session.invalidate();
        }
        return "redirect:/webapp/security/login";
    }

}
