This application will run on cs262.cs.calvin.edu, which is configured to run your information servers. All teams will use this same Postgres installation. You can access the server from the Lab using Linux ssh and scp.

- To run this application as a RESTful web service do the following.
    Execute start.sh.
  This will start your service on the machine. It will run until you execute the stop.sh script.
  Note: a single user can only run one instance of this startup script with the same Name.

Webservice responds to URLs:

    http://cs262.cs.calvin.edu:8084/hello
  

 In the future it will respond to URLs like:

    http://cs262.cs.calvin.edu:8084/Tasks

