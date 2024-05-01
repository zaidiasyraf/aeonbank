FROM maven:3.8.3-openjdk-17
WORKDIR /aeon-bank
COPY . .
RUN mvn clean install -Dmaven.test.skip
CMD mvn spring-boot:run