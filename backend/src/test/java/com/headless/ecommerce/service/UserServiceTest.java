package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.LoginRequest;
import com.headless.ecommerce.dto.request.RegisterRequest;
import com.headless.ecommerce.dto.response.JwtResponse;
import com.headless.ecommerce.dto.response.UserResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.mapper.UserMapper;
import com.headless.ecommerce.model.User;
import com.headless.ecommerce.model.enums.UserRole;
import com.headless.ecommerce.repository.UserRepository;
import com.headless.ecommerce.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .username("testuser")
            .password("encoded_password")
            .nickname("Test User")
            .role(UserRole.BUYER)
            .build();
    }

    @Nested
    @DisplayName("Registration Tests")
    class RegistrationTests {

        @Test
        @DisplayName("Should register new user successfully")
        void register_success() {
            RegisterRequest request = RegisterRequest.builder()
                .username("newuser")
                .password("password123")
                .nickname("New User")
                .build();

            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(jwtTokenProvider.generateToken(anyLong(), anyString(), anyString()))
                .thenReturn("jwt-token");

            JwtResponse response = userService.register(request);

            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("jwt-token");
            assertThat(response.getTokenType()).isEqualTo("Bearer");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when username already exists")
        void register_duplicateUsername_throwsException() {
            RegisterRequest request = RegisterRequest.builder()
                .username("existinguser")
                .password("password123")
                .build();

            when(userRepository.existsByUsername("existinguser")).thenReturn(true);

            assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Username already exists");

            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should use username as nickname when nickname is null")
        void register_nullNickname_usesUsername() {
            RegisterRequest request = RegisterRequest.builder()
                .username("nonickname")
                .password("password123")
                .build();

            when(userRepository.existsByUsername("nonickname")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
            when(jwtTokenProvider.generateToken(anyLong(), anyString(), anyString()))
                .thenReturn("token");

            userService.register(request);

            verify(userRepository).save(argThat(user ->
                "nonickname".equals(user.getNickname())));
        }

        @Test
        @DisplayName("Should assign BUYER role by default")
        void register_defaultRole_isBuyer() {
            RegisterRequest request = RegisterRequest.builder()
                .username("roleuser")
                .password("password123")
                .build();

            when(userRepository.existsByUsername("roleuser")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });
            when(jwtTokenProvider.generateToken(anyLong(), anyString(), anyString()))
                .thenReturn("token");

            userService.register(request);

            verify(userRepository).save(argThat(user ->
                user.getRole() == UserRole.BUYER));
        }
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with correct credentials")
        void login_success() {
            LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);
            when(jwtTokenProvider.generateToken(1L, "testuser", "BUYER"))
                .thenReturn("jwt-token");

            JwtResponse response = userService.login(request);

            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("jwt-token");
            assertThat(response.getUsername()).isEqualTo("testuser");
            assertThat(response.getRole()).isEqualTo("BUYER");
        }

        @Test
        @DisplayName("Should throw exception when username not found")
        void login_userNotFound_throwsException() {
            LoginRequest request = LoginRequest.builder()
                .username("nonexistent")
                .password("password123")
                .build();

            when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid username or password");
        }

        @Test
        @DisplayName("Should throw exception when password is incorrect")
        void login_wrongPassword_throwsException() {
            LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
            when(passwordEncoder.matches("wrongpassword", "encoded_password")).thenReturn(false);

            assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid username or password");
        }
    }

    @Nested
    @DisplayName("User Query Tests")
    class UserQueryTests {

        @Test
        @DisplayName("Should find user by ID")
        void findById_success() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            User found = userService.findById(1L);

            assertThat(found).isNotNull();
            assertThat(found.getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user not found by ID")
        void findById_notFound_throwsException() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Should find user by username")
        void findByUsername_success() {
            when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

            Optional<User> found = userService.findByUsername("testuser");

            assertThat(found).isPresent();
            assertThat(found.get().getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("Should return empty when username not found")
        void findByUsername_notFound_returnsEmpty() {
            when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

            Optional<User> found = userService.findByUsername("nonexistent");

            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("Should get current user as UserResponse")
        void getCurrentUser_success() {
            UserResponse expectedResponse = UserResponse.builder()
                .id(1L)
                .username("testuser")
                .nickname("Test User")
                .role("BUYER")
                .build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userMapper.toResponse(testUser)).thenReturn(expectedResponse);

            UserResponse response = userService.getCurrentUser(1L);

            assertThat(response).isNotNull();
            assertThat(response.getUsername()).isEqualTo("testuser");
            assertThat(response.getRole()).isEqualTo("BUYER");
        }
    }

    @Nested
    @DisplayName("User Update Tests")
    class UserUpdateTests {

        @Test
        @DisplayName("Should update user profile fields")
        void updateCurrentUser_success() {
            UserResponse updateRequest = UserResponse.builder()
                .nickname("Updated Nick")
                .phone("1234567890")
                .address("New Address")
                .build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toResponse(any(User.class))).thenReturn(UserResponse.builder()
                .id(1L).username("testuser").nickname("Updated Nick").build());

            UserResponse response = userService.updateCurrentUser(1L, updateRequest);

            assertThat(response).isNotNull();
            verify(userRepository).save(argThat(user ->
                "Updated Nick".equals(user.getNickname()) &&
                "1234567890".equals(user.getPhone()) &&
                "New Address".equals(user.getAddress())));
        }

        @Test
        @DisplayName("Should not update null fields")
        void updateCurrentUser_nullFields_skipped() {
            UserResponse updateRequest = UserResponse.builder()
                .nickname("New Nick")
                .build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(userMapper.toResponse(any(User.class))).thenReturn(UserResponse.builder()
                .id(1L).username("testuser").nickname("New Nick").build());

            userService.updateCurrentUser(1L, updateRequest);

            verify(userRepository).save(argThat(user ->
                "New Nick".equals(user.getNickname()) &&
                user.getPhone() == null &&
                user.getAddress() == null));
        }
    }
}
