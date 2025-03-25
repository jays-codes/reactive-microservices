package jayslabs.demo03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@SpringBootApplication(scanBasePackages = {"jayslabs.demo${sec}"})
@EnableR2dbcRepositories(basePackages = {"jayslabs.demo${sec}"})
public class WebfluxSandboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxSandboxApplication.class, args);
	}

}
