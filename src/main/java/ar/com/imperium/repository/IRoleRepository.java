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

/**
 * @author user
 * 
 */
public interface IRoleRepository extends IBaseRepository<Role, Long>
{
    public List<Subject> addToRole(Role role, List<Subject> subjectList)
        throws ImperiumException;

    public Long findQtyForApplication(Application application)
        throws ImperiumException;

    public List<Role> findAllForApplication(Application application,
        Integer page, Integer maxSize, String sortCriteria)
        throws ImperiumException;

    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination);

    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination, Map<String, Object> order);

    public Long findQtyForPermissionsForRole(Map<String, Object> queryParams);

    public List<Permission> findPermissionForRole(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order);

    public Long findPermissionQty(Role role);

    public void assignPermissions(Role role, List<Permission> permissionList)
        throws ImperiumException;

    public void removePermissions(Role role, List<Permission> permissionList)
        throws ImperiumException;

    public List<Role> findForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception;

    public Long findQtyForSubjectInApplication(Map<String, Object> queryParams)
        throws Exception;

    public List<Role> findAvailableForSubjectInApplication(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order) throws Exception;

    public Long findAvailableQtyForSubjectInApplication(
        Map<String, Object> queryParams) throws Exception;

}
