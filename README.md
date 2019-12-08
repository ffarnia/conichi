#Conichi challenge code utilized by Spring Boot Microservice

Application is written with below technologies and tools:
- Java 8
- Maven
- Spring boot 2.2
- IntelliJ IDEA


Base on emailed document this application is written by spring boot as a service and provides 3 rest webservices include:
- Getting current time: can be reached via /api/currentTime endpoint produce Json output and it is formatted.
- Convert currency from source to target currency for specified amount:can be accessible via /api/currency/convert/amount/{amount}/source-currency/{sourceCurrency}/target-currency/{targetCurrency} all required parameters should be passed in url and output will be converted total amount. it uses from public Api mentioned in document.Although there is a good endpoint for convert currency base on app requirement, i could not used it because it was not free only "live"" and "history" was on free plan of website and it has 250 times call limit.so i had to use live endpoint and calculate result myself. there is another important tip, in currency conversion test case expected total result and conversion rate base on time of this test may differ so if test fails don not worry just change expected result of converted.
- Lookup VAT number and validate it: can be reached via /api/lookupVat endpoint. vatCode should be passed by request body.

All details of services are documented via swagger and it is accessible via http://127.0.0.1:8081/swagger-ui.html

Application run on port 8081.
For all services test cases are provided.
Caching mechanism is enabled.
Dockerfile is provided under root folder and just need to build it via docker build command.

Completing this Technical test took about 1 day (i had some problem with connecting to public apis on the internet they are not accessible from Iran so i had to use vpn for accessing them).


