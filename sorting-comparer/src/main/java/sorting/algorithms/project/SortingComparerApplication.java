package sorting.algorithms.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Spring Boot application.
 * This class configures and launches the application context.
 */
@SpringBootApplication // Enables auto-configuration, component scanning, etc.
public class SortingComparerApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Launches the Spring application context.
		SpringApplication.run(SortingComparerApplication.class, args);
	}

}