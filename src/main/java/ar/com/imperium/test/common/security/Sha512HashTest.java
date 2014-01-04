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
package ar.com.imperium.test.common.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.imperium.common.security.IHashService;
import ar.com.imperium.common.security.Sha1Hasher;

public class Sha512HashTest
{
    private static final Logger logger = LoggerFactory
        .getLogger(Sha512HashTest.class);

    public static void main(String[] args)
    {
        try {
            Sha512HashTest.test();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void test() throws Exception
    {
        IHashService hashService = new Sha1Hasher();

        List<String> passwordList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            passwordList.add("password" + i);
        }
        String hashed;
        for (String eachString : passwordList) {
            hashed = hashService.hashString(eachString);
            logger.debug("<string, hash>" + eachString + "," + hashed);
        }

    }

}
