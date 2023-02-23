package fr.fne.arangodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"fr.fne.arangodb"})
public class TestArangodbApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestArangodbApplication.class, args);
	}
}
