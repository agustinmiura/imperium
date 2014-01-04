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
package ar.com.imperium.exception;

/**
 * @author user
 * 
 */
public class EntityNotFoundException extends ImperiumException
{
    private static final long serialVersionUID = 5395941555278609687L;
    protected String attributeName;
    protected Object value;

    protected void subConstructor(String attributeName, Object value)
    {
        this.attributeName = attributeName;
        this.value = value;
    }

    public EntityNotFoundException(String className, String attributeName,
        Object value)
    {
        super("Cannot find object of class " + className
            + " with using attribute " + attributeName + " with value " + value);

        subConstructor(attributeName, value);
    }

    public EntityNotFoundException(String attributeName, Object value)
    {
        super("Cannot find entity");
        subConstructor(attributeName, value);
    }

    public EntityNotFoundException(String attributeName, Object value,
        boolean ajaxResponse)
    {
        super("Cannot find entity with attribute :" + attributeName + ":"
            + value, ajaxResponse);

        subConstructor(attributeName, value);
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public Object getValue()
    {
        return value;
    }
}
