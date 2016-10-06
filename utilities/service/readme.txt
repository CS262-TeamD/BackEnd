Two approaches to running the Grizzley database as a system service.

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
