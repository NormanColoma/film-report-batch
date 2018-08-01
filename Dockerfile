FROM maven:3.5.0-jdk-8-alpine

ADD . /usr/src/films
RUN cd /usr/src/films && mvn clean install spring-boot:repackage -DskipTests \
    && mv target/films-1.0.0.jar /films.jar \
    && mv credentials.json /credentials.json

RUN rm -rf /usr/src/films/
ENTRYPOINT exec java $JAVA_OPTS -jar /films.jar