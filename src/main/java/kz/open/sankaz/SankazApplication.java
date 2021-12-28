package kz.open.sankaz;

import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@SpringBootApplication
public class SankazApplication {

	public static void main(String[] args) {
		SpringApplication.run(SankazApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return  args -> {
			userService.createRole(new SecRole(null, "ROLE_SUPER_ADMIN"));
			userService.createRole(new SecRole(null, "ROLE_ADMIN"));
			userService.createRole(new SecRole(null, "ROLE_MANAGER"));
			userService.createRole(new SecRole(null, "ROLE_USER"));

			userService.createUser(new SecUser(null, "superAdmin", "123", true, new HashSet<>(), "superadmin@mail.kz", "Super", "Admin"));
			userService.createUser(new SecUser(null, "admin", "123", true, new HashSet<>(), "admin@mail.kz", "Simple", "Admin"));
			userService.createUser(new SecUser(null, "manager", "123", true, new HashSet<>(), "manager@mail.kz", "Simple", "Manager"));
			userService.createUser(new SecUser(null, "user", "123", true, new HashSet<>(), "user@mail.kz", "Simple", "User"));

			userService.addRoleToUser("superAdmin", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("superAdmin", "ROLE_ADMIN");
			userService.addRoleToUser("superAdmin", "ROLE_MANAGER");
			userService.addRoleToUser("superAdmin", "ROLE_USER");
			userService.addRoleToUser("admin", "ROLE_ADMIN");
			userService.addRoleToUser("manager", "ROLE_MANAGER");
			userService.addRoleToUser("user", "ROLE_USER");
		};
	}

}
