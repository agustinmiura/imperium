/**
 * 
 */
package ar.com.imperium.dbutils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ar.com.imperium.common.security.IRandomService;
import ar.com.imperium.common.security.RandomServiceImpl;
import ar.com.imperium.domain.Application;
import ar.com.imperium.domain.Permission;
import ar.com.imperium.domain.Role;
import ar.com.imperium.domain.Subject;
import ar.com.imperium.domain.User;
import ar.com.imperium.domain.UserType;
import ar.com.imperium.service.interfaces.IApplicationService;
import ar.com.imperium.service.interfaces.IPermissionService;
import ar.com.imperium.service.interfaces.IRoleService;
import ar.com.imperium.service.interfaces.ISubjectService;
import ar.com.imperium.service.interfaces.IUserService;

/**
 * @author user
 * 
 */
public class SampleDbCreator
{
    private static final Logger logger = LoggerFactory
        .getLogger(SampleDbCreator.class);

    private static final Random random = new Random();

    private static final SecureRandom secureRandom = new SecureRandom();

    public static Integer getRandom(Integer max)
    {
        return random.nextInt(max);
    }

    public static String getRandomString()
    {
        String answer = new BigInteger(130, random).toString(32);
        return answer.substring(0, 7);
    }

    public static void main(String[] args)
    {
        try {

            Integer applicationQty = 10;
            Integer roleQty = 1;
            Integer subjectQty = 1;
            Integer permissionQty = 1;
            Integer userQty = 1;

            SampleDbCreator creator = null;
            creator =
                new SampleDbCreator(
                    applicationQty,
                    roleQty,
                    subjectQty,
                    permissionQty,
                    userQty);

            creator.create();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Integer applicationQty;
    private Integer roleQty;
    private Integer subjectQty;
    private Integer permissionQty;
    private Integer userQty;

    public SampleDbCreator(Integer applicationQty, Integer roleQty,
        Integer subjectQty, Integer permissionQty, Integer userQty)
    {
        this.applicationQty = applicationQty;
        this.roleQty = roleQty;
        this.subjectQty = subjectQty;
        this.permissionQty = permissionQty;
        this.userQty = userQty;
    }

    public SampleDbCreator()
    {
        this(100, 1, 1, 1, 1);
    }

    private GenericApplicationContext bootstrapContext()
    {
        GenericXmlApplicationContext context =
            new GenericXmlApplicationContext();
        // context.load("classpath:jpa-app-context.xml");
        context.load("classpath:jpa-app-context.xml");
        context.refresh();
        return context;
    }

    private void addPermissionsToRole(List<Application> appList,
        GenericXmlApplicationContext context) throws Exception
    {
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IPermissionService permissionService =
            (IPermissionService) context.getBean("jpaComplexPermissionService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        List<Role> roleList;
        List<Permission> permissionList;
        for (Application eachApplication : appList) {

            permissionList =
                permissionService.findAllForApplication(
                    eachApplication,
                    0,
                    1000,
                    "name");

            roleList =
                roleService.findAllForApplication(
                    eachApplication,
                    0,
                    1000,
                    "name");

            for (Role eachRole : roleList) {
                roleService.assignPermissions(eachRole, permissionList);
            }
        }

    }

    private List<Subject> generateSubjects(Integer start, Integer end,
        String prefix, String suffix)
    {
        List<Subject> answer = new ArrayList<Subject>();

        String name;
        Subject subject;
        String random;
        for (int i = 0; i < end; i++) {
            random = SampleDbCreator.getRandomString();
            name = prefix + "Subject" + suffix + "" + i + "_" + random;
            subject = new Subject(name);
            answer.add(subject);
        }

        return answer;
    }

    private void addSubjectsToRole(List<Application> appList,
        GenericXmlApplicationContext context) throws Exception
    {
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IPermissionService permissionService =
            (IPermissionService) context.getBean("jpaComplexPermissionService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        List<Role> roleList = new ArrayList<Role>();
        List<Subject> subjectList = new ArrayList<Subject>();
        Application fullApplication;
        for (Application eachApplication : appList) {

            fullApplication =
                service.findOneWithDetailById(eachApplication.getId());

            subjectList =
                subjectService.findAllForApplication(
                    fullApplication,
                    0,
                    900,
                    "name");

            roleList =
                roleService.findAllForApplication(
                    fullApplication,
                    0,
                    900,
                    "name");

            for (Role eachRole : roleList) {
                roleService.addToRole(eachRole, subjectList);
            }
        }
    }

    private void createSubjectsForApplications(List<Application> appList,
        GenericXmlApplicationContext context) throws Exception
    {
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IPermissionService permissionService =
            (IPermissionService) context.getBean("jpaComplexPermissionService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        ISubjectService subjectService =
            (ISubjectService) context.getBean("jpaComplexSubjectService");

        List<Subject> managedSubjectList;
        List<Subject> subjectList;
        String name;
        Application fullApplication;
        for (Application eachApplication : appList) {

            fullApplication =
                service.findOneWithDetailById(eachApplication.getId());

            name = fullApplication.getName();

            subjectList = this.generateSubjects(0, subjectQty, "", name);

            managedSubjectList = subjectService.create(subjectList);

            subjectService
                .addToApplication(fullApplication, managedSubjectList);
        }
    }

    private List<Permission> generatePermissions(Integer qty)
    {
        List<Permission> answer = new ArrayList<Permission>();

        String[] actionNames =
            {
                    "admin",
                    "read",
                    "print",
                    "create",
                    "update",
                    "delete",
                    "remove",
                    "config",
                    "setup",
                    "deploy",
                    "cancel",
                    "book" };

        Integer actionQty = actionNames.length;

        String[] resourceNames =
            {
                    "spreadSheet",
                    "graphics",
                    "documentProperties",
                    "document",
                    "watermark",
                    "row",
                    "presentation",
                    "personalInformation",
                    "images",
                    "sounds",
                    "videos",
                    "youtubevideos",
                    "interactive_videos" };

        Integer resourceQty = resourceNames.length;

        Permission permission;
        String resource;
        String action;
        Integer actionIndex;
        Integer resourceIndex;
        String randomString;
        for (int i = 0; i < qty; i++) {
            randomString = SampleDbCreator.getRandomString();

            actionIndex = getRandom(actionQty);
            resourceIndex = getRandom(resourceQty);

            action = actionNames[actionIndex];
            resource = resourceNames[resourceIndex];

            action = action + "_" + i;
            resource = resource + "_" + randomString + "_" + i;

            permission = new Permission(resource, action);
            answer.add(permission);
        }

        return answer;
    }

    private List<Role> generateRoles(Integer qty)
    {
        List<Role> answer = new ArrayList<Role>();

        String[] roleNames =
            { "CreateSheetRole", "UpdateSheetRole", "RemoveSheetRole" };

        String namePrefix = "RoleName_";
        String descriptionPrefix = "RoleDescription_";

        String roleBaseName;
        String roleName;
        String description;
        Role role;
        String random;

        Integer j = 0;
        for (int i = 0; i < qty; i++) {
            random = SampleDbCreator.getRandomString();
            j = (j + 1) % (3);
            roleBaseName = roleNames[j];
            roleName = namePrefix + roleBaseName + i + "_" + random;
            description = descriptionPrefix + roleBaseName + i + "_" + random;

            role = new Role(roleName, description);
            answer.add(role);
        }

        return answer;
    }

    private void createPermissionsForApplicationAndNotRole(
        List<Application> appList, GenericXmlApplicationContext context)
        throws Exception
    {
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IPermissionService permissionService =
            (IPermissionService) context.getBean("jpaComplexPermissionService");

        List<Permission> permissionList;
        for (Application eachApplication : appList) {
            permissionList = this.generatePermissions(this.permissionQty);
            permissionList = permissionService.create(permissionList);
            service.addPermissions(eachApplication, permissionList);
        }
    }

    private void createRolesForApplication(List<Application> appList,
        GenericXmlApplicationContext context) throws Exception
    {
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        List<Role> roleList;
        for (Application eachApplication : appList) {
            roleList = this.generateRoles(roleQty);
            roleService.create(roleList);
            service.addRoles(eachApplication, roleList);
        }
    }

    private List<User> generateUsers() throws Exception
    {
        List<User> userList = new ArrayList<User>();

        Long currentTime = System.currentTimeMillis();

        User user = null;
        String baseName = "User_" + currentTime + "_";
        String name;
        for (int i = 0; i < this.userQty; i++) {
            name = baseName + i;
            user = new User(name, "password_" + name, UserType.USER.getType());
            userList.add(user);
        }

        return userList;
    }

    private void createUsers(GenericXmlApplicationContext context)
        throws Exception
    {
        IUserService service =
            context.getBean("jpaUserService", IUserService.class);
        List<User> userList = generateUsers();
        service.create(userList);
    }

    public void create() throws Exception
    {
        GenericXmlApplicationContext context =
            (GenericXmlApplicationContext) bootstrapContext();
        IApplicationService service =
            context.getBean(
                "jpaComplexApplicationService",
                IApplicationService.class);

        IPermissionService permissionService =
            (IPermissionService) context.getBean("jpaComplexPermissionService");

        IRoleService roleService =
            (IRoleService) context.getBean("jpaComplexRoleService");

        int start = 0;
        int max = this.applicationQty;

        String baseName = "Excell version:";
        String description = "Excell application version number:";

        Application eachApplication = null;
        List<Role> roleList = new ArrayList<Role>();
        List<Permission> permissionList = new ArrayList<Permission>();
        List<Application> applicationList = new ArrayList<Application>();
        String tempName;
        Application tempApp;
        String random;
        IRandomService randomService = new RandomServiceImpl();
        for (int i = start; i < max; i++) {
            random = SampleDbCreator.getRandomString();
            tempName = baseName + i + "_" + random;
            eachApplication =
                new Application(
                    tempName,
                    description + i,
                    randomService.generateRandomString(64));

            roleList = this.generateRoles(roleQty);
            for (Role tempRole : roleList) {
                eachApplication.addRoleWithValidation(tempRole);
            }

            permissionList = this.generatePermissions(permissionQty);
            boolean check;
            for (Permission tempPermission : permissionList) {
                check = eachApplication.addPermission(tempPermission);
            }

            tempApp = service.create(eachApplication);
            applicationList.add(tempApp);
        }

        // add the created permissions to all the roles
        addPermissionsToRole(applicationList, context);

        // create subjects and add them to an application
        createSubjectsForApplications(applicationList, context);
        addSubjectsToRole(applicationList, context);

        /**
         * Create new permissions to an application and not assign them in a
         * role
         */
        createPermissionsForApplicationAndNotRole(applicationList, context);

        /**
         * Create new roles and add them only to the application
         */
        createRolesForApplication(applicationList, context);

        /**
         * Create users for the web application
         */
        createUsers(context);

    }
}
