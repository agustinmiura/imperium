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
package ar.com.imperium.service.interfaces;

import java.util.List;
import java.util.Map;

import ar.com.imperium.domain.User;

public interface IUserService extends IService<User, Long>
{
    public User findOneByUserAndPassword(String user, String password)
        throws Exception;

    public void changePassword(User user, String newPassword) throws Exception;

    public void changePassword(User user, String oldPassword, String newPassword)
        throws Exception;

    public List<User> listAll(Map<String, Object> pagination,
        Map<String, Object> order) throws Exception;

    public List<User> listAllWhere(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws Exception;

    public Long getTotal() throws Exception;

    public Long getTotalWhere(Map<String, Object> queryParams) throws Exception;

}
