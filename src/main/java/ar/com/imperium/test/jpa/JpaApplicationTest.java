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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ar.com.imperium.common.security.IRandomService;
import ar.com.imperium.common.security.RandomServiceImpl;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.jpa.ApplicationRepository;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IPermissionService;
import ar.com.imperium.service.interfaces.IRoleService;

/**
 * @author user
 * 
 */
public class JpaApplicationTest
{
    private static final Logger logger = Logger
        .getLogger(JpaApplicationTest.class);

    public static void main(String[] args)
    {
        try {

        	System.out.println("Start");
            testRemoval();
            System.out.println("End");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            System.out.println("Message:" + e.getMessage());

            System.out.println("Cause" + e.getCause());
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

    public static void testCreate() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IRandomService randomService = new RandomServiceImpl();
        Application application =
            new Application("name", "description", "ssssssss");
        service.create(application);

        logger.debug("The application created is : " + application);
    }

    public static void testCount() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);
        Long qty = service.getApplicationQty();
        System.out.println("The qty " + qty);
    }

    public static void testApplicationRepository() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        ApplicationRepository repository =
            (ApplicationRepository) context.getBean("applicationRepository");
    }

    public static void showApplication(Application application)
    {
        System.out.println("See the application : " + application);
        Set<Role> roles = application.getRoles();
        for (Role eachRole : roles) {
            System.out.println("The role is : " + eachRole);
        }
    }

    public static void testPagination() throws ImperiumException
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        List<Application> appList = service.findAll(0, 25, null);

        for (Application application : appList) {
            System.out.println("The id is:" + application.getId());
        }
    }

    public static void testUpdate() throws ImperiumException
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        Application application = service.findById(new Long(1));
        application.setDescription("new description");
        service.update(application);

        showApplication(application);
    }

    /**
     * Test removePermissions from the application service
     * 
     * @throws Exception
     */
    public static void testPermissionsRemoval() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        Application application = service.findOneWithDetailById(new Long(1));

        Set<Permission> permissions = application.getPermissions();

        boolean found = false;
        Long eachId;
        Permission permissionFound = null;
        for (Permission eachPermission : permissions) {
            eachId = eachPermission.getId();
            found = eachId.compareTo(new Long(112)) == 0;
            if (found) {
                permissionFound = eachPermission;
                break;
            }
        }

        List<Permission> permissionList = new ArrayList<Permission>();
        if (permissionFound != null) {
            permissionList.add(permissionFound);
            service.removePermissions(application, permissionList);
            System.out.println("I removed the permissions");
        } else {
            System.out.println("Cannot found the permission");
        }
    }

    public static void testRemoval() throws ImperiumException
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        Application application = service.findById(new Long(103));

        service.delete(application);

    }

    public static void testFindById() throws ImperiumException
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        Application application = service.findById(new Long(24));

        showApplication(application);
    }

    /**
     * Tested and functions
     * 
     * @throws ImperiumException
     */
    public static void massiveInsertion() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IPermissionService permissionService =
            (IPermissionService) context.getBean("jpaComplexPermissionService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        /**
         * Generate 600 excell applications with roles and description
         * "sampleSpreadSheet"
         */

        int start = 0;
        int max = 2;
        String baseName = "Excell version:";
        String description = "Excell application version number:";

        int i = 0;
        Application eachApplication = null;
        Role eachRole = null;
        Permission eachPermission = null;

        String[] roleNames =
            { "CreateSheetRole", "UpdateSheetRole", "RemoveSheetRole" };
        String[] actionNames = { "admin", "read", "print" };
        String[] resourceNames =
            { "spreadSheet", "graphics", "documentProperties" };

        String prefixSubject = "subject";
        Integer firstSubjectIndex = 1;
        Integer lastSubjectIndex = 100;

        List<Role> roleList = new ArrayList<Role>();
        List<Permission> permissionList = new ArrayList<Permission>();
        List<Application> applicationList = new ArrayList<Application>();
        String tempName;
        Application tempApp;
        IRandomService randomService = new RandomServiceImpl();
        for (i = 0; i < max; i++) {
            tempName = baseName + i;
            eachApplication =
                new Application(
                    tempName,
                    description + i,
                    randomService.generateRandomString(64));
            roleList = new ArrayList<Role>();
            permissionList = new ArrayList<Permission>();

            logger.info("<----- Processing application : " + tempName + "--->");

            logger.info("<---Adding roles to the application--->");
            /**
             * Add roles for the application
             */
            for (String eachName : roleNames) {
                eachRole = new Role(eachName, eachName);
                eachRole.setApplication(eachApplication);
                eachApplication.addRole(eachRole);
                roleList.add(eachRole);
            }

            logger.info("<--Adding permissions to the application-->");

            /**
             * Add permissions for the application
             */
            boolean check = false;
            for (String eachResourceName : resourceNames) {
                for (String eachActionName : actionNames) {
                    eachPermission =
                        new Permission(
                            eachResourceName,
                            eachActionName,
                            eachApplication);
                    permissionList.add(eachPermission);
                }
            }
            for (Permission tempPermission : permissionList) {
                check = eachApplication.addPermission(tempPermission);
            }

            tempApp = service.create(eachApplication);
            applicationList.add(tempApp);
        }

        addPermissionsToRole(applicationList, context);
    }

    private static void addPermissionsToRole(List<Application> appList,
        GenericXmlApplicationContext context) throws Exception
    {
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IPermissionService permissionService =
            (IPermissionService) context.getBean("jpaComplexPermissionService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        List<Role> roleList;
        List<Permission> permissionList;
        for (Application eachApplication : appList) {
            permissionList =
                permissionService.findAllForApplication(
                    eachApplication,
                    0,
                    1000,
                    "name");

            roleList =
                roleService.findAllForApplication(
                    eachApplication,
                    0,
                    1000,
                    "name");

            for (Role eachRole : roleList) {
                roleService.assignPermissions(eachRole, permissionList);
            }
        }

    }

    public static void testFindAllWhere() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();

        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        Map<String, Object> queryParams = new HashMap<String, Object>();
        Map<String, Object> paginationMap = new HashMap<String, Object>();
        Map<String, Object> orderMap = new HashMap<String, Object>();

        queryParams.put("query", "z11");

        paginationMap.put("page", 0);
        paginationMap.put("maxSize", 25);

        orderMap.put("order", "id");
        orderMap.put("direction", "ASC");

        List<Application> appList =
            service.findAllWhere(queryParams, paginationMap, orderMap);

        for (Application eachApp : appList) {
        }
    }

    public static void testUpdateRolesOfAnApplication() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();

        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        Long applicationId = new Long(24);

        Long eachId;
        String[] idStringArray = new String[] {};

        List<Long> idsToAdd = new ArrayList<Long>();
        idStringArray = new String[] {};

        List<Long> idsToRemove = new ArrayList<Long>();
        idStringArray = new String[] { "80", "81", "82", "83", "84" };
        for (String eachString : idStringArray) {
            idsToRemove.add(new Long(eachString));
        }

        Map<String, ?> answer =
            service.updateRoles(applicationId, idsToAdd, idsToRemove);

        List<Role> added = (List<Role>) answer.get("added");
        List<Role> removed = (List<Role>) answer.get("removed");

    }

}
