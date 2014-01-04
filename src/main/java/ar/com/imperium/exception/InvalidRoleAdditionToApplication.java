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

import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Role;

public class InvalidRoleAdditionToApplication extends ImperiumRuntimeException
{
    private static final long serialVersionUID = 5813322680429943032L;
    protected Application application;
    protected Role role;

    public InvalidRoleAdditionToApplication(Application application, Role role)
    {
        super("Cannot add the role with id " + role.getId()
            + " to the application " + application.getId());
        this.application = application;
        this.role = role;

    }
}
