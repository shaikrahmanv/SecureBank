package Jar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "Jar", "com.banking" })
public class BankingWebApplication1Application {

	public static void main(String[] args) {
		SpringApplication.run(BankingWebApplication1Application.class, args);
	}
}