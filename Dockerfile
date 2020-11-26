# Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk11

# Add Maintainer Info
LABEL maintainer="mail@odit.no"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port available to the world outside this container
EXPOSE 80

# The application's jar file
ARG JAR_FILE=target/EHFsok-API-1.0.jar

# Add the application's jar to the container
ADD ${JAR_FILE} EHFsok-API.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/EHFsok-API.jar"]

# Building command:
# docker build -t ehfsok-api .
