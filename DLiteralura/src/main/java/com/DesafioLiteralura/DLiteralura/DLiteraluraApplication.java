package com.DesafioLiteralura.DLiteralura;

import com.DesafioLiteralura.DLiteralura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.DesafioLiteralura.DLiteralura")
public class DLiteraluraApplication implements CommandLineRunner {

	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(DLiteraluraApplication.class, args);
	}
	@Override
	public void run(String... args) {
		principal.muestraElMenu();
	}

}
