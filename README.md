# spring_security_implentition
I have implemented the basic Auth and with user data saved in Microsoft SQL Server database.
15-Aug-2023
added google oauth2.0 in the login page.
Modified the login for a form based login along with http basics.
Also added a google oauth 2.0
steps to add oauth 2.0
1. add dependency in pom.xml 
  <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-oauth2-client</artifactId>
		</dependency>
2. in the security filter chain add the below auth method
   .oauth2Login(withDefaults())
3. add the client-id, client-secret, redirect-uri to the application properties.
    we have to create this from all the open auth provider, like for google I have created it from https://console.cloud.google.com/
