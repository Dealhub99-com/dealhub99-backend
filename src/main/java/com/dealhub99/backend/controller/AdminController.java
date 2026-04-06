package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/dashboard")
    public ResponseEntity<BaseResponse<String>> getAdminDashboard() {
        return ResponseEntity.ok(BaseResponse.success("Welcome to the Admin Dashboard!", "Admin Access Granted"));
    }
}
