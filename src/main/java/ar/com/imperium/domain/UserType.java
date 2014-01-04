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

import java.util.HashMap;
import java.util.Map;

/**
 * @author user
 * 
 */
public enum UserType
{
    USER(0, "USER"), ADMIN(1, "ADMIN");

    private Integer type;
    private String representation;

    private UserType(Integer type, String representation)
    {
        this.type = type;
        this.representation = representation;
    }

    public String toString()
    {
        return representation;
    }

    public Integer getType()
    {
        return this.type;
    }

    public Map<String, Object> getAsMap()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("representation", representation);
        return map;
    }

    public static UserType fromCode(Integer type)
    {
        UserType answer = null;
        if (type == UserType.ADMIN.getType()) {
            answer = UserType.ADMIN;
        } else {
            answer = UserType.USER;
        }
        return answer;
    }

}
