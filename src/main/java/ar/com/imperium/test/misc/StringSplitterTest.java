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
package ar.com.imperium.test.misc;

import com.google.common.base.Splitter;

public class StringSplitterTest {

	public static void main(String[] args) {
		testStringSplitter();
	}

	private static void testStringSplitter() {
		String sample = "create, read, update, remove";
		Iterable<String> answer = Splitter.on(",").trimResults()
				.omitEmptyStrings().split(sample);

		for (String string : answer) {
			System.out.println("The answer is :" + string);
		}
	}
}
