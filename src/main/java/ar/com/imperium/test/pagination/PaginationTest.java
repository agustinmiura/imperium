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
package ar.com.imperium.test.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.imperium.common.PaginationHelper;

/**
 * @author user
 * 
 */
public class PaginationTest
{
    public static final Logger logger = LoggerFactory
        .getLogger(PaginationTest.class);

    public static void main(String[] args)
    {
        try {
            // testPaginationHelper();
            // testPaginationHelper();
            testPageFromOffset();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void testPageFromOffset() throws Exception
    {
        PaginationHelper helper = new PaginationHelper();
        Integer total = 500;
        Integer pageSize = 10;
        Integer[] pagesArray =
            new Integer[] {
                    0,
                    1,
                    2,
                    3,
                    11,
                    12,
                    13,
                    14,
                    15,
                    16,
                    17,
                    18,
                    19,
                    20,
                    21 };

        Integer eachOffset;
        Integer page;
        for (int i = 0; i < 52; i++) {
            eachOffset = i;
            page = helper.getPageFromOffset(eachOffset, pageSize, total);
        }

    }

    public static void testCeil()
    {
        System.out.println("Ceil of 0.5:" + Math.ceil(0.5));
        System.out.println("Ceil of 0.000002:" + Math.ceil(0.000002));
        System.out.println("Ceil of 1:" + Math.ceil(1.0));
        System.out.println("Ceil if 1.2:" + Math.ceil(1.2));
        System.out.println("Ceil of 1.5:" + Math.ceil(1.5));
        System.out.println("Ceil of 1.7:" + Math.ceil(1.7));
    }

    public static void testPaginationHelper() throws Exception
    {
        PaginationHelper helper = new PaginationHelper();

        Integer[] pages = new Integer[] { 1, 2, 4, 5, 6, 7, 8, 10, 11 };

        Integer min = 1;
        Integer max = 25;

        Integer recordQty = 603;
        Integer pageSize = 25;
        Integer pageQty = helper.getPageQty(pageSize, recordQty);

        System.out.println("Page qty:" + pageQty);

        Integer pageToRequest = null;
        for (int i = min; i <= max; i++) {
            pageToRequest = helper.getPageToUseWithIndexChanged(i, pageQty);
            System.out.println("The input is :" + i);
            System.out.println("The page to request is :" + pageToRequest);
        }

        Integer[] invalidValues = new Integer[] { -1, -2, -3, 66, 25, 26 };

        for (Integer toUse : invalidValues) {
            System.out.println("The index to use is :"
                + helper.getPageToUseWithIndexChanged(toUse, pageQty));
        }
    }

}
