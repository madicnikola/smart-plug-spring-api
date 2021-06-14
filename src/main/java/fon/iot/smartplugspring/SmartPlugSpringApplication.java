package fon.iot.smartplugspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartPlugSpringApplication {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/smart-plug-spring");
        SpringApplication.run(SmartPlugSpringApplication.class, args);
    }

}
