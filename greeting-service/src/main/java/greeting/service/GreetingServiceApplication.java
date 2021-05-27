package greeting.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class GreetingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(GreetingServiceApplication.class, args);
	}
	@RestController
	public static class GreetingController {
		@GetMapping("/{name}")
		public ResponseEntity<String> getGreeting(@PathVariable("name") String name) {
			String message = String.format("Greetings %s , you are welcome to the GreetingService.", name);
			return ResponseEntity.ok(message);
		}
	}
}
