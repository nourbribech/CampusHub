package tn.enicarthage.campushub.auth.service;


import tn.enicarthage.campushub.auth.dto.AuthResponse;
import tn.enicarthage.campushub.auth.dto.LoginRequest;
import tn.enicarthage.campushub.auth.dto.UserDto;
import tn.enicarthage.campushub.auth.security.JwtService;
import tn.enicarthage.campushub.shared.model.User;
import tn.enicarthage.campushub.shared.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Enum::name).toList())
                .avatar(user.getAvatar())
                .departement(user.getDepartement())
                .build();

        return AuthResponse.builder()
                .token(token)
                .user(userDto)
                .build();
    }
}