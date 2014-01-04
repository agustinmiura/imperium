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
package ar.com.imperium.common.json.implementation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import ar.com.imperium.common.json.IJsonHelper;
import ar.com.imperium.common.json.gson.entitiyserializers.ApplicationSerializer;
import ar.com.imperium.common.json.gson.entitiyserializers.PermissionSerializer;
import ar.com.imperium.common.json.gson.entitiyserializers.RoleSerializer;
import ar.com.imperium.common.json.gson.entitiyserializers.SubjectSerializer;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author user
 * 
 */
@Component("gsonJsonHelper")
public class GsonImplementation implements IJsonHelper
{
    private Gson gson;

    public GsonImplementation()
    {
        subConstructor();
    }

    private void subConstructor()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();

        ApplicationSerializer appSerializer = new ApplicationSerializer();

        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Application.class, appSerializer);
        gsonBuilder.registerTypeAdapter(
            Permission.class,
            new PermissionSerializer());
        gsonBuilder.registerTypeAdapter(Subject.class, new SubjectSerializer());
        gsonBuilder.registerTypeAdapter(Role.class, new RoleSerializer());

        this.gson = gsonBuilder.create();
    }

    @Override
    public String toJson(Object src) throws Exception
    {
        return gson.toJson(src);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ar.com.imperium.common.json.IJsonHelper#getServerAnswerTempalte(boolean)
     */
    @Override
    public String getServerAnswerTemplate(boolean success) throws Exception
    {
        Map<String, Object> aMap = new HashMap<String, Object>();
        aMap.put("success", success);
        aMap.put("errorCode", "{toReplaceErrorCode}");
        aMap.put("data", "{toReplaceData}");
        aMap.put("errorCode", "{toReplaceErrorCode}");
        aMap.put("message", "{toReplaceErrorMessage}");
        aMap.put("total", "{toReplaceTotal}");

        return gson.toJson(aMap, aMap.getClass());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ar.com.imperium.common.json.IJsonHelper#decodeJson(java.lang.String)
     */
    @Override
    public Map<String, Object> decodeJson(String content) throws Exception
    {
        Map<String, Object> answer = gson.fromJson(content, Map.class);
        return answer;
    }

    public String encodeMap(Map map) throws Exception
    {
        return gson.toJson(map, Map.class);
    }

}
