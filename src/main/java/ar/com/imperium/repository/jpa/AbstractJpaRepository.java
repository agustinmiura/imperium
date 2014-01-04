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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ar.com.imperium.domain.Application;
import ar.com.imperium.repository.IBaseRepository;

import com.google.common.base.Joiner;

@Repository
public abstract class AbstractJpaRepository<T, ID extends Serializable>
		implements IBaseRepository<T, ID> {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractJpaRepository.class);

	@PersistenceContext
	protected EntityManager em;

	protected Class<T> tClass;

	public void setClass(final Class<T> tClass) {
		this.tClass = tClass;
	}

	public T findOneById(ID id) {
		T t = em.find(tClass, id);
		return t;
	}

	public String getBaseDqlForFindAllApplication() {
		String answer = null;

		String className = getEntityNameForQueries();
		answer = "SELECT entity FROM "
				+ className
				+ " entity JOIN entity.application application WHERE application.id = :applicationId";

		return answer;
	}

	public abstract List<T> findAllForApplication(Application application,
			Integer page, Integer maxSize, String sortCriteria, String direction);

	public Long getQtyForApplication(Application application) {
		String entityName = getEntityNameForQueries();
		String dql = "SELECT COUNT(entity.id) FROM "
				+ entityName
				+ " entity JOIN entity.application application WHERE application.id = :applicationId";

		TypedQuery<Long> query = em.createQuery(dql, Long.class);
		query.setParameter("applicationId", application.getId());
		return executeCountQuery(query);
	}

	public abstract T findOneWithDetailById(ID id);

	public abstract String getEntityNameForQueries();

	public T create(T t) {
		if (!em.contains(t)) {
			em.persist(t);
		}
		return t;
	}

	public T update(T t) {
		boolean contains = em.contains(t);
		T mergedEntity = t;
		if (!contains) {
			mergedEntity = em.merge(t);
		}
		return mergedEntity;
	}

	public void delete(T t) {
		T merged = em.merge(t);
		em.remove(merged);
	}

	public List<T> findAll(Integer page, Integer pageSize, String sortCriteria) {
		String sql = "from " + tClass.getName() + " ";
		if (sortCriteria != null) {
			sql = sql + " order by " + sortCriteria;
		}
		TypedQuery<T> query = em.createQuery(sql, tClass);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public List<T> findAll(Integer page, Integer pageSize, String sortCriteria,
			String direction) {
		String sql = "from " + tClass.getName() + " ";
		if (sortCriteria != null) {
			sql = sql + " order by " + sortCriteria + " " + direction;
		}
		TypedQuery<T> query = em.createQuery(sql, tClass);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public Long getEntityQty() {
		String entityName = this.getEntityNameForQueries();
		String dql = "SELECT COUNT(x.id) FROM " + entityName + " x ";
		TypedQuery<Long> query = em.createQuery(dql, Long.class);
		return executeCountQuery(query);
	}

	public List<T> create(List<T> entityList) {
		List<T> answer = new ArrayList<T>();
		T managedEntity = null;
		boolean isManaged;

		for (T eachEntity : entityList) {
			isManaged = (em.contains(eachEntity));
			if (!isManaged) {
				em.persist(eachEntity);
				managedEntity = eachEntity;
			} else {
				managedEntity = eachEntity;
			}
			answer.add(managedEntity);
		}
		return answer;
	}

	public void remove(List<T> entityList) {
		T managedEntity = null;
		boolean isManaged;

		for (T eachEntity : entityList) {
			isManaged = (em.contains(eachEntity));
			if (isManaged) {
				em.remove(eachEntity);
			}
		}

	}

	public List<T> findWithIds(List<ID> idList) {
		// create the X,X,X,X,X
		Joiner joiner = Joiner.on(",").skipNulls();
		String idsJoined = joiner.join(idList);
		idsJoined = "(" + idsJoined + ")";

		/**
		 * SELECT entity FROM X Where id in (x,x,x,x,x)
		 */
		String entityName = this.getEntityNameForQueries();
		String jpql = " SELECT entity FROM " + entityName + " entity";
		jpql = jpql + " WHERE  entity.id IN " + idsJoined;

		TypedQuery<T> query = em.createQuery(jpql, this.tClass);
		return query.getResultList();
	}

	@SuppressWarnings("finally")
	protected Long executeCountQuery(TypedQuery<Long> query) {
		Long answer = null;
		try {
			answer = query.getSingleResult();
		} catch (NoResultException exception) {
			answer = new Long(0);
		} finally {
			return answer;
		}

	}

}
