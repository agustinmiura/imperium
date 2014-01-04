Testing plan for Imperium
==================

##1 Test login

1.   User enters invalid password or user and sees the error
2.  User tries to login with the form incomplete and received the error
   messages.
3.  User logins with valid credentials and is redirected to the applications 
   list page
   
##2 Applications test

1. User enters url to see the applications list and sees the list.
2. Add an user : 
 *  User clicks in the add button.
 * User enters valid information in the form
 * User is redirected to the application list
3. Invalid information:
  * User enters invalid information in the login .
  * Click in the add button.
  * User sees the errors.
4. Search for an application :
 * User enters the name of an applicaion in the input.
 * User clicks and sees the applications.
5. Search for non existant applications :
 * With the name of a non existant application the user 
  clicks on search.
  * User sees an empty list.
##3 Update basic application information.
1.  Update application with valid information : 
 * User clicks the update icon.
 * User enters valid update information.
 * User is redirected to the application list.
2. Update basic application information with invalid information:
 * User enters invalid information in the application edition form.
 * User sees the error messages.
3. Cancel the update
 * User enters the update basic information form.
 * User clicks on the cancel button.
 * User is redirected to the application list page.
##4 Remove an application.
1.  Valid remove:
  * User clicks the delete icon.
  *  User accepts the dialog.
  *  User is redirected to the list.
2. Cancel the remove:
 * User clicks the delete icon.
 * User cancels the removal of the application.
 * User sees the list.
##5 See the api key
 * User clicks in the see the api icon.
 * User sees the application key
##6 Reset the application key
1. Reset the application key
 * User clicks in the reset key operation.
 * User sees the new key.
##7 Change user password
1. Change password
 * User clicks in the change password icon.
 * User enters valid information
 * User is redirected to the application list
2. Change password with invalid information:
 * User selects change password option
 * User enters invalid information .
 * User sees the error.
3. Cancel the password change.
 * User clicks the change password icon.
 * User clicks on cancel and is redirected to the application list page.
##8 Logout
 1. Do logout:
 * User selects the logout option.
 * User is redirected to the login form.
##9 Create a role for the application.
 1.Create a role for the application:
  * User enters valid information in the role form.
  * User is redirected to the role list for that application.

