FROM openjdk:17
COPY build/libs/valhallabot2-*-all.jar valhallabot2.jar
EXPOSE 8080
CMD java -jar valhallabot2.jar
