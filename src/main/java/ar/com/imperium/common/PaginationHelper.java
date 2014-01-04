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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("paginationHelper")
public class PaginationHelper
{
    public static final Logger logger = LoggerFactory
        .getLogger(PaginationHelper.class);

    public static void main(String[] args)
    {

        try {
            PaginationHelper helper = new PaginationHelper();
            System.out.println("The page is :"
                + helper.getOffsetFromPage(1, 25));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public Integer getPageQty(Integer pageSize, Long total)
    {
        Integer totalInteger = Integer.parseInt(total.toString());
        return this.getPageQty(pageSize, totalInteger);
    }

    public Integer getPageQty(Integer pageSize, Integer total)
    {
        Double doublePageQty = (double) (total * 1.0 / pageSize * 1.0);
        Integer pageQty = (int) Math.ceil(doublePageQty);

        return pageQty;
    }

    public Integer getPageToUse(Integer pageRequested, Integer pageQty)
        throws Exception
    {
        Integer answer = null;

        if ((pageRequested >= 0) && (pageRequested < pageQty)) {
            answer = pageRequested;
        } else if (pageRequested < 0) {
            answer = 0;
        } else {
            answer = pageQty - 1;
        }
        return answer;
    }

    /**
     * Get the index of page to request when we use a pagination plugin that
     * request from page 1 -> to max Page qty
     * 
     * @param pageRequested
     * @param pageNumberChanged
     * @return
     */
    public Integer getPageToUseWithIndexChanged(Integer pageRequested,
        Integer pageQty) throws Exception
    {
        return getPageToUse(pageRequested - 1, pageQty);
    }

    public Integer getPageFromOffset(Integer offset, Integer pageSize,
        Integer total) throws Exception
    {
        Integer answer = null;
        Integer pageQty =
            this.getPageQty(pageSize, Long.parseLong(total.toString()));

        Double page = (offset * 1.0) / (pageSize * 1.0);
        answer = (int) Math.ceil(page);
        return answer;
    }

    public Integer getPageFromOffset(Integer offset, Integer pageSize,
        Long total) throws Exception
    {
        Integer totalAsInteger = Integer.parseInt(total.toString());
        Integer answer = getPageFromOffset(offset, pageSize, totalAsInteger);
        return answer;
    }

    /**
     * Received pages from 1 -> to max and the page size to return the offset or
     * starting index
     * 
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    public Integer getOffsetFromPage(Integer page, Integer pageSize)
        throws Exception
    {
        Integer answer = null;
        Integer maxIndex = page * pageSize;
        answer = maxIndex - pageSize;
        return answer;
    }

}
