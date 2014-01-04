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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

@Entity
@Table(name = "im_application")
public class Application implements IDomainEntity<Application>
{
    private static final Logger logger = LoggerFactory
        .getLogger(Application.class);

    /**
     * 
     */
    private static final long serialVersionUID = -7174111508296098243L;
    private Long id;
    private String name;
    private String description;
    private String apiKey;

    private Set<Role> roles = new HashSet<Role>();

    private Set<Permission> permissions = new HashSet<Permission>();

    private Set<Subject> subjects = new HashSet<Subject>();

    public Application()
    {
        this("", "", "");
    }

    public Application(String name, String description, String key)
    {
        subConstructor(name, description, key);
    }

    private void subConstructor(String name, String description, String key)
    {
        this.name = name;
        this.description = description;
        this.apiKey = key;

    }

    /**
     * Mappings
     */

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

    @Column(name = "apiKey", nullable = false)
    public String getApiKey()
    {
        return apiKey;
    }

    @OneToMany(
        mappedBy = "application",
        fetch = FetchType.LAZY,
        targetEntity = ar.com.imperium.domain.Role.class,
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    public Set<Role> getRoles()
    {
        return roles;
    }

    @OneToMany(
        mappedBy = "application",
        fetch = FetchType.LAZY,
        targetEntity = ar.com.imperium.domain.Permission.class,
        cascade = { CascadeType.ALL },
        orphanRemoval = false)
    public Set<Permission> getPermissions()
    {
        return permissions;
    }

    @OneToMany(
        mappedBy = "application",
        fetch = FetchType.LAZY,
        targetEntity = ar.com.imperium.domain.Subject.class,
        cascade = { CascadeType.PERSIST },
        orphanRemoval = false)
    public Set<Subject> getSubjects()
    {
        return subjects;
    }

    /**
     * @todo test
     * @param subject
     * @return
     */
    public boolean addSubject(Subject subject)
    {
        boolean check = subjects.add(subject);
        if (check) {
            subject.setApplication(this);
        }
        return check;
    }

    public boolean removeSubject(Subject subject)
    {
        boolean check = subjects.remove(subject);
        if (check) {
            subject.removeApplication();
        }
        return check;
    }

    public boolean contains(Subject subject)
    {
        boolean answer = false;
        if (subjects != null) {
            answer = subjects.contains(subject);
        }
        return answer;
    }

    /**
     * 
     * @param permission
     */
    public boolean addPermission(Permission permission)
    {
        boolean check = permissions.add(permission);
        permission.setApplication(this);
        return check;
    }

    /**
     * 
     * @param permission
     * @return
     */
    public boolean removePermission(Permission permission)
    {
        boolean exist = permissions.remove(permission);
        if (exist) {
            permission.removeApplication();
        }
        return exist;
    }

    public boolean containsRole(Role role)
    {
        boolean answer = false;
        if (roles != null) {
            answer = roles.contains(role);
        }
        return answer;
    }

    /**
     * Getters and setters
     */

    /**
     * 
     * @param roles
     */
    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public void setPermissions(Set<Permission> permissions)
    {
        this.permissions = permissions;
    }

    public void setSubjects(Set<Subject> subjects)
    {
        this.subjects = subjects;
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

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public void addRole(Role role)
    {
        roles.add(role);
        role.setApplication(this);
    }

    public boolean addRoleWithValidation(Role role)
    {
        boolean check = roles.add(role);
        if (check) {
            role.setApplication(this);
        }
        return check;
    }

    public void removeRole(Role role)
    {
        roles.remove(role);
    }

    public boolean removeRoleWithValidation(Role role)
    {
        return roles.remove(role);
    }

    public Role getRoleWithName(String name)
    {
    	Set<Role> roles = this.roles;
    	Iterator<Role> iterator = roles.iterator();
    	Role answer = null;
    	Role currentRole;
    	String currentName;
    	while(iterator.hasNext()) {
    		currentRole = iterator.next();
    		currentName = currentRole.getName();
    		if (currentName.compareTo(name)==0) {
    			answer = currentRole;
    			break;
    		}
    	}
    	return answer;
    }
    
    public Subject getSubjectWithName(String name)
    {
    	Subject answer = null;
    	
    	String currentName;
    	Set<Subject> subjects  = this.subjects;
    	for(Subject eachSubject:subjects) {
    		answer = eachSubject;
    		currentName = answer.getName();
    		if (currentName.compareTo(name)==0) {
    			break;
    		}
    	}
    	
    	return answer;
    }
    
    public List<Permission> getPermissionsForResource(String resourceName)
    {
    	List<Permission> answer = new ArrayList<Permission>();
    	
    	Set<Permission> permissions = this.permissions;
    	String eachResourceName=null;
    	for(Permission eachPermission:permissions) {
    		eachResourceName = eachPermission.getResource();
    		if (eachResourceName.compareTo(resourceName)==0) {
    			answer.add(eachPermission);
    		}
    	}
    	
    	return answer;
    }
    
    public String toString()
    {
        return Objects
            .toStringHelper(getClass())
            .add("id", id)
            .add("name", name)
            .add("description", description)
            .add("apiKey", apiKey)
            .toString();
    }

    @Override
    public int compareTo(Application o)
    {
        Integer currentId = Integer.parseInt(id.toString());
        Integer otherId = Integer.parseInt(o.getId().toString());

        return (currentId - otherId);
    }

    @Transient
    public Map<String, Object> getAsMap() throws Exception
    {
        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("id", id);
        answer.put("DT_RowId", id);
        answer.put("name", name);
        answer.put("description", description);
        answer.put("action", "");
        answer.put("apiKey", "");
        return answer;
    }
}
