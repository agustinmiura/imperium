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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.User;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.IUserRepository;
import ar.com.imperium.service.interfaces.IUserService;

@Service("jpaUserService")
@Transactional
public class UserServiceImpl implements IUserService
{

    private static final Logger logger = LoggerFactory
        .getLogger(UserServiceImpl.class);

    private IUserRepository userRepository;

    @Autowired
    @Qualifier("userRepository")
    public void setUserRepository(final IUserRepository userRepository)
    {
        userRepository.setClass(User.class);
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll(Integer page, Integer maxSize, String sortCriteria)
        throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Long findTotal()
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public List<User> findAllWithDetail(Integer page, Integer maxSize,
        String sortCriteria) throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public User findById(Long id) throws ImperiumException
    {
        return userRepository.findOneById(id);
    }

    @Override
    public User findWithDetailById(Long id) throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public List<User> create(List<User> entityList) throws ImperiumException
    {
        return userRepository.create(entityList);

    }

    @Override
    public User create(User entityData) throws ImperiumException
    {
        return userRepository.create(entityData);

    }

    @Override
    public User update(User entity) throws ImperiumException
    {
        return userRepository.update(entity);
    }

    @Override
    public User update(Long id, Map<String, Object> newValues) throws Exception
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public void delete(User entity) throws ImperiumException
    {
        userRepository.delete(entity);

    }

    @Override
    public List<User> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria)
        throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public List<User> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria, String direction)
        throws ImperiumException
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
    public Long findQtyForApplication(Application application)
        throws ImperiumException
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
    public List<User> findWithIds(List<Long> idList)
    {
        throw new RuntimeException("Method not implemented");

    }

    @Override
    public User findOneByUserAndPassword(String user, String password)
        throws Exception
    {
        return userRepository.getByUserAndPassword(user, password);
    }

    @Override
    public void changePassword(User user, String newPassword) throws Exception
    {
        userRepository.changePassword(user, newPassword);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword)
        throws Exception
    {
        String validOldPassword = user.getPassword();

        if (validOldPassword.compareTo(oldPassword) != 0) {
            throw new RuntimeException(
                "The old password does not match the old password user");
        } else {
            userRepository.changePassword(user, newPassword);
        }
    }

    public List<User> listAll(Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        return userRepository.listAll(pagination, order);
    }

    public List<User> listAllWhere(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws Exception
    {
        return userRepository.listAllWhere(queryParams, pagination, order);
    }

    public Long getTotal() throws Exception
    {
        return userRepository.getTotal();
    }

    public Long getTotalWhere(Map<String, Object> queryParams) throws Exception
    {
        return userRepository.getTotalWhere(queryParams);
    }

}
