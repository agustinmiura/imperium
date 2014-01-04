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
package ar.com.imperium.test.json;

import com.google.common.base.Objects;

/**
 * @author user
 * 
 */
public class EmbeddedObject
{
    private Integer id;

    private SampleObject sampleObject;

    public EmbeddedObject(Integer id)
    {
        this.id = id;
    }

    public SampleObject getSampleObject()
    {
        return sampleObject;
    }

    public void setSampleObject(SampleObject sampleObject)
    {
        this.sampleObject = sampleObject;
    }

    public String toString()
    {
        return Objects
            .toStringHelper(getClass())
            .add("id", id)
            .add("sampleObject", sampleObject.toString())
            .toString();

    }
}
