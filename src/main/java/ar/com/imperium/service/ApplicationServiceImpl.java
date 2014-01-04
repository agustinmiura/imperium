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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.imperium.common.helper.PermissionHelper;
import ar.com.imperium.common.security.IRandomService;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.IRoleRepository;
import ar.com.imperium.repository.ISubjectRepository;
import ar.com.imperium.repository.jpa.ApplicationRepository;
import ar.com.imperium.repository.jpa.PermissionRepository;
import ar.com.imperium.service.interfaces.IApplicationService;

@Service("jpaComplexApplicationService")
@Transactional
public class ApplicationServiceImpl implements IApplicationService {
	private static final Logger logger = LoggerFactory
			.getLogger(ApplicationServiceImpl.class);

	private ApplicationRepository applicationRepository;

	private PermissionRepository permissionRepository;

	private ISubjectRepository subjectRepository;

	private IRoleRepository roleRepository;

	private IRandomService randomService;

	@Autowired
	@Qualifier("permissionHelper")
	private PermissionHelper permissionHelper;

	@Autowired
	@Qualifier("randomService")
	public void setRandomService(final IRandomService randomService) {
		this.randomService = randomService;
	}

	@Autowired
	@Qualifier("applicationRepository")
	public void setApplicationRepository(final ApplicationRepository appRepo) {
		appRepo.setClass(Application.class);
		this.applicationRepository = appRepo;
	}

	@Autowired
	@Qualifier("permissionRepository")
	public void setPermissionRepository(
			final PermissionRepository permissionRepository) {
		permissionRepository.setClass(Permission.class);
		this.permissionRepository = permissionRepository;
	}

	@Autowired
	@Qualifier("subjectRepository")
	public void setSubjectRepository(final ISubjectRepository subjectRepository) {
		subjectRepository.setClass(Subject.class);
		this.subjectRepository = subjectRepository;
	}

	@Autowired
	@Qualifier("roleRepository")
	public void setRoleRepository(IRoleRepository roleRepository) {
		roleRepository.setClass(Role.class);
		this.roleRepository = roleRepository;
	};

	@Transactional(readOnly = true)
	public List<Application> findAll(Integer page, Integer maxSize,
			String sortCriteria) {
		return applicationRepository.findAll(page, maxSize, sortCriteria);
	}

	@Transactional(readOnly = true)
	public List<Application> findAllWithDetail(Integer page, Integer maxSize,
			String sortCriteria) {
		return applicationRepository.findAllWithDetail(page, maxSize,
				sortCriteria);
	}

	@Override
	public Application findById(Long id) {
		return applicationRepository.findOneById(id);
	}

	@Override
	public Long getApplicationQty() {
		return applicationRepository.getEntityQty();
	}

	@Transactional
	public Application create(Application application) {
		return applicationRepository.create(application);
	}

	public Application update(Application application) {
		return applicationRepository.update(application);
	}

	@Transactional
	public void delete(Application application) {
		// TODO Auto-generated method stub
		applicationRepository.delete(application);
	}

	public void removePermissions(Application application,
			List<Permission> permissionList) throws ImperiumException {
		subRemovePermissions(application, permissionList);
	}

	private void subRemovePermissions(Application application,
			List<Permission> permissionList) throws ImperiumException {
		Set<Permission> toRemoveSet = new HashSet<Permission>();
		toRemoveSet.addAll(permissionList);

		Application foundApplication = applicationRepository
				.findOneWithDetailById(application.getId());

		Set<Permission> permissionSet = foundApplication.getPermissions();

		boolean exist;
		Permission permissionFound;
		for (Permission toRemove : toRemoveSet) {
			Long idRemoval = toRemove.getId();
			/**
			 * Try to find a permission that has been
			 * assigned to roles if we cannot find 
			 * it then the permission could exist with
			 * that id and without roles
			 */
			permissionFound = permissionRepository
					.findOneWithDetailById(idRemoval);
			if (permissionFound==null) {
				permissionFound = permissionRepository.findOneById(idRemoval);
			}
			
			if (permissionFound == null) {
				throw new RuntimeException("Permission with id " + idRemoval
						+ " cannot be removed");
			} else {
				permissionFound.clearRoles();
				permissionFound.removeApplication();
				permissionRepository.delete(permissionFound);
			}
		}
		
	}

	public Application findOneWithDetailById(Long id) throws ImperiumException {
		return applicationRepository.findOneWithDetailById(id);
	}

	public Long findTotal() {
		throw new RuntimeException(
				"Method not implemented in application service jpa implementation");
	}

	public Application findWithDetailById(Long id) throws ImperiumException {
		return applicationRepository.findOneWithDetailById(id);
	}

	public List<Application> create(List<Application> entityList)
			throws ImperiumException {
		return applicationRepository.create(entityList);
	}

	public Application update(Long id, Map<String, Object> newValues)
			throws Exception {
		throw new RuntimeException(
				"Method not implemented in application service jpa implementation");
	}

	public List<Application> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria)
			throws ImperiumException {
		throw new RuntimeException(
				"Method not implemented in application service jpa implementation");
	}

	public List<Application> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria, String direction)
			throws ImperiumException {
		throw new RuntimeException(
				"Method not implemented in application service jpa implementation");
	}

	public List<Application> findAllForApplicationWhere(
			Application application, Map<String, Object> queryParams,
			Map<String, Object> pagination, Map<String, Object> order) {
		throw new RuntimeException(
				"Method not implemented in application service jpa implementation");
	}

	public Long findQtyForApplication(Application application)
			throws ImperiumException {
		throw new RuntimeException(
				"Method not implemented in application service jpa implementation");
	}

	public Long findQtyForApplicationWhere(Application application,
			Map<String, Object> queryParams) {
		throw new RuntimeException(
				"Method not implemented in application service jpa implementation");
	}

	public void addPermissions(Application application,
			List<Permission> permissionList) throws ImperiumException {
		applicationRepository.addPermissions(application, permissionList);
	}

	@Override
	public void addRoles(Application application, List<Role> roleList)
			throws Exception {
		applicationRepository.addRoles(application, roleList);
	}

	public void addSubjects(Application application, List<Subject> subjectList)
			throws Exception {
		applicationRepository.addSubjects(application, subjectList);
	}

	public List<Application> findWithIds(List<Long> idList) {
		return applicationRepository.findWithIds(idList);
	}

	public Long getQtyForFindAllWhere(Map<String, Object> queryParams)
			throws ImperiumException {
		return applicationRepository.getQtyForFindAllWhere(queryParams);
	}

	public List<Application> findAllWhere(Map<String, Object> queryParams,
			Map<String, Object> pagination, Map<String, Object> order)
			throws ImperiumException {
		return applicationRepository.findAllWhere(queryParams, pagination,
				order);
	}

	public List<Application> findAll(Map<String, Object> pagination,
			Map<String, Object> order) throws ImperiumException {
		return applicationRepository.findAll(pagination, order);
	}

	public Map<String, Object> updateRoles(Long applicationId,
			List<Long> toAdd, List<Long> toRemove) throws Exception {
		Application application = applicationRepository
				.findOneById(applicationId);

		/**
		 * First remove the roles in toRemove from the subjects
		 */
		// get the roles to remove
		List<Role> rolesToRemove = roleRepository.findWithIds(toRemove);
		// get the subjects that own that role
		List<Subject> ownerList;
		Long subjectQty;
		Map<String, Object> paramMap;
		Map<String, Object> paginationMap;

		// order map
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("sort", "id");
		orderMap.put("direction", "ASC");

		return applicationRepository
				.updateRoles(applicationId, toAdd, toRemove);
	}

	@Override
	public void resetApiKey(Long applicationId) throws Exception {
		String newKey = randomService.generateRandomString(64);
		Application application = applicationRepository
				.findOneWithDetailById(applicationId);
		application.setApiKey(newKey);
	}
}
