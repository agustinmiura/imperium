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
package ar.com.imperium.test.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ar.com.imperium.domain.User;
import ar.com.imperium.service.interfaces.IUserService;

public class JpaUserTest
{
    private static final Logger logger = LoggerFactory
        .getLogger(JpaUserTest.class);

    public static void main(String[] args)
    {
        try {
            listAllWhere();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void listAllWhere() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        IUserService userService =
            (IUserService) context.getBean("jpaUserService");

        Map<String, Object> pagination = new HashMap<String, Object>();
        Map<String, Object> order = new HashMap<String, Object>();
        Map<String, Object> queryParams = new HashMap<String, Object>();

        pagination.put("page", 0);
        pagination.put("maxSize", 25);

        order.put("sort", "name");
        order.put("direction", "ASC");

        queryParams.put("query", "_12");

        List<User> listAll =
            userService.listAllWhere(queryParams, pagination, order);

        Long size = userService.getTotalWhere(queryParams);
        logger.debug("The total is :" + size);

        for (User eachUser : listAll) {
            logger.debug("The user is : " + eachUser);
        }
    }

    public static void listAll() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        IUserService userService =
            (IUserService) context.getBean("jpaUserService");

        Map<String, Object> pagination = new HashMap<String, Object>();
        Map<String, Object> order = new HashMap<String, Object>();

        pagination.put("page", 0);
        pagination.put("maxSize", 25);

        order.put("sort", "name");
        order.put("direction", "ASC");

        List<User> listAll = userService.listAll(pagination, order);

        Long size = userService.getTotal();
        logger.debug("The total is :" + size);

        for (User eachUser : listAll) {
            logger.debug("The user is : " + eachUser);
        }
    }

    public static void testGetByUserAndPassword() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        IUserService userService =
            (IUserService) context.getBean("jpaUserService");

        User user = userService.findOneByUserAndPassword("user", "password");

        logger.debug("The user found is :" + user);
    }

    public static void testChangePassword() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        IUserService userService =
            (IUserService) context.getBean("jpaUserService");

        User user = userService.findById(new Long(1));

        userService.changePassword(user, "12345678");

    }

    private static GenericApplicationContext bootstrapContext()
    {
        GenericXmlApplicationContext context =
            new GenericXmlApplicationContext();
        context.load("classpath:jpa-app-context.xml");
        context.refresh();
        return context;
    }

    private static GenericXmlApplicationContext boostrapXmlContext()
    {
        return (GenericXmlApplicationContext) bootstrapContext();
    }

}
