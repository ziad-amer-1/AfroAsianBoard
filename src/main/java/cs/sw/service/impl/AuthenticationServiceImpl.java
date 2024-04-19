package cs.sw.service.impl;

import cs.sw.dto.AuthenticationRequestDTO;
import cs.sw.dto.AuthenticationResponseDTO;
import cs.sw.dto.RegisterDTO;
import cs.sw.entity.AppUser;
import cs.sw.service.AuthenticationService;
import cs.sw.service.UserService;
import cs.sw.utils.JwtUtils;
import jakarta.el.PropertyNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO request, HttpServletResponse response) {
        try {
            return processAuthenticationAndTokenGeneration(request, response);
        } catch (BadCredentialsException bc) {
            log.error("حطأ في رقم الهاتف أو كلمة المرور");
            throw new IllegalStateException("حطأ في رقم الهاتف أو كلمة المرور");
        }
    }

    @Override
    public AuthenticationResponseDTO register(RegisterDTO request, HttpServletResponse response) {
        Objects.requireNonNull(request.name());
        Objects.requireNonNull(request.phoneNumber());
        Objects.requireNonNull(request.email());
        Objects.requireNonNull(request.password());
        AppUser newStudent = userService.createUser(request);
        return createTokenAndSetToHeader(newStudent, response);
    }

    private AuthenticationResponseDTO processAuthenticationAndTokenGeneration(AuthenticationRequestDTO request, HttpServletResponse response) {
        validateAndAuthenticate(request);
        return createTokenAndSetToHeader(request, response);
    }

    private void validateAndAuthenticate(AuthenticationRequestDTO request) {
        log.info("Validating user request...");
        validateAuthenticationRequest(request);
        log.info("Trying to authenticate the user with email: {}", request.email());
        tryingToAuthenticateUser(request);
        log.info("User with email: {} authenticated Successfully", request.email());
    }

    private void validateAuthenticationRequest(AuthenticationRequestDTO request) {
        if (request.email() == null || request.password() == null) {
            log.error("You should provide all the required information [phoneNumber, password]");
            throw new PropertyNotFoundException("You should provide all the required information [phoneNumber, password]");
        }
    }

    private void tryingToAuthenticateUser(AuthenticationRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim(), request.password().trim()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Map<String, Object> getClaimsFromUser(AppUser appUser) {
        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("user_id", appUser.getId());
        userInfo.put("role", appUser.getRole());
        userInfo.put("username", appUser.getName());

        return userInfo;
    }

    private AuthenticationResponseDTO createTokenAndSetToHeader(AuthenticationRequestDTO request, HttpServletResponse response) {
        AppUser appUser = loadUserByEmailIfExist(request.email().trim());
        String token = createTokenFromUser(appUser);
        setTokenToHeaderAfterAuthSuccess(response, token);
        return AuthenticationResponseDTO
                .builder()
                .token(token)
                .role(appUser.getRole())
                .name(appUser.getName())
                .build();
    }

    private AuthenticationResponseDTO createTokenAndSetToHeader(AppUser appUser, HttpServletResponse response) {
        String token = createTokenFromUser(appUser);
        setTokenToHeaderAfterAuthSuccess(response, token);
        return AuthenticationResponseDTO
                .builder()
                .token(token)
                .role(appUser.getRole())
                .name(appUser.getName())
                .build();
    }

    private AppUser loadUserByEmailIfExist(String email) {
        return userService.findByEmail(email);
    }

    private String createTokenFromUser(AppUser appUser) {
        return jwtUtils.generateToken(getClaimsFromUser(appUser), appUser);
    }

    private void setTokenToHeaderAfterAuthSuccess(HttpServletResponse response, String token) {
        response.setHeader(HttpHeaders.AUTHORIZATION, token);
    }
}