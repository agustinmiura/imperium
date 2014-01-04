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
package ar.com.imperium.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

@Entity
@Table(name = "im_permission")
public class Permission implements IDomainEntity<Permission>
{
    private static final long serialVersionUID = -3937832033341642010L;

    private Long id;

    private String resource;

    private String action;

    private Application application;

    private Set<Role> roles = new HashSet<Role>();

    public Permission()
    {
        this("", "");
    }

    public Permission(String resource, String action)
    {
        this.resource = resource;
        this.action = action;
    }

    public Permission(String resource, String action, Application application)
    {
        this(resource, action);
        this.application = application;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    public Long getId()
    {
        // TODO Auto-generated method stub
        return id;
    }

    @Column(name = "resource", nullable = false, length = 255)
    public String getResource()
    {
        return resource;
    }

    @Column(name = "action", nullable = false, length = 255)
    public String getAction()
    {
        return action;
    }

    @ManyToOne
    @JoinColumn(name = "application_id")
    public Application getApplication()
    {
        return this.application;
    }

    @ManyToMany
    @JoinTable(name = "im_permission_role", joinColumns = @JoinColumn(
        name = "permission_id"), inverseJoinColumns = @JoinColumn(
        name = "role_id"))
    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public boolean addRole(Role role)
    {
        return roles.add(role);
    }

    public boolean removeRole(Role role)
    {
        return roles.remove(role);
    }

    // misc methods

    public void removeApplication()
    {
        if (application != null) {
            application = null;
        }
    }

    public boolean hasApplication()
    {
        return (application != null);
    }

    @Transient
    public String getDescriptionString()
    {
        return resource + "." + action;
    }

    // getters and setters

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        Long idToHash = new Long(-1);
        if (id != null) {
            idToHash = id;
        }
        Integer applicationHash = -1;
        if (application != null) {
            applicationHash = application.hashCode();
        }
        
        return Objects.hashCode(idToHash, resource, action, applicationHash);
    }

    @Override
    public int compareTo(Permission o)
    {
        return (this.hashCode() - o.hashCode());
    }

    public boolean equals(Object object)
    {
        Permission permission = (Permission) object;
        return (hashCode() == permission.hashCode());
    }

    public String toString()
    {
        ToStringHelper toStringHelper = Objects.toStringHelper(getClass());
        toStringHelper.add("id", id);
        toStringHelper.add("resource", resource);
        toStringHelper.add("action", action);

        if (application != null) {
            toStringHelper.add("application", application.toString());
        } else {
            toStringHelper.add("application", "Application = NULL");
        }

        return toStringHelper.toString();
    }

    public void clearRoles()
    {
        this.roles.clear();
    }

    @Transient
    public Map<String, Object> getAsMap() throws Exception
    {
        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("id", this.id);
        answer.put("resource", resource);
        answer.put("action", action);
        return answer;
    }
    
    public static Permission createWithId(Long id) 
    {
    	Permission permission = new Permission();
    	permission.setId(id);
    	return permission;
    }

}
