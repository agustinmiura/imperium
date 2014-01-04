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
package ar.com.imperium.common.json.gson;

import java.lang.reflect.Type;
import java.util.Set;

import org.springframework.stereotype.Component;

import ar.com.imperium.common.json.IEntityEncoder;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Role;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component("depthApplicationSerializer")
public class DepthApplicationSerializer implements IEntityEncoder,
    JsonSerializer<Application>
{
    private Gson gson;

    public DepthApplicationSerializer()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Application.class, this);
        this.gson = gsonBuilder.create();
    }

    @Override
    public JsonElement serialize(Application src, Type typeOfSrc,
        JsonSerializationContext context)
    {
        JsonElement answer;

        if (src == null) {
            answer = new JsonNull();
        } else {
            JsonObject tempObject = new JsonObject();

            tempObject.add("id", new JsonPrimitive(src.getId()));
            tempObject.add("name", new JsonPrimitive(src.getName()));
            tempObject.add(
                "description",
                new JsonPrimitive(src.getDescription()));

            Set<Role> roles = src.getRoles();

            JsonArray array = new JsonArray();
            for (Role eachRole : roles) {
                array.add(transformRole(eachRole));
            }
            tempObject.add("roles", array);

            answer = tempObject;

        }
        return answer;
    }

    private JsonElement transformRole(Role role)
    {
        JsonElement answer;
        if (role == null) {
            answer = JsonNull.INSTANCE;
        } else {
            answer = new JsonObject();

            ((JsonObject) answer).add("id", new JsonPrimitive(role.getId()));
            ((JsonObject) answer)
                .add("name", new JsonPrimitive(role.getName()));
            ((JsonObject) answer).add(
                "description",
                new JsonPrimitive(role.getDescription()));
            String applicationName =
                (role.getApplication() != null) ? (role.getApplication()
                    .getName()) : ("null");
            ((JsonObject) answer).add("application", new JsonPrimitive(
                applicationName));
        }

        return answer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ar.com.imperium.common.json.IEntityEncoder#encode(java.lang.Object)
     */
    @Override
    public String encode(Object object) throws Exception
    {
        // TODO Auto-generated method stub
        return gson.toJson(object);
    }

}
