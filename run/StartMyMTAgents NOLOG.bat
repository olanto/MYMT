call  C:\MYMT\run\SetFileName.bat

cd "C:\MYMT"
net stop tomcat7
net start tomcat7
java -Dfile.encoding=UTF-8 -Xmx1024m -Djava.rmi.server.codebase="file:///C:/MYMT/dist/myMT.jar" -Djava.security.policy="file:///C:/MYMT/rmi.policy"  -classpath "./dist/myMT.jar" org.olanto.smt.master.DeployServers
pause