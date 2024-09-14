//package com.authentication.module.services;
//
//import com.authentication.module.domain.PasswordResetToken;
//import com.authentication.module.domain.User;
//import com.authentication.module.exceptions.custom.InvalidTokenException;
//import com.authentication.module.exceptions.custom.UserNotVerifiedException;
//import com.authentication.module.repositories.PasswordResetTokenRepository;
//import com.authentication.module.repositories.UserRepository;
//import jakarta.mail.MessagingException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class LoginServiceTest {
//
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @Mock
//    private TokenService tokenService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordResetTokenRepository tokenRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private EmailService emailService;
//
//    @InjectMocks
//    private LoginService loginService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @Disabled
//    void testLogin_SuccessByLogin() {
//        User user = new User();
//        user.setLogin("testUser");
//        user.setPassword("encodedPassword");
//        user.setEmailVerified(true);
//
//        when(userRepository.findUserByLogin("testUser")).thenReturn(Optional.of(user));
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(user);
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//        when(tokenService.generateToken(any(User.class))).thenReturn("jwtToken");
//
//        String token = loginService.login("testUser", "password");
//
//        assertEquals("jwtToken", token);
//    }
//
//    @Test
//    @Disabled
//    void testLogin_SuccessByEmail() {
//        User user = new User();
//        user.setLogin("testUser");
//        user.setPassword("encodedPassword");
//        user.setEmailVerified(true);
//
//        when(userRepository.findUserByLogin("testUser")).thenReturn(Optional.empty());
//        when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(user));
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(user);
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenReturn(authentication);
//        when(tokenService.generateToken(any(User.class))).thenReturn("jwtToken");
//
//        String token = loginService.login("testUser", "password");
//
//        assertEquals("jwtToken", token);
//    }
//
//    @Test
//    void testLogin_UserNotFound() {
//        when(userRepository.findUserByLogin("testUser")).thenReturn(Optional.empty());
//        when(userRepository.findByEmail("testUser")).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class, () -> loginService.login("testUser", "password"));
//    }
//
//    @Test
//    void testLogin_EmailNotVerified() {
//        User user = new User();
//        user.setLogin("testUser");
//        user.setPassword("encodedPassword");
//        user.setEmailVerified(false);
//
//        when(userRepository.findUserByLogin("testUser")).thenReturn(Optional.of(user));
//
//        assertThrows(UserNotVerifiedException.class, () -> loginService.login("testUser", "password"));
//    }
//
//    @Test
//    void testGeneratePasswordResetToken_Success() throws MessagingException {
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
//
//        loginService.generatePasswordResetToken("test@example.com");
//
//        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
//        verify(emailService, times(1)).sendPasswordResetEmail(eq("test@example.com"), anyString());
//    }
//
//    @Test
//    void testGeneratePasswordResetToken_UserNotFound() {
//        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
//
//        assertThrows(UsernameNotFoundException.class, () -> loginService.generatePasswordResetToken("test@example.com"));
//    }
//
//    @Test
//    void testResetPassword_Success() {
//        User user = new User();
//        user.setPassword("oldPassword");
//
//        PasswordResetToken resetToken = new PasswordResetToken("validToken", user);
//
//        when(tokenRepository.findByToken("validToken")).thenReturn(Optional.of(resetToken));
//        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
//
//        loginService.resetPassword("validToken", "newPassword");
//
//        assertEquals("encodedNewPassword", user.getPassword());
//        verify(userRepository, times(1)).save(user);
//        verify(tokenRepository, times(1)).delete(resetToken);
//    }
//
//    @Test
//    void testResetPassword_InvalidToken() {
//        when(tokenRepository.findByToken("invalidToken")).thenReturn(Optional.empty());
//
//        assertThrows(InvalidTokenException.class, () -> loginService.resetPassword("invalidToken", "newPassword"));
//    }
//}