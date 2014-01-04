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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionTesting {

	public static void main(String[] args) {
		testWordMatch();
	}

	private static void testWordMatch() {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
	
		System.out.println("With one i get :"+pattern.matcher("one").matches());
		System.out.println("With '' i get :"+pattern.matcher("").matches());
		System.out.println("With 'one ' i get "+pattern.matcher("one ").matches());
	}
	
	private static void testListMatch() {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+(,[a-zA-Z0-9]+)*$");
		Matcher matcher = pattern.matcher("one,ee,www,eee");

		System.out.println("With one,ee,www,ee i get the result :"
				+ matcher.matches());
		System.out.println("With one i get the result :"
				+ pattern.matcher("one").matches());
		System.out
				.println("With empty i get :" + pattern.matcher("").matches());
		System.out.println("With 'one,' i get :"
				+ pattern.matcher("one,").matches());
		System.out.println("With 'one, two, three' "
				+ pattern.matcher("one, two, three").matches());
	}
}
