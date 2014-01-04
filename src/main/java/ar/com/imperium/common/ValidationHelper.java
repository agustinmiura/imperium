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

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component("validationHelper")
public class ValidationHelper {

	private Pattern wordPattern;
	private Pattern wordListPattern;

	public ValidationHelper() {
		wordListPattern = Pattern.compile("^[a-zA-Z0-9]+(,[a-zA-Z0-9]+)*$");
		wordPattern = Pattern.compile("^[a-zA-Z0-9]+$");
	}

	public boolean isValidWord(String word) {
		return wordPattern.matcher(word).matches();
	}

	public boolean isValidWordList(String input) {
		return (wordListPattern.matcher(input).matches());
	}

}
