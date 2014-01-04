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

import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.User;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.IUserRepository;

@Service("userRepository")
@Repository
public class UserRepository extends AbstractJpaRepository<User, Long> implements
    IUserRepository
{

    private static final Logger logger = LoggerFactory
        .getLogger(UserRepository.class);

    @Override
    public User update(Long id, Map<String, Object> newValues)
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public List<User> findAllForApplicationWhere(Application application,
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order)
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Long findQtyForApplicationWhere(Application application,
        Map<String, Object> queryParams)
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public List<User> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria, String direction)
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public User findOneWithDetailById(Long id)
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public String getEntityNameForQueries()
    {
        return "User";
    }

    @Override
    public User getByUserAndPassword(String user, String password)
        throws Exception
    {
        String jpql = " SELECT user FROM User user ";
        jpql = jpql + " WHERE user.name = :name AND ";
        jpql = jpql + " user.password = :password ";

        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("name", user);
        query.setParameter("password", password);

        try {
            return query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }

    }

    @Override
    public void changePassword(User user, String newPassword) throws Exception
    {
        User managedUser = user;
        if (!em.contains(user)) {
            managedUser = em.merge(user);
        }
        managedUser.setPassword(newPassword);
    }

    public List<Application> findAll(Map<String, Object> pagination,
        Map<String, Object> order) throws ImperiumException
    {
        throw new RuntimeException("Not implemented");
    }

    public List<User> listAll(Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        // fetch params
        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        String jpql = "SELECT user FROM User user ORDER BY user." + sort;
        jpql = jpql + " " + direction;

        TypedQuery<User> query = em.createQuery(jpql, User.class);

        query.setFirstResult(page * maxSize);
        query.setMaxResults(maxSize);

        return query.getResultList();
    }

    public List<User> listAllWhere(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws Exception
    {
        // fetch params
        Integer page = (Integer) pagination.get("page");
        Integer maxSize = (Integer) pagination.get("maxSize");

        String sort = (String) order.get("sort");
        String direction = (String) order.get("direction");

        String queryParam = (String) queryParams.get("query");

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT DISTINCT user FROM User user WHERE ");
        sBuffer.append(" user.name LIKE :name ");
        sBuffer.append(" ORDER BY user." + sort + " " + direction);

        String jpql = sBuffer.toString();

        TypedQuery<User> query = em.createQuery(jpql, User.class);
        String param = "%" + queryParam + "%";
        query.setParameter("name", param);

        query.setMaxResults(maxSize);
        query.setFirstResult(page * maxSize);

        return query.getResultList();
    }

    public Long getTotal() throws Exception
    {
        String jpql = " SELECT COUNT(user.id) FROM User user";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        return executeCountQuery(query);
    }

    public Long getTotalWhere(Map<String, Object> queryParams) throws Exception
    {
        String queryParam = (String) queryParams.get("query");

        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(" SELECT COUNT(user.id) FROM User user WHERE ");
        sBuffer.append(" user.name LIKE :name ");

        String jpql = sBuffer.toString();

        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        String param = "%" + queryParam + "%";
        query.setParameter("name", param);

        return executeCountQuery(query);

    }

}
