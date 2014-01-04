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
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.IRoleRepository;

@Service("roleRepository")
@Repository
public class RoleRepository extends AbstractJpaRepository<Role, Long> implements
    IRoleRepository
{

    private static final Logger logger = LoggerFactory
        .getLogger(RoleRepository.class);

    @Override
    public Role findOneWithDetailById(Long id)
    {
        String jpql = "SELECT role FROM Role role";
        jpql = jpql + " JOIN role.application application ";
        jpql = jpql + " LEFT JOIN role.permissions permission ";
        jpql = jpql + " LEFT JOIN role.subjects subjects ";
        jpql = jpql + " WHERE role.id = :id ";
        TypedQuery<Role> query = em.createQuery(jpql, Role.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public String getEntityNameForQueries()
    {
        // TODO Auto-generated method stub
        return "Role";
    }

    public List<Role> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria)
    {
        Map<String, String> orderMap = this.getOrderMap();
        String order = orderMap.get(sortCriteria);
        Long applicationId = application.getId();

        String sql =
            "SELECT role FROM Role role "
                + " JOIN role.application application  "
                + " WHERE application.id = :applicationId  ORDER BY " + order;
        TypedQuery<Role> query = em.createQuery(sql, Role.class);

        query.setParameter("applicationId", applicationId);
        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    @Override
    public Long findQtyForApplication(Application application)
        throws ImperiumException
    {
        String sql =
            "SELECT COUNT(role.id) FROM Role role "
                + " JOIN role.application application  "
                + " WHERE application.id = :applicationId ";
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("applicationId", application.getId());
        return executeCountQuery(query);
    }

    public void delete(Role role)
    {
        boolean withEm = em.contains(role);
        Role toRemove = role;
        if (!withEm) {
            toRemove = em.merge(role);
        }
        Application application = toRemove.getApplication();
        application.removeRole(toRemove);
    }

    @Override
    public Role update(Long id, Map<String, Object> newValues)
    {
        Role role = this.findOneById(id);

        String newName = (String) newValues.get("name");
        String description = (String) newValues.get("description");

        Application application = null;
        if (newValues.containsKey("applicationId")) {
            Long applicationId = (Long) newValues.get("applicationId");
            application = em.find(Application.class, applicationId);
        }

        role.setName(newName);
        role.setDescription(description);

        if ((application != null)) {
            role.setApplication(application);
        } else {
            role.removeApplication();
        }

        return role;
    }

    public void assignPermissions(Role role, List<Permission> permissionList)
        throws ImperiumException
    {
        Role managedRole = null;
        if (em.contains(role)) {
            managedRole = role;
        } else {
            managedRole = em.merge(role);
        }

        boolean successOperation;
        Permission managedPermission = null;
        for (Permission eachPermission : permissionList) {
            managedPermission = eachPermission;
            if (!em.contains(eachPermission)) {
                managedPermission = em.merge(eachPermission);
            }

            successOperation = true;
            if (!managedRole.hasPermission(managedPermission)) {
                successOperation = managedRole.addPermission(managedPermission);
            } else {
            }

            if (!successOperation) {
                throw new RuntimeException(
                    "Cannot add the permission to the role:"
                        + managedPermission + " to the role:" + managedRole);
            }
        }
    }

    @Override
    public List<Subject> addToRole(Role role, List<Subject> subjectList)
        throws ImperiumException
    {
        Role managedRole = null;
        if (!em.contains(role)) {
            managedRole = em.merge(role);
        } else {
            managedRole = role;
        }

        List<Subject> managedSubjectList = new ArrayList<Subject>();
        Subject managedSubject = null;
        for (Subject eachSubject : subjectList) {
            if (!em.contains(eachSubject)) {
                managedSubject = em.merge(eachSubject);
            } else {
                managedSubject = eachSubject;
            }
            managedSubjectList.add(managedSubject);
        }

        for (Subject eachManaged : managedSubjectList) {
            eachManaged.addRole(managedRole);
        }

        return managedSubjectList;
    }

    private Map<String, String> getOrderMap()
    {
        String entityName = "role";
        Map<String, String> answer = new HashMap<String, String>();
        answer.put("id", entityName + ".id");
        answer.put("name", entityName + ".name");
        answer.put("description", entityName + ".description");
        answer.put("application", "application.name");

        return answer;
    }

    @Override
    public List<Role> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria, String direction)
    {
        Map<String, String> map = this.getOrderMap();

        Long applicationId = application.getId();
        String sort = map.get(sortCriteria);

        String sql =
            "SELECT role FROM Role role "
                + " JOIN role.application application  "
                + " WHERE application.id = :applicationId " + " ORDER BY "
                + sort + " " + direction;

        TypedQuery<Role> query = em.createQuery(sql, Role.class);

        query.setParameter("applicationId", applicationId);
        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    @Override
    public List<Role> findAllForApplicationWhere(Application application,
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

        // dql
        String dql =
            "SELECT role FROM Role role "
                + " JOIN role.application application "
                + " WHERE application.id = :applicationId "
                + " AND ( (role.name like :name) OR "
                + "      role.description like :description ) " + " ORDER BY "
                + sortDql + " " + direction;

        TypedQuery<Role> query = em.createQuery(dql, Role.class);

        String tmpString = "%" + queryParam + "%";
        query.setParameter("applicationId", applicationId);
        query.setParameter("name", tmpString);
        query.setParameter("description", tmpString);

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

        // dql
        String dql =
            "SELECT COUNT(role.id) FROM Role role "
                + " JOIN role.application  application "
                + " WHERE application.id = :applicationId "
                + " AND ( (role.name like :name) OR "
                + "      role.description like :description ) ";

        TypedQuery<Long> query = em.createQuery(dql, Long.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("name", "%" + queryParam + "%");
        query.setParameter("description", "%" + queryParam + "%");

        return executeCountQuery(query);
    }

    @Override
    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination)
    {
        Long id = role.getId();

        String dql =
            " SELECT permissions FROM Role role JOIN role.permissions permissions WHERE role.id = :roleId";

        TypedQuery<Permission> query = em.createQuery(dql, Permission.class);

        query.setParameter("roleId", id);

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");
        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    @Override
    public Long findPermissionQty(Role role)
    {
        Long id = role.getId();

        String dql =
            " SELECT count(permissions) FROM Role role JOIN role.permissions permissions WHERE role.id = :roleId";

        TypedQuery<Long> query = em.createQuery(dql, Long.class);

        query.setParameter("roleId", id);

        return executeCountQuery(query);
    }

    public void removePermissions(Role role, List<Permission> permissionList)
        throws ImperiumException
    {
        Role managedRole = null;
        if (em.contains(role)) {
            managedRole = role;
        } else {
            managedRole = em.merge(role);
        }

        boolean successOperation;
        Permission managedPermission = null;
        for (Permission eachPermission : permissionList) {
            managedPermission = eachPermission;
            if (!em.contains(eachPermission)) {
                managedPermission = em.merge(eachPermission);
            }
            successOperation = managedRole.removePermission(managedPermission);

            if (!successOperation) {
                throw new RuntimeException(
                    "Cannot remove the permission to the role:"
                        + managedPermission + " to the role:" + managedRole);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ar.com.imperium.repository.IRoleRepository#findPermissions(ar.com.imperium
     * .domain.Role, java.util.Map, java.util.Map)
     */
    @Override
    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination, Map<String, Object> order)
    {
        Map<String, String> map = this.getOrderMap();

        Long id = role.getId();

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");
        sort = map.get(sort);

        String dql =
            " SELECT permissions FROM Role role "
                + " JOIN role.permissions permissions "
                + " JOIN role.application application "
                + " WHERE role.id = :roleId order by " + sort + " " + direction;

        TypedQuery<Permission> query = em.createQuery(dql, Permission.class);

        query.setParameter("roleId", id);

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");
        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    @Override
    public Long findQtyForPermissionsForRole(Map<String, Object> queryParams)
    {
        Long roleId = (Long) queryParams.get("roleId");
        String queryString = (String) queryParams.get("query");

        String likePermissions =
            " ( permissions.resource LIKE :query OR permissions.action LIKE :query ) ";

        String dql =
            " SELECT COUNT(permissions) FROM Role role "
                + " JOIN role.permissions permissions "
                + " JOIN role.application application "
                + " WHERE role.id = :roleId ";
        dql = dql + " AND " + likePermissions;

        TypedQuery<Long> query = em.createQuery(dql, Long.class);

        query.setParameter("roleId", roleId);

        String queryLike = "%" + queryString + "%";
        query.setParameter("query", queryLike);

        return executeCountQuery(query);
    }

    public List<Permission> findPermissionForRole(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order)
    {
        Long roleId = (Long) queryParams.get("roleId");
        String queryString = (String) queryParams.get("query");

        Map<String, String> map = this.getOrderMap();

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");
        sort = map.get(sort);

        String likePermissions =
            " ( permissions.resource LIKE :query OR permissions.action LIKE :query ) ";
        String orderJpql = " order by " + sort + " " + direction;

        String dql =
            " SELECT DISTINCT permissions FROM Role role "
                + " JOIN role.permissions permissions "
                + " JOIN role.application application "
                + " WHERE role.id = :roleId ";
        dql = dql + " AND " + likePermissions;
        dql = dql + orderJpql;

        TypedQuery<Permission> query = em.createQuery(dql, Permission.class);

        query.setParameter("roleId", roleId);

        String queryLike = "%" + queryString + "%";
        query.setParameter("query", queryLike);

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");
        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    private Long _findAvailableQtyForSubject(Map<String, Object> queryParams)
        throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");

        String subJpqlQuery =
            " SELECT r.id FROM Role r " + " JOIN r.application a "
                + " JOIN r.subjects s  " + " WHERE a.id = :applicationId "
                + " AND s.id = :subjectId";

        String jpql =
            " SELECT COUNT(role.id) FROM Role role "
                + " JOIN role.application application "
                + " WHERE application.id = :applicationId "
                + " AND role.id NOT IN (" + subJpqlQuery + ")"
                + " GROUP BY application.id ";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("roleId", subjectId);

        return executeCountQuery(query);
    }

    private Long _findAvailableQtyForSubjectWhere(
        Map<String, Object> queryParams) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");
        String likeParameter = (String) queryParams.get("query");
        likeParameter = "%" + likeParameter + "%";

        String subJpqlQuery =
            " SELECT r.id FROM Role r " + " JOIN r.application a "
                + " JOIN r.subjects s  " + " WHERE a.id = :applicationId "
                + " AND s.id = :subjectId";

        String likeFragment = " (role.name LIKE :query OR  ";
        likeFragment += " role.description LIKE :query ) ";

        String jpql =
            " SELECT COUNT(role) FROM Role role "
                + " JOIN role.application application "
                + " WHERE application.id = :applicationId "
                + " AND role.id NOT IN (" + subJpqlQuery + ")" + " AND "
                + likeFragment + " GROUP BY application.id ";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("applicationId", applicationId);
        query.setParameter("subjectId", subjectId);
        query.setParameter("query", likeParameter);

        return executeCountQuery(query);
    }

    public Long findAvailableQtyForSubjectInApplication(
        Map<String, Object> queryParams) throws Exception
    {
        String queryString = (String) queryParams.get("query");

        Long answer = null;

        if (queryString != null) {
            answer = _findAvailableQtyForSubjectWhere(queryParams);
        } else {
            answer = _findAvailableQtyForSubject(queryParams);
        }

        return answer;
    }

    public List<Role> findForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");
        String queryString = (String) queryParams.get("query");

        Integer page = (Integer) pagination.get("pagination");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        List<Role> answer = null;

        // like query
        if (queryString != null) {
            answer =
                this.subFindForSubjectInApplicationLike(
                    queryParams,
                    pagination,
                    order);
            // list all
        } else {
            answer =
                this.subFindForSubjectInApplication(
                    queryParams,
                    pagination,
                    order);
        }

        return answer;
    }

    /**
     * @todo go on Here
     * @param queryParams
     * @param pagination
     * @param order
     * @return
     * @throws Exception
     */
    private List<Role> subFindForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        Map<String, String> orderMap = this.getOrderMap();
        String sortJpql = orderMap.get(sort);
        sortJpql = " ORDER BY " + sortJpql + " " + direction;

        String jpql =
            "SELECT role FROM Role role  "
                + " JOIN role.application application  "
                + " JOIN role.subjects subjects  "
                + " WHERE application.id = :applicationId "
                + " AND subjects.id = :subjectId " + sortJpql;

        TypedQuery<Role> query = em.createQuery(jpql, Role.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("subjectId", subjectId);

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    private List<Role> subFindForSubjectInApplicationLike(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");
        String queryString = (String) queryParams.get("query");

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        Map<String, String> orderMap = this.getOrderMap();
        String sortJpql = orderMap.get(sort);
        sortJpql = " ORDER BY " + sortJpql + " " + direction;

        String jpql =
            "SELECT role FROM Role role  "
                + " JOIN role.application application  "
                + " JOIN role.subjects subjects  "
                + " WHERE application.id = :applicationId "
                + " AND subjects.id = :subjectId "
                + " AND ( role.name like :query OR role.description like :query)"
                + sortJpql;

        TypedQuery<Role> query = em.createQuery(jpql, Role.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("subjectId", subjectId);

        String queryParameter = "%" + queryString + "%";
        query.setParameter("query", queryParameter);

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    public Long findQtyForSubjectInApplication(Map<String, Object> queryParams)
        throws Exception
    {
        String queryString = (String) queryParams.get("query");

        Long answer = null;
        /**
         * Find qty for subject in application where role like
         */
        if (queryString != null) {
            answer = this.subFindQtyForSubjectInApplicationWhere(queryParams);

            /**
             * Find how many subjects has the role
             */
        } else {
            answer = this.subFindQtyForSubjectInApplication(queryParams);
        }

        return answer;
    }

    @SuppressWarnings("finally")
    private Long subFindQtyForSubjectInApplication(
        Map<String, Object> queryParams) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");

        String jpql =
            "SELECT COUNT(role.id) FROM Role role  "
                + " JOIN role.application application  "
                + " JOIN role.subjects subjects  "
                + " WHERE application.id = :applicationId "
                + " AND subjects.id = :subjectId ";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("subjectId", subjectId);

        return executeCountQuery(query);
    }

    private Long subFindQtyForSubjectInApplicationWhere(
        Map<String, Object> queryParams) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");
        String queryString = (String) queryParams.get("query");

        String jpql =
            "SELECT COUNT(role.id) FROM Role role  "
                + " JOIN role.application application  "
                + " JOIN role.subjects subjects  "
                + " WHERE application.id = :applicationId "
                + " AND subjects.id = :subjectId "
                + " AND (role.name LIKE :query OR role.description LIKE :query)"
                + " GROUP BY application.id ";

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("subjectId", subjectId);

        queryString = "%" + queryString + "%";
        query.setParameter("query", queryString);

        return executeCountQuery(query);
    }

    private List<Role> subFindAvailableForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        Map<String, String> orderMap = this.getOrderMap();
        String sortJpql = orderMap.get(sort);
        sortJpql = " ORDER BY " + sortJpql + " " + direction;

        String subJpqlQuery =
            " SELECT r.id FROM Role r " + " JOIN r.application a "
                + " JOIN r.subjects s  " + " WHERE a.id = :applicationId "
                + " AND s.id = :subjectId";

        String jpql =
            " SELECT DISTINCT role FROM Role role "
                + " JOIN role.application application "
                + " WHERE application.id = :applicationId "
                + " AND role.id NOT IN (" + subJpqlQuery + ")" + " " + sortJpql;

        TypedQuery<Role> query = em.createQuery(jpql, Role.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("subjectId", subjectId);

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    private List<Role> subFindAvailableForSubjectInApplicationWhere(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        Long applicationId = (Long) queryParams.get("applicationId");
        Long subjectId = (Long) queryParams.get("subjectId");
        String likeParameter = (String) queryParams.get("query");
        likeParameter = "%" + likeParameter + "%";

        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        Map<String, String> orderMap = this.getOrderMap();
        String sortJpql = orderMap.get(sort);
        sortJpql = " ORDER BY " + sortJpql + " " + direction;
        /**
         * jpql query
         */
        String subJpqlQuery =
            " SELECT r.id FROM Role r " + " JOIN r.application a "
                + " JOIN r.subjects s  " + " WHERE a.id = :applicationId "
                + " AND s.id = :subjectId";

        String likeFragment = " (role.name LIKE :query OR  ";
        likeFragment += " role.description LIKE :query ) ";

        String jpql =
            " SELECT DISTINCT role FROM Role role "
                + " JOIN role.application application "
                + " WHERE application.id = :applicationId "
                + " AND role.id NOT IN (" + subJpqlQuery + ")" + " AND "
                + likeFragment + sortJpql;

        TypedQuery<Role> query = em.createQuery(jpql, Role.class);

        query.setParameter("applicationId", applicationId);
        query.setParameter("subjectId", subjectId);
        query.setParameter("query", likeParameter);

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    public List<Role> findAvailableForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        String queryString = (String) queryParams.get("query");

        List<Role> answer = null;
        // like query
        if (queryString != null) {
            answer =
                subFindAvailableForSubjectInApplicationWhere(
                    queryParams,
                    pagination,
                    order);

            // list all for
        } else {
            answer =
                subFindAvailableForSubjectInApplication(
                    queryParams,
                    pagination,
                    order);
        }

        return answer;
    }

    @Override
    public List<Role> findWithIds(List<Long> idList)
    {
        return super.findWithIds(idList);
    }

    public List<Application> findAll(Map<String, Object> pagination,
        Map<String, Object> order) throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");
    }

    private List<Subject> subFindSubjectsWhereRole(Role role) throws Exception
    {
        String jpql =
            "SELECT subject FROM Subject subject JOIN subject.roles roles WHERE roles.id = :roleId";

        TypedQuery<Subject> query = em.createQuery(jpql, Subject.class);

        query.setParameter("roleId", role.getId());

        return query.getResultList();
    }

    public List<Subject> removeRoleFromSubjects(Role role) throws Exception
    {
        List<Subject> roleList = this.subFindSubjectsWhereRole(role);

        boolean success = false;
        for (Subject eachSubject : roleList) {
            success = true;

        }

        throw new RuntimeException("Method not implemented");
    }

}
