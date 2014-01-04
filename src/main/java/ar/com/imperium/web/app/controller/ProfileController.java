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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.com.imperium.common.ServletHelper;
import ar.com.imperium.common.security.IHashService;
import ar.com.imperium.domain.User;
import ar.com.imperium.exception.NotLoggedInException;
import ar.com.imperium.exception.domain.UserNotFound;
import ar.com.imperium.service.interfaces.IUserService;

@Controller
public class ProfileController
{
    private static final Logger logger = LoggerFactory
        .getLogger(ProfileController.class);

    public static final String PARAM_NAME_NEW_PASSWORD_MATCH_FAILED =
        "NEW_PASSWORD_MATCH_FAILED";

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

    @RequestMapping(
        value = "/webapp/profile/change-password",
        method = RequestMethod.GET)
    public ModelAndView seeChangePasswordForm(HttpServletRequest request,
        HttpServletResponse response) throws Exception
    {
        ModelAndView modelAndView = new ModelAndView("security/changePassword");
        return modelAndView;
    }

    @RequestMapping(
        value = "/webapp/profile/do-change-password",
        method = RequestMethod.POST)
    public String doChangePassword(HttpServletRequest request,
        HttpServletResponse response,
        final RedirectAttributes redirectAttributes) throws Exception
    {
        String oldPassword =
            servletHelper.readStringFromRequest(request, "password");
        String newPassword =
            servletHelper.readStringFromRequest(request, "newPassword");
        String newPasswordAgain =
            servletHelper.readStringFromRequest(request, "newPasswordAgain");

        User user =
            (User) request.getSession().getAttribute(
                SecurityController.PARAM_NAME_USER);

        User inDb =
            (user != null) ? (userService.findById(user.getId())) : null;

        String answer = "redirect:/webapp/profile/change-password";
        if (user == null) {
            String message = "Cannot change the password if the user is";
            message = message + "not logged in";
            throw new NotLoggedInException(message);
        } else if (inDb == null) {
            throw new UserNotFound("id", user.getId());
        } else if (newPassword.compareTo(newPasswordAgain) != 0) {
            redirectAttributes.addFlashAttribute(
                ProfileController.PARAM_NAME_NEW_PASSWORD_MATCH_FAILED,
                true);
            throw new RuntimeException("Passwords do not match");
        } else {
            newPassword = hashService.hashString(newPassword);
            userService.changePassword(user, newPassword);
            answer = "redirect:/application";
        }

        return answer;
    }
}
