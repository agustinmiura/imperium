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
package ar.com.imperium.common.json;

import java.util.Map;

/**
 * @author user
 * 
 */
public interface IJsonHelper
{
    public String toJson(Object src) throws Exception;

    public String getServerAnswerTemplate(boolean success) throws Exception;

    public Map<String, Object> decodeJson(String content) throws Exception;

    public String encodeMap(Map map) throws Exception;
}
