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
package ar.com.imperium.common;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @todo add a method to create a map with the default pagination values
 * 
 * @author user
 * 
 */
@Component("applicationPropertiesHelper")
public class ApplicationProperties implements IPropertiesHelper
{
    @Autowired
    @Qualifier("applicationProperties")
    private Properties applicationProperties;

    private static final Logger logger = LoggerFactory
        .getLogger(ApplicationProperties.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * ar.com.imperium.common.IPropertiesHelper#getProperty(java.lang.String)
     */
    @Override
    public String getProperty(String name)
    {
        String value = applicationProperties.getProperty(name);
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ar.com.imperium.common.IPropertiesHelper#contains(java.lang.String)
     */
    @Override
    public boolean contains(String propertyName)
    {
        return (applicationProperties.getProperty(propertyName) != null);
    }

}
