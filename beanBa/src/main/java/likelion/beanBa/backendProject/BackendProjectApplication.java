package likelion.beanBa.backendProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendProjectApplication.class, args);
	}

}
