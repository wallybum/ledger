package app.ledger.user.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserTestController{

    @GetMapping("/ping")
    public String ping() {
        return "user-service is alive";
    }
}