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

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import ar.com.imperium.common.string.StringJoiner;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.IPermissionRepository;

@Service("permissionRepository")
@Repository
public class PermissionRepository extends
		AbstractJpaRepository<Permission, Long> implements
		IPermissionRepository {
	private static final Logger logger = LoggerFactory
			.getLogger(PermissionRepository.class);

	@Autowired
	@Qualifier("stringJoiner")
	private StringJoiner stringJoiner;

	public List<Permission> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria) {
		Long applicationId = application.getId();

		String sql = "SELECT DISTINCT permission FROM Permission permission "
				+ " JOIN permission.application application  "
				+ " WHERE application.id = :applicationId ";

		TypedQuery<Permission> query = em.createQuery(sql, Permission.class);

		query.setParameter("applicationId", applicationId);
		query.setMaxResults(maxSize);
		query.setFirstResult(page * maxSize);

		return query.getResultList();
	}

	public Long getQtyForApplication(Application application) {
		String sql = "SELECT COUNT(permission.id) FROM Permission permission "
				+ " JOIN permission.application application  "
				+ " WHERE application.id = :applicationId ";
		TypedQuery<Long> query = em.createQuery(sql, Long.class);
		query.setParameter("applicationId", application.getId());
		return executeCountQuery(query);
	}

	@Override
	public Permission findOneWithDetailById(Long id) {
		String dql = "SELECT permission FROM Permission permission";
		dql = dql + " JOIN permission.roles roles ";
		dql = dql + " JOIN permission.application application ";
		dql = dql + " WHERE permission.id = :id ";
		TypedQuery<Permission> query = em.createQuery(dql, Permission.class);
		query.setParameter("id", id);
		try {
			return query.getSingleResult();
		} catch (NoResultException exception) {
			return null;
		}
	}

	@Override
	public String getEntityNameForQueries() {
		return "Permission";
	}

	@Override
	public Permission update(Long id, Map<String, Object> newValues) {
		throw new RuntimeException("Method not implemented");
	}

	private Map<String, String> getOrderMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "permission.id");
		map.put("resource", "permission.resource");
		map.put("action", "permission.action");
		map.put("application", "application.name");

		return map;
	}

	@Override
	public List<Permission> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria, String direction) {
		Map<String, String> orderMap = this.getOrderMap();

		Long applicationId = application.getId();
		String sort = orderMap.get(sortCriteria);

		String sql = "SELECT permission FROM Permission permission "
				+ " JOIN permission.application application  "
				+ " WHERE application.id = :applicationId " + " ORDER BY "
				+ sort + " " + direction + " ";

		TypedQuery<Permission> query = em.createQuery(sql, Permission.class);

		query.setParameter("applicationId", applicationId);
		query.setMaxResults(maxSize);
		query.setFirstResult(page * maxSize);

		return query.getResultList();
	}

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
			Map<String, Object> order) throws Exception {
		// fetch params
		Integer page = (Integer) pagination.get("page");
		Integer maxSize = (Integer) pagination.get("maxSize");

		String sort = (String) order.get("sort");
		String direction = (String) order.get("direction");

		/**
		 * Get permissions where permission.resource starts with
		 * {resource.prefix}
		 */
		String prefix = (String) queryParams.get("resource.prefix");
		Long applicationId = (Long) queryParams.get("applicationId");

		// sort param
		Map<String, String> sortMap = this.getOrderMap();
		String sortDql = sortMap.get(sort);

		String jpql = " SELECT permission FROM Permission permission";
		jpql = jpql + " JOIN permission.application application ";
		jpql = jpql + " WHERE application.id = :applicationId ";
		jpql = jpql + " AND permission.resource LIKE :resource ";
		jpql = jpql + " ORDER BY " + sortDql + " " + direction;

		TypedQuery<Permission> query = em.createQuery(jpql, Permission.class);

		query.setParameter("applicationId", applicationId);
		String prefixToSet = prefix + "%";
		query.setParameter("resource", prefixToSet);

		query.setMaxResults(maxSize);
		query.setFirstResult(page * maxSize);

		return query.getResultList();
	}

	public Permission findAllWhereNameResourceApplication(
			Map<String, Object> queryParams) throws Exception {
		Long applicationId = (Long) queryParams.get("applicationId");
		String resource = (String) queryParams.get("resource");
		String action = (String) queryParams.get("action");

		String jqpl = " SELECT permission FROM Permission permission  ";
		jqpl += " JOIN permission.application application ";
		jqpl += " WHERE permission.resource = :resource  ";
		jqpl += " AND permission.action = :action ";
		jqpl += " AND application.id = :applicationId  ";

		TypedQuery<Permission> query = em.createQuery(jqpl, Permission.class);
		query.setParameter("applicationId", applicationId);
		query.setParameter("resource", resource);
		query.setParameter("action", action);

		query.setMaxResults(1);

		Permission answer = null;
		try {
			answer = query.getSingleResult();
		} catch (NoResultException noResultException) {
			return answer;
		}
		return answer;
	}

	public List<Permission> findAllWhereAction(Map<String, Object> queryParams,
			Map<String, Object> pagination, Map<String, Object> order)
			throws Exception {
		// fetch params
		Integer page = (Integer) pagination.get("page");
		Integer maxSize = (Integer) pagination.get("maxSize");

		String sort = (String) order.get("sort");
		String direction = (String) order.get("direction");

		/**
		 * Get permissions where permission.resource starts with
		 * {resource.prefix}
		 */
		String prefix = (String) queryParams.get("action.prefix");
		Long applicationId = (Long) queryParams.get("applicationId");

		// sort param
		Map<String, String> sortMap = this.getOrderMap();
		String sortDql = sortMap.get(sort);

		String jpql = " SELECT permission FROM Permission permission";
		jpql = jpql + " JOIN permission.application application ";
		jpql = jpql + " WHERE application.id = :applicationId ";
		jpql = jpql + " AND permission.action LIKE :action ";
		jpql = jpql + " ORDER BY " + sortDql + " " + direction;

		TypedQuery<Permission> query = em.createQuery(jpql, Permission.class);

		query.setParameter("applicationId", applicationId);
		String prefixToSet = prefix + "%";
		query.setParameter("action", prefixToSet);

		query.setMaxResults(maxSize);
		query.setFirstResult(page * maxSize);

		return query.getResultList();
	}

	@Override
	public List<Permission> findAllForApplicationWhere(Application application,
			Map<String, Object> queryParams, Map<String, Object> pagination,
			Map<String, Object> order) {
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

		String sql = "SELECT DISTINCT permission "
				+ "FROM Permission permission "
				+ "JOIN permission.application application "
				+ "WHERE application.id = :applicationId "
				+ "AND ( (permission.resource LIKE :resource) "
				+ "OR (permission.action LIKE :action) ) " + "ORDER BY "
				+ sortDql + " " + direction;

		TypedQuery<Permission> query = em.createQuery(sql, Permission.class);

		String tmpString = "%" + queryParam + "%";

		query.setParameter("applicationId", applicationId);
		query.setParameter("resource", tmpString);
		query.setParameter("action", tmpString);

		query.setMaxResults(maxSize);
		query.setFirstResult(page * maxSize);

		return query.getResultList();
	}

	@Override
	public Long findQtyForApplicationWhere(Application application,
			Map<String, Object> queryParams) {
		String queryParam = (String) queryParams.get("query");
		Long applicationId = application.getId();

		String sql = "SELECT COUNT(permission.id) FROM Permission permission "
				+ "JOIN permission.application application "
				+ "WHERE application.id = :applicationId "
				+ "AND ( (permission.resource LIKE :resource) "
				+ "OR (permission.action LIKE :action) ) ";

		TypedQuery<Long> query = em.createQuery(sql, Long.class);

		String tmpString = "%" + queryParam + "%";

		query.setParameter("applicationId", applicationId);
		query.setParameter("resource", tmpString);
		query.setParameter("action", tmpString);

		return executeCountQuery(query);
	}

	@Override
	public List<Permission> findAvailableForRoleInApplication(
			Map<String, Object> queryParams, Map<String, Object> pagination,
			Map<String, Object> order) throws Exception {
		Long applicationIdParameter = (Long) queryParams.get("applicationId");
		Long roleIdParameter = (Long) queryParams.get("roleId");
		String queryParameter = (String) queryParams.get("query");

		String queryAsString = (queryParameter != null) ? (queryParameter)
				: " NULL QUERY";

		Integer page = (Integer) pagination.get("page");
		Integer maxSize = (Integer) pagination.get("maxSize");

		String sort = (String) order.get("sort");
		String direction = (String) order.get("direction");
		/**
		 * This case covers the permissions assigned to another roles and not
		 * the current one
		 */
		String sql = " SELECT DISTINCT permission FROM Permission permission JOIN permission.application application JOIN permission.roles roles WHERE (application.id = :applicationId ) AND ( roles.id != :roleId ) ";
		/**
		 * This one covers the permissions that belong to an application but
		 * they have not been assigned to any role
		 */
		sql = " SELECT DISTINCT permission FROM Permission permission JOIN permission.application application WHERE (application.id = :applicationId ) AND permission.id NOT IN (SELECT p.id FROM permission p JOIN p.roles r JOIN p.application a  WHERE a.id = :applicationId AND r.id = :roleId)";

		if (queryParameter != null) {
			sql = sql
					+ " AND ( (permission.resource LIKE :resource ) OR ( permission.action LIKE :action ) ) ";
		}
		sql = sql + " ORDER BY permission." + sort + " " + direction;

		TypedQuery<Permission> query = em.createQuery(sql, Permission.class);

		query.setParameter("applicationId", applicationIdParameter);
		query.setParameter("roleId", roleIdParameter);

		query.setMaxResults(maxSize);
		query.setFirstResult(page * maxSize);

		if (queryParameter != null) {
			String toSet = "%" + queryParameter + "%";
			query.setParameter("resource", toSet);
			query.setParameter("action", toSet);
		}
		return query.getResultList();
	}

	@Override
	public Long findQtyForAvailableForRoleInApplication(
			Map<String, Object> queryParams) throws Exception {
		Long applicationIdParameter = (Long) queryParams.get("applicationId");
		Long roleIdParameter = (Long) queryParams.get("roleId");
		String queryParameter = (String) queryParams.get("query");

		String sql = " SELECT COUNT(permission.id) FROM Permission permission JOIN permission.application application WHERE (application.id = :applicationId ) AND permission.id NOT IN (SELECT p.id FROM permission p JOIN p.roles r JOIN p.application a  WHERE a.id = :applicationId AND r.id = :roleId)";

		if (queryParameter != null) {
			sql = sql + " AND ( (permission.resource LIKE :resource )  "
					+ " OR  ( permission.action LIKE :action ) ) ";
		}

		sql = sql + " GROUP BY application.id";

		TypedQuery<Long> query = em.createQuery(sql, Long.class);

		query.setParameter("applicationId", applicationIdParameter);
		query.setParameter("roleId", roleIdParameter);

		if (queryParameter != null) {
			String toSet = "%" + queryParameter + "%";
			query.setParameter("resource", toSet);
			query.setParameter("action", toSet);
		}

		return executeCountQuery(query);
	}

	@Override
	public List<Permission> findWithIds(List<Long> idList) {
		return super.findWithIds(idList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ar.com.imperium.repository.IBaseRepository#findAll(java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public List<Application> findAll(Map<String, Object> pagination,
			Map<String, Object> order) throws ImperiumException {
		throw new RuntimeException("Method not implemented");
	}

	public List<Permission> findAllWithResourceAction(Long applicationId,
			String resourceName, List<String> actionList) throws Exception {
		List<Permission> permissionList = new ArrayList<Permission>();

		String jpql = " SELECT permission FROM Permission permission JOIN permission.application application ";
		jpql += " WHERE application.id = :applicationId ";
		jpql += " AND permission.resource = :resource ";
		jpql += " AND permission.action IN (";

		/**
		 * Prepare a map with :action0, :action1 and add to the jpql the
		 * parameters
		 */
		Map<String, String> paramMap = new HashMap<String, String>();
		Integer index = 0;
		String paramName;
		for (String actionName : actionList) {
			paramName = "action" + index.toString();
			paramMap.put(paramName, actionName);

			jpql += ":" + paramName + ",";

			index++;
		}
		Integer size = jpql.length();
		jpql = jpql.substring(0, size - 1);

		jpql += ")";

		TypedQuery<Permission> query = em.createQuery(jpql, Permission.class);

		query.setParameter("applicationId", applicationId);
		query.setParameter("resource", resourceName);
		// Add the action names
		Set<String> keySet = paramMap.keySet();
		for (String key : keySet) {
			query.setParameter(key, paramMap.get(key));
		}

		return query.getResultList();
	}
	
	public void delete(Permission permission) 
	{
		String jpql = "DELETE FROM Permission permission ";
		jpql += " WHERE permission.id = :id ";
		Query query = em.createQuery(jpql);
		query.setParameter("id", permission.getId());
		Integer rows = query.executeUpdate();
	}

}
