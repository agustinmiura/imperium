To start the tc server instance run
the following command:

user@netherworld:~/software/springsource/vfabric-tc-server-developer-2.7.2.RELEASE$
 ./tcruntime-ctl.sh tc-server-instance

Use the command:
"./tcruntime-ctl.sh tc-server-instance"

Install the tc-server-vmware instance:

1_Download the server from vmWare

2_

The move the directory and execute:

./tcruntime-instance.sh create --instance-directory /home/user/software/vfabric-server-instances myInstance

To install an instance called "myInstance" in the directory
"myInstance" located in
"/home/user/software/vfabric-server-instances"


3_
Start the instance

./tcruntime-ctl.sh myInstance start -n /home/user/software/vfabric-server-instances

to run the instance of the server called "myInstance" located in
/home/user/software/vfabric-server-instances

4_Inside the directory

myInstance/conf/catalina.properties

Add the following content:

base.jmx.port=49000
bio.http.port=50000
bio.https.port=50001

So the server starts in the port 50001

5_Add to tomcat-users.xml the following content:

<?xml version="1.0"?>
<tomcat-users>
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="manager-jmx"/>
<user username="admin" password="admin" roles="manager-gui,manager-script,manager-jmx"/>
</tomcat-users>

So the user "admin" with password "admin" cant access the
manager Ui

6_Go to the url "http://localhost:50000/manager"
and copy the war of the webapplication inside the
webapps directory.

7_
The copy the file spring-instrument-tomcat-*.jar
inside the directories of the tomcat-6 and tomcat-7 lib .
In our example copy the file to :
"/home/user/software/vfabric-tc-server-developer-2.8.1.RELEASE/X"
and
"/home/user/software/vfabric-tc-server-developer-2.8.1.RELEASE/Y"

where X is "tomcat-6.0.36.A.RELEASE/lib"
and Y is "tomcat-7.0.36.A.RELEASE/lib"

8_Set the logging levels to ERROR so the server starts faster.

8_Restart everything.



Troubleshooting:

1_When i try to deploy the webapp i see:

Caused by: java.lang.ClassNotFoundException: org.springframework.instrument.classloading.tomcat.TomcatInstrumentableClassLoader

