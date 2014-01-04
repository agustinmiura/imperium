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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.imperium.domain.Application;

import com.google.gson.JsonElement;

/**
 * @author user
 * 
 */
public class ApplicationTypeAdapterFactory extends
    CustomizedTypeAdapterFactory<Application>
{
    public static Logger logger = LoggerFactory
        .getLogger(ApplicationTypeAdapterFactory.class);

    public ApplicationTypeAdapterFactory()
    {
        super(Application.class);
    }

    @Override
    protected void beforeWrite(Application source, JsonElement toSerialize)
    {

    }

    @Override
    protected void afterRead(JsonElement deserialized)
    {
    }

}
