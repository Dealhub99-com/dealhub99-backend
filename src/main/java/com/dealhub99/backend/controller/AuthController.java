package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.*;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<User>> registerUser(@Valid @RequestBody UserRegistrationRequest signUpRequest) {
        User user = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(BaseResponse.success("User registered successfully!", user));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> authenticateUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        AuthResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(BaseResponse.success("Login successful!", response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.createPasswordResetToken(request);
        return ResponseEntity.ok(BaseResponse.success("Password reset link sent to your email", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<BaseResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(BaseResponse.success("Password has been reset successfully", null));
    }
}
