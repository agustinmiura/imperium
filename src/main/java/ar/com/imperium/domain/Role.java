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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

@Entity
@Table(name = "im_role")
public class Role implements IDomainEntity<Role>
{

    private static final long serialVersionUID = -7811281901058704279L;

    private Long id;

    private String name;

    private String description;

    private Application application;

    private Set<Permission> permissions;

    private Set<Subject> subjects;

    public Role()
    {
        this("", "");
    }

    public Role(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.permissions = new HashSet<Permission>();
        this.subjects = new HashSet<Subject>();
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    public Long getId()
    {
        return id;
    }

    @Column(name = "name", nullable = false, length = 64)
    public String getName()
    {
        return name;
    }

    @Column(name = "description", nullable = false, length = 64)
    public String getDescription()
    {
        return description;
    }

    @ManyToOne
    @JoinColumn(name = "application_id")
    public Application getApplication()
    {
        return this.application;
    }

    @ManyToMany(mappedBy = "roles")
    public Set<Permission> getPermissions()
    {
        return this.permissions;
    }

    @ManyToMany(mappedBy = "roles")
    public Set<Subject> getSubjects()
    {
        return this.subjects;
    }
    
    public void setSubjects(Set<Subject> subjects)
    {
        this.subjects = subjects;
    }

    public void setPermissions(Set<Permission> permissions)
    {
        this.permissions = permissions;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public boolean hasApplication()
    {
        return (application != null);
    }

    public boolean hasPermission(Permission permission)
    {
        return permissions.contains(permission);
    }

    public String toString()
    {
        ToStringHelper toStringHelper = Objects.toStringHelper(getClass());
        toStringHelper.add("id", id);
        toStringHelper.add("name", name);
        toStringHelper.add("description", description);
        if (application != null) {
            toStringHelper.add("application", application.toString());
        } else {
            toStringHelper.add("application", "Application = NULL");
        }
        return toStringHelper.toString();
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
        return Objects.hashCode(idToHash, name, description, applicationHash);
    }

    @Override
    public int compareTo(Role o)
    {
        return (this.hashCode() - o.hashCode());
    }

    public boolean equals(Object object)
    {
        Role role = (Role) object;
        return (hashCode() == role.hashCode());
    }

    public void removeApplication()
    {
        this.application = null;
    }

    public boolean addPermission(Permission permission)
    {
        return permission.addRole(this);
    }

    public boolean removePermission(Permission permission)
    {
        return permission.removeRole(this);
    }

    @Transient
    public Map<String, Object> getAsMap() throws Exception
    {
        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("id", id);
        answer.put("name", name);
        answer.put("description", description);
        return answer;
    }

}
