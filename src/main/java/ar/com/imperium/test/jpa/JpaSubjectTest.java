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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IRoleService;
import ar.com.imperium.service.interfaces.ISubjectService;

import com.google.common.collect.Iterables;

/**
 * @author user
 * 
 */
public class JpaSubjectTest
{
    private static final Logger logger = LoggerFactory
        .getLogger(JpaSubjectTest.class);

    public static void main(String[] args)
    {
        try {
            // testCreate();
            // testFindOne();
            // testCreateWithList();
            // testAddToApplication();
            // testFindFullAllForApplication();
            // foo2();
            // testAddSubjectToRole();
            // testFindAllForApplicationWithParams();
            // testListForRole();
            // testFindOneWithDetail();
            // testUpdateRolesOfSubject();
            // testListSubjectIds();
            testUpdateSubject();
        } catch (Exception e) {
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

    private static GenericXmlApplicationContext boostrapXmlContext()
    {
        return (GenericXmlApplicationContext) bootstrapContext();
    }

    /**
     * Functions
     * 
     * @throws Exception
     */
    public static void testCreate() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        Subject subject = new Subject("aSampleSubject");

        Subject managedSubject = subjectService.create(subject);
    }

    /**
     * Functions
     * 
     * @throws Exception
     */
    public static void testFindOne() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        Subject subject = subjectService.findById(new Long(1));

        logger.debug("The subject created is :" + subject);

    }

    /**
     * Functions perfect
     * 
     * @throws Exception
     */
    public static void testCreateWithList() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        List<Subject> subjectList = new ArrayList<Subject>();

        int max = 60;
        Subject subject = new Subject();
        for (int i = 0; i < max; i++) {
            subject = new Subject("name:" + i);
            subjectList.add(subject);
        }
        subjectService.create(subjectList);
    }

    public static void foo() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Application app = new Application("name", "description", "");

        applicationService.create(app);
    }

    public static void foo2() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Role role = new Role("roleName", "descriptionRole");

        Application application =
            applicationService.findOneWithDetailById(new Long(1));

        boolean check = application.addRoleWithValidation(role);
        applicationService.update(application);

    }

    /**
     * Tested
     * 
     * @throws Exception
     */
    public static void testAddToApplication() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Long id = new Long(1);
        Application app = applicationService.findOneWithDetailById(id);

        List<Subject> subjectList = subjectService.findAll(0, 60, "name");

        subjectService.addToApplication(app, subjectList);
    }

    public static void testFindFullAllForApplication() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Long id = new Long(1);
        Application application = applicationService.findById(id);

        Long qty = subjectService.findQtyForApplication(application);
        logger.debug("The qty is :" + qty);

        List<Subject> subjectList = new ArrayList<Subject>();
        subjectList =
            subjectService
                .findAllForApplication(application, 0, 5, "id", "asc");

        for (Subject eachSubject : subjectList) {
            logger.info("The each subject is :" + eachSubject);
        }
    }

    /**
     * Tested
     * 
     * @throws Exception
     */
    public static void testFindAllForApplication() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Long id = new Long(1);
        Application application = applicationService.findById(id);

        Long qty = subjectService.findQtyForApplication(application);
        logger.debug("The qty is :" + qty);

        List<Subject> subjectList = new ArrayList<Subject>();
        subjectList =
            subjectService.findAllForApplication(application, 0, 900, "name");

        for (Subject eachSubject : subjectList) {
            logger.info("The each subject is :" + eachSubject);
        }
    }

    /**
     * Functions ok
     * 
     * @throws Exception
     */
    public static void testAddSubjectToRole() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        Role role = roleService.findById(new Long(1));

        Application application =
            applicationService.findOneWithDetailById(new Long(1));

        List<Subject> subjectList =
            subjectService.findAllForApplication(application, 0, 600, "name");

        roleService.addToRole(role, subjectList);
    }

    public static void testFindAllForApplicationWithParams() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Long id = new Long(4);
        Application application = applicationService.findById(id);

        Map<String, Object> queryParams = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        Map<String, Object> order = new HashMap<String, Object>();

        pagination.put("page", new Integer(0));
        pagination.put("maxSize", 25);
        order.put("sort", "name");
        order.put("direction", "ASC");

        List<Subject> subjectList = new ArrayList<Subject>();
        subjectList =
            subjectService.findAllForApplicationWhere(
                application,
                queryParams,
                pagination,
                order);

        Long qty =
            subjectService.findQtyForApplicationWhere(application, queryParams);

        logger.debug("The qty of applications is :" + qty);

        for (Subject eachSubject : subjectList) {
            logger.info("The each subject is :" + eachSubject);
        }
    }

    public static void testListForRole() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Long id = new Long(4);

        Map<String, Object> queryParams = new HashMap<String, Object>();
        Map<String, Object> pagination = new HashMap<String, Object>();
        Map<String, Object> order = new HashMap<String, Object>();

        queryParams.put("query", "nb2");
        queryParams.put("roleId", id);
        pagination.put("page", new Integer(0));
        pagination.put("maxSize", 25);
        order.put("sort", "name");
        order.put("direction", "ASC");

        Long total = subjectService.findQtyForSubjectsForRole(queryParams);

        List<Subject> subjectList =
            subjectService.findSubjectsForRole(queryParams, pagination, order);

        Integer size = subjectList.size();

        logger.debug("The total is :" + total);

        logger.debug("The answer of the size is :" + size);

        for (Subject eachSubject : subjectList) {
            logger.debug("Subject with id :" + eachSubject.getId());
        }
    }

    public static void testFindOneWithDetail() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        Long id = new Long(122);
        Subject subject = subjectService.findWithDetailById(id);

        logger.debug("Got the subject");

        Application application = subject.getApplication();
        Set<Role> roles = subject.getRoles();

        logger.debug("Got the application :" + application);

        Iterator<Role> iterator = roles.iterator();
        Long eachId;
        Role eachRole;
        while (iterator.hasNext()) {
            eachRole = iterator.next();
            logger.debug("Has the role with id :" + eachRole.getId());
        }
    }

    public static void testUpdateRolesOfSubject() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");
        /***
         * Prepare data for the method
         */
        Long id = new Long(122);
        Subject subject = subjectService.findWithDetailById(id);
        Set<Role> tempRoleSet = subject.getRoles();

        // to remove
        List<Role> toRemove = new ArrayList<Role>();
        boolean transformResult = Iterables.addAll(toRemove, tempRoleSet);

        // to add
        List<Role> toAdd = new ArrayList<Role>();
        Integer startId = 85;
        Integer endId = 89;

        Role tempRole;
        Long tempId;
        for (int i = startId; i < (endId + 1); i++) {
            tempId = new Long(i);
            tempRole = roleService.findById(tempId);
            toAdd.add(tempRole);
        }

        subjectService.updateRoles(id, toAdd, toRemove);

        logger.debug("Updated the roles for the subject");

        subject = subjectService.findWithDetailById(id);
        tempRoleSet = subject.getRoles();
        for (Role eachRole : tempRoleSet) {
            logger.debug("Each role with the id:" + eachRole.getId());
        }
    }

    public static void testListSubjectIds() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();
        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");
        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");
        IApplicationService applicationService =
            (IApplicationService) context
                .getBean("jpaComplexApplicationService");

        List<Long> ids = new ArrayList<Long>();
        Long eachId;
        for (int i = 122; i < 127; i++) {
            eachId = new Long(i);
            ids.add(eachId);
        }
        List<Subject> subjectList = subjectService.findWithIds(ids);

        for (Subject eachSubject : subjectList) {
            eachId = eachSubject.getId();
            logger.debug("The subject with id :" + eachId + " has been found");
        }
    }

    public static void testUpdateSubject() throws Exception
    {
        GenericXmlApplicationContext context = boostrapXmlContext();
        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        Long id = new Long(2);
        Map<String, Object> newValues = new HashMap<String, Object>();
        newValues.put("name", "aNewName");

        Subject updated = subjectService.update(id, newValues);

        logger.debug("The updated subject is :" + updated);
    }
}
