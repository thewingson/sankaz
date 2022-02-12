package kz.open.sankaz;

import kz.open.sankaz.model.*;
import kz.open.sankaz.repo.RoleRepo;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.repo.dictionary.CityRepo;
import kz.open.sankaz.repo.dictionary.CompanyCategoryRepo;
import kz.open.sankaz.repo.dictionary.SanTypeRepo;
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
	CommandLineRunner run(UserRepo userRepo,
						  RoleRepo roleRepo,
						  SanTypeRepo sanTypeRepo,
						  CityRepo cityRepo,
						  CompanyCategoryRepo companyCategoryRepo,
						  PasswordEncoder passwordEncoder){
		return  args -> {
			callScript(userRepo, roleRepo, sanTypeRepo, cityRepo, companyCategoryRepo, passwordEncoder);
		};
	}

	public static void callScript(UserRepo userRepo,
								  RoleRepo roleRepo,
								  SanTypeRepo sanTypeRepo,
								  CityRepo cityRepo,
								  CompanyCategoryRepo companyCategoryRepo,
								  PasswordEncoder passwordEncoder) {
		SecRole admin = new SecRole(null, "ROLE_ADMIN");
//		admin.setCreatedBy("admin");
//		admin.setCreateTs(LocalDateTime.now());
		roleRepo.save(admin);
		SecRole moderator = new SecRole(null, "ROLE_MODERATOR");
//		moderator.setCreatedBy("admin");
//		moderator.setCreateTs(LocalDateTime.now());
		roleRepo.save(moderator);
		SecRole manager = new SecRole(null, "ROLE_ORG");
//		manager.setCreatedBy("admin");
//		manager.setCreateTs(LocalDateTime.now());
		roleRepo.save(manager);
		SecRole user = new SecRole(null, "ROLE_USER");
//		user.setCreatedBy("admin");
//		user.setCreateTs(LocalDateTime.now());
		roleRepo.save(user);

		SecUser user1 = new SecUser();
		user1.setUserType("USER");
		user1.setFirstName("Admin");
		user1.setLastName("Admin");
//			user1.setCity("Almaty");
		user1.setTelNumber("+77777777777");
		user1.setEmail("admin@mail.kz");
		user1.setUsername("+77777777777");
		user1.setPassword(passwordEncoder.encode("123"));
//			user1.setGender("male");
		user1.addRole(admin);
		user1.setConfirmationStatus("FINISHED");
		user1.setResetNumberStatus("EMPTY");
		user1.setConfirmationNumber(0);
		user1.setConfirmedBy("admin");
		user1.setConfirmedTs(LocalDateTime.now());
		user1.setActive(true);
		user1.setLoggedOut(false);
//		user1.setCreatedBy("admin");
//		user1.setCreateTs(LocalDateTime.now());
		userRepo.save(user1);

		SecUser user2 = new SecUser();
		user2.setUserType("USER");
		user2.setFirstName("Moderator");
		user2.setLastName("Moderator");
//			user2.setCity("Almaty");
		user2.setTelNumber("+77777777778");
		user2.setEmail("moderator@mail.kz");
		user2.setUsername("+77777777778");
		user2.setPassword(passwordEncoder.encode("123"));
//			user2.setGender("male");
		user2.addRole(admin);
		user2.setConfirmationStatus("FINISHED");
		user2.setResetNumberStatus("EMPTY");
		user2.setConfirmationNumber(0);
		user2.setConfirmedBy("admin");
		user2.setConfirmedTs(LocalDateTime.now());
		user2.setActive(true);
		user2.setLoggedOut(false);
//		user2.setCreatedBy("admin");
//		user2.setCreateTs(LocalDateTime.now());
		userRepo.save(user2);

		SanType sanType = new SanType();
		sanType.setCode("type1");
		sanType.setName("type 1");
		sanType.setDescription("type tpe 1");
//		sanType.setCreatedBy("admin");
//		sanType.setCreateTs(LocalDateTime.now());
		sanTypeRepo.save(sanType);

		SanType sanType2 = new SanType();
		sanType2.setCode("type2");
		sanType2.setName("type 2");
		sanType2.setDescription("type tpe 2");
//		sanType2.setCreatedBy("admin");
//		sanType2.setCreateTs(LocalDateTime.now());
		sanTypeRepo.save(sanType2);

		City city = new City();
		city.setCode("ALM");
		city.setName("Almaty");
		city.setDescription("type tpe 2");
//		city.setCreatedBy("admin");
//		city.setCreateTs(LocalDateTime.now());
		cityRepo.save(city);

		City city2 = new City();
		city2.setCode("AST");
		city2.setName("Astana");
		city2.setDescription("type tpe 2");
//		city2.setCreatedBy("admin");
//		city2.setCreateTs(LocalDateTime.now());
		cityRepo.save(city2);

		CompanyCategory companyCategory = new CompanyCategory();
		companyCategory.setCode("ALM");
		companyCategory.setName("Almaty");
		companyCategory.setDescription("type tpe 2");
//		companyCategory.setCreatedBy("admin");
//		companyCategory.setCreateTs(LocalDateTime.now());
		companyCategoryRepo.save(companyCategory);

		CompanyCategory companyCategory1 = new CompanyCategory();
		companyCategory1.setCode("AST");
		companyCategory1.setName("Astana");
		companyCategory1.setDescription("type tpe 2");
//		companyCategory1.setCreatedBy("admin");
//		companyCategory1.setCreateTs(LocalDateTime.now());
		companyCategoryRepo.save(companyCategory1);
	}

}
