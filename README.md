# Photopile

> Your photos, your way.

## Building

    ./gradlew clean build
    
## Database Initialization

> TBD... create the 'photopile' database in PostgreSql and configured the user...
    
## Development Run

    ./gradlew bootRun

## Remote shell

Any user with the ADMIN role can access the remote administration shell using:

    ssh -p 2000 USERNAME@HOSTNAME
    
The remote shell provides various application administration functions.

### User Management

The server is initialized with a default ADMIN user with "admin" as both the username and password. Your should at least change the password after the
server is installed. In the remote shell, run:

    user update -p <YOUR_PASSWORD> 1
    
to update the password for the 'admin' user.

You can also list the configured users, and add, update or delete existing users. Be careful!
