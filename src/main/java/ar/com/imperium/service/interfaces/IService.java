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

import ar.com.imperium.domain.Application;
import ar.com.imperium.exception.ImperiumException;

/**
 * @author user
 * 
 */
public interface IService<T, ID>
{
    public List<T> findAll(Integer page, Integer maxSize, String sortCriteria)
        throws ImperiumException;

    public Long findTotal();

    public List<T> findAllWithDetail(Integer page, Integer maxSize,
        String sortCriteria) throws ImperiumException;

    public T findById(Long id) throws ImperiumException;

    public T findWithDetailById(Long id) throws ImperiumException;

    public List<T> create(List<T> entityList) throws ImperiumException;

    public T create(T entityData) throws ImperiumException;

    public T update(T entity) throws ImperiumException;

    public T update(ID id, Map<String, Object> newValues) throws Exception;

    public void delete(T entity) throws ImperiumException;

    public List<T> findAllForApplication(Application application, Integer page,
        Integer maxSize, String sortCriteria) throws ImperiumException;

    public List<T> findAllForApplication(Application application, Integer page,
        Integer maxSize, String sortCriteria, String direction)
        throws ImperiumException;

    public List<T> findAllForApplicationWhere(Application application,
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order);

    public Long findQtyForApplication(Application application)
        throws ImperiumException;

    public Long findQtyForApplicationWhere(Application application,
        Map<String, Object> queryParams);

    public List<T> findWithIds(List<ID> idList);

}
