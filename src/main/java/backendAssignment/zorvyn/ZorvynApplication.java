package backendAssignment.zorvyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ZorvynApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZorvynApplication.class, args);
	}

}
