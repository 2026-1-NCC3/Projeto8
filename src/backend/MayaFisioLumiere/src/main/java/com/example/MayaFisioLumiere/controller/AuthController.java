package com.example.MayaFisioLumiere.controller;

import com.example.MayaFisioLumiere.Services.AdminService;
import com.example.MayaFisioLumiere.entity.AdminEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//LOGIN DE ADMINISTRADORES - FAZER AUTENTICAÇÃO, LOGIN, LOGOUT AQUI
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/admin/login")
    public AdminEntity login(@RequestBody AdminEntity admin){

        return adminService.loginAdmin(
                admin.getAdminEmail(),
                admin.getAdminPassword()
        );
    }

    //FAZER LOGOUT QUANDO TIVER JWT, HASH...
}
