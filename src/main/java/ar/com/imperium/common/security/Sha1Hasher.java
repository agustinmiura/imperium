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

import org.apache.commons.codec.digest.DigestUtils;

public class Sha1Hasher implements IHashService
{
    @Override
    public String hashString(String input) throws Exception
    {
        String hashed = DigestUtils.sha512Hex(input);
        String answer = hashed;
        if (hashed.length() >= (IHashService.HASH_LENGTH + 1)) {
            answer = hashed.substring(0, IHashService.HASH_LENGTH);
        }
        return answer;
    }

}
