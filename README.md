# Aeon Bank Backend Assessment
Check Backend Engineer Technical Assessment.pdf for the assessment detail

## Tech Stack
1. Java17
2. Spring Boot 3.2.5
3. Mysql 5.7
4. Maven

## Dependency
1. Java17
2. Mysql 5.7


### Database
Database used is Mysql.
Reason behind choosing mysql
1. The RDBMS that I most familiar of
2. The most popular RDBMS database

## Config
Do update application.properties for db config and server port

### Build project
Need to be at root directory. Make sure test.properties config is correct
Need to create the database first. Can left it empty as app will update the db with the schema as app start-up
```
./mvnw clean install
```

### Run the spring boot app
Need to create the database first. Can left it empty as app will update the db with the schema as app start-up
```
./mvnw spring-boot:run
```

### Documentation
Change domain or port as per needed

Swagger
```
http://localhost:8080/swagger-ui/index.html#/
```

Open API Doc in JSON
```
http://localhost:8080/api-docs
```

## Assumption
Assumption made on top of existing requirement
1. If there is already existing borrower record with same request name and email. API will throw non 200
   1. Reason is we dont want to have duplicate user. Will introduce complexity to the system
2. Added pagination for get list of all book API
   1. Best practice when deal with list of object. To avoid server overload
3. Adding book status enum into book entity
   1. For better clarity on the status of the book(borrowed or available)
4. Can return a book even book is not borrowed
   1. To handle scenario where the book borrow is not recorded into system
5. Cannot return or borrow book that is not exist in database
6. Added new entity called BookBorrower
   1. To record the trail of book being borrowed and returned
7. Can return a book without need to know the borrower


## Code structure
1. Code is grouped by feature, not layer
   1. Mean book is grouped under book package, borrower is grouped under borrower package
   2. Feature repository level, service level and controller level are all under same package
1. Test code is also grouped by feature. The test package mimicking the main package
   1. Mean book test class are all under book package
   2. All test like repository test, service test and controller test are all under same package given same feature

## Requirement Checklist
- [X] Use of Java 17 and Spring Boot framework
- [X] Use a package manager to manage project dependencies. (Maven)
- [X] Implement proper data validation and error handling.
- [X] Use a database to store borrower and book data. and Justify
- [X] Implement REST API endpoints for each action
- [X] Multiple books with the same ISBN number should be registered as books with different
  ids.
- [X] Ensure that no more than one member is borrowing the same book (same book id) at a
  time.
- [X] Provide clear documentation for how to use your API
- [X] Provide documentation of all your assumptions for any requirements that are not
  explicitly stated in this task
- [X] Include unit tests to verify the functionality of your code.

