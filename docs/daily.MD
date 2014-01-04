How to work every day with the application:

1_Add the vFabric tc server and start the server.
2_Set the eclipse to "never build automatically".
3_Set the server to:
Publishing:Automatically publish after a build event.
Application reload behaviour: Enable JMX-based reloading.
4_Setup Eclipse :
_1 windows->preferences->General->Startup and shutdown ->Disable all.
_2 Disable the spring dashboard and remove all the urls.
_3 Editors->Check spelling ->Disable check spelling.
_4 Disable the antivirus software for the eclipse folder and the project folder.
_5 Windows ->preferences->validation ->Leave only the validations for 
build process and remove the validations for the xml's and the javascripts.
_6 Use the sun jdk.
_7 Set the logging level to FATAL.

Setup chrome:

1_Remove all the extensions.
2_Install one tab extension.

So :
Each time you change a static resource that change is reflected in the 
webapplication.

And when you change a java class then you have to do a build of the application.

Explanation:

Once the server has started you only have to reload the application and 
not restart the whole server .

