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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("dataTablesHelper")
public class DataTablesPluginHelper
{
    @Autowired
    @Qualifier("servletHelper")
    private ServletHelper servletHelper;

    private Logger logger = LoggerFactory
        .getLogger(DataTablesPluginHelper.class);

    private String[] integerParams;
    private String[] stringParams;
    private String[] booleanParams;

    public DataTablesPluginHelper()
    {
        integerParams =
            new String[] {
                    "iDisplayStart",
                    "iDisplayLength",
                    "iColumns",
                    "iSortingCols",
                    "iSortCol_0",
                    "sEcho" };

        stringParams = new String[] { "sSearch", "iSortingCols", "sSortDir_0" };

        booleanParams = new String[] { "bRegex" };
    }

    /**
     * @todo add docs for the method and refactor into a shorter one
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String, Object> getDatatableParameters(HttpServletRequest request)
        throws Exception
    {
        Map<String, Object> answer = new HashMap<String, Object>();
        // read integer params
        Integer value;
        for (String eachName : integerParams) {
            value = servletHelper.readIntegerFromRequest(request, eachName, -1);
            answer.put(eachName, value);
        }

        // read string params
        String stringValue;
        for (String eachName : stringParams) {
            stringValue =
                servletHelper.readStringFromRequest(request, eachName, "");
            answer.put(eachName, stringValue);
        }
        // read boolean params
        Boolean boolValue;
        String boolString;
        for (String eachName : booleanParams) {
            boolString =
                servletHelper.readStringFromRequest(request, eachName, "false");

            boolValue = false;
            if (boolString.compareTo("true") == 0) {
                boolValue = true;
            }
            answer.put(eachName, boolValue);
        }

        Integer columnQty = (Integer) answer.get("iColumns");
        String indexString;
        String columnName;
        for (int i = 0; i < columnQty; i++) {
            // read the cols name
            indexString = "mDataProp_" + i;

            columnName =
                servletHelper.readStringFromRequest(request, indexString, "");

            answer.put(indexString, columnName);

            // read if the columns are sortable
            indexString = "bSortable_" + i;
            boolValue = false;
            boolString =
                servletHelper.readStringFromRequest(
                    request,
                    indexString,
                    "false");
            if (boolString.compareTo("true") == 0) {
                boolValue = true;
            }
            answer.put(indexString, boolValue);
        }

        return answer;
    }

    /**
     * Given the params from the client side gets the sorting criteria requested
     * 
     * @param clientParams
     * @return
     */
    public Map<String, String> getSortingCriteria(
        Map<String, Object> clientParams)
    {
        Map<String, String> answer = new HashMap<String, String>();

        Integer colIndex = (Integer) clientParams.get("iSortCol_0");
        String direction = (String) clientParams.get("sSortDir_0");
        String name = (String) clientParams.get("mDataProp_" + colIndex);

        answer.put("name", name);
        answer.put("direction", direction);

        return answer;
    }

    public Integer getPaginationOffset(Map<String, Object> clientParams)
    {
        return (Integer) clientParams.get("iDisplayStart");
    }

    public Integer getPageSize(Map<String, Object> clientParams)
    {
        return (Integer) clientParams.get("iDisplayLength");
    }

    public Integer getVerificationString(Map<String, Object> clientParams)
    {
        return (Integer) clientParams.get("sEcho");
    }

    public String getSearchParam(Map<String, Object> clientParams)
    {
        return (String) clientParams.get("sSearch");
    }

}
