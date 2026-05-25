package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.request.LoginRequest;
import com.headless.ecommerce.dto.request.RegisterRequest;
import com.headless.ecommerce.dto.request.UserUpdateRequest;
import com.headless.ecommerce.dto.response.JwtResponse;
import com.headless.ecommerce.dto.response.UserResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.mapper.UserMapper;
import com.headless.ecommerce.model.User;
import com.headless.ecommerce.model.enums.UserRole;
import com.headless.ecommerce.repository.UserRepository;
import com.headless.ecommerce.security.JwtTokenProvider;
import com.headless.ecommerce.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of UserService for authentication and user management.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already exists: " + request.getUsername());
        }

        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .nickname(request.getNickname() != null ? request.getNickname() : request.getUsername())
            .role(UserRole.BUYER)
            .build();

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(
            savedUser.getId(), savedUser.getUsername(), savedUser.getRole().name());

        return JwtResponse.of(token, savedUser.getId(),
            savedUser.getUsername(), savedUser.getRole().name());
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new BusinessException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid username or password");
        }

        String token = jwtTokenProvider.generateToken(
            user.getId(), user.getUsername(), user.getRole().name());

        return JwtResponse.of(token, user.getId(),
            user.getUsername(), user.getRole().name());
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        User user = findById(userId);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateCurrentUser(Long userId, UserUpdateRequest request) {
        User user = findById(userId);

        if (request.nickname() != null) {
            user.setNickname(request.nickname());
        }
        if (request.avatar() != null) {
            user.setAvatar(request.avatar());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone());
        }
        if (request.address() != null) {
            user.setAddress(request.address());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
