package kz.open.sankaz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class SankazApplication {

	public static void main(String[] args) {
		SpringApplication application= new SpringApplication(SankazApplication.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);
	}

}
