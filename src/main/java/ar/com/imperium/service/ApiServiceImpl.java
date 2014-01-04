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
package ar.com.imperium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.imperium.service.interfaces.IApiService;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IPermissionService;
import ar.com.imperium.service.interfaces.IRoleService;
import ar.com.imperium.service.interfaces.ISubjectService;

@Service("apiServiceImpl")
@Transactional
public class ApiServiceImpl implements IApiService
{

    private static final Logger logger = LoggerFactory
        .getLogger(ApiServiceImpl.class);

    /**
     * Application service
     */
    // @Autowired
    // @Qualifier("jpaComplexApplicationService")
    // private IApplicationService applicationService;

    /**
     * Permission service
     */
    @Autowired
    @Qualifier("jpaComplexPermissionService")
    private IPermissionService permissionService;

    /**
     * Subject service
     */
    @Autowired
    @Qualifier("jpaComplexSubjectService")
    private ISubjectService subjectService;

    @Autowired
    @Qualifier("jpaComplexRoleService")
    private IRoleService roleService;

}
