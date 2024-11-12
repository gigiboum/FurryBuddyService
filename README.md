# FurryBuddyService

## Running this project

### Prepare the domain

1. Open the domain project (FurryBuddy)
2. Click on the Maven menu on the right.
3. Select **clean**, **compile**, **package**, and **install** under the Lifecycle menu and Run the Maven Build.
4. Make sure that build is successful

### Starting Payara server (in terminal)

1. Launch the Terminal (on Mac) or Command Line or PowerShell (on Windows).
2. Navigate to your Payara's home directory (using cd command)
3. Navigate to the bin directory by typing cd bin
4. Run the following command to start the Payara Server ```./asadmin start-domain --debug``` (or
   ```.\asadmin start-domain --debug``` on Windows)
5. The command start-domain should be executed successfully

### Deploying the service

1. Return to this project on IntelliJ (FurryBuddyService)
2. Make sure that Payara is configured
3. Run Payara (play button)

A web page should open if successfully deployed

#### Performing CRUD operations

Basic operations to interact with the application (Create, Read, Update, Delete)
using Postman 

| Operation | call method |
|-----------|-------------|
| Create    | POST        |
| Read      | GET         |
| Update    | POST        |
| Delete    | DELETE      |


### Stopping the Payara server
**NOTE** : When you are done using / interacting with the application, remember to stop the server

1. Stop Payara in IntelliJ 
2. Back in terminal, run this command ```./asadmin stop-domain``` (```.\asadmin stop-domain``` on Windows)