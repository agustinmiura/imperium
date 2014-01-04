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

import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.exception.ImperiumException;

/**
 * @author user
 * 
 */
public interface IRoleService extends IService<Role, Long>
{
    public void assignPermissions(Role role, List<Permission> permissionList)
        throws ImperiumException;

    public List<Subject> addToRole(Role role, List<Subject> subjectList)
        throws ImperiumException;

    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination);

    public List<Permission> findPermissions(Role role,
        Map<String, Object> pagination, Map<String, Object> order);

    public Long findPermissionQty(Role role);

    /**
     * Add to the role the permissions with id in toAdd Remove from the role the
     * permissions with id in toRemove
     * 
     * @param roleId
     * @param toAdd
     * @param toRemove
     * @return Map with two lists with the permissions added and removed from
     *         the role
     * @throws ImperiumException
     */
    public Map<String, Object> updatePermissions(Long roleId, List<Long> toAdd,
        List<Long> toRemove) throws ImperiumException;

    public Long findQtyForPermissionsForRole(Map<String, Object> queryParams);

    public List<Permission> findPermissionForRole(
        Map<String, Object> queryParams, Map<String, Object> pagination,
        Map<String, Object> order);

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

    public void remove(List<Role> toRemove) throws Exception;
}
