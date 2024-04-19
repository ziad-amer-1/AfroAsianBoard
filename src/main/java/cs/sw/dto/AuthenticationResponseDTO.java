package cs.sw.dto;

import cs.sw.entity.Role;
import lombok.Builder;

@Builder
public record AuthenticationResponseDTO(String token, Role role, String name) {
}