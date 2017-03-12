FROM openjdk:8-jre-alpine
ADD target/roulette.jar /
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /roulette.jar" ]
