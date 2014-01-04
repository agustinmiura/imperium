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
package ar.com.imperium.common.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.com.imperium.domain.Permission;
import org.springframework.stereotype.Component;

@Component("permissionHelper")
public class PermissionHelper {

	public boolean containsSetPermissionById(Permission permission,
			Set<Permission> permissions) {

		boolean answer = false;
		Long idToSearch = permission.getId();
		Long otherId = null;
		for (Permission eachPermission : permissions) {
			otherId = eachPermission.getId();
			if (otherId.compareTo(idToSearch)==0) {
				answer = true;
				break;
			}
		}
		return answer;
	}
	/**
	 * From a list of permissions convert them as Maps 
	 * and return a list
	 * @param permissions
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMapListFromPermissions(List<Permission> permissions) throws Exception 
	{
		List<Map<String, Object>> answer = new ArrayList<Map<String, Object>>();
	
		for (Permission eachPermission : permissions) {
			answer.add(eachPermission.getAsMap());
		}
		
		return answer;
	}
	/**
	 * Given a permission list return a
	 * string with the format user,create;user,remove
	 * 
	 * @param permissions
	 * @return
	 * @throws Exception
	 */
	public String convertToString(List<Permission> permissions) throws Exception
	{
		StringBuffer sBuffer = new StringBuffer();
		
		String permissionString;
		for (Permission permission : permissions) {
			permissionString = permission.getResource()+","+permission.getAction();
			sBuffer.append(permissionString+";");
		}
		sBuffer.deleteCharAt(sBuffer.length()-1);
		
		return sBuffer.toString();
	}

}







