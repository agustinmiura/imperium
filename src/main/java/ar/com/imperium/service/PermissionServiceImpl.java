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
package ar.com.imperium.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.jpa.PermissionRepository;
import ar.com.imperium.service.interfaces.IPermissionService;

@Service("jpaComplexPermissionService")
@Transactional
public class PermissionServiceImpl implements IPermissionService {
	private static final Logger logger = LoggerFactory
			.getLogger(PermissionServiceImpl.class);

	private PermissionRepository permissionRepository;

	@Autowired
	@Qualifier("permissionRepository")
	public void setPermissionRepository(
			final PermissionRepository permissionRepository) {
		permissionRepository.setClass(Permission.class);
		this.permissionRepository = permissionRepository;
	}

	@Override
	public List<Permission> findAll(Integer page, Integer maxSize,
			String sortCriteria) throws ImperiumException {
		return permissionRepository.findAll(page, maxSize, sortCriteria);
	}

	@Override
	public List<Permission> findAllWithDetail(Integer page, Integer maxSize,
			String sortCriteria) throws ImperiumException {
		throw new RuntimeException("Method not implemented");
	}

	@Override
	public Permission findById(Long id) throws ImperiumException {
		return permissionRepository.findOneById(id);
	}

	@Override
	public Permission create(Permission permission) throws ImperiumException {
		return permissionRepository.create(permission);
	}

	@Override
	public Permission update(Permission permission) throws ImperiumException {
		return permissionRepository.update(permission);

	}

	@Override
	public void delete(Permission permission) throws ImperiumException {
		permissionRepository.delete(permission);
	}

	@Override
	public List<Permission> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria)
			throws ImperiumException {
		return permissionRepository.findAllForApplication(application, page,
				maxSize, sortCriteria);
	}

	@Override
	public Long getPermissionQtyForApplication(Application application)
			throws ImperiumException {
		return permissionRepository.getQtyForApplication(application);
	}

	@Override
	public Long findTotal() {
		throw new RuntimeException("Method not implementedd");
	}

	@Override
	public Permission findWithDetailById(Long id) throws ImperiumException {
		throw new RuntimeException("Method not implementedd");
	}

	@Override
	public List<Permission> create(List<Permission> entityList)
			throws ImperiumException {
		return permissionRepository.create(entityList);
	}

	@Override
	public Permission update(Long id, Map<String, Object> newValues)
			throws Exception {
		throw new RuntimeException("Method not implementedd");
	}

	@Override
	public List<Permission> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria, String direction)
			throws ImperiumException {
		return permissionRepository.findAllForApplication(application, page,
				maxSize, sortCriteria, direction);
	}

	@Override
	public Long findQtyForApplication(Application application)
			throws ImperiumException {
		return permissionRepository.getQtyForApplication(application);
	}

	@Override
	public List<Permission> findAllForApplicationWhere(Application application,
			Map<String, Object> queryParams, Map<String, Object> pagination,
			Map<String, Object> order) {
		return permissionRepository.findAllForApplicationWhere(application,
				queryParams, pagination, order);
	}

	@Override
	public Long findQtyForApplicationWhere(Application application,
			Map<String, Object> queryParams) {
		return permissionRepository.findQtyForApplicationWhere(application,
				queryParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ar.com.imperium.service.interfaces.IPermissionService#
	 * findAvailableForRoleInApplication(java.util.Map, java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public List<Permission> findAvailableForRoleInApplication(
			Map<String, Object> queryParams, Map<String, Object> pagination,
			Map<String, Object> order) throws Exception {
		return permissionRepository.findAvailableForRoleInApplication(
				queryParams, pagination, order);
	}

	@SuppressWarnings("finally")
	@Override
	public Long findQtyForAvailableForRoleInApplication(
			Map<String, Object> queryParams) throws Exception {
		Long qty = null;
		try {
			qty = permissionRepository
					.findQtyForAvailableForRoleInApplication(queryParams);
		} catch (NoResultException e) {
			qty = new Long(0);
		} finally {
			return qty;
		}
	}

	@Override
	public List<Permission> findWithIds(List<Long> idList) {
		return permissionRepository.findWithIds(idList);
	}

	/**
	 * Iterate a list with permissions and remove the ones with duplicate
	 * resource name
	 * 
	 * @param permissionList
	 * @return
	 */
	private List<Permission> removeWithSameResource(
			List<Permission> permissionList) {
		List<Permission> answer = new ArrayList<Permission>();

		Map<String, Permission> permissionMap = new HashMap<String, Permission>();
		String eachResourceName = null;
		for (Permission permission : permissionList) {
			eachResourceName = permission.getResource();
			if (!permissionMap.containsKey(eachResourceName)) {
				answer.add(permission);
				permissionMap.put(eachResourceName, permission);
			}
		}
		return answer;
	}

	@Override
	public List<Permission> findAllWhereResource(
			Map<String, Object> queryParams, Map<String, Object> pagination,
			Map<String, Object> order) throws Exception {
		List<Permission> permissionList = permissionRepository
				.findAllWhereResource(queryParams, pagination, order);

		return (removeWithSameResource(permissionList));
	}

	@Override
	public List<Permission> findAllWhereAction(Map<String, Object> queryParams,
			Map<String, Object> pagination, Map<String, Object> order)
			throws Exception {
		return permissionRepository.findAllWhereAction(queryParams, pagination,
				order);
	}

	@Override
	public Permission findAllWhereNameResourceApplication(
			Map<String, Object> queryParams) throws Exception {
		// TODO Auto-generated method stub
		return permissionRepository
				.findAllWhereNameResourceApplication(queryParams);
	}

	@Override
	public List<Permission> findAllWithResourceAction(Long applicationId,
			String resourceName, List<String> actionList) throws Exception {

		return permissionRepository.findAllWithResourceAction(applicationId,
				resourceName, actionList);
	}

}
