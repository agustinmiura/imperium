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
@Table(name = "im_subject")
public class Subject implements IDomainEntity<Subject>
{

    /**
     * 
     */
    private static final long serialVersionUID = -792930555481093667L;
    private Long id;
    private String name;
    private Set<Role> roles = new HashSet<Role>();
    private Application application;

    public Subject()
    {
        this("");
    }

    public Subject(String name)
    {
        subConstructor(name);
    }

    private void subConstructor(String name)
    {
        this.name = name;
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

    @ManyToMany
    @JoinTable(
        name = "im_subject_role",
        joinColumns = @JoinColumn(name = "subject_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles()
    {
        return roles;
    }

    @ManyToOne
    @JoinColumn(name = "application_id")
    public Application getApplication()
    {
        return this.application;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean hasRole(Role role)
    {
        return roles.contains(role);
    }

    public boolean addRole(Role role)
    {
        boolean check = roles.add(role);
        return check;
    }

    public void removeRole(Role role)
    {
        roles.remove(role);
    }

    public void clearRoles()
    {
        roles.clear();
    }

    public boolean removeRoleWithResult(Role role)
    {
        return roles.remove(role);
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

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

    /**
     * hash code , compare to and toString
     */
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
        return Objects.hashCode(idToHash, name, applicationHash);
    }

    public int compareTo(Subject o)
    {
        return (this.hashCode() - o.hashCode());
    }

    public boolean equals(Object object)
    {
        Subject subject = (Subject) object;
        return (hashCode() == subject.hashCode());
    }

    public String toString()
    {
        String idString = " id = NULL";
        if (id != null) {
            idString = id.toString();
        }

        ToStringHelper toStringHelper = Objects.toStringHelper(getClass());
        toStringHelper.add("id", idString);
        toStringHelper.add("name", name);
        return toStringHelper.toString();
    }

    @Transient
    public Map<String, Object> getAsMap() throws Exception
    {
        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("id", id);
        answer.put("name", name);
        return answer;
    }

}
