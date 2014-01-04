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
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;

/**
 * @author user
 * 
 */
public interface ISubjectService extends IService<Subject, Long>
{
    public void addToApplication(Application application,
        List<Subject> subjectList) throws Exception;

    public List<Subject> findSubjectsForRole(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws Exception;

    public Long findQtyForSubjectsForRole(Map<String, Object> queryParams)
        throws Exception;

    public List<Subject> findAvailableForRoleInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception;

    public Long findQtyAvailableForRoleInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception;

    public Map<String, Object> updateRoles(Long subjectId, List<Role> toAdd,
        List<Role> toRemove) throws Exception;

    public Map<String, Object> updateRoles(Subject subject, List<Long> toAdd,
        List<Long> toRemove) throws Exception;

    /**
     * For each role return a long with the subject qty where the role is being
     * used
     * 
     * @param roleList
     * @return
     * @throws Exception
     */
    public Map<Long, Long> findSubjectQtyForRole(List<Role> roleList)
        throws Exception;
}
