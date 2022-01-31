package kz.open.sankaz;

import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.repo.RoleRepo;
import kz.open.sankaz.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class SankazApplication {

	public static void main(String[] args) {
		SpringApplication.run(SankazApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder){
		return  args -> {
			SecRole admin = new SecRole(null, "ROLE_ADMIN");
			admin.setCreatedBy("admin");
			admin.setCreateTs(LocalDateTime.now());
			roleRepo.save(admin);
			SecRole manager = new SecRole(null, "ROLE_MANAGER");
			manager.setCreatedBy("admin");
			manager.setCreateTs(LocalDateTime.now());
			roleRepo.save(manager);
			SecRole user = new SecRole(null, "ROLE_USER");
			user.setCreatedBy("admin");
			user.setCreateTs(LocalDateTime.now());
			roleRepo.save(user);

			SecUser user1 = new SecUser();
			user1.setFirstName("Admin");
			user1.setLastName("Admin");
			user1.setCity("Almaty");
			user1.setTelNumber("+77777777777");
			user1.setEmail("admin@mail.kz");
			user1.setUsername("admin");
			user1.setPassword(passwordEncoder.encode("123"));
			user1.setGender("male");
			user1.addRole(admin);
			user1.setConfirmationStatus("FINISHED");
			user1.setResetNumberStatus("EMPTY");
			user1.setConfirmationNumber(0);
			user1.setConfirmedBy("admin");
			user1.setConfirmedTs(LocalDateTime.now());
			user1.setActive(true);
			user1.setLoggedOut(false);
			user1.setCreatedBy("admin");
			user1.setCreateTs(LocalDateTime.now());
			userRepo.save(user1);
		};
	}

}
