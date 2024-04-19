package cs.sw.service;

import cs.sw.dto.AuthenticationRequestDTO;
import cs.sw.dto.AuthenticationResponseDTO;
import cs.sw.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    AuthenticationResponseDTO login(AuthenticationRequestDTO request, HttpServletResponse response) throws Exception;
    AuthenticationResponseDTO register(RegisterDTO request, HttpServletResponse response);
}
