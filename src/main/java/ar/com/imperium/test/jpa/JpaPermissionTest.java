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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ar.com.imperium.common.security.IRandomService;
import ar.com.imperium.dbutils.SampleDbCreator;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IPermissionService;

/**
 * @author user
 * 
 */
public class JpaPermissionTest {
	private static final Logger logger = LoggerFactory
			.getLogger(JpaPermissionTest.class);

	public static void main(String[] args) {
		try {
			System.out.println("Permission test");
			testReproduceBug2();
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static GenericApplicationContext bootstrapContext() {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext();
		context.load("classpath:jpa-app-context.xml");
		context.refresh();
		return context;
	}

	private static void testReproduceBug2() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService applicationService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		Long id = new Long(101);

		Application application = applicationService.findWithDetailById(id);

		Permission permission = Permission.createWithId(new Long(152));
		List<Permission> permissionList = new ArrayList<Permission>();
		permissionList.add(permission);

		applicationService.removePermissions(application, permissionList);

		System.out.println("Finished with success the removal here");
	}

	/**
	 * If we try to remove a permission that belongs to an application but it is
	 * not assigned to any role then a bug appears this method reproduces this
	 * bug.
	 * 
	 * @throws Exception
	 */
	public static void testReproduceBug() throws Exception {

		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService applicationService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		Long id = new Long(100);

		Application application = applicationService.findWithDetailById(id);

		Long permissionId = new Long(113);
		Permission permission = Permission.createWithId(permissionId);
		List<Permission> toRemoveList = new ArrayList<Permission>();
		toRemoveList.add(permission);

		applicationService.removePermissions(application, toRemoveList);

		int status = -11;
	}

	public static void testFullFindAllForApplication() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService appService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		Application application = appService.findById(new Long(1));

		List<Permission> permissionList = permissionService
				.findAllForApplication(application, 0, 25, "application");

		Long qty = permissionService
				.getPermissionQtyForApplication(application);

		logger.debug("The application qty is :" + qty);

		for (Permission eachPermission : permissionList) {
			logger.debug("The permission is :" + eachPermission);
		}
	}

	public static void testPagination() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService appService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		Application application = appService.findById(new Long(1));

		List<Permission> permissionList = permissionService
				.findAllForApplication(application, 0, 25, null);

		Long qty = permissionService
				.getPermissionQtyForApplication(application);

		logger.debug("The application qty is :" + qty);

		for (Permission eachPermission : permissionList) {
			logger.debug("The permission is :" + eachPermission);
		}
	}

	/**
	 * Done
	 * 
	 * @throws Exception
	 */
	public static void testUpdate() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		Permission permission = permissionService.findById(new Long(2));

		permission.setAction("new action");

		permissionService.update(permission);
	}

	/**
	 * Tested
	 * 
	 * @throws Exception
	 */
	public static void testRemove() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();
		/*
		 * IPermissionService permissionService = (IPermissionService)
		 * context.getBean("jpaComplexPermissionService");
		 * 
		 * Permission permission = permissionService.findById(new Long(1));
		 * 
		 * permissionService.delete(permission);
		 */

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService appService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		/*
		 * Application application = appService.findById(new Long(1));
		 */
		Application application = appService.findById(new Long(2));

		List<Permission> permissionList = new ArrayList<Permission>();
		Integer start = 10;
		Integer max = 13;
		Permission permission = null;
		String tempString;
		for (int i = start; i < max; i++) {
			tempString = (new Integer(i)).toString();
			permission = new Permission();
			permission.setId(Long.parseLong(tempString));
			permissionList.add(permission);
		}

		appService.removePermissions(application, permissionList);

		/*
		 * boolean result = application.removePermission(permission);
		 * 
		 * appService.update(application); permissionService.update(permission);
		 */
		logger.debug("The result is :");
	}

	/**
	 * @Tested
	 * @throws Exception
	 */
	public static void testFindOne() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		Permission permission = permissionService.findById(new Long(1));

		logger.debug("Found the permission :" + permission);
	}

	/**
	 * Tested
	 * 
	 * @throws Exception
	 */
	public static void testPermission() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();
		Permission permission = new Permission("users", "admin");
		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		permissionService.create(permission);

	}

	/**
	 * Tested
	 * 
	 * @throws Exception
	 */
	public static void testListAll() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();
		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		List<Permission> permissionList = permissionService
				.findAll(0, 15, null);

		for (Permission eachPermission : permissionList) {
			logger.debug("I see the permission" + eachPermission);
		}

	}

	/**
	 * Tested
	 * 
	 * @throws Exception
	 */
	public static void massiveInsertion() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();
		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService appService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		IRandomService randomService = (IRandomService) context
				.getBean("randomService");

		Application application = new Application("name", "description",
				randomService.generateRandomString(16));
		application = appService.create(application);

		Permission eachPermission;
		Integer start = 0;
		Integer last = 12;
		for (int i = start; i < last; i++) {
			logger.debug("The index is :" + i);

			eachPermission = new Permission("resource:" + i, "action:" + i);

			application.addPermission(eachPermission);

			permissionService.create(eachPermission);

			logger.debug("Created the permission:" + eachPermission);
		}

	}

	public static void testFindAllForApplicationWithParams() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService appService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		Application application = appService.findById(new Long(1));

		Map<String, Object> queryParams = new HashMap<String, Object>();
		Map<String, Object> pagination = new HashMap<String, Object>();
		Map<String, Object> order = new HashMap<String, Object>();

		queryParams.put("query", "doc");
		pagination.put("page", new Integer(0));
		pagination.put("maxSize", 25);
		order.put("sort", "name");
		order.put("direction", "ASC");

		List<Permission> permissionList = permissionService
				.findAllForApplicationWhere(application, queryParams,
						pagination, order);

		logger.debug("The result size is :" + permissionList.size());

		Long qty = permissionService.findQtyForApplicationWhere(application,
				queryParams);

		logger.debug("The application qty is :" + qty);

		for (Permission eachPermission : permissionList) {
			logger.debug("The permission is :" + eachPermission);
		}
	}

	public static void testListAvailableForRoleWithParams() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		IApplicationService appService = (IApplicationService) context
				.getBean("jpaComplexApplicationService");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		Map<String, Object> pagination = new HashMap<String, Object>();
		Map<String, Object> order = new HashMap<String, Object>();

		queryParams.put("applicationId", new Long(1));
		queryParams.put("roleId", new Long(7));
		pagination.put("page", new Integer(0));
		pagination.put("maxSize", 100);
		order.put("sort", "id");
		order.put("direction", "ASC");

		List<Permission> permissionList = permissionService
				.findAvailableForRoleInApplication(queryParams, pagination,
						order);
		/*
		 * Long qty = permissionService
		 * .findQtyForAvailableForRoleInApplication(queryParams);
		 * 
		 * logger.debug("Total records to be get : " + qty);
		 */
		logger.debug("The result size is :" + permissionList.size());

		for (Permission eachPermission : permissionList) {
			logger.debug("The id is :" + eachPermission.getId());
		}

	}

	public static void testFindForResourcePrefix() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		Map<String, Object> pagination = new HashMap<String, Object>();
		Map<String, Object> order = new HashMap<String, Object>();

		queryParams.put("applicationId", new Long(2));
		queryParams.put("resource.prefix", "water");
		pagination.put("page", new Integer(0));
		pagination.put("maxSize", 100);
		order.put("sort", "resource");
		order.put("direction", "ASC");

		List<Permission> permissionList = permissionService
				.findAllWhereResource(queryParams, pagination, order);

		for (Permission eachPermission : permissionList) {
			logger.debug("Each permission:" + eachPermission.getId()
					+ " and resource:" + eachPermission.getResource());
		}
	}

	/**
	 * @todo test
	 * @throws Exception
	 */
	public static void testFindByActionResourceAndApplicationId()
			throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("applicationId", new Long(100));
		queryParams.put("resource", "account");
		queryParams.put("action", "create");

		Permission permission = permissionService
				.findAllWhereNameResourceApplication(queryParams);

		System.out.println("The permission is :" + permission);
	}

	public static void testFindForActionPrefix() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();

		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		Map<String, Object> queryParams = new HashMap<String, Object>();
		Map<String, Object> pagination = new HashMap<String, Object>();
		Map<String, Object> order = new HashMap<String, Object>();

		queryParams.put("applicationId", new Long(4));
		queryParams.put("action.prefix", "a");
		pagination.put("page", new Integer(0));
		pagination.put("maxSize", 2);
		order.put("sort", "id");
		order.put("direction", "ASC");

		List<Permission> permissionList = permissionService.findAllWhereAction(
				queryParams, pagination, order);

		logger.debug("<START>");

		for (Permission eachPermission : permissionList) {
			logger.debug("Each permission:" + eachPermission.getId()
					+ " and resource:" + eachPermission.getAction());
		}

		logger.debug("////////////////////////////////////END");
	}

	public static void testFindAllWithResourceAction() throws Exception {
		GenericXmlApplicationContext context = (GenericXmlApplicationContext) bootstrapContext();
		IPermissionService permissionService = (IPermissionService) context
				.getBean("jpaComplexPermissionService");

		Long applicationId = new Long(100);
		String resourceName = "user";

		String[] actionArray = new String[] { "create", "read", "update",
				"customAction1" };
		List<String> stringList = new ArrayList<String>();
		for (String action : actionArray) {
			stringList.add(action);
		}

		List<Permission> permissionList = permissionService
				.findAllWithResourceAction(applicationId, resourceName,
						stringList);

		System.out.println("End the test here");
	}
}
