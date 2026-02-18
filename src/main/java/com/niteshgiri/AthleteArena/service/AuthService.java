package com.niteshgiri.AthleteArena.service;

import com.niteshgiri.AthleteArena.config.AuthUtil;
import com.niteshgiri.AthleteArena.dto.LoginRequestDto;
import com.niteshgiri.AthleteArena.dto.LoginResponseDto;
import com.niteshgiri.AthleteArena.dto.SignUpRequestDto;
import com.niteshgiri.AthleteArena.dto.SignupResponseDto;
import com.niteshgiri.AthleteArena.entity.User;
import com.niteshgiri.AthleteArena.entity.type.RoleType;
import com.niteshgiri.AthleteArena.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    public SignupResponseDto signup(SignUpRequestDto signUpRequestDto) {
        Optional<User> existingUser = userRepository.findByUsername(signUpRequestDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User user = new User();
        user.setUsername(signUpRequestDto.getUsername());
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        user.setRoles(Collections.singleton(RoleType.valueOf("USER")));
        User savedUser = userRepository.save(user);
        String token=authUtil.generateAccessToken(savedUser);
        return new SignupResponseDto(savedUser.getId(),savedUser.getUsername(),token);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );
        User user= (User) authentication.getPrincipal();
        String token=authUtil.generateAccessToken(user);
        return new LoginResponseDto(user.getId(), token);
    }
}

