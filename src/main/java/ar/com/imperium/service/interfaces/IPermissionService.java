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
package ar.com.imperium.service.interfaces;

import java.util.List;
import java.util.Map;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.exception.ImperiumException;

/**
 * @author user
 * 
 */
public interface IPermissionService extends IService<Permission, Long> {
	/**
	 * 
	 * @param page
	 * @param maxSize
	 * @param sortCriteria
	 * @return
	 * @throws ImperiumException
	 */
	public List<Permission> findAll(Integer page, Integer maxSize,
			String sortCriteria) throws ImperiumException;

	/**
	 * 
	 * @param page
	 * @param maxSize
	 * @param sortCriteria
	 * @return
	 * @throws ImperiumException
	 */
	public List<Permission> findAllWithDetail(Integer page, Integer maxSize,
			String sortCriteria) throws ImperiumException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ImperiumException
	 */
	public Permission findById(Long id) throws ImperiumException;

	/**
	 * 
	 * @param permission
	 * @return
	 * @throws ImperiumException
	 */
	public Permission create(Permission permission) throws ImperiumException;

	/**
	 * 
	 * @param permission
	 * @return
	 * @throws ImperiumException
	 */
	public Permission update(Permission permission) throws ImperiumException;

	/**
	 * @param permission
	 * @throws ImperiumException
	 */
	public void delete(Permission permission) throws ImperiumException;

	/**
	 * 
	 * @param application
	 * @param page
	 * @param maxSize
	 * @param sortCriteria
	 * @return
	 * @throws ImperiumException
	 */
	public List<Permission> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria)
			throws ImperiumException;

	public Long getPermissionQtyForApplication(Application application)
			throws ImperiumException;

	/**
	 * 
	 * @param queryParams
	 *            Map with ["applicationId"] ["roleId"] ["query"]:Optional
	 *            parameter asking to get all the permissions that contain the
	 *            query in their resource or action
	 * @param pagination
	 *            Map with ["page"] = Page to get ["maxSize"] = Max size
	 * @param order
	 *            Map with ["sort"] = sort attribute ["direction"] = direction
	 *            attribute
	 * @return
	 * @throws Exception
	 */
	public List<Permission> findAvailableForRoleInApplication(
			Map<String, Object> queryParams, Map<String, Object> pagination,
			Map<String, Object> order) throws Exception;

	public Long findQtyForAvailableForRoleInApplication(
			Map<String, Object> queryParams) throws Exception;

	/**
	 * Get all the permissions where resource starts with and belong to the
	 * application with id
	 * 
	 * @param queryParams
	 * @param pagination
	 * @param order
	 * @return
	 */
	public List<Permission> findAllWhereResource(
			Map<String, Object> queryParams, Map<String, Object> pagination,
			Map<String, Object> order) throws Exception;

	/**
	 * Get all the permissions where action starts with and belong to the
	 * application with id
	 * 
	 * @param queryParams
	 * @param pagination
	 * @param order
	 * @return
	 */
	List<Permission> findAllWhereAction(Map<String, Object> queryParams,
			Map<String, Object> pagination, Map<String, Object> order)
			throws Exception;

	/**
	 * Find only one permission with the resource , action and for the
	 * application with id
	 * 
	 * @param queryParams
	 *            Map where queryParams[applicationId] = applicationId Long
	 *            queryParams[resource] = permissionResource String
	 *            queryParams[action] = permissionAction String
	 * @return
	 * @throws Exception
	 */
	public Permission findAllWhereNameResourceApplication(
			Map<String, Object> queryParams) throws Exception;

	/**
	 * Find all the permissions with the given actions and resources in the
	 * application
	 * 
	 * @param applicationId
	 * @param resourceName
	 * @param actionList
	 * @return
	 * @throws Exception
	 */
	public List<Permission> findAllWithResourceAction(Long applicationId,
			String resourceName, List<String> actionList) throws Exception;
}
