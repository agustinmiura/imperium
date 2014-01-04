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

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.ISubjectRepository;

@Service("subjectRepository")
@Repository
public class SubjectRepositoryImpl extends AbstractJpaRepository<Subject, Long>
    implements ISubjectRepository
{
    private static final Logger logger = LoggerFactory
        .getLogger(SubjectRepositoryImpl.class);

    @Override
    public Subject update(Long id, Map<String, Object> newValues)
    {
        Subject subject = this.findOneWithDetailById(id);

        if (subject == null) {
            throw new RuntimeException("Subject with id :" + id
                + " cannot be found");
        } else {
            String name = (String) newValues.get("name");
            subject.setName(name);
            return subject;
        }
    }

    @Override
    public Long findQtyForApplication(Application application)
        throws ImperiumException
    {
        String dql =
            "SELECT COUNT(subject.id) FROM Subject subject JOIN subject.application application WHERE application.id = :applicationId";
        TypedQuery<Long> query = em.createQuery(dql, Long.class);
        query.setParameter("applicationId", application.getId());
        return executeCountQuery(query);
    }

    @Override
    public List<Subject> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria)
        throws ImperiumException
    {
        Long applicationId = application.getId();

        String dql =
            "SELECT subject FROM Subject subject JOIN subject.application application WHERE application.id = :applicationId";

        String sort = null;
        if ((sortCriteria.compareTo("name") == 0)) {
            sort = " ORDER BY subject.name ASC ";
        } else {
            sort = " ORDER BY application.name ASC ";
        }
        dql += sort;

        TypedQuery<Subject> query = em.createQuery(dql, Subject.class);

        query.setParameter("applicationId", applicationId);
        query.setMaxResults(maxSize);
        query.setFirstResult(page);

        return query.getResultList();
    }

    @Override
    public Subject findOneWithDetailById(Long id)
    {
        String jpql =
            " SELECT DISTINCT subject FROM Subject subject "
                + " LEFT JOIN FETCH subject.application application "
                + " LEFT JOIN FETCH subject.roles roles "
                + " WHERE subject.id = :subjectId ";

        TypedQuery<Subject> query = em.createQuery(jpql, Subject.class);
        query.setParameter("subjectId", id);

        return query.getSingleResult();
    }

    @Override
    public String getEntityNameForQueries()
    {
        // TODO Auto-generated method stub
        return "Subject";
    }

    @Override
    public void addToApplication(Application application,
        List<Subject> subjectList) throws Exception
    {
        Application managedApplication = null;
        if (!em.contains(application)) {
            managedApplication = em.merge(application);
        }

        List<Subject> managedSubjectList = new ArrayList<Subject>();
        Subject managedSubject;
        for (Subject eachSubject : subjectList) {
            if (!em.contains(eachSubject)) {
                managedSubject = em.merge(eachSubject);
            } else {
                managedSubject = eachSubject;
            }
            managedSubjectList.add(managedSubject);
        }

        Boolean added = false;
        for (Subject eachSubject : managedSubjectList) {
            added = application.addSubject(eachSubject);
        }
    }

    private Map<String, String> getOrderMap()
    {
        Map<String, String> orderMap = new HashMap<String, String>();
        String entityName = "subject";
        orderMap.put("id", entityName + ".id");
        orderMap.put("name", entityName + ".name");
        orderMap.put("application", "application.name");

        return orderMap;
    }

    @Override
    public List<Subject> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria, String direction)
    {
        Map<String, String> orderMap = getOrderMap();

        Long applicationId = application.getId();
        String order = orderMap.get(sortCriteria);

        String dql =
            "SELECT subject FROM Subject subject "
                + " JOIN subject.application application  "
                + " WHERE application.id = :applicationId  " + " ORDER BY "
                + order + " " + direction;

        String sort = null;
        if ((sortCriteria.compareTo("name") == 0)) {
            sort = " ORDER BY subject.name ASC ";
        } else {
            sort = " ORDER BY application.name ASC ";
        }
        dql += sort;

        TypedQuery<Subject> query = em.createQuery(dql, Subject.class);

        query.setParameter("applicationId", applicationId);
        query.setMaxResults(maxSize);
        query.setFirstResult(page);

        return query.getResultList();
    }

    @Override
    public List<Subject> findAllForApplicationWhere(Application application,
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order)
    {
        // fetch params
        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        String queryParam = (String) queryParams.get("query");
        Long applicationId = application.getId();

        // sort param
        Map<String, String> sortMap = this.getOrderMap();
        String sortDql = sortMap.get(sort);

        String dql =
            "SELECT subject FROM Subject subject "
                + " JOIN subject.application application  "
                + " WHERE application.id = :applicationId  "
                + " AND ( (subject.name like :name) ) " + " ORDER BY "
                + sortDql + " " + direction;
        ;

        TypedQuery<Subject> query = em.createQuery(dql, Subject.class);
        String tmpString = "%" + queryParam + "%";
        query.setParameter("applicationId", applicationId);
        query.setParameter("name", tmpString);

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    @Override
    public Long findQtyForApplicationWhere(Application application,
        Map<String, Object> queryParams)
    {
        String queryParam = (String) queryParams.get("query");
        Long applicationId = application.getId();

        String dql =
            "SELECT COUNT(subject.id) FROM Subject subject "
                + " JOIN subject.application application  "
                + " WHERE application.id = :applicationId  "
                + " AND ( (subject.name like :name) ) ";

        TypedQuery<Long> query = em.createQuery(dql, Long.class);
        String tmpString = "%" + queryParam + "%";
        query.setParameter("applicationId", applicationId);
        query.setParameter("name", tmpString);

        return executeCountQuery(query);
    }

    private TypedQuery<Subject> subFindSubjectsForRole(Long roleId,
        Map<String, Object> pagination, Map<String, Object> order)
    {
        Map<String, String> orderMap = getOrderMap();

        String jpql = "SELECT subject FROM Subject subject ";
        jpql = jpql + " JOIN subject.roles roles  ";
        jpql = jpql + " WHERE roles.id = :roleId ";

        String jpqlOrder = null;
        String orderCriteria = orderMap.get(order.get("sort"));
        String direction = (String) order.get("direction");
        jpqlOrder = " ORDER BY " + orderCriteria + " " + direction;

        jpql = jpql + jpqlOrder;

        TypedQuery<Subject> query = em.createQuery(jpql, Subject.class);
        query.setParameter("roleId", roleId);

        Integer maxSize = (Integer) pagination.get("maxSize");
        Integer page = (Integer) pagination.get("page");
        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query;
    }

    private TypedQuery<Subject> subFindSubjectsForRoleWhere(Long roleId,
        String queryParam, Map<String, Object> pagination,
        Map<String, Object> order)
    {
        Map<String, String> orderMap = getOrderMap();

        String jpql = "SELECT subject FROM Subject subject ";
        jpql = jpql + " JOIN subject.roles roles  ";
        jpql = jpql + " WHERE roles.id = :roleId ";
        jpql =
            jpql + " AND (subject.name LIKE :query OR subject.id LIKE :query) ";

        String jpqlOrder = null;
        String orderCriteria = orderMap.get(order.get("sort"));
        String direction = (String) order.get("direction");
        jpqlOrder = " ORDER BY " + orderCriteria + " " + direction;

        jpql = jpql + jpqlOrder;

        TypedQuery<Subject> query = em.createQuery(jpql, Subject.class);

        query.setParameter("roleId", roleId);
        String queryToSet = "%" + queryParam + "%";
        query.setParameter("query", queryToSet);

        Integer maxSize = (Integer) pagination.get("maxSize");
        Integer page = (Integer) pagination.get("page");
        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query;
    }

    private TypedQuery<Long> subFindQtySubjectsForRole(Long roleId)
        throws Exception
    {
        String jpql = "SELECT COUNT(subject.id) FROM Subject subject ";
        jpql = jpql + " JOIN subject.roles roles  ";
        jpql = jpql + " WHERE roles.id = :roleId ";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("roleId", roleId);
        return query;
    }

    private TypedQuery<Long> subFindQtyForSubjectsWhere(Long roleId,
        String queryParam) throws Exception
    {
        String jpql = "SELECT COUNT(subject.id) FROM Subject subject ";
        jpql = jpql + " JOIN subject.roles roles  ";
        jpql = jpql + " WHERE roles.id = :roleId ";
        jpql =
            jpql + " AND (subject.name LIKE :query OR subject.id LIKE :query) ";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        query.setParameter("roleId", roleId);
        String queryToSet = "%" + queryParam + "%";
        query.setParameter("query", queryToSet);

        return query;
    }

    @Override
    public List<Subject> findSubjectsForRole(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws Exception
    {
        Long roleId = (Long) queryParams.get("roleId");
        TypedQuery<Subject> query = null;
        if (queryParams.containsKey("query")) {
            String queryParam = (String) queryParams.get("query");
            query =
                this.subFindSubjectsForRoleWhere(
                    roleId,
                    queryParam,
                    pagination,
                    order);
        } else {

            query = this.subFindSubjectsForRole(roleId, pagination, order);
        }

        return query.getResultList();
    }

    @Override
    public Long findQtyForSubjectsForRole(Map<String, Object> queryParams)
        throws Exception
    {
        Long roleId = (Long) queryParams.get("roleId");
        TypedQuery<Long> query = null;
        if (queryParams.containsKey("query")) {
            String queryParam = (String) queryParams.get("query");
            query = this.subFindQtyForSubjectsWhere(roleId, queryParam);
        } else {

            query = this.subFindQtySubjectsForRole(roleId);
        }

        return executeCountQuery(query);
    }

    private TypedQuery<Subject> subFindAvailableForRoleInApplication(
        Long applicationId, Long roleId, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {

        throw new RuntimeException("Method not implemented");
    }

    private TypedQuery<Subject> subFindAvailableForRoleInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        throw new RuntimeException("Method not implemented");
    }

    public List<Subject> findAvailableForRoleInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long roleId = (Long) queryParams.get("roleId");
        TypedQuery<Subject> query = null;
        if (queryParams.containsKey("query")) {
            query =
                this.subFindAvailableForRoleInApplication(
                    queryParams,
                    pagination,
                    order);
        } else {
            query =
                this.subFindAvailableForRoleInApplication(
                    applicationId,
                    roleId,
                    pagination,
                    order);
        }

        return query.getResultList();
    }

    @Override
    public Long findQtyAvailableForRoleInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Map<String, Object> updateRoles(Long subjectId,
        List<Role> rolesToAdd, List<Role> rolesToRemove) throws Exception
    {
        Subject subject = findOneWithDetailById(subjectId);

        //
        List<Role> rolesAdded = new ArrayList<Role>();
        List<Role> rolesRemoved = new ArrayList<Role>();

        boolean successOperation;
        Role managedRole = null;
        // add roles to the subject
        for (Role eachRole : rolesToAdd) {
            managedRole = eachRole;
            if (!em.contains(eachRole)) {
                managedRole = em.merge(eachRole);
            }
            successOperation = true;
            if (!subject.hasRole(managedRole)) {
                successOperation = subject.addRole(managedRole);
            }
            if (!successOperation) {
                throw new RuntimeException("Cannot add role to subject");
            }

            rolesAdded.add(managedRole);
        }
        // remove roles from the subject
        for (Role eachToRemove : rolesToRemove) {
            managedRole = eachToRemove;
            if (!em.contains(eachToRemove)) {
                managedRole = em.merge(eachToRemove);
            }
            successOperation = subject.removeRoleWithResult(managedRole);

            if (!successOperation) {
                throw new RuntimeException("Cannot remove role from subject");
            }

            rolesRemoved.add(managedRole);
        }

        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("add", rolesAdded);
        answer.put("removed", rolesRemoved);

        return answer;
    }

    @Override
    public List<Subject> findWithIds(List<Long> idList)
    {
        return super.findWithIds(idList);
    }

    @Override
    public Map<String, Object> updateRoles(Subject subject, List<Long> toAdd,
        List<Long> toRemove) throws Exception
    {
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
        return updateRoles(subject.getId(), rolesToAdd, rolesToRemove);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ar.com.imperium.repository.IBaseRepository#findAll(java.util.Map,
     * java.util.Map)
     */
    @Override
    public List<Application> findAll(Map<String, Object> pagination,
        Map<String, Object> order) throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ar.com.imperium.repository.ISubjectRepository#removeSubjectFromRoles(
     * ar.com.imperium.domain.Subject, java.util.List)
     */
    @Override
    public void removeSubjectFromRoles(Subject subject, List<Role> roleList)
        throws Exception
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Map<Long, Long> findSubjectQtyForRole(List<Role> roleList)
        throws Exception
    {

        Map<Long, Long> answer = new HashMap<Long, Long>();

        Map<String, Object> queryParams = new HashMap<String, Object>();
        Long roleId;
        Long subjectQty;
        for (Role role : roleList) {
            roleId = role.getId();

            // query map
            queryParams.clear();
            queryParams.put("roleId", roleId);
            // find subject qty
            subjectQty = this.findQtyForSubjectsForRole(queryParams);
            // add to answer
            answer.put(roleId, subjectQty);
        }

        return answer;
    }
}
