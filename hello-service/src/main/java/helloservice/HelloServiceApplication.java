package helloservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class HelloServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloServiceApplication.class, args);
	}

	@FeignClient(name = "greeting-service")
	public interface GreetingServiceProxy {
		@GetMapping("/{name}")
		String getGreeting(@PathVariable("name") String name);
	}

	@RestController
	public static class HelloController {
		@Autowired
		private GreetingServiceProxy proxy;
		@GetMapping("/{name}")
		public ResponseEntity<String> getGreeting(@PathVariable("name") String name) {
			return ResponseEntity.ok(proxy.getGreeting(name));
		}
	}

}
