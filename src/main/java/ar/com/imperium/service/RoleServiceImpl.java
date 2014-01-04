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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.jpa.PermissionRepository;
import ar.com.imperium.repository.jpa.RoleRepository;
import ar.com.imperium.service.interfaces.IRoleService;

@Service("jpaComplexRoleService")
@Transactional
public class RoleServiceImpl implements IRoleService
{
    private static final Logger logger = LoggerFactory
        .getLogger(RoleServiceImpl.class);

    private RoleRepository roleRepository;

    private PermissionRepository permissionRepository;

    @Autowired
    @Qualifier("roleRepository")
    public void setRoleRepository(final RoleRepository roleRepository)
    {
        roleRepository.setClass(Role.class);
        this.roleRepository = roleRepository;
    }

    @Autowired
    @Qualifier("permissionRepository")
    public void setPermissionRepository(
        final PermissionRepository permissionRepository)
    {
        permissionRepository.setClass(Permission.class);
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Role> findAll(Integer offset, Integer maxSize,
        String sortCriteria) throws ImperiumException
    {
        return roleRepository.findAll(offset, maxSize, sortCriteria);
    }

    @Override
    public List<Role> findAllWithDetail(Integer offset, Integer maxSize,
        String sortCriteria) throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Role findById(Long id) throws ImperiumException
    {
        return roleRepository.findOneById(id);
    }

    @Override
    public Role findWithDetailById(Long id) throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Role create(Role entityData) throws ImperiumException
    {
        return roleRepository.create(entityData);
    }

    @Override
    public Role update(Role entity) throws ImperiumException
    {
        return roleRepository.update(entity);
    }

    @Override
    public void delete(Role entity) throws ImperiumException
    {
        roleRepository.delete(entity);
    }

    @Override
    public List<Role> findAllForApplication(Application application,
        Integer offset, Integer maxSize, String sortCriteria)
        throws ImperiumException
    {
        return this.roleRepository.findAllForApplication(
            application,
            offset,
            maxSize,
            sortCriteria);
    }

    @Override
    public Long findQtyForApplication(Application application)
        throws ImperiumException
    {
        return roleRepository.findQtyForApplication(application);
    }

    @Override
    public Role update(Long id, Map<String, Object> newValues) throws Exception
    {
        return roleRepository.update(id, newValues);
    }

    public void assignPermissions(Role role, List<Permission> permissionList)
        throws ImperiumException
    {
        roleRepository.assignPermissions(role, permissionList);
    }

    @Override
    public List<Role> create(List<Role> entityList) throws ImperiumException
    {
        return roleRepository.create(entityList);
    }

    @Override
    public List<Subject> addToRole(Role role, List<Subject> subjectList)
        throws ImperiumException
    {
        return roleRepository.addToRole(role, subjectList);
    }

    @Override
    public Long findTotal()
    {
        return roleRepository.getEntityQty();
    }

    @Override
    public List<Role> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria, String direction)
        throws ImperiumException
    {
        return roleRepository.findAllForApplication(
            application,
            page,
            maxSize,
            sortCriteria,
            direction);
    }

    @Override
    public List<Role> findAllForApplicationWhere(Application application,
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order)
    {
        return roleRepository.findAllForApplicationWhere(
            application,
            queryParams,
            pagination,
            order);
    }

    public Long findQtyForApplicationWhere(Application application,
        Map<String, Object> queryParams)
    {
        return roleRepository.findQtyForApplicationWhere(
            application,
            queryParams);
    }

    @Override
    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination)
    {
        return roleRepository.findPermissions(role, pagination);
    }

    @Override
    public Long findPermissionQty(Role role)
    {
        return roleRepository.findPermissionQty(role);
    }

    @Override
    public Map<String, Object> updatePermissions(Long roleId, List<Long> toAdd,
        List<Long> toRemove) throws ImperiumException
    {
        Role role = roleRepository.findOneById(roleId);

        List<Permission> permissionsToAdd = new ArrayList<Permission>();
        List<Permission> permissionsToRemove = new ArrayList<Permission>();

        for (Long eachId : toAdd) {
            permissionsToAdd.add(permissionRepository.findOneById(eachId));
        }

        for (Long eachId : toRemove) {
            permissionsToRemove.add(permissionRepository.findOneById(eachId));
        }

        roleRepository.assignPermissions(role, permissionsToAdd);
        roleRepository.removePermissions(role, permissionsToRemove);

        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("added", toAdd);
        answer.put("removed", toRemove);

        return answer;
    }

    @Override
    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination, Map<String, Object> order)
    {
        return roleRepository.findPermissions(role, pagination, order);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ar.com.imperium.service.interfaces.IRoleService#findQtyForPermissionsForRole
     * (java.util.Map)
     */
    @Override
    public Long findQtyForPermissionsForRole(Map<String, Object> queryParams)
    {
        return roleRepository.findQtyForPermissionsForRole(queryParams);
    }

    public List<Permission> findPermissionForRole(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order)
    {
        return roleRepository.findPermissionForRole(
            queryParams,
            pagination,
            order);
    }

    public List<Role> findForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        return roleRepository.findForSubjectInApplication(
            queryParams,
            pagination,
            order);
    }

    public Long findQtyForSubjectInApplication(Map<String, Object> queryParams)
        throws Exception
    {
        return roleRepository.findQtyForSubjectInApplication(queryParams);
    }

    public Long findAvailableQtyForSubjectInApplication(
        Map<String, Object> queryParams) throws Exception
    {
        return roleRepository
            .findAvailableQtyForSubjectInApplication(queryParams);
    }

    public List<Role> findAvailableForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        return roleRepository.findAvailableForSubjectInApplication(
            queryParams,
            pagination,
            order);
    }

    @Override
    public List<Role> findWithIds(List<Long> idList)
    {
        return roleRepository.findWithIds(idList);
    }

    public List<Subject> removeRoleFromSubjects(Subject subject)
        throws Exception
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public void remove(List<Role> toRemove) throws Exception
    {
        Role fromDbRole = null;

        Set<Subject> subjectSet = null;

        Set<Permission> permissionSet = null;

        for (Role eachToRemove : toRemove) {
            fromDbRole =
                roleRepository.findOneWithDetailById(eachToRemove.getId());
            // remove application
            fromDbRole.removeApplication();

            // remove from the subjects being used
            subjectSet = fromDbRole.getSubjects();
            for (Subject eachSubject : subjectSet) {
                eachSubject.removeRole(fromDbRole);
            }

            // remove all the permissions from the subject
            permissionSet = fromDbRole.getPermissions();
            for (Permission eachPermission : permissionSet) {
                eachPermission.removeRole(fromDbRole);
            }
        }
    }

}
