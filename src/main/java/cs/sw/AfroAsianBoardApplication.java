package cs.sw;

import cs.sw.entity.*;
import cs.sw.repository.CourseRepo;
import cs.sw.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class AfroAsianBoardApplication {

	private final UserRepo userRepo;
	private final CourseRepo courseRepo;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AfroAsianBoardApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			AppUser manager = AppUser
					.builder()
					.name("manager")
					.phoneNumber("0111")
					.email("manager@gmail.com")
					.password(passwordEncoder.encode("123"))
					.role(Role.MANAGER)
					.build();

			AppUser student = AppUser
					.builder()
					.name("student")
					.phoneNumber("0222")
					.email("student@gmail.com")
					.password(passwordEncoder.encode("123"))
					.role(Role.STUDENT)
					.build();

			userRepo.save(manager);
			userRepo.save(student);
		};
	}

}
