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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;
import ar.com.imperium.repository.IRoleRepository;
import ar.com.imperium.repository.jpa.SubjectRepositoryImpl;
import ar.com.imperium.service.interfaces.ISubjectService;

@Service("jpaComplexSubjectService")
@Transactional
public class SubjectServiceImpl implements ISubjectService
{
    private static final Logger logger = LoggerFactory
        .getLogger(SubjectServiceImpl.class);

    private SubjectRepositoryImpl subjectRepository;

    private IRoleRepository roleRepository;

    @Autowired
    @Qualifier("subjectRepository")
    public void setSubjectRepositoryImpl(
        final SubjectRepositoryImpl subjectRepository)
    {
        subjectRepository.setClass(Subject.class);
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Subject> findAll(Integer offset, Integer maxSize,
        String sortCriteria) throws ImperiumException
    {
        return subjectRepository.findAll(offset, maxSize, sortCriteria);
    }

    @Override
    public List<Subject> findAllWithDetail(Integer offset, Integer maxSize,
        String sortCriteria) throws ImperiumException
    {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Subject findById(Long id) throws ImperiumException
    {
        return subjectRepository.findOneById(id);
    }

    @Override
    public Subject findWithDetailById(Long id) throws ImperiumException
    {
        return subjectRepository.findOneWithDetailById(id);
    }

    @Override
    public Subject create(Subject entityData) throws ImperiumException
    {
        return subjectRepository.create(entityData);
    }

    @Override
    public Subject update(Subject entity) throws ImperiumException
    {
        Map<String, Object> newValues = new HashMap<String, Object>();
        newValues.put("name", entity.getName());
        return subjectRepository.update(entity.getId(), newValues);
    }

    @Override
    public Subject update(Long id, Map<String, Object> newValues)
        throws Exception
    {
        return subjectRepository.update(id, newValues);
    }

    @Override
    public void delete(Subject entity) throws ImperiumException
    {
        Subject found = subjectRepository.findOneWithDetailById(entity.getId());

        // remove from the application
        found.removeApplication();

        // remove from the roles being used
        found.clearRoles();
    }

    @Override
    public List<Subject> findAllForApplication(Application application,
        Integer offset, Integer maxSize, String sortCriteria)
        throws ImperiumException
    {
        return subjectRepository.findAllForApplication(
            application,
            offset,
            maxSize,
            sortCriteria);
    }

    @Override
    public Long findQtyForApplication(Application application)
        throws ImperiumException
    {
        return subjectRepository.findQtyForApplication(application);
    }

    @Override
    public List<Subject> create(List<Subject> entityList)
        throws ImperiumException
    {
        return subjectRepository.create(entityList);
    }

    @Override
    public void addToApplication(Application application,
        List<Subject> subjectList) throws Exception
    {
        subjectRepository.addToApplication(application, subjectList);
    }

    @Override
    public Long findTotal()
    {
        return subjectRepository.getEntityQty();
    }

    @Override
    public List<Subject> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria, String direction)
        throws ImperiumException
    {
        return subjectRepository.findAllForApplication(
            application,
            page,
            maxSize,
            sortCriteria,
            direction);
    }

    @Override
    public List<Subject> findAllForApplicationWhere(Application application,
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order)
    {
        return subjectRepository.findAllForApplicationWhere(
            application,
            queryParams,
            pagination,
            order);
    }

    @Override
    public Long findQtyForApplicationWhere(Application application,
        Map<String, Object> queryParams)
    {
        return subjectRepository.findQtyForApplicationWhere(
            application,
            queryParams);
    }

    @Override
    public List<Subject> findSubjectsForRole(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws Exception
    {
        return subjectRepository.findSubjectsForRole(
            queryParams,
            pagination,
            order);
    }

    @Override
    public Long findQtyForSubjectsForRole(Map<String, Object> queryParams)
        throws Exception
    {
        return subjectRepository.findQtyForSubjectsForRole(queryParams);
    }

    @Override
    public Map<String, Object> updateRoles(Long subjectId, List<Role> toAdd,
        List<Role> toRemove) throws Exception
    {
        return subjectRepository.updateRoles(subjectId, toAdd, toRemove);
    }

    @Override
    public List<Subject> findAvailableForRoleInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        return subjectRepository.findAvailableForRoleInApplication(
            queryParams,
            pagination,
            order);
    }

    @Override
    public Long findQtyAvailableForRoleInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception
    {
        return subjectRepository.findQtyAvailableForRoleInApplication(
            queryParams,
            pagination,
            order);
    }

    @Override
    public Map<String, Object> updateRoles(Subject subject, List<Long> toAdd,
        List<Long> toRemove) throws Exception
    {
        return subjectRepository.updateRoles(subject, toAdd, toRemove);
    }

    @Override
    public List<Subject> findWithIds(List<Long> idList)
    {
        return subjectRepository.findWithIds(idList);
    }

    @Override
    public Map<Long, Long> findSubjectQtyForRole(List<Role> roleList)
        throws Exception
    {
        return subjectRepository.findSubjectQtyForRole(roleList);
    }

}
