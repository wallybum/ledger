package app.ledger.user.dto;

import app.ledger.user.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private String email;
    private String name;
    private Role role;
}
