FROM tomcat:8
COPY build/libs/avaxo-client.war /usr/local/tomcat/webapps/ROOT.war
