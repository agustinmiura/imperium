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
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

@Entity
@Table(name = "im_user")
public class User implements IDomainEntity<User>
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "type")
    private Integer type;

    public User()
    {
        subConstructor("", "", UserType.USER.getType());
    }

    public User(String name, String password, Integer type)
    {
        this.subConstructor(name, password, type);
    }

    private void subConstructor(String name, String password, Integer type)
    {
        this.name = name;
        this.password = password;
        this.type = type;

    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return this.type;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Transient
    public Map<String, Object> getAsMap() throws Exception
    {
        Map<String, Object> answer = new HashMap<String, Object>();
        answer.put("id", this.id);
        answer.put("name", name);
        answer.put("password", "");
        answer.put("type", type.toString());

        UserType userType = UserType.fromCode(type);
        answer.put("typeAsString", userType.toString());

        answer.put("DT_RowId", this.id);
        answer.put("action", this.id);
        answer.put("isAdmin", type == UserType.ADMIN.getType());
        return answer;
    }

    @Override
    public int compareTo(User o)
    {
        return (hashCode() - o.hashCode());
    }

    public int hashCode()
    {
        Long idToHash = new Long(-1);
        if (id != null) {
            idToHash = id;
        }
        return Objects.hashCode(idToHash, name, password, type);

    }

    public boolean equals(Object object)
    {
        User user = (User) object;
        return (hashCode() == user.hashCode());
    }

    public String toString()
    {
        Long idToUser = new Long(-1);
        if (id != null) {
            idToUser = id;
        }
        ToStringHelper toStringHelper = Objects.toStringHelper(getClass());
        toStringHelper.add("name", name);
        toStringHelper.add("password", password);
        toStringHelper.add("id", idToUser);
        toStringHelper.add("type", type);
        return toStringHelper.toString();
    }

}
