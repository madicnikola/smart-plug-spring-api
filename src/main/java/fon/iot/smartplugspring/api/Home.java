package fon.iot.smartplugspring.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {
    @GetMapping("/")
    public String hello() {
        return "Welcome Dragane!";
    }
}
