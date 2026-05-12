package com.example.MayaFisioLumiere.Controller;

import com.example.MayaFisioLumiere.Domain.Admin.AdminRequestDTO;
import com.example.MayaFisioLumiere.Domain.Admin.AdminResponseDTO;
import com.example.MayaFisioLumiere.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    // Cria novo admin
    @PostMapping("/register/admin")
    public ResponseEntity<AdminResponseDTO> register(@RequestBody AdminRequestDTO body) {
        AdminResponseDTO newAdmin = this.adminService.createAdmin(body);
        return ResponseEntity.ok(newAdmin);
    }

    // Login retorna jwt
    @PostMapping("/login/admin")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdminRequestDTO body) {
        String token = adminService.loginAdmin(body.adminEmail(), body.adminPassword());

        // Retorno em json pro frontend entender
        return ResponseEntity.ok(Map.of("token", token));
    }

    // Logout que invalida o token criado na sessão
    @PostMapping("/logout/admin")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token, String authorizationHeader) {
        adminService.logoutAdmin(token, authorizationHeader);
        return ResponseEntity.noContent().build();
    }
}
