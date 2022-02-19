package kz.open.sankaz;

import kz.open.sankaz.model.*;
import kz.open.sankaz.repo.RoleRepo;
import kz.open.sankaz.repo.SanRepo;
import kz.open.sankaz.repo.UserRepo;
import kz.open.sankaz.repo.dictionary.CityRepo;
import kz.open.sankaz.repo.dictionary.CompanyCategoryRepo;
import kz.open.sankaz.repo.dictionary.GenderRepo;
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
						  SanRepo sanRepo,
						  RoleRepo roleRepo,
						  SanTypeRepo sanTypeRepo,
						  CityRepo cityRepo,
						  GenderRepo genderRepo,
						  CompanyCategoryRepo companyCategoryRepo,
						  PasswordEncoder passwordEncoder){
		return  args -> {
			callScript(userRepo, sanRepo, roleRepo, sanTypeRepo, cityRepo, genderRepo, companyCategoryRepo, passwordEncoder);
		};
	}

	public static void callScript(UserRepo userRepo,
								  SanRepo sanRepo,
								  RoleRepo roleRepo,
								  SanTypeRepo sanTypeRepo,
								  CityRepo cityRepo,
								  GenderRepo genderRepo,
								  CompanyCategoryRepo companyCategoryRepo,
								  PasswordEncoder passwordEncoder) {
		SecRole admin = new SecRole(null, "ROLE_ADMIN");
		roleRepo.save(admin);
		SecRole moderator = new SecRole(null, "ROLE_MODERATOR");
		roleRepo.save(moderator);
		SecRole manager = new SecRole(null, "ROLE_ORG");
		roleRepo.save(manager);
		SecRole user = new SecRole(null, "ROLE_USER");
		roleRepo.save(user);

		SecUser adminUser = new SecUser();
		adminUser.setUserType("USER");
		adminUser.setFirstName("Admin");
		adminUser.setLastName("Admin");
		adminUser.setTelNumber("+77770000000");
		adminUser.setEmail("admin@mail.kz");
		adminUser.setUsername("+77770000000");
		adminUser.setPassword(passwordEncoder.encode("123"));
		adminUser.addRole(admin);
		adminUser.setConfirmationStatus("FINISHED");
		adminUser.setResetNumberStatus("EMPTY");
		adminUser.setConfirmationNumber(0);
		adminUser.setConfirmedBy("admin");
		adminUser.setConfirmedDate(LocalDateTime.now());
		adminUser.setActive(true);
		adminUser.setLoggedOut(false);
		userRepo.save(adminUser);

		SecUser moderatorUser = new SecUser();
		moderatorUser.setUserType("USER");
		moderatorUser.setFirstName("Moderator");
		moderatorUser.setLastName("Moderator");
		moderatorUser.setTelNumber("+77770000001");
		moderatorUser.setEmail("moderator@mail.kz");
		moderatorUser.setUsername("+77770000001");
		moderatorUser.setPassword(passwordEncoder.encode("123"));
		moderatorUser.addRole(moderator);
		moderatorUser.setConfirmationStatus("FINISHED");
		moderatorUser.setResetNumberStatus("EMPTY");
		moderatorUser.setConfirmationNumber(0);
		moderatorUser.setConfirmedBy("admin");
		moderatorUser.setConfirmedDate(LocalDateTime.now());
		moderatorUser.setActive(true);
		moderatorUser.setLoggedOut(false);
		userRepo.save(moderatorUser);

		SecUser testUser = new SecUser();
		testUser.setUserType("USER");
		testUser.setFirstName("Test_user");
		testUser.setLastName("Test_user");
		testUser.setTelNumber("+77770000002");
		testUser.setEmail("testuser@mail.kz");
		testUser.setUsername("+77770000002");
		testUser.setPassword(passwordEncoder.encode("123"));
		testUser.addRole(user);
		testUser.setConfirmationStatus("FINISHED");
		testUser.setResetNumberStatus("EMPTY");
		testUser.setConfirmationNumber(0);
		testUser.setConfirmedBy("admin");
		testUser.setConfirmedDate(LocalDateTime.now());
		testUser.setActive(true);
		testUser.setLoggedOut(false);
		userRepo.save(testUser);

		SecUser orgUser = new SecUser();
		orgUser.setUserType("ORG");
		orgUser.setFirstName("Org");
		orgUser.setLastName("Org");
		orgUser.setTelNumber("+77770000003");
		orgUser.setEmail("org@mail.kz");
		orgUser.setUsername("+77770000003");
		orgUser.setPassword(passwordEncoder.encode("123"));
		orgUser.addRole(manager);
		orgUser.setConfirmationStatus("FINISHED");
		orgUser.setResetNumberStatus("EMPTY");
		orgUser.setConfirmationNumber(0);
		orgUser.setConfirmedBy("admin");
		orgUser.setConfirmedDate(LocalDateTime.now());
		orgUser.setActive(true);
		orgUser.setLoggedOut(false);
		userRepo.save(orgUser);

		SanType sanType = new SanType();
		sanType.setCode("ENTERTAINMENT");
		sanType.setName("Развлекательный");
		sanType.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		sanTypeRepo.save(sanType);

		SanType sanType2 = new SanType();
		sanType2.setCode("MEDICAL");
		sanType2.setName("Лечебный");
		sanType2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		sanTypeRepo.save(sanType2);

		City city = new City();
		city.setCode("ALM");
		city.setName("Алматы");
		city.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		cityRepo.save(city);

		City city2 = new City();
		city2.setCode("AST");
		city2.setName("Астана");
		city2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		cityRepo.save(city2);

		Gender gender = new Gender();
		gender.setCode("MALE");
		gender.setName("Муж.");
		gender.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		genderRepo.save(gender);

		Gender gender2 = new Gender();
		gender2.setCode("FEMALE");
		gender2.setName("Жен.");
		gender2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		genderRepo.save(gender2);

		CompanyCategory companyCategory = new CompanyCategory();
		companyCategory.setCode("CAT1");
		companyCategory.setName("companyCategory 1");
		companyCategory.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		companyCategoryRepo.save(companyCategory);

		CompanyCategory companyCategory1 = new CompanyCategory();
		companyCategory1.setCode("CAT2");
		companyCategory1.setName("companyCategory 2");
		companyCategory1.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		companyCategoryRepo.save(companyCategory1);

		San san = new San();
		san.setName("test 1");
		san.setDescription("test 1 test 1 test 1");
		san.setSanType(sanType);
		san.setCity(city);
		sanRepo.save(san);

	}

}
