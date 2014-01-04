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
package ar.com.imperium.common.json;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author user
 * 
 */
public class ServerResponse
{
    public static final String NULL_STRING = "null";

    protected boolean success;
    protected Object data;
    protected Integer code;
    protected Integer errorCode;
    protected String message;
    protected Integer total;
    protected Integer page;

    /* Collections with additional parameters */
    protected Object extraData;

    /**
     * Attributes for the dataTables jquery
     */
    protected String verificationString;
    protected Integer iTotalRecords;
    protected Integer iTotalDisplayRecords;

    public ServerResponse(boolean success, Object data, Integer code,
        Integer errorCode, String message, Integer total, Integer page)
    {
        super();
        this.success = success;
        this.data = data;
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
        this.total = total;
        this.page = page;

        this.extraData = new HashMap<String, Object>();
    }

    public static ServerResponse createAnswerForDataTable(Collection data,
        Integer total, Integer page, String verificationString)
        throws Exception
    {
        ServerResponse answer =
            new ServerResponse(true, data, -1, -1, NULL_STRING, total, page);

        answer.setiTotalDisplayRecords(total);
        answer.setiTotalRecords(total);
        answer.setVerificationString(verificationString);

        return answer;
    }

    public static ServerResponse createSuccessGridAnswer(Collection data,
        Integer total, Integer page)
    {
        ServerResponse answer = null;
        answer =
            new ServerResponse(true, data, -1, -1, NULL_STRING, total, page);
        return answer;
    }

    public static ServerResponse createFailureResponse(Integer code,
        Integer errorCode, String message)
    {
        ServerResponse serverResponse = null;

        serverResponse =
            new ServerResponse(
                false,
                NULL_STRING,
                code,
                errorCode,
                message,
                -1,
                -1);

        return serverResponse;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public Object getData()
    {
        return data;
    }

    public Integer getCode()
    {
        return code;
    }

    public Integer getErrorCode()
    {
        return errorCode;
    }

    public String getMessage()
    {
        return message;
    }

    public Integer getTotal()
    {
        return total;
    }

    public Integer getPage()
    {
        return page;
    }

    public String getVerificationString()
    {
        return verificationString;
    }

    public void setVerificationString(String verificationString)
    {
        this.verificationString = verificationString;
    }

    public Integer getiTotalRecords()
    {
        return iTotalRecords;
    }

    public void setiTotalRecords(Integer iTotalRecords)
    {
        this.iTotalRecords = iTotalRecords;
    }

    public Integer getiTotalDisplayRecords()
    {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(Integer iTotalDisplayRecordS)
    {
        this.iTotalDisplayRecords = iTotalDisplayRecordS;
    }

    public void setExtraData(Object extraData)
    {
        this.extraData = extraData;
    }

    public Object getExtraData()
    {
        return this.extraData;
    }

    public void addToExtraData(String name, Object value)
    {
        ((HashMap<String, Object>) this.extraData).put(name, value);
    }

    public String toString()
    {
        ToStringHelper helper = Objects.toStringHelper(getClass());
        String extraDataString = this.extraData.toString();

        return Objects
            .toStringHelper(getClass())
            .add("success", success)
            .add("data", data.toString())
            .add("code", code.toString())
            .add("errorCode", errorCode)
            .add("message", message)
            .add("total", total)
            .add("page", page)
            .add("extraData", extraDataString)
            .toString();
    }

}
