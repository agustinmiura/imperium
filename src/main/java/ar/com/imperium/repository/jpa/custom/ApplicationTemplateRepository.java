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
package ar.com.imperium.repository.jpa.custom;

import java.util.List;
import java.util.Map;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import ar.com.imperium.domain.Application;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.IBaseRepository;

@SuppressWarnings("deprecation")
public class ApplicationTemplateRepository extends JpaDaoSupport implements
		IBaseRepository<Application, Long> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ar.com.imperium.repository.IBaseRepository#findOneById(java.io.Serializable
	 * )
	 */
	@Override
	public Application findOneById(Long id) {
		// TODO Auto-generated method stub
		return getJpaTemplate().find(Application.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ar.com.imperium.repository.IBaseRepository#create(java.lang.Object)
	 */
	@Override
	public Application create(Application t) {
		// TODO Auto-generated method stub
		getJpaTemplate().persist(t);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ar.com.imperium.repository.IBaseRepository#update(java.lang.Object)
	 */
	@Override
	public Application update(Application t) {
		// TODO Auto-generated method stub
		return getJpaTemplate().merge(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ar.com.imperium.repository.IBaseRepository#delete(java.lang.Object)
	 */
	@Override
	public void delete(Application t) {
		getJpaTemplate().remove(t);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ar.com.imperium.repository.IBaseRepository#findAll(java.lang.Integer,
	 * java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<Application> findAll(Integer page, Integer pageSize,
			String sortCriteria) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ar.com.imperium.repository.IBaseRepository#update(java.io.Serializable,
	 * java.util.Map)
	 */
	@Override
	public Application update(Long id, Map<String, Object> newValues) {
		throw new RuntimeException("Method not implemented");
	}

	@Override
	public List<Application> create(List<Application> entityList) {
		throw new RuntimeException("Method not implemented");
	}

	@Override
	public List<Application> findAllForApplicationWhere(
			Application application, Map<String, Object> queryParams,
			Map<String, Object> pagination, Map<String, Object> order) {
		throw new RuntimeException("Method not implemented");
	}

	@Override
	public Long findQtyForApplicationWhere(Application application,
			Map<String, Object> queryParams) {
		throw new RuntimeException("Method not implemented");
	}

	@Override
	public List<Application> findWithIds(List<Long> idList) {
		throw new RuntimeException("Method not implemented");
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

	@Override
	public void setClass(Class<Application> tClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(List<Application> entityList) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Method not implemented");

	}

}
