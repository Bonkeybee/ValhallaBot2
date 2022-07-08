FROM openjdk:17-alpine
RUN apk add --no-cache libstdc++
COPY build/libs/valhallabot2-*-all.jar valhallabot2.jar
EXPOSE 8080
CMD java -noverify ${JAVA_OPTS} -jar valhallabot2.jar
