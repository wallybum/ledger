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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "사용자 등록 및 조회 API")
public class UserController {

    private final UserService fUserService;

    public UserController(UserService userService) {
        fUserService = userService;
    }

    @PostMapping("/external/register")
    @Operation(summary = "소셜 사용자 등록", description = "Google/Apple 기반의 소셜 사용자 또는 자체 가입자를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 유효성 검증 실패"),
            @ApiResponse(responseCode = "409", description = "이미 등록된 사용자")
    })
    public ResponseEntity<UserResponse> registerExternalUser(@Valid @RequestBody SocialUserDto socialUserDto) {
        User user = fUserService.registerExternal(socialUserDto);
        return ResponseEntity.ok(fUserService.toResponse(user));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "이메일로 사용자 조회", description = "등록된 사용자를 이메일 주소로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자")
    })
    public ResponseEntity<UserResponse> getUser(@PathVariable String email) {
        return ResponseEntity.ok(fUserService.toResponse(fUserService.getUserByEmail(email)));
    }
}
