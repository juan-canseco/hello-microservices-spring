# hello-microservices-spring


Simple proyecto que hice solo para enseñarme las bases de los microservicios.

El proyecto consiste de 2 microservicios uno que se llama **hello-service** el cual se comunica con **greeting-service** para obtener un saludo.

# Arquitectura 
![alt text](https://github.com/juan-canseco/hello-microservices-spring/blob/main/img/arquitectura-hola-microservicios.png "Arquitectura del Proecto")


## Registration Service 
Primero hay que escribir la anotación **@EnableEurekaServer** en el archivo **RegistrationServerApplication**.
```java
@SpringBootApplication
@EnableEurekaClient 
public class RegistrationServerApplication {
  // Code Here
}
```

**application.properties**
```java
# Service Configuration 
spring.application.name=registration-server
server.port=8761

# Eureka Client
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
## Discovery Server  
El primer paso es añadir la anotación **@EnableZuulProxy** y **@EnableEurekaClient** en el archivo DiscoveryServiceApplication.
```java
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class DiscoveryServiceApplication {
  // Code
}
```
**application.properties**
```java
# Application Configuration
spring.application.name=discovery-service
server.port=8080

# Registration Service
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Discovery Service
zuul.routes.hello.service-id=hello-service
zuul.routes.hello.path=/api/hello/**
zuul.routes.greeting.service-id=greeting-service
zuul.routes.greeting.path=/api/greeting/**

```
## Greeting Service 
En la clase aplicación del proyecto de spring agregar la anotación **@EnableEurekaClient**
```java
@EnableEurekaClient
@SpringBootApplication
public class GreetingServiceApplication  {
  // App Code 
}
```
**GreetingController**
```java
@RestController
public static class GreetingController {
 @GetMapping("/{name}")
 public ResponseEntity<String> getGreeting(@PathVariable("name") String name) {
	 String message = String.format("Greetings %s , you are welcome to the GreetingService.", name);
	 return ResponseEntity.ok(message);
 }
}
```
**application.properties**
```java
# App Config
spring.application.name=greeting-service
server.port=${PORT:0}
# Registration Service Connection
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```
## Hello Service 
El servicio hola se comunica con el servicio saludo por medio de feign-client, para habilitarlo es necesario agregar la anotación @EnableFeignClients, y @EnableEurekaClient para el servidor de registro.
```java
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class HelloServiceApplication {
  // App Code
}
```
**Feign Client**
```java
@FeignClient(name = "greeting-service")
public interface GreetingServiceProxy {
  @GetMapping("/{name}")
	String getGreeting(@PathVariable("name") String name);
}

```
**HelloController**
```java
@RestController
public static class HelloController {
	@Autowired
	private GreetingServiceProxy proxy;
	@GetMapping("/{name}")
	public ResponseEntity<String> getGreeting(@PathVariable("name") String name) {
		return ResponseEntity.ok(proxy.getGreeting(name));
	}
 }
```
**application.properties**
```java
# App Configuration
spring.application.name=hello-service
server.port=${PORT:0}

#Eureka Server Connection
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

```
