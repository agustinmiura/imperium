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
package ar.com.imperium.test.common;

import ar.com.imperium.common.ValidationHelper;

public class ValidationHelperTest {

	public static void main(String[] args) {
		try {
			testListMatch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testListMatch() throws Exception{
		
		String sample = "create,read,update";
		
		ValidationHelper helper = new ValidationHelper();
		boolean isValid = helper.isValidWordList(sample);
		
		System.out.println("For the sample :"+sample+" i see the answer :"+isValid);
	}
	
}
