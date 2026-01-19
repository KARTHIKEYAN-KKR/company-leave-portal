# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the WAR file
RUN mvn clean package -DskipTests

# Runtime Stage
FROM tomcat:10.1-jdk17-temurin-jammy
# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy the built WAR to Tomcat's webapps folder as ROOT.war
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
