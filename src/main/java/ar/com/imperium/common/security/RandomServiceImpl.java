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
package ar.com.imperium.common.security;

import java.security.SecureRandom;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component("randomService")
public class RandomServiceImpl implements IRandomService
{
    private SecureRandom secureRandom;

    public RandomServiceImpl() throws Exception
    {
        secureRandom = SecureRandom.getInstance("SHA1PRNG");
    }

    @Override
    public String generateRandomString(int size) throws Exception
    {
        String randomString = new Integer(secureRandom.nextInt()).toString();
        String hash = DigestUtils.sha512Hex(randomString);
        String answer = hash;
        if (answer.length() >= size) {
            answer = hash.substring(0, size - 1);
        }
        return answer;
    }

}
