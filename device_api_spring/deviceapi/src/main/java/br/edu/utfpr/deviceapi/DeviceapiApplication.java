package br.edu.utfpr.deviceapi;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@EnableRabbit
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class DeviceapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceapiApplication.class, args);
	}

}
