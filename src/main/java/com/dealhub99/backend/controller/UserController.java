package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.User;
import com.dealhub99.backend.repository.UserRepository;
import com.dealhub99.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse<User>> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(BaseResponse.success("Profile fetched successfully", user));
    }

    @PostMapping("/profile")
    public ResponseEntity<BaseResponse<User>> updateProfile(@RequestBody User updateData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (updateData.getFullName() != null) user.setFullName(updateData.getFullName());
        if (updateData.getMobileNumber() != null) user.setMobileNumber(updateData.getMobileNumber());
        
        return ResponseEntity.ok(BaseResponse.success("Profile updated successfully", userRepository.save(user)));
    }
}
