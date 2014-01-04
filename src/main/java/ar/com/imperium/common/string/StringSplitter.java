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
package ar.com.imperium.common.string;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;

import org.springframework.stereotype.Component;

@Component("stringSplitter")
public class StringSplitter {

	private Iterable<String> subSplit(String input, String separator) {
		return Splitter.on(separator).trimResults().omitEmptyStrings().split(input);
	}
	
	public Iterable<String> split(String input) {
		return subSplit(input, ",");
	}
	
	public List<String> getListFromSplit(String input, String separator) 
	{
		List<String> answer = new ArrayList<String>();
		Iterable<String> stringIterable = subSplit(input, separator);
		for (String string : stringIterable) {
			answer.add(string);
		}
		return answer;
	}
}
