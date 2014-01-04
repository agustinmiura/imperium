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
package ar.com.imperium.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ar.com.imperium.domain.Application;
import ar.com.imperium.exception.ImperiumException;

/**
 * @author user
 * 
 */
public interface IBaseRepository<T, ID extends Serializable>
{
    public void setClass(final Class<T> tClass);

    public T findOneById(ID id);

    public T create(T t);

    public T update(T t);

    public T update(ID id, Map<String, Object> newValues);

    public void delete(T t);

    public List<Application> findAll(Map<String, Object> pagination,
        Map<String, Object> order) throws ImperiumException;

    public List<T> findAll(Integer page, Integer pageSize, String sortCriteria);

    public List<T> create(List<T> entityList);

    public List<T> findWithIds(List<ID> idList);

    public List<T> findAllForApplicationWhere(Application application,
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order);

    public Long findQtyForApplicationWhere(Application application,
        Map<String, Object> queryParams);
    
    public void remove(List<T> entityList);

}
