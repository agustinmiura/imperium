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
package ar.com.imperium.common.json.gson.entitiyserializers;

import java.lang.reflect.Type;

import ar.com.imperium.domain.Subject;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author user
 * 
 */
public class SubjectSerializer implements JsonSerializer<Subject>
{
    @Override
    public JsonElement serialize(Subject src, Type typeOfSrc,
        JsonSerializationContext context)
    {
        JsonElement answer = null;

        if (src == null) {
            answer = JsonNull.INSTANCE;
        } else {
            JsonObject tempObject = new JsonObject();
            tempObject.add("id", new JsonPrimitive(src.getId()));
            tempObject.add("DT_RowId", new JsonPrimitive(src.getId()));
            tempObject.add("name", new JsonPrimitive(src.getName()));

            String application = "";
            if (src.hasApplication()) {
                application = src.getApplication().getName();
            }
            tempObject.add("application", new JsonPrimitive(application));

            answer = tempObject;
        }
        return answer;
    }

}
