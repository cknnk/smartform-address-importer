package cz.smartform.addressimporter;

import cz.smartform.addressimporter.service.DataImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AddressImporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AddressImporterApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(DataImportService dataImportService) {
		return args -> dataImportService.importData();
	}
}