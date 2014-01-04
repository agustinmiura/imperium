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

import java.util.List;
import java.util.Map;

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;

public interface IApplicationRepository extends
    IBaseRepository<Application, Long>
{
    public void addPermissions(Application application,
        List<Permission> permissionList) throws ImperiumException;

    public void addRoles(Application application, List<Role> roleList)
        throws Exception;

    public void addSubjects(Application application, List<Subject> subjectList)
        throws Exception;

    public List<Application> findAllWhere(Map<String, Object> queryParams,
        Map<String, Object> pagination, Map<String, Object> order)
        throws ImperiumException;

    public Long getQtyForFindAllWhere(Map<String, Object> queryParams)
        throws ImperiumException;

    public Map<String, Object> updateRoles(Long applicatioId, List<Long> toAdd,
        List<Long> toRemove) throws ImperiumException;
}
