FROM tomcat:8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY build/libs/avaxo-client.war /usr/local/tomcat/webapps/ROOT.war
VOLUME /usr/local/tomcat/logs