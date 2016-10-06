To initialize the database, do something like this:
% sudo -u postgres -i
postgres% createdb monopoly
postgres% psql monopoly < service/database/monopoly.sql
postgres% exit

To start this REST service from the current command line, run these commands at the root of the Intellij project root.
- On Windows
"C:\Program Files\PostgreSQL\9.4\bin\postgres" -D "C:\Program Files\PostgreSQL\9.4\data"
"C:\Program Files\Java\jdk1.7.0_79\bin\java" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.7.0_79\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\jce.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\jfxrt.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\resources.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.7.0_79\jre\lib\ext\zipfs.jar;D:\projects\cs262\cs262-applications\Lab09\out\production\Lab09;D:\projects\cs262\cs262-applications\Lab09\lib\asm-3.3.1.jar;D:\projects\cs262\cs262-applications\Lab09\lib\jersey-client-1.12.jar;D:\projects\cs262\cs262-applications\Lab09\lib\jersey-grizzly-1.12.jar;D:\projects\cs262\cs262-applications\Lab09\lib\jersey-multipart-1.12.jar;D:\projects\cs262\cs262-applications\Lab09\lib\jersey-core-1.12.jar;D:\projects\cs262\cs262-applications\Lab09\lib\jersey-bundle-1.12.jar;D:\projects\cs262\cs262-applications\Lab09\lib\gson-2.6.2.jar;D:\projects\cs262\cs262-applications\Lab09\lib\postgresql-9.4-1201.jdbc4.jar;D:\projects\cs262\cs262-applications\Lab09\lib\javax.ws.rs-api-2.0.jar;C:\Program Files (x86)\JetBrains\IntelliJ IDEA 2016.1.2\lib\idea_rt.jar" edu.calvin.cs262.MonopolyResource
- On Linux
./startService.sh

To start this REST service as a system service, use either of these methods.

Method 1 – systemctl:
In /var/cs262/Lab09/src/systemctl/ :
lab09.service – systemctl service file; it is copied in /lib/systemd/system/ (and ‘init q’ is run to read it in).   This enables systemctl to know what to do for the ‘lab09’ service.  The “startExec” line in here is what launches our server, by firing up screen with the “startService.sh” script.
startService.sh – your basic start script, modified slightly to correctly change directories on launch and create pid files.
stopService.sh – a basic stop script.  It reads in the pidfile and kills all child processes of this pid.  Cleans up screen afterwards.

Usage (as your non-privileged user):
Start:  sudo systemctl start lab09
Stop:  sudo systemctl stop lab09
Status:  sudu systemctl status lab09

Method 2 – manual:
In /var/cs262/Lab09/src/manual/ :
start.sh – basic shell script that fires up screen with the true start script start-java.sh
start-java.sh – your basic start script, modified slightly to correctly change directories on launch and create pid files.
stop.sh – a basic stop script.  It reads in the pidfile and kills all child processes of this pid.  Cleans up screen afterwards.

Usage (as your non-privileged user):
Start:  ./start.sh
Stop:  ./stop.sh
Status:  ps –ef | grep <username> | grep `cat <pidfile>`

