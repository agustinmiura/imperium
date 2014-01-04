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

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IRoleService;

public class JpaRoleTest
{
    private static final Logger logger = LoggerFactory
        .getLogger(JpaRoleTest.class);

    public static void main(String[] args)
    {
        try {
            // testFindPermissionsLike();
            // testFindPermissions();
            // testFindForApplicationLike();
            testListRolesForSubject();
            // testListRolesForSubjectLike();

            testListRolesAvailableForSubjectLike();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static GenericApplicationContext bootstrapContext()
    {
        GenericXmlApplicationContext context =
            new GenericXmlApplicationContext();
        context.load("classpath:jpa-app-context.xml");
        context.refresh();
        return context;
    }

    private static GenericXmlApplicationContext bootstrapXmlContext()
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) JpaRoleTest.bootstrapContext();
        return context;
    }

    public static void testFindAll() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        List<Role> roleList = roleService.findAll(0, 30, "name");

        for (Role eachRole : roleList) {
            logger.debug("Each role is : " + eachRole);
        }

    }

    public static void testFindOneById() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Role role = roleService.findById(new Long(1));

        logger.debug("The role is " + role);
    }

    public static void testCreate() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Application application = applicationService.findById(new Long(1));

        Role role = new Role("aName", "aDescription");
        role.setApplication(application);

        Role created = roleService.create(role);

        logger.debug("The role is created " + created);
    }

    public static void testUpdate() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Role role = roleService.findById(new Long(1803));
        role.setName("1803 new name");
        role.removeApplication();

        Role updated = roleService.update(role);

        logger.debug("The updated role is :" + updated);
    }

    public static void testFindForApplicationLike() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Application application = new Application();
        application.setId(new Long(1));

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("query", "z115");

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", new Integer(0));
        pagination.put("maxSize", 25);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", "name");
        order.put("direction", "ASC");

        List<Role> roleList =
            roleService.findAllForApplicationWhere(
                application,
                queryParams,
                pagination,
                order);

        Long qty =
            roleService.findQtyForApplicationWhere(application, queryParams);

        logger.debug("<Role list here> and the qty is :" + qty);

        logger.debug("Before the for each with list:" + roleList);
        for (Role eachRole : roleList) {

            logger.debug("Each role is :" + eachRole);
        }

    }

    public static void testFindAllForApplicationFullTest() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Application application = new Application();
        application.setId(new Long(1));

        List<Role> roleList =
            roleService.findAllForApplication(
                application,
                0,
                5,
                "description",
                "asc");

        Long qty = roleService.findQtyForApplication(application);

        logger.debug("<Role list here> and the qty is :" + qty);

        for (Role eachRole : roleList) {
            logger.debug("Each role is :" + eachRole);
        }
    }

    public static void testFindAllForApplication() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Application application = new Application();
        application.setId(new Long(1));

        List<Role> roleList =
            roleService.findAllForApplication(application, 0, 60, "name");

        Long qty = roleService.findQtyForApplication(application);

        logger.debug("<Role list here> and the qty is :" + qty);

        for (Role eachRole : roleList) {
            logger.debug("Each role is :" + eachRole);
        }
    }

    public static void testRemoval() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Role role = roleService.findById(new Long(1));

        logger.debug("To remove the role in test" + role);

        roleService.delete(role);

    }

    public static void testFindPermissions() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Role role = roleService.findById(new Long(1));

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", 0);

        pagination.put("maxSize", 10);

        List<Permission> permissionList =
            roleService.findPermissions(role, pagination);

        for (Permission eachPermission : permissionList) {
            logger.debug("The eachPermission is :" + eachPermission);
        }

    }

    public static void testFindPermissionsLike() throws Exception
    {
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Role role = roleService.findById(new Long(2));

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", 0);
        pagination.put("maxSize", 10);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", "id");
        order.put("direction", "ASC");

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("roleId", role.getId());
        queryParams.put("query", "spreadSheet");

        List<Permission> permissionList =
            roleService.findPermissionForRole(queryParams, pagination, order);

        Long totalSize = roleService.findQtyForPermissionsForRole(queryParams);
        logger.debug("The total size is :" + totalSize);

        logger.debug("The result size is :" + permissionList.size());
        for (Permission eachPermission : permissionList) {
            logger.debug("The eachPermission is :" + eachPermission.getId());
        }
    }

    public static void testListRolesForSubject() throws Exception
    {
        // Params
        Long applicationId = new Long(24);
        Long subjectId = new Long(123);
        /**
         * 
         */
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", 0);
        pagination.put("maxSize", 10);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", "id");
        order.put("direction", "ASC");

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("applicationId", applicationId);
        queryParams.put("subjectId", subjectId);

        List<Role> roleList =
            roleService.findForSubjectInApplication(
                queryParams,
                pagination,
                order);

        Long total = roleService.findQtyForSubjectInApplication(queryParams);

        logger.debug("The total records is :" + total);

        logger.debug("In list roles for subject and application");
        Integer size = roleList.size();
        logger.debug("The qty of roles is :" + size);

        for (Role eachRole : roleList) {
            logger.debug("The role with id :" + eachRole.getId());
        }

    }

    public static void testListRolesForSubjectLike() throws Exception
    {
        // Params
        Long applicationId = new Long(24);
        Long subjectId = new Long(123);
        /**
         * 
         */
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", 0);
        pagination.put("maxSize", 10);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", "id");
        order.put("direction", "ASC");

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("applicationId", applicationId);
        queryParams.put("subjectId", subjectId);
        queryParams.put("query", "upd");

        List<Role> roleList =
            roleService.findForSubjectInApplication(
                queryParams,
                pagination,
                order);

        Long total = roleService.findQtyForSubjectInApplication(queryParams);
        logger.debug("The total records is :" + total);

        logger.debug("In list roles for subject and application");
        Integer size = roleList.size();
        logger.debug("The qty of roles is :" + size);

        for (Role eachRole : roleList) {
            logger.debug("The role with id :" + eachRole.getId());
        }
    }

    public static void testListRolesAvailableForSubject() throws Exception
    {
        // Params
        Long applicationId = new Long(24);
        Long subjectId = new Long(123);
        /**
         * 
         */
        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", 0);
        pagination.put("maxSize", 10);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", "id");
        order.put("direction", "ASC");

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("applicationId", applicationId);
        queryParams.put("subjectId", subjectId);

        List<Role> roleList =
            roleService.findAvailableForSubjectInApplication(
                queryParams,
                pagination,
                order);

        Long total = roleService.findQtyForSubjectInApplication(queryParams);
        logger.debug("The total records is :" + total);

        logger.debug("In list roles for subject and application");
        Integer size = roleList.size();
        logger.debug("The qty of roles is :" + size);

        for (Role eachRole : roleList) {
            logger.debug("The role with id :" + eachRole.getId());
        }

    }

    public static void testListRolesAvailableForSubjectLike() throws Exception
    {
        // Params
        Long applicationId = new Long(24);
        Long subjectId = new Long(123);
        String query = "Upd";

        GenericXmlApplicationContext context = bootstrapXmlContext();
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Map<String, Object> pagination = new HashMap<String, Object>();
        pagination.put("page", 0);
        pagination.put("maxSize", 10);

        Map<String, Object> order = new HashMap<String, Object>();
        order.put("sort", "id");
        order.put("direction", "ASC");

        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("applicationId", applicationId);
        queryParams.put("subjectId", subjectId);
        queryParams.put("query", query);

        List<Role> roleList =
            roleService.findAvailableForSubjectInApplication(
                queryParams,
                pagination,
                order);

        Long total =
            roleService.findAvailableQtyForSubjectInApplication(queryParams);

        logger.debug("The total is : " + total);

        logger.debug("In list roles for subject and application");
        Integer size = roleList.size();
        logger.debug("The qty of roles is :" + size);

        for (Role eachRole : roleList) {
            logger.debug("The role with id :" + eachRole.getId());
        }
    }
}
