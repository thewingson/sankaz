package kz.open.sankaz;

import kz.open.sankaz.model.*;
import kz.open.sankaz.model.enums.ConfirmationStatus;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import kz.open.sankaz.model.enums.ResetNumberStatus;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.OrganizationRegisterDto;
import kz.open.sankaz.repo.*;
import kz.open.sankaz.repo.dictionary.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class SankazApplication {

	public static void main(String[] args) {
		SpringApplication.run(SankazApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepo userRepo,
						  OrganizationRepo organizationRepo,
						  SanRepo sanRepo,
						  RoomClassDicRepo roomClassDicRepo,
						  RoomRepo roomRepo,
						  RoleRepo roleRepo,
						  SanTypeRepo sanTypeRepo,
						  CityRepo cityRepo,
						  GenderRepo genderRepo,
						  CompanyCategoryRepo companyCategoryRepo,
						  PasswordEncoder passwordEncoder){
		return  args -> {
//			callScript(userRepo, organizationRepo, sanRepo, roomClassDicRepo, roomRepo, roleRepo, sanTypeRepo, cityRepo, genderRepo, companyCategoryRepo, passwordEncoder);
		};
	}

	public static void callScript(UserRepo userRepo,
								  OrganizationRepo organizationRepo,
								  SanRepo sanRepo,
								  RoomClassDicRepo roomClassDicRepo,
								  RoomRepo roomRepo,
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
		SecRole user = new SecRole(null, "ROLE_USER");
		roleRepo.save(user);

		SecUser adminUser = new SecUser();
		adminUser.setUserType(UserType.USER);
		adminUser.setFirstName("Admin");
		adminUser.setLastName("Admin");
		adminUser.setTelNumber("+77770000000");
		adminUser.setEmail("admin@admin.kz");
		adminUser.setUsername("+77770000000");
		adminUser.setPassword(passwordEncoder.encode("123123123"));
		adminUser.addRole(admin);
		adminUser.setConfirmationStatus(ConfirmationStatus.FINISHED);
		adminUser.setResetNumberStatus(ResetNumberStatus.EMPTY);
		adminUser.setConfirmationNumber(0);
		adminUser.setConfirmedBy("admin");
		adminUser.setConfirmedDate(LocalDateTime.now());
		adminUser.setActive(true);
		adminUser.setLoggedOut(false);
		userRepo.save(adminUser);

		SecUser testUser = new SecUser();
		testUser.setUserType(UserType.USER);
		testUser.setFirstName("User");
		testUser.setLastName("User");
		testUser.setTelNumber("+77770000002");
		testUser.setEmail("user@user.kz");
		testUser.setUsername("+77770000002");
		testUser.setPassword(passwordEncoder.encode("123123123"));
		testUser.addRole(user);
		testUser.setConfirmationStatus(ConfirmationStatus.FINISHED);
		testUser.setResetNumberStatus(ResetNumberStatus.EMPTY);
		testUser.setConfirmationNumber(0);
		testUser.setConfirmedBy("admin");
		testUser.setConfirmedDate(LocalDateTime.now());
		testUser.setActive(true);
		testUser.setLoggedOut(false);
		userRepo.save(testUser);

		SecUser testUser2 = new SecUser();
		testUser2.setUserType(UserType.USER);
		testUser2.setFirstName("User2");
		testUser2.setLastName("User2");
		testUser2.setTelNumber("+77770000004");
		testUser2.setEmail("user4@user.kz");
		testUser2.setUsername("+77770000004");
		testUser2.setPassword(passwordEncoder.encode("123123123"));
		testUser2.addRole(user);
		testUser2.setConfirmationStatus(ConfirmationStatus.FINISHED);
		testUser2.setResetNumberStatus(ResetNumberStatus.EMPTY);
		testUser2.setConfirmationNumber(0);
		testUser2.setConfirmedBy("admin");
		testUser2.setConfirmedDate(LocalDateTime.now());
		testUser2.setActive(true);
		testUser2.setLoggedOut(false);
		userRepo.save(testUser2);

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
		companyCategory.setCode("TOUR");
		companyCategory.setName("Туристическая");
		companyCategory.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		companyCategoryRepo.save(companyCategory);

		CompanyCategory companyCategory1 = new CompanyCategory();
		companyCategory1.setCode("PROF");
		companyCategory1.setName("Прфилактическая");
		companyCategory1.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		companyCategoryRepo.save(companyCategory1);

		createOrganization1(userRepo,
				moderator,
				companyCategory,
				sanType,
				city,
				organizationRepo,
				sanRepo,
				roomClassDicRepo,
				roomRepo,
				passwordEncoder);

		createOrganization2(userRepo,
				moderator,
				companyCategory,
				sanType,
				city,
				organizationRepo,
				sanRepo,
				roomClassDicRepo,
				roomRepo,
				passwordEncoder);

		createOrganization3(userRepo,
				moderator,
				companyCategory1,
				sanType2,
				city2,
				organizationRepo,
				sanRepo,
				roomClassDicRepo,
				roomRepo,
				passwordEncoder);

		createOrganization4(userRepo,
				moderator,
				companyCategory1,
				sanType2,
				city2,
				organizationRepo,
				sanRepo,
				roomClassDicRepo,
				roomRepo,
				passwordEncoder);


	}

	public static void createOrganization1(UserRepo userRepo,
								  SecRole moderator,
								  CompanyCategory companyCategory,
								  SanType sanType,
								  City city,
								  OrganizationRepo organizationRepo,
								  SanRepo sanRepo,
								  RoomClassDicRepo roomClassDicRepo,
								  RoomRepo roomRepo,
								  PasswordEncoder passwordEncoder) {
		SecUser orgUser = new SecUser();
		orgUser.setUserType(UserType.ORG);
		orgUser.setFirstName("Organ");
		orgUser.setLastName("Organ");
		orgUser.setTelNumber("+77770000070");
		orgUser.setEmail("organ@organ.kz");
		orgUser.setUsername("+77770000070");
		orgUser.setPassword(passwordEncoder.encode("123123123"));
		orgUser.addRole(moderator);
		orgUser.setConfirmationStatus(ConfirmationStatus.FINISHED);
		orgUser.setResetNumberStatus(ResetNumberStatus.EMPTY);
		orgUser.setConfirmationNumber(0);
		orgUser.setConfirmedBy("admin");
		orgUser.setConfirmedDate(LocalDateTime.now());
		orgUser.setActive(true);
		orgUser.setLoggedOut(false);
		userRepo.save(orgUser);

		Organization organization = new Organization();
		organization.setSiteLink("smsc.kz");
		organization.setInstagramLink("instagram.com/rkalmat");
		organization.setAddress("г.Сатпаев, ул.Абая, д.175");
		organization.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		organization.setCompanyName("Берега");
		organization.setConfirmedBy("+77770000000");
		organization.setApprovedDate(LocalDateTime.now());
		organization.setCompanyCategory(companyCategory);
		organization.setIin("707070707070");
		organization.setIban("70707070707070");
		organization.setTelNumber(orgUser.getTelNumber());
		organization.setManagerFullName(orgUser.getFullName());
		organization.setUser(orgUser);
		organization.setName("ТОО Отдых");
		organization.setEmail("org@mail.kz");
		organization.setConfirmationStatus(OrganizationConfirmationStatus.SERVICE_CREATED);
		organizationRepo.save(organization);

		San san = new San();
		san.setName("Ақбұлак");
		san.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		san.setAddress("Талгар, ул.Бокейханов, д.17");
		san.setSanType(sanType);
		san.setCity(city);
		san.setOrganization(organization);
		sanRepo.save(san);

		RoomClassDic roomClassDic = new RoomClassDic();
		roomClassDic.setSan(san);
		roomClassDic.setCode("LUX");
		roomClassDic.setName("Люкс");
		roomClassDic.setNameKz("Люкс");
		roomClassDic.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic);

		RoomClassDic roomClassDic2 = new RoomClassDic();
		roomClassDic2.setSan(san);
		roomClassDic2.setCode("COM");
		roomClassDic2.setName("Комфорт");
		roomClassDic2.setNameKz("Комфорт");
		roomClassDic2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic2.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic2);

		Room room = new Room();
		room.setRoomClassDic(roomClassDic2);
		room.setPrice(BigDecimal.valueOf(14500));
		room.setRoomCount(2);
		room.setBedCount(1);
		room.setRoomNumber("201E");
		roomRepo.save(room);

		Room room2 = new Room();
		room2.setRoomClassDic(roomClassDic);
		room2.setPrice(BigDecimal.valueOf(34500));
		room2.setRoomCount(3);
		room2.setBedCount(2);
		room2.setRoomNumber("101");
		roomRepo.save(room2);
	}

	public static void createOrganization2(UserRepo userRepo,
										   SecRole moderator,
										   CompanyCategory companyCategory,
										   SanType sanType,
										   City city,
										   OrganizationRepo organizationRepo,
										   SanRepo sanRepo,
										   RoomClassDicRepo roomClassDicRepo,
										   RoomRepo roomRepo,
										   PasswordEncoder passwordEncoder) {
		SecUser orgUser = new SecUser();
		orgUser.setUserType(UserType.ORG);
		orgUser.setFirstName("Organ 1");
		orgUser.setLastName("Organ 1");
		orgUser.setTelNumber("+77770000071");
		orgUser.setEmail("organ1@organ.kz");
		orgUser.setUsername("+77770000071");
		orgUser.setPassword(passwordEncoder.encode("123123123"));
		orgUser.addRole(moderator);
		orgUser.setConfirmationStatus(ConfirmationStatus.FINISHED);
		orgUser.setResetNumberStatus(ResetNumberStatus.EMPTY);
		orgUser.setConfirmationNumber(0);
		orgUser.setConfirmedBy("admin");
		orgUser.setConfirmedDate(LocalDateTime.now());
		orgUser.setActive(true);
		orgUser.setLoggedOut(false);
		userRepo.save(orgUser);

		Organization organization = new Organization();
		organization.setSiteLink("github.com");
		organization.setInstagramLink("instagram.com/fckairat");
		organization.setAddress("г.Сатпаев, ул.Төле би, д.100");
		organization.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		organization.setCompanyName("Номад");
		organization.setConfirmedBy("+77770000000");
		organization.setApprovedDate(LocalDateTime.now());
		organization.setCompanyCategory(companyCategory);
		organization.setIin("717171717171");
		organization.setIban("71717171717171");
		organization.setTelNumber(orgUser.getTelNumber());
		organization.setManagerFullName(orgUser.getFullName());
		organization.setUser(orgUser);
		organization.setName("ИП Общак");
		organization.setEmail("org1@mail.kz");
		organization.setConfirmationStatus(OrganizationConfirmationStatus.SERVICE_CREATED);
		organizationRepo.save(organization);

		San san = new San();
		san.setName("Лесная сказка");
		san.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		san.setAddress("Талгар, ул.Абылай хан, д.20");
		san.setSanType(sanType);
		san.setCity(city);
		san.setOrganization(organization);
		sanRepo.save(san);

		RoomClassDic roomClassDic = new RoomClassDic();
		roomClassDic.setSan(san);
		roomClassDic.setCode("LUX");
		roomClassDic.setName("Люкс");
		roomClassDic.setNameKz("Люкс");
		roomClassDic.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic);

		RoomClassDic roomClassDic2 = new RoomClassDic();
		roomClassDic2.setSan(san);
		roomClassDic2.setCode("COM");
		roomClassDic2.setName("Комфорт");
		roomClassDic2.setNameKz("Комфорт");
		roomClassDic2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic2.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic2);

		Room room = new Room();
		room.setRoomClassDic(roomClassDic2);
		room.setPrice(BigDecimal.valueOf(14500));
		room.setRoomCount(2);
		room.setBedCount(1);
		room.setRoomNumber("31");
		roomRepo.save(room);

		Room room2 = new Room();
		room2.setRoomClassDic(roomClassDic);
		room2.setPrice(BigDecimal.valueOf(34500));
		room2.setRoomCount(3);
		room2.setBedCount(2);
		room2.setRoomNumber("9");
		roomRepo.save(room2);
	}

	public static void createOrganization3(UserRepo userRepo,
										   SecRole moderator,
										   CompanyCategory companyCategory,
										   SanType sanType,
										   City city,
										   OrganizationRepo organizationRepo,
										   SanRepo sanRepo,
										   RoomClassDicRepo roomClassDicRepo,
										   RoomRepo roomRepo,
										   PasswordEncoder passwordEncoder) {
		SecUser orgUser = new SecUser();
		orgUser.setUserType(UserType.ORG);
		orgUser.setFirstName("Organ 2");
		orgUser.setLastName("Organ 2");
		orgUser.setTelNumber("+77770000072");
		orgUser.setEmail("organ2@organ.kz");
		orgUser.setUsername("+77770000072");
		orgUser.setPassword(passwordEncoder.encode("123123123"));
		orgUser.addRole(moderator);
		orgUser.setConfirmationStatus(ConfirmationStatus.FINISHED);
		orgUser.setResetNumberStatus(ResetNumberStatus.EMPTY);
		orgUser.setConfirmationNumber(0);
		orgUser.setConfirmedBy("admin");
		orgUser.setConfirmedDate(LocalDateTime.now());
		orgUser.setActive(true);
		orgUser.setLoggedOut(false);
		userRepo.save(orgUser);

		Organization organization = new Organization();
		organization.setSiteLink("jira.com");
		organization.setInstagramLink("instagram.com/dauletten");
		organization.setAddress("г.Жезказган, ул.Сейдімбек, д.10");
		organization.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		organization.setCompanyName("Степь");
		organization.setConfirmedBy("+77770000000");
		organization.setApprovedDate(LocalDateTime.now());
		organization.setCompanyCategory(companyCategory);
		organization.setIin("727272727272");
		organization.setIban("72727272727272");
		organization.setTelNumber(orgUser.getTelNumber());
		organization.setManagerFullName(orgUser.getFullName());
		organization.setUser(orgUser);
		organization.setName("ИП Улытау");
		organization.setEmail("org2@mail.kz");
		organization.setConfirmationStatus(OrganizationConfirmationStatus.CONFIRMED);
		organizationRepo.save(organization);

		San san = new San();
		san.setName("Шымбұлак");
		san.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		san.setAddress("Талгар, ул.Есенберлин, д.");
		san.setSanType(sanType);
		san.setCity(city);
		san.setOrganization(organization);
		sanRepo.save(san);

		RoomClassDic roomClassDic = new RoomClassDic();
		roomClassDic.setSan(san);
		roomClassDic.setCode("LUX");
		roomClassDic.setName("Люкс");
		roomClassDic.setNameKz("Люкс");
		roomClassDic.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic);

		RoomClassDic roomClassDic2 = new RoomClassDic();
		roomClassDic2.setSan(san);
		roomClassDic2.setCode("COM");
		roomClassDic2.setName("Комфорт");
		roomClassDic2.setNameKz("Комфорт");
		roomClassDic2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic2.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic2);

		Room room = new Room();
		room.setRoomClassDic(roomClassDic2);
		room.setPrice(BigDecimal.valueOf(14500));
		room.setRoomCount(2);
		room.setBedCount(1);
		room.setRoomNumber("13");
		roomRepo.save(room);

		Room room2 = new Room();
		room2.setRoomClassDic(roomClassDic);
		room2.setPrice(BigDecimal.valueOf(34500));
		room2.setRoomCount(3);
		room2.setBedCount(2);
		room2.setRoomNumber("7");
		roomRepo.save(room2);
	}

	public static void createOrganization4(UserRepo userRepo,
										   SecRole moderator,
										   CompanyCategory companyCategory,
										   SanType sanType,
										   City city,
										   OrganizationRepo organizationRepo,
										   SanRepo sanRepo,
										   RoomClassDicRepo roomClassDicRepo,
										   RoomRepo roomRepo,
										   PasswordEncoder passwordEncoder) {
		SecUser orgUser = new SecUser();
		orgUser.setUserType(UserType.ORG);
		orgUser.setFirstName("Organ 3");
		orgUser.setLastName("Organ 3");
		orgUser.setTelNumber("+77770000073");
		orgUser.setEmail("organ3@organ.kz");
		orgUser.setUsername("+77770000073");
		orgUser.setPassword(passwordEncoder.encode("123123123"));
		orgUser.addRole(moderator);
		orgUser.setConfirmationStatus(ConfirmationStatus.FINISHED);
		orgUser.setResetNumberStatus(ResetNumberStatus.EMPTY);
		orgUser.setConfirmationNumber(0);
		orgUser.setConfirmedBy("admin");
		orgUser.setConfirmedDate(LocalDateTime.now());
		orgUser.setActive(true);
		orgUser.setLoggedOut(false);
		userRepo.save(orgUser);

		Organization organization = new Organization();
		organization.setSiteLink("bitbucket.com");
		organization.setInstagramLink("instagram.com/apple");
		organization.setAddress("г.Кокшетау, ул.Тау, д.14а");
		organization.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		organization.setCompanyName("Көкше");
		organization.setConfirmedBy("+77770000000");
		organization.setApprovedDate(LocalDateTime.now());
		organization.setCompanyCategory(companyCategory);
		organization.setIin("737373737373");
		organization.setIban("73737373737373");
		organization.setTelNumber(orgUser.getTelNumber());
		organization.setManagerFullName(orgUser.getFullName());
		organization.setUser(orgUser);
		organization.setName("Kokshe Group");
		organization.setEmail("org3@mail.kz");
		organization.setConfirmationStatus(OrganizationConfirmationStatus.SERVICE_CREATED);
		organizationRepo.save(organization);

		San san = new San();
		san.setName("8 озер");
		san.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		san.setSanType(sanType);
		san.setAddress("Талгар, ул.Кунаев, д.90");
		san.setCity(city);
		san.setOrganization(organization);
		sanRepo.save(san);

		RoomClassDic roomClassDic = new RoomClassDic();
		roomClassDic.setSan(san);
		roomClassDic.setCode("LUX");
		roomClassDic.setName("Люкс");
		roomClassDic.setNameKz("Люкс");
		roomClassDic.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic);

		RoomClassDic roomClassDic2 = new RoomClassDic();
		roomClassDic2.setSan(san);
		roomClassDic2.setCode("COM");
		roomClassDic2.setName("Комфорт");
		roomClassDic2.setNameKz("Комфорт");
		roomClassDic2.setDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDic2.setDescriptionKz("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
		roomClassDicRepo.save(roomClassDic2);

		Room room = new Room();
		room.setRoomClassDic(roomClassDic2);
		room.setPrice(BigDecimal.valueOf(14500));
		room.setRoomCount(2);
		room.setBedCount(1);
		room.setRoomNumber("13");
		roomRepo.save(room);

		Room room2 = new Room();
		room2.setRoomClassDic(roomClassDic);
		room2.setPrice(BigDecimal.valueOf(34500));
		room2.setRoomCount(3);
		room2.setBedCount(2);
		room2.setRoomNumber("7");
		roomRepo.save(room2);
	}



}
