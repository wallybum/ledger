package app.ledger.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.ledger.user.domain.entity.User;
import app.ledger.user.dto.SocialUserDto;
import app.ledger.user.dto.UserResponse;
import app.ledger.user.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService fUserService;

    public UserController(UserService userService) {
        fUserService = userService;
    }

    @PostMapping("/external/register")
    public ResponseEntity<UserResponse> registerExternalUser(@Valid @RequestBody SocialUserDto socialUserDto) {
        User user = fUserService.registerExternal(socialUserDto);
        return ResponseEntity.ok(fUserService.toResponse(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
        return ResponseEntity.ok(fUserService.toResponse(fUserService.getUserByEmail(email)));
    }
}
