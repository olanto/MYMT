#!/bin/bash

sudo /etc/init.d/tomcat7 restart

cd /home/olanto/MYMT/dist


sudo java -Xmx2000m -Djava.rmi.server.codebase="file:///home/olanto/MYMT/dist/myMT.jar" -Djava.security.policy="file:///home/olanto/MYMT/config/rmi.policy"  -classpath "./myMT.jar" org.olanto.smt.master.DeployServers


