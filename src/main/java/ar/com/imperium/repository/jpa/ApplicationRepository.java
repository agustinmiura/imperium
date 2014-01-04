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
package ar.com.imperium.repository.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.exception.InvalidRoleAdditionToApplication;
import ar.com.imperium.repository.IApplicationRepository;

@Service("applicationRepository")
@Repository
public class ApplicationRepository extends
    AbstractJpaRepository<Application, Long> implements IApplicationRepository
{

    private static final Logger logger = LoggerFactory
        .getLogger(ApplicationRepository.class);

    private Map<String, String> getSortMap()
    {
        Map<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("id", "application.id");
        hashMap.put("name", "application.name");
        hashMap.put("description", "application.description");

        return hashMap;
    }

    public List<Application> findAllWithDetail(Integer page, Integer maxSize,
        String sortCriteria)
    {
        String sql =
            "SELECT distinct a " + "FROM Application a "
                + " LEFT JOIN FETCH a.roles "
                + " LEFT JOIN FETCH a.permissions "
                + " LEFT JOIN FETCH a.subjects ";
        TypedQuery<Application> findAllQuery =
            em.createQuery(sql, Application.class);

        findAllQuery.setMaxResults(new Integer(maxSize.toString()));
        findAllQuery.setFirstResult(page * maxSize);

        return findAllQuery.getResultList();
    }

    public Application findOneWithDetailById(Long id)
    {
        String sql =
            "SELECT distinct a " + "FROM Application a "
                + " LEFT JOIN FETCH a.roles "
                + " LEFT JOIN FETCH a.permissions "
                + " LEFT JOIN FETCH a.subjects " + " WHERE a.id = :id ";
        TypedQuery<Application> query = em.createQuery(sql, Application.class);

        query.setParameter("id", id);
        Application application = query.getSingleResult();

        return application;
    }

    public String getEntityNameForQueries()
    {
        // TODO Auto-generated method stub
        return "Application";
    }

    public Application update(Long id, Map<String, Object> newValues)
    {
        throw new RuntimeException("Method not implemented");
    }

    public List<Application> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria, String direction)
    {
        throw new RuntimeException("Method not implemented");
    }

    public Long getQtyForApplication(Application application)
    {
        throw new RuntimeException("Method not implemented");
    }

    public List<Application> findAllForApplicationWhere(
        Application application, Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
    {
        throw new RuntimeException("Method not implemented");
    }

    public Long findQtyForApplicationWhere(Application application,
        Map<String, Object> queryParams)
    {
        throw new RuntimeException("Method not implemented");
    }

    public void addPermissions(Application application,
        List<Permission> permissionList) throws ImperiumException
    {
        Application found = this.findOneWithDetailById(application.getId());

        boolean addSuccess = false;
        Permission managedPermission = null;
        for (Permission permission : permissionList) {
            if (!em.contains(permission)) {
                managedPermission =
                    em.find(Permission.class, permission.getId());
            } else {
                managedPermission = permission;
            }

            managedPermission.removeApplication();
            addSuccess = found.addPermission(managedPermission);
            if (!addSuccess) {
                StringBuffer sBuffer =
                    new StringBuffer("The permission with id");
                sBuffer.append(" " + managedPermission.getId()
                    + " cannot be added");
                sBuffer.append("to the application with id :" + found.getId());
                throw new RuntimeException(sBuffer.toString());
            }
        }
    }

    public void addRoles(Application application, List<Role> roleList)
        throws Exception
    {
        Application managedApp =
            this.findOneWithDetailById(application.getId());

        boolean addSuccess = false;
        Role managedRole;
        for (Role eachRole : roleList) {
            if (em.contains(eachRole)) {
                managedRole = eachRole;
            } else {
                managedRole = em.find(Role.class, eachRole.getId());
            }

            managedRole.removeApplication();
            addSuccess = managedApp.addRoleWithValidation(managedRole);
            if (!addSuccess) {
                throw new InvalidRoleAdditionToApplication(
                    managedApp,
                    managedRole);
            }
        }

    }

    public void addSubjects(Application application, List<Subject> subjectList)
        throws Exception
    {
        Application managedApp = findOneWithDetailById(application.getId());

        boolean addSuccess = false;
        Subject managedSubject;
        for (Subject eachSubject : subjectList) {
            if (em.contains(eachSubject)) {
                managedSubject = eachSubject;
            } else {
                managedSubject = em.find(Subject.class, eachSubject.getId());
            }
            managedSubject.removeApplication();
            addSuccess = managedApp.addSubject(managedSubject);
            if (!addSuccess) {
                throw new RuntimeException("Cannot add subject to application");
            }
        }

    }

    public List<Application> findWithIds(List<Long> idList)
    {
        return super.findWithIds(idList);
    }

    public List<Application> findAllWhere(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws ImperiumException
    {
        String queryParameter = (String) queryParams.get("query");

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        String jpql = "SELECT application FROM Application application";
        jpql = jpql + " WHERE ( application.name LIKE :query ";
        jpql = jpql + " OR application.description LIKE :query )";

        Map<String, String> sortMap = this.getSortMap();
        String jpqlOrder = " ORDER BY " + sortMap.get(sort) + " " + direction;
        jpql = jpql + jpqlOrder;

        TypedQuery<Application> query = em.createQuery(jpql, Application.class);
        query.setParameter("query", "%" + queryParameter + "%");

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    public Long getQtyForFindAllWhere(Map<String, Object> queryParams)
        throws ImperiumException
    {
        String queryParameter = (String) queryParams.get("query");

        String jpql =
            "SELECT COUNT(application.id) FROM Application application";
        jpql = jpql + " WHERE ( application.name LIKE :query ";
        jpql = jpql + " OR application.description LIKE :query )";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("query", "%" + queryParameter + "%");

        return executeCountQuery(query);
    }

    public List<Application> findAll(Map<String, Object> pagination,
        Map<String, Object> order) throws ImperiumException
    {
        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        String jpql = "SELECT application FROM Application application";

        Map<String, String> sortMap = this.getSortMap();
        String jpqlOrder = " ORDER BY " + sortMap.get(sort) + " " + direction;
        jpql = jpql + jpqlOrder;

        TypedQuery<Application> query = em.createQuery(jpql, Application.class);

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    /**
     * To update each role :
     * 
     * 1_Add all the roles to the application. 2_Remove the roles from the
     * application.
     * 
     * This method has the precondition that the role must not be used by a
     * subject
     * 
     * @param application
     * @param rolesToAdd
     * @param rolesToRemove
     * @return
     * @throws ImperiumException
     */
    public Map<String, Object> updateRoles(Application application,
        List<Role> rolesToAdd, List<Role> rolesToRemove)
        throws ImperiumException
    {
        /**
         * @todo remove
         */

        Application managedApplication = application;
        if (!em.contains(application)) {
            managedApplication = em.merge(application);
        }

        List<Role> rolesAdded = new ArrayList<Role>();
        List<Role> rolesRemoved = new ArrayList<Role>();

        boolean successOperation = false;
        Role managedRole = null;
        // add roles to the application
        for (Role eachRole : rolesToAdd) {
            managedRole = eachRole;
            if (!em.contains(eachRole)) {
                managedRole = em.merge(eachRole);
            }

            successOperation = true;
            if (!managedApplication.containsRole(eachRole)) {
                successOperation =
                    application.addRoleWithValidation(managedRole);
            }
            if (!successOperation) {
                throw new RuntimeException("Cannot add role to the subject");
            }
            rolesAdded.add(managedRole);
        }
        // remove roles from the application
        for (Role eachRole : rolesToRemove) {
            managedRole = eachRole;
            if (!em.contains(eachRole)) {
                managedRole = eachRole;
            }

            successOperation = true;
            if (managedApplication.containsRole(managedRole)) {
                successOperation =
                    application.removeRoleWithValidation(managedRole);
            }
            if (!successOperation) {
                throw new RuntimeException(
                    "Cannot remove role from application");
            }
            rolesRemoved.add(managedRole);
        }

        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("added", rolesAdded);
        answer.put("removed", rolesRemoved);

        return answer;
    }

    public Map<String, Object> updateRoles(Long applicationId,
        List<Long> toAdd, List<Long> toRemove) throws ImperiumException
    {
        Application managedApplication =
            this.findOneWithDetailById(applicationId);

        List<Role> rolesToAdd = new ArrayList<Role>();
        List<Role> rolesToRemove = new ArrayList<Role>();

        Role eachRole;
        for (Long idToAdd : toAdd) {
            eachRole = em.find(Role.class, idToAdd);
            rolesToAdd.add(eachRole);
        }
        for (Long idToRemove : toRemove) {
            eachRole = em.find(Role.class, idToRemove);
            rolesToRemove.add(eachRole);
        }

        return this.updateRoles(managedApplication, rolesToAdd, rolesToRemove);
    }
    
    private void removePermissionsFromRole(Application application) {
    	Set<Permission> permissions = application.getPermissions();
    	for (Permission permission : permissions) {
			permission.clearRoles();
		}
    	
    }
    
    public void delete(Application application) {
    	Application managedApplication = this.findOneWithDetailById(application.getId());
    	
    	/**
    	 * remove the permissions from the roles
    	 */
    	this.removePermissionsFromRole(managedApplication);
    	/**
    	 * Remove roles from subjects
    	 */
    	Set<Subject> subjects = managedApplication.getSubjects();
    	for(Subject eachSubject:subjects) {
    		eachSubject.clearRoles();
    	}
    	/**
    	 * Remove subjects,roles and permissions from the application
    	 */
    	for(Subject eachSubject:subjects) {
    		eachSubject.removeApplication();
    		em.remove(eachSubject);
    	}
    	Set<Role> roles = managedApplication.getRoles();
    	Set<Permission> permissions = managedApplication.getPermissions();
    	for(Role role:roles) {
    		role.removeApplication();
    		em.remove(role);
    	}
    	for(Permission permission:permissions) {
    		permission.removeApplication();
    		em.remove(permission);
    	}
    	/**
    	 * Remove entities
    	 */
    	em.remove(managedApplication);
    	
    }
    
}
