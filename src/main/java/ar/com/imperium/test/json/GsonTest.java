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
package ar.com.imperium.test.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ar.com.imperium.common.json.IJsonHelper;
import ar.com.imperium.common.json.ServerResponse;
import ar.com.imperium.common.json.gson.ApplicationTypeAdapterFactory;
import ar.com.imperium.common.json.gson.entitiyserializers.ApplicationSerializer;
import ar.com.imperium.common.json.gson.entitiyserializers.SubjectSerializer;
import ar.com.imperium.common.json.implementation.GsonImplementation;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.service.interfaces.IApplicationService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author user
 * 
 */
public class GsonTest
{
    public static final Logger logger = LoggerFactory.getLogger(GsonTest.class);

    public static void main(String[] args)
    {
        try {
            testCreateAnswerForDataTable();
            // generateAnswerForJQueryTablePlugin();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void testCreateAnswerForDataTable() throws Exception
    {
        logger.debug("Started the method :testCreateAnswerForDataTable");

        List<Integer> sampleList = new ArrayList<Integer>();
        sampleList.add(new Integer(1));
        sampleList.add(new Integer(2));

        ServerResponse serverResponse =
            ServerResponse.createAnswerForDataTable(
                sampleList,
                new Integer(10),
                new Integer(11),
                "verificationString");

        Map<String, Object> extraData = new HashMap<String, Object>();
        extraData.put("successSample", true);
        List<String> list = new ArrayList<String>();
        list.add("1,1");
        list.add("2,33");
        extraData.put("sampleList", list);

        serverResponse.setExtraData(extraData);

        IJsonHelper jsonHelper = new GsonImplementation();

        String answer = jsonHelper.toJson(serverResponse);

        logger.error("The json is :" + answer);

        logger.error("testCreateAnswerForDataTable");
    }

    private static void generateAnswerForJQueryTablePlugin() throws Exception
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        ApplicationSerializer appSerializer = new ApplicationSerializer();

        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Application.class, appSerializer);
        gsonBuilder.registerTypeAdapter(Subject.class, new SubjectSerializer());

        Gson gson = gsonBuilder.create();

        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);

        Map<String, Object> aMap = new HashMap<String, Object>();
        aMap.put("sEcho", 10);
        aMap.put("iTotalRecords", 100);
        aMap.put("iTotalDisplayRecords", 100);
        aMap.put("bbData", integerList);
        aMap.put("aaData", getSubjectList());

        String encoded = gson.toJson(aMap, aMap.getClass());

        System.out.println("Encoded:" + encoded);
    }

    private static List<Subject> getSubjectList()
    {
        List<Subject> answer = new ArrayList<Subject>();
        Subject eachSubject;
        for (int i = 0; i < 10; i++) {
            eachSubject = new Subject("name" + i);
            eachSubject.setId(new Long(i));
            answer.add(eachSubject);
        }
        return answer;
    }

    private static void testAnswer2() throws Exception
    {
        /**
         * Application part here
         */
        GenericApplicationContext context = bootstrapContext();
        IApplicationService applicationService =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        List<Application> appList =
            applicationService.findAllWithDetail(0, 2, "name");

        /**
         * Json part here
         */
        GsonBuilder gsonBuilder = new GsonBuilder();

        ApplicationSerializer appSerializer = new ApplicationSerializer();

        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Application.class, appSerializer);

        Gson gson = gsonBuilder.create();

        ServerResponse response =
            ServerResponse.createSuccessGridAnswer(appList, 123, 0);

        String asJson = gson.toJson(response);

    }

    private static void testAnswer() throws Exception
    {
        Gson gson = new Gson();

        IJsonHelper jsonHelper = new GsonImplementation();
        String answerTemplate = jsonHelper.getServerAnswerTemplate(true);

        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);

        Map<String, Object> aMap = new HashMap<String, Object>();
        aMap.put("total", 10);
        aMap.put("list", integerList);

        /*
         * String jsonList = gson.toJson(integerList, integerList.getClass());
         */
        String dataString = gson.toJson(aMap, aMap.getClass());

        answerTemplate = answerTemplate.replace("{toReplaceData}", dataString);

        logger.debug("The answer template is :" + answerTemplate);

    }

    private static void testMapSerialization() throws Exception
    {
        GsonBuilder gSonBuilder = new GsonBuilder();
        gSonBuilder.setPrettyPrinting();

        Map<String, Object> aMap = new HashMap<String, Object>();
        aMap.put("success", true);
        aMap.put("errorCode", "{errorCode}");
        aMap.put("data", "{data}");
        aMap.put("errorCode", "{errorCode}");
        aMap.put("message", "{errorMessage}");

        Gson gson = gSonBuilder.create();

        String jsonString = gson.toJson(aMap, aMap.getClass());

        logger.debug("The map is" + jsonString);

    }

    private static void appListSerializationWithRoles3() throws Exception
    {

        GenericApplicationContext context = bootstrapContext();
        IApplicationService applicationService =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        List<Application> appList =
            applicationService.findAllWithDetail(0, 2, "name");

        GsonBuilder gSonBuilder = new GsonBuilder();

        gSonBuilder.registerTypeAdapter(
            Application.class,
            new ApplicationSerializer());
        gSonBuilder.setPrettyPrinting();

        Gson gson = gSonBuilder.create();

        String asString = gson.toJson(appList);

    }

    private static void appSerializeWithRoles2() throws Exception
    {
        GenericApplicationContext context = bootstrapContext();
        IApplicationService applicationService =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        List<Application> appList =
            applicationService.findAllWithDetail(0, 2, "name");

        Application application = appList.get(0);

        GsonBuilder gSonBuilder = new GsonBuilder();
        gSonBuilder.registerTypeAdapter(
            Application.class,
            new ApplicationSerializer());
        Gson gson = gSonBuilder.create();

        String asString = gson.toJson(application);

        logger.debug("The app as json is :" + asString);
    }

    /**
     * test
     * 
     * @throws Exception
     */
    private static void appSerializeWithRoles() throws Exception
    {
        Application app = new Application("name", "description", "");
        Role role1 = new Role("admin", "Administrator");
        Role role2 = new Role("manager", "Manager");

        app.addRole(role1);
        app.addRole(role2);
        /*
         * Gson gson = new Gson(); String asJson = gson.toJson(gson);
         */
        GsonBuilder gSonBuilder = new GsonBuilder();
        gSonBuilder
            .registerTypeAdapterFactory(new ApplicationTypeAdapterFactory());

        Gson gson = gSonBuilder.create();
        String asString = gson.toJson(app);
    }

    /**
     * Functions
     * 
     * @throws Exception
     */
    private static void appSerializeTest() throws Exception
    {
        Application app = new Application("name", "description", "");

        Role role1 = new Role("admin", "Administrator");
        Role role2 = new Role("manager", "Manager");

        Gson gson = new Gson();
        String asJson = gson.toJson(app);

        logger.debug("The app as json" + asJson);
    }

    /**
     * Functions
     * 
     * @return
     */
    private static SampleObject createObject()
    {
        List<String> stringList = new ArrayList<String>();
        stringList.add("one");
        stringList.add("two");
        stringList.add("three");

        SampleObject sampleInstance =
            new SampleObject(true, "aDescription", stringList);

        return sampleInstance;
    }

    /**
     * Functions
     * 
     * @throws Exception
     */
    private static void serializeComposition() throws Exception
    {
        EmbeddedObject parent = new EmbeddedObject(new Integer(1));
        parent.setSampleObject(createObject());

        Gson gson = new Gson();
        String asJson = gson.toJson(parent);

        logger.debug("The json string is:" + asJson);
    }

    /**
     * Functions
     * 
     * @throws Exception
     */
    private static void readJsonTest() throws Exception
    {
        SampleObject sampleInstance = createObject();
        Gson gson = new Gson();
        String asString = gson.toJson(sampleInstance);

        SampleObject readFromString =
            gson.fromJson(asString, SampleObject.class);

        logger.debug("Read from string : " + readFromString);
    }

    /**
     * Functions
     * 
     * @throws Throwable
     */
    private static void serializeDataObject() throws Throwable
    {
        SampleObject sampleInstance = createObject();

        Gson gson = new Gson();
        String jsonString = gson.toJson(sampleInstance);

        logger.debug("The sample instance as json is :" + jsonString);
    }

    private static GenericApplicationContext bootstrapContext()
    {
        GenericXmlApplicationContext context =
            new GenericXmlApplicationContext();
        context.load("classpath:jpa-app-context.xml");
        context.refresh();
        return context;
    }

    private static void appTest() throws Exception
    {
        GenericApplicationContext context = bootstrapContext();
        IApplicationService applicationService =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        List<Application> appList =
            applicationService.findAllWithDetail(0, 2, "name");

        Application application = appList.get(0);

        Gson gson = new Gson();
        String appAsJson = gson.toJson(application);

        logger.debug("The application as json :" + appAsJson);

    }
}
