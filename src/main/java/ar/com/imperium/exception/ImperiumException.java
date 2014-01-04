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
package ar.com.imperium.exception;

import java.util.Map;

/**
 * @author user
 * 
 */
public class ImperiumException extends Exception
{
	protected Map<String, String> errorMap;

    protected boolean ajaxResponse = false;

    /**
     * 
     */
    private static final long serialVersionUID = -5564753084151310517L;

    public ImperiumException(String message)
    {
        super(message);
        this.ajaxResponse = false;
    }

    public ImperiumException(String message, boolean ajaxResponse)
    {
        super(message);
        this.ajaxResponse = ajaxResponse;
    }
    
    public ImperiumException(String message, boolean ajaxResponse, Map<String, String> errorMap) 
    {
    	super(message);
    	this.ajaxResponse = ajaxResponse;
    	this.errorMap = errorMap;
    }

    public void setAsAjaxResponse()
    {
        this.ajaxResponse = true;
    }

    public boolean isAjaxResponse()
    {
        return ajaxResponse;
    }

    public boolean hasErrorMap()
    {
    	return (errorMap!=null);
    }
    
    public Map<String, String> getErrorMap() 
    {
    	return errorMap;
    }
    
}
