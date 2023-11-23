package com.LP2.EventScheduler;

import com.LP2.EventScheduler.model.Category;
import com.LP2.EventScheduler.repository.CategoryRepository;

import jakarta.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EventSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventSchedulerApplication.class, args);
	}

    @Transactional
	@Bean
	CommandLineRunner commandLineRunner(
			CategoryRepository categoryRepository
	) {
		return args -> {
			Category fiesta = Category.builder()
					.name("Fiesta")
					.icon("https://cdn-icons-png.flaticon.com/512/3656/3656845.png")
					.build();
			categoryRepository.save(fiesta);
			Category birthday = Category.builder()
					.name("Cumpleaños")
					.icon("https://cdn.icon-icons.com/icons2/1465/PNG/512/557birthdaycake1_100853.png")
					.build();
			categoryRepository.save(birthday);
			Category cine = Category.builder()
					.name("Cine")
					.icon("https://cdn-icons-png.flaticon.com/512/2809/2809590.png")
					.build();
			categoryRepository.save(cine);
			Category renunion = Category.builder()
					.name("Reunion")
					.icon("https://cdn-icons-png.flaticon.com/512/1376/1376519.png")
					.build();
			categoryRepository.save(renunion);
		};
	}
}
